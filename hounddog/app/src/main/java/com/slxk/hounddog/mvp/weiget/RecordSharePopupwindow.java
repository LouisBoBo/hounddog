package com.slxk.hounddog.mvp.weiget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.slxk.hounddog.R;

/**
 * 录音分享
 */
public class RecordSharePopupwindow extends PopupWindow {

    private Context mContext;
    private TextView tvShare;
    private onRecordShareChange deleteChange;

    public RecordSharePopupwindow(Context context){
        super(context);
        this.mContext = context;
        View root = View.inflate(context, R.layout.popup_record_share, null);
        tvShare = root.findViewById(R.id.tv_share);

        //设置SelectPicPopupWindow的View
        this.setContentView(root);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.transparent)));

        initData();
    }

    private void initData(){
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteChange != null){
                    deleteChange.onRecordShareClick();
                }
            }
        });
    }

    /**
     * 兼容 android 7.0之后设置showAsDropDown失效问题
     * @param anchor
     */
    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            int[] a = new int[2];
            anchor.getLocationInWindow(a);
            showAtLocation(anchor, Gravity.NO_GRAVITY, xoff, a[1] + anchor.getHeight() + yoff);
        } else {
            super.showAsDropDown(anchor, xoff, yoff);
        }
    }

    public void setRecordShareChange(onRecordShareChange change){
        this.deleteChange = change;
    }

    public interface onRecordShareChange{

        /**
         * 分享
         */
        void onRecordShareClick();

    }


}
