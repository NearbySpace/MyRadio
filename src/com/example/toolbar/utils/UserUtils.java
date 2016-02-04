package com.example.toolbar.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strawberryradio.R;
import com.example.toolbar.activity.LoginActivity;
import com.example.toolbar.activity.MyProgramActivity;
import com.example.toolbar.activity.SettingActivity;
import com.example.toolbar.application.MyApplication;

public class UserUtils {

	public static String getUid() {
		if (MyApplication.getInstance().getSpUtil().getUid().isEmpty()) {
			return null;
		} else {
			return MyApplication.getInstance().getSpUtil().getUid();
		}
	}

	public static boolean isLogin() {

		if (MyApplication.getInstance().getSpUtil().getUid().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 已登录返回false，未登录返回true
	 * @param context
	 * @return
	 */
	public static boolean checkLogin(Context context) {

		if (MyApplication.getInstance().getSpUtil().getUid().isEmpty()) {
			IntentUtils.startActivity(context, LoginActivity.class);
			ToastUtils.show(context, "只有登录了，才能执行此操作", Toast.LENGTH_SHORT);
			return true;
		}
		return false;
	}

	public static void showLoginDialog(final Context context) {
		final AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_delete, null);
		dialog=builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		TextView content = (TextView) view.findViewById(R.id.delete_dialog_tv);
		Button cancel = (Button) view.findViewById(R.id.delete_dialog_cancel);
		Button sure = (Button) view.findViewById(R.id.delete_dialog_sure);
		content.setText("您尚未登录，请先登录再操作");
		sure.setText("登录");
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);
				dialog.dismiss();
			}
		});
//		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Intent intent = new Intent(context, LoginActivity.class);
//				context.startActivity(intent);
//				dialog.dismiss();
//				// ((Activity) context).finish();
//			}
//		});
		dialog.show();
	}
}
