package com.example.weibotest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.weibotest.R;

public class EmptyView_ListView extends FrameLayout {
	public EmptyView_ListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public EmptyView_ListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	public EmptyView_ListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView();
	}

	LinearLayout dataloading;
	LinearLayout dataempty;
	boolean isLoading = false;
	TextView loadingtext;
	TextView emptyText;
	ProgressBar ProgressBar;

	private void initView() {
		View v = LayoutInflater.from(getContext()).inflate(
				R.layout.emptyviewlayout, null);
		ProgressBar = (ProgressBar) v.findViewById(R.id.progressBar11);
		dataloading = (LinearLayout) v
				.findViewById(R.id.emptyviewlayout_loadingdata);
		loadingtext = (TextView) dataloading
				.findViewById(R.id.emptyviewlayout_loadingtext);
		dataempty = (LinearLayout) v
				.findViewById(R.id.emptyviewlayout_dataempty);
		emptyText = (TextView) dataempty
				.findViewById(R.id.emptyviewlayout_text);
		if (isLoading) {
			dataloading.setVisibility(View.VISIBLE);
			dataempty.setVisibility(View.GONE);
		} else {
			dataloading.setVisibility(View.GONE);
			dataempty.setVisibility(View.VISIBLE);
		}
		addView(v);
	}

	public void setLoadingText(String loadingtextString) {
		loadingtext.setText(loadingtextString);
	}

	public void setEmptyText(String emptytextString) {
		emptyText.setText(emptytextString);
	}

	public void setLoadingState(boolean Loading) {
		if (Loading) {
			dataloading.setVisibility(View.VISIBLE);
			dataempty.setVisibility(View.GONE);
		} else {
			dataloading.setVisibility(View.GONE);
			dataempty.setVisibility(View.VISIBLE);
		}
	}

}
