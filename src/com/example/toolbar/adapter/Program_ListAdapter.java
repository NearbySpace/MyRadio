package com.example.toolbar.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.view.CrossView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Program_ListAdapter extends BaseAdapter {
	public Context context;
	private LayoutInflater inflater;
	private Map<String, String> maps;
	private List<Map<String, String>> listdata;
	private ViewHolder viewHolder;
	private ImageLoader mImageLoader;
	

	public Program_ListAdapter(Context context, List<Map<String, String>> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.listdata = list;
		maps = new HashMap<String, String>();
		mImageLoader = ImageLoader.getInstance();
	}

	static class ViewHolder {
		public ImageView thumb;
		public TextView content_tt;
		public TextView name_tt;
		public TextView time_tt;
		CrossView crossView ;

	}

	public void uploadMsg(List<Map<String, String>> data) {
		listdata.addAll(data);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listdata.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_show_list,
					null);
			viewHolder = new ViewHolder();
			viewHolder.content_tt = (TextView) convertView
					.findViewById(R.id.show_title);
			viewHolder.name_tt = (TextView) convertView
					.findViewById(R.id.compere);
			viewHolder.time_tt = (TextView) convertView
					.findViewById(R.id.show_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		maps = listdata.get(position);
		// LogHelper.e(maps.toString());

		viewHolder.name_tt.setText(maps.get("typename"));
		viewHolder.content_tt.setText(maps.get("title"));
		viewHolder.time_tt.setText("主持人：" + maps.get("name"));
//		viewHolder.author_tt.setOnClickListener(new AuthorOnClick(maps
//				.get("truename")));
		viewHolder.time_tt.setText("时间 ：" + maps.get("addtime"));
		viewHolder.crossView.setOnTouchListener(new OnTouchListener() {
			
			 public boolean onTouch(View v, MotionEvent event) {               
		            if(event.getAction() == MotionEvent.ACTION_DOWN){  
		            	
		            }else if(event.getAction() == MotionEvent.ACTION_UP){  
		            	
		            }  
		            return false;       
		    }       
		});
		// 加载图片
		if (maps.get("thumb").length() < 8) {
			viewHolder.thumb.setVisibility(View.GONE);
		} else {
			viewHolder.thumb.setVisibility(View.VISIBLE);
			String pathString = maps.get("thumb");
			// bmpManager.loadBitmap(pathString,listItemView.thumb,defaultbitmap);
			mImageLoader.displayImage(pathString, viewHolder.thumb,
					ImageLoaderHelper.getDisplayImageOptions());
			viewHolder.thumb.setOnClickListener(new ImageOnClickListener(
					context, pathString));

		}
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
//			Intent intent = new Intent(context, ImageViewPreview.class);
//			intent.putExtra("thumb", path);
//			context.startActivity(intent);
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
	};
}
