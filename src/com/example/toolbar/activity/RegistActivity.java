package com.example.toolbar.activity;

import java.util.Map;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.bean.RegistBean;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.common.utils.MyHttpUtils;
import com.example.toolbar.common.utils.NetUtil;
import com.example.toolbar.common.utils.StringUtils;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.IntentUtils;
import com.example.toolbar.utils.ToastUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 注册模块
 * 
 * @author hxc
 * 
 */
public class RegistActivity extends AppCompatActivity implements
		OnClickListener {
	// 密码，确认密码，昵称，电话输入框
	private EditText usernameTv, password, password2, nickname, tel, idCard;
	private String url;
	// 注册失败时的提示tips
	private TextView tips;
	// 提交按钮
	private Button submit;
	// 进度栏对话框
	private ProgressDialog dialog;
	private EditText emailText;
	private Toolbar mToolbar;
	
	private RegistBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		initHeadActionBar();

	}

	// 初始化视图
	public void initHeadActionBar() {
		// super.initHeadActionBar();
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("注册");
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationContentDescription(R.drawable.login_back);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
		tips = (TextView) findViewById(R.id.tips);
		usernameTv = (EditText) findViewById(R.id.username);
		emailText = (EditText) findViewById(R.id.emailText);
		password = (EditText) findViewById(R.id.password);
		password2 = (EditText) findViewById(R.id.password2);
		nickname = (EditText) findViewById(R.id.nickname);
		idCard = (EditText) findViewById(R.id.id_card);
		tel = (EditText) findViewById(R.id.tel);
		dialog = new ProgressDialog(this);
	}

	// 点击事件 统一管理
	public void onClick(View v) {
		// super.onClick(v);
		switch (v.getId()) {
		case R.id.submit:
			dialog.show();
			post();

			break;
		}
	};

	// 提交表单
	public void post() {
		// if (username.getText().length() < 2) {
		// ToastUtils.showShort(RegistActivity.this,"用户名不能少于两个字");
		// tips.setText("用户名不能少于两个字");
		// dialog.dismiss();
		// return;
		// }
		if (!NetUtil.isNetConnected(this)) {
			return;
		}
		
		if (nickname.getText().length() < 1) {
			ToastUtils.showShort(RegistActivity.this, "昵称不能为空！");
			tips.setText("昵称不能为空！");
			dialog.dismiss();
			return;
		}
		
		if(tel.getText().toString().trim().length()!=0&&tel.getText().toString().trim().length()<11){
			ToastUtils.showShort(RegistActivity.this, "电话号码不正确");
			tips.setText("电话号码不正确");
			dialog.dismiss();
			return;
		}else if("".equals(tel.getText().toString().trim())){
			ToastUtils.showShort(RegistActivity.this, "电话号码不能为空！");
			tips.setText("电话号码不能为空！");
			dialog.dismiss();
			return;
		}

		if ("".equals(emailText.getText().toString())) {
			ToastUtils.showShort(RegistActivity.this, "邮箱地址不能为空！");
			tips.setText("邮箱地址不能为空！");
			dialog.dismiss();
			return;
		}
		
		if (!StringUtils.isEmail(emailText.getText().toString())) {
			ToastUtils.showShort(RegistActivity.this, "邮箱地址不符合规范，请填写正确地址！");
			tips.setText("邮箱地址不符合规范！");
			dialog.dismiss();
			return;
		}

		if (password.getText().length() == 0) {
			ToastUtils.showShort(RegistActivity.this, "密码不能为空！");
			tips.setText("密码不能为空！");
			dialog.dismiss();
			return;
		}
		if (!password.getText().toString()
				.equals(password2.getText().toString())) {
			ToastUtils.showShort(RegistActivity.this, "两次输入密码必需相同！");
			tips.setText("两次输入密码必需相同！");
			dialog.dismiss();
			return;
		}

		
		tips.setText("请稍等，提交中...");
		submit.setEnabled(false);
		HttpManage.getRegistResult(usernameTv.getText().toString().trim(),
				password.getText().toString().trim(), 
				nickname.getText().toString().trim(),
				tel.getText().toString().trim(), 
				emailText.getText().toString().trim(), 
				idCard.getText().toString().trim(), 
				new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String result=new String(arg2);
						submit.setEnabled(true);
						LogHelper.e(result);
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						if (result == null) {
							return;
						}
						Map<String, String> data = Common.str2mapstr(result);
						switch (Integer.parseInt(data.get("status"))) {
						case 0:
							Gson gson = new Gson();
							bean=gson.fromJson(result, RegistBean.class);
							break;
						case 1:
							tips.setText(data.get("msg"));
							return;
						case 2:
							tips.setText(data.get("msg"));
							return;
						case 3:
							tips.setText(data.get("msg"));
							return;
						case 4:
							tips.setText(data.get("msg"));
							return;
						default:
							break;
						}
						
						ToastUtils.showShort(RegistActivity.this, "注册成功!");
						Log.i("RegistActivity", "bean.status---->"+bean.status);
						IntentUtils.startActivity(RegistActivity.this,
								LoginActivity.class);
						
						RegistActivity.this.finish();
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						
					}
				});
