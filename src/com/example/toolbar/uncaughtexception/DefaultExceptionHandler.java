package com.example.toolbar.uncaughtexception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.utils.ToastUtils;

public class DefaultExceptionHandler implements UncaughtExceptionHandler {
	public Context mContext;
	public String info_phone;
	public String info = null;
	public ByteArrayOutputStream baos = null;
	public PrintStream printStream = null;
	public static DefaultExceptionHandler mDefaultExceptionHandler;
	private Thread.UncaughtExceptionHandler mDefaultHandler; // 系统默认的UncaughtException处理类

	public DefaultExceptionHandler(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
//	public synchronized static DefaultExceptionHandler getInstance(Context context) {
//		if (mDefaultExceptionHandler == null) {
//			mDefaultExceptionHandler = new DefaultExceptionHandler(context);
//		}
//		return mDefaultExceptionHandler;
//	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		if (ex == null) {
			mDefaultHandler.uncaughtException(thread, ex);// 系统处理
		} else {
			// 自己处理
						
			try {
				baos = new ByteArrayOutputStream();
				printStream = new PrintStream(baos);
				ex.printStackTrace(printStream);
				byte[] data = baos.toByteArray();
				info = new String(data);
				LogHelper.e(info);
				data = null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (printStream != null) {
						printStream.close();
					}
					if (baos != null) {
						baos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
//			Intent intent = new Intent(mContext,ExceptionInfoService.class);
//			intent.putExtra("info", info);
//			mContext.startService(intent);			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handleException();
			ToastUtils.showShort(mContext, "程序发生意外，应用即将退出！");
		}
	}

	private void handleException() {
		// 退出程序
		System.exit(1);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
