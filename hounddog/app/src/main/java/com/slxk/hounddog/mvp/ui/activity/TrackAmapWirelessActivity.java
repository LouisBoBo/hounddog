package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.UrlTileProvider;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.db.DeviceTrackModel;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.di.component.DaggerTrackAmapWirelessComponent;
import com.slxk.hounddog.mvp.contract.TrackAmapWirelessContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.presenter.TrackAmapWirelessPresenter;
import com.slxk.hounddog.mvp.ui.view.data.haibin.Calendar;
import com.slxk.hounddog.mvp.utils.BitmapHelperAmap;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.DateUtils;
import com.slxk.hounddog.mvp.utils.DeviceUtils;
import com.slxk.hounddog.mvp.utils.FileUtilApp;
import com.slxk.hounddog.mvp.utils.GpsUtils;
import com.slxk.hounddog.mvp.utils.MapImageCache;
import com.slxk.hounddog.mvp.utils.Play;
import com.slxk.hounddog.mvp.utils.SmoothModelMarker;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.AlertAppDialog;
import com.slxk.hounddog.mvp.weiget.DateSelectPopupWindow;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 轨迹-高德地图-无线模式
 * <p>
 * Created by MVPArmsTemplate on 01/05/2022 15:50
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class TrackAmapWirelessActivity extends BaseActivity<TrackAmapWirelessPresenter> implements TrackAmapWirelessContract.View, AMap.InfoWindowAdapter,
        AMap.OnMapClickListener, AMap.OnMarkerClickListener, AMap.OnCameraChangeListener, AMapLocationListener {

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

    private AMap mAMap;
    private float mZoom = 16;
    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption;
    private MyLocationStyle myLocationStyle;
    private LatLngBounds mLatLngBounds; // 地图内可见经纬度
    private double mLatitude = 0.0; // 定位获取到的纬度
    private double mLongitude = 0.0; // 定位获取到的经度

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
    private BitmapHelperAmap bitmapHelper;

    private boolean isTrackDataComplete = false; // 是否获取完当前的数据
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

    private String mCompassImei = "00000000"; // 罗盘虚拟imei号，用来判断是否点击了罗盘
    private HashMap<String, Marker> deviceMap; // 存储任务

    private MyDao trackBeanDao;

    private SmoothModelMarker smoothMoveMarker; // 播放动画
    private boolean isPauseNow = false;//是否暂停动画
    private int playSpeed = 90; // 播放速度
    private int processPosition = 0; // 播放进度
    private int playType = 1; // 播放速度，1：快，播放速度为90，2：中，播放速度为50，3：慢，播放速度为10，默认快速
    private int playIndex = 0; // 播放进度条上的进度
    private int drawableId = R.drawable.icon_device_line_on; // 播放轨迹图标
    private boolean isMakeAllVisual = true;//一旦有图标被隐藏,那么这个变量就是false
    private Marker currentMarker;//当前marker   起点   终点  停车点

    // 谷歌瓦片地图
    // 判断当前使用高德地图，还是高德地图的谷歌瓦片地图
    private TileOverlay mtileOverlay;
    private int mapType = 0; // 地图类型,0：高德地图，1：百度地图，2：谷歌地图
    private boolean isChina = false; // 是否在中国
    private String ALBUM_PATH = FileUtilApp.File_Google_Map_SatelliteMap; // 存储路径
    private String mapUrl = Api.Map_SatelliteMap; // 请求地图url地址

    public static Intent newInstance(String imei, String name) {
        Intent intent = new Intent(MyApplication.getMyApp(), TrackAmapWirelessActivity.class);
        intent.putExtra(Intent_Imei_Key, imei);
        intent.putExtra(Intent_Name_Key, name);
        return intent;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTrackAmapWirelessComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_track_amap_wireless;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
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
        try {
            trackBeanDao = new MyDao(DeviceTrackModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        currentSelectDay = java.util.Calendar.getInstance(Locale.ENGLISH);
        mQuestData = DateUtils.getTodayDateTime_3();
        bitmapHelper = new BitmapHelperAmap(this);
        isChina = ConstantValue.isInChina();
        mapType = ConstantValue.getMapType();

        tvSpeed.setVisibility(View.GONE);
        tvAddress.setVisibility(View.GONE);
        cvBaseStationSwitch.setVisibility(View.GONE);
        tvData.setText(DateUtils.getTodayDateTime_3());
        onShowLocationInfo(false);
        initMaps();
        loadMapInfoWindow();
        onSeekBarProcess();
        onQuestTrackModels(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 是否显示底部定位点详细信息
     *
     * @param isShow
     */
    private void onShowLocationInfo(boolean isShow) {
        if (isShow) {
            llTime.setVisibility(View.VISIBLE);
        }else{
            tvLocationTime.setText(getString(R.string.time));
            tvSpeed.setText(getString(R.string.speed));
            tvAddress.setText(getString(R.string.address));
            llTime.setVisibility(View.GONE);
        }
    }

    private void initMaps() {
        if (mAMap == null) {
            mAMap = mapView.getMap();
            mAMap.getUiSettings().setScaleControlsEnabled(true);
            mAMap.getUiSettings().setRotateGesturesEnabled(false);
            mAMap.getUiSettings().setZoomControlsEnabled(false);
            mAMap.getUiSettings().setMyLocationButtonEnabled(false);
            mAMap.setOnMarkerClickListener(this);
            mAMap.setOnMapClickListener(this);
            mAMap.setOnCameraChangeListener(this);
            mAMap.setInfoWindowAdapter(this);

            // 渲染瓦片地图
            mAMap.showMapText(false);
            //设置Logo下边界距离屏幕底部的边距,设置为负值即可
            mAMap.getUiSettings().setLogoBottomMargin(-150);
            //在线瓦片数据
            useOMCMap();

            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(3000);
            // 设置获取手机传感器方向
            mLocationOption.setSensorEnable(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除

            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.interval(3000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.myLocationIcon(bitmapHelper.getBitmapZoomForUserLocation(mZoom));
            myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
            myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
            // 设置定位监听
            mAMap.setMyLocationStyle(myLocationStyle);
            mAMap.setMyLocationEnabled(true);
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
                    generateSmoothMarker(mTrackAllModels, playIndex);
                    isPauseNow = true;
                    smoothMoveMarker.pauseMove();
                    ivPlay.setImageResource(R.drawable.icon_track_play);
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
        if (getDeviceTrackDataBase() != null) {
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
        try {
            return trackBeanDao.getDeviceTrackPagination(
                    mImei,
                    DateUtils.data_5(mQuestData + " " + startHour),
                    DateUtils.data_5(mQuestData + " " + endHour),
                    Track_Number,
                    Track_Number * mTrack_Page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 重置轨迹数据及相关查询条件
     */
    private void onResetTrackData() {
        cancelSmoothMarker();
        onClearData();
        mAMap.clear();
        mTrackAllModels.clear();
        mTrack_Page = 0;
        mLatLngBounds = null;
        isTrackDataComplete = false;
        mJzMarkerList.clear();
        tvLocationTime.setText(getString(R.string.time));
        tvAddress.setText(getString(R.string.address));
        onShowLocationInfo(false);

        //在线瓦片数据
        useOMCMap();
    }

    /**
     * 清除数据，恢复原来的数据
     */
    private void onClearData() {
        isPauseNow = false;
        deviceMap.clear();
        processPosition = 0; // 播放进度
        seekbarProcess.setProgress(1);
        ivPlay.setImageResource(R.drawable.icon_track_play);
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mlocationClient != null) {
                    mlocationClient.startLocation();
                }
            }
        }, 1000);

        if (currentMarker != null) {
            currentMarker.remove();
            currentMarker = null;
        }
    }

    @Override
    protected void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
        if (mlocationClient != null) {
            mlocationClient.startLocation();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
        if (mapView != null) {
            mapView.onDestroy();
            mapView = null;
        }
        super.onDestroy();
    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
//                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                aMapLocation.getLatitude();//获取纬度
//                aMapLocation.getLongitude();//获取经度
//                aMapLocation.getAccuracy();//获取精度信息

                mLatitude = aMapLocation.getLatitude();
                mLongitude = aMapLocation.getLongitude();
                MyApplication.getMyApp().setLatitude(mLatitude);
                MyApplication.getMyApp().setLongitude(mLongitude);
                MyApplication.getMyApp().setMobile_location_state(getString(R.string.mobile_gps_location));

                SPUtils.getInstance().put(ConstantValue.Is_In_China, GpsUtils.isChinaLocation(mLatitude, mLongitude));

                onShowCompass(mLatitude, mLongitude);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getInfoWindow(Marker marker) {
        String imei = marker.getTitle();
        if (!TextUtils.isEmpty(imei) && !imei.equals(mCompassImei)) {
            currentMarker = marker;
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
            updateMapCenter(marker.getPosition());
            return mapInfoWindow.viewInfoWindows;
        }
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        try {
            mZoom = cameraPosition.zoom;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.hideInfoWindow();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            if (smoothMoveMarker != null) {
                if (smoothMoveMarker.getMarker().equals(marker)) {
                    return true;
                }
            }
            marker.showInfoWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 绘制罗盘位置
     *
     * @param lat
     * @param lon
     */
    private void onShowCompass(double lat, double lon) {
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

            Marker marker = mAMap.addMarker(markerOption);
            deviceMap.put(mCompassImei, marker);
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
                distance = AMapUtils.calculateLineDistance(latLng_1, latLng_2);
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
                distance = AMapUtils.calculateLineDistance(latLng_A, latLng_B);
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
                            LatLng beforeLatLng = DeviceUtils.getAmapLatLng(mArrowBeforeLat, mArrowBeforeLon);
                            LatLng newLatLng = DeviceUtils.getAmapLatLng(mTrackCurrentGPSModels.get(index).getLat(), mTrackCurrentGPSModels.get(index).getLon());
                            float tmpDistance = AMapUtils.calculateLineDistance(beforeLatLng, newLatLng);
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
                LatLng point = DeviceUtils.getAmapLatLng(mTrackCurrentGPSModels.get(i).getLat(), mTrackCurrentGPSModels.get(i).getLon());
                lineList.add(point);
                if (isClearTrack) {
                    boundsBuilder.include(point);
                }
            }
            // 绘制轨迹
            mAMap.addPolyline(new PolylineOptions().addAll(lineList).width(14).color(color)).setZIndex(9999);

            //调整角度
            if (isClearTrack) {
                mLatLngBounds = boundsBuilder.build();
                mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 200));
                mZoom = mAMap.getCameraPosition().zoom;
            }
        }else{
            //调整角度
            if (isClearTrack) {
                boundsBuilder = new LatLngBounds.Builder();
                for (int i = 0; i < mTrackCurrentGPSModels.size(); i++) {
                    LatLng point = DeviceUtils.getAmapLatLng(mTrackCurrentGPSModels.get(i).getLat(), mTrackCurrentGPSModels.get(i).getLon());
                    boundsBuilder.include(point);
                }
                mLatLngBounds = boundsBuilder.build();
                mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 200));
                mZoom = mAMap.getCameraPosition().zoom;
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
                LatLng latLng = DeviceUtils.getAmapLatLng(mTrackAllModels.get(0).getLat(), mTrackAllModels.get(0).getLon());
                Marker qiMarket = mAMap.addMarker(new MarkerOptions().zIndex(1)
                        .position(latLng)
                        .title(mImei + "")
                        .snippet(getTrackDetailInfo(mTrackAllModels.get(0)))
                        .icon(bitmapHelper.getBitmapZoomForStart(mZoom)));
                qiMarket.setToTop();
                mMarkerList.add(qiMarket);
            }
        }

        // 基站红点
        if (mTrackBaseLocationModels.size() > 0) {
            DeviceTrackModel routePointTmp;
            for (int j = 0; j < mTrackBaseLocationModels.size(); j++) {
                routePointTmp = mTrackBaseLocationModels.get(j);

                LatLng latLng = DeviceUtils.getAmapLatLng(routePointTmp.getLat(), routePointTmp.getLon());
                ;
                Marker jzMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
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
                pre = DeviceUtils.getAmapLatLng(mArrowPointList.get(i - 1).getLat(), mArrowPointList.get(i - 1).getLon());
                next = DeviceUtils.getAmapLatLng(routePoint.getLat(), routePoint.getLon());
                rotateMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(next)
                        .title(mImei + "")
                        .snippet(getTrackDetailInfo(routePoint))
                        .icon(bitmapHelper.getBitmapZoomForDirection(mZoom)));

                float rotate = Utils.getRotate(pre, next);
                rotateMarker.setRotateAngle(360 - rotate + mAMap.getCameraPosition().bearing);
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
                    LatLng latLng = DeviceUtils.getAmapLatLng(mTrackAllModels.get(endPosition).getLat(), mTrackAllModels.get(endPosition).getLon());
                    Marker endMarket = mAMap.addMarker(new MarkerOptions().zIndex(1)
                            .position(latLng)
                            .title(mImei + "")
                            .snippet(getTrackDetailInfo(mTrackAllModels.get(endPosition)))
                            .icon(bitmapHelper.getBitmapZoomForEnd(mZoom)));
                    endMarket.setToTop();
                    mMarkerList.add(endMarket);
                }
            }

            llData.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //在线瓦片数据
                    useOMCMap();
                }
            }, 550);
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
            tvLocationTime.setText(getString(R.string.time) + "：" + currentPoint.getTime());
            tvAddress.setText(getString(R.string.address) + "：" + ((double) currentPoint.getLat() / 1000000) + "," + ((double) currentPoint.getLon() / 1000000));
        }
    }

    /**
     * 地图中心点位置
     */
    private void updateMapCenter(LatLng point) {
        if (mAMap != null) {
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, mZoom));
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

    @OnClick({R.id.tv_before_day, R.id.ll_data, R.id.tv_after_day, R.id.cv_clear_track, R.id.iv_play, R.id.iv_play_speed,
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
                case R.id.cv_clear_track:
                    onClearTrackConfirm();
                    break;
                case R.id.iv_play:
                    playTrack();
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
        //加载基站信息
        if (mJzMarkerList != null) {
            for (int i = 0; i < mJzMarkerList.size(); ++i) {
                mJzMarkerList.get(i).setVisible(isShowBase);
            }
        }
    }

    /**
     * 快进播放，播放速度，1：快，播放速度为10，2：中，播放速度为6，3：慢，播放速度为2，默认快速
     */
    private void onPlaySpeed() {
        if (playType == 1) {
            playType = 2;
            playSpeed = 50;
            tvPlaySpeed.setText(getString(R.string.medium_speed));
        } else if (playType == 2) {
            playType = 3;
            playSpeed = 10;
            tvPlaySpeed.setText(getString(R.string.slow));
        } else {
            playType = 1;
            playSpeed = 90;
            tvPlaySpeed.setText(getString(R.string.fast));
        }

        if (smoothMoveMarker == null) {
            smoothMoveMarker = new SmoothModelMarker(mAMap);
        }
        int duration = 50 + (10 * (100 - playSpeed));
        smoothMoveMarker.setTotalDuration(duration);
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

    /**
     * 播放动画
     */
    private void playTrack() {
        if (mTrackAllModels.size() < 3) {
            ToastUtils.show(getString(R.string.not_track_play));
            return;
        }

        if (smoothMoveMarker == null) {
            ivPlay.setImageResource(R.drawable.icon_track_play_stop);
            //txtPlay.setText(getString(R.string.txt_track_stop));
            generateSmoothMarker(mTrackAllModels, 0);
        } else {
            if (smoothMoveMarker.getRemainDistance() == 0.0f) {
                //已经播放了一遍,再播放一遍
                ivPlay.setImageResource(R.drawable.icon_track_play_stop);
                //txtPlay.setText(getString(R.string.txt_track_stop));
                generateSmoothMarker(mTrackAllModels, 0);
                return;
            }

            if (!smoothMoveMarker.isPauseAnimationNow()) {
                if (!isPauseNow) {
                    //点击暂停
                    ivPlay.setImageResource(R.drawable.icon_track_play);
                    //txtPlay.setText(getString(R.string.txt_track_play));
                    isPauseNow = true;
                    smoothMoveMarker.pauseMove();
                } else {
                    //暂停后恢复播放
                    ivPlay.setImageResource(R.drawable.icon_track_play_stop);
                    //txtPlay.setText(getString(R.string.txt_track_stop));
                    isPauseNow = false;
                    //找到真正播放的索引
                    mAMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            smoothMoveMarker.getPosition(),//先移动到点
                            mZoom, //新的缩放级别
                            0, //俯仰角0°~45°（垂直与地图时为0）
                            0  ////偏航角 0~360° (正北方为0)
                    )));
                    smoothMoveMarker.resumeMove();
                }
            }
        }
    }

    /***
     *  生成点平滑移动
     */
    private void generateSmoothMarker(ArrayList<DeviceTrackModel> mTrackAllModels, int index) {

        try {
            if (mTrackAllModels == null || mTrackAllModels.size() == 0) {
                ToastUtils.show(getString(R.string.no_trace_data));
                return;
            }

            if (smoothMoveMarker != null) {
                smoothMoveMarker.destroy();
                smoothMoveMarker = null;
            }

            DeviceTrackModel routePoint;
            //当前轨迹的播放列表
            ArrayList<LatLng> points = new ArrayList<>();
            for (int i = 0; i < mTrackAllModels.size(); i++) {
                routePoint = mTrackAllModels.get(i);
                points.add(DeviceUtils.getAmapLatLng(routePoint.getLat(), routePoint.getLon()));
            }

            smoothMoveMarker = new SmoothModelMarker(mAMap);
            smoothMoveMarker.setDescriptor(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), drawableId)));
            LatLng drivePoint = points.get(index);
            processPosition = index;
            Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
            points.set(pair.first, drivePoint);
            List<DeviceTrackModel> subList = mTrackAllModels.subList(pair.first, points.size());
            // 设置滑动的轨迹左边点
            smoothMoveMarker.setPoints(subList, play);
            //CommonUtils.LogKevin("轨迹","总共长度:"+points.size(),TrackActivity.this);
            mAMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    drivePoint,//先移动到点
                    mZoom, //新的缩放级别
                    0, //俯仰角0°~45°（垂直与地图时为0）
                    0  ////偏航角 0~360° (正北方为0)
            )));

            // 设置滑动的总时间
            int duration = 50 + (10 * (100 - playSpeed));
            smoothMoveMarker.setTotalDuration(duration);
            // 开始滑动
            smoothMoveMarker.setMoveListener(smoothListener);
            smoothMoveMarker.setFinishMoveListener(smoothFinishMoveListener);
            smoothMoveMarker.startSmoothMove();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消动画
     */
    private void cancelSmoothMarker() {
        if (smoothMoveMarker != null) {
            smoothMoveMarker.destroy();
            smoothMoveMarker = null;
            isPauseNow = false;
            ivPlay.setImageResource(R.drawable.icon_track_play);
        }
    }

    /**
     * todo 监听动画结束的监听器
     */
    private SmoothModelMarker.SmoothFinishMoveListener smoothFinishMoveListener = new SmoothModelMarker.SmoothFinishMoveListener() {
        @Override
        public void onFinishMove() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0x0100);
                    isPauseNow = false;
                }
            });

        }
    };

    /**
     * 所有图标都可见
     */
    private void allMarkerVisual() {
        for (int i = 0; i < mMarkerList.size(); i++) {
            mMarkerList.get(i).setVisible(true);
        }
        isMakeAllVisual = true;
    }

    /**
     * 监听动画播放的过程
     */
    private SmoothModelMarker.SmoothMarkerMoveListener smoothListener = new SmoothModelMarker.SmoothMarkerMoveListener() {
        @Override
        public void move(final double v, final DeviceTrackModel currentPoint, final DeviceTrackModel nextPoint) {

            runOnUiThread(new Runnable() {
                @SuppressLint({"SetTextI18n", "DefaultLocale"})
                @Override
                public void run() {
                    try {
                        if (mTrackAllModels == null || mTrackAllModels.size() < 3) {
                            return;
                        }

                        if (smoothMoveMarker != null) {
                            int process = (processPosition + smoothMoveMarker.getIndex()) * 100 / mTrackAllModels.size();
                            seekbarProcess.setProgress(process);
                        }

                        if (!isMakeAllVisual) {
                            //如果有图标没有显示出来,让所有图标都显示
                            allMarkerVisual();
                        }

                        LatLng currentLat = DeviceUtils.getAmapLatLng(currentPoint.getLat(), currentPoint.getLon());
                        updateMapCenter(currentLat);

                        // 判断当前点的经纬度是否在中国范维内的经纬度多边形内，判断国内和海外
//                        boolean isChina = false; // 是否在国内
//                        com.baidu.mapapi.model.LatLng baiduLatLng = DeviceUtils.getBaiduLatLng(currentPoint.getLat(), currentPoint.getLon());
//                        if (com.baidu.mapapi.utils.SpatialRelationUtil.isPolygonContainsPoint(PolygonalArea.getBaiduListPoint(), baiduLatLng)) {
//                            isChina = true;
//                        }
//                        if (isChina) {
//                            AddressParseUtil.getAmapAddress(TrackAmapWirelessActivity.this, tvAddress, currentPoint.getLat(), currentPoint.getLon(),
//                                    getString(R.string.address) + "：");
//                        } else {
//                            AddressParseUtil.getBaiduAddress(TrackAmapWirelessActivity.this, tvAddress, currentPoint.getLat(), currentPoint.getLon(),
//                                    getString(R.string.address) + "：");
//                        }

                        tvLocationTime.setText(getString(R.string.time) + "：" + currentPoint.getTime());
                        tvAddress.setText(getString(R.string.address) + "：" + ((double) currentPoint.getLat() / 1000000) + ","
                                + ((double) currentPoint.getLon() / 1000000));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    /**
     * 播放完成回调
     */
    private Play play = new Play() {
        @Override
        public void complete(int state) {
            if (state == 0) {
                handler.sendEmptyMessage(0x0100);
                seekbarProcess.setProgress(100);
                isPauseNow = false;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x0100) {
                playIndex = mTrackAllModels.size() - 1;
                onSeekBarInfoShow();
                ivPlay.setImageResource(R.drawable.icon_track_play);
                //txtPlay.setText(getString(R.string.txt_track_play));
            } else if (msg.what == 0x0200) {
                updateBaseStationSwitch();
            }
        }
    };


    // ------------------------------------------ 谷歌瓦片地图 -------------------------------------------

    /**
     * 获取文件夹路径
     *
     * @param path
     * @return
     */
    private String getMapPath(String path) {
        return Environment.getExternalStorageDirectory() + path + "/";
    }

    /**
     * 加载在线瓦片数据
     */
    private void useOMCMap() {
        TileOverlayOptions tileOverlayOptions =
                new TileOverlayOptions().tileProvider(new UrlTileProvider(256, 256) {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public URL getTileUrl(int x, int y, int zoom) {
                        try {
                            //return new URL(String.format(url, zoom + 1, TileXYToQuadKey(x, y, zoom)));
                            //return new URL(String.format(url, x, y, zoom));
                            String mFileDirName;
                            String mFileName;
                            mFileDirName = String.format("L%02d/", zoom + 1);
                            mFileName = String.format("%s", TileXYToQuadKey(x, y, zoom));//为了不在手机的图片中显示,取消jpg后缀,文件名自己定义,写入和读取一致即可,由于有自己的bingmap图源服务,所以此处我用的bingmap的文件名
                            String LJ = getMapPath(ALBUM_PATH) + mFileDirName + mFileName;
                            if (MapImageCache.getInstance().isBitmapExit(mFileDirName + mFileName, ALBUM_PATH)) {//判断本地是否有图片文件,如果有返回本地url,如果没有,缓存到本地并返回googleurl
                                return new URL("file://" + LJ);
                            } else {
                                String filePath = String.format(mapUrl, x, y, zoom);
                                Bitmap mBitmap;
                                //mBitmap = BitmapFactory.decodeStream(getImageStream(filePath));//不知什么原因导致有大量的图片存在坏图,所以重写InputStream写到byte数组方法
                                mBitmap = getImageBitmap(getImageStream(filePath));
                                try {
                                    saveFile(mBitmap, mFileName, mFileDirName);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return new URL(filePath);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
        tileOverlayOptions.diskCacheEnabled(false)   //由于自带的缓存在关闭程序后会自动释放,所以无意义,关闭本地缓存
                .diskCacheDir("/storage/emulated/0/amap/OMCcache")
                .diskCacheSize(1024000)
                .memoryCacheEnabled(true)
                .memCacheSize(102400)
                .zIndex(-9999);
        mtileOverlay = mAMap.addTileOverlay(tileOverlayOptions);
    }

    /**
     * 瓦片数据坐标转换
     */
    private String TileXYToQuadKey(int tileX, int tileY, int levelOfDetail) {
        StringBuilder quadKey = new StringBuilder();
        for (int i = levelOfDetail; i > 0; i--) {
            char digit = '0';
            int mask = 1 << (i - 1);
            if ((tileX & mask) != 0) {
                digit++;
            }
            if ((tileY & mask) != 0) {
                digit++;
                digit++;
            }
            quadKey.append(digit);
        }
        return quadKey.toString();
    }

    public Bitmap getImageBitmap(InputStream imputStream) {
        // 将所有InputStream写到byte数组当中
        byte[] targetData = null;
        byte[] bytePart = new byte[4096];
        while (true) {
            try {
                int readLength = imputStream.read(bytePart);
                if (readLength == -1) {
                    break;
                } else {
                    byte[] temp = new byte[readLength + (targetData == null ? 0 : targetData.length)];
                    if (targetData != null) {
                        System.arraycopy(targetData, 0, temp, 0, targetData.length);
                        System.arraycopy(bytePart, 0, temp, targetData.length, readLength);
                    } else {
                        System.arraycopy(bytePart, 0, temp, 0, readLength);
                    }
                    targetData = temp;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 指使Bitmap通过byte数组获取数据
        Bitmap bitmap = BitmapFactory.decodeByteArray(targetData, 0, targetData.length);
        return bitmap;
    }

    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * 保存文件
     */
    public void saveFile(final Bitmap bm, final String fileName, final String fileDirName) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (bm != null) {
                        File dirFile = new File(getMapPath(ALBUM_PATH) + fileDirName);
                        if (!dirFile.exists()) {
                            dirFile.mkdir();
                        }
                        File myCaptureFile = new File(getMapPath(ALBUM_PATH) + fileDirName + fileName);
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                        bos.flush();
                        bos.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 删除文件
     */
    static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

}