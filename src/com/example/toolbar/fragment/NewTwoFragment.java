package com.example.toolbar.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dolphinradio.R;
import com.example.toolbar.activity.NewMainActivity;

public class NewTwoFragment extends Fragment implements OnClickListener{
	private View view;
	private TextView tv_find;
	private TextView tv_ranking;
	private ImageView iv_back;
	private List<Fragment> list;
	private FragmentManager fm;
	private int current=0;//当前位置
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tv_find.setOnClickListener(this);
		tv_ranking.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list=new ArrayList<Fragment>();
		list.add(new FindFragment());
		list.add(new ProgramFragment());
		fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.new_two_frameLayout, list.get(current));
		ft.commit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_new_tow, null);
		tv_find=(TextView) view.findViewById(R.id.new_two_find);
		tv_ranking=(TextView) view.findViewById(R.id.new_two_ranking);
		iv_back=(ImageView) view.findViewById(R.id.new_tow_back);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_two_find:
			if(current==0){
				return;
			}
			current=0;
			fm.beginTransaction().replace(R.id.new_two_frameLayout, list.get(current)).commit();
			tv_find.setBackgroundResource(R.drawable.corners_left_bg_red);
			tv_find.setTextColor(Color.WHITE);
			tv_ranking.setBackgroundResource(R.drawable.corners_right_bg_white);
			tv_ranking.setTextColor(Color.BLACK);
			break;
		case R.id.new_two_ranking:
			if(current==1){
				return;
			}
			current=1;
			fm.beginTransaction().replace(R.id.new_two_frameLayout, list.get(current)).commit();
			tv_find.setBackgroundResource(R.drawable.corners_left_bg_white);
			tv_find.setTextColor(Color.BLACK);
			tv_ranking.setBackgroundResource(R.drawable.corners_right_bg_red);
			tv_ranking.setTextColor(Color.WHITE);
			break;
		case R.id.new_tow_back:
			((NewMainActivity)getActivity()).switchFragment(0);
			break;

		default:
			break;
		}
		
	}
	
}
