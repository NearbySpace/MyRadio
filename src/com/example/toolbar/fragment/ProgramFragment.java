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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.dolphinradio.R;
import com.example.toolbar.activity.ProgramListActivity;
import com.example.toolbar.adapter.ProgerAdapter_Cg;
import com.example.toolbar.bean.Program;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.common.utils.MyJsonUtils;
import com.example.toolbar.common.utils.NetUtil;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.http.HttpManage.OnCallBack;
import com.example.toolbar.http.HttpUtils;
import com.example.toolbar.utils.ToastUtils;
import com.example.toolbar.view.progress.CircularProgress;
import com.example.toolbar.widget.flowview.MyViewFlow;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.loopj.android.http.TextHttpResponseHandler;

public class ProgramFragment extends BaseFragment {
	private final String TAG="ProgramFragment";
	public static ProgramFragment mFragment;
	private Context mContext;
	private View mView;
	private PullToRefreshGridView mPullToRefreshGridView;// 列表控件
	private GridView mGridView;
	private CircularProgress progress;
	private MyViewFlow mViewFlow;// 顶部新闻切换栏
	private List<Program> program;
	private ProgerAdapter_Cg mAdapter;

	private int page = 0;
	private List<Map<String, String>> dataList;
	List<Map<String, String>> data;
	private boolean IS_WITHOOUT_REFRESH = false;

	public static Fragment getInstance() {
		// if (mFragment == null) {
		// mFragment = new ProgramFragment();
		// }
		mFragment = new ProgramFragment();
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
			mView = inflater.inflate(R.layout.fragment_program, null);
			mContext = getActivity();
			initView();
			initData();
			mGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent=new Intent(mContext,ProgramListActivity.class);
					intent.putExtra("programme_id", data.get(position).get("id"));
					startActivity(intent);
				}
			});
		}
		return mView;

	}

	private void initView() {
		progress = (CircularProgress) mView.findViewById(R.id.progress);
		mPullToRefreshGridView = (PullToRefreshGridView) mView
				.findViewById(R.id.gridView1);
		mGridView = mPullToRefreshGridView.getRefreshableView();
		mPullToRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						page = +1;
						initData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						page = 0;
						initData();
					}

				});

	}

	private void initData() {
		String url = HttpManage.address + "?d=android&c=program&m=play_lists";
		HttpManage.getNetData(url, null, 1, new OnCallBack() {
			
			@Override
			public void onSuccess(byte[] arg2) {
				progress.setVisibility(View.GONE);
				String result = new String(arg2);
				jsonData(result);
//				Log.i(TAG, arg2);
				mPullToRefreshGridView.onRefreshComplete();
			}
			
			@Override
			public void onFailure(byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	private void jsonData(String json) {
		try {
			data = new ArrayList<Map<String, String>>();
			data = Common.strtolist(json);
			// LogHelper.e("sada" + data.get(0));

			mAdapter = new ProgerAdapter_Cg(mContext, data);
			mGridView.setAdapter(mAdapter);
			
			// List<Program> program = new ArrayList<Program>();
			// Gson gson = new Gson();
			// Type type = new TypeToken<List<Program>>() {
			// }.getType();
			// program = gson.fromJson(json, type);
			//
		} catch (Exception e) {
			// mAdapter.setDutyList(null);
			// ToastUtils.showShort(context, "获取数据出错");
			e.printStackTrace();
			LogHelper.e("获取数据出错");
		}
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
				// mPullRefreshScrollView.onRefreshComplete();
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
