package com.slxk.hounddog.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.MapOtherBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 地图类型选择
 */
public class MapOtherAdapter extends CommonAdapter<MapOtherBean> {

    public MapOtherAdapter(Context context, int layoutId, List<MapOtherBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, MapOtherBean item, int position) {
        ImageView ivMap = viewHolder.getView(R.id.iv_map);
        TextView tvMap = viewHolder.getView(R.id.tv_map);
        tvMap.setText(item.getName());
        if (item.getId() == 0){
            if (item.isSelect()){
                ivMap.setImageResource(R.drawable.icon_road_conditions_show);
            }else{
                ivMap.setImageResource(R.drawable.icon_road_conditions);
            }
        }else{
            if (item.isSelect()){
                ivMap.setImageResource(R.drawable.icon_street_view_show);
            }else{
                ivMap.setImageResource(R.drawable.icon_street_view);
            }
        }
        if (item.getId() == 0){
            if (item.isSelect()){
                tvMap.setTextColor(mContext.getResources().getColor(R.color.color_00C8F8));
            }else{
                tvMap.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            }
        }else{
            tvMap.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        }
    }
}
