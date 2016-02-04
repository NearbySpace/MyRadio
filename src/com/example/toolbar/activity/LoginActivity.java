package com.example.toolbar.activity;

import java.io.InputStream;
import java.util.Map;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.common.utils.NetUtil;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.ImageUtils;
import com.example.toolbar.utils.IntentUtils;
import com.example.toolbar.utils.OneKeyExit;
import com.example.toolbar.utils.SharePreferenceUtil;
import com.example.toolbar.utils.ToastUtils;
import com.example.toolbar.view.InputLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 登陆模块
 * 
 * @author jhz
 * 
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {
	private Toolbar mToolbar;
	// 用户名、密码
	private EditText userName, passWord;
	// 震动动画
	private Animation shake;
	// 监听键盘布局
	private InputLayout linearLayout;
	// 退出
	private OneKeyExit exit = new OneKeyExit();
	// 是否记住密码
	private CheckBox remember;
	private ImageView back;
	
	private boolean autoLogin = false;
	
	private TextView registerTV;
	// 登陆提示框
	private ProgressDialog loadingDialog = null;
	private SharePreferenceUtil sharePreferenceUtil;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (MyApplication.getInstance().getSpUtil().getIsAutoLogin()) {
//            autoLogin = true;
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }
		setContentView(R.layout.activity_login);
		initView();
		myOnClickLister();
		shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (autoLogin) {
            return;
        }
		sharePreferenceUtil = new SharePreferenceUtil(this,
				ConfigUtils.appSharePreferenceName);
		if (sharePreferenceUtil.getUserName() != null) {
			userName.setText(sharePreferenceUtil.getUserName());
		}

	}
	
	
	/* (non-Javadoc)
	 * @see android.support.v7.app.AppCompatActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("LoginActivity", "onDestroy");
	}

	
	/**
	 * 初始化控件
	 */
	public void initView() {
		// TODO Auto-generated method stub
		// super.initView();
//		mToolbar = (Toolbar) findViewById(R.id.toolbar);
//		mToolbar.setAlpha(0);
//		mToolbar.setTitle("登录");
//		setSupportActionBar(mToolbar);
//		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		mToolbar.setNavigationOnClickListener(new OnClickListener() {
//
//			public void onClick(View arg0) {
//				finish();
//			}
//		});
		
		back=(ImageView) findViewById(R.id.login_back);
		userName = (EditText) findViewById(R.id.login_activity_username);
		passWord = (EditText) findViewById(R.id.login_activity_password);
		linearLayout = (InputLayout) findViewById(R.id.login_activity_layout);
		remember = (CheckBox) findViewById(R.id.login_remember);
		registerTV = (TextView) findViewById(R.id.login_register);
		findViewById(R.id.login_resetpassword).setOnClickListener(this);
//		setLayoutBackground(R.drawable.login_bg);
		linearLayout
				.setKeyBoardListener(new InputLayout.OnKeyBoardShowListener() {

					@Override
					public void onShown() {
						// TODO Auto-generated method stub
//						findViewById(R.id.login_activity_layout_without)
//								.setVisibility(View.GONE);
					}

					@Override
					public void onHidden() {
						// TODO Auto-generated method stub
						findViewById(R.id.login_activity_layout_without)
								.setVisibility(View.VISIBLE);

					}
				});

		sharePreferenceUtil = new SharePreferenceUtil(this,
				ConfigUtils.appSharePreferenceName);
	}
	
	@SuppressLint("NewApi")
	private void setLayoutBackground(int resId){
		Bitmap bm=ImageUtils.getMatchBitmap(LoginActivity.this, resId);
		Drawable drawable=new BitmapDrawable(getResources(), bm);
		linearLayout.setBackground(drawable);
	}

	private void myOnClickLister() {
		registerTV.setOnClickListener((OnClickListener) this);
		findViewById(R.id.login_activity_submit).setOnClickListener(this);
		back.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		// super.onClick(v);
		switch (v.getId()) {
		case R.id.login_back:
			finish();
			break;
		case R.id.login_activity_submit:
			 postLogin();
			break;
		case R.id.login_register:
			IntentUtils.startActivity(this, RegistActivity.class);
			break;
		case R.id.login_resetpassword:
			// IntentUtils.startActivity(this, ResetPasswordActivity.class);
			break;

		default:
			break;
		}
	}

	// 提交登陆表单
	private void postLogin() {

		if (!NetUtil.isNetConnected(this)) {
			return;
		}
		if (userName.getText().length() == 0) {
			ToastUtils.showShort(this, "用户名不能为空！");
			userName.startAnimation(shake);
			return;
		}
		if (passWord.getText().length() == 0) {
			ToastUtils.showShort(this, "密码不能为空！");
			passWord.startAnimation(shake);
			return;
		}

		if (loadingDialog == null) {
			loadingDialog = new ProgressDialog(this);
			loadingDialog.setMessage(getResources().getString(
					R.string.login_message));
			loadingDialog.setCancelable(true);
			loadingDialog.setCanceledOnTouchOutside(false);
		}
		loadingDialog.show();
		String getUserName = userName.getText().toString();
		String getPassWord = passWord.getText().toString();
		HttpManage.getLogin(new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String result = new String(arg2);
				// Gson gson = new Gson();
				LogHelper.e("登录返回值：" + result);
				Map<String, String> data = Common.str2mapstr(result);
				if (data.get("status")!=null) {
					loadingDialog.dismiss();
					switch (Integer.parseInt( data.get("status") )) {
					case 8:
							//账号不存在
							ToastUtils.showShort(LoginActivity.this, data.get("msg")+",请注册");
							return;
					case 2:
						//密码错误
						ToastUtils.showShort(LoginActivity.this, data.get("msg")+"，请重新输入");
						return;
					case 3:
						//账号已被锁定，请联系管理员
						ToastUtils.showShort(LoginActivity.this, data.get("msg")+"，账号已被锁定，请联系管理员");
						return;
					case 5:
						//用户名和密码不能为空
						ToastUtils.showShort(LoginActivity.this, data.get("msg")+",请输入账号密码");
						return;

					default:
						break;
					}
				}
				
				

				if (remember.isChecked()) { // 记住登录 先更新数据
					sharePreferenceUtil.setSavePassword(true);
					sharePreferenceUtil.setIsAutoLogin(true);
				} else {
					sharePreferenceUtil.setSavePassword(false);
					sharePreferenceUtil.setIsAutoLogin(false);
				}
				
				sharePreferenceUtil.setUid(data.get("id"));
				Log.i("LoginActivity", data.get("id"));
				sharePreferenceUtil.setEmail(data.get("email"));
				sharePreferenceUtil.setNick(data.get("nickname"));
				sharePreferenceUtil.setHeadIcon(data.get("thumb"));
				sharePreferenceUtil.setSign(data.get("sign"));
				sharePreferenceUtil.setPhone(data.get("tel"));
				sharePreferenceUtil.setisLogin(true);
				//切换或注销账户时所传过来的参数，MainActivity已被关闭，需重启
				boolean again=getIntent().getBooleanExtra("again", false);
				if(again){
					IntentUtils.startActivity(LoginActivity.this,
					NewMainActivity.class);
					finish();
				}
				setResult(Activity.RESULT_OK);
				ToastUtils.showShort(LoginActivity.this, "已登录");
				finish();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				if (arg2 == null) {
					ToastUtils.showShort(LoginActivity.this,
							getString(R.string.error_internet));
					return;
				}
				if (arg2.equals("")) {
					ToastUtils.showShort(LoginActivity.this,
							getString(R.string.error_internet));
					return;
				}
				// TODO Auto-generated method stub

			}
		}, getUserName, getPassWord);

	}
	
}
