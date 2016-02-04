//package com.example.toolbar.adapter;
//
//import java.util.ArrayList;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//
//public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
//	private ArrayList<Fragment> fragmentList;
//
//	public MyFragmentPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments) {
//		// TODO Auto-generated constructor stub
//		super(fragmentManager);
//		this.fragmentList = fragments;
//
//	}
//
//	// ViewPage中显示的内容
//	@Override
//	public Fragment getItem(int arg0) {
//		// TODO Auto-generated method stub
//		return fragmentList.get(arg0 % fragmentList.size());
//
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return fragmentList == null ? 0 : fragmentList.size();
//	}
//}