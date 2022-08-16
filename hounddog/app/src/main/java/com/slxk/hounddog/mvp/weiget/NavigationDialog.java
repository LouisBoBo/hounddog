package com.slxk.hounddog.mvp.weiget;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.NavigationBean;
import com.slxk.hounddog.mvp.ui.adapter.NavigationAdapter;
import com.slxk.hounddog.mvp.utils.Utils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 导航
 */
public class NavigationDialog extends DialogFragment {

    private RecyclerView recyclerView;
    private TextView tvCancel;
    private List<NavigationBean> navigationBeans; // 导航选择
    private NavigationAdapter mAdapter;
    private double deviceLatitude; // 设备纬度
    private double deviceLongitude; // 设备经度

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_navigation, null);
        dialog.setContentView(viewGroup);
        dialog.setCanceledOnTouchOutside(true);
        initView(dialog);
        return dialog;
    }

    private void initView(Dialog dialog) {
        try {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = dialog.findViewById(R.id.recyclerview);
        tvCancel = dialog.findViewById(R.id.tv_cancel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setNavigationMapData();

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Utils.isButtonQuickClick()){
                    NavigationBean bean = navigationBeans.get(position);
                    if (bean.isHasApp()) {
                        onSelectNavigation(bean.getId());
                    } else {
                        onSelectError(bean.getId());
                    }
                }
            }
        });
    }

    /**
     * 设置地图app数据
     */
    private void setNavigationMapData() {
        navigationBeans = new ArrayList<>();
        if (!Utils.isPkgInstalled(getContext(), "com.autonavi.minimap")) {
            navigationBeans.add(new NavigationBean(0, getString(R.string.amap_not_install), false));
        } else {
            navigationBeans.add(new NavigationBean(0, getString(R.string.amap_map), true));
        }

        if (!Utils.isPkgInstalled(getContext(), "com.baidu.BaiduMap")) {
            navigationBeans.add(new NavigationBean(1, getString(R.string.baidu_not_install), false));
        } else {
            navigationBeans.add(new NavigationBean(1, getString(R.string.baidu_map), true));
        }

        if (!Utils.isPkgInstalled(getContext(), "com.tencent.map")) {
            navigationBeans.add(new NavigationBean(2, getString(R.string.tencent_not_install), false));
        } else {
            navigationBeans.add(new NavigationBean(2, getString(R.string.tencent_map), true));
        }

        mAdapter = new NavigationAdapter(R.layout.item_navigation, navigationBeans);
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 调起导航
     *
     * @param id
     */
    private void onSelectNavigation(int id) {
        Intent intent;
        if (id == 0) {
            try {
                //调用高德的外部导航
                intent = new Intent("android.intent.action.VIEW",
                        Uri.parse("androidamap://navi?sourceApplication="
                                + getString(R.string.app_name) + "&lat="
                                + deviceLatitude + "&lon="
                                + deviceLongitude + "&dev=0"));
                intent.setPackage("com.autonavi.minimap");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == 1) {
            try {
                //调用百度SDK   将高德经纬度转换为百度经纬度   然后调用百度的外部导航
                intent = Intent.getIntent("intent://map/navi?location=" + deviceLatitude + "," + deviceLongitude + "&src=thirdapp.navi." + getString(R.string.app_name) + "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                startActivity(intent); //启动调用
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            Uri uri = Uri.parse("qqmap://map/routeplan?type=drive&to="
                    + getString(R.string.my_dst) + "&tocoord=" + deviceLatitude + "," + deviceLongitude);
            intent.setData(uri);
            intent.setPackage("com.tencent.map");
            startActivity(intent);
        }
        dismiss();
    }

    /**
     * 地图未安装，弹出提示
     *
     * @param id
     */
    private void onSelectError(int id) {
        switch (id) {
            case 0:
                ToastUtils.showShort(getString(R.string.not_install_amap));
                break;
            case 1:
                ToastUtils.showShort(getString(R.string.not_install_baidu));
                break;
            case 2:
                ToastUtils.showShort(getString(R.string.not_install_tencent));
                break;
        }
    }

    public void show(FragmentManager manager, double latitude, double longitude) {
        if (isAdded()) {
            dismiss();
        }
        this.deviceLatitude = latitude;
        this.deviceLongitude = longitude;
        super.show(manager, "NavigationDialog");
    }

}
