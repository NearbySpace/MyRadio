package com.example.toolbar.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.activity.RadioPlayActivity;
import com.example.toolbar.bean.DownloadedBean;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.db.DBUtil;
import com.example.toolbar.db.SQLHelper;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DownloadedFragment extends Fragment {
	private View view;
	private List<File> mFiles;
	private List<DownloadedBean> mList;
	private ListView lv;
	private LayoutInflater mLayoutInflater;
	private ImageLoader mImageLoader;
	private MyAdapter adapter;
	private int deletePosition;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayoutInflater=LayoutInflater.from(getActivity());
		mList=new ArrayList<DownloadedBean>();
		mImageLoader = ImageLoader.getInstance();
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=mLayoutInflater.inflate(R.layout.fragment_downloaded, null);
		lv=(ListView) view.findViewById(R.id.downloaded_lv);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		adapter=new MyAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemLongClickListener(new MyOnItemLongClickListener());
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(getActivity(),RadioPlayActivity.class);
				Bundle bundle=new Bundle();
				DownloadedBean downloadedBean=(DownloadedBean) parent.getAdapter().getItem(position);
				bundle.putSerializable("downloadedBean", downloadedBean);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
			}
		});
	}
	
	private void initData() {
		String downloadPath=ConfigUtils.getDownloadPath(getActivity());
		File file=new File(downloadPath);
		Cursor cursor=DBUtil.getInstance(getActivity()).selectData(SQLHelper.TABLE_DOWNLOADED, 
				null, "", null, "", "", "");
		if(cursor!=null){
			while(cursor.moveToNext()){
				DownloadedBean info=new DownloadedBean();
				info.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
				info.setName(cursor.getString(cursor.getColumnIndex("name")));
				info.setProgramId(cursor.getString(cursor.getColumnIndex("program_id")));
				info.setStoragePath(cursor.getString(cursor.getColumnIndex("storage_path")));
				info.setThumb(cursor.getString(cursor.getColumnIndex("thumb")));
				mList.add(info);
			}
		}
	}
	
	
	class MyOnItemLongClickListener implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			deletePosition=position;
			Utils.showDelDialog(getActivity(), 
					"确定删除该文件？", new DialogOnClicListener());
			return true;
		}
	}
	
	class DialogOnClicListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			delLocalFile();
		}
	}
	
	private void delLocalFile() {
		DownloadedBean bean=mList.get(deletePosition);
		DBUtil.getInstance(getActivity()).deleteData(SQLHelper.TABLE_DOWNLOADED,
				"name = ?", new String[]{bean.getName()});
		mList.remove(bean);
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
		deleteFile(bean.getName());
	}
	
	private boolean deleteFile(String name) {
		File sd_file = new File(ConfigUtils.SDDownloadPath);
		File[] files;
		String fileName;
		Vector<String> vecFile = new Vector<String>();
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
		for (int i = 0; i < files.length; i++) {
			// 判断是否为文件夹
			if (!files[i].isDirectory()) {
				fileName = files[i].getName();
				
				if ((name+".mp3").equals(fileName)) {//name+".mp3"需要修改一下，格式可能多样
					files[i].delete();
					return true;
				} 
			}
		}
		return false;
	}
	
	class MyAdapter extends BaseAdapter{
		Holder holder;
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DownloadedBean info=mList.get(position);
			if(convertView==null){
				convertView=mLayoutInflater.inflate(R.layout.item_downloaded, null);
				holder=new Holder();
				holder.iv=(ImageView) convertView.findViewById(R.id.downloaded_iv);
				holder.tv_title=(TextView) convertView.findViewById(R.id.downloaded_title_item);
				holder.tv_size=(TextView) convertView.findViewById(R.id.downloaded_size_item);
				holder.tv_source=(TextView) convertView.findViewById(R.id.downloaded_source_item);
				holder.tv_time=(TextView) convertView.findViewById(R.id.downloaded_time_item);
				convertView.setTag(holder);
			}else{
				holder=(Holder) convertView.getTag();
			}
			mImageLoader.displayImage(info.getThumb(), holder.iv,
					ImageLoaderHelper.getDisplayImageOptions());
			holder.tv_title.setText(info.getName());
			holder.tv_size.setText("未知");
			holder.tv_source.setText(info.getAuthor());
			return convertView;
		}
		
	}
	
	class Holder {
		public ImageView iv;
		public TextView tv_title;
		public TextView tv_size;
		public TextView tv_source;
		public TextView tv_time;
	}

}
