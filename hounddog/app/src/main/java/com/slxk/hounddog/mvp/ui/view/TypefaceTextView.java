package com.slxk.hounddog.mvp.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.slxk.hounddog.R;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 自定义TextView
 */
public class TypefaceTextView extends AppCompatTextView {

    public TypefaceTextView(Context context) {
        this(context, null);
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypefaceTextView(context, attrs);
    }

    private void initTypefaceTextView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);
        String type = typedArray.getString(R.styleable.TypefaceTextView_typeface);
        if (type == null) {
            return;
        }
        Typeface typeface;
        // 根据attr属性，设置对应的字体包
        switch (type) {
            case "DINProBlack":
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DINPro-Black.otf");
                setTypeface(typeface);
                break;
            case "DINProBold":
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DINPro-Bold.otf");
                setTypeface(typeface);
                break;
            case "DINProLight":
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DINPro-Light.otf");
                setTypeface(typeface);
                break;
            case "DINProMedium":
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DINPro-Medium.otf");
                setTypeface(typeface);
                break;
            case "DINProRegular":
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DINPro-Regular.otf");
                setTypeface(typeface);
                break;
            case "TencentSansW3":
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/TencentSans-W3.otf");
                setTypeface(typeface);
                break;
            case "TencentSansW7":
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/TencentSans-W7.otf");
                setTypeface(typeface);
                break;
            default:
                break;
        }
        typedArray.recycle();
        typeface = null;
    }
}
