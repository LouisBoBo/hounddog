package com.slxk.hounddog.mvp.utils;

import android.widget.Toast;

import com.slxk.hounddog.app.MyApplication;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Toast工具类
 */
public class ToastUtils {
    /**
     */
    @IntDef({LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    /**
     * Show the view or text notification for a short period of time.  This time
     * could be user-definable.  This is the default.
     */
    public static final int LENGTH_SHORT = 0;

    /**
     * Show the view or text notification for a long period of time.  This time
     * could be user-definable.
     */
    public static final int LENGTH_LONG = 1;

    /**
     * 显示Toast
     *
     * @param message
     */
    public static void show(String message) {
        Toast.makeText(MyApplication.getMyApp(), message, Toast.LENGTH_SHORT).show();
    }

}
