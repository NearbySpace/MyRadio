package com.example.toolbar.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.toolbar.bean.Click_Ranking;
import com.example.toolbar.bean.Favorite;
import com.example.toolbar.bean.Program;
import com.example.toolbar.bean.SelectProgram;
import com.example.dolphinradio.R;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.fragment.ProgramFragment;
import com.example.toolbar.utils.DensityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
/**
 * 适配器
 * @author 
 *
 */
public class FavoriteAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mInflater;
	private Map<String, String> mDataMap;
	private List<Favorite> totalProgram;
	private List<Favorite> programLists;
	private List<Favorite> programs;
	private Favorite mFavorite;
	private ViewHolder viewHolder;
	private ImageLoader mImageLoader;

	public FavoriteAdapter(Context context, List<Favorite> programLists,List<Favorite> programs) {
		super();
		// TODO Auto-generated constructor stub
		
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.programLists = programLists;
		this.programs=programs;
//		LogHelper.e("获取节目数据：" + program.toString());
		mImageLoader = ImageLoader.getInstance();
//		mImageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	}
	
	private class ViewHolder{
		TextView title;
		TextView show_number,name;
		ImageView imageView;
		ImageView imageViewMore;
		
	}
	public void uploadMsg(List<Favorite> programLists,List<Favorite> programs) {
		this.programLists=programLists;
		this.programs=programs;
		notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount() {
		Log.i("FavoriteAdapter", "数据长度getCount----->"+programLists.size()+programs.size()+1);
		return programLists.size()+programs.size()+1;
	}

	@Override
	public Object getItem(int position) {
		if(position==programLists.size()){
			return null;
		}else if(0<=position&&position<programLists.size()){
			return programLists.get(position);
		}else if(position>programLists.size()){
			return programs.get(position-(programLists.size()+1));
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		
		if(position==programLists.size()){
			TextView tv=new TextView(mContext);
			int padding=DensityUtil.dip2px(mContext, 5);
			tv.setPadding(padding, padding, 0, padding);
			tv.setText("节目共："+programs.size()+"个");
			Log.i("FavoriteAdapter", "position位置:"+position+"----->TextView地址:"+tv);
			return tv;
		}else if(position<programLists.size()){
			mFavorite=programLists.get(position);
		}else if(position>programLists.size()){
			int newPosition=position-programLists.size()-1;
			mFavorite=programs.get(newPosition);
		}
		View view=mInflater.inflate(R.layout.item_addproger_select, null);
		
		// TODO Auto-generated method stub
		if (convertView !=null&& convertView instanceof LinearLayout) {
			
			viewHolder = (ViewHolder) convertView.getTag();
			
		}else {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_addproger_select, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.show_icon);
			viewHolder.title = (TextView) convertView.findViewById(R.id.show_titel);
			viewHolder.show_number = (TextView) convertView.findViewById(R.id.shows_number);
			viewHolder.imageViewMore=(ImageView) convertView.findViewById(R.id.more);
			convertView.setTag(viewHolder);
			
		}
		Log.i("FavoriteAdapter", "位置position:"+position+"----->mFavorite地址："+mFavorite+"----->viewHolder.title地址："+viewHolder.title);
		viewHolder.title.setText(mFavorite.getTitle());
		viewHolder.show_number.setText("主持人："+mFavorite.getOwner()+"    时长："
				+mFavorite.getPlaytimes());
		viewHolder.imageViewMore.setVisibility(View.GONE);
//		viewHolder.show_number.setText(program.get(position).getProgram_count());
//		LogHelper.e("获取节目标题："+program.get(position).getTitle());
//		if (dataMap.get("name").equals("0")) {
//			viewHolder.content.setVisibility(View.GONE);
//		}else if (dataMap.get("type").equals("1")) {
//			viewHolder.content.setVisibility(View.VISIBLE);
//			viewHolder.content.setText(dataMap.get("title"));
//		}
		mImageLoader.displayImage(mFavorite.getThumb(), viewHolder.imageView,
				ImageLoaderHelper.getDisplayImageOptions());
		return convertView;
	}

}
