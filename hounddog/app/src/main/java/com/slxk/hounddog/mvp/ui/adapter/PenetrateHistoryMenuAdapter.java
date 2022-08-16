package com.slxk.hounddog.mvp.ui.adapter;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.SMSListMenuBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 透传历史回复列表
 */
public class PenetrateHistoryMenuAdapter extends BaseQuickAdapter<SMSListMenuBean, BaseViewHolder> {

    public PenetrateHistoryMenuAdapter(int layoutResId, @Nullable List<SMSListMenuBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SMSListMenuBean item) {
        helper.setText(R.id.tv_menu, item.getName());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }
}
