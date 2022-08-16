package com.slxk.hounddog.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.MapTypeBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 地图类型选择
 */
public class MapTypeAdapter extends CommonAdapter<MapTypeBean> {

    public MapTypeAdapter(Context context, int layoutId, List<MapTypeBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, MapTypeBean item, int position) {
        ImageView ivMap = viewHolder.getView(R.id.iv_map);
        TextView tvMap = viewHolder.getView(R.id.tv_map);
        tvMap.setText(item.getMapName());
        switch (item.getId()){
            case 0:
                if (item.isSelect()){
                    ivMap.setImageResource(R.drawable.icon_map_amap_show);
                }else{
                    ivMap.setImageResource(R.drawable.icon_map_amap);
                }
                break;
            case 1:
                if (item.isSelect()){
                    ivMap.setImageResource(R.drawable.icon_map_baidu_show);
                }else{
                    ivMap.setImageResource(R.drawable.icon_map_baidu);
                }
                break;
            case 2:
                if (item.isSelect()){
                    ivMap.setImageResource(R.drawable.icon_map_google_show);
                }else{
                    ivMap.setImageResource(R.drawable.icon_map_google);
                }
                break;
        }
        if (item.isSelect()){
            tvMap.setTextColor(mContext.getResources().getColor(R.color.color_00C8F8));
        }else{
            tvMap.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        }
    }
}