//		new AsyncTask<Object, String, String>() {
//			String result;
//			protected String doInBackground(Object... params) {
//				url = ConfigUtils.baseurl + "index.php?c=member&m=regist";
//				RequestParams requestParams = new RequestParams();
//				requestParams.addBodyParameter("username", usernameTv.getText()
//						.toString());
//				requestParams.addBodyParameter("password", password.getText()
//						.toString());
//				requestParams.addBodyParameter("nickname", nickname.getText()
//						.toString());
//				requestParams.addBodyParameter("tel", tel.getText().toString());
//				requestParams.addBodyParameter("email", emailText.getText()
//						.toString());
//				requestParams.addBodyParameter("IDcard", idCard.getText()
//						.toString());
//				HttpManage.getRegistResult(usernameTv.getText().toString().trim(),
//						password.getText().toString().trim(), 
//						nickname.getText().toString().trim(),
//						tel.getText().toString().trim(), 
//						emailText.getText().toString().trim(), 
//						idCard.getText().toString().trim(), 
//						new AsyncHttpResponseHandler() {
//							
//							@Override
//							public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//								result=new String(arg2);
//								
//							}
//							
//							@Override
//							public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//								result="获取数据失败";
//								
//							}
//						});
//				LogHelper.e("注册地址：" + url);
//				return result;
//			}
//
//			protected void onPostExecute(String result) {
//				submit.setEnabled(true);
//				LogHelper.e(result);
//				if (dialog.isShowing()) {
//					dialog.dismiss();
//				}
//				if (result == null) {
//					return;
//				}
//				Map<String, String> data = Common.str2mapstr(result);
//				switch (Integer.parseInt(data.get("status"))) {
//				case 0:
//					Gson gson = new Gson();
//					bean=gson.fromJson(result, RegistBean.class);
//					break;
//				case 1:
//					tips.setText(data.get("msg"));
//					return;
//				case 2:
//					tips.setText(data.get("msg"));
//					return;
//				case 3:
//					tips.setText(data.get("msg"));
//					return;
//				case 4:
//					tips.setText(data.get("msg"));
//					return;
//				default:
//					break;
//				}
//				
//				if (data.containsKey("error_code")
//						&& !data.get("error_code").equals("0")) {
//					ToastUtils.showShort(RegistActivity.this, data.get("msg"));
//					tips.setText(data.get("msg"));
//					return;
//				}
//				if (!data.containsKey("error_code")) {
//					ToastUtils.showShort(RegistActivity.this, "注册成功!");
//					Log.i("RegistActivity", bean.status);
//					IntentUtils.startActivity(RegistActivity.this,
//							MainActivity.class);
//					RegistActivity.this.finish();
//				}
//				super.onPostExecute(result);
//			}
//		}.execute();
	}
}
