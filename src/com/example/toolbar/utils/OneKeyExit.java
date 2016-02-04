package com.example.toolbar.utils;

import android.os.Handler;
import android.os.HandlerThread;

public class OneKeyExit {
	 private boolean isExit = false; 
     private Runnable task = new Runnable() { 
         @Override 
         public void run() { 
             isExit = false; 
         } 
     }; 
   
     public  void doExitInOneSecond() { 
         isExit = true; 
         HandlerThread thread = new HandlerThread("doTask"); 
         thread.start(); 
         new Handler(thread.getLooper()).postDelayed(task, 1500); 
     } 
       
     public boolean isExit() { 
         return isExit; 
     }  
   
     public void setExit(boolean isExit) { 
         this.isExit = isExit; 
     } 

}
