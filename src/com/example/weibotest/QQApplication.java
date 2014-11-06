package com.example.weibotest;

import com.activeandroid.ActiveAndroid;
import com.example.weibotest.utils.Utils;

import android.app.Application;

public class QQApplication extends Application {
	public static boolean isDebug = false;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ActiveAndroid.initialize(this);
		isDebug = Utils.isDebugable(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
	}
}
