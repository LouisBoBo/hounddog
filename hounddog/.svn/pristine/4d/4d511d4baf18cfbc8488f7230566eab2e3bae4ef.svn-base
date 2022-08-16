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
 * 蓝牙连接-取消配对 or 蓝牙升级
 */
public class BluetoothSetPopupwindow extends PopupWindow {

    private Context mContext;
    private TextView tvDisConnect;
    private TextView tvFirmwareUpgrade;
    private onBluetoothSetChange bluetoothSetChange;

    public BluetoothSetPopupwindow(Context context){
        super(context);
        this.mContext = context;
        View root = View.inflate(context, R.layout.popup_bluetooth_set, null);
        tvDisConnect = root.findViewById(R.id.tv_disconnect);
        tvFirmwareUpgrade = root.findViewById(R.id.tv_firmware_upgrade);
        initData();

        //设置SelectPicPopupWindow的View
        this.setContentView(root);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
//        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.transparent)));
    }

    private void initData(){
        tvDisConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothSetChange != null){
                    bluetoothSetChange.onBluetoothDisConnect();
                }
                dismiss();
            }
        });
        tvFirmwareUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothSetChange != null){
                    bluetoothSetChange.onFirmwareUpgrade();
                }
                dismiss();
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

    public void setBluetoothSetChange(onBluetoothSetChange change){
        this.bluetoothSetChange = change;
    }

    public interface onBluetoothSetChange{

        /**
         * 断开连接
         */
        void onBluetoothDisConnect();

        /**
         * 固件升级
         */
        void onFirmwareUpgrade();

    }

}
