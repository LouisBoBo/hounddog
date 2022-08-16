package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.slxk.hounddog.di.component.DaggerTrackBaiduComponent;
import com.slxk.hounddog.mvp.contract.TrackBaiduContract;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.model.bean.RoutePointBaidu;
import com.slxk.hounddog.mvp.model.bean.TrackHasDataResultBean;
import com.slxk.hounddog.mvp.model.bean.TrackListResultBean;
import com.slxk.hounddog.mvp.model.putbean.TrackHasDataPutBean;
import com.slxk.hounddog.mvp.model.putbean.TrackListPutBean;
import com.slxk.hounddog.mvp.presenter.TrackBaiduPresenter;
import com.slxk.hounddog.mvp.ui.view.data.haibin.Calendar;
import com.slxk.hounddog.mvp.utils.AddressParseUtil;
import com.slxk.hounddog.mvp.utils.BitmapHelperBaidu;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.DateUtils;
import com.slxk.hounddog.mvp.utils.GpsUtils;
import com.slxk.hounddog.mvp.utils.LocationAddressParsedBaidu;
import com.slxk.hounddog.mvp.utils.MyOrientationListener;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.AlertAppDialog;
import com.slxk.hounddog.mvp.weiget.DateSelectPopupWindow;

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
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/19/2022 16:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class TrackBaiduActivity extends BaseActivity<TrackBaiduPresenter> implements TrackBaiduContract.View,
        BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener, BaiduMap.OnMapStatusChangeListener {

    @BindView(R.id.view_title)
    View viewTitle;
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
    @BindView(R.id.cv_base_station_switch)
    CardView cvBaseStationSwitch;
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
    @BindView(R.id.ll_location_info)
    LinearLayout llLocationInfo;

    public static final String Intent_Simei_Key = "simei_key";
    public static final String Intent_Name_Key = "name_key";
    public static final String Intent_Imei_Key = "imei_key";
    private String mSimei; // 设备imei号
    private String mDeviceName = ""; // 设备名称
    private String mImei; // 设备imei号

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

    private long startTimeForQuest; // 开始时间
    private long endTimeForQuest; // 结束时间
    private String startData; // 开始日期
    private String endData; // 结束日期
    private String startHour = "00:00"; // 开始时间后缀
    private String endHour = "23:59"; // 结束时间后缀
    private int mLimitSize = 2000; // 限制获取数量
    private long mLastTime = 0; // 最后时间
    private boolean isTrackDataComplete = false; // 是否获取完当前的数据
    private LatLngBounds mLatLngBounds; // 地图内可见经纬度

    private ArrayList<Long> trackDataBeans; // 轨迹日期数据

    private boolean isShowBase = false; // 是否显示基站
    private DateSelectPopupWindow dateSelectPopupWindow;

    private ArrayList<Calendar> trackDateList;
    private ArrayList<Calendar> selectTrackDateList;
    private java.util.Calendar currentSelectDay; // 请求的日期的第一个，包括多日期请求
    private Calendar currentDay; // 请求的日期的第一个，包括多日期请求，用于显示在日历上

    static class MapInfoWindow {
        public View viewInfoWindows;
        public TextView tvMarkerImei; // 设备号
        public TextView tvMarkerName; // 设备名称
        public TextView tvMarkerTime; // 打点时间
        public TextView tvMarkerAddress; // 地址
    }

    private MapInfoWindow mapInfoWindow = new MapInfoWindow();

    private ArrayList<RoutePointBaidu> jzRouteList;//基站点
    private ArrayList<RoutePointBaidu> jzRouteListForSegmented;//基站点
    private ArrayList<RoutePointBaidu> wifiRoutePointList; // wifi点
    private ArrayList<RoutePointBaidu> wifiRoutePointListForSegmented; // wifi点
    private ArrayList<RoutePointBaidu> playData; // 总数据
    private ArrayList<RoutePointBaidu> playDataForSegmented; // 分段数据-用来计算总数据的
    private ArrayList<RoutePointBaidu> arrowPointList; // 方向箭头
    private ArrayList<Marker> markerList;//保存地图中所有图标的列表
    private ArrayList<Marker> jzmarkerList;//保存地图中所有图标的列表
    private Marker currentMarker, qiMarket, zhongMarket, playCurrrentMarker;//当前marker   起点   终点  停车点
    private Marker jzMarker;
    private BitmapHelperBaidu bitmapHelper;

    private boolean isPauseNow = false;//是否暂停动画
    private int playSpeed = 80; // 播放速度
    private int playType = 1; // 播放速度，1：快，播放速度为90，2：中，播放速度为50，3：慢，播放速度为10，默认快速
    private int playIndex = 0; // 播放进度条上的进度
    private Timer onTimerPlay = null;
    private int playLongTime = 1000; // 播放轨迹间隔时长

    private int drawableId = R.drawable.icon_device_line_on; // 播放轨迹图标

    private MyOrientationListener myOrientationListener; // 手机传感器方向
    private float mXDirection = 0;
    private float mCurrentAccracy;
    private String mCompassImei = "00000000"; // 罗盘虚拟imei号，用来判断是否点击了罗盘
    private HashMap<String, Marker> deviceMap; // 存储任务

    public static Intent newInstance(String imei, String simei, String name) {
        Intent intent = new Intent(MyApplication.getMyApp(), TrackBaiduActivity.class);
        intent.putExtra(Intent_Simei_Key, simei);
        intent.putExtra(Intent_Name_Key, name);
        intent.putExtra(Intent_Imei_Key, imei);
        return intent;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTrackBaiduComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_track_baidu;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.track));
        mSimei = getIntent().getStringExtra(Intent_Simei_Key);
        mDeviceName = getIntent().getStringExtra(Intent_Name_Key);
        mImei = getIntent().getStringExtra(Intent_Imei_Key);

        trackDataBeans = new ArrayList<>();
        trackDateList = new ArrayList<>();
        selectTrackDateList = new ArrayList<>();
        jzRouteList = new ArrayList<>();
        jzRouteListForSegmented = new ArrayList<>();
        markerList = new ArrayList<>();
        jzmarkerList = new ArrayList<>();
        playData = new ArrayList<>();
        playDataForSegmented = new ArrayList<>();
        wifiRoutePointList = new ArrayList<>();
        wifiRoutePointListForSegmented = new ArrayList<>();
        arrowPointList = new ArrayList<>();
        deviceMap = new HashMap<>();
        tvData.setText(DateUtils.getTodayDateTime_3());
        cvClearTrack.setVisibility(View.GONE);

        playLongTime = 50 + (10 * (100 - playSpeed));

        currentSelectDay = java.util.Calendar.getInstance(Locale.ENGLISH);
        bitmapHelper = new BitmapHelperBaidu(this);

        onShowLocationInfo(false);
        setDataForTrack("", "");
        initMap();
        loadMapInfoWindow();
        onSeekBarProcess();
        updateBaseStationSwitch();

        getTrackHasForData();
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
                if (playData != null && playData.size() > 3) {
                    playIndex = playData.size() * seekBar.getProgress() / 100;
                    if (playIndex >= playData.size()) {
                        playIndex = playData.size() - 1;
                    }
                    onSeekBarInfoShow();
                    isPauseNow = true;
                    ivPlay.setImageResource(R.drawable.icon_track_play);
                    onPlayTrack();

                    if (playCurrrentMarker == null) {
                        playCurrrentMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().zIndex(100)
                                .position(playData.get(playIndex).point)
                                .title(mImei + "")
                                .anchor(0.5f, 0.5f)
                                .icon(BitmapDescriptorFactory.fromResource(drawableId)));
                    } else {
                        playCurrrentMarker.setPosition(playData.get(playIndex).point);
                    }
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(playData.get(playIndex).point).zoom(mZoom).build()));
                }
            }
        });
    }

    /**
     * 拖动结束点的信息
     */
    @SuppressLint("SetTextI18n")
    private void onSeekBarInfoShow() {
        if (playIndex < playData.size()) {
            RoutePointBaidu currentPoint = playData.get(playIndex);
            AddressParseUtil.getBaiduAddress(this, tvAddress, currentPoint.getLat(), currentPoint.getLon(), getString(R.string.address) + "：");

            tvLocationTime.setText(getString(R.string.time) + "：" + DateUtils.timedate(String.valueOf(currentPoint.getTime())));
            tvSpeed.setText(getString(R.string.speed) + "：" + ((double) currentPoint.getSpeed() / 10) + "km/h");
        }
    }

    /**
     * 基站定位点显示开关
     */
    private void updateBaseStationSwitch() {
        ivBaseStationSwitch.setImageResource(isShowBase ? R.drawable.icon_switch_on_two : R.drawable.icon_switch_off_two);
        //加载基站信息
        if (jzmarkerList != null) {
            for (int i = 0; i < jzmarkerList.size(); ++i) {
                jzmarkerList.get(i).setVisible(isShowBase);
            }
        }
    }

    /**
     * 初始化时间，重置筛选时间
     *
     * @param start 开始日期
     * @param end   结束日期
     */
    @SuppressLint("SetTextI18n")
    private void setDataForTrack(String start, String end) {
        if (TextUtils.isEmpty(start) && TextUtils.isEmpty(end)) {
            startData = DateUtils.getTodayDateTime_3();
            endData = DateUtils.getTodayDateTime_3();
        } else {
            startData = start;
            endData = end;
        }
        startTimeForQuest = Long.parseLong(DateUtils.data_2(startData + " " + startHour + ":00"));
        endTimeForQuest = Long.parseLong(DateUtils.data_2(endData + " " + endHour + ":59"));
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
     * 查询有轨迹的日期
     */
    private void getTrackHasForData() {
        TrackHasDataPutBean.ParamsBean paramsBean = new TrackHasDataPutBean.ParamsBean();
        paramsBean.setSimei(mSimei);

        TrackHasDataPutBean bean = new TrackHasDataPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Track_Data_Get);
        bean.setModule(ModuleValueService.Module_For_Track_Data_Get);

        if (getPresenter() != null) {
            getPresenter().getTrackHasForData(bean);
        }
    }

    /**
     * 获取轨迹列表
     *
     * @param isShow      是否显示加载框
     * @param isResetData 是否是请求新的一天轨迹数据，true:请求新的一天数据，false:请求同一天的轨迹数据，后续的数据
     */
    private void getTrackList(boolean isShow, boolean isResetData) {
        TrackListPutBean.ParamsBean paramsBean = new TrackListPutBean.ParamsBean();
        paramsBean.setSimei(mSimei);
        paramsBean.setTime_end(endTimeForQuest);
        paramsBean.setTime_begin(startTimeForQuest);
        paramsBean.setLimit_size(mLimitSize);
        if (mLastTime != 0) {
            paramsBean.setLast_time(mLastTime);
        }

        TrackListPutBean bean = new TrackListPutBean();
        bean.setFunc(ModuleValueService.Fuc_For_Track_List_Data);
        bean.setModule(ModuleValueService.Module_For_Track_List_Data);
        bean.setParams(paramsBean);

        if (getPresenter() != null) {
            getPresenter().getTrackList(bean, isShow, isResetData);
        }
    }

    /**
     * 重置数据
     *
     * @param start 开始时间
     * @param end   结束时间
     */
    private void onResetData(String start, String end) {
        stopPlayTrack();
        ivPlay.postDelayed(new Runnable() {
            @Override
            public void run() {
                onClearData();
                mLastTime = 0;
                isTrackDataComplete = false;
                setDataForTrack(start, end);

                getTrackList(true, true);
            }
        }, 300);
    }

    /**
     * 清除数据，恢复原来的数据
     */
    private void onClearData() {
        isPauseNow = false;
        playData.clear();
        mBaiduMap.clear();
        deviceMap.clear();
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLocationClient != null) {
                    mLocationClient.start();
                }
            }
        }, 1000);

        if (playCurrrentMarker != null) {
            playCurrrentMarker.remove();
            playCurrrentMarker = null;
        }

        if (qiMarket != null) {
            qiMarket.remove();
            qiMarket = null;
        }

        if (zhongMarket != null) {
            zhongMarket.remove();
            zhongMarket = null;
        }

        if (currentMarker != null) {
            currentMarker.remove();
            currentMarker = null;
        }
        stopPlayTrack();
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
        handler.removeCallbacksAndMessages(null);
        handler = null;
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
                    mapInfoWindow.tvMarkerAddress.setVisibility(View.VISIBLE);
                    mapInfoWindow.tvMarkerTime.setText(getString(R.string.time) + "：" + detailInfo[0]);
                    LocationAddressParsedBaidu.getLocationParsedInstance()
                            .Parsed(marker.getPosition().latitude, marker.getPosition().longitude)
                            .setAddressListener(new LocationAddressParsedBaidu.getAddressListener() {
                                @Override
                                public void getAddress(String address) {
                                    mapInfoWindow.tvMarkerAddress.setText(getString(R.string.address) + "：" + address);
                                }
                            });
                } else {
                    mapInfoWindow.tvMarkerTime.setText(getString(R.string.time));
                    mapInfoWindow.tvMarkerTime.setVisibility(View.GONE);
                    mapInfoWindow.tvMarkerAddress.setText(getString(R.string.address));
                    mapInfoWindow.tvMarkerAddress.setVisibility(View.GONE);
                }
            }
            targetLatLng = marker.getPosition();
            updateMapCenter();
            mBaiduMap.showInfoWindow(new InfoWindow(mapInfoWindow.viewInfoWindows, marker.getPosition(), -80));
        }
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

    @OnClick({R.id.tv_before_day, R.id.ll_data, R.id.tv_after_day, R.id.cv_base_station_switch, R.id.iv_play, R.id.iv_play_speed, R.id.tv_play_speed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_before_day:
                onBeforeDayTrack();
                break;
            case R.id.ll_data:
                onSelectData();
                break;
            case R.id.tv_after_day:
                onAfterDayTrack();
                break;
            case R.id.cv_base_station_switch:
                isShowBase = !isShowBase;
                updateBaseStationSwitch();
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

                    String dataTime = selectTrackDateList.get(0).getYear() + "-" +
                            selectTrackDateList.get(0).getMonth() + "-" +
                            selectTrackDateList.get(0).getDay();

                    tvData.setText(dataTime);

                    onResetData(dataTime, dataTime);
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

                    onResetData(startTime, endTime);
                }
                seekbarProcess.setProgress(1);
            }
        });
        dateSelectPopupWindow.showAsDropDown(viewTitle, 0, 0);
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

        String beforeTime = currentSelectDay.get(java.util.Calendar.YEAR) + "-" +
                (currentSelectDay.get(java.util.Calendar.MONTH) + 1) + "-" +
                (currentSelectDay.get(java.util.Calendar.DAY_OF_MONTH));

        tvData.setText(beforeTime);
        startHour = "00:00"; // 开始时间后缀
        endHour = "23:59"; // 结束时间后缀

        onResetData(beforeTime, beforeTime);
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

        String afterTime = currentSelectDay.get(java.util.Calendar.YEAR) + "-" +
                (currentSelectDay.get(java.util.Calendar.MONTH) + 1) + "-" +
                (currentSelectDay.get(java.util.Calendar.DAY_OF_MONTH));

        tvData.setText(afterTime);
        startHour = "00:00"; // 开始时间后缀
        endHour = "23:59"; // 结束时间后缀

        onResetData(afterTime, afterTime);
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

    @Override
    public void getTrackHasForDataSuccess(TrackHasDataResultBean trackHasDataResultBean) {
        trackDataBeans.clear();
        if (trackDateList != null) {
            trackDateList.clear();
        }
        if (trackHasDataResultBean.getDate() != null) {
            trackDataBeans.addAll(trackHasDataResultBean.getDate());
        }
        boolean isHasTodayTrack = false; // 是否有当天的轨迹
        String today = DateUtils.getTodayDateTime_3();
        for (int i = 0; i < trackDataBeans.size(); i++) {
            String day = DateUtils.timedate_2(String.valueOf(trackDataBeans.get(i)));
            if (day.equals(today)) {
                isHasTodayTrack = true;
            }
            String[] dayDayas = day.split("-");
            trackDateList.add(getCalendar(Integer.parseInt(dayDayas[0]), Integer.parseInt(dayDayas[1]), Integer.parseInt(dayDayas[2])));
        }


        if (trackDataBeans.size() == 0) {
            onEndMoreData();
            currentSelectDay = java.util.Calendar.getInstance(Locale.ENGLISH);
            ToastUtils.show(getString(R.string.no_trace_data));
        } else {
            if (isHasTodayTrack) {
                currentSelectDay = java.util.Calendar.getInstance(Locale.ENGLISH);
                onResetData(today, today);
            } else {
                AlertBean bean = new AlertBean();
                bean.setTitle(getString(R.string.tip));
                bean.setAlert(getString(R.string.no_trace_data_ex));
                bean.setConfirmTip(getString(R.string.confirm));
                bean.setCancelTip(getString(R.string.cancel));
                AlertAppDialog dialog = new AlertAppDialog();
                dialog.show(getSupportFragmentManager(), bean, new AlertAppDialog.onAlertDialogChange() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onConfirm() {
                        String mostRecentDate = DateUtils.timedate_2(String.valueOf(trackDataBeans.get(trackDataBeans.size() - 1))); // 最近的日期
                        String[] dayDayas = mostRecentDate.split("-");
                        currentSelectDay = java.util.Calendar.getInstance(Locale.ENGLISH);
                        currentSelectDay.set(
                                Integer.parseInt(dayDayas[0]),
                                Integer.parseInt(dayDayas[1]) - 1,
                                Integer.parseInt(dayDayas[2]),
                                0,
                                0,
                                0);
                        tvData.setText(dayDayas[0] + "-" + dayDayas[1] + "-" + dayDayas[2]);
                        onResetData(mostRecentDate, mostRecentDate);
                    }

                    @Override
                    public void onCancel() {
                        onEndMoreData();
                        currentSelectDay = java.util.Calendar.getInstance(Locale.ENGLISH);
                    }
                });
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getTrackListSuccess(TrackListResultBean trackListResultBean, boolean isResetData) {
        if (isResetData) {
            jzRouteList.clear();
            playData.clear();
            wifiRoutePointList.clear();
            markerList.clear();
            jzmarkerList.clear();
            mLatLngBounds = null;
        }
        playDataForSegmented.clear();
        jzRouteListForSegmented.clear();
        wifiRoutePointListForSegmented.clear();
        arrowPointList.clear();

        if (trackListResultBean.getData() == null || (trackListResultBean.getData().size() == 0 && playData.size() == 0)) {
            onEndMoreData();
            ToastUtils.show(getString(R.string.no_trace_data));
            onShowLocationInfo(false);
            return;
        }

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (int i = 0; i < trackListResultBean.getData().size(); i++) {
            TrackListResultBean.DataBean bean = trackListResultBean.getData().get(i);
            double lat = (double) bean.getLat() / 1000000;
            double lon = (double) bean.getLon() / 1000000;
            RoutePointBaidu routePoint = new RoutePointBaidu(new LatLng(lat, lon), bean.getType(), bean.getTime(), bean.getSpeed(),
                    lat, lon, bean.getDirection(), bean.getPtype(), bean.getStart_time(), bean.getDistance(), bean.getDuration(), bean.getEnd_time());
            if (bean.getType() == ResultDataUtils.Location_Base_Station_Track || bean.getType() == ResultDataUtils.Location_Static_Base_Station_Track) {
                jzRouteListForSegmented.add(routePoint);
            } else if (bean.getType() == ResultDataUtils.Location_GPS_Track || bean.getType() == ResultDataUtils.Location_Static_Gps_Track) {
                boundsBuilder.include(routePoint.getPoint());
                playDataForSegmented.add(routePoint);
            } else {
                wifiRoutePointListForSegmented.add(routePoint);
            }
        }
        if (isResetData) {
            if (playDataForSegmented.size() == 0) {
                if (wifiRoutePointListForSegmented.size() > 0) {
                    for (int i = 0; i < wifiRoutePointListForSegmented.size(); i++) {
                        boundsBuilder.include(wifiRoutePointListForSegmented.get(i).getPoint());
                    }
                } else {
                    for (int i = 0; i < jzRouteListForSegmented.size(); i++) {
                        boundsBuilder.include(jzRouteListForSegmented.get(i).getPoint());
                    }
                }
            }
            mLatLngBounds = boundsBuilder.build();
            //调整角度
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(mLatLngBounds, 1000, 1000));
            mZoom = mBaiduMap.getMapStatus().zoom;
        }

        if (arrowPointList.size() == 0) {
            if (playDataForSegmented.size() > 0) {
                arrowPointList.add(playDataForSegmented.get(0));
                for (int i = 1; i < playDataForSegmented.size(); i++) {
                    RoutePointBaidu lastPoint = arrowPointList.get(arrowPointList.size() - 1);
                    float tmpDistance = (float) DistanceUtil.getDistance(lastPoint.getPoint(), playDataForSegmented.get(i).getPoint());
                    if (tmpDistance > 500) {
                        arrowPointList.add(playDataForSegmented.get(i));
                    }
                }
            }
        } else {
            for (int i = 0; i < playDataForSegmented.size(); i++) {
                RoutePointBaidu lastPoint = arrowPointList.get(arrowPointList.size() - 1);
                float tmpDistance = (float) DistanceUtil.getDistance(lastPoint.getPoint(), playDataForSegmented.get(i).getPoint());
                if (tmpDistance > 500) {
                    arrowPointList.add(playDataForSegmented.get(i));
                }
            }
        }

        //绘制轨迹线
        ArrayList<LatLng> lineList = new ArrayList<>();
        int color = Color.rgb(47, 217, 167);
        if (playData.size() > 0) {
            playDataForSegmented.add(0, playData.get(playData.size() - 1));
        }
        if (playDataForSegmented.size() >= 2) {
            for (int i = 0; i < playDataForSegmented.size(); i++) {
                lineList.add(playDataForSegmented.get(i).getPoint());
            }
            mBaiduMap.addOverlay(new PolylineOptions().points(lineList).width(12).color(color));
        }

        // 添加分段数据到总数据中 - 绘制轨迹数据
        playData.addAll(playDataForSegmented);
        // 添加分段数据到总数据中 - 基站点数据
        jzRouteList.addAll(jzRouteListForSegmented);
        // 添加分段数据到总数据中 - wifi点数据
        wifiRoutePointList.addAll(wifiRoutePointListForSegmented);

        // 展示第一个点的信息
        if (isResetData) {
            if (playData.size() > 0) {
                onShowLocationInfo(true);
                RoutePointBaidu currentPoint = playData.get(0);
                AddressParseUtil.getBaiduAddress(this, tvAddress, currentPoint.getLat(), currentPoint.getLon(), getString(R.string.address) + "：");

                tvLocationTime.setText(getString(R.string.time) + "：" + DateUtils.timedate(String.valueOf(currentPoint.getTime())));
                tvSpeed.setText(getString(R.string.speed) + "：" + ((double) currentPoint.getSpeed() / 10) + "km/h");
            }
        }

        //3.绘制最后一个点   终点
        if (isTrackDataComplete) {
            // 如果是第一页就加载完了，则判断从总数据索引 1  -  size-1 中间段执行
            if (isResetData) {
                for (int i = 1; i < playData.size() - 1; i++) {
                    if (playData.get(i).getPtype() == 1) {
                        Marker rotateMarker;
                        rotateMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().zIndex(1)
                                .position(playData.get(i).point)
                                .title(mImei + "")
                                .icon(bitmapHelper.getBitmapZoomParkingForGPS(mZoom)));
                        Bundle bundle = new Bundle();
                        bundle.putString("snippet", getTrackDetailInfo(playData.get(i)));
                        rotateMarker.setExtraInfo(bundle);
//                        rotateMarker.setToTop();
                        markerList.add(rotateMarker);
                    }
                }
            } else {
                // 如果是第二页++ 加载完成，则判断从当前页数据的索引  0  -   size-1  中间段执行
                for (int i = 0; i < playDataForSegmented.size() - 1; i++) {
                    if (playDataForSegmented.get(i).getPtype() == 1) {
                        Marker rotateMarker;
                        rotateMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().zIndex(1)
                                .position(playDataForSegmented.get(i).point)
                                .title(mImei + "")
                                .icon(bitmapHelper.getBitmapZoomParkingForGPS(mZoom)));
                        Bundle bundle = new Bundle();
                        bundle.putString("snippet", getTrackDetailInfo(playDataForSegmented.get(i)));
                        rotateMarker.setExtraInfo(bundle);
//                        rotateMarker.setToTop();
                        markerList.add(rotateMarker);
                    }
                }
            }

            // 终点
            if (playData.size() > 1) {
                int endPosition = playData.size() - 1;
                int lastOneType = playData.get(endPosition).type;
                zhongMarket = (Marker) mBaiduMap.addOverlay(new MarkerOptions().zIndex(1)
                        .position(playData.get(endPosition).point)
                        .title(mImei + "")
                        .icon(bitmapHelper.getBitmapZoomForEnd(mZoom)));
                Bundle bundle = new Bundle();
                bundle.putString("snippet", getTrackDetailInfo(playData.get(endPosition)));
                zhongMarket.setExtraInfo(bundle);
                zhongMarket.setToTop();
                markerList.add(zhongMarket);
            }
        } else {
            if (isResetData) {
                for (int i = 1; i < playDataForSegmented.size(); i++) {
                    if (playDataForSegmented.get(i).getPtype() == 1) {
                        Marker rotateMarker;
                        rotateMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().zIndex(1)
                                .position(playDataForSegmented.get(i).point)
                                .title(mImei + "")
                                .icon(bitmapHelper.getBitmapZoomParkingForGPS(mZoom)));
                        Bundle bundle = new Bundle();
                        bundle.putString("snippet", getTrackDetailInfo(playDataForSegmented.get(i)));
                        rotateMarker.setExtraInfo(bundle);
//                        rotateMarker.setToTop();
                        markerList.add(rotateMarker);
                    }
                }
            } else {
                for (int i = 0; i < playDataForSegmented.size(); i++) {
                    if (playDataForSegmented.get(i).getPtype() == 1) {
                        Marker rotateMarker;
                        rotateMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().zIndex(1)
                                .position(playDataForSegmented.get(i).point)
                                .title(mImei + "")
                                .icon(bitmapHelper.getBitmapZoomParkingForGPS(mZoom)));
                        Bundle bundle = new Bundle();
                        bundle.putString("snippet", getTrackDetailInfo(playDataForSegmented.get(i)));
                        rotateMarker.setExtraInfo(bundle);
//                        rotateMarker.setToTop();
                        markerList.add(rotateMarker);
                    }
                }
            }
        }

        //2.绘制第一个点   起点
        if (isResetData) {
            if (playData.size() > 0) {
                int firstType = playData.get(0).type;
                qiMarket = (Marker) mBaiduMap.addOverlay(new MarkerOptions().zIndex(1)
                        .position(playData.get(0).point)
                        .title(mImei + "")
                        .icon(bitmapHelper.getBitmapZoomForStart(mZoom)));
                Bundle bundle = new Bundle();
                bundle.putString("snippet", getTrackDetailInfo(playData.get(0)));
                qiMarket.setExtraInfo(bundle);
                qiMarket.setToTop();
                markerList.add(qiMarket);
            }
        }

        // 基站红点
        if (jzRouteListForSegmented.size() > 0) {
            RoutePointBaidu routePointTmp;
            for (int j = 0; j < jzRouteListForSegmented.size(); j++) {
                routePointTmp = jzRouteListForSegmented.get(j);

                jzMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(routePointTmp.point)
                        .title(mImei + "")
                        .visible(isShowBase)
                        .icon(bitmapHelper.getBitmapZoomForBaseStation(mZoom)));
                Bundle bundle = new Bundle();
                bundle.putString("snippet", getTrackDetailInfo(routePointTmp));
                jzMarker.setExtraInfo(bundle);
                jzmarkerList.add(jzMarker);
            }
        }

        // wifi蓝点
        if (wifiRoutePointListForSegmented.size() > 0) {
            RoutePointBaidu routePointTmp;
            for (int j = 0; j < wifiRoutePointListForSegmented.size(); j++) {
                routePointTmp = wifiRoutePointListForSegmented.get(j);

                jzMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(routePointTmp.point)
                        .title(mImei + "")
                        .visible(true)
                        .icon(bitmapHelper.getBitmapZoomForWifi(mZoom)));
                Bundle bundle = new Bundle();
                bundle.putString("snippet", getTrackDetailInfo(routePointTmp));
                jzMarker.setExtraInfo(bundle);
                jzMarker.setVisible(true);
            }
        }

        //5.绘制角度
        if (arrowPointList.size() > 1) {
            Marker rotateMarker;
            RoutePointBaidu routePoint;
            LatLng pre;
            LatLng next;
            // 再循环
            for (int i = 1; i < arrowPointList.size(); i++) {
                routePoint = arrowPointList.get(i);
                pre = arrowPointList.get(i - 1).point;
                next = routePoint.point;
                rotateMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(next)
                        .title(mImei + "")
                        .icon(bitmapHelper.getBitmapZoomForDirection(mZoom)));

                Bundle bundle = new Bundle();
                bundle.putString("snippet", getTrackDetailInfo(routePoint));
                rotateMarker.setExtraInfo(bundle);
                float rotate = Utils.getRotateBaidu(pre, next);
                rotateMarker.setRotate(360 - rotate + mBaiduMap.getMapStatus().rotate);

                markerList.add(rotateMarker);
            }
        }

        updateBaseStationSwitch();
        if (!isTrackDataComplete) {
            mLastTime = trackListResultBean.getData().get(trackListResultBean.getData().size() - 1).getTime();
            ivPlay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getTrackList(false, false);
                }
            }, 100);
        }
    }

    @Override
    public void onEndMoreData() {
        isTrackDataComplete = true;
    }

    /**
     * 轨迹点的详细信息
     *
     * @param routePoint
     * @return
     */
    private String getTrackDetailInfo(RoutePointBaidu routePoint) {
        // 定位时间 + 经度 + 纬度
        return DateUtils.timedate(String.valueOf(routePoint.getTime())) + "," + routePoint.getLat() + "," + routePoint.getLon();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x0100) {
                ivPlay.setImageResource(R.drawable.icon_track_play);
                //txtPlay.setText(getString(R.string.txt_track_play));
            } else if (msg.what == 0x0200) {
                updateBaseStationSwitch();
            }
        }
    };

