package com.example.toolbar.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strawberryradio.R;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.common.utils.NetUtil;
import com.example.toolbar.common.utils.PictrueTools;
import com.example.toolbar.common.utils.PictureHelper;
import com.example.toolbar.http.AsyncHttpClientUtils;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.ImageUtils;
import com.example.toolbar.utils.ToastUtils;
import com.example.toolbar.view.photo.PhotoViewAttacher;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 头像更改
 * 
 * @author hxc
 * 
 */
public class UpUserIconActivity extends AppCompatActivity implements
		OnClickListener {
	private Toolbar mToolbar;
	private ImageView imageView;
	private Button btn_sure;
	private String camerapath = "", photp_path = "";
	private ImageLoader mImageLoader;
	private AlertDialog uploadDialog ;
	public static final int TAKE_CAMREA = 21;
	public static final int PICK_PHOTO = 32;
	private final int REQUEST_CODE_PHOTE = 0; // 访问相册的请求码
	private final int REQUEST_CODE_CAMERA = 1;// 访问相机的请求码
	private final int REQUEST_CODE_CROP = 2;// 调用系统裁剪的请求码
	private final String IMAGE_FILE_NAME = "Strawberry";// 文件夹的名称（保存相机拍摄的图片）
	private String fileName;
	private final int OK = 1;
	private final int ERROR = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studenticon_activity);
		initHeadActionBar();
		getdata();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	public void initHeadActionBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("更新头像");
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		findViewById(R.id.studenticon_activity_photo).setOnClickListener(this);
		findViewById(R.id.studenticon_activity_camera).setOnClickListener(this);
		btn_sure = (Button) findViewById(R.id.studenticon_activity_sure);
		btn_sure.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.studenticon_activity_imageView1);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_action_backimage:
			setResult(Activity.RESULT_OK);
			finish();
			break;

		case R.id.studenticon_activity_sure: // 上传图片
			final File filePost = new File(getAlbumStorageDir("HeadImage"), fileName);
			uploadUserIcon(filePost.getAbsolutePath());
			break;

		case R.id.studenticon_activity_photo:
			Intent intentFromPhoto = new Intent();
			intentFromPhoto.setAction(Intent.ACTION_PICK);
			intentFromPhoto.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intentFromPhoto, REQUEST_CODE_PHOTE);
			// PictrueTools.getPicture(this);
			break;

		case R.id.studenticon_activity_camera:
			Intent intentFromCamera = new Intent();
			intentFromCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(Environment.getExternalStorageDirectory(),
					IMAGE_FILE_NAME);
			if (!file.exists()) {
				file.mkdirs();
			}
			// 指定调用相机拍照后的照片存储的路径
			intentFromCamera.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(file, "headIcon.png")));
			startActivityForResult(intentFromCamera, REQUEST_CODE_CAMERA);
			break;

		default:
			break;
		}
	}

	private void getdata() {
		if (!NetUtil.isNetConnected(this)) {
			return;
		}

		String url = MyApplication.getInstance().getSpUtil().getHeadIcon();
		if (url.contains("_200_200")) {
			url = url.replace("_200_200", "");
		}
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.displayImage(url, imageView,
				ImageLoaderHelper.getDisplayImageOptions());

	}

	// 获取相机
	private void takeCamera() {
		// TODO Auto-generated method stub
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			Log.v("TestFile", "SD card 不存在，请检查SD card ！");
			return;
		}
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/DCIM/Camera/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		camerapath = path + System.currentTimeMillis() + ".jpg";
		Uri uri = Uri.fromFile(new File(camerapath));
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, TAKE_CAMREA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 用户没有进行有效的设置操作，返回
		if (resultCode == RESULT_CANCELED) {
			Toast.makeText(UpUserIconActivity.this, "操作取消", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		switch (requestCode) {
		case REQUEST_CODE_PHOTE:
			System.out.println(data.getData());
			startPictureCrop(data.getData());
			break;
		case REQUEST_CODE_CAMERA:
			File tempFile = new File(Environment.getExternalStorageDirectory(),
					IMAGE_FILE_NAME);
			startPictureCrop(Uri.fromFile(new File(tempFile, "headIcon.png")));
			break;

		case REQUEST_CODE_CROP:
			setImageToHeadView(data);

			break;

		default:
			break;
		}

		// TODO Auto-generated method stub
		// if (resultCode == Activity.RESULT_OK) {
		// if (requestCode == TAKE_CAMREA) {
		//
		// String pathString = ConfigUtils.SDcardPath
		// + ConfigUtils.cameraimagepath;
		// // 生成常规缩略图
		// File file = ImageUtils.ZoomImageView(camerapath, pathString,
		// MyApplication.getInstance().display_width,
		// MyApplication.getInstance().display_height);
		//
		// saveStudentIcon(file.getAbsolutePath());
		// } else if (requestCode == PictrueTools.CHOOSE_KIT_PICTURE ||
		// requestCode == PictrueTools.CHOOSE_BIG_PICTURE) {// 4.4以后系统
		// getPhotoFileINKITKAT(data.getData());
		// }
		//
		// }

		super.onActivityResult(requestCode, resultCode, data);

	}

	// 开始裁剪
	private void startPictureCrop(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		System.out.println("uri----->" + uri);
		intent.setDataAndType(uri, "image/*");
		// crop=true是设置在开启的Intent中设置显示的VIEW可裁剪,即设置可裁剪
		intent.putExtra("crop", true);
		// aspectX aspectY 是裁剪框宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高,这里设置为像素100*100
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);
		// 启动裁剪页
		startActivityForResult(intent, REQUEST_CODE_CROP);
	}

	// 提取保存裁剪之后的图片数据,设置给ImageView
	private void setImageToHeadView(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			imageView.setImageBitmap(photo);
			addSignatureToGallery(photo);
			btn_sure.setEnabled(true);
		}

	}

	// 把得到的Bitmap转换成指定格式的图片
	public void saveBitmapToPNG(Bitmap bitmap, File photo) throws IOException {
		Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(bitmap, 0, 0, null);
		OutputStream stream = new FileOutputStream(photo);
		newBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
		stream.close();
	}

	// 取得图片的保存文件夹
	public File getAlbumStorageDir(String albumName) {
		// Get the directory for the user's public pictures directory.
		// 保存图片的文件夹
		File file = new File(new File(
				Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME),
				albumName);
		if (!file.mkdirs()) {
			Log.e("SignaturePad", "文件目录没有被创建");
		}
		return file;
	}

	// 保存图片并通知媒体库有内容更新
	public boolean addSignatureToGallery(Bitmap signature) {
		boolean result = false;
		try {
			// String.format("Signature_%d.jpg",
			// System.currentTimeMillis()),%d代表后面的一个参数
			fileName = String.format("head_icon_%d.jpg",
					System.currentTimeMillis());
			File photo = new File(getAlbumStorageDir("HeadImage"), fileName);
			saveBitmapToPNG(signature, photo);
			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri contentUri = Uri.fromFile(photo);
			mediaScanIntent.setData(contentUri);
			UpUserIconActivity.this.sendBroadcast(mediaScanIntent);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void getPhotoFileINKITKAT(Uri data) {

		String imageFilePath = PictureHelper.getPath(UpUserIconActivity.this,
				data); // 获取图片的绝对路径
		LogHelper.e("path:" + imageFilePath);
		if (imageFilePath != null) {
			// 生成常规缩略图
			String pathString = ConfigUtils.SDcardPath
					+ ConfigUtils.pickimagepath;
			File file = ImageUtils.ZoomImageView(imageFilePath, pathString,
					MyApplication.getInstance().display_width,
					MyApplication.getInstance().display_height);

			uploadUserIcon(file.getAbsolutePath());
		} else {
			Toast.makeText(this, "选择文件不正确", Toast.LENGTH_LONG).show();

		}
	}

	/**
	 * 上传图片 上传的图片不能太大，不然会上传失败
	 * 
	 * @param imageurl
	 */
	private void uploadUserIcon(final String imageurl) {
		if (!NetUtil.isNetConnected(this)) {
			return;
		}
		uploadDialog = showUploadWaitDialog();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// TODO Auto-generated method stub
				String res = new String(response);
				LogHelper.e(res);
				Map<String, String> dataMap = Common.str2mapstr(res);
				if (dataMap.containsKey("status")
						&& dataMap.get("status").equals("0")) {
					ToastUtils.showShort(UpUserIconActivity.this, "修改成功！");
					if (dataMap.get("msg") != null) {
						MyApplication.getInstance().getSpUtil()
								.setHeadIcon(dataMap.get("msg"));
						getdata();
						setResult(OK);
						uploadDialog.dismiss();
						finish();
					}

				} else {
					ToastUtils.showShort(UpUserIconActivity.this, "修改失败！");
					setResult(ERROR);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				// TODO Auto-generated method stub
				ToastUtils.showShort(UpUserIconActivity.this, "上传失败！");
				uploadDialog.dismiss();
				setResult(ERROR);
			}
		};
		String url = "http://vroad.bbrtv.com/cmradio/index.php?d=android&c=member&m=thumb_save";
		String uid = MyApplication.getInstance().getSpUtil().getUid();
		LogHelper.e("uid:" + uid);
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		File file = new File(imageurl);
		if(file.exists()){
			try {
				params.put("thumb", new File(imageurl));
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}else{
			ToastUtils.show(UpUserIconActivity.this, "文件不存在，请重新选择", Toast.LENGTH_SHORT);
			return ;
		}
		
		AsyncHttpClientUtils.postForAsyn(handler, params, url);
	}
	
	
	/**
	 *  上传等待对话框
	 */
	private AlertDialog showUploadWaitDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.dialog_check_update, null);
		TextView tv = (TextView) view.findViewById(R.id.dialog_check_update_tv);
		tv.setText("正在上传头像...");
		builder.setView(view);
		AlertDialog dialog;
		dialog=builder.show();
		dialog.setCancelable(false);//设置对话框点击屏幕不消失
		return dialog;
	}

	/**
	 * 监听返回--是否退出程序
	 */
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// boolean flag = true;
	// if (keyCode == KeyEvent.KEYCODE_MENU) {
	//
	// return flag;
	// } else if (keyCode == KeyEvent.KEYCODE_BACK) {
	// // 是否退出应用
	// setResult(Activity.RESULT_OK);
	// finish();
	// } else {
	// flag = super.onKeyDown(keyCode, event);
	// }
	// return flag;
	// }

	
}
