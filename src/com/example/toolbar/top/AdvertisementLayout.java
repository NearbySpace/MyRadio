package com.example.toolbar.top;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.dolphinradio.R;
import com.example.toolbar.bean.Top;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AdvertisementLayout extends LinearLayout {


	// com.baomihua.xingzhizhul.weight.AdvertisementLayout
	public AdvertisementLayout(Context context) {
		super(context);
		mContext = context;
		initWeight();
	}

	public AdvertisementLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initWeight();

	}

	public AdvertisementLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initWeight();
	}

	private Context mContext;
	private double scale = 2.13;
	private ImageView _iv;
	private List<Top> top;
//	private List<String> listAd = new ArrayList<String>();

	private int defaultImage = R.drawable.mall_defalut;

	/**
	 * @param defaultImage
	 *            the defaultImage to set
	 */
	public void setDefaultImage(int defaultImage) {
		this.defaultImage = defaultImage;
		// viewLayout.setBackgroundDrawable(getContext().getResources().getDrawable(defaultImage));

	}

	/**
	 * 轮播广告 宽/高 的比例
	 * */
	public void setScale(double scale) {
		this.scale = scale;
		try {
			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) vp
					.getLayoutParams(); // 取控件mGrid当前的布局参数
			linearParams.height = (int) (((double) SettingUtil
					.getDisplaywidthPixels()) / scale);
			vp.setLayoutParams(linearParams);

			RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) viewLayout
					.getLayoutParams(); // 取控件mGrid当前的布局参数
			linearParams2.height = (int) (((double) SettingUtil
					.getDisplaywidthPixels()) / scale);
			viewLayout.setLayoutParams(linearParams2);

		} catch (Exception e) {
		}
	}

	public void setListAd(Context context, List<Top> top) {
		this.mContext = context;
		this.top = top;
		creatView();
	}

	private ChildViewPager vp;
	private ArrayList<View> listView = new ArrayList<View>();
	private LinearLayout IndicatorLayout;

	public LinearLayout.LayoutParams pageLineLayoutParams = null;
	View viewLayout = null;

	private void initWeight() {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.viewpager_advertisement_layout, null);
		viewLayout = view.findViewById(R.id.viewLayout);
		// viewLayout.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.default_guang_gao));
		//

		vp = (ChildViewPager) view.findViewById(R.id.vp);
		IndicatorLayout = (LinearLayout) view
				.findViewById(R.id.IndicatorLayout);

		pageLineLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				makesurePosition();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		creatView();

		touchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
		touchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();

		this.addView(view);

		try {
			RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) vp
					.getLayoutParams(); // 取控件mGrid当前的布局参数
			linearParams.height = (int) (((double) Util.getWindewScreenSize().widthPixels) / scale);
			vp.setLayoutParams(linearParams);

			RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) viewLayout
					.getLayoutParams(); // 取控件mGrid当前的布局参数
			linearParams2.height = (int) (((double) Util.getWindewScreenSize().widthPixels) / scale);
			viewLayout.setLayoutParams(linearParams2);

		} catch (Exception e) {

		}
	}

	int touchSlop = 0;

	@SuppressLint("ClickableViewAccessibility")
	public void creatView() {
		listView.clear();
		for (int i = 0; i < top.size(); i++) {
			View views = LayoutInflater.from(mContext).inflate(
					R.layout.viewpager_adverisement_layout_item, null);
			listView.add(views);
		}
		vp.setAdapter(new mypageAdapter());
		creatIndex();
		vp.setOnTouchListener(touchListener);
	}

	/**
	 * 创建导航点.
	 */
	public void creatIndex() {
		try {
			// 显示下面的点
			IndicatorLayout.removeAllViews();
			IndicatorLayout.setGravity(Gravity.CENTER);
			for (int j = 0; j < listView.size(); j++) {
				ImageView imageView = new ImageView(mContext);
				pageLineLayoutParams.setMargins(5, 5, 5, 5);
				imageView.setLayoutParams(pageLineLayoutParams);
				if (j == 0) {
					imageView
							.setImageResource(R.drawable.chat_indicator_selected);
				} else {
					imageView
							.setImageResource(R.drawable.chat_indicator_defaut);
				}
				IndicatorLayout.addView(imageView, j);
			}

		} catch (Exception e) {
		} catch (OutOfMemoryError e) {
		} catch (Error e) {
		}
	}

	int position = 0;

	/**
	 * 定位点的位置.
	 */
	public void makesurePosition() {
		
		position = vp.getCurrentItem();
		for (int j = 0; j < listView.size(); j++) {
			if (position == j) {
				((ImageView) IndicatorLayout.getChildAt(position))
						.setImageResource(R.drawable.chat_indicator_selected);
			} else {
				((ImageView) IndicatorLayout.getChildAt(j))
						.setImageResource(R.drawable.chat_indicator_defaut);
			}
		}

	}

	class mypageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return listView.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (View) arg1;
		}

		@Override
		public Object instantiateItem(View v, int position) {
			((ViewPager) v).addView(listView.get(position));
			_iv = (ImageView) listView.get(position).findViewById(
					R.id.ivBigPricture);
//			try {
				_iv.setImageResource(defaultImage);
			
//			ULog.d("轮播图片路径:" + listAd.get(position).getPic());
			ImageLoadingUtil.loadingImg(_iv, top.get(position).getThumb());
				
			
//			} catch (Exception e) {
//			}catch (OutOfMemoryError e) {
//				e.printStackTrace();
//				  android.os.Process.killProcess(android.os.Process.myPid()) ;   //获取PID 
//				  System.exit(0); 
//			}catch (Error e) {
//			}
			return listView.get(position);
		}

		@Override
		public void destroyItem(View v, int position, Object arg2) {
			((ViewPager) v).removeView(listView.get(position));
		}
	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				// 动态更新UI界面
				ImageLoadingUtil.loadingImg(_iv, top.get(position).getThumb(),
						ImageLoadingUtil.getImageOptions(defaultImage));
			}
		};
	};

	class myThread implements Runnable {
		private Handler handler;
		private int num = 0;

		public myThread(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {

			while (true) {
				try {

					Message msg = new Message();
					msg.what = 1;

					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// System.out.println("thread error...");
				}
			}
		}
	}

	private CallBackViewPagerOnclickListener viewPagerOnclickListener = null;

	public void setCallBack(CallBackViewPagerOnclickListener callBack) {
		viewPagerOnclickListener = callBack;
	}

	public interface CallBackViewPagerOnclickListener {
		public void callback(int position);
	}

	float downX = 0;
	private OnTouchListener touchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				downX = event.getX();
				stopPlay();
			} else if (MotionEvent.ACTION_UP == event.getAction()) {
				startPlay();
				float lastX = event.getX();
				float lastDistance = Math.abs(lastX - downX);
				// 当是触摸viewPager时，不是滑动（滑动不做任何操作）
				if (lastDistance < 10) {
					if (viewPagerOnclickListener != null)
						viewPagerOnclickListener.callback(position);
				}
			}
			return false;
		}
	};

	boolean play = false;

	/** 用与轮换的 handler. */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				int count = listView.size();
				int i = vp.getCurrentItem();
				if (count < 2)
					return;
				if (i + 1 == count) {
					i = 0;
				} else {
					i++;
				}
				try {
					
				vp.setCurrentItem(i, true);
				} catch (Exception e) {
				}
				if (play) {
					handler.postDelayed(runnable, 3000);
				}
			}
		}
	};
	/** 用于轮播的线程. */
	private Runnable runnable = new Runnable() {
		public void run() {
			if (vp != null) {
				handler.sendEmptyMessage(0);
			}
		}
	};

	/**
	 * 描述：自动轮播.
	 */
	public void startPlay() {
		if (handler != null) {
			play = true;
			handler.postDelayed(runnable, 3000);
		}
	}

	/**
	 * 描述：自动轮播.
	 */
	public void stopPlay() {
		if (handler != null) {
			play = false;
			handler.removeCallbacks(runnable);
		}
	}


}
