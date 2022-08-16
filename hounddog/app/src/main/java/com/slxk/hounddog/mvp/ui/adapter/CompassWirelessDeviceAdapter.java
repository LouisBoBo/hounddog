package com.slxk.hounddog.mvp.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.slxk.hounddog.R;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.mvp.model.bean.ColorBean;
import com.slxk.hounddog.mvp.ui.view.MarkView;
import com.slxk.hounddog.mvp.utils.DeviceUtils;
import com.slxk.hounddog.mvp.utils.MyDisplayMetrics;
import com.slxk.hounddog.mvp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 罗盘-设备列表
 */
public class CompassWirelessDeviceAdapter extends BaseQuickAdapter<DeviceModel, BaseViewHolder> {

    private ArrayList<ColorBean> mColorLists; // 设备颜色池

    public CompassWirelessDeviceAdapter(int layoutResId, @Nullable List<DeviceModel> data,Context context,ArrayList<ColorBean> colorBeans) {
        super(layoutResId, data);
        mColorLists = colorBeans;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(@NonNull BaseViewHolder helper, DeviceModel item) {
        ImageView ivState = helper.getView(R.id.iv_state);
        TextView tvName = helper.getView(R.id.tv_name);
        TextView tvBattery = helper.getView(R.id.tv_battery);
        TextView tvDistance = helper.getView(R.id.tv_distance);
        RelativeLayout marklayout = helper.getView(R.id.mark_view);
        marklayout.setBackgroundResource(R.drawable.device_mark);
        int state = 2; // 设备状态，0：静止，1：在线，2：离线
        if (!TextUtils.isEmpty(item.getDev_state())){
            String strFormat = DeviceUtils.onLocationType(item.getDev_state());
            char a6 = strFormat.charAt(1); // 设备状态
            if ("1".equals(String.valueOf(a6))) {
                state = 1;
            } else {
                state = 0;
            }
            if (DeviceUtils.isDeviceOnOff(item.getCommunication_time())) {
                state = 2;
            }
        }
        switch (state) {
            case 0:
                ivState.setImageResource(R.drawable.icon_device_line_sleep);
                break;
            case 1:
                ivState.setImageResource(R.drawable.icon_device_line_on);
                break;
            case 2:
                ivState.setImageResource(R.drawable.icon_device_off);
                break;
        }
        if (!TextUtils.isEmpty(item.getDevice_name())) {
            tvName.setText(item.getDevice_name());
        } else {
            tvName.setText(item.getImei());
        }
        tvBattery.setText(mContext.getString(R.string.battery) + item.getPower() + "%");
        if (item.getDistance() == 0){
            tvDistance.setText(mContext.getString(R.string.distance) + "：" + mContext.getString(R.string.not));
        }else{
            tvDistance.setText(mContext.getString(R.string.distance) + DeviceUtils.getDeviceDistance(mContext, item.getDistance()));
        }
        if (item.getLat() != 0 && item.getLon() != 0){
            if (!TextUtils.isEmpty(item.getColor())){
                tvName.setTextColor(Color.parseColor(item.getColor()));
                tvBattery.setTextColor(Color.parseColor(item.getColor()));
                tvDistance.setTextColor(Color.parseColor(item.getColor()));
            }else{
                tvName.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                tvBattery.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                tvDistance.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            }
        }else{
            tvName.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            tvBattery.setTextColor(mContext.getResources().getColor(R.color.color_999999));
            tvDistance.setTextColor(mContext.getResources().getColor(R.color.color_999999));
        }

        //item添加勾选框
        marklayout.removeAllViews();

        if(item.isIs_select()){
            int id = R.color.red;
            if (!TextUtils.isEmpty(item.getColor())) {
                for (ColorBean bean : mColorLists) {
                    if (bean.getColor().equals(item.getColor())) {
                        id = bean.getColorId();
                        break;
                    }
                }
            }

            MarkView triangleTransparent = new MarkView(mContext, R.color.transparent);
            // 添加三角形父布局
            LinearLayout view = new LinearLayout(mContext);
            view.setOrientation(LinearLayout.VERTICAL);
            // 添加三角形
            MarkView triangleView = new MarkView(mContext, id);
            view.addView(triangleView);
            view.addView(triangleTransparent);
            marklayout.addView(view);

            GradientDrawable shapeDrawable  = (GradientDrawable) marklayout.getBackground();
            shapeDrawable.setStroke((int) MyDisplayMetrics.dpToPxInt(1),marklayout.getResources().getColor(id));

        }else {
            GradientDrawable shapeDrawable  = (GradientDrawable) marklayout.getBackground();
            shapeDrawable.setStroke((int) MyDisplayMetrics.dpToPxInt(1),marklayout.getResources().getColor(R.color.color_D7D7D7));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

}
