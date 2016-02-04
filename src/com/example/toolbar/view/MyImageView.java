package com.example.toolbar.view;

import com.example.toolbar.utils.DensityUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyImageView extends View {
	private Paint paint1;
	private Paint paint2;
	private Paint paint3;
	private float radius;
	private float outRadius;
	private int circle_color=0xffdddddd;
	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inti(context);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inti(context);
	}

	public MyImageView(Context context) {
		super(context);
		inti(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float X=getWidth()/2;
		float Y=getHeight()/2;
		paint2.setColor(circle_color);
		canvas.drawCircle(X, Y, radius, paint2);
		canvas.drawCircle(X, Y, outRadius, paint3);
		canvas.drawLine(X, Y-outRadius, X, 0, paint1);
		canvas.drawLine(X, Y+outRadius, X, getHeight(), paint1);
	}
	
	private void inti(Context context){
		paint1=new Paint();
		paint1.setColor(0xffdddddd);
		paint1.setAntiAlias(true);
		paint1.setStyle(Paint.Style.FILL);
		paint1.setStrokeWidth(DensityUtil.dip2px(context, 2));
//		paint1.setStrokeWidth(6);
		
		//初始化画圆的Paint
		paint2=new Paint();
		paint2.setAntiAlias(true);
		paint2.setStyle(Paint.Style.FILL);
		radius=DensityUtil.dip2px(context, 4);
//		radius=15;
		
		//初始化外圆的画笔
		paint3=new Paint();
		paint3.setAntiAlias(true);
		paint3.setStyle(Paint.Style.STROKE);
		paint3.setColor(0xffdddddd);
		outRadius=DensityUtil.dip2px(context, 8);
		paint3.setStrokeWidth(DensityUtil.dip2px(context, 2));
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = getDefaultSize(getSuggestedMinimumHeight(),heightMeasureSpec);
		int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		setMeasuredDimension(width, height);
		
	}

	/**
	 * 设置内圆的颜色
	 * @param color
	 */
	public void setColor(int color){
		circle_color=color;
//		invalidate();
		postInvalidate();
		Log.i("MyImageView", color+"");
	}
	
}
