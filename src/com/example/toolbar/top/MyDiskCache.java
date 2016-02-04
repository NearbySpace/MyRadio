package com.example.toolbar.top;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.BaseDiscCache;
import com.nostra13.universalimageloader.utils.IoUtils.CopyListener;

public class MyDiskCache extends BaseDiscCache{

	public static long MAX_DIR_CACHE_SIZE = 1024*1024*300;
	public static long REMOVE_SIZE_EVERY = 1024*1024*10;
	public MyDiskCache(File cacheDir) {
		super(cacheDir);
	}
	
	private long getCacheDirSize() throws Exception{		
		return getFileSize(this.cacheDir);
	}
	
	private void clearCacheFile(){
		
		Comparator<File> fileComparator = new Comparator<File>() {
			@Override
			public int compare(File lhs, File rhs) {
				if(lhs.lastModified() > rhs.lastModified())  
	            {  
	                return -1;  
	            }else  
	            {  
	                return 1;  
	            }  
			}
		};
		
		File[] files = this.cacheDir.listFiles();
		
		ArrayList<File> arrFiles = new ArrayList<File>();  
        for (int i = 0; i < files.length; i++) {  
            File file = files[i]; 
            if(file.isFile()){
            	arrFiles.add(file);  
            }
        }  
        
        Collections.sort(arrFiles, fileComparator);
        
        long size = 0;
        for(File f : arrFiles){
        	size += f.length();
        	f.delete();
        	
        	if(size >= REMOVE_SIZE_EVERY){
        		break;
        	}
        }
	}
	
	
	
	@Override
	public boolean save(String arg0, InputStream arg1, CopyListener arg2)
			throws IOException {
		
		try {
			if(getCacheDirSize()>=MAX_DIR_CACHE_SIZE){
				clearCacheFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.save(arg0, arg1, arg2);
	}
	
	@Override
	public boolean save(String imageUri, Bitmap bitmap) throws IOException {
		try {
			if(getCacheDirSize()>=MAX_DIR_CACHE_SIZE){
				clearCacheFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.save(imageUri, bitmap);
	}
	
//	@Override
//	protected File getFile(String imageUri) {
//		
//		String netStr=NetService.getCurrentNetType(App.getIns());
//		String[] arr = imageUri.split("@"); 
//		if(arr.length > 1){
//			imageUri = arr[0] + "_wifi";
//		}
//		
//		String fileName = this.fileNameGenerator.generate(imageUri);
//	    File dir = this.cacheDir;
//	    if ((!this.cacheDir.exists()) && (!this.cacheDir.mkdirs()) && 
//	      (this.reserveCacheDir != null) && (
//	      (this.reserveCacheDir.exists()) || (this.reserveCacheDir.mkdirs()))) {
//	      dir = this.reserveCacheDir;
//	    }
//	      
//	   File file = new File(dir, fileName);
//	   if(file.exists()){
//		   return file;
//	   }
//	
//	   fileName = this.fileNameGenerator.generate(arr[0] + "_" + netStr);
//	   
//	   return new File(dir, fileName);
//	}
	
	
	private long getFileSize(File f) throws Exception
	 {
	  long size = 0;
	  File flist[] = f.listFiles();
	  for (int i = 0; i < flist.length; i++)
	  {
	   if (flist[i].isDirectory())
	   {
	    size = size + getFileSize(flist[i]);
	   }
	   else
	   {
	    size = size + flist[i].length();
	   }
	  }
	  return size;
	 }
	

}
