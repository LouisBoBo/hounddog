package com.slxk.hounddog.mvp.ui.adapter;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.PhoneAreaResultBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 手机号码国际区号
 */
public class PhoneAreaAdapter extends BaseQuickAdapter<PhoneAreaResultBean.ItemsBean, BaseViewHolder> {

    public PhoneAreaAdapter(int layoutResId, @Nullable List<PhoneAreaResultBean.ItemsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PhoneAreaResultBean.ItemsBean item) {
        helper.setText(R.id.tv_country, item.getCountry());
        helper.setText(R.id.tv_zone, item.getZone() + "");
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

}
