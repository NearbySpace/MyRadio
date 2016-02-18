package com.example.toolbar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyVisualizerView extends View {
	// bytes数组保存了波形抽样点的值
	private byte[] bytes;
	private float[] points;
	private Paint paint = new Paint();
	private Rect rect = new Rect();
	private byte type = 1;
	private static final int DN_W = 470;//view宽度与单个音频块占比 - 正常480 需微调
    private static final int DN_H = 180;//view高度与单个音频块占比
    private static final int DN_SL = 15;//单个音频块宽度
    private static final int DN_SW = 5;//单个音频块高度

    private int hgap = 0;
    private int vgap = 0;
    private int levelStep = 0;
    private float strokeWidth = 0;
    private float strokeLength = 0;
    
    protected final static int MAX_LEVEL = 20;//音量柱·音频块 - 最大个数

    protected final static int CYLINDER_NUM = 15;//音量柱 - 最大个数

    protected Visualizer mVisualizer = null;//频谱器

    protected Paint mPaint = null;//画笔

    protected byte[] mData = {8,8,8,8,8,8,8,8,8,8,8,8,8,8,8};//音量柱 数组(每个能量柱的音频块数)

    boolean mDataEn = true;
    
	public MyVisualizerView(Context context) {
		super(context);
		initView();
	}

	public MyVisualizerView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public MyVisualizerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
//		bytes = null;
//		// 设置画笔的属性
//		paint.setStrokeWidth(1f);
//		paint.setAntiAlias(true);// 抗锯齿
//		paint.setColor(Color.WHITE);// 画笔颜色
//		paint.setStyle(Paint.Style.FILL);
		
		mPaint = new Paint();//初始化画笔工具
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setColor(Color.WHITE);//画笔颜色

	}

	public void updateVisualizer(byte[] ftt)
    {
    	mData = ftt;
        // 通知该组件重绘自己。
        invalidate();
    }
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		float w, h, xr, yr;

        w = right - left;
        h = bottom - top;
        xr = w / (float) DN_W;
        yr = h / (float) DN_H;

        strokeWidth = DN_SW * yr;
        strokeLength = DN_SL * xr;
        hgap = (int) ((w - strokeLength * CYLINDER_NUM) / (CYLINDER_NUM + 1));
        vgap = (int) (h / (MAX_LEVEL + 2));

        mPaint.setStrokeWidth(strokeWidth); //设置频谱块宽度
	}
	
	//绘制频谱块和倒影
    protected void drawCylinder(Canvas canvas, float x, byte value) {
        if (value <= 0) value = 1;//最少有一个频谱块

        for (int i = 0; i < value; i++) { //每个能量柱绘制value个能量块
            float y = (getHeight() - i * vgap - vgap) - 10;//计算y轴坐标

            //绘制频谱块
            mPaint.setColor(Color.WHITE);//画笔颜色
            canvas.drawLine(x, y, (x + strokeLength), y, mPaint);//绘制频谱块

            //绘制音量柱倒影
//            if (i <= 6 && value > 0) {
//                mPaint.setColor(Color.WHITE);//画笔颜色
//                mPaint.setAlpha(100 - (100 / 6 * i));//倒影颜色
//                canvas.drawLine(x, -y + 210, (x + strokeLength), -y + 210, mPaint);//绘制频谱块
//            }
        }
    }
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < CYLINDER_NUM; i++) { //绘制15个能量柱
            drawCylinder(canvas, strokeWidth / 2 + hgap + i * (hgap + strokeLength), mData[i]);
//            drawCylinder(canvas, 5, mData[i]);
        }
//		if (bytes == null) {
//			return;
//		}
//		// 绘制白色背景
//		// canvas.drawColor(Color.WHITE);
//		// 使用rect对象记录该组件的宽度和高度
//		rect.set(0, 0, getWidth(), getHeight());
//		switch (type) {
//		// -------绘制块状的波形图-------
//		case 0:
//			for (int i = 0; i < bytes.length - 1; i++) {
//				float left = getWidth() * i / (bytes.length - 1);
//				// 根据波形值计算该矩形的高度
//				float top = rect.height() - (byte) (bytes[i + 1] + 128)
//						* rect.height() / 128;
//				float right = left + 1;
//				float bottom = rect.height();
//				canvas.drawRect(left, top, right, bottom, paint);
//			}
//			break;
//		// -------绘制柱状的波形图（每隔18个抽样点绘制一个矩形）-------
//		case 1:
//			 for (int i = 0; i < bytes.length - 1; i += 18)
//			 {
//			 float left = rect.width()*i/(bytes.length - 1);
//			 // 根据波形值计算该矩形的高度
//			 float top = rect.height()-(byte)(bytes[i+1]+128)
//			 * rect.height() / 128;
//			 float right = left + 6;
//			 float bottom = rect.height();
//			 canvas.drawRect(left, top, right, bottom, paint);
//			 System.out.println("bytes.length:"+bytes.length);
//			 Log.i("MyVisualizerVeiw", bytes[i+1]+"");
//			 }
////			for (int i = 0; i < bytes.length - 1; i += 1) {
////				float left = rect.width() * i / (bytes.length - 1);
////				float top = rect.height() - (byte) (bytes[i + 1] + 128)
////						* rect.height() / 128;
////				float right = left + 6;
////				float bottom = rect.height();
////				canvas.drawRect(left, top, right, bottom, paint);
////			}
//			break;
//		// -------绘制曲线波形图-------
//		case 2:
//			// 如果point数组还未初始化
//			if (points == null || points.length < bytes.length * 4) {
//				points = new float[bytes.length * 4];
//			}
//			for (int i = 0; i < bytes.length - 1; i++) {
//				// 计算第i个点的x坐标
//				points[i * 4] = rect.width() * i / (bytes.length - 1);
//				// 根据bytes[i]的值（波形点的值）计算第i个点的y坐标
//				points[i * 4 + 1] = (rect.height() / 2)
//						+ ((byte) (bytes[i] + 128)) * 128 / (rect.height() / 2);
//				// 计算第i+1个点的x坐标
//				points[i * 4 + 2] = rect.width() * (i + 1) / (bytes.length - 1);
//				// 根据bytes[i+1]的值（波形点的值）计算第i+1个点的y坐标
//				points[i * 4 + 3] = (rect.height() / 2)
//						+ ((byte) (bytes[i + 1] + 128)) * 128
//						/ (rect.height() / 2);
//			}
//			// 绘制波形曲线
//			canvas.drawLines(points, paint);
//			break;
//		}
	}
}
