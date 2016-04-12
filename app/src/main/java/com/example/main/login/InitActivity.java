package com.example.main.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.main.login.fragment.View1;
import com.example.main.login.fragment.View2;
import com.example.main.login.fragment.View3;

import java.util.ArrayList;
import java.util.List;

public class InitActivity extends FragmentActivity {
	private List<Fragment> fragments;
	private ViewPager viewPager;
	private Fragment view1, view2, view3;
	public static SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_activity);
		
		sp = getSharedPreferences("info", MODE_PRIVATE);
		
		if (sp.getBoolean("Automatic", false)) { // 自动登录
			startActivity(new Intent(InitActivity.this, MainActivity.class));
		} else if (sp.getBoolean("init", false)) { // 不可自动登录
			startActivity(new Intent(InitActivity.this, LoginActivity.class));
		} else { // 第一次打开软件
			initview();
		}
		
	}
	
	public void initview() {
		fragments = new ArrayList<Fragment>();
		viewPager = (ViewPager)findViewById(R.id.container);
		view1 = new View1();
		view2 = new View2();
		view3 = new View3();
		fragments.add(view1);
		fragments.add(view2);
		fragments.add(view3);
		
		viewPager.setAdapter(new PlaceholderFragment(getSupportFragmentManager()));
	}

	class PlaceholderFragment extends FragmentPagerAdapter {
		
		private List<Fragment> fragments;

		public PlaceholderFragment(FragmentManager fm) {
			super(fm);
			fragments = InitActivity.this.fragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
	
}
