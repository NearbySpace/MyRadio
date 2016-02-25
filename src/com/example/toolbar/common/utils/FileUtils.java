package com.example.toolbar.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;


import java.util.Vector;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.ViewDebug.FlagToString;
import android.widget.Toast;

/**
 * 文件操作工具包
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class FileUtils {
	
	/**
	 *  TODO 判断sd卡是否存在，返回 false代表不存在
	 */
	public static boolean SDCardExist(Context context){
		
		String SDCardstate = Environment.getExternalStorageState();
		if (!SDCardstate.equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(context, "内存卡不存在,请插入内存卡！", Toast.LENGTH_LONG).show();
			return false;
		}else {
			return true;
		}
		
	}
	
	
	/**
	 * TODO 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
	 * 
	 * @param context
	 * @param msg
	 */
	public static void write(Context context, String fileName, String content) {
		if (content == null)
			content = "";
		
		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			fos.write(content.getBytes());

			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *TODO 读取文本文件
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String read(Context context, String fileName) {
		try {
			FileInputStream in = context.openFileInput(fileName);
			return readInStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private static String readInStream(FileInputStream inStream) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}

			outStream.close();
			inStream.close();
			return outStream.toString();
		} catch (IOException e) {
			Log.i("FileTest", e.getMessage());
		}
		return null;
	}

	public static File createFile(String folderPath, String fileName) {
		File destDir = new File(folderPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		return new File(folderPath, fileName + fileName);
	}

	/**
	 *TODO 向手机写图片
	 * 
	 * @param buffer
	 * @param folder
	 * @param fileName
	 * @return
	 */
	public static boolean writeFile(byte[] buffer, String folder,
			String fileName) {
		boolean writeSucc = false;

		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

		String folderPath = "";
		if (sdCardExist) {
			folderPath = Environment.getExternalStorageDirectory()
					+ File.separator + folder + File.separator;
		} else {
			writeSucc = false;
		}

		File fileDir = new File(folderPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		File file = new File(folderPath + fileName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
			writeSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return writeSucc;
	}

	/**
	 *TODO 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (StringUtils.isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	/**
	 *TODO 根据文件的绝对路径获取文件名但不包含扩展名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameNoFormat(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return "";
		}
		int point = filePath.lastIndexOf('.');
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
				point);
	}

	/**
	 *TODO 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (StringUtils.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	/**
	 *TODO 获取文件大小
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		long size = 0;

		File file = new File(filePath);
		if (file != null && file.exists()) {
			size = file.length();
		}
		return size;
	}

	/**
	 *TODO 获取文件大小
	 * 
	 * @param size
	 *            字节
	 * @return
	 */
	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		float temp = (float) size / 1024;
		if (temp >= 1024) {
			return df.format(temp / 1024) + "M";
		} else {
			return df.format(temp) + "K";
		}
	}

	/**
	 *TODO 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 *TODO 获取目录文件大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		
		return dirSize;
	}
	
	
	public static String getFriendlyDirSize(long size){
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		if (size < 1024) {
			return size + "B";
		}else if ( 1024 < size && size < 1024*1024 ) {
			return (size/1024) + "K";
		}else if (size > 1024*1024) {
			return (df.format((double) size / 1048576)) + "M";
		//	return (size/1048576) + "M";
		}else {
			return "0K";
		}
	}

	/**
	 * TODO获取目录文件个数
	 * 
	 * @param f
	 * @return
	 */
	public long getFileList(File dir) {
		long count = 0;
		File[] files = dir.listFiles();
		count = files.length;
		for (File file : files) {
			if (file.isDirectory()) {
				count = count + getFileList(file);// 递归
				count--;
			}
		}
		return count;
	}

	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			out.write(ch);
		}
		byte buffer[] = out.toByteArray();
		out.close();
		return buffer;
	}

	/**
	 *TODO 检查文件是否存在;有的话返回true
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkFileExists(String name) {
		boolean status;
		if (!name.equals("")) {
			File file = new File(name);
			if (!file.exists()) {
				status = false;
			}else{
				status = true;
			}
		} else {
			status = false;
		}
		return status;

	}
	
	/**
	 * 判断一个文件夹下是否有相同的文件
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isFileSame(String name,String path) {
		File sd_file = new File(path);
		// File rom_file = new File(getFilesDir().getPath() + "/Download/");
		File[] files;
		String fileName;
		Vector<String> vecFile = new Vector<String>();
		if (sd_file.exists()) {
			// 取得SD卡下的Download目录下的所有文件
			files = sd_file.listFiles();
		} else {
			sd_file.mkdirs();
			return false;
			// 取得ROM下的Download目录下的所有文件
			// files = rom_file.listFiles();
			// Log.i(TAG, "ROM卡下" + files);
		}
		// 历遍判断文件名是否相同
		if (files == null)
			return false;
		for (int iFileLength = 0; iFileLength < files.length; iFileLength++) {
			// 判断是否为文件夹
			if (!files[iFileLength].isDirectory()) {
				fileName = files[iFileLength].getName();
				if (name.equals(fileName)) {
					return true;
				} 
			}
		}
		return false;
	}
	
	/**
	 *TODO 检查文件是否存在;没有则建立，有的话返回true
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkFileWithMkdirs(String name) {
		boolean status;
		if (!name.equals("")) {
			File file = new File(name);
			if (!file.exists()) {
				status = file.mkdirs();
			}else{
				status = true;
			}
		} else {
			status = false;
		}
		return status;

	}

	/**
	 *TODO 计算SD卡的剩余空间
	 * 
	 * @return 返回-1，说明没有安装sd卡
	 */
	public static long getFreeDiskSpace() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				freeSpace = availableBlocks * blockSize / 1024;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return -1;
		}
		return (freeSpace);
	}
	
	/**
	 *TODO 计算SD卡的剩余空间是否大于10M
	 * 
	 * @return 返回-1，说明没有安装sd卡
	 */
	public static boolean isSKCardSpaceEnough() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				freeSpace = (availableBlocks * blockSize / 1024) /1024 ;
				if (freeSpace > 10 ) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return false;
		}
		return false;
	}
	
	
	/**
	 *TODO新建目录
	 * 
	 * @param directoryName
	 * @return
	 */
	public static boolean createDirectory(String directoryName) {
		boolean status;
		if (!directoryName.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + directoryName);
			status = newPath.mkdir();
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 *TODO 检查是否安装SD卡
	 * 
	 * @return
	 */
	public static boolean checkSaveLocationExists() {
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status;
		if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 *TODO 删除目录(包括：目录里的所有文件)
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteDirectory(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isDirectory()) {
				String[] listfile = newPath.list();
				// delete all files within the specified directory and then
				// delete the directory
				try {
					for (int i = 0; i < listfile.length; i++) {
						File deletedFile = new File(newPath.toString() + "/"
								+ listfile[i].toString());
						deletedFile.delete();
					}
					newPath.delete();
					Log.i("DirectoryManager deleteDirectory", fileName);
					status = true;
				} catch (Exception e) {
					e.printStackTrace();
					status = false;
				}

			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 *TODO 删除文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isFile()) {
				try {
					Log.i("DirectoryManager deleteFile", fileName);
					newPath.delete();
					status = true;
				} catch (SecurityException se) {
					se.printStackTrace();
					status = false;
				}
			} else
				status = false;
		} else
			status = false;
		return status;
	}
	
	//TODO 写入到SD下
	public static void writeStringToSD(String directoryName, String fileName,
			String content) {

		try {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + directoryName);
			if (!newPath.exists()) {
				newPath.mkdir();
			}
			

			File file = new File(newPath, fileName);
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStreamWriter write = new OutputStreamWriter(fOut);
			write.write(content);
			write.flush();
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//TODO 读取SD 指定文件
	public static String readStringFromSD(String directoryName, String fileName) {
		String str = "";

		try {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + directoryName);

			File file = new File(newPath, fileName);
			FileInputStream inputStream = new FileInputStream(file);
			str = readInStream(inputStream);

		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return str;
	}

	//TODO 获取最后修改时间
	public static Long getLastModifyTime(String directoryName, String fileName) {
		Long time;

		File path = Environment.getExternalStorageDirectory();
		File newPath = new File(path.toString() + directoryName);

		File file = new File(newPath, fileName);
		time = file.lastModified();

		return time;
	}

}