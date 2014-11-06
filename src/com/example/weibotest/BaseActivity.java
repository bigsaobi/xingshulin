package com.example.weibotest;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weibotest.R;
import com.example.weibotest.utils.ConfigPreferences;

public class BaseActivity extends FragmentActivity {
	ConfigPreferences configPre;
	protected ImageView titleBack;
	protected TextView titleText;
	private boolean isUseBaselayout = true;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		configPre = ConfigPreferences.getInstance(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		if (isUseBaselayout) {
			super.setContentView(R.layout.baseactivity_layout);
			FrameLayout contentLayout = (FrameLayout) findViewById(R.id.baseactivity_layout_content);
			LayoutInflater.from(this).inflate(layoutResID, contentLayout);
			titleBack = (ImageView) findViewById(R.id.titlebar_layout_back);
			titleBack.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			titleText = (TextView) findViewById(R.id.titlebar_layout_titletext);
		} else {
			super.setContentView(layoutResID);
		}

	}

	public void useBaseLayout(boolean usebaselayout) {
		this.isUseBaselayout = usebaselayout;
	}

	public void setTitleText(String titleString) {
		titleText.setText(titleString);
	}

	public void setTitleText(int titleResID) {
		titleText.setText(titleResID);
	}

	public void addNetReq_Get(String getUrl,
			Response.Listener<JSONObject> successlistener,
			Response.ErrorListener errorListener) {
		RequestQueue rq = Volley.newRequestQueue(this);
		JsonObjectRequest jsonRequest = new JsonObjectRequest(
				Request.Method.GET, getUrl, null, successlistener,
				errorListener);
		rq.add(jsonRequest);
	}

	public void addNetReq_Post(String postUrl, JSONObject obj,
			Response.Listener<JSONObject> successlistener,
			Response.ErrorListener errorListener) {
		RequestQueue rq = Volley.newRequestQueue(this);
		JsonObjectRequest jsonRequest = new JsonObjectRequest(
				Request.Method.POST, postUrl, obj, successlistener,
				errorListener);
		rq.add(jsonRequest);

	}

	public void showToast(String toastString) {
		Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
	}

	boolean isfinish = false;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isfinish = true;
		configPre = null;
		System.gc();

	}
}
