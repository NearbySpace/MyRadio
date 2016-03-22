package com.example.toolbar.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.strawberryradio.R;

public class DialogUtils {

	public static void showNetWorkAlerDialog(Context context,
			DialogInterface.OnClickListener sure,
			DialogInterface.OnClickListener cancel) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("警告");
		builder.setMessage(R.string.network_remind);
		builder.setPositiveButton("确定", sure);
		builder.setNegativeButton("取消", cancel);
		builder.setCancelable(false);
		builder.show();

	}

	public static void showChooseDialog(Context ctx, int title, String msg,
			View contentView, DialogInterface.OnClickListener listener) {

		final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setCancelable(true);
		if (title != 0) {
			builder.setTitle(title);
		}
		if (msg != null)
			builder.setMessage(msg);
		if (contentView != null)
			builder.setView(contentView);
		if (title != 0) {
			builder.setPositiveButton(R.string.ok, listener);
			builder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});
		}
		final AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(true);
		alert.show();
	}

	public static void showDelDialog(final Context context, String msg,
			final OnClickListener onClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_delete, null);
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		dialog.show();
		Button cancel = (Button) view.findViewById(R.id.delete_dialog_cancel);
		Button sure = (Button) view.findViewById(R.id.delete_dialog_sure);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickListener.onClick(v);
				ToastUtils.show(context, "已删除", Toast.LENGTH_SHORT);
				dialog.dismiss();
			}
		});
	}

}
