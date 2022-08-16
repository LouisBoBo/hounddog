package com.slxk.hounddog.mvp.ui.view.data;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.slxk.hounddog.mvp.ui.view.data.haibin.Calendar;
import com.slxk.hounddog.mvp.ui.view.data.haibin.MultiMonthView;


public class TrackCustomMultiMonthView extends MultiMonthView {
    private int mRadius;

    public TrackCustomMultiMonthView(Context context) {
        super(context);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme,
                                     boolean isSelectedPre, boolean isSelectedNext) {

        mSelectedPaint.setStyle(Paint.Style.STROKE);
        mSelectedPaint.setStrokeWidth(3);
        RectF rect = new RectF();
        rect.set(x + 15, y + 15, x + mItemWidth - 15, y + mItemHeight -15);
        float rx = 6;
        float ry = 6;
        canvas.drawRoundRect(rect,rx,ry,mSelectedPaint);

        if(hasScheme){
            mSelectedPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x + mItemWidth - 25, y + mItemHeight -25, 10, mSelectedPaint);
        }

        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y, boolean isSelected) {

        mSchemePaint.setStyle(Paint.Style.STROKE);
        mSchemePaint.setStrokeWidth(3);

        mSelectedPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x + mItemWidth - 25, y + mItemHeight -25, 10, mSelectedPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        if (calendar.isWeekend() && calendar.isCurrentMonth()) {
            mCurMonthTextPaint.setColor(0xFF489dff);
            mCurMonthLunarTextPaint.setColor(0xFF489dff);
            mSchemeTextPaint.setColor(0xFF489dff);
            mSchemeLunarTextPaint.setColor(0xFF489dff);
            mOtherMonthLunarTextPaint.setColor(0xFF489dff);
            mOtherMonthTextPaint.setColor(0xFF489dff);
            mCurDayLunarTextPaint.setColor(0xFF489dff);
            mCurDayLunarTextPaint.setColor(0xFF489dff);
        } else {
            mCurMonthTextPaint.setColor(0xff333333);
            mCurMonthLunarTextPaint.setColor(0xffCFCFCF);
            mSchemeTextPaint.setColor(0xff333333);
            mSchemeLunarTextPaint.setColor(0xffCFCFCF);

            mOtherMonthTextPaint.setColor(0xFFe1e1e1);
            mOtherMonthLunarTextPaint.setColor(0xFFe1e1e1);
        }


        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 8;
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, calendar.isCurrentDay() ? mCurDayTextPaint : mSelectTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, calendar.isCurrentDay() ? mCurDayLunarTextPaint : mSelectedLunarTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, calendar.isCurrentDay() ? mCurDayTextPaint : calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSchemeLunarTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, calendar.isCurrentDay() ? mCurDayTextPaint : calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, calendar.isCurrentDay() ? mCurDayLunarTextPaint : mCurMonthLunarTextPaint);
        }
    }
}
