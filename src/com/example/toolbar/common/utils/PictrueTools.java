package com.example.toolbar.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 相片裁剪 Created by fylder on 2015/3/26.
 */
public class PictrueTools {

	private static final String TAG = "Pictrue";

	private static String FILE_PATH = "/Android/data/com.pub.parents/cache";// 文件路径
	private static String PICTURE_NAME = "corp_picture.jpg";// 图片

	public static final String IMAGE_FILE_LOCATION = "file://" + Environment.getExternalStorageDirectory().getPath()
			+ FILE_PATH + "/" + PICTURE_NAME;
	public static final String PICTURE_PATH = Environment.getExternalStorageDirectory().getPath() + FILE_PATH;// 图片目录
	public static final String PICTURE_FILE_PATH = PICTURE_PATH + "/" + PICTURE_NAME;// 图片文件path

	public static final int TAKE_BIG_PICTURE = 1;//相机
	public static final int CROP_BIG_PICTURE = 3;
	public static final int CHOOSE_BIG_PICTURE = 5;//4.4之前系统
	public static final int CHOOSE_KIT_PICTURE = 6;//4.4之后系统
	public static final int CHOOSE_KIT_PICTURE_CROP = 7;

	// aspectX aspectY 是宽高的比例
	private static int outputX = 480;
	private static int outputY = 480;
	// outputX outputY 是裁剪图片宽高
	private static int aspectX = 1;
	private static int aspectY = 1;
	
	
	
	/**
	 * 从相册获取-并且返回原图
	 *
	 * @param activity
	 * @param imageUri
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static void getPicture(Activity activity) {
		Intent intent;
		LogHelper.e("SDK:" + Build.VERSION.SDK_INT);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			activity.startActivityForResult(intent, CHOOSE_KIT_PICTURE);
			
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			activity.startActivityForResult(intent, CHOOSE_BIG_PICTURE);
		}
	}

	/**
	 * 从相册获取-并且进行裁剪
	 *
	 * @param activity
	 * @param imageUri
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static void getPictureForCrop(Activity activity, Uri imageUri) {
		Intent intent;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			activity.startActivityForResult(intent, CHOOSE_KIT_PICTURE);
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", aspectX);
			intent.putExtra("aspectY", aspectY);
			intent.putExtra("outputX", outputX);
			intent.putExtra("outputY", outputY);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", false);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", false); // no face detection
			activity.startActivityForResult(intent, CHOOSE_BIG_PICTURE);
		}
	}

	/**
	 * 从相机获取
	 *
	 * @param activity
	 * @param imageUri
	 */
	public static void getCamera(Activity activity, Uri imageUri) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// action is
																	// capture
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		activity.startActivityForResult(intent, TAKE_BIG_PICTURE);
	}
	
	/**
	 * 从相机获取
	 *
	 * @param activity
	 * @param imageUri
	 */
	public static void getCameraForPath(Activity activity,String imagePath) {
		
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// action is
		Uri uri = Uri.fromFile(new File(imagePath));
		intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		activity.startActivityForResult(intent, TAKE_BIG_PICTURE);
	}
	
	
	
	/**
	 * 相册回调-并且返回原图
	 *
	 * @param data
	 * @param activity
	 */
	public static void resultPicture(Intent data, Activity activity) {
		Uri selectedImage = data.getData();
		String imagePath = PictureHelper.getPath(activity, selectedImage); // 获取图片的绝对路径
		Uri newUri = Uri.parse("file:///" + imagePath); // 将绝对路径转换为URL
	}

	/**
	 * 相册回调-并且进行裁剪-4.4以后使用的方法
	 *
	 * @param data
	 * @param activity
	 */
	public static void resultPictureForCrop(Intent data, Activity activity) {
		Uri selectedImage = data.getData();
		String imagePath = PictureHelper.getPath(activity, selectedImage); // 获取图片的绝对路径
		Uri newUri = Uri.parse("file:///" + imagePath); // 将绝对路径转换为URL
		startPhotoZoom(newUri, activity);
	}

	/**
	 * 调用系统剪裁-4.4以后使用的方法
	 *
	 * @param uri
	 * @param activity
	 */
	public static void startPhotoZoom(Uri uri, Activity activity) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);

		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("return-data", true);

		intent.putExtra("noFaceDetection", true);
		activity.startActivityForResult(intent, CHOOSE_KIT_PICTURE_CROP);
	}

	/**
	 * 从Uri提取Bitmap
	 *
	 * @param uri
	 * @param context
	 * @return
	 */
	public static Bitmap decodeUriAsBitmap(Uri uri, Context context) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			return null;
		}
		return bitmap;
	}

	/**
	 * 从Intent提取Bitmap
	 *
	 * @param data
	 * @return
	 */
	public static Bitmap intentToBitmap(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			return extras.getParcelable("data");
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param bmp
	 * @param filename
	 * @return
	 */
	public static boolean saveBitmap2file(Bitmap bmp, String filePath) {
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bmp.compress(format, quality, stream);
	}
	
	

}
