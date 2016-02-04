package com.example.toolbar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

	View mTopView;
	View mFlowView;
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		
		super.onScrollChanged(l, t, oldl, oldt);
		if(mTopView != null && mFlowView != null) {
			if(t >= mTopView.getHeight()) {
				mFlowView.setVisibility(View.VISIBLE);
			} else {
				mFlowView.setVisibility(View.GONE);
			}
		}
	}
	/**
	 * ��������view�Ĺ���״̬
	 * @param topView ��������view������ScrollView�����ĸ߶�Ҫ���ڵ����ĸ�view��ʱ������floatview
	 * @param flowView ����view����Ҫ�ĸ�viewͣ���ڶ���
	 */
	public void listenerFlowViewScrollState(View topView, View flowView) {
		mTopView = topView;
		mFlowView = flowView;
	}
	
}
