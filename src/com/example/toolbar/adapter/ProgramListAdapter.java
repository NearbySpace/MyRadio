package com.example.toolbar.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.activity.ProgramListActivity;
import com.example.toolbar.bean.ProgramListBean.ProgramListInfo;
import com.example.toolbar.utils.MediaUtil;
import com.example.toolbar.view.MyImageView;

public class ProgramListAdapter extends BaseAdapter {
	// private ProgramListBean bean;
	private final String TAG="ProgramListAdapter";
	private Context context;
	private Holder holder;
	private ProgramListInfo info;
	private ArrayList<ProgramListInfo> mProgramListInfos;
	private boolean isEditor = false;
	private boolean isSure = false;

	public ProgramListAdapter(Context context,
			ArrayList<ProgramListInfo> programListInfos) {
		super();
		mProgramListInfos = programListInfos;
		this.context = context;
	}

	public void setIsEditor(boolean isEditor) {
		this.isEditor = isEditor;
	}

	public void setIsSure(boolean isSure) {
		this.isSure = isSure;
	}
	
	public void setDate(ArrayList<ProgramListInfo> ProgramListInfos){
		mProgramListInfos = ProgramListInfos;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// Log.i("ProgramListAdapter", "bean.list的长度：" + bean.list.size());
		return mProgramListInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mProgramListInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	

	// 实现移除操作
	public void remove(int position) {
		mProgramListInfos.remove(position);
		this.notifyDataSetChanged();
	}

	// 实现插入操作
	public void inster(ProgramListInfo info, int position) {
		mProgramListInfos.add(position, info);
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_program_list_item, null);
			holder = new Holder();
			holder.iv = (MyImageView) convertView
					.findViewById(R.id.program_list_iv_item);
			holder.et_set_time = (EditText) convertView
					.findViewById(R.id.program_list_set_time_et);
			holder.tv_set_time = (TextView) convertView
					.findViewById(R.id.program_list_set_time_tv);
			holder.tv_set_unit = (TextView) convertView
					.findViewById(R.id.program_list_set_time_unit);
			holder.rl_layout = (RelativeLayout) convertView
					.findViewById(R.id.item_program_layout_ll);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.program_list_title_item);
			holder.tv_host_name = (TextView) convertView
					.findViewById(R.id.program_list_name_item);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.program_list_time_time);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.program_list_checkbox);
			holder.tv_delete = (TextView) convertView
					.findViewById(R.id.program_list_delete);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		if ((position % 2) == 0) {
			holder.rl_layout.setBackgroundColor(context.getResources()
					.getColor(R.color.white));
		} else {
			holder.rl_layout.setBackgroundColor(context.getResources()
					.getColor(R.color.color_gray_light3));

		}
		
		if (isEditor) {
			holder.et_set_time.setVisibility(View.VISIBLE);
			holder.tv_set_unit.setVisibility(View.VISIBLE);
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.tv_delete.setVisibility(View.VISIBLE);
			holder.tv_set_unit.setVisibility(View.VISIBLE);
			holder.tv_set_time.setVisibility(View.GONE);
			holder.iv.setBackgroundColor(0xFF01AED9);
		} else {
			holder.et_set_time.setVisibility(View.GONE);
			holder.checkBox.setVisibility(View.GONE);
			holder.tv_set_unit.setVisibility(View.GONE);
			holder.tv_delete.setVisibility(View.GONE);
			holder.tv_set_unit.setVisibility(View.GONE);
			holder.tv_set_time.setVisibility(View.VISIBLE);
			holder.iv.setBackgroundColor(0x00000000);
		}
		info = mProgramListInfos.get(position);
		holder.tv_title.setText(info.title + info.addtime);
		holder.tv_host_name.setText(info.nickname);
		holder.tv_time.setText(info.program_time);
		if(!info.timespan.isEmpty()){
			float timespan = Float.valueOf(info.timespan);
			holder.tv_set_time.setText(MediaUtil
					.formatTime((long) (timespan * 60 * 1000)));
		}
		
		
		if (isSure) {
			String str = holder.et_set_time.getText().toString().trim();

			if (!str.isEmpty()) {
				float time = Float.valueOf(str);
				info.timespan=str;
				Log.i(TAG, "设置的时间str---->"+str);
				holder.tv_set_time.setText(MediaUtil
						.formatTime((long) (time * 60 * 1000)));
			}
		}

		holder.checkBox
				.setOnCheckedChangeListener(new MyOnCheckedChangeListener(info.program_id));
		return convertView;
	}

	class Holder {
		private MyImageView iv;
		private EditText et_set_time;
		private TextView tv_set_time;
		private TextView tv_set_unit;
		private TextView tv_title;
		private TextView tv_host_name;
		private TextView tv_time;
		private RelativeLayout rl_layout;
		private CheckBox checkBox;
		private TextView tv_delete;
	}

	class MyOnCheckedChangeListener implements OnCheckedChangeListener {
		private String programId;

		public MyOnCheckedChangeListener(String id) {
			super();
			programId = id;

		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				if (context instanceof ProgramListActivity) {
					((ProgramListActivity) context)
							.addDeleteProgramId(programId);
					Log.i("ProgramListAdapter", "programId:" + programId);
				}
			} else {
				if (context instanceof ProgramListActivity) {
					((ProgramListActivity) context).removeProgramId(programId);
				}
			}

		}

	}

}
