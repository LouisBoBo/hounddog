package com.slxk.hounddog.mvp.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by slxk-002 on 2017/3/1.
 */

public class CharsFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence charSequence, int start, int end, Spanned dest, int dstart, int dend) {

        charSequence= charSequence.toString().replace(",","");
        charSequence= charSequence.toString().replace("\'","");
        charSequence= charSequence.toString().replace("\"","");
        charSequence= charSequence.toString().replace(":","");
        charSequence= charSequence.toString().replace(";","");
        charSequence= charSequence.toString().replace(".","");
        charSequence= charSequence.toString().replace("?","");
        charSequence=charSequence.toString().replace("!","");
        charSequence=charSequence.toString().replace("/","");
        charSequence=charSequence.toString().replace("\\","");

        return charSequence;
    }
}
