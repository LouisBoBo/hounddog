package com.slxk.hounddog.mvp.ui.adapter;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.NavigationBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NavigationAdapter extends BaseQuickAdapter<NavigationBean, BaseViewHolder> {

    public NavigationAdapter(int layoutResId, @Nullable List<NavigationBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NavigationBean item) {
        helper.setText(R.id.tv_navigation, item.getName());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

}
