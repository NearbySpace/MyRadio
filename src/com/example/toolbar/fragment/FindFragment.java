package com.example.toolbar.fragment;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.activity.PrivateMainActivity;
import com.example.toolbar.activity.ProgramClassifyListActivity;
import com.example.toolbar.adapter.FindClassAdapter;
import com.example.toolbar.adapter.FindImgAdapter;
import com.example.toolbar.bean.FindBean;
import com.example.toolbar.bean.FindBean.IconTop;
import com.example.toolbar.bean.MainShows;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.utils.DensityUtil;
import com.example.toolbar.utils.UserUtils;
import com.example.toolbar.view.MyGridView;
import com.example.toolbar.view.MyListView;
import com.example.toolbar.view.progress.CircularProgress;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class FindFragment extends BaseFragment {
	// private Boolean boolean1= false;

	public static FindFragment mFragment;
	private Context mContext;
	private View mView;
	private MainShows shows;

	private SwipeRefreshLayout refresh;

	private PullToRefreshScrollView mPullRefreshScrollView;// 列表控件
	// private HorizontalListView mListView;//横向滑动的list
	private ScrollView mScrollView;
	private CircularProgress progress;

	private LinearLayout pointGroup;

	private MyGridView gridView_top;
	private MyGridView gridView_classify;
	private MyListView listView;
	private TextView tv_guess_find;
	private TextView tv_remind;
	// private FlowViewAdatpter flowAdapter;

	private int page = 0;
	private boolean IS_WITHOOUT_REFRESH = false;

	private FindBean bean;
	private FindImgAdapter mAdapter;
	private FindClassAdapter classAdapter;

	public static Fragment getInstance() {
		// if (mFragment == null) {
		// mFragment = new FirstFragment();
		// }
		mFragment = new FindFragment();
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
			mView = inflater.inflate(R.layout.fragment_find, null);
			mContext = getActivity();
			initView();
			initData();
		}
		return mView;

	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	private void initView() {
		progress = (CircularProgress) mView.findViewById(R.id.progress);

//		refresh = (SwipeRefreshLayout) mView.findViewById(R.id.find_pull_refresh_scrollview);
//		gridView_top = (MyGridView) mView.findViewById(R.id.mygv_recomment_find);
//		gridView_classify=(MyGridView) mView.findViewById(R.id.mygv_classify_find);
//		gridView_classify.setVerticalSpacing(DensityUtil.dip2px(getActivity(), 6));
//		tv_guess_find=(TextView) mView.findViewById(R.id.tv_guess_find);
		tv_remind=(TextView) mView.findViewById(R.id.find_remind);
		refresh = (SwipeRefreshLayout) mView.findViewById(R.id.find_pull_refresh_scrollview);
		tv_guess_find=(TextView) mView.findViewById(R.id.tv_guess_find);
		gridView_top = (MyGridView) mView.findViewById(R.id.mygv_recomment_find);
		gridView_top.setHorizontalSpacing(DensityUtil.dip2px(getActivity(), 18));
		gridView_top.setVerticalSpacing(DensityUtil.dip2px(getActivity(), 22));
		gridView_classify=(MyGridView) mView.findViewById(R.id.mygv_classify_find);
		gridView_classify.setVerticalSpacing(DensityUtil.dip2px(getActivity(), 6));
		gridView_classify.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(getActivity(),ProgramClassifyListActivity.class);
				intent.putExtra("type_id", bean.type_list.get(position).id);
				intent.putExtra("top_name", bean.type_list.get(position).title);
				startActivity(intent);
			}
		});
		
		gridView_top.setOnItemClickListener(clickListener);

		initRefresh();
	}

	private void initRefresh() {
		// refresh = (RefreshLayout) findViewById(R.id.refresh);
		// 刷新顶部有个貌似progress的东西，可以设置它的颜色，如下
		refresh.setColorSchemeResources(R.color.blue, R.color.red,
				R.color.green, R.color.cadetblue);
		// 为SwipeRefreshLayout绑定下拉监听事件\

		refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refresh.setRefreshing(true);
				// TODO Auto-generated method stub
				initData();
			}
		});
	}

	private void initData() {
		HttpManage.getFindData(new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				refresh.setRefreshing(false);
				String result = new String(arg2);
				Gson gson = new Gson();
				bean = gson.fromJson(result, FindBean.class);
				mAdapter = new FindImgAdapter(getActivity(), bean.radio_list);
				gridView_classify.setAdapter(new FindClassAdapter(
						getActivity(), bean.type_list));
				gridView_top.setAdapter(mAdapter);
				tv_remind.setVisibility(View.GONE);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				tv_remind.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_guess_find:

			break;

		default:
			break;
		}
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

	private OnItemClickListener clickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long itemid) {
			if(UserUtils.checkLogin(getActivity())){
				return;
			}
			IconTop top = (IconTop) mAdapter.getItem(position);
			Intent intent = new Intent(getActivity(),
					PrivateMainActivity.class);
			intent.putExtra("host_id", top.id);
			startActivity(intent);
		}
	};

}
