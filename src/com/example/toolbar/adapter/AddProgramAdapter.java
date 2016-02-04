package com.example.toolbar.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.bean.AddProgram;
import com.example.toolbar.bean.AddProgram.AddProgramInfo;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.view.CrossView;
import com.example.toolbar.view.OpAnimationView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.view.View.OnClickListener;

public class AddProgramAdapter extends BaseAdapter {
	public Context context;
	private LayoutInflater inflater;
	private List<AddProgramInfo> list;
	private ArrayList<String> idList;
	private ViewHolder viewHolder;
	OnItemClickClass onItemClickClass;
	private ImageLoader mImageLoader;
	private boolean isRgihtShape = false;

	public AddProgramAdapter(Context context, List<AddProgramInfo> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		mImageLoader = ImageLoader.getInstance();
	}

	public AddProgramAdapter(Context context, List<AddProgramInfo> list,
			ArrayList<String> idList,OnItemClickClass onItemClickClass) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.onItemClickClass = onItemClickClass;
		this.list = list;
		this.idList=idList;
		mImageLoader = ImageLoader.getInstance();
	}

	static class ViewHolder {
		public ImageView thumb;
		public TextView content_tt;
		public TextView name_tt;
		public TextView time_tt;
		public CheckBox checkBox;
//		public OpAnimationView crossView;

	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ClickableViewAccessibility")
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_addproger, null);
			viewHolder = new ViewHolder();
			viewHolder.thumb = (ImageView) convertView
					.findViewById(R.id.show_icon);
			viewHolder.content_tt = (TextView) convertView
					.findViewById(R.id.show_title);
			viewHolder.name_tt = (TextView) convertView
					.findViewById(R.id.show_name);
			viewHolder.time_tt = (TextView) convertView
					.findViewById(R.id.show_time);
			viewHolder.checkBox = (CheckBox) convertView
					.findViewById(R.id.select_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.name_tt.setText("主持人：" + list.get(position).owner);
		viewHolder.content_tt.setText(list.get(position).title);
		// viewHolder.author_tt.setOnClickListener(new AuthorOnClick(maps
		// .get("truename")));
		viewHolder.time_tt.setText("时长 ："
				+ list.get(position).program_time);
		if(idList!=null&&0<idList.size()){
			for(int i=0;i<idList.size();i++){
				if(idList.get(i).equals(list.get(position).id)){
					viewHolder.checkBox.setChecked(true);
				}
			}
		}
		
		// 加载图片
		if (list.get(position).thumb == null) {
			viewHolder.thumb.setVisibility(View.GONE);
		} else {
			viewHolder.thumb.setVisibility(View.VISIBLE);
			String pathString = list.get(position).thumb;
			// bmpManager.loadBitmap(pathString,listItemView.thumb,defaultbitmap);
			mImageLoader.displayImage(pathString, viewHolder.thumb,
					ImageLoaderHelper.getDisplayImageOptions());
			viewHolder.thumb.setOnClickListener(new ImageOnClickListener(
					context, pathString));

		}
		convertView.setOnClickListener(new OnPhotoClick(position, viewHolder.checkBox));
		return convertView;
	}

	// 预览图片事件监听
	private class ImageOnClickListener implements View.OnClickListener {
		Context context;
		String path;

		public ImageOnClickListener(Context context, String path) {
			super();
			this.context = context;
			this.path = path;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Intent intent = new Intent(context, ImageViewPreview.class);
			// intent.putExtra("thumb", path);
			// context.startActivity(intent);
		}
	}

	public interface OnItemClickClass{
		public void OnItemClick(View v,int Position,CheckBox checkBox);
	}
	
	public class OnPhotoClick implements OnClickListener{
		int position;
		CheckBox checkBox;
		
		public OnPhotoClick(int position,CheckBox checkBox) {
			this.position=position;
			this.checkBox=checkBox;
		}
		@Override
		public void onClick(View v) {
			if (list!=null && onItemClickClass!=null ) {
				onItemClickClass.OnItemClick(v, position, checkBox);
			}
		}
	}

	
	
	public class AuthorOnClick implements View.OnClickListener {
		String nameString;

		public AuthorOnClick(String name) {
			// TODO Auto-generated constructor stub
			nameString = name;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// ContactsDialogUtils contactsDialogUtils = new
			// ContactsDialogUtils(context,nameString);
			// contactsDialogUtils.showContactsDialog();
		}
	}
}
