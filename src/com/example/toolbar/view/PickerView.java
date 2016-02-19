package com.example.toolbar.view;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.strawberryradio.R;
import com.example.toolbar.bean.PickerBean;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PickerView extends View {

	public static final String TAG = "PickerView";
	/**
	 * text之间间距和minTextSize之比
	 */
	public static final float MARGIN_ALPHA = 1.8f;
//	public static final float MARGIN_ALPHA = 2.8f;
	/**
	 * 自动回滚到中间的速度
	 */
	public static final float SPEED = 2;

	// private List<String> mDataList;
	/**
	 * 选中的位置，这个位置是mDataList的中心位置，一直不变
	 */
	private int mCurrentSelected;
	private Paint mPaint;

	private float mMaxTextSize = 70;
	private float mMinTextSize = 40;

	private float mMaxTextAlpha = 255;
	private float mMinTextAlpha = 120;

	private int mColorText = 0xff0088a8;

	private int mViewHeight;
	private int mViewWidth;

	private float mLastDownY;
	/**
	 * 滑动的距离
	 */
	private float mMoveLen = 0;
	private boolean isInit = false;
	private onSelectListener mSelectListener;
	private Timer timer;
	private MyTimerTask mTask;
	private PickerBean mPickerBean;

	Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (Math.abs(mMoveLen) < SPEED) {
				mMoveLen = 0;
				if (mTask != null) {
					mTask.cancel();
					mTask = null;
					performSelect();
				}
			} else
				// 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
				mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
			invalidate();
		}

	};

	public PickerView(Context context) {
		super(context);
		init();
	}

	public PickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void setOnSelectListener(onSelectListener listener) {
		mSelectListener = listener;
	}

	private void performSelect() {
		if (mSelectListener != null)
			// mSelectListener.onSelect(mDataList.get(mCurrentSelected));
			mSelectListener.onSelect(
					mPickerBean.list.get(mCurrentSelected).title,
					mPickerBean.list.get(mCurrentSelected).id);
	}

	public void setData(PickerBean pickerData) {
		mPickerBean = pickerData;
		mCurrentSelected = pickerData.list.size() / 2;
		// mDataList = datas;
		// mCurrentSelected = datas.size() / 2;
		invalidate();
	}
	
	/**
	 * 选择选中的item的index
	 * 
	 * @param selected
	 */
	public void setSelected(int selected) {
		mCurrentSelected = selected;
		// int distance = mDataList.size() / 2 - mCurrentSelected;
		int distance = mPickerBean.list.size() / 2 - mCurrentSelected;
		if (distance < 0)
			for (int i = 0; i < -distance; i++) {
				moveHeadToTail();
				mCurrentSelected--;
			}
		else if (distance > 0)
			for (int i = 0; i < distance; i++) {
				moveTailToHead();
				mCurrentSelected++;
			}
		invalidate();
	}
	

	/**
	 * 选择选中的内容
	 * 
	 * @param mSelectItem
	 */
	public void setSelected(String mSelectItem) {
		for (int i = 0; i < mPickerBean.list.size(); i++)
			if (mPickerBean.list.get(i).title.equals(mSelectItem)) {
				setSelected(i);
				break;
			}
	}

	private void moveHeadToTail() {
		PickerBean.Classify head = mPickerBean.list.get(0);
		mPickerBean.list.remove(0);
		mPickerBean.list.add(head);
	}

	private void moveTailToHead() {
		PickerBean.Classify tail = mPickerBean.list
				.get(mPickerBean.list.size() - 1);
		mPickerBean.list.remove(mPickerBean.list.size() - 1);
		mPickerBean.list.add(0, tail);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mViewHeight = getMeasuredHeight();
		mViewWidth = getMeasuredWidth();
		// 按照View的高度计算字体大小
		mMaxTextSize = mViewHeight / 4.0f;
		mMinTextSize = mMaxTextSize / 2f;
		isInit = true;
		invalidate();
	}

	private void init() {
		timer = new Timer();
		mPickerBean = new PickerBean();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Style.FILL);
		mPaint.setTextAlign(Align.CENTER);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 根据index绘制view
		if (isInit)
			drawData(canvas);
	}

	private void drawData(Canvas canvas) {
		// 先绘制选中的text再往上往下绘制其余的text
		float scale = parabola(mViewHeight / 4.0f, mMoveLen);
//		float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
		float size = (mMaxTextSize - mMinTextSize) * scale + 20;
		mPaint.setTextSize(size);
//		Log.i("PickerView", "size值："+size);
		mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
		mPaint.setColor(0xffffaa33);
		mPaint.setFakeBoldText(true);
		// text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
		float x = (float) (mViewWidth / 2.0);
		float y = (float) (mViewHeight / 2.0 + mMoveLen);
		FontMetricsInt fmi = mPaint.getFontMetricsInt();
		float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
		if (mPickerBean.list == null) {
			Log.i(TAG, "mPickerBean.list是空");
			return;
		}
		canvas.drawText(mPickerBean.list.get(mCurrentSelected).title+"频道", x,
				baseline, mPaint);
		
		// 绘制上方data
		for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
			drawOtherText(canvas, i, -1);
		}
		// 绘制下方data
		for (int i = 1; (mCurrentSelected + i) < mPickerBean.list.size(); i++) {
			drawOtherText(canvas, i, 1);
		}
		
