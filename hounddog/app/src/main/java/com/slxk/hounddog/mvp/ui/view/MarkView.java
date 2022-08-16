package com.slxk.hounddog.mvp.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.utils.MyDisplayMetrics;

public class MarkView extends View {
    private static final int TOP = 0;
    private static final int BOTTOM = 1;
    private static final int RIGHT = 2;
    private static final int LEFT = 3;
    private static final int DEFUALT_WIDTH = 20;
    private static final int DEFUALT_HEIGHT = 20;
    private int DEFUALT_COLOR = R.color.red;
    private Paint mPaint;
    private int mColor;
    private int mWidth;
    private int mHeight;
    private int mDirection;
    private Path mPath;

    public MarkView(Context context, int colorId) {
        this(context, null);
        init(context, null, colorId);
    }

    public MarkView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, R.color.red);
    }

    private void init(Context context, AttributeSet attrs, int colorId){
        DEFUALT_COLOR = colorId;
        initPaint();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TriangleView, 0, 0);
        mColor = typedArray.getColor(R.styleable.TriangleView_trv_color, ContextCompat.getColor(getContext(), DEFUALT_COLOR));
        mDirection = typedArray.getInt(R.styleable.TriangleView_trv_direction, mDirection);
        typedArray.recycle();
        mPaint.setColor(mColor);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
        mDirection = TOP;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (mWidth == 0 || widthMode != MeasureSpec.EXACTLY) {
            mWidth = MyDisplayMetrics.dpToPxInt(DEFUALT_WIDTH);
        }
        if (mHeight == 0 || heightMode != MeasureSpec.EXACTLY) {
            mHeight = MyDisplayMetrics.dpToPxInt(DEFUALT_HEIGHT);
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mDirection) {
            case TOP:

                mPath.moveTo(2,mHeight/2f+10);
                mPath.lineTo(mWidth/2f,mHeight);
                mPath.lineTo(mWidth,5);
                mPath.lineTo(mWidth-5,0);
                mPath.lineTo(mWidth/2f-2,mHeight-12);
                mPath.lineTo(2,mHeight/2+10 -10);
                mPath.lineTo(2,mHeight/2f+10);

                break;
            case BOTTOM:
                mPath.moveTo(0, 0);
                mPath.lineTo(mWidth / 2f, mHeight);
                mPath.lineTo(mWidth, 0);
                break;
            case RIGHT:
                mPath.moveTo(0, 0);
                mPath.lineTo(0, mHeight);
                mPath.lineTo(mWidth, mHeight / 2f);
                break;
            case LEFT:
                mPath.moveTo(0, mHeight / 2f);
                mPath.lineTo(mWidth, mHeight);
                mPath.lineTo(mWidth, 0);
                break;
            default:
                break;
        }

        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }
}
