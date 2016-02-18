package com.example.toolbar.framework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.strawberryradio.R;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.FileUtils;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.common.utils.MyJsonUtils;
import com.example.toolbar.common.utils.NetUtil;
import com.example.toolbar.http.HttpUtils;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.ToastUtils;

public class UpdateApk {
	ProgressDialog pd;
	NotificationManager notificationManager;
	Notification notification;
	Context activity;
	private ProgressDialog downDialog;
	private static final int DOWN_APK_OK = 1;
	private static final int DOWN_APK_ERROR = 2;

	public UpdateApk(Context context) {

		activity = (Context) context;
		notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(android.R.drawable.stat_sys_download,
				"下载更新", System.currentTimeMillis());
		Intent intent = new Intent();
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		notification.contentIntent = pi;
		notification.contentView = new RemoteViews(context.getPackageName(),
				R.layout.updateapk_progressbar);
		notification.contentView.setProgressBar(R.id.updateapk_progressBar,
				100, 0, false);
		notification.flags = Notification.FLAG_AUTO_CANCEL;

	}

	
	/**
	 * 检查是否有版本更新
	 * @param context
	 * @param flag  用于标记是首页请求检查更新还是设置页请求检查更新 .1为首页，2设置页
	 */
	public static void checkVersion(final Context context ,final int flag) {
		if (!NetUtil.isNetConnected(context)) {
			return;
		}
		MyApplication app = MyApplication.getInstance();
		final PackageInfo pinfo = app.getPackageInfo();
		new AsyncTask<Object, Object, String>() {
			protected String doInBackground(Object... params) {

				String url = ConfigUtils.baseurl
						+ "index.php?d=android&c=api&m=android_version";
				return HttpUtils.getString(url);
			}

			protected void onPostExecute(String version) {
				if (version == null) {
					ToastUtils.showShort(context, "网络错误,请稍候尝试!");
					return;
				}
				if (version.equals("")) {
					ToastUtils.showShort(context, "网络错误 ,请稍候尝试!");
					return;
				}
				Map<String, String> map = Common.str2mapstr(version);
				LogHelper.e(map.toString());
				// 发现新版本
				if (map.isEmpty()) {
					ToastUtils.showShort(context, "网络错误 ,无法检查更新！");
					return;
				}
				if (map.containsKey("version")
						&& !map.get("version").equals(pinfo.versionName)) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setTitle("海豚电台客户端有新的版本啦,V" + map.get("version"));
					builder.setMessage(Html.fromHtml(map.get("message")));
					builder.setPositiveButton("立即更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									ToastUtils
											.show(context, "开始下载新版本安装包...", 1);
									boolean isEnough = FileUtils
											.isSKCardSpaceEnough(); // 计算存储卡的大小
									if (isEnough) {
										File file = new File(
												ConfigUtils.SDcardPath);

										if (!file.exists()) {
											file.mkdirs();
										}
										new UpdateApk(context)
												.doNewVersionUpdate();
									} else {
										Toast.makeText(context,
												"内存卡不可用或者空间不足...请检查后再进行下载！", 1)
												.show();
									}

								}
							});

					builder.setNegativeButton("暂不",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int arg1) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}

							});
					// builder.setCancelable(false);
					builder.show();

				} else {
					if(flag == 2){
						ToastUtils.showShort(context, "您以是最新版本，暂无更新！");
					}
				}
			}
		}.execute();
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case DOWN_APK_OK:
				if (downDialog.isShowing()) {
					downDialog.dismiss();
				}
				notification.contentView = new RemoteViews(
						activity.getPackageName(),
						R.layout.updateapk_progressbar);
				notification.contentView.setTextViewText(R.id.updateapk_tv1,
						"海豚电台客户端下载完成!");
				notification.contentView.setTextViewText(R.id.updateapk_tv2,
						"100%");
				notification.contentView.setProgressBar(
						R.id.updateapk_progressBar, 100, 100, false);
				notificationManager.notify(2, notification);
				openInstallFile(new File(ConfigUtils.SDcardPath,
						ConfigUtils.apkName));
				break;
			case DOWN_APK_ERROR:
				if (downDialog.isShowing()) {
					downDialog.dismiss();
				}
				Toast.makeText(activity, "客户端更新失败！", Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}

			super.handleMessage(msg);
		}

	};

	/**
	 * 更新版本
	 */
	public void doNewVersionUpdate() {
		// 下载对话框
		downDialog = new ProgressDialog(activity);
		downDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		downDialog.setTitle("正在下载更新程序……");
		downDialog.setIcon(R.drawable.app_logo);
		downDialog.setMax(100);
		downDialog.setMessage("下载进度：");
		downDialog.setCancelable(true);
		downDialog.setCanceledOnTouchOutside(true);
		downDialog.show();

		long time = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
		Date date = new Date(time);
		String timeString = format.format(date);

		notification.contentView
				.setTextViewText(R.id.updateapk_tv3, timeString);
		notificationManager.notify(2, notification);
		// downFile(config.baseurl + config.apkName);
		downFile(ConfigUtils.baseurl + "index.php?c=download&m=getApk");

	}

	/**
	 * 下载apk
	 */
	public void downFile(final String url) {
		new Thread() {
			public void run() {
				int updateprogress = 0;
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					downDialog.setMax((int) length);
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(ConfigUtils.SDcardPath,
								ConfigUtils.apkName);
						if(file.exists()){
							file.delete();
						}
						fileOutputStream = new FileOutputStream(file);
						byte[] b = new byte[1024];
						int charb = -1;
						int count = 0;
						while ((charb = is.read(b)) != -1) {
							fileOutputStream.write(b, 0, charb);
							count += charb;
							downDialog.setProgress(count);
							int pp = (int) (100 * count / length);
							if (updateprogress == 0
									|| (pp - 10) > updateprogress) {

								updateprogress += 10;
								notification.contentView.setProgressBar(
										R.id.updateapk_progressBar, 100, pp,
										false);
								notification.contentView.setTextViewText(
										R.id.updateapk_tv2, pp + "%");
								notificationManager.notify(2, notification);
							}
						}
					}

					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					downOK();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					downError();
				}
			}
		}.start();
	}

	// /**
	// * 下载完成，通过handler将下载对话框取消
	// */
	private void downOK() {
		Message message = new Message();
		message.what = DOWN_APK_OK;
		handler.sendMessage(message);
	}

	private void downError() {
		Message message = new Message();
		message.what = DOWN_APK_ERROR;
		handler.sendMessage(message);
	}

	/**
	 * 进入安装界面
	 */
	private void openInstallFile(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		// intent 类型
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(file), type);
		activity.startActivity(intent);
		downDialog.dismiss();
	}

}
