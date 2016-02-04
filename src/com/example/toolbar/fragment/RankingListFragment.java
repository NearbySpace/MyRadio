package com.example.toolbar.fragment;

import com.example.strawberryradio.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RankingListFragment extends Fragment {
	private ListView lv;
	private String[] name={"私家车","好声音","970FM黄佳璐","交通状况FM","养身","听故事",
			"唱起来","泡花碱","法制节目","校园","爱笑话"};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=View.inflate(getActivity(), R.layout.fragment_home_ranking, null);
		lv=(ListView) view.findViewById(R.id.iv_fragment4);
		lv.setAdapter(new MyAdapter());
		return view;
	}
	
	class MyAdapter extends BaseAdapter{
		Holder holder;
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return name.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				holder=new Holder();
				convertView=View.inflate(getActivity(), R.layout.ranking_list_item, null);
				holder.name=(TextView) convertView.findViewById(R.id.title);
				holder.number=(TextView) convertView.findViewById(R.id.tv_item_list_find);
				convertView.setTag(holder);
			}else{
				holder=(Holder) convertView.getTag();
			}
			if(position<3){
				holder.name.setTextColor(Color.BLUE);
				holder.number.setTextColor(Color.BLUE);
			}else{
				holder.name.setTextColor(Color.BLACK);
				holder.number.setTextColor(Color.BLACK);
			}
			holder.name.setText(name[position]);
			holder.number.setText((position+1)+".");
			return convertView;
		}
		
	}
	
	class Holder{
		private TextView number;
		private TextView name;
	}

}
