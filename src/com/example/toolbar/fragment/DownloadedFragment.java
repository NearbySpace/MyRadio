package com.example.toolbar.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.activity.RadioPlayActivity;
import com.example.toolbar.adapter.DownloadedFragmentAdapter;
import com.example.toolbar.bean.DownloadedBean;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.db.DBUtil;
import com.example.toolbar.db.SQLHelper;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.DialogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DownloadedFragment extends Fragment implements OnClickListener{
	private View view;
	private List<File> mFiles;
	private List<DownloadedBean> mList;
	private ListView lv;
	private LayoutInflater mLayoutInflater;
	private DownloadedFragmentAdapter adapter;
	private LinearLayout mLinearLayout;
	private TextView tv_sure;
	private TextView tv_cancel;
	
	private List<Integer> delList;
	private boolean isEditor = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayoutInflater = LayoutInflater.from(getActivity());
		mList = new ArrayList<DownloadedBean>();
		delList = new ArrayList<Integer>();
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = mLayoutInflater.inflate(R.layout.fragment_downloaded, null);
		initView();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		adapter = new DownloadedFragmentAdapter(getActivity(),mList);
		lv.setAdapter(adapter);
		lv.setOnItemLongClickListener(new MyOnItemLongClickListener());
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(!isEditor){
					Intent intent = new Intent(getActivity(),
							RadioPlayActivity.class);
					Bundle bundle = new Bundle();
					DownloadedBean downloadedBean = (DownloadedBean) parent
							.getAdapter().getItem(position);
					bundle.putSerializable("downloadedBean", downloadedBean);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}else{
					CheckBox cb = (CheckBox) view.findViewById(R.id.downloaded_checkbox_item);
					if(cb.isChecked()){
						cb.setChecked(false);
						mList.get(position).setChecked_state(false);
						if(delList.contains(position)){
							delList.remove(position);
						}
					}else{
						cb.setChecked(true);
						mList.get(position).setChecked_state(true);
						delList.add(position);
					}
				}
				
			}
		});
	}
	
	private void initView(){
		lv = (ListView) view.findViewById(R.id.downloaded_lv);
		mLinearLayout = (LinearLayout) view.findViewById(R.id.downloaded_ll_editor);
		tv_sure = (TextView) view.findViewById(R.id.downloaded_sure);
		tv_cancel = (TextView) view.findViewById(R.id.downloaded_cancel);
		tv_sure.setOnClickListener(this);
		tv_cancel.setOnClickListener(this);
	}

	private void initData() {
		String downloadPath = ConfigUtils.getDownloadPath(getActivity());
		// File file = new File(downloadPath);
		Cursor cursor = DBUtil.getInstance(getActivity()).selectData(
				SQLHelper.TABLE_DOWNLOADED, null, "", null, "", "", "");
		if (cursor != null) {
			while (cursor.moveToNext()) {
				DownloadedBean info = new DownloadedBean();
				info.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
				info.setName(cursor.getString(cursor.getColumnIndex("name")));
				info.setProgramId(cursor.getString(cursor
						.getColumnIndex("program_id")));
				info.setStoragePath(cursor.getString(cursor
						.getColumnIndex("storage_path")));
				info.setThumb(cursor.getString(cursor.getColumnIndex("thumb")));
				info.setChecked_state(false);
				mList.add(info);
			}
		}
	}
	
	//设置编辑栏的取消和确认按钮可见
	public void setLinearLayoutVisibility(int visibility){
		isEditor = true;
		mLinearLayout.setVisibility(visibility);
		mLinearLayout.post(new Runnable() {
			
			@Override
			public void run() {
				int marginButtomHeight = mLinearLayout.getHeight();
				lv.setPadding(0, 0, 0, marginButtomHeight);
				
			}
		});
		adapter.setEditorState(true);
	}

	class MyOnItemLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// deletePosition=position;
			DialogUtils.showDelDialog(getContext(), "确定删除该文件？",
					new DialogOnClicListener(position));
			return true;
		}
	}

	class DialogOnClicListener implements OnClickListener {
		private int deletePosition;

		public DialogOnClicListener(int position) {
			super();
			deletePosition = position;
		}

		@Override
		public void onClick(View v) {
			delLocalFile(new Integer[]{deletePosition});
		}
	}

	/**
	 * 删除文件
	 * @param deletePositions  将要被删除的文件在listview的位置的集和
	 */
	private void delLocalFile(Integer[] deletePositions) {
		int length = deletePositions.length;
		String[] name = new String[length];
		for(int i=0;i<length;i++){
			DownloadedBean bean = mList.get(deletePositions[i]);
			name[i] = bean.getName();
			DBUtil.getInstance(getActivity()).deleteData(
					SQLHelper.TABLE_DOWNLOADED, "name = ?",
					new String[]{bean.getName()});
			mList.remove(bean);
		}
		
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		deleteFile(name);
	}

	private File sd_file = new File(ConfigUtils.SDDownloadPath);
	private File[] files;
	private boolean deleteFile(String[] name) {
		String fileName;
		if (sd_file.exists()) {
			// 取得SD卡下的Download目录下的所有文件
			files = sd_file.listFiles();
		} else {
			sd_file.mkdirs();
			return false;
			// 取得ROM下的Download目录下的所有文件
			// files = rom_file.listFiles();
			// Log.i(TAG, "ROM卡下" + files);
		}
		// 历遍判断文件名是否相同
		if (files == null)
			return false;
		for(int k=0;k<name.length;k++){
			for (int i = 0; i < files.length; i++) {
				// 判断是否为文件夹
				if (!files[i].isDirectory()) {
					fileName = files[i].getName();

					if ((name[k] + ".mp3").equals(fileName)) {// name+".mp3"需要修改一下，格式可能多样
						files[i].delete();
					}
				}
			}
			
		}
		
		return true;
	}

	public void changeData() {
		mList.clear();
		initData();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.downloaded_cancel:
			mLinearLayout.setVisibility(View.GONE);
			mLinearLayout.setPadding(0, 0, 0, 0);
			isEditor = false;
			adapter.setEditorState(isEditor);
			break;
		case R.id.downloaded_sure:
			mLinearLayout.setVisibility(View.GONE);
			mLinearLayout.setPadding(0, 0, 0, 0);
			Integer[] deletePositions = delList.toArray(new Integer[delList.size()]);
			delLocalFile(deletePositions);
			isEditor = false;
			adapter.setEditorState(isEditor);
			break;


		default:
			break;
		}
		
	}

}
