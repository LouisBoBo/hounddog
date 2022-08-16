package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.db.DeviceTrackModel;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.di.component.DaggerTrackGoogleWirelessComponent;
import com.slxk.hounddog.mvp.contract.TrackGoogleWirelessContract;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.presenter.TrackGoogleWirelessPresenter;
import com.slxk.hounddog.mvp.ui.view.data.haibin.Calendar;
import com.slxk.hounddog.mvp.utils.BitmapHelperGoogle;
import com.slxk.hounddog.mvp.utils.DateUtils;
import com.slxk.hounddog.mvp.utils.DeviceUtils;
import com.slxk.hounddog.mvp.utils.GoogleLocationUtils;
import com.slxk.hounddog.mvp.utils.GoogleMapUtils;
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
 * Description: 轨迹-谷歌地图-无线模式
 * <p>
 * Created by MVPArmsTemplate on 01/05/2022 15:53
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class TrackGoogleWirelessActivity extends BaseActivity<TrackGoogleWirelessPresenter> implements TrackGoogleWirelessContract.View,
        GoogleMap.OnMarkerClickListener, OnMapReadyCallback,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnCameraChangeListener, GoogleMap.InfoWindowAdapter {

    @BindView(R.id.view_title)
    View viewTitle;
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
    @BindView(R.id.ll_location_info)
    LinearLayout llLocationInfo;
    @BindView(R.id.cv_base_station_switch)
    CardView cvBaseStationSwitch;

    public static final String Intent_Imei_Key = "imei_key";
    public static final String Intent_Name_Key = "name_key";
    private String mImei; // 设备imei号
    private String mDeviceName = ""; // 设备名称

    private float mZoom = 16;
    private LatLng targetLatLng = new LatLng(39.90923, 116.397428); // 轨迹播放点的位置
    private double mLatitude = 0.0; // 定位获取到的纬度
    private double mLongitude = 0.0; // 定位获取到的经度
    private boolean isLocationChina = true; // 当前手机定位是否是在中国，在中国的话不显示3D地图建筑物名称，台湾显示建筑物名称

    private SupportMapFragment mapFragment; // 谷歌地图
    private GoogleMap mGoogleMap; // 谷歌地图

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
    private BitmapHelperGoogle bitmapHelper;

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

    private MyDao trackBeanDao;
    private String mCompassImei = "00000000"; // 罗盘虚拟imei号，用来判断是否点击了罗盘
    private String mUserImei = "11111111"; // 用户当前位置虚拟imei号，用来判断是否点击了用户位置
    private HashMap<String, Marker> deviceMap; // 存储任务
    // 手机传感器方向
    private MyOrientationListener myOrientationListener;
    private float mXDirection = 0;

    public static Intent newInstance(String imei, String name) {
        Intent intent = new Intent(MyApplication.getMyApp(), TrackGoogleWirelessActivity.class);
        intent.putExtra(Intent_Imei_Key, imei);
        intent.putExtra(Intent_Name_Key, name);
        return intent;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTrackGoogleWirelessComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_track_google_wireless;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.track));
        mImei = getIntent().getStringExtra(Intent_Imei_Key);
        mDeviceName = getIntent().getStringExtra(Intent_Name_Key);

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
        try{
            trackBeanDao = new MyDao(DeviceTrackModel.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        currentSelectDay = java.util.Calendar.getInstance(Locale.ENGLISH);
        mQuestData = DateUtils.getTodayDateTime_3();
        bitmapHelper = new BitmapHelperGoogle(this);
        playLongTime = 50 + (10 * (100 - playSpeed));

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        tvSpeed.setVisibility(View.GONE);
        tvAddress.setVisibility(View.GONE);
        cvBaseStationSwitch.setVisibility(View.GONE);
        tvData.setText(DateUtils.getTodayDateTime_3());
        initOritationListener();
        onShowLocationInfo(false);
        loadMapInfoWindow();
        onSeekBarProcess();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);//选择手势
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);//缩放按钮
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        mGoogleMap.setInfoWindowAdapter(this);
        mGoogleMap.setOnMyLocationClickListener(this);
        mGoogleMap.setOnCameraChangeListener(this);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(targetLatLng)      // Sets the center of the map to Mountain View
                .zoom(mZoom)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                  // Sets the tilt of the camera to 30 degrees
                .build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (isLocationChina) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }

        onQuestTrackModels(true);
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return mapInfoWindow.viewInfoWindows;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Override
    public void onCameraChange(@NonNull CameraPosition cameraPosition) {
        try {
            mZoom = cameraPosition.zoom;
            targetLatLng = cameraPosition.target;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String imei = marker.getTitle();
        if (!TextUtils.isEmpty(imei) && !imei.equals(mCompassImei) && !imei.equals(mUserImei)){
            // 定位时间 + 经度 + 纬度
            String snippetString = marker.getSnippet();
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
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

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
                if (mGoogleMap != null) {
                    if (deviceMap.get(mUserImei) != null) {
                        Objects.requireNonNull(deviceMap.get(mUserImei)).setRotation(mXDirection);
                    }
                }
            }
        });
    }

    /**
     * 定位
     */
    public void getLocation() {
        String ll = GoogleLocationUtils.getLocation(this);
        if (!TextUtils.isEmpty(ll)) {
            String[] location = ll.split(",");
            mLatitude = Double.parseDouble(location[0]);
            mLongitude = Double.parseDouble(location[1]);
            // 我的位置
            LatLng myLocation = getLatLngChange(mLatitude, mLongitude);
            addMyLocalMaker(myLocation);
        }
    }

    /**
     * 将我的位置的坐标添加进去
     *
     * @param location 我的位置经纬度
     */
    private void addMyLocalMaker(LatLng location) {
        if (mGoogleMap != null && location != null && location.latitude != 0 && location.longitude != 0) {
            // 开始绘制设备marker
            if (deviceMap.get(mUserImei) != null) {
                Objects.requireNonNull(deviceMap.get(mUserImei)).setPosition(location);
                Objects.requireNonNull(deviceMap.get(mUserImei)).setRotation(mXDirection);
                Objects.requireNonNull(deviceMap.get(mUserImei)).setIcon(getUserLocationMarkerIcon());
            } else {
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(location);
                markerOption.title(mUserImei);
                markerOption.icon(getUserLocationMarkerIcon());
                markerOption.anchor(0.5f, 0.5f);
                markerOption.draggable(false);
                markerOption.visible(true);
                markerOption.zIndex(-9999);

                Marker marker = mGoogleMap.addMarker(markerOption);
                deviceMap.put(mUserImei, marker);
            }
            onShowCompass(location);
        }
    }

    /**
     * 绘制设备图标等信息
     *
     * @return
     */
    private BitmapDescriptor getUserLocationMarkerIcon() {
        View view = View.inflate(this, R.layout.layout_user_location_marker, null);
        return convertViewToBitmap(view);
    }

    /**
     * 绘制罗盘位置
     */
    private void onShowCompass(LatLng compassLng) {
        // 开始绘制设备marker
        if (mGoogleMap != null){
            if (deviceMap.get(mCompassImei) != null) {
                Objects.requireNonNull(deviceMap.get(mCompassImei)).setPosition(compassLng);
                Objects.requireNonNull(deviceMap.get(mCompassImei)).setIcon(getCompassMarkerIcon());
            } else {
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(compassLng);
                markerOption.title(mCompassImei);
                markerOption.icon(getCompassMarkerIcon());
                markerOption.anchor(0.5f, 0.5f);
                markerOption.draggable(false);
                markerOption.visible(true);
                markerOption.zIndex(-9999);

                Marker marker = mGoogleMap.addMarker(markerOption);
                deviceMap.put(mCompassImei, marker);
            }
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
     * 卫星图需要做偏移
     *
     * @param lat
     * @param lon
     * @return
     */
    private LatLng getLatLngChange(double lat, double lon) {
        // 设备经纬度，切换为卫星地图之后，坐标系要转为WGS84坐标系
        double[] deviceWGS84 = GpsUtils.toWGS84Point(lat, lon);
        return new LatLng(deviceWGS84[0], deviceWGS84[1]);
    }

    /**
     * 设置当前点位于地图中心
     */
    private void updateMapCenter() {
        if (mGoogleMap != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLatLng, mZoom));
        }
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
                    LatLng latLng = DeviceUtils.getGoogleLatLng(mTrackAllModels.get(playIndex).getLat(), mTrackAllModels.get(playIndex).getLon());
                    if (mGoogleMap != null){
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mGoogleMap.getCameraPosition().zoom));
                    }
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
        if (mGoogleMap != null){
            mGoogleMap.clear();
        }
        mTrackAllModels.clear();
        mTrack_Page = 0;
        mLatLngBounds = null;
        isTrackDataComplete = false;
        mJzMarkerList.clear();
        tvLocationTime.setText(getString(R.string.time));
        tvAddress.setText(getString(R.string.address));
        onShowLocationInfo(false);
    }

    /**
     * 轨迹绘制
     *
     * @param isClearTrack 是否清除地图上的轨迹
     */
    @SuppressLint("SetTextI18n")
    private void onTrackShow(boolean isClearTrack) {
        if (mGoogleMap != null){
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
                    distance = GoogleMapUtils.toRadiusMeters(latLng_1, latLng_2);
                    // 计算两点之间的每秒平均速度
                    speed_dis = distance / time_difference;
                    if (speed_dis == 0 || speed_dis > 50) {
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
                    distance = GoogleMapUtils.toRadiusMeters(latLng_A, latLng_B);
                    // 计算两点之间的每秒平均速度
                    speed_dis = distance / time_difference;
                    if (speed_dis == 0 || speed_dis > 40) {
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
                                LatLng beforeLatLng = DeviceUtils.getGoogleLatLng(mArrowBeforeLat, mArrowBeforeLon);
                                LatLng newLatLng = DeviceUtils.getGoogleLatLng(mTrackCurrentGPSModels.get(index).getLat(), mTrackCurrentGPSModels.get(index).getLon());
                                double tmpDistance = GoogleMapUtils.toRadiusMeters(beforeLatLng, newLatLng);
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
                    LatLng point = DeviceUtils.getGoogleLatLng(mTrackCurrentGPSModels.get(i).getLat(), mTrackCurrentGPSModels.get(i).getLon());
                    lineList.add(point);
                    if (isClearTrack) {
                        boundsBuilder.include(point);
                    }
                }
                // 绘制轨迹
                mGoogleMap.addPolyline(new PolylineOptions().addAll(lineList).width(14).color(color));

                //调整角度
                if (isClearTrack) {
                    mLatLngBounds = boundsBuilder.build();
                    //调整角度
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 200));
                    mZoom = mGoogleMap.getCameraPosition().zoom;
                }
            }else{
                //调整角度
                if (isClearTrack) {
                    boundsBuilder = new LatLngBounds.Builder();
                    for (int i = 0; i < mTrackCurrentGPSModels.size(); i++) {
                        LatLng point = DeviceUtils.getGoogleLatLng(mTrackCurrentGPSModels.get(i).getLat(), mTrackCurrentGPSModels.get(i).getLon());
                        boundsBuilder.include(point);
                    }
                    mLatLngBounds = boundsBuilder.build();
                    //调整角度
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 200));
                    mZoom = mGoogleMap.getCameraPosition().zoom;
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
                    LatLng latLng = DeviceUtils.getGoogleLatLng(mTrackAllModels.get(0).getLat(), mTrackAllModels.get(0).getLon());
                    Marker qiMarket = mGoogleMap.addMarker(new MarkerOptions().zIndex(1)
                            .position(latLng)
                            .title(mImei + "")
                            .snippet(getTrackDetailInfo(mTrackAllModels.get(0)))
                            .icon(bitmapHelper.getBitmapZoomForStart(mZoom)));
                    mMarkerList.add(qiMarket);
                }
            }

            // 基站红点
            if (mTrackBaseLocationModels.size() > 0) {
                DeviceTrackModel routePointTmp;
                for (int j = 0; j < mTrackBaseLocationModels.size(); j++) {
                    routePointTmp = mTrackBaseLocationModels.get(j);

                    LatLng latLng = DeviceUtils.getGoogleLatLng(routePointTmp.getLat(), routePointTmp.getLon());
                    Marker jzMarker = mGoogleMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                            .position(latLng)
                            .title(mImei + "")
                            .snippet(getTrackDetailInfo(routePointTmp))
                            .visible(isShowBase)
                            .icon(bitmapHelper.getBitmapZoomForBaseStation(mZoom)));
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
                    pre = DeviceUtils.getGoogleLatLng(mArrowPointList.get(i - 1).getLat(), mArrowPointList.get(i - 1).getLon());
                    next = DeviceUtils.getGoogleLatLng(routePoint.getLat(), routePoint.getLon());
                    rotateMarker = mGoogleMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                            .position(next)
                            .title(mImei + "")
                            .snippet(getTrackDetailInfo(routePoint))
                            .icon(bitmapHelper.getBitmapZoomForDirection(mZoom)));

                    float rotate = GoogleMapUtils.getGoogleRotate(pre, next);
                    rotateMarker.setRotation(rotate + mGoogleMap.getCameraPosition().bearing);
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
                        LatLng latLng = DeviceUtils.getGoogleLatLng(mTrackAllModels.get(endPosition).getLat(), mTrackAllModels.get(endPosition).getLon());
                        Marker endMarket = mGoogleMap.addMarker(new MarkerOptions().zIndex(1)
                                .position(latLng)
                                .title(mImei + "")
                                .snippet(getTrackDetailInfo(mTrackAllModels.get(endPosition)))
                                .icon(bitmapHelper.getBitmapZoomForEnd(mZoom)));
                        mMarkerList.add(endMarket);
                    }
                }
            }

            getLocation();
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
        // 开启方向传感器
        if (myOrientationListener != null) {
            myOrientationListener.start();
        }
        super.onResume();
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
            LatLng point = DeviceUtils.getGoogleLatLng(mTrackAllModels.get(playIndex).getLat(), mTrackAllModels.get(playIndex).getLon());
            if (playCurrrentMarker == null && mGoogleMap != null) {
                playCurrrentMarker = mGoogleMap.addMarker(new MarkerOptions().zIndex(100)
                        .position(point)
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
            LatLng point = DeviceUtils.getGoogleLatLng(mTrackAllModels.get(playIndex).getLat(), mTrackAllModels.get(playIndex).getLon());
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
