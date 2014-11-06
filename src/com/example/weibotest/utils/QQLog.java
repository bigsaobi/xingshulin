package com.example.weibotest.utils;

import com.example.weibotest.QQApplication;

import android.content.Context;
import android.util.Log;

public class QQLog {
	private static final String TAG = "QQLog";

	public static void e(String log) {
		if (QQApplication.isDebug) {
			Log.e(TAG, log);
		}
	}

	public static void w(Context context, String log) {
		if (QQApplication.isDebug) {
			Log.w(TAG, log);
		}
	}

	public static void d(Context context, String log) {
		if (QQApplication.isDebug) {
			Log.d(TAG, log);
		}
	}

	public static void i(Context context, String log) {
		if (QQApplication.isDebug) {
			Log.i(TAG, log);
		}
	}

	public static void v(Context context, String log) {
		if (QQApplication.isDebug) {
			Log.v(TAG, log);
		}
	}
}
