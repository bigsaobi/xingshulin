package com.example.weibotest.utils;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Utils {

	/**
	 * 是否为调试模式
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isDebugable(Context context) {
		return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE;
	}

	/**
	 * 网络是否可用
	 * 
	 * @param context
	 * @return boolean
	 * */
	public static boolean isNetAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return (info != null && info.isAvailable());
	}

	/**
	 * app是否在栈顶̨
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isAppOnForeground(Context context) {
		// Returns a list of application processes that are running on the
		// device
		List<RunningAppProcessInfo> appProcesses = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE))
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	public static void openKeyboard(final Context context, long delayTime) {

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, delayTime);
	}

	public static void openKeyboard(final EditText edit, final Context context) {

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(edit, InputMethodManager.SHOW_FORCED);
			}
		}, 200);
	}

	// 关闭软键盘
	public static void closeKeyboard(final Context context, long delayTime) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				// imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				if (imm != null && imm.isActive()) {
					View focusView = ((Activity) context).getWindow()
							.getCurrentFocus();
					if (focusView != null) {
						imm.hideSoftInputFromWindow(focusView.getWindowToken(),
								0);
					}
				}// isOpen若返回true，则表示输入法打开
			}
		}, delayTime);

	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
