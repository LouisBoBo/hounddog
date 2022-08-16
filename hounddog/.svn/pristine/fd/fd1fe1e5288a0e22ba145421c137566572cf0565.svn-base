package com.slxk.hounddog.mvp.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.slxk.hounddog.R;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.mvp.utils.DeviceUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 设备列表
 */
public class DeviceListWirelessAdapter extends BaseQuickAdapter<DeviceModel, BaseViewHolder> {

    private onDeviceChange deviceChange;

    public DeviceListWirelessAdapter(int layoutResId, @Nullable List<DeviceModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DeviceModel item) {
        if (!TextUtils.isEmpty(item.getDevice_name())){
            helper.setText(R.id.tv_imei, item.getDevice_name());
        }else{
            helper.setText(R.id.tv_imei, item.getImei());
        }
        TextView tvState = helper.getView(R.id.tv_state);
        int state = 2; // 设备状态，0：静止，1：在线，2：离线
        if (!TextUtils.isEmpty(item.getDev_state())) {
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
        switch (state){
            case 0:
                tvState.setText(mContext.getString(R.string.state_line_static));
                tvState.setTextColor(mContext.getResources().getColor(R.color.color_FF2F25));
                break;
            case 1:
                tvState.setText(mContext.getString(R.string.state_line_on));
                tvState.setTextColor(mContext.getResources().getColor(R.color.color_2ABA5A));
                break;
            case 2:
                tvState.setText(mContext.getString(R.string.state_line_down));
                tvState.setTextColor(mContext.getResources().getColor(R.color.color_999999));
                break;
        }
        SwipeMenuLayout swipeMenuLayout = helper.getView(R.id.swipe_menu_layout);
        Button btnDelete = helper.getView(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceChange != null){
                    deviceChange.onDeviceDelete(item.getImei(), item.getSimei(), item.getDevice_name());
                }
                swipeMenuLayout.quickClose();
            }
        });

        helper.addOnClickListener(R.id.tv_imei);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    public void setDeviceChange(onDeviceChange change){
        this.deviceChange = change;
    }

    public interface onDeviceChange{

        /**
         * 删除设备
         * @param imei 需要删除的设备号
         */
        void onDeviceDelete(String imei, String simei, String name);

    }

}
