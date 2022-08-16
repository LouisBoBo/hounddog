package com.slxk.hounddog.mvp.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.slxk.hounddog.R;
import com.slxk.hounddog.db.MapDownloadModel;
import com.slxk.hounddog.mvp.ui.view.CircularProgressView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 离线地图
 */
public class OfflineMapAdapter extends BaseQuickAdapter<MapDownloadModel, BaseViewHolder> {

    private onOfflineMapChange onOfflineMapChange;

    public OfflineMapAdapter(int layoutResId, @Nullable List<MapDownloadModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MapDownloadModel item) {
        helper.setText(R.id.tv_name, item.getMap_name());
        helper.setText(R.id.tv_map_size, item.getTotal_size());
        helper.setText(R.id.tv_download_progress, item.getProgress() + "%");
        ImageView ivStart = helper.getView(R.id.iv_start);
        CircularProgressView progressView = helper.getView(R.id.circular_view);
        progressView.setProgress(item.getProgress());
        if (item.getProgress() != 100 && item.isDownload()){
            ivStart.setImageResource(R.drawable.icon_download_map_stop);
        }else{
            ivStart.setImageResource(R.drawable.icon_download_map_start);
        }
        SwipeMenuLayout swipeMenuLayout = helper.getView(R.id.swipe_menu_layout);
        if (item.isDownload()){
            swipeMenuLayout.setSwipeEnable(false);
        }else{
            swipeMenuLayout.setSwipeEnable(true);
        }
        Button btnDelete = helper.getView(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeMenuLayout.quickClose();
                if (onOfflineMapChange != null){
                    onOfflineMapChange.onDeleteMap(item);
                }
            }
        });

        helper.addOnClickListener(R.id.rl_download_progress);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    public void setOfflineMapChange(onOfflineMapChange change){
        this.onOfflineMapChange = change;
    }

    public interface onOfflineMapChange{

        /**
         * 删除任务
         * @param model
         */
        void onDeleteMap(MapDownloadModel model);

    }

}
