package com.slxk.hounddog.mvp.ui.adapter;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.LocationModeTime;
import com.slxk.hounddog.mvp.utils.DeviceUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 定位模式
 */
public class LocationModeAdapter extends BaseQuickAdapter<LocationModeTime, BaseViewHolder> {

    public LocationModeAdapter(int layoutResId, @Nullable List<LocationModeTime> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LocationModeTime item) {
        helper.setText(R.id.tv_mode_type, item.getName());
        helper.setText(R.id.tv_mode_time, DeviceUtils.onLocationModeTimeShow(mContext, item.getSmode_value()));
        helper.addOnClickListener(R.id.ll_mode_time);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

}
