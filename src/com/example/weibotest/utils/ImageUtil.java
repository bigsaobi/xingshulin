package com.example.weibotest.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class ImageUtil {

	/**
	 * 用于修复图片颠倒的问题
	 * */
	public static boolean fixImage(String inputImageFile,
			String outputImageFile, boolean recycleInput) {
		int degrees = 0;
		try {
			ExifInterface exif = new ExifInterface(inputImageFile);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degrees = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degrees = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degrees = 270;
				break;
			default:
				degrees = 0;
				break;
			}
		} catch (Exception e) {
		}
		BitmapFactory.Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(inputImageFile, options);
		if (options.outWidth > 0 && options.outHeight > 0) {
			options.inSampleSize = computeSampleSize(options, UNCONSTRAINED,
					1920 * 1080);
			options.inJustDecodeBounds = false;
			if (degrees == 0 && options.inSampleSize <= 1) {
				return false;
			}
			Bitmap bitmapFix = BitmapFactory
					.decodeFile(inputImageFile, options);
			FileOutputStream os = null;
			try {
				if (degrees > 0) {
					bitmapFix = rotate(bitmapFix, degrees);
				}
				os = new FileOutputStream(outputImageFile);
				bitmapFix.compress(CompressFormat.JPEG, 85, os);
				File fileInput = new File(inputImageFile);
				if (recycleInput && fileInput.exists()) {
					fileInput.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != bitmapFix)
					bitmapFix.recycle();
				closeSilently(os);
			}
			bitmapFix = null;
		} else {
			return false;
		}
		return true;

	}

	private static void closeSilently(Closeable c) {
		if (c == null)
			return;
		try {
			c.close();
		} catch (Throwable t) {
			// do nothing
		}
	}

	// Rotates the bitmap by the specified degree.
	// If a new bitmap is created, the original bitmap is recycled.
	private static Bitmap rotate(Bitmap b, int degrees) {
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) b.getWidth() / 2,
					(float) b.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
						b.getHeight(), m, true);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				// We have no memory to rotate. Return the original bitmap.
				ex.printStackTrace();
			}
		}
		return b;
	}

	private static final int UNCONSTRAINED = -1;

	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
				.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
				.min(Math.floor(w / minSideLength),
						Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == UNCONSTRAINED)
				&& (minSideLength == UNCONSTRAINED)) {
			return 1;
		} else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 读取一个要求宽高的本地图片
	 * */
	public static synchronized Bitmap decodeSampledBitmapFromFile(
			String filename, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'container sample
			// down
			// further.
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

}
