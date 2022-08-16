package com.slxk.hounddog.mvp.weiget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.SMSListMenuBean;
import com.slxk.hounddog.mvp.ui.adapter.PenetrateHistoryMenuAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 设备管理功能
 */
public class SMSListPopupwindow extends PopupWindow {

    private Context mContext;
    private RecyclerView recyclerView;
    private List<SMSListMenuBean> menuBeans;
    private PenetrateHistoryMenuAdapter mAdapter;
    private onSMSSelect select;
    private LinearLayout ll_parent;

    public SMSListPopupwindow(Context context){
        super(context);
        this.mContext = context;
        View root = View.inflate(context, R.layout.popupwindow_sms_list, null);
        recyclerView = root.findViewById(R.id.recyclerView);
        ll_parent = root.findViewById(R.id.ll_parent);

        //设置SelectPicPopupWindow的View
        this.setContentView(root);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.color_black_40)));

        initData();
    }

    private void initData(){
        menuBeans = new ArrayList<>();
        //重启 恢复出厂设置 查询域名 查询版本
        String [] orderList = new String[]{"SL RT","SL FT","SL CX","SL VR"};
        menuBeans.add(new SMSListMenuBean(0, mContext.getString(R.string.order_rt),orderList[0]));
        menuBeans.add(new SMSListMenuBean(1, mContext.getString(R.string.order_ft),orderList[1]));
        menuBeans.add(new SMSListMenuBean(2, mContext.getString(R.string.order_cx),orderList[2]));
        menuBeans.add(new SMSListMenuBean(3, mContext.getString(R.string.order_vr),orderList[3]));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new PenetrateHistoryMenuAdapter(R.layout.item_penetrate_menu, menuBeans);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (select != null){
                    select.onSMSSelect(menuBeans.get(position).getContent());
                }
                dismiss();
            }
        });
        ll_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 兼容 android 7.0之后设置showAsDropDown失效问题
     * @param anchor
     */
    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            int[] a = new int[2];
            anchor.getLocationInWindow(a);
            showAtLocation(anchor, Gravity.NO_GRAVITY, xoff, a[1] + anchor.getHeight() + yoff);
        } else {
            super.showAsDropDown(anchor, xoff, yoff);
        }
    }

    public void setOnSMSSelect(onSMSSelect select){
        this.select = select;
    }

    public interface onSMSSelect{

        /**
         * 选择的指令
         * @param
         */
        void onSMSSelect(String content);

    }

}
