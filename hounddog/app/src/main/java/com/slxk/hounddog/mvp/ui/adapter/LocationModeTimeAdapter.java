package com.slxk.hounddog.mvp.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.LocationModeTime;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 定位时间选择
 */
public class LocationModeTimeAdapter extends BaseQuickAdapter<LocationModeTime.ListBean, BaseViewHolder> {

    public LocationModeTimeAdapter(int layoutResId, @Nullable List<LocationModeTime.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LocationModeTime.ListBean item) {
        helper.setText(R.id.tv_time, item.getS());
        View view = helper.getView(R.id.view);
        view.setVisibility(item.isEnd() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }
}
