package com.example.toolbar.fragment;


import com.example.toolbar.view.progress.CircularProgress;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class BaseFragment extends Fragment implements OnClickListener{
	public Context mContext;
	public View mView;
	public CircularProgress mCircleProgressBar;// 进度条
	public LinearLayout mCircleProgressBarLayout;//进度栏布局
	public LinearLayout withoutLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		mView = inflater.inflate(R.layout.flash_activity, null);
//		initView();
//		initData();
//		return mView;
//		
//	}
	
	
	
	private void initView() {
		// TODO Auto-generated method stub

	}
	
	private void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
