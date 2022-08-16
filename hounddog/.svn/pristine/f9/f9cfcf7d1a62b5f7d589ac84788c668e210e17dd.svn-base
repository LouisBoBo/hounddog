package com.slxk.hounddog.mvp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * @date 2020/10/5
 * description:
 */
public class WarpView extends LinearLayout {

    public WarpView(Context context) {
        super(context);
    }

    public WarpView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WarpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        Log.i("DMUI", "--------------------------------");
//
//        int with = MeasureSpec.getSize(widthMeasureSpec);
//        Log.i("DMUI", "onMeasure with: " + with);
//
//        for (int i = 0; i < getChildCount(); i++) {
//            View view = getChildAt(i);
//            Log.i("DMUI", "onMeasure width: " + view.getMeasuredWidth());
//            Log.i("DMUI", "onMeasure height: " + view.getMeasuredHeight());
//        }

        if (getChildCount() == 2 && getChildAt(0) instanceof TextView && getChildAt(1) instanceof TextView) {
            TextView textTv = (TextView) getChildAt(0);
            TextView iconTv = (TextView) getChildAt(1);
//            Log.i("DMUI", "onMeasure width: " + textTv.getMeasuredWidth());
            if (getMeasuredWidth() - iconTv.getMeasuredWidth() < textTv.getMeasuredWidth()) {
                int newWidthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - iconTv.getMeasuredWidth(), MeasureSpec.EXACTLY);
                measureChild(textTv, newWidthSpec, heightMeasureSpec);
//                Log.i("DMUI", "onMeasure new width: " + textTv.getMeasuredWidth());
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        Log.i("DMUI", "++++++++++++++++++++++++++");
//        Log.i("DMUI", "onLayout with: " + getWidth());
//        for (int i = 0; i < getChildCount(); i++) {
//            View view = getChildAt(i);
//            Log.i("DMUI", "onLayout width: " + view.getWidth());
//            Log.i("DMUI", "onLayout height: " + view.getHeight());
//        }

    }
}
