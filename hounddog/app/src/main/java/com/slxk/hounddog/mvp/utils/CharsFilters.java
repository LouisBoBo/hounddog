package com.slxk.hounddog.mvp.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by slxk-002 on 2017/3/1.
 */


/** 限制是能输入中英文数字 */
public class CharsFilters implements InputFilter {
    @Override
    public CharSequence filter(CharSequence charSequence, int start, int end, Spanned dest, int dstart, int dend) {

        return Utils.compileExChar2(charSequence.toString());

    }
}