// ------------------------------ 播放动画 --------------------------------

    /**
     * 播放动画
     */
    @SuppressLint("SetTextI18n")
    private void playTrack() {
        try {
            closeTimer();

            if (playData == null || playData.size() < 3) {
                ToastUtils.show(getString(R.string.not_track_play));
                return;
            }

            if (playCurrrentMarker == null) {
                playCurrrentMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().zIndex(100)
                        .position(playData.get(playIndex).point)
                        .title(mImei + "")
                        .anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromResource(drawableId)));
            }

            ivPlay.setImageResource(R.drawable.icon_track_play_stop);
            if (playIndex == 0) {
                RoutePointBaidu currentPoint = playData.get(0);
                AddressParseUtil.getBaiduAddress(this, tvAddress, currentPoint.getLat(), currentPoint.getLon(), getString(R.string.address) + "：");

                tvLocationTime.setText(getString(R.string.time) + "：" + DateUtils.timedate(String.valueOf(currentPoint.getTime())));
                tvSpeed.setText(getString(R.string.speed) + "：" + ((double) currentPoint.getSpeed() / 10) + "km/h");
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
        if (playIndex <= playData.size() - 1) {
            seekbarProcess.setProgress(playIndex * 100 / playData.size());
            isPauseNow = true;
            playCurrrentMarker.setPosition(playData.get(playIndex).point);
            tvLocationTime.setText(getString(R.string.time) + "：" + DateUtils.timedate(String.valueOf(playData.get(playIndex).getTime())));
            tvSpeed.setText(getString(R.string.speed) + "：" + ((double) playData.get(playIndex).getSpeed() / 10) + "km/h");
            AddressParseUtil.getBaiduAddress(this, tvAddress, playData.get(playIndex).getLat(),
                    playData.get(playIndex).getLon(), getString(R.string.address) + "：");

            targetLatLng = playData.get(playIndex).point;
            updateMapCenter();
        } else {
            isPauseNow = false;
            ivPlay.setImageResource(R.drawable.icon_track_play);
            closeTimer();
            playIndex = 0;
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
     * 停止播放
     */
    private void stopPlayTrack() {
        playIndex = 0;
        seekbarProcess.setProgress(1);
        closeTimer();
        if (currentMarker != null) {
            currentMarker.remove();
            currentMarker = null;
        }
        isPauseNow = false;
        ivPlay.setImageResource(R.drawable.icon_track_play);
    }

}
