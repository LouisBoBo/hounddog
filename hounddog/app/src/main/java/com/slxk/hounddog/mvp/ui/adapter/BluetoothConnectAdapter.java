package com.slxk.hounddog.mvp.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.BluetoothBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 已连接的接收机
 */
public class BluetoothConnectAdapter extends BaseQuickAdapter<BluetoothBean, BaseViewHolder> {

    public BluetoothConnectAdapter(int layoutResId, @Nullable List<BluetoothBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BluetoothBean item) {
        helper.setText(R.id.tv_bluetooth, item.getName());
        helper.addOnClickListener(R.id.iv_set);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }
}
