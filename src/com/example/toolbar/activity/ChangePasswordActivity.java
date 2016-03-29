package com.example.toolbar.activity;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import com.example.dolphinradio.R;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.http.HttpManage.OnCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {
	private Toolbar mToolbar;
	private EditText et_old_password;
	private EditText et_new_password;
	private EditText et_repeat_password;
	private Button bt_sure;
	private CheckBox cb_show_password;
	private MyTextWatcher watcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		initView();
	}

	private void initView() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("更改密码");
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});

		et_old_password = (EditText) findViewById(R.id.et_old_password);
		et_new_password = (EditText) findViewById(R.id.et_new_password);
		et_repeat_password = (EditText) findViewById(R.id.et_repeat_new_password);
		bt_sure = (Button) findViewById(R.id.bt_sure);
		cb_show_password = (CheckBox) findViewById(R.id.cb_show_password);
		cb_show_password.setChecked(false);
		watcher = new MyTextWatcher();
		et_old_password.addTextChangedListener(watcher);
		et_new_password.addTextChangedListener(watcher);
		et_repeat_password.addTextChangedListener(watcher);
		bt_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String uid = MyApplication.getInstance().getSpUtil().getUid();
				String old_password = et_old_password.getText().toString()
						.trim();
				String new_password = et_new_password.getText().toString()
						.trim();
				if (!isPasswordSame()) {
					Toast.makeText(ChangePasswordActivity.this,
							"两次密码不一致，请重新输入", 0).show();
					return;
				}
				subPassworldChange(uid, old_password, new_password);
			}
		});

		cb_show_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							// 编辑框中的内容：显示
							et_old_password
									.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
							et_new_password
									.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
							et_repeat_password
									.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
							setFlickerytion();
						} else {
							// 编辑框中的内容：隐藏
							et_old_password
									.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD
											| InputType.TYPE_CLASS_TEXT);
							et_new_password
									.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD
											| InputType.TYPE_CLASS_TEXT);
							et_repeat_password
									.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD
											| InputType.TYPE_CLASS_TEXT);
							setFlickerytion();
						}
					}
				});
	}

	private void subPassworldChange(String uid, String old_password,
			String new_password) {
		String url = HttpManage.changePassworkUrl;
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("uid", uid);
		paramsMap.put("old_password", old_password);
		paramsMap.put("new_password", new_password);
		HttpManage.getNetData(url, paramsMap, 0, new OnCallBack() {
			
			@Override
			public void onSuccess(byte[] arg2) {

				String result = new String(arg2);
				Log.i("ChangePassword", result);
				Map<String, String> map = Common.str3map(result);
				// 对字符串进行判断，判断密码是否更改成功
				switch (Integer.valueOf(map.get("status"))) {
				case 0:
					// 提示密码更换成功
					Toast.makeText(ChangePasswordActivity.this, "密码更换成功", 0)
							.show();
					break;
				case 1:
					Toast.makeText(ChangePasswordActivity.this, "原密码或新密码不能为空",
							0).show();
					break;
				case 2:
					Toast.makeText(ChangePasswordActivity.this, "该用户不存在", 0)
							.show();
					break;
				case 3:
					Toast.makeText(ChangePasswordActivity.this, "原密码不正确", 0)
							.show();
					break;
				case 4:
					Toast.makeText(ChangePasswordActivity.this,
							"对不起，出错了，请稍后再试", 0).show();
					break;
				case 5:
					Toast.makeText(ChangePasswordActivity.this, "原密码和新密码不能相同",
							0).show();
					break;

				default:
					break;
				}
			}
			
			@Override
			public void onFailure(byte[] arg2, Throwable arg3) {
				Toast.makeText(ChangePasswordActivity.this, "密码更换失败", 0).show();
			}
		});
		
//		HttpManage.changePasswork(new AsyncHttpResponseHandler() {
//
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				String result = new String(arg2);
//				Log.i("ChangePassword", result);
//				Map<String, String> map = Common.str3map(result);
//				// 对字符串进行判断，判断密码是否更改成功
//				switch (Integer.valueOf(map.get("status"))) {
//				case 0:
//					// 提示密码更换成功
//					Toast.makeText(ChangePasswordActivity.this, "密码更换成功", 0)
//							.show();
//					break;
//				case 1:
//					Toast.makeText(ChangePasswordActivity.this, "原密码或新密码不能为空",
//							0).show();
//					break;
//				case 2:
//					Toast.makeText(ChangePasswordActivity.this, "该用户不存在", 0)
//							.show();
//					break;
//				case 3:
//					Toast.makeText(ChangePasswordActivity.this, "原密码不正确", 0)
//							.show();
//					break;
//				case 4:
//					Toast.makeText(ChangePasswordActivity.this,
//							"对不起，出错了，请稍后再试", 0).show();
//					break;
//				case 5:
//					Toast.makeText(ChangePasswordActivity.this, "原密码和新密码不能相同",
//							0).show();
//					break;
//
//				default:
//					break;
//				}
//			}
//
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//					Throwable arg3) {
//				Toast.makeText(ChangePasswordActivity.this, "密码更换失败", 0).show();
//			}
//		}, uid, old_password, new_password);

	}

	private Boolean isPasswordSame() {
		String new_password = et_new_password.getText().toString().trim();
		String new_repeat_password = et_repeat_password.getText().toString()
				.trim();
		if (new_password.equals(new_repeat_password)) {
			return true;
		}
		return false;
	}

	private void setFlickerytion() {
		// 光标的位置显示的文字后
		et_old_password.setSelection(et_old_password.getText().toString()
				.length());
		et_new_password.setSelection(et_new_password.getText().toString()
				.length());
		et_repeat_password.setSelection(et_repeat_password.getText().toString()
				.length());
	}
	
	class MyTextWatcher implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(!et_old_password.getText().toString().isEmpty()
					&& !et_new_password.getText().toString().isEmpty()
					&& !et_repeat_password.getText().toString().isEmpty()){
				bt_sure.setEnabled(true);
				bt_sure.setBackgroundResource(R.drawable.corners_selector);
			}else{
				bt_sure.setEnabled(false);
				bt_sure.setBackgroundResource(R.drawable.corners_gray_bg);
			}
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
