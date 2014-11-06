package com.example.weibotest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.android.volley.QQImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.example.weibotest.utils.DateTimeUtils;
import com.example.weibotest.utils.QQLog;
import com.example.weibotest.view.EmptyView_ListView;
import com.example.weibotest.view.QTextView;
import com.example.weibotest.view.refresh.PullToRefreshListView;
import com.example.weibotest.view.refresh.PullToRefreshBase;

public class MainActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2, OnItemClickListener {
	PullToRefreshListView listView;
	List<WeiboMsg> list = new ArrayList<WeiboMsg>();
	LayoutInflater inflater;
	Handler _handler = new Handler();
	WeiboListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inflater = LayoutInflater.from(this);
		listView = (PullToRefreshListView) findViewById(R.id.activity_main_listview);
		listView.setOnRefreshListener(this);
		EmptyView_ListView emptyView = new EmptyView_ListView(this);
		emptyView.setId(android.R.id.empty);
		listView.setEmptyView(emptyView);
		listView.setOnItemClickListener(this);
		adapter = new WeiboListAdapter();
		listView.setAdapter(adapter);
		initData();

	}

	private void initData() {
		list.addAll(getData());
		adapter.notifyDataSetChanged();
	}

	private class WeiboListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewCache cache = new ViewCache();
			if (convertView == null) {
				cache = new ViewCache();
				convertView = inflater.inflate(R.layout.listitem_layout, null);
				cache.image = (ImageView) convertView
						.findViewById(R.id.listitem_layout_image);
				cache.name = (TextView) convertView
						.findViewById(R.id.listitem_layout_title);
				cache.content = (QTextView) convertView
						.findViewById(R.id.listitem_layout_msgcontent);
				cache.time = (TextView) convertView
						.findViewById(R.id.listitem_layout_time);
				convertView.setTag(cache);
			} else {
				cache = (ViewCache) convertView.getTag();
			}
			WeiboMsg msg = list.get(position);
			ImageListener imageListener = QQImageLoader.getImageListener(
					cache.image, R.drawable.ic_launcher,
					R.drawable.ic_launcher, 10);
			QQImageLoader.getInstance(
					MainActivity.this.getApplicationContext(), true)
					.disPlayImage(imageListener, msg.userimage);
			cache.name.setText(msg.username);
			cache.content.setText(msg.content);
			cache.content.setMovementMethod(QTextView.LocalLinkMovementMethod
					.getInstance());
			cache.time.setText(DateTimeUtils.getFormatTime(msg.createtime));
			return convertView;
		}

	}


	private static class ViewCache {
		ImageView image;
		TextView name;
		QTextView content;
		TextView time;
	}

	public List<WeiboMsg> getData() {
		List<WeiboMsg> newdata = new Select().from(WeiboMsg.class).limit(15)
				.orderBy("createtime DESC").execute();
		if (newdata == null || newdata.isEmpty()) {
			if (newdata == null) {
				newdata = new ArrayList<WeiboMsg>();
			}
			for (int i = 0; i < 100; i++) {
				WeiboMsg bean = new WeiboMsg(
						String.valueOf(i),
						"username_" + i,
						"http://qlogo3.store.qq.com/qzone/277085306/277085306/100?0",
						System.currentTimeMillis() - (10000000 * i),
						"这是微博正文,详情请查看http://www.baidu.com");
				bean.save();
				if (i < 15) {
					newdata.add(bean);
				}

			}
		}
		return newdata;
	}

	public List<WeiboMsg> getDataMore(WeiboMsg lastMsg) {
		return new Select().from(WeiboMsg.class)
				.where("createtime < " + lastMsg.createtime).limit(15)
				.orderBy("createtime DESC").execute();
	}

	public void print() {
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			str = str + list.get(i).toString() + "\n";
		}
		System.err.println(str);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub

		_handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				list.clear();
				list.addAll(getData());
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
			}
		}, 2000);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub
		_handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				list.addAll(getDataMore(list.get(list.size() - 1)));
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
			}
		}, 2000);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		QQLog.d(this, "点击了xxx" + arg2);
	}
}
