package com.slxk.hounddog.mvp.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.di.component.DaggerCompassWirelessComponent;
import com.slxk.hounddog.mvp.contract.CompassWirelessContract;
import com.slxk.hounddog.mvp.model.bean.ColorBean;
import com.slxk.hounddog.mvp.presenter.CompassWirelessPresenter;
import com.slxk.hounddog.mvp.ui.adapter.CompassWirelessDeviceAdapter;
import com.slxk.hounddog.mvp.ui.view.TriangleView;
import com.slxk.hounddog.mvp.utils.DeviceUtils;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 无线定位-罗盘
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 09:20
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class CompassWirelessFragment extends BaseFragment<CompassWirelessPresenter> implements CompassWirelessContract.View, SensorEventListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_handheld_battery)
    TextView tvHandheldBattery;
    @BindView(R.id.tv_connected)
    TextView tvConnected;
    @BindView(R.id.tv_mobile_location_state)
    TextView tvMobileLocationState;

    @BindView(R.id.rl_triangle)
    RelativeLayout rlTriangle;
    @BindView(R.id.rl_compass_parent)
    RelativeLayout rlCompassParent;

    private ArrayList<DeviceModel> mDeviceModels;
    private CompassWirelessDeviceAdapter mAdapter;
    // 设备颜色池
    private ArrayList<ColorBean> mColorLists; // 设备颜色池

    private float fromDegrees = 0; // 旋转开始点位置
    private float toDegrees = 0; // 旋转结束位置

    private double mLatitude = 0; // 定位获取到的纬度
    private double mLongitude = 0; // 定位获取到的经度
    private String mImei; // 选中的设备imei号
    private DeviceModel mSelectModel;

    private Sensor sensor;
    private SensorManager sensorManager;// 声明一个传感管理器对象

    // 设备信息处理Dao
    private MyDao mDeviceBeanDao;

    private ChangePageReceiver receiver; // 注册广播接收器

    private int fragmentPosition = 0; // 当前切换的fragment位置

    public static CompassWirelessFragment newInstance() {
        CompassWirelessFragment fragment = new CompassWirelessFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCompassWirelessComponent //如找不到该类,请编译一下项�?
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compass_wireless, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mDeviceModels = new ArrayList<>();
        mColorLists = new ArrayList<>();
        mColorLists.addAll(DeviceUtils.getColorList(getContext()));
        try {
            mDeviceBeanDao = new MyDao(DeviceModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 从系统服务中获取传感管理器对象
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        //通过 getDefaultSensor 获得指南针传感器
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CompassWirelessDeviceAdapter(R.layout.item_compass_device_list, mDeviceModels, getContext(), mColorLists);
        recyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Utils.isButtonQuickClick()) {
                    DeviceModel model = mDeviceModels.get(position);
                    boolean is_select = model.isIs_select();
                    is_select = !is_select;
                    if (is_select) {
                        int select_count = 0;
                        for (DeviceModel mDeviceGetBean : mDeviceModels) {
                            if (mDeviceGetBean.isIs_select()) select_count++;
                        }
                        if (select_count >= 5) {
                            ToastUtils.show(getString(R.string.select_count_tip));
                            return;
                        }
                    }
                    model.setIs_select(is_select);
                    onAddDeviceToCompass();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        // 注册一个广播接收器，用于接收从首页跳转到报警消息页面的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("compass_list");
        receiver = new ChangePageReceiver();
        //注册切换页面广播接收者
        LocalBroadcastManager.getInstance(MyApplication.getMyApp()).registerReceiver(receiver, filter);

        onRegisterListener();
        onShowHandheldState();
        getDeviceListForDB();
    }

    /**
     * 页面切换广播，广播接收器
     */
    private class ChangePageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            fragmentPosition = intent.getIntExtra("position", 0);
            if (fragmentPosition == 1) {
                onRegisterListener();
                onShowHandheldState();
                getDeviceListForDB();
            } else {
                onUnregisterListener();
            }
        }
    }

    /**
     * 显示接收机对应状态
     */
    private void onShowHandheldState() {
        if (!TextUtils.isEmpty(MyApplication.getMyApp().getHandheld_battery())) {
            tvHandheldBattery.setText(MyApplication.getMyApp().getHandheld_battery());
        } else {
            tvHandheldBattery.setText(getString(R.string.handheld_battery));
        }
        if (!TextUtils.isEmpty(MyApplication.getMyApp().getHandheld_connected())) {
            tvConnected.setText(MyApplication.getMyApp().getHandheld_connected());
        } else {
            tvConnected.setText(getString(R.string.not_connected));
        }
        if (!TextUtils.isEmpty(MyApplication.getMyApp().getMobile_location_state())) {
            tvMobileLocationState.setText(MyApplication.getMyApp().getMobile_location_state());
        } else {
            tvMobileLocationState.setText(getString(R.string.mobile_gps_location_not));
        }
        mLatitude = MyApplication.getMyApp().getLatitude();
        mLongitude = MyApplication.getMyApp().getLongitude();
        mImei = MyApplication.getMyApp().getDevice_imei();
    }

    /**
     * 从数据库查询设备列表数据
     */
    private void getDeviceListForDB() {
        mDeviceModels.clear();
        if (getDeviceListDataBase() != null) {
            mDeviceModels.addAll(getDeviceListDataBase());
            onSetDeviceColor();
            onMobileAndDeviceDistance();
            onResetCompassDevice();
        }
    }

    /**
     * 从数据库get数据-设备列表
     *
     * @return
     */
    public List<DeviceModel> getDeviceListDataBase() {
        try {
            return (List<DeviceModel>) mDeviceBeanDao.queryAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置设备颜色
     */
    private void onSetDeviceColor() {
        mSelectModel = null;
        int index = 0;
        if (mDeviceModels.size() > 0) {
            for (int i = 0; i < mDeviceModels.size(); i++) {
                mDeviceModels.get(i).setColor(mColorLists.get(index).getColor());
                index++;
                if (index > mColorLists.size() - 1) {
                    index = 0;
                }
                if (!TextUtils.isEmpty(mImei)) {
                    if (mImei.equals(mDeviceModels.get(i).getImei())) {
                        mSelectModel = mDeviceModels.get(i);
                    }
                }
            }
        }
        if (mSelectModel != null) {
            mDeviceModels.remove(mSelectModel);
            mDeviceModels.add(0, mSelectModel);
        }
    }

    /**
     * 计算手机与设备的距离
     */
    private void onMobileAndDeviceDistance() {
        for (DeviceModel bean : mDeviceModels) {
            if (bean.getLat() != 0 && bean.getLon() != 0) {
                LatLng userLocation = new LatLng(mLatitude, mLongitude); // 手机当前位置
                LatLng carLocation = new LatLng((double) bean.getLat() / 1000000, (double) bean.getLon() / 1000000); // 设备位置
                //计算p1、p2两点之间的直线距离，单位：米
                float distance = AMapUtils.calculateLineDistance(userLocation, carLocation);
                bean.setDistance(distance);
            } else {
                bean.setDistance(0);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加设备到罗盘上
     */
    private void onAddDeviceToCompass() {
        rlTriangle.removeAllViews();
        if (mDeviceModels.size() > 0) {
            for (int i = 0; i < mDeviceModels.size(); i++) {
                DeviceModel model = mDeviceModels.get(i);
                if (model.getLat() == 0 && model.getLon() == 0 || !model.isIs_select()) {
                    continue;
                }
                int id = R.color.red;
                if (!TextUtils.isEmpty(model.getColor())) {
                    for (ColorBean bean : mColorLists) {
                        if (bean.getColor().equals(model.getColor())) {
                            id = bean.getColorId();
                            break;
                        }
                    }
                }

                TriangleView triangleTransparent = new TriangleView(getContext(), R.color.transparent);
                // 添加三角形父布局
                LinearLayout view = new LinearLayout(getContext());
                view.setOrientation(LinearLayout.VERTICAL);
                // 添加三角形
                TriangleView triangleView = new TriangleView(getContext(), id);
                view.addView(triangleView);
                view.addView(triangleTransparent);
                // 计算手机定位点和设备定位点的相对方向
                float rotation = 0;
                if (mLatitude != 0 && mLongitude != 0 && model.getLat() != 0 && model.getLon() != 0) {
                    LatLng userLocation = new LatLng(mLatitude, mLongitude);
                    LatLng deviceLocation = DeviceUtils.getAmapLatLng(model.getLat(), model.getLon());
                    rotation = Utils.getRotate(deviceLocation, userLocation);
                }

                float degrees = Math.abs(rotation + 180);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                view.setLayoutParams(params);
                view.setRotation(degrees);
                rlTriangle.addView(view);
            }
        }
    }

    /**
     * 判断是否只显示选中设备
     */
    private void onResetCompassDevice() {
        rlTriangle.removeAllViews();
        if (mSelectModel != null) {
            //有选中的设备添加勾选
            for (DeviceModel mDeviceGetBean : mDeviceModels) {
                if (mDeviceGetBean == mSelectModel) {
                    mDeviceGetBean.setIs_select(true);
                    onAddDeviceToCompass();
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } else {
            //如果没有选中的设备默认选中前3个设备
            for (int i = 0; i < mDeviceModels.size(); i++) {
                if (i > 2) {
                    break;
                }
                DeviceModel model = mDeviceModels.get(i);
                model.setIs_select(true);
            }
            onAddDeviceToCompass();
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {//顺时针转动为正，故手机顺时针转动时，图片得逆时针转动
            toDegrees = -event.values[0];

            if (rlCompassParent != null && fromDegrees != toDegrees) {
                //让图片相对自身中心点转动，开始角度默认为0；此后开始角度等于上一次结束角度
                RotateAnimation ra = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //动画时间200毫秒
                ra.setDuration(200);
                ra.setFillAfter(true);

                rlCompassParent.startAnimation(ra);
                fromDegrees = toDegrees;
            }
        }
    }

    /**
     * 当传感器精度改变时回调该方法，一般无需处理
     *
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 为传感器管理者注册监听器，第三个参数指获取速度正常
     */
    private void onRegisterListener() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 注销当前活动的传感监听器
     */
    private void onUnregisterListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onPause() {
        // 注销当前活动的传感监听器
        onUnregisterListener();
        super.onPause();
    }

    @Override
    public void onResume() {
        //为传感器管理者注册监听器，第三个参数指获取速度正常
        if (recyclerview != null) {
            onRegisterListener();
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(MyApplication.getMyApp()).unregisterReceiver(receiver);
        super.onDestroyView();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }
}
