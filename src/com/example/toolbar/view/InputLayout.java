package com.example.toolbar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by hxc 2014/12/10. 
 * 用于监听键盘弹出和收起的改变
 */
public class InputLayout extends LinearLayout {

    private OnKeyBoardShowListener keyBoardListener;

    public InputLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public InputLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec,
                             final int heightMeasureSpec) {
        if (keyBoardListener != null) {
            final int newSpec = View.MeasureSpec.getSize(heightMeasureSpec);//新布局的高度
            final int oldSpec = getMeasuredHeight();//旧布局的高度
            // If layout became smaller, that means something forced it to
            // resize. Probably soft keyboard :)
            if (oldSpec >= newSpec) {
                keyBoardListener.onShown();
            } else {
                keyBoardListener.onHidden();
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setKeyBoardListener(OnKeyBoardShowListener keyBoardListener) {
        this.keyBoardListener = keyBoardListener;
    }

    // Simplest possible listener :)
    public interface OnKeyBoardShowListener {
        public void onShown();

        public void onHidden();
    }
}
