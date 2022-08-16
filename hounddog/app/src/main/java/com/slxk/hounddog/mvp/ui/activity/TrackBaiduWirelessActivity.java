package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.db.DeviceTrackModel;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.di.component.DaggerTrackBaiduWirelessComponent;
import com.slxk.hounddog.mvp.contract.TrackBaiduWirelessContract;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.presenter.TrackBaiduWirelessPresenter;
import com.slxk.hounddog.mvp.ui.view.data.haibin.Calendar;
import com.slxk.hounddog.mvp.utils.BitmapHelperBaidu;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.DateUtils;
import com.slxk.hounddog.mvp.utils.DeviceUtils;
import com.slxk.hounddog.mvp.utils.GpsUtils;
import com.slxk.hounddog.mvp.utils.MyOrientationListener;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.AlertAppDialog;
import com.slxk.hounddog.mvp.weiget.DateSelectPopupWindow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 轨迹-百度地图-无线模式
 * <p>
 * Created by MVPArmsTemplate on 01/05/2022 15:50
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class TrackBaiduWirelessActivity extends BaseActivity<TrackBaiduWirelessPresenter> implements TrackBaiduWirelessContract.View,
        BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener, BaiduMap.OnMapStatusChangeListener {

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_before_day)
    TextView tvBeforeDay;
    @BindView(R.id.tv_data)
    TextView tvData;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.tv_after_day)
    TextView tvAfterDay;
    @BindView(R.id.iv_base_station_switch)
    ImageView ivBaseStationSwitch;
    @BindView(R.id.cv_clear_track)
    CardView cvClearTrack;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.seekbar_process)
    SeekBar seekbarProcess;
    @BindView(R.id.iv_play_speed)
    ImageView ivPlaySpeed;
    @BindView(R.id.tv_play_speed)
    TextView tvPlaySpeed;
    @BindView(R.id.tv_location_time)
    TextView tvLocationTime;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.view_title)
    View viewTitle;
    @BindView(R.id.ll_location_info)
    LinearLayout llLocationInfo;
    @BindView(R.id.cv_base_station_switch)
    CardView cvBaseStationSwitch;

    public static final String Intent_Imei_Key = "imei_key";
    public static final String Intent_Name_Key = "name_key";
    private String mImei; // 设备imei号
    private String mDeviceName = ""; // 设备名称

    private BaiduMap mBaiduMap;
    private float mZoom = 16;
    private LatLng targetLatLng; // 轨迹播放点的位置
    private LocationClient mLocationClient;
    private double mLatitude = 0.0; // 定位获取到的纬度
    private double mLongitude = 0.0; // 定位获取到的经度
    private boolean isFirstLocation = true; // 是否是第一次定位，避免后续定位成功后，一直设置定位点为中心点
    private MyLocationConfiguration.LocationMode mCurrentMode; // 定位模式 地图SDK支持三种定位模式：NORMAL（普通态）, FOLLOWING（跟随态）, COMPASS（罗盘态）
    private static final int accuracyCircleFillColor = 0x00FF00FF; // 自定义精度圈填充颜色
    private static final int accuracyCircleStrokeColor = 0x00FF00FF; // 自定义精度圈边框颜色

    // 轨迹筛选日期
    private ArrayList<String> mTrackScreenDatas;
    private ArrayList<DeviceTrackModel> mTrackAllModels; // 每次查询的总GPS数据
    private ArrayList<DeviceTrackModel> mTrackCurrentGPSModels; // 当前查询下的GPS数据
    private ArrayList<DeviceTrackModel> mTrackBaseLocationModels; // 基站数据
    private ArrayList<DeviceTrackModel> mTrackModels; // 每次绘制的条数
    private ArrayList<DeviceTrackModel> mArrowPointList; // 方向箭头
    private static final int Arrow_Point_Index = 10; // 方向箭头取点间隔，每隔10个点取一个点判断箭头方向
    private static final long Track_Number = 1000; // 每次绘制的条数的阀值，同时也是向数据库查询每页条数
    private int mTrack_Page = 0; // 数据库查询，页码
    private String startHour = "00:00:00";
    private String endHour = "23:59:59";
    // 无线模式下
    private String mQuestData = ""; // 当前查询数据库的日期
    private LatLngBounds mLatLngBounds; // 地图内可见经纬度
    private BitmapHelperBaidu bitmapHelper;

    // 播放动画
    private boolean isPauseNow = false;//是否暂停动画
    private int playSpeed = 80; // 播放速度
    private int playType = 1; // 播放速度，1：快，播放速度为90，2：中，播放速度为50，3：慢，播放速度为10，默认快速
    private int playIndex = 0; // 播放进度条上的进度
    private Timer onTimerPlay = null;
    private int playLongTime = 1000; // 播放轨迹间隔时长

    private boolean isTrackDataComplete = false; // 是否获取完当前的数据
    private Marker playCurrrentMarker;//当前marker   起点   终点  停车点
    private ArrayList<Marker> mJzMarkerList;//保存地图中所有基站点图标的列表
    private ArrayList<Marker> mMarkerList;//保存地图中所有图标的列表
    private long mArrowBeforeLat = 0; // 方向箭头的上一个点，用来判断是否满足打方向箭头
    private long mArrowBeforeLon = 0; // 方向箭头的上一个点，用来判断是否满足打方向箭头
    private static final int Arrow_Distance = 500; // 方向箭头打点间隔，单位米

    // 日历相关
    private ArrayList<Calendar> trackDateList;
    private ArrayList<Calendar> selectTrackDateList;
    private java.util.Calendar currentSelectDay; // 请求的日期的第一个，包括多日期请求
    private Calendar currentDay; // 请求的日期的第一个，包括多日期请求，用于显示在日历上

    private boolean isShowBase = false; // 是否显示基站
    private DateSelectPopupWindow dateSelectPopupWindow;

    static class MapInfoWindow {
        public View viewInfoWindows;
        public TextView tvMarkerImei; // 设备号
        public TextView tvMarkerName; // 设备名称
        public TextView tvMarkerTime; // 打点时间
        public TextView tvMarkerAddress; // 地址
    }

    private MapInfoWindow mapInfoWindow = new MapInfoWindow();

    private MyOrientationListener myOrientationListener; // 手机传感器方向
    private float mXDirection = 0;
    private float mCurrentAccracy;
    private String mCompassImei = "00000000"; // 罗盘虚拟imei号，用来判断是否点击了罗盘
    private HashMap<String, Marker> deviceMap; // 存储任务

    private MyDao trackBeanDao;

    public static Intent newInstance(String imei, String name) {
        Intent intent = new Intent(MyApplication.getMyApp(), TrackBaiduWirelessActivity.class);
        intent.putExtra(Intent_Imei_Key, imei);
        intent.putExtra(Intent_Name_Key, name);
        return intent;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTrackBaiduWirelessComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_track_baidu_wireless;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.track));
        mTrackScreenDatas = new ArrayList<>();
        mTrackAllModels = new ArrayList<>();
        mTrackCurrentGPSModels = new ArrayList<>();
        mTrackBaseLocationModels = new ArrayList<>();
        mTrackModels = new ArrayList<>();
        mArrowPointList = new ArrayList<>();
        trackDateList = new ArrayList<>();
        selectTrackDateList = new ArrayList<>();
        mJzMarkerList = new ArrayList<>();
        mMarkerList = new ArrayList<>();
        deviceMap = new HashMap<>();
        mImei = getIntent().getStringExtra(Intent_Imei_Key);
        mDeviceName = getIntent().getStringExtra(Intent_Name_Key);
        try{
            trackBeanDao = new MyDao(DeviceTrackModel.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        currentSelectDay = java.util.Calendar.getInstance(Locale.ENGLISH);
        mQuestData = DateUtils.getTodayDateTime_3();
        bitmapHelper = new BitmapHelperBaidu(this);
        playLongTime = 50 + (10 * (100 - playSpeed));

        tvSpeed.setVisibility(View.GONE);
        tvAddress.setVisibility(View.GONE);
        cvBaseStationSwitch.setVisibility(View.GONE);
        tvData.setText(DateUtils.getTodayDateTime_3());
        onShowLocationInfo(false);
        initMap();
        loadMapInfoWindow();
        onSeekBarProcess();
        onQuestTrackModels(true);
    }

    /**
     * 是否显示底部定位点详细信息
     * @param isShow
     */
    private void onShowLocationInfo(boolean isShow){
//        llLocationInfo.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (!isShow){
            tvLocationTime.setText(getString(R.string.time));
            tvSpeed.setText(getString(R.string.speed));
            tvAddress.setText(getString(R.string.address));
        }
    }

    /**
     * 初始化百度地图
     */
    private void initMap() {
        mBaiduMap = mapView.getMap();
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        if (mBaiduMap != null) {
            mapView.showZoomControls(false);
            mBaiduMap.getUiSettings().setZoomGesturesEnabled(true);
            mBaiduMap.setMyLocationEnabled(true);
            mBaiduMap.setMaxAndMinZoomLevel(20, 4);
            mBaiduMap.setOnMarkerClickListener(this);
            mBaiduMap.setOnMapClickListener(this);
            mBaiduMap.setOnMapStatusChangeListener(this);

            mBaiduMap.setMyLocationEnabled(true);
            //实例化UiSettings类对象
            UiSettings mUiSettings = mBaiduMap.getUiSettings();
            //通过设置enable为true或false 选择是否启用地图旋转功能
            mUiSettings.setRotateGesturesEnabled(false);

            //定位初始化
            mLocationClient = new LocationClient(this);
            //通过LocationClientOption设置LocationClient相关参数
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true); // 打开gps
            option.setCoorType("gcj02"); // 设置坐标类型
            //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
            option.setScanSpan(3000);
            //可选，设置是否需要地址信息，默认不需要
            option.setIsNeedAddress(true);
            //可选，设置是否需要地址描述
            option.setIsNeedLocationDescribe(true);

            //设置locationClientOption
            mLocationClient.setLocOption(option);

            initOritationListener();
            //注册LocationListener监听器
            MyLocationListener myLocationListener = new MyLocationListener();
            mLocationClient.registerLocationListener(myLocationListener);
        }
    }

    /**
     * 初始化方向传感器
     */
    private void initOritationListener() {
        myOrientationListener = new MyOrientationListener(MyApplication.getMyApp());
        myOrientationListener.setmOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mXDirection = x;
                if (mBaiduMap != null) {
                    // 构造定位数据
                    MyLocationData locData = new MyLocationData.Builder()
                            .accuracy(mCurrentAccracy)
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(mXDirection)
                            .latitude(mLatitude)
                            .longitude(mLongitude).build();
                    // 设置定位数据
                    mBaiduMap.setMyLocationData(locData);
                    // 设置自定义图标
                    // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
                    MyLocationConfiguration config = new MyLocationConfiguration(
                            mCurrentMode, true, bitmapHelper.getBitmapZoomForUserLocation(mZoom), accuracyCircleFillColor, accuracyCircleStrokeColor);
                    mBaiduMap.setMyLocationConfiguration(config);
                }
            }
        });
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            mCurrentAccracy = location.getRadius();
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            MyApplication.getMyApp().setLatitude(mLatitude);
            MyApplication.getMyApp().setLongitude(mLongitude);

            SPUtils.getInstance().put(ConstantValue.Is_In_China, GpsUtils.isChinaLocation(mLatitude, mLongitude));

            isFirstLocation = false;

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mXDirection)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            onShowCompass(mLatitude, mLongitude);
        }
    }

    /**
     * 绘制罗盘位置
     * @param lat
     * @param lon
     */
    private void onShowCompass(double lat, double lon){
        // 开始绘制设备marker
        LatLng deviceLatLng = new LatLng(lat, lon);
        if (deviceMap.get(mCompassImei) != null) {
            Objects.requireNonNull(deviceMap.get(mCompassImei)).setPosition(deviceLatLng);
            Objects.requireNonNull(deviceMap.get(mCompassImei)).setIcon(getCompassMarkerIcon());
        } else {
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(deviceLatLng);
            markerOption.title(mCompassImei);
            markerOption.icon(getCompassMarkerIcon());
            markerOption.anchor(0.5f, 0.5f);
            markerOption.draggable(false);
            markerOption.visible(true);
            markerOption.zIndex(-9999);

            Marker marker = (Marker) mBaiduMap.addOverlay(markerOption);
            deviceMap.put(mCompassImei + "", marker);
        }
    }

    /**
     * 绘制设备图标等信息
     *
     * @return
     */
    private BitmapDescriptor getCompassMarkerIcon() {
        View view = View.inflate(this, R.layout.layout_compass_marker, null);
        ImageView ivCompass = view.findViewById(R.id.iv_compass);
        ivCompass.setImageResource(Utils.isLocaleForCN() ? R.drawable.icon_compass_zh : R.drawable.icon_compass_es);
        return convertViewToBitmap(view);
    }

    private static BitmapDescriptor convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return BitmapDescriptorFactory.fromBitmap(view.getDrawingCache());
    }

    /**
     * 初始化气泡框
     */
    @SuppressLint("InflateParams")
    private void loadMapInfoWindow() {
        mapInfoWindow.viewInfoWindows = getLayoutInflater().inflate(R.layout.layout_infowindow, null);
        mapInfoWindow.tvMarkerImei = mapInfoWindow.viewInfoWindows.findViewById(R.id.tv_marker_imei);
        mapInfoWindow.tvMarkerName = mapInfoWindow.viewInfoWindows.findViewById(R.id.tv_marker_name);
        mapInfoWindow.tvMarkerTime = mapInfoWindow.viewInfoWindows.findViewById(R.id.tv_marker_time);
        mapInfoWindow.tvMarkerAddress = mapInfoWindow.viewInfoWindows.findViewById(R.id.tv_marker_address);
    }

    /**
     * 进度条事件监听
     */
    private void onSeekBarProcess() {
        seekbarProcess.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mTrackAllModels != null && mTrackAllModels.size() > 3) {
                    playIndex = mTrackAllModels.size() * seekBar.getProgress() / 100;
                    if (playIndex >= mTrackAllModels.size()) {
                        playIndex = mTrackAllModels.size() - 1;
                    }
                    onSeekBarInfoShow();
                    isPauseNow = true;
                    ivPlay.setImageResource(R.drawable.icon_track_play);
                    onPlayTrack();

                    LatLng latLng = DeviceUtils.getBaiduLatLng(mTrackAllModels.get(playIndex).getLat(), mTrackAllModels.get(playIndex).getLon());
                    if (playCurrrentMarker == null) {
                        playCurrrentMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().zIndex(100)
                                .title(mImei + "")
                                .position(latLng)
                                .anchor(0.5f, 0.5f)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_device_line_on)));
                    } else {
                        playCurrrentMarker.setPosition(latLng);
                    }
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(latLng).zoom(mZoom).build()));
                }
            }
        });
    }

    /**
     * 查询轨迹数据
     *
     * @param isClearTrack 是否清除地图上的轨迹
     */
    private void onQuestTrackModels(boolean isClearTrack) {
        mTrackModels.clear();
        if (isClearTrack) {
            onResetTrackData();
        }
        if (getDeviceTrackDataBase() != null){
            mTrackModels.addAll(getDeviceTrackDataBase());
        }

        llData.postDelayed(new Runnable() {
            @Override
            public void run() {
                onTrackShow(isClearTrack);
            }
        }, 500);

        trackDateList.clear();
        for (DeviceTrackModel mTrackModel : mTrackModels) {
            if(mTrackModel.getTime().length() > 10) {
                String day = mTrackModel.getTime().substring(0,10);
                String[] dayDayas = day.split("-");
                trackDateList.add(getCalendar(Integer.parseInt(dayDayas[0]), Integer.parseInt(dayDayas[1]), Integer.parseInt(dayDayas[2])));
            }
            break;
        }
    }

    /**
     * 格式化有轨迹数据的日期
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return
     */
    private Calendar getCalendar(int year, int month, int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(getResources().getColor(R.color.color_2EB6B9));
        return calendar;
    }

    /**
     * 从数据库get数据-设备轨迹列表
     *
     * @return
     */
    public List<DeviceTrackModel> getDeviceTrackDataBase() {
        try{
            return trackBeanDao.getDeviceTrackPagination(
                    mImei,
                    DateUtils.data_5(mQuestData + " " + startHour),
                    DateUtils.data_5(mQuestData + " " + endHour),
                    Track_Number,
                    Track_Number * mTrack_Page);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 重置轨迹数据及相关查询条件
     */
    private void onResetTrackData() {
        mBaiduMap.clear();
        mTrackAllModels.clear();
        mTrack_Page = 0;
        mLatLngBounds = null;
        isTrackDataComplete = false;
        mJzMarkerList.clear();
        tvLocationTime.setText(getString(R.string.time));
        tvAddress.setText(getString(R.string.address));
        onShowLocationInfo(false);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mBaiduMap != null) {
            mBaiduMap.hideInfoWindow();
        }
    }

    @Override
    public void onMapPoiClick(MapPoi mapPoi) {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        mZoom = mapStatus.zoom;
        targetLatLng = mapStatus.target;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (Utils.isButtonQuickClick()) {
            try {
                getInfoWindow(marker);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 气泡框
     *
     * @param marker
     */
    @SuppressLint("SetTextI18n")
    private void getInfoWindow(Marker marker) {
        String imei = marker.getTitle();
        if (!TextUtils.isEmpty(imei) && !imei.equals(mCompassImei)){
            // 定位时间 + 经度 + 纬度
            String snippetString = (String) marker.getExtraInfo().get("snippet");
            mapInfoWindow.tvMarkerImei.setText(getString(R.string.device_number) + "：" + mImei);
            mapInfoWindow.tvMarkerName.setText(getString(R.string.name) + "：" + StringUtils.null2Length0(mDeviceName));
            if (!TextUtils.isEmpty(snippetString)) {
                String[] detailInfo = snippetString.split(",");
                if (detailInfo.length == 3) {
                    mapInfoWindow.tvMarkerTime.setVisibility(View.VISIBLE);
                    mapInfoWindow.tvMarkerTime.setText(getString(R.string.time) + "：" + detailInfo[0]);
                } else {
                    mapInfoWindow.tvMarkerTime.setText(getString(R.string.time));
                    mapInfoWindow.tvMarkerTime.setVisibility(View.GONE);
                }
                mapInfoWindow.tvMarkerAddress.setText(getString(R.string.address));
                mapInfoWindow.tvMarkerAddress.setVisibility(View.GONE);
            }
            targetLatLng = marker.getPosition();
            updateMapCenter();
            mBaiduMap.showInfoWindow(new InfoWindow(mapInfoWindow.viewInfoWindows, marker.getPosition(), -80));
        }
    }

    /**
     * 设置当前点位于地图中心
     */
    private void updateMapCenter() {
        if (mBaiduMap != null) {
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(targetLatLng).zoom(mZoom).build()));
        }
    }

    /**
     * 轨迹绘制
     *
     * @param isClearTrack 是否清除地图上的轨迹
     */
    @SuppressLint("SetTextI18n")
    private void onTrackShow(boolean isClearTrack) {
        mTrackCurrentGPSModels.clear();
        mTrackBaseLocationModels.clear();
        mArrowPointList.clear();
        if (isClearTrack && mTrackModels.size() == 0) {
            ToastUtils.show(getString(R.string.no_trace_data));
        }

        if (mTrackAllModels.size() > 0) {
            mTrackCurrentGPSModels.add(mTrackAllModels.get(mTrackAllModels.size() - 1));
        }
        // 开始过滤经纬度
        long lastLat;
        long lastLon;
        String lastTime; // 上一个点定位时间
        long time_difference; // 时间差
        double distance; // 两点之间的距离
        double speed_dis; // 每秒速度
        String currentTime; // 当前定位时间
        for (int i = 0; i < mTrackModels.size(); i++) {
            DeviceTrackModel model = mTrackModels.get(i);
            currentTime = model.getTime(); // 当前点的时间
            if (i > 0){
                //1.过滤时间相同的点
                lastTime = mTrackModels.get(i - 1).getTime();
                if (lastTime.equals(model.getTime())){
                    continue;
                }
                //2.过滤位置相同的点
                lastLat = mTrackModels.get(i - 1).getLat();
                lastLon = mTrackModels.get(i - 1).getLon();
                if (lastLat == model.getLat() && lastLon == model.getLon()){
                    continue;
                }

                //3.计算两点之间的距离以及每秒钟的速度，判断每秒时速是否在0 - 50的范围内，不在就过滤掉
                time_difference = DateUtils.data_6(currentTime) - DateUtils.data_6(lastTime);
                LatLng latLng_1 = new LatLng((double) lastLat / 1000000, (double) lastLon / 1000000);
                LatLng latLng_2 = new LatLng((double) model.getLat() / 1000000, (double) model.getLon() / 1000000);
                // 计算两点之间的距离
                distance = DistanceUtil.getDistance(latLng_1, latLng_2);
                // 计算两点之间的每秒平均速度
                speed_dis = distance / time_difference;
                if (speed_dis == 0 || speed_dis > 40) {
                    continue;
                }
            }

            //4.计算已纳入绘制轨迹的最后一个点与当前点对比，判断每秒时速是否在0 - 50的范围内，不在就过滤掉
            if (mTrackCurrentGPSModels.size() > 0){
                lastTime = mTrackCurrentGPSModels.get(mTrackCurrentGPSModels.size() - 1).getTime();
                lastLat = mTrackCurrentGPSModels.get(mTrackCurrentGPSModels.size() - 1).getLat();
                lastLon = mTrackCurrentGPSModels.get(mTrackCurrentGPSModels.size() - 1).getLon();
                time_difference = DateUtils.data_6(currentTime) - DateUtils.data_6(lastTime);
                LatLng latLng_A = new LatLng((double) lastLat / 1000000, (double) lastLon / 1000000);
                LatLng latLng_B = new LatLng((double) model.getLat() / 1000000, (double) model.getLon() / 1000000);
                // 计算两点之间的距离
                distance = DistanceUtil.getDistance(latLng_A, latLng_B);
                // 计算两点之间的每秒平均速度
                speed_dis = distance / time_difference;
                if (speed_dis == 0 || speed_dis > 50) {
                    continue;
                }
            }

            mTrackCurrentGPSModels.add(model);
        }
        //绘制轨迹线
        ArrayList<LatLng> lineList = new ArrayList<>();
        int color = Color.rgb(47, 217, 167);
        LatLngBounds.Builder boundsBuilder;
        if (mTrackCurrentGPSModels.size() >= 2) {
            boundsBuilder = new LatLngBounds.Builder();
            int indexNumber = mTrackCurrentGPSModels.size() / Arrow_Point_Index;
            mArrowBeforeLat = 0;
            mArrowBeforeLon = 0;
            if (indexNumber > 0) {
                for (int i = 0; i < indexNumber; i++) {
                    int index = i * Arrow_Point_Index;
                    if (index < mTrackCurrentGPSModels.size()) {
                        if (i > 0) {
                            LatLng beforeLatLng = DeviceUtils.getBaiduLatLng(mArrowBeforeLat, mArrowBeforeLon);
                            LatLng newLatLng = DeviceUtils.getBaiduLatLng(mTrackCurrentGPSModels.get(index).getLat(), mTrackCurrentGPSModels.get(index).getLon());
                            float tmpDistance = (float) DistanceUtil.getDistance(beforeLatLng, newLatLng);
                            if (tmpDistance > Arrow_Distance) {
                                mArrowPointList.add(mTrackCurrentGPSModels.get(index));
                            }
                        }
                        mArrowBeforeLat = mTrackCurrentGPSModels.get(index).getLat();
                        mArrowBeforeLon = mTrackCurrentGPSModels.get(index).getLon();
                    }
                }
            }
            for (int i = 0; i < mTrackCurrentGPSModels.size(); i++) {
                LatLng point = DeviceUtils.getBaiduLatLng(mTrackCurrentGPSModels.get(i).getLat(), mTrackCurrentGPSModels.get(i).getLon());
                lineList.add(point);
                if (isClearTrack) {
                    boundsBuilder.include(point);
                }
            }
            // 绘制轨迹
            mBaiduMap.addOverlay(new PolylineOptions().points(lineList).width(12).color(color));

            //调整角度
            if (isClearTrack) {
                mLatLngBounds = boundsBuilder.build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(mLatLngBounds, 1000, 1000));
                mZoom = mBaiduMap.getMapStatus().zoom;
            }
        }else{
            //调整角度
            if (isClearTrack) {
                boundsBuilder = new LatLngBounds.Builder();
                for (int i = 0; i < mTrackCurrentGPSModels.size(); i++) {
                    LatLng point = DeviceUtils.getBaiduLatLng(mTrackCurrentGPSModels.get(i).getLat(), mTrackCurrentGPSModels.get(i).getLon());
                    boundsBuilder.include(point);
                }
                mLatLngBounds = boundsBuilder.build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(mLatLngBounds, 1000, 1000));
                mZoom = mBaiduMap.getMapStatus().zoom;
            }
        }
        mTrackAllModels.addAll(mTrackCurrentGPSModels);

        // 显示第一个点的信息
        if (isClearTrack) {
            if (mTrackAllModels.size() > 0) {
                onShowLocationInfo(true);
                DeviceTrackModel model = mTrackAllModels.get(0);
                tvLocationTime.setText(getString(R.string.time) + "：" + model.getTime());
                tvAddress.setText(getString(R.string.address) + "：" + ((double) model.getLat() / 1000000) + "," + ((double) model.getLon() / 1000000));

                //绘制第一个点   起点
                LatLng latLng = DeviceUtils.getBaiduLatLng(mTrackAllModels.get(0).getLat(), mTrackAllModels.get(0).getLon());
                Marker qiMarket = (Marker) mBaiduMap.addOverlay(new MarkerOptions().zIndex(1)
                        .position(latLng)
                        .title(mImei + "")
                        .icon(bitmapHelper.getBitmapZoomForStart(mZoom)));
                Bundle bundle = new Bundle();
                bundle.putString("snippet", getTrackDetailInfo(mTrackAllModels.get(0)));
                qiMarket.setExtraInfo(bundle);
                qiMarket.setToTop();
                mMarkerList.add(qiMarket);
            }
        }

        // 基站红点
        if (mTrackBaseLocationModels.size() > 0) {
            DeviceTrackModel routePointTmp;
            for (int j = 0; j < mTrackBaseLocationModels.size(); j++) {
                routePointTmp = mTrackBaseLocationModels.get(j);

                LatLng latLng = DeviceUtils.getBaiduLatLng(routePointTmp.getLat(), routePointTmp.getLon());
                Marker jzMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(latLng)
                        .title(mImei + "")
                        .visible(isShowBase)
                        .icon(bitmapHelper.getBitmapZoomForBaseStation(mZoom)));
                Bundle bundle = new Bundle();
                bundle.putString("snippet", getTrackDetailInfo(routePointTmp));
                jzMarker.setExtraInfo(bundle);
                mJzMarkerList.add(jzMarker);
            }
        }

        //5.绘制角度
        if (mArrowPointList.size() > 1) {
            Marker rotateMarker;
            DeviceTrackModel routePoint;
            LatLng pre;
            LatLng next;
            // 再循环
            for (int i = 1; i < mArrowPointList.size(); i++) {
                routePoint = mArrowPointList.get(i);
                pre = DeviceUtils.getBaiduLatLng(mArrowPointList.get(i - 1).getLat(), mArrowPointList.get(i - 1).getLon());
                next = DeviceUtils.getBaiduLatLng(routePoint.getLat(), routePoint.getLon());
                rotateMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(next)
                        .title(mImei + "")
                        .icon(bitmapHelper.getBitmapZoomForDirection(mZoom)));

                Bundle bundle = new Bundle();
                bundle.putString("snippet", getTrackDetailInfo(routePoint));
                rotateMarker.setExtraInfo(bundle);
                float rotate = Utils.getRotateBaidu(pre, next);
                rotateMarker.setRotate(360 - rotate + mBaiduMap.getMapStatus().rotate);
                mMarkerList.add(rotateMarker);
            }
        }

        // 优先判断当天是否查询完毕了，未查询完毕，继续查询，查询完毕，轮循下一个日期
        if (mTrackModels.size() >= Track_Number) {
            mTrack_Page++;
            tvData.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 继续下一段查询
                    onQuestTrackModels(false);
                }
            }, 100);
        } else {
            if (mTrackScreenDatas.size() > 0) {
                tvData.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 继续下一段查询
                        mQuestData = mTrackScreenDatas.get(0);
                        mTrackScreenDatas.remove(mQuestData);
                        onQuestTrackModels(false);
                    }
                }, 100);
            } else {
                isTrackDataComplete = true;

                // 终点
                if (mTrackAllModels.size() > 1) {
                    int endPosition = mTrackAllModels.size() - 1;
                    LatLng latLng = DeviceUtils.getBaiduLatLng(mTrackAllModels.get(endPosition).getLat(), mTrackAllModels.get(endPosition).getLon());
                    Marker endMarket = (Marker) mBaiduMap.addOverlay(new MarkerOptions().zIndex(1)
                            .position(latLng)
                            .title(mImei + "")
                            .icon(bitmapHelper.getBitmapZoomForEnd(mZoom)));
                    Bundle bundle = new Bundle();
                    bundle.putString("snippet", getTrackDetailInfo(mTrackAllModels.get(endPosition)));
                    endMarket.setExtraInfo(bundle);
                    endMarket.setToTop();
                    mMarkerList.add(endMarket);
                }
            }
        }
    }

    /**
     * 轨迹点的详细信息
     *
     * @param routePoint
     * @return
     */
    private String getTrackDetailInfo(DeviceTrackModel routePoint) {
        // 定位时间 + 经度 + 纬度
        return routePoint.getTime() + "," + routePoint.getLat() + "," + routePoint.getLon();
    }

    /**
     * 拖动结束点的信息
     */
    @SuppressLint("SetTextI18n")
    private void onSeekBarInfoShow() {
        if (playIndex < mTrackAllModels.size()) {
            DeviceTrackModel currentPoint = mTrackAllModels.get(playIndex);
            tvLocationTime.setText(getString(R.string.time) + currentPoint.getTime());
            tvAddress.setText(getString(R.string.address) + "：" + ((double) currentPoint.getLat() / 1000000) + ","
                    + ((double) currentPoint.getLon() / 1000000));
        }
    }

    @Override
    public void onBackPressed() {
        closeTimer();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
        if (mLocationClient != null) {
            mLocationClient.start();
        }
        // 开启方向传感器
        if (myOrientationListener != null) {
            myOrientationListener.start();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        // 关闭方向传感器
        if (myOrientationListener != null) {
            myOrientationListener.stop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopPlayTrack();
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        mBaiduMap.setMyLocationEnabled(false);
        if (mapView != null) {
            mapView.onDestroy();
        }
        mapView = null;
        super.onDestroy();
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
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_before_day, R.id.ll_data, R.id.tv_after_day, R.id.iv_base_station_switch, R.id.cv_clear_track, R.id.iv_play, R.id.iv_play_speed,
            R.id.tv_play_speed})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()) {
            switch (view.getId()) {
                case R.id.tv_before_day:
                    onBeforeDayTrack();
                    break;
                case R.id.tv_after_day:
                    onAfterDayTrack();
                    break;
                case R.id.ll_data:
                    onSelectData();
                    break;
                case R.id.iv_base_station_switch:
                    isShowBase = !isShowBase;
                    updateBaseStationSwitch();
                    break;
                case R.id.cv_clear_track:
                    onClearTrackConfirm();
                    break;
                case R.id.iv_play:
                    onPlayTrack();
                    break;
                case R.id.iv_play_speed:
                case R.id.tv_play_speed:
                    onPlaySpeed();
                    break;
            }
        }
    }

    /**
     * 从数据库删除轨迹数据确认
     */
    private void onClearTrackConfirm(){
        AlertBean bean = new AlertBean();
        bean.setTitle(getString(R.string.tip));
        bean.setAlert(getString(R.string.clear_track_hint));
        bean.setConfirmTip(getString(R.string.confirm));
        bean.setCancelTip(getString(R.string.cancel));
        AlertAppDialog dialog = new AlertAppDialog();
        dialog.show(getSupportFragmentManager(), bean, new AlertAppDialog.onAlertDialogChange() {
            @Override
            public void onConfirm() {
                onClearTrackForDB();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 从数据库删除轨迹数据
     */
    private void onClearTrackForDB() {
        showProgressDialog();
        cvClearTrack.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                ToastUtils.show(getString(R.string.clear_success));
                onResetTrackData();
            }
        }, 2000);
        try {
            trackBeanDao.deleteDeviceData(mImei);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 基站定位点显示开关
     */
    private void updateBaseStationSwitch() {
        ivBaseStationSwitch.setImageResource(isShowBase ? R.drawable.icon_switch_on_two : R.drawable.icon_switch_off_two);
        //加载基站信息
        if (mJzMarkerList != null) {
            for (int i = 0; i < mJzMarkerList.size(); ++i) {
                mJzMarkerList.get(i).setVisible(isShowBase);
            }
        }
    }

    /**
     * 快进播放，播放速度，1：快，播放速度为90，2：中，播放速度为50，3：慢，播放速度为10，默认快速
     */
    private void onPlaySpeed() {
        if (playType == 1) {
            playType = 2;
            playSpeed = 40;
            tvPlaySpeed.setText(getString(R.string.medium_speed));
        } else if (playType == 2) {
            playType = 3;
            playSpeed = 10;
            tvPlaySpeed.setText(getString(R.string.slow));
        } else {
            playType = 1;
            playSpeed = 80;
            tvPlaySpeed.setText(getString(R.string.fast));
        }

        playLongTime = 50 + (10 * (100 - playSpeed));
        pausePlayTrack();
    }

    /**
     * 播放轨迹/停止播放
     */
    private void onPlayTrack() {
        if (isPauseNow) {
            pausePlayTrack();
        } else {
            playTrack();
        }
    }

    private void pausePlayTrack() {
        closeTimer();
        isPauseNow = false;
        ivPlay.setImageResource(R.drawable.icon_track_play);
    }

    private void closeTimer() {
        if (onTimerPlay != null) {
            onTimerPlay.cancel();
            onTimerPlay = null;
        }
    }

    /**
     * 前一天的数据
     */
    @SuppressLint("SetTextI18n")
    private void onBeforeDayTrack() {
        if (!isTrackDataComplete) {
            ToastUtils.show(getString(R.string.track_data_quest_hint));
            return;
        }

        if (currentSelectDay == null) {
            currentSelectDay = java.util.Calendar.getInstance(Locale.ENGLISH);
        }

        currentSelectDay.add(java.util.Calendar.DAY_OF_MONTH, -1);

        mQuestData = currentSelectDay.get(java.util.Calendar.YEAR) + "-" +
                (currentSelectDay.get(java.util.Calendar.MONTH) + 1) + "-" +
                (currentSelectDay.get(java.util.Calendar.DAY_OF_MONTH));
        tvData.setText(mQuestData);

        mTrackScreenDatas.clear();
        onQuestTrackModels(true);
    }

    /**
     * 后一天的数据
     */
    @SuppressLint("SetTextI18n")
    private void onAfterDayTrack() {
        if (!isTrackDataComplete) {
            ToastUtils.show(getString(R.string.track_data_quest_hint));
            return;
        }
        if (currentSelectDay == null) {
            currentSelectDay = java.util.Calendar.getInstance(Locale.ENGLISH);
        }
        currentSelectDay.add(java.util.Calendar.DAY_OF_MONTH, 1);
        java.util.Calendar tmpDate = java.util.Calendar.getInstance(Locale.ENGLISH);
        if (currentSelectDay.compareTo(tmpDate) > 0) {
            currentSelectDay.add(java.util.Calendar.DAY_OF_MONTH, -1);
            ToastUtils.show(getString(R.string.max_track_date));
            return;
        }

        mQuestData = currentSelectDay.get(java.util.Calendar.YEAR) + "-" +
                (currentSelectDay.get(java.util.Calendar.MONTH) + 1) + "-" +
                (currentSelectDay.get(java.util.Calendar.DAY_OF_MONTH));
        tvData.setText(mQuestData);

        mTrackScreenDatas.clear();
        onQuestTrackModels(true);
    }

    /**
     * 选择日期
     */
    private void onSelectData() {
        if (currentDay == null) {
            currentDay = new Calendar();
        }
        currentDay.setYear(currentSelectDay.get(java.util.Calendar.YEAR));
        currentDay.setMonth(currentSelectDay.get(java.util.Calendar.MONTH) + 1);
        currentDay.setDay(currentSelectDay.get(java.util.Calendar.DAY_OF_MONTH));
        dateSelectPopupWindow = new DateSelectPopupWindow(this, trackDateList, currentDay);
        dateSelectPopupWindow.setCallbackAction(new DateSelectPopupWindow.CallbackAction() {
            @SuppressLint("SetTextI18n")
            @Override
            public void OnSelectDate(List<Calendar> calendarList) {
                if (!isTrackDataComplete) {
                    ToastUtils.show(getString(R.string.track_data_quest_hint));
                    return;
                }
                java.util.Calendar compareCalendar = java.util.Calendar.getInstance(Locale.ENGLISH);
                Calendar currentDate = new Calendar();
                currentDate.setDay(compareCalendar.get(java.util.Calendar.DAY_OF_MONTH));
                currentDate.setMonth(compareCalendar.get(java.util.Calendar.MONTH) + 1);
                currentDate.setYear(compareCalendar.get(java.util.Calendar.YEAR));
                boolean isRet = false;
                for (int i = 0; i < calendarList.size(); ++i) {
                    if (calendarList.get(i).compareTo(currentDate) > 0) {
                        isRet = true;
                        break;
                    }
                }

                if (isRet) {
                    ToastUtils.show(getString(R.string.max_track_date));
                    return;
                }

                dateSelectPopupWindow.dismiss();
                selectTrackDateList.clear();
                selectTrackDateList.addAll(calendarList);

                if (selectTrackDateList.size() == 1) {
                    //单日
                    currentSelectDay.set(
                            selectTrackDateList.get(0).getYear(),
                            selectTrackDateList.get(0).getMonth() - 1,
                            selectTrackDateList.get(0).getDay(),
                            0,
                            0,
                            0);

                    mQuestData = selectTrackDateList.get(0).getYear() + "-" +
                            selectTrackDateList.get(0).getMonth() + "-" +
                            selectTrackDateList.get(0).getDay();

                    tvData.setText(mQuestData);

                    mTrackScreenDatas.clear();
                    onQuestTrackModels(true);
                } else {
                    //多日
                    currentSelectDay.set(
                            selectTrackDateList.get(0).getYear(),
                            selectTrackDateList.get(0).getMonth() - 1,
                            selectTrackDateList.get(0).getDay(),
                            0,
                            0,
                            0);

                    String startTime = selectTrackDateList.get(0).getYear() + "-" +
                            selectTrackDateList.get(0).getMonth() + "-" +
                            selectTrackDateList.get(0).getDay();
                    String endTime = selectTrackDateList.get(selectTrackDateList.size() - 1).getYear() + "-" +
                            selectTrackDateList.get(selectTrackDateList.size() - 1).getMonth() + "-" +
                            selectTrackDateList.get(selectTrackDateList.size() - 1).getDay();
                    tvData.setText(startTime + "\n" + endTime);

                    mTrackScreenDatas.clear();
                    for (int i = 0; i < selectTrackDateList.size(); i++) {
                        String time = selectTrackDateList.get(i).getYear() + "-" +
                                selectTrackDateList.get(i).getMonth() + "-" +
                                selectTrackDateList.get(i).getDay();
                        mTrackScreenDatas.add(time);
                    }

                    if (mTrackScreenDatas.size() > 0) {
                        mQuestData = mTrackScreenDatas.get(0);
                        mTrackScreenDatas.remove(mQuestData);
                        onQuestTrackModels(true);
                    }
                }
                seekbarProcess.setProgress(1);
            }
        });
        dateSelectPopupWindow.showAsDropDown(viewTitle, 0, 0);
    }

    // ------------------------------ 播放动画 --------------------------------

    /**
     * 播放动画
     */
    @SuppressLint("SetTextI18n")
    private void playTrack() {
        try {
            closeTimer();

            if (mTrackAllModels == null || mTrackAllModels.size() < 3) {
                ToastUtils.show(getString(R.string.not_track_play));
                return;
            }
            LatLng point = DeviceUtils.getBaiduLatLng(mTrackAllModels.get(playIndex).getLat(), mTrackAllModels.get(playIndex).getLon());
            if (playCurrrentMarker == null) {
                playCurrrentMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().zIndex(100)
                        .position(point)
                        .title(mImei + "")
                        .anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_device_line_on)));
            }

            ivPlay.setImageResource(R.drawable.icon_track_play_stop);
            if (playIndex == 0) {
                DeviceTrackModel currentPoint = mTrackAllModels.get(0);
                tvLocationTime.setText(getString(R.string.time) + "：" + currentPoint.getTime());
                tvAddress.setText(getString(R.string.address) + "：" + ((double) currentPoint.getLat() / 1000000) + ","
                        + ((double) currentPoint.getLon() / 1000000));
            }
            try {
                onTimerPlay = new Timer();
                onTimerPlay.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ivPlay.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                onTrackPlayNow();
                            }
                        }, 100);
                    }
                }, 0, playLongTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放轨迹
     */
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void onTrackPlayNow() {
        playIndex++;
        if (playIndex <= mTrackAllModels.size() - 1) {
            seekbarProcess.setProgress(playIndex * 100 / mTrackAllModels.size());
            isPauseNow = true;
            LatLng point = DeviceUtils.getBaiduLatLng(mTrackAllModels.get(playIndex).getLat(), mTrackAllModels.get(playIndex).getLon());
            playCurrrentMarker.setPosition(point);
            tvLocationTime.setText(getString(R.string.time) + mTrackAllModels.get(playIndex).getTime());
            tvAddress.setText(getString(R.string.address) + "：" + ((double) mTrackAllModels.get(playIndex).getLat() / 1000000) + ","
                    + ((double) mTrackAllModels.get(playIndex).getLon() / 1000000));

            targetLatLng = point;
            updateMapCenter();
        } else {
            isPauseNow = false;
            ivPlay.setImageResource(R.drawable.icon_track_play);
            closeTimer();
            playIndex = 0;
        }
    }

    /**
     * 停止播放
     */
    private void stopPlayTrack() {
        playIndex = 0;
        seekbarProcess.setProgress(1);
        closeTimer();
        isPauseNow = false;
        ivPlay.setImageResource(R.drawable.icon_track_play);
    }

}
