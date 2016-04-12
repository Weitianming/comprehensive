package com.example.main.login.Mainfragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.login.R;
import com.example.view.ViewPagerAdapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainView4 extends Fragment {
	private ViewPager vp;
	private ViewPagerAdapter viewPagerAdapter;
	private List<View> views;
	private ScheduledExecutorService service;
	private TextView view1_show1, view1_show2, view1_show3, view1_show4;
	private ListView view_list;

	private static final int[] pics = { R.drawable.a3, R.drawable.a4,
			R.drawable.a5, R.drawable.a6, R.drawable.a7 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.main_view4, container, false);

		vp = (ViewPager) rootView.findViewById(R.id.vptow);
		view1_show1 = (TextView) rootView.findViewById(R.id.view1_show1);
		view1_show2 = (TextView) rootView.findViewById(R.id.view1_show2);
		view1_show3 = (TextView) rootView.findViewById(R.id.view1_show3);
		view1_show4 = (TextView) rootView.findViewById(R.id.view1_show4);
		view_list = (ListView) rootView.findViewById(R.id.view_list);

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		views = new ArrayList<View>();

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < pics.length; i++) {
			ImageView im = new ImageView(getActivity());
			im.setLayoutParams(params);
			im.setImageResource(pics[i]);
			views.add(im);
		}

		viewPagerAdapter = new ViewPagerAdapter(views);
		vp.setAdapter(viewPagerAdapter);

		service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(new ScrollTask(), 5, 5, TimeUnit.SECONDS);

		view_list.setAdapter(new MyAdapter());
	}

	@Override
	public void onStop() {
		super.onStop();
		service.shutdown();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater(null);
			convertView = inflater.inflate(R.layout.view1_list, null);

			return convertView;
		}

	}

	private int currentItem = 0;

	private class ScrollTask implements Runnable {

		@Override
		public void run() {
			currentItem = (currentItem + 1) % views.size();
			handler.obtainMessage().sendToTarget();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			vp.setCurrentItem(currentItem);
		}
	};

}
