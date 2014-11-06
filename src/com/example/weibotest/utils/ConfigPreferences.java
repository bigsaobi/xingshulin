package com.example.weibotest.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.text.TextUtils;

/**
 * 保存常用数据
 * 
 * 
 */
public class ConfigPreferences {
	private static final String CONFIG_NAME = "free_config";
	private SharedPreferences spf;
	private Editor editor;
	private Context context;
	private static ConfigPreferences instance;
	List<OnSharedPreferenceChangeListener> listeners = new ArrayList<SharedPreferences.OnSharedPreferenceChangeListener>();

	public static ConfigPreferences getInstance(Context ctx) {
		if (instance == null) {
			instance = new ConfigPreferences(ctx);
		}
		return instance;
	}

	public void registerChangeListener(OnSharedPreferenceChangeListener listener) {
		if (spf != null) {
			spf.registerOnSharedPreferenceChangeListener(listener);
			listeners.add(listener);
		}
	}

	public void unRegisterChangeListener(
			OnSharedPreferenceChangeListener listener) {
		if (spf != null) {
			spf.unregisterOnSharedPreferenceChangeListener(listener);
			listeners.remove(listener);
		}
	}

	private ConfigPreferences(Context context) {
		this.context = context;
		spf = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
		editor = spf.edit();
	}

	public void setUserInfo(String account, String pwd, String nick, int gen) {
		if (!TextUtils.isEmpty(account)) {
			editor.putString("useraccount_key", account);
		}
		if (!TextUtils.isEmpty(pwd)) {
			editor.putString("userpwd_key", pwd);
		}
		if (!TextUtils.isEmpty(nick)) {
			editor.putString("usernick_key", nick);
		}
		if (gen != 0) {
			editor.putInt("usergeb_key", gen);
		}
		editor.commit();
	}

}
