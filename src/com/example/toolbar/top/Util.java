package com.example.toolbar.top;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.ImageView;



@SuppressLint("SimpleDateFormat")
public class Util {
	
	private static Activity activity;
	

	public Util(Activity a) {
		activity = a;
	}


	/**
	 * 图片缩放
	 * 
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Drawable resizeImage(Bitmap bitmap, int w, int h) {

		Bitmap BitmapOrg = bitmap;

		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);

		return new BitmapDrawable(resizedBitmap);

	}

	public static Bitmap showSDPic(String path) {
		Bitmap bmp = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = computeSampleSize(options, -1, 256 * 256);

		options.inJustDecodeBounds = false;
		try {
			bmp = BitmapFactory.decodeFile(path, options);

		} catch (OutOfMemoryError err) {
		}

		return bmp;
	}

	/**
	 * YL 计算inSampleSize
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	/**
	 * YL 从原图计算图片的SampleSize
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}

	}

	public static Bitmap showBigSDPic(String path) {
		Bitmap bmp = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = computeSampleSize(options, -1, 128 * 128);

		options.inJustDecodeBounds = false;
		try {
			bmp = BitmapFactory.decodeFile(path, options);
		} catch (OutOfMemoryError err) {

		}

		return bmp;
	}

	

	/**
	 * Durable 转换成bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) //
	{
		int width = drawable.getIntrinsicWidth(); // 取drawable的长�?
		int height = drawable.getIntrinsicHeight();
		if (width <= 0)
			width = 100;
		if (height <= 0)
			height = 100;
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // 取drawable的颜色格�?
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应bitmap的画�?
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // 把drawable内容画到画布�?
		return bitmap;
	}

	
	public static void showImagebyPath(ImageView image, String path) {
		// Bitmap bm=showSDPic(path);
		Bitmap bm = showBigSDPic(path);
		image.setImageBitmap(bm);
	}



	/**
	 * Android获取Ip
	 * 
	 * @return
	 */
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			// Log.e(LOG_TAG, ex.toString());
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 取得版本�?
	 * 
	 * @param a
	 * @return
	 * @throws NameNotFoundException
	 */
	public static String getVersionNo(Activity a)
			throws NameNotFoundException {
		return a.getPackageManager().getPackageInfo(a.getPackageName(), 0).versionName;
	}

	
	





	/**
	 * 生成文件md5�?
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}






	/**
	 * 取得系统时间
	 * 
	 * @return
	 */
	public static String getSystemDatatime() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(new Date());
	}

	/**
	 * 取得系统时间
	 * 
	 * @return
	 */
	public static String getSystemDatatimeYY() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date());
	}

	/**
	 * 取得系统当前日期
	 * 
	 * @return
	 */
	public static String getSystemDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(new Date());
	}



	/**
	 * 从assets里取数据
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFromAssets(Context a, String fileName) {
		String Result = "";
		try {
			InputStreamReader inputReader = new InputStreamReader(a
					.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
			return Result;
		}
	}

	/**
	 * 字节转换成整�? *
	 * 
	 * @param b
	 * @return
	 * @throws IOException
	 */
	public static int byteToInt(byte[] b) throws IOException {

		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream out = new DataInputStream(buf);
		// System.out.println("i:" + i);

		int a = out.readInt();

		// System.out.println("i:" + b);
		out.close();
		buf.close();
		return a;
	}

	public static int bytesToInt(byte b[]) {
		byte bufint[] = { b[3], b[2], b[1], b[0] };
		try {
			return byteToInt(bufint);
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 字节转换成字符串
	 * 
	 * @param b
	 * @return
	 * @throws IOException
	 */
	public static String byteToStirng(byte[] b) throws IOException {

		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		DataInputStream out = new DataInputStream(buf);
		String a = out.readUTF();
		out.close();
		buf.close();
		return a;
	}

	public final static byte[] getBytes(int s, boolean asc) {
		byte[] buf = new byte[4];
		if (asc)
			for (int i = buf.length - 1; i >= 0; i--) {
				buf[i] = (byte) (s & 0x000000ff);
				s >>= 8;
			}
		else
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (s & 0x000000ff);
				s >>= 8;
			}
		return buf;
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String toHexString(byte[] b) {
		// String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}



	public static String moneyFormat(double money) {
		try
		{
		  if (money == 0) {
		 	 return "0";
		  }
		  DecimalFormat df = new DecimalFormat("#0.00");
		   return df.format(money);
		}catch(Exception e)
		{
			return "";
		}
	}





	/**
	 * 计算距离
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public static String calculateDistance(String lat1, String lng1,
			String lat2, String lng2) {
		double pk = (double) (180 / Math.PI);
		try {
			double a1 = Double.parseDouble(lat1) / pk;
			double a2 = Double.parseDouble(lng1) / pk;
			double b1 = Double.parseDouble(lat2) / pk;
			double b2 = Double.parseDouble(lng2) / pk;
			double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1)
					* Math.cos(b2);
			double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1)
					* Math.sin(b2);
			double t3 = Math.sin(a1) * Math.sin(b1);
			double tt = Math.acos(t1 + t2 + t3);

			int md = (int) (6366000 * tt / 10);
			double m = ((double) md) / 100;
			if (m == 0) {
				m = 0.01;
			}
			return m + "km";
		} catch (Exception e) {
			return "未知";
		}
	}

	public static String parseDistance(String distance) {
		try {
			int md = (int) (Double.valueOf(distance) / 10);
			double m = ((double) md) / 100f;
			if (m >= 2) {
				DecimalFormat df = new DecimalFormat("0.0");
				return df.format(m) + "km";
			} else {
				if (m == 0) {
					m = 0.01;
				}
				m *= 1000;
				return (int) m + "米以�?";
			}
		} catch (Exception e) {

		}
		return "";
	}
	
	
	public static String parseDistances(String distance) {
		try {
			int md = (int) (Double.valueOf(distance) / 10);
			double m = ((double) md) / 100f;
			if (m >= 2) {
				DecimalFormat df = new DecimalFormat("0.0");
				return df.format(m) + "km";
			} else {
				if (m == 0) {
					m = 0.01;
				}
				m *= 1000;
				return (int) m + "m";
			}
		} catch (Exception e) {

		}
		return "";
	}
	
	/**
	 * 取得屏幕大小
	 * 
	 * @return
	 */
	public static DisplayMetrics getWindewScreenSize() {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	/**
	 * dp 转换�?  px
	 */
	public static int dp2px(float dpValue) {
		final float scale = App.getIns().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	public static int sp2px(float spValue) { 
        final float fontScale = App.getIns().getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 

	
	
	
	public static final String join(String join,String[] strAry){
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<strAry.length;i++){
             if(i==(strAry.length-1)){
                 sb.append(strAry[i]);
             }else{
                 sb.append(strAry[i]).append(join);
             }
        }
       
        return new String(sb);
    }
	/**
	 * 根据秒数转换成功 hh：MM:dd
	 * */
	public static String getTimeStr(int time){
		if(time<1){
			return "00:00:00";
		}
		
		int hour = time / 3600;
		int mouth = time % 3600 / 60;
		int second = time % 60;
		return (hour < 10 ? "0" + hour : hour+"") +":"+(mouth < 10 ? "0" + mouth : mouth +"")+":"+(second < 10 ? "0" + second : second + "");
	}
}
