package com.example.toolbar.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.strawberryradio.R;

public class DialogUtils {

	public static void showNetWorkAlerDialog(Context context,DialogInterface.OnClickListener sure
			,DialogInterface.OnClickListener cancel) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("警告");
		builder.setMessage(R.string.network_remind);
		builder.setPositiveButton("确定", sure);
		builder.setNegativeButton("取消", cancel);
		builder.setCancelable(false);
		builder.show();

	}

}
