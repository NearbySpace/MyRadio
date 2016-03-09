package com.example.toolbar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MyToast {
	/** 
     * 窗体管理者 
     */  
    public static WindowManager wm;  
    private static WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();  
//    private static View mView;  
    private static TextView tv;  
    
    /** 
     * 显示自定义吐司 
     *  
     * @param info 
     * @param context 
     */  
    public static void show(String message, Context context) {  
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
        tv = new TextView(context);  
        tv.setText(message);  
        tv.setTextSize(17); 
        tv.setBackgroundColor(Color.BLACK);
        tv.setTextColor(Color.WHITE);
        // 原来TN所做的工作  
        WindowManager.LayoutParams params = mParams;  
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;  
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;  
        params.format = PixelFormat.TRANSLUCENT;  
        params.type = WindowManager.LayoutParams.TYPE_TOAST;  
        params.setTitle("Toast");  
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON  
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;  
  
        wm.addView(tv, params); 
  
    }  
    
    public static void removeView(){
    	 wm.removeView(tv);
    }
    
}
