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
import com.slxk.hounddog.mvp.utils.ResultDataUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 设备列表
 */
public class DeviceListAdapter extends BaseQuickAdapter<DeviceModel, BaseViewHolder> {

    private onDeviceChange deviceChange;

    public DeviceListAdapter(int layoutResId, @Nullable List<DeviceModel> data) {
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
        switch (item.getState()) {
            case ResultDataUtils.Device_State_Line_Down:
                tvState.setText(mContext.getString(R.string.state_line_down));
                tvState.setTextColor(mContext.getResources().getColor(R.color.color_999999));
                break;
            case ResultDataUtils.Device_State_Line_On:
                tvState.setText(mContext.getString(R.string.state_line_on));
                tvState.setTextColor(mContext.getResources().getColor(R.color.color_2ABA5A));
                break;
            case ResultDataUtils.Device_State_Line_Sleep:
                tvState.setText(mContext.getString(R.string.state_line_static));
                tvState.setTextColor(mContext.getResources().getColor(R.color.color_FF2F25));
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
