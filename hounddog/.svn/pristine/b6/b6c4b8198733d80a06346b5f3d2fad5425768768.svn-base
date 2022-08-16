package com.slxk.hounddog.mvp.ui.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.SettingFunctionBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 设置页面功能列表
 */
public class SettingAdapter extends BaseQuickAdapter<SettingFunctionBean, BaseViewHolder> {

    public SettingAdapter(int layoutResId, @Nullable List<SettingFunctionBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SettingFunctionBean item) {
        ImageView ivPath = helper.getView(R.id.iv_path);
        ivPath.setImageResource(item.getDrawableId());
        helper.setText(R.id.tv_function, item.getFunction());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }
}
