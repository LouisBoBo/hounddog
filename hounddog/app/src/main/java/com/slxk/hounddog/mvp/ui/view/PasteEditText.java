package com.slxk.hounddog.mvp.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 自定义EditText用来监听粘贴事件
 */
public class PasteEditText extends AppCompatEditText {

    private OnPasteCallback mOnPasteCallback;

    public PasteEditText(Context context) {
        super(context);
    }

    public PasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        switch (id) {
            case android.R.id.cut:
                // 剪切
            case android.R.id.copy:
                // 复制
            case android.R.id.paste:
                // 粘贴
                if (mOnPasteCallback != null) {
                    mOnPasteCallback.onEditPaste();
                }
                break;
        }
        return super.onTextContextMenuItem(id);
    }

    public void setOnPasteCallback(OnPasteCallback onPasteCallback) {
        mOnPasteCallback = onPasteCallback;
    }

    public interface OnPasteCallback {

        /**
         * 粘贴动作
         */
        void onEditPaste();

    }

}
