package com.example.toolbar.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.strawberryradio.R;
import com.example.toolbar.activity.RadioPlayActivity;
import com.example.toolbar.adapter.FirstHotAdapter;
import com.example.toolbar.adapter.TopFlowViewAdapter;
import com.example.toolbar.adapter.TopGridViewAdapter;
import com.example.toolbar.bean.Click_Ranking;
import com.example.toolbar.bean.Hot;
import com.example.toolbar.bean.MainShows;
import com.example.toolbar.bean.Top;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.common.utils.MyJsonUtils;
import com.example.toolbar.common.utils.NetUtil;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.http.HttpUtils;
import com.example.toolbar.utils.ToastUtils;
import com.example.toolbar.view.MyListView;
import com.example.toolbar.view.progress.CircularProgress;
import com.example.toolbar.widget.flowview.CircleFlowIndicator;
import com.example.toolbar.widget.flowview.MyViewFlow;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class FirstFragment extends BaseFragment {
	private static int TOTAL_COUNT = 3;

	public static FirstFragment mFragment;
	private Context mContext;
	private View mView;
	private PullToRefreshScrollView mPullRefreshScrollView;// 列表控件
	private ScrollView mScrollView;
	private CircularProgress progress;
	private MyViewFlow mViewFlow;// 图片轮播--X 与下拉刷新有冲突
	private ViewPager viewPager;// 热播节目横向滑动-->第二版上
	private LinearLayout pointGroup;
	private GridView gridView;
	private MyListView listView;
	private int page = 0;
	private List<MainShows> data;
	private List<Map<String, String>> dataList;
	private boolean IS_WITHOOUT_REFRESH = false;
	private MainShows mMainShow;
	private TopGridViewAdapter mAdapter;
	private FirstHotAdapter hotAdapter;

	public static Fragment getInstance() {
		// if (mFragment == null) {
		// mFragment = new FirstFragment();
		// }
		mFragment = new FirstFragment();
		return mFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Auto-generated method stub
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_frist, null);
			mContext = getActivity();
			initView();
			initData();
		}
		return mView;

	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	private void initView() {
		progress = (CircularProgress) mView.findViewById(R.id.progress);
		mPullRefreshScrollView = (PullToRefreshScrollView) mView
				.findViewById(R.id.pull_refresh_scrollview);

		mViewFlow = (MyViewFlow) mView.findViewById(R.id.viewflow);

		CircleFlowIndicator indic = (CircleFlowIndicator) mView
				.findViewById(R.id.viewflowindic);
		mViewFlow.setFlowIndicator(indic);
		mViewFlow.setTimeSpan(4000);
		mViewFlow.setSelection(1 * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放

		gridView = (GridView) mView.findViewById(R.id.gridView1);
		listView = (MyListView) mView
				.findViewById(R.id.main_radio_frag_listview_top);

		

		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (mPullRefreshScrollView.getCurrentMode() == Mode.PULL_FROM_START) { // 往下拉刷新
					page = 0;
					initData();
				} else {
					page = page + 1;
					initData();
				}
			}
		});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
	}

	private void initData() {
		HttpManage.getMainData(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				mPullRefreshScrollView.onRefreshComplete();	
				progress.setVisibility(View.GONE);
				String result = new String(arg2);

				Gson gson = new Gson();
				mMainShow = gson.fromJson(result, MainShows.class);				
				mAdapter = new TopGridViewAdapter(mContext, mMainShow.getHot());
				gridView.setAdapter(mAdapter);

				List<Top> top = mMainShow.getTop();
				if (top != null && top.size() != 0) {
					mViewFlow.setAdapter(new TopFlowViewAdapter(mContext, top));
					mViewFlow.setmSideBuffer(top.size()); // 实际图片张数，
					mViewFlow.startAutoFlowTimer(); // 启动自动播放 //
					LogHelper.e("count:" + top.size());
				}
				
				hotAdapter = new FirstHotAdapter(mContext, mMainShow
						.getRanking());
				listView.setAdapter(hotAdapter);
							
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(getActivity(),
								RadioPlayActivity.class);
						Bundle bundle = new Bundle();
						bundle.putParcelableArrayList("music_list",
								(ArrayList) mMainShow.getHot());
						bundle.putInt("position", position);
						bundle.putString("type", "hot");
						intent.putExtra("bundle", bundle);
						startActivity(intent);
					}
				});

				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView, View view,
							int position, long arg3) {
						Intent intent = new Intent();
						LogHelper.e("节目id："
								+ ((Click_Ranking) adapterView.getAdapter().getItem(
										position)).getId());

						Bundle bundle = new Bundle();
						intent.setClass(getActivity(), RadioPlayActivity.class);
						bundle.putParcelableArrayList("music_list",
								(ArrayList) mMainShow.getRanking());
						bundle.putInt("position", position);
						intent.putExtra("bundle", bundle);
						bundle.putString("type", "ranking");
						startActivity(intent);

					}
				});

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});
		
	}

	/**
	 * 数据获取
	 */
	public void getListdata() {

		if (!NetUtil.isNetConnected(getActivity())) {
			return;
		}
		if (IS_WITHOOUT_REFRESH) {
			mCircleProgressBarLayout.setVisibility(View.VISIBLE);
		}
		new AsyncTask<Void, Void, List<Map<String, String>>>() {

			@Override
			protected void onPreExecute() {
				// Auto-generated method stub
				super.onPreExecute();
				// mPullRefreshListView.setRefreshing();
			}

			@Override
			protected List<Map<String, String>> doInBackground(Void... params) {
				// Auto-generated method stub
				String URL = HttpManage.address
						+ "?d=android&c=program&m=index";
				String result = HttpUtils.httpGet(URL);
				LogHelper.e("URL:" + URL + "数据：" + result);
				if (MyJsonUtils.isGetListSuccess(result)) {
					String data = MyJsonUtils.getArrayForJson("data", result);
					dataList = Common.strtolist(data);
				} else {
					dataList = new ArrayList<Map<String, String>>();
				}
				return dataList;
			}

			@Override
			protected void onPostExecute(List<Map<String, String>> result) {
				// Auto-generated method stub
				super.onPostExecute(result);
				IS_WITHOOUT_REFRESH = false;
				progress.setVisibility(View.GONE);
				if (result != null) {
					if (page == 0) {
						if (result.isEmpty()) {
							// withoutLayout.setVisibility(View.VISIBLE);
						} else {
							// mAdapter = new
							// MainShiPinListViewAdapter(mContext,
							// result);
							// mListView.setAdapter(mAdapter);
							// setBottomAdapter();
							// 显示版权信息
							// mView.findViewById(R.id.copyright_layout_ll).setVisibility(View.VISIBLE);
							// withoutLayout.setVisibility(View.GONE);
						}
					} else {
						if (result.isEmpty()) {
							ToastUtils.showShort(mContext, "信息已经加载完毕！");
						} else {
							// mAdapter.uploadMsg(result);
						}
					}

				} else {
					withoutLayout.setVisibility(View.VISIBLE);
					ToastUtils.showShort(mContext, "数据加载有误，请稍候尝试！");
				}

				// mPullRefreshListView.onRefreshComplete();
			}

		}.execute();

	}

	@Override
	public void onResume() {
		// Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// Auto-generated method stub
		// LogHelper.e("MainShouyeFragment_onDestroy");
		if (mView != null) {
			((ViewGroup) mView.getParent()).removeView(mView);
		}
		super.onDestroyView();
	}

}
