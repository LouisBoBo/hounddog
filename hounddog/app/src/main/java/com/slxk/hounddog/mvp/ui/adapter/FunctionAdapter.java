package com.slxk.hounddog.mvp.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.FunctionBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 首页功能菜单
 */
public class FunctionAdapter extends CommonAdapter<FunctionBean> {

    public FunctionAdapter(Context context, int layoutId, List<FunctionBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, FunctionBean item, int position) {
        ImageView ivMenu = viewHolder.getView(R.id.iv_menu);
        ivMenu.setImageResource(item.getDrawableId());
        viewHolder.setText(R.id.tv_menu, item.getName());
    }
}
