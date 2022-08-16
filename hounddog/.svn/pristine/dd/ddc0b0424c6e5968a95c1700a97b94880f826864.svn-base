package com.slxk.hounddog.mvp.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cunoraz.gifview.library.GifView;
import com.slxk.hounddog.R;
import com.slxk.hounddog.db.RecordModel;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 录音列表
 */
public class RecordAdapter extends BaseQuickAdapter<RecordModel, BaseViewHolder> {

    private int mWidth; // 屏幕的宽度

    public RecordAdapter(int layoutResId, @Nullable List<RecordModel> data, int width) {
        super(layoutResId, data);
        this.mWidth = width;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RecordModel item) {
        ImageView ivSelect = helper.getView(R.id.iv_select);
        ImageView ivPlay = helper.getView(R.id.iv_play);
        GifView gifView = helper.getView(R.id.gif_play);
        View viewPlay = helper.getView(R.id.view_isRead);
        helper.setText(R.id.tv_time, item.getRecordTimeShow());
        File file = new File(item.getPath());
        if (file.exists()) { // 判断是否存在
            int tmpSecond = item.getSecond();
            helper.setText(R.id.tv_record_time,tmpSecond + "\"");

            // 根据时长来显示录音布局长度
            ViewGroup.LayoutParams params = helper.getView(R.id.rl_record).getLayoutParams();
            if (tmpSecond < 20){
                params.width = mWidth / 4;
            }else if(tmpSecond < 30){
                float floatWidth = (float) mWidth / 3.8f;
                params.width = (int) Math.ceil(floatWidth);
            }else if(tmpSecond < 40){
                float floatWidth = (float) mWidth / 3.6f;
                params.width = (int) Math.ceil(floatWidth);
            }else if(tmpSecond < 50){
                float floatWidth = (float) mWidth / 3.4f;
                params.width = (int) Math.ceil(floatWidth);
            }else if(tmpSecond < 60){
                float floatWidth = (float) mWidth / 3.2f;
                params.width = (int) Math.ceil(floatWidth);
            }else if(tmpSecond < 100){
                float floatWidth = (float) mWidth / 3.2f;
                params.width = (int) Math.ceil(floatWidth);
            }else if(tmpSecond < 180){
                params.width = mWidth / 3;
            }else if(tmpSecond < 240){
                float floatWidth = (float) mWidth / 2.8f;
                params.width = (int) Math.ceil(floatWidth);
            }else if(tmpSecond < 300){
                float floatWidth = (float) mWidth / 2.6f;
                params.width = (int) Math.ceil(floatWidth);
            }else{
                float floatWidth = (float) mWidth / 2.4f;
                params.width = (int) Math.ceil(floatWidth);
            }
            helper.getView(R.id.rl_record).setLayoutParams(params);
        }
        if (item.isPlayed()){
            viewPlay.setVisibility(View.INVISIBLE);
        }else{
            viewPlay.setVisibility(View.VISIBLE);
        }
        gifView.setVisibility(View.GONE);
        gifView.pause();
        ivPlay.setVisibility(View.VISIBLE);
        if (item.isPlayNow()) {
            ivPlay.setVisibility(View.GONE);
            gifView.setVisibility(View.VISIBLE);
            if (gifView.isPaused()) {
                gifView.play();
            }
        }
        if (item.isDelete()){
            ivSelect.setVisibility(View.VISIBLE);
        }else{
            ivSelect.setVisibility(View.GONE);
        }
        if (item.isSelect()){
            ivSelect.setImageResource(R.drawable.icon_select_two);
        }else{
            ivSelect.setImageResource(R.drawable.icon_unselected_two);
        }

        helper.addOnClickListener(R.id.rl_record);
        helper.addOnLongClickListener(R.id.rl_record);
        helper.addOnClickListener(R.id.iv_select);
        helper.addOnClickListener(R.id.rl_record_parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

}
