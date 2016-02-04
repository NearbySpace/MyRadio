package com.example.toolbar.adapter;

import com.example.toolbar.fragment.FindFragment;
import com.example.toolbar.fragment.FirstFragment;
import com.example.toolbar.fragment.ProgramFragment;
import com.example.toolbar.widget.SuperAwesomeCardFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
	private final String[] TITLES = { "首页", "播单", "发现", "排行" };

	public PagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public CharSequence getPageTitle(int position) {
		return TITLES[position];
	}

	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		 if (arg0 == 0) {
		 return FirstFragment.getInstance();
		 } else if (arg0 == 1) {
		 return ProgramFragment.getInstance();
		 // return CourseTableFragment.getInstance();
		 } else if (arg0 == 2) {
		 return FindFragment.getInstance();
		 // return SeatingFragment.getInstance();
		 } else {
		 return ProgramFragment.getInstance();
		 // return DutyFragment.getInstance();
		 }
//		return SuperAwesomeCardFragment.newInstance(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return TITLES.length;
	}

}