//		for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
//			drawOtherText(canvas, i-1, -2);
//		}

	}

	/**
	 * @param canvas
	 * @param position
	 *            距离mCurrentSelected的差值
	 * @param type
	 *            1表示向下绘制，-1表示向上绘制
	 */
	private void drawOtherText(Canvas canvas, int position, int type) {
		float d = (float) (MARGIN_ALPHA * mMinTextSize * position + type
				* mMoveLen);
		float scale = parabola(mViewHeight / 4.0f, d);
		float size = (mMaxTextSize - mMinTextSize) * scale + 30;
		mPaint.setTextSize(size);
		mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
		mPaint.setColor(mColorText);
//		mPaint.setFakeBoldText(true);
		float y = (float) (mViewHeight / 2.0 + type * d);
		FontMetricsInt fmi = mPaint.getFontMetricsInt();
		float baseline ;
		if(position == 1){
			baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0))+10*type;
		}else{
			baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
		}
		Log.i(TAG, "type: "+type+"-------->position:"+ type * position);
		canvas.drawText(
				mPickerBean.list.get(mCurrentSelected + type * position).title+"频道",
				(float) (mViewWidth / 2.0), baseline, mPaint);
	}
	
	

	/**
	 * 抛物线
	 * 
	 * @param zero
	 *            零点坐标
	 * @param x
	 *            偏移量
	 * @return scale
	 */
	private float parabola(float zero, float x) {
		float f = (float) (1 - Math.pow(x / zero, 2));
		return f < 0 ? 0 : f;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			doDown(event);
			break;
		case MotionEvent.ACTION_MOVE:
			doMove(event);
			break;
		case MotionEvent.ACTION_UP:
			doUp(event);
			break;
		}
		return true;
	}

	private void doDown(MotionEvent event) {
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}
		mLastDownY = event.getY();
	}

	private void doMove(MotionEvent event) {

		mMoveLen += (event.getY() - mLastDownY);

		if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
			// 往下滑超过离开距离
			moveTailToHead();
			mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
		} else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
			// 往上滑超过离开距离
			moveHeadToTail();
			mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
		}

		mLastDownY = event.getY();
		invalidate();
	}

	private void doUp(MotionEvent event) {
		// 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
		if (Math.abs(mMoveLen) < 0.0001) {
			mMoveLen = 0;
			return;
		}
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}
		mTask = new MyTimerTask(updateHandler);
		timer.schedule(mTask, 0, 10);
	}

	class MyTimerTask extends TimerTask {
		Handler handler;

		public MyTimerTask(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			handler.sendMessage(handler.obtainMessage());
		}

	}

	public interface onSelectListener {
		void onSelect(String title, String classifyId);
	}
}
