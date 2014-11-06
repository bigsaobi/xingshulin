package com.example.weibotest.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weibotest.R;
import com.example.weibotest.utils.Utils;

/**
 * 转菊花dialog
 * 
 * @author Song
 * 
 */
public class BaseProgressDialog extends ProgressDialog {

	private Context mContext;
	private Animation animation;
	private ImageView image;
	private TextView msgText;
	private String msg;

	public BaseProgressDialog(Context context) {
		super(context);
		init(context);
	}

	public BaseProgressDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		LayoutParams lp = window.getAttributes();
		window.setGravity(Gravity.CENTER);
		lp.width = Utils.dip2px(mContext, 140);
		lp.height = Utils.dip2px(mContext, 120);

		lp.alpha = 1.0f;
		lp.dimAmount = 0.8f;

		window.setAttributes(lp);

		View v = LayoutInflater.from(mContext).inflate(
				com.example.weibotest.R.layout.base_progress_dialog, null);

		image = (ImageView) v.findViewById(R.id.progress_dialog_image);
		image.clearAnimation();
		animation = AnimationUtils.loadAnimation(mContext, R.anim.linearanim);
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);

		setContentView(v);

		msgText = (TextView) v.findViewById(R.id.progress_text);

		setCanceledOnTouchOutside(false);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		image.clearAnimation();
	}

	@Override
	public void cancel() {
		super.cancel();
		image.clearAnimation();
	}

	@Override
	public void show() {
		super.show();
		image.startAnimation(animation);
		if (msg == null) {
			msgText.setText(mContext.getString(R.string.loading_text));
		} else {
			msgText.setText(msg);
		}

	}

	public void setProText(String text) {
		msg = text;
	}
}
