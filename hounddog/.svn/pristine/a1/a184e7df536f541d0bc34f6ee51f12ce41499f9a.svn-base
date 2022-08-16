package com.slxk.hounddog.mvp.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.MapSatelliteBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 2D地图，卫星地图
 */
public class MapSatelliteAdapter extends CommonAdapter<MapSatelliteBean> {

    public MapSatelliteAdapter(Context context, int layoutId, List<MapSatelliteBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, MapSatelliteBean item, int position) {
        ImageView ivPath = viewHolder.getView(R.id.iv_map_type);
        TextView tvName = viewHolder.getView(R.id.tv_name);
        tvName.setText(item.getName());
        if (item.getId() == 0){
            if (item.isSelect()){
                ivPath.setImageResource(R.drawable.icon_ordinary_map_show);
            }else{
                ivPath.setImageResource(R.drawable.icon_ordinary_map);
            }
        }else{
            if (item.isSelect()){
                ivPath.setImageResource(R.drawable.icon_satellite_map_show);
            }else{
                ivPath.setImageResource(R.drawable.icon_satellite_map);
            }
        }
        if (item.isSelect()){
            tvName.setTextColor(mContext.getResources().getColor(R.color.color_00C8F8));
        }else{
            tvName.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        }
    }
}
