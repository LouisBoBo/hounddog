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
import com.slxk.hounddog.di.component.DaggerTrackAmapComponent;
import com.slxk.hounddog.mvp.contract.TrackAmapContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.model.bean.RoutePoint;
import com.slxk.hounddog.mvp.model.bean.TrackHasDataResultBean;
import com.slxk.hounddog.mvp.model.bean.TrackListResultBean;
import com.slxk.hounddog.mvp.model.putbean.TrackHasDataPutBean;
import com.slxk.hounddog.mvp.model.putbean.TrackListPutBean;
import com.slxk.hounddog.mvp.presenter.TrackAmapPresenter;
import com.slxk.hounddog.mvp.ui.view.data.haibin.Calendar;
import com.slxk.hounddog.mvp.utils.AddressParseUtil;
import com.slxk.hounddog.mvp.utils.BitmapHelperAmap;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.DateUtils;
import com.slxk.hounddog.mvp.utils.FileUtilApp;
import com.slxk.hounddog.mvp.utils.GpsUtils;
import com.slxk.hounddog.mvp.utils.LocationAddressParsedAmap;
import com.slxk.hounddog.mvp.utils.LocationAddressParsedBaidu;
import com.slxk.hounddog.mvp.utils.MapImageCache;
import com.slxk.hounddog.mvp.utils.Play;
import com.slxk.hounddog.mvp.utils.PolygonalArea;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;
import com.slxk.hounddog.mvp.utils.SmoothMarker;
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
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/19/2022 16:33
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class TrackAmapActivity extends BaseActivity<TrackAmapPresenter> implements TrackAmapContract.View, AMap.InfoWindowAdapter,
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
    @BindView(R.id.ll_time)
    LinearLayout llTime;

    public static final String Intent_Simei_Key = "simei_key";
    public static final String Intent_Name_Key = "name_key";
    public static final String Intent_Imei_Key = "imei_key";
    private String mSimei; // 设备imei号
    private String mDeviceName = ""; // 设备名称
    private String mImei; // 设备imei号

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

    private long startTimeForQuest; // 开始时间
    private long endTimeForQuest; // 结束时间
    private String startData; // 开始日期
    private String endData; // 结束日期
    private String startHour = "00:00"; // 开始时间后缀
    private String endHour = "23:59"; // 结束时间后缀
    private int mLimitSize = 2000; // 限制获取数量
    private long mLastTime = 0; // 最后时间
    private boolean isTrackDataComplete = false; // 是否获取完当前的数据

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

    private ArrayList<RoutePoint> jzRouteList;//基站点
    private ArrayList<RoutePoint> jzRouteListForSegmented;//基站点
    private ArrayList<RoutePoint> wifiRoutePointList; // wifi点
    private ArrayList<RoutePoint> wifiRoutePointListForSegmented; // wifi点
    private ArrayList<RoutePoint> playData; // 总数据
    private ArrayList<RoutePoint> playDataForSegmented; // 分段数据-用来计算总数据的
    private ArrayList<RoutePoint> arrowPointList; // 方向箭头
    private ArrayList<Marker> markerList;//保存地图中所有图标的列表
    private ArrayList<Marker> jzmarkerList;//保存地图中所有图标的列表
    private Marker currentMarker, qiMarket, zhongMarket, playCurrrentMarker;//当前marker   起点   终点  停车点
    private Marker jzMarker;
    private BitmapHelperAmap bitmapHelper;

    private String mCompassImei = "00000000"; // 罗盘虚拟imei号，用来判断是否点击了罗盘
    private HashMap<String, Marker> deviceMap; // 存储任务

    private SmoothMarker smoothMoveMarker; // 播放动画
    private boolean isPauseNow = false;//是否暂停动画
    private int playSpeed = 90; // 播放速度
    private int processPosition = 0; // 播放进度
    private int playType = 1; // 播放速度，1：快，播放速度为90，2：中，播放速度为50，3：慢，播放速度为10，默认快速
    private int playIndex = 0; // 播放进度条上的进度
    private int drawableId = R.drawable.icon_device_line_on; // 播放轨迹图标
    private boolean isMakeAllVisual = true;//一旦有图标被隐藏,那么这个变量就是false

    // 谷歌瓦片地图
    // 判断当前使用高德地图，还是高德地图的谷歌瓦片地图
    private TileOverlay mtileOverlay;
    private int mapType = 0; // 地图类型,0：高德地图，1：百度地图，2：谷歌地图
    private boolean isChina = false; // 是否在中国
    private String ALBUM_PATH = FileUtilApp.File_Google_Map_SatelliteMap; // 存储路径
    private String mapUrl = Api.Map_SatelliteMap; // 请求地图url地址

    public static Intent newInstance(String imei, String simei, String name) {
        Intent intent = new Intent(MyApplication.getMyApp(), TrackAmapActivity.class);
        intent.putExtra(Intent_Simei_Key, simei);
        intent.putExtra(Intent_Name_Key, name);
        intent.putExtra(Intent_Imei_Key, imei);
        return intent;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTrackAmapComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_track_amap;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        setTitle(getString(R.string.track));
        mSimei = getIntent().getStringExtra(Intent_Simei_Key);
        mDeviceName = getIntent().getStringExtra(Intent_Name_Key);
        mImei = getIntent().getStringExtra(Intent_Imei_Key);
        isChina = ConstantValue.isInChina();
        mapType = ConstantValue.getMapType();

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

        currentSelectDay = java.util.Calendar.getInstance(Locale.ENGLISH);
        bitmapHelper = new BitmapHelperAmap(this);

        onShowLocationInfo(false);
        setDataForTrack("", "");
        initMaps();
        loadMapInfoWindow();
        onSeekBarProcess();
        updateBaseStationSwitch();

        getTrackHasForData();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
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
                if (playData != null && playData.size() > 3) {
                    playIndex = playData.size() * seekBar.getProgress() / 100;
                    if (playIndex >= playData.size()) {
                        playIndex = playData.size() - 1;
                    }
                    onSeekBarInfoShow();
                    generateSmoothMarker(playData, playIndex);
                    isPauseNow = true;
                    smoothMoveMarker.pauseMove();
                    ivPlay.setImageResource(R.drawable.icon_track_play);
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
            RoutePoint currentPoint = playData.get(playIndex);
            AddressParseUtil.getAmapAddress(this, tvAddress, currentPoint.getLat(), currentPoint.getLon(), getString(R.string.address) + "：");

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
     *
     * @param isShow
     */
    private void onShowLocationInfo(boolean isShow) {
        if (isShow) {
            llTime.setVisibility(View.VISIBLE);
            tvAddress.setVisibility(View.VISIBLE);
        }else{
            tvLocationTime.setText(getString(R.string.time));
            tvSpeed.setText(getString(R.string.speed));
            tvAddress.setText(getString(R.string.address));
            llTime.setVisibility(View.GONE);
            tvAddress.setVisibility(View.GONE);
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
        cancelSmoothMarker();
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
        mAMap.clear();
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
                    mapInfoWindow.tvMarkerAddress.setVisibility(View.VISIBLE);
                    mapInfoWindow.tvMarkerTime.setText(getString(R.string.time) + "：" + detailInfo[0]);
                    if (GpsUtils.isChinaTaiWan(marker.getPosition().latitude, marker.getPosition().longitude)) {
                        LocationAddressParsedBaidu.getLocationParsedInstance()
                                .Parsed(marker.getPosition().latitude, marker.getPosition().longitude)
                                .setAddressListener(new LocationAddressParsedBaidu.getAddressListener() {
                                    @Override
                                    public void getAddress(String address) {
                                        mapInfoWindow.tvMarkerAddress.setText(getString(R.string.address) + "：" + address);
                                    }
                                });
                    } else {
                        LocationAddressParsedAmap.getLocationParsedInstance()
                                .Parsed(this, marker.getPosition().latitude, marker.getPosition().longitude)
                                .setAddressListener(new LocationAddressParsedAmap.getAddressListener() {
                                    @Override
                                    public void getAddress(String address) {
                                        mapInfoWindow.tvMarkerAddress.setText(getString(R.string.address) + "：" + address);
                                    }
                                });
                    }
                } else {
                    mapInfoWindow.tvMarkerTime.setText(getString(R.string.time));
                    mapInfoWindow.tvMarkerTime.setVisibility(View.GONE);
                    mapInfoWindow.tvMarkerAddress.setText(getString(R.string.address));
                    mapInfoWindow.tvMarkerAddress.setVisibility(View.GONE);
                }
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
                playTrack();
                break;
            case R.id.iv_play_speed:
            case R.id.tv_play_speed:
                onPlaySpeed();
                break;
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
            smoothMoveMarker = new SmoothMarker(mAMap);
        }
        int duration = 50 + (10 * (100 - playSpeed));
        smoothMoveMarker.setTotalDuration(duration);
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
            llData.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //在线瓦片数据
                    useOMCMap();
                }
            }, 550);
            return;
        }

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (int i = 0; i < trackListResultBean.getData().size(); i++) {
            TrackListResultBean.DataBean bean = trackListResultBean.getData().get(i);
            double lat = (double) bean.getLat() / 1000000;
            double lon = (double) bean.getLon() / 1000000;
            RoutePoint routePoint = new RoutePoint(new LatLng(lat, lon), bean.getType(), bean.getTime(), bean.getSpeed(),
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
            mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 200));
            mZoom = mAMap.getCameraPosition().zoom;
        }

        if (arrowPointList.size() == 0) {
            if (playDataForSegmented.size() > 0) {
                arrowPointList.add(playDataForSegmented.get(0));
                for (int i = 1; i < playDataForSegmented.size(); i++) {
                    RoutePoint lastPoint = arrowPointList.get(arrowPointList.size() - 1);
                    float tmpDistance = AMapUtils.calculateLineDistance(lastPoint.getPoint(), playDataForSegmented.get(i).getPoint());
                    if (tmpDistance > 500) {
                        arrowPointList.add(playDataForSegmented.get(i));
                    }
                }
            }
        } else {
            for (int i = 0; i < playDataForSegmented.size(); i++) {
                RoutePoint lastPoint = arrowPointList.get(arrowPointList.size() - 1);
                float tmpDistance = AMapUtils.calculateLineDistance(lastPoint.getPoint(), playDataForSegmented.get(i).getPoint());
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
            mAMap.addPolyline(new PolylineOptions().addAll(lineList).width(14).color(color)).setZIndex(9999);
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
                RoutePoint currentPoint = playData.get(0);
                AddressParseUtil.getAmapAddress(this, tvAddress, currentPoint.getLat(), currentPoint.getLon(), getString(R.string.address) + "：");

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
                        rotateMarker = mAMap.addMarker(new MarkerOptions().zIndex(1)
                                .position(playData.get(i).point)
                                .title(mImei + "")
                                .snippet(getTrackDetailInfo(playData.get(i)))
                                .icon(bitmapHelper.getBitmapZoomParkingForGPS(mZoom)));
                        markerList.add(rotateMarker);
                    }
                }
            } else {
                // 如果是第二页++ 加载完成，则判断从当前页数据的索引  0  -   size-1  中间段执行
                for (int i = 0; i < playDataForSegmented.size() - 1; i++) {
                    if (playDataForSegmented.get(i).getPtype() == 1) {
                        Marker rotateMarker;
                        rotateMarker = mAMap.addMarker(new MarkerOptions().zIndex(1)
                                .position(playDataForSegmented.get(i).point)
                                .title(mImei + "")
                                .snippet(getTrackDetailInfo(playDataForSegmented.get(i)))
                                .icon(bitmapHelper.getBitmapZoomParkingForGPS(mZoom)));
                        markerList.add(rotateMarker);
                    }
                }
            }

            // 终点
            if (playData.size() > 1) {
                int endPosition = playData.size() - 1;
                int lastOneType = playData.get(endPosition).type;
                zhongMarket = mAMap.addMarker(new MarkerOptions().zIndex(1)
                        .position(playData.get(endPosition).point)
                        .title(mImei + "")
                        .snippet(getTrackDetailInfo(playData.get(endPosition)))
                        .icon(bitmapHelper.getBitmapZoomForEnd(mZoom)));
                zhongMarket.setToTop();
                markerList.add(zhongMarket);
            }
        } else {
            if (isResetData) {
                for (int i = 1; i < playDataForSegmented.size(); i++) {
                    if (playDataForSegmented.get(i).getPtype() == 1) {
                        Marker rotateMarker;
                        rotateMarker = mAMap.addMarker(new MarkerOptions().zIndex(1)
                                .position(playDataForSegmented.get(i).point)
                                .title(mImei + "")
                                .snippet(getTrackDetailInfo(playDataForSegmented.get(i)))
                                .icon(bitmapHelper.getBitmapZoomParkingForGPS(mZoom)));
                        markerList.add(rotateMarker);
                    }
                }
            } else {
                for (int i = 0; i < playDataForSegmented.size(); i++) {
                    if (playDataForSegmented.get(i).getPtype() == 1) {
                        Marker rotateMarker;
                        rotateMarker = mAMap.addMarker(new MarkerOptions().zIndex(1)
                                .position(playDataForSegmented.get(i).point)
                                .title(mImei + "")
                                .snippet(getTrackDetailInfo(playDataForSegmented.get(i)))
                                .icon(bitmapHelper.getBitmapZoomParkingForGPS(mZoom)));
                        markerList.add(rotateMarker);
                    }
                }
            }
        }

        //2.绘制第一个点   起点
        if (isResetData) {
            if (playData.size() > 0) {
                int firstType = playData.get(0).type;
                qiMarket = mAMap.addMarker(new MarkerOptions().zIndex(1)
                        .position(playData.get(0).point)
                        .title(mImei + "")
                        .snippet(getTrackDetailInfo(playData.get(0)))
                        .icon(bitmapHelper.getBitmapZoomForStart(mZoom)));
                qiMarket.setToTop();
                markerList.add(qiMarket);
            }
        }

        // 基站红点
        if (jzRouteListForSegmented.size() > 0) {
            RoutePoint routePointTmp;
            for (int j = 0; j < jzRouteListForSegmented.size(); j++) {
                routePointTmp = jzRouteListForSegmented.get(j);

                jzMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(routePointTmp.point)
                        .title(mImei + "")
                        .snippet(getTrackDetailInfo(routePointTmp))
                        .visible(isShowBase)
                        .icon(bitmapHelper.getBitmapZoomForBaseStation(mZoom)));
                jzmarkerList.add(jzMarker);
            }
        }

        // wifi蓝点
        if (wifiRoutePointListForSegmented.size() > 0) {
            RoutePoint routePointTmp;
            for (int j = 0; j < wifiRoutePointListForSegmented.size(); j++) {
                routePointTmp = wifiRoutePointListForSegmented.get(j);

                jzMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(routePointTmp.point)
                        .title(mImei + "")
                        .snippet(getTrackDetailInfo(routePointTmp))
                        .visible(true)
                        .icon(bitmapHelper.getBitmapZoomForWifi(mZoom)));
                jzMarker.setVisible(true);
            }
        }

        //5.绘制角度
        if (arrowPointList.size() > 1) {
            Marker rotateMarker;
            RoutePoint routePoint;
            LatLng pre;
            LatLng next;
            // 再循环
            for (int i = 1; i < arrowPointList.size(); i++) {
                routePoint = arrowPointList.get(i);
                pre = arrowPointList.get(i - 1).point;
                next = routePoint.point;
                rotateMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(next)
                        .title(mImei + "")
                        .snippet(getTrackDetailInfo(routePoint))
                        .icon(bitmapHelper.getBitmapZoomForDirection(mZoom)));

                float rotate = Utils.getRotate(pre, next);
                rotateMarker.setRotateAngle(360 - rotate + mAMap.getCameraPosition().bearing);
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
        } else {
            llData.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //在线瓦片数据
                    useOMCMap();
                }
            }, 550);
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
    private String getTrackDetailInfo(RoutePoint routePoint) {
        // 定位时间 + 经度 + 纬度
        return DateUtils.timedate(String.valueOf(routePoint.getTime())) + "," + routePoint.getLat() + "," + routePoint.getLon();
    }

    /**
     * 播放动画
     */
    private void playTrack() {
        if (playData.size() < 3) {
            ToastUtils.show(getString(R.string.not_track_play));
            return;
        }

        if (smoothMoveMarker == null) {
            ivPlay.setImageResource(R.drawable.icon_track_play_stop);
            //txtPlay.setText(getString(R.string.txt_track_stop));
            generateSmoothMarker(playData, 0);
        } else {
            if (smoothMoveMarker.getRemainDistance() == 0.0f) {
                //已经播放了一遍,再播放一遍
                ivPlay.setImageResource(R.drawable.icon_track_play_stop);
                //txtPlay.setText(getString(R.string.txt_track_stop));
                generateSmoothMarker(playData, 0);
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
    private void generateSmoothMarker(ArrayList<RoutePoint> playData, int index) {

        try {
            if (playData == null || playData.size() == 0) {
                ToastUtils.show(getString(R.string.no_trace_data));
                return;
            }

            if (smoothMoveMarker != null) {
                smoothMoveMarker.destroy();
                smoothMoveMarker = null;
            }

            RoutePoint routePoint;
            //当前轨迹的播放列表
            ArrayList<LatLng> points = new ArrayList<>();
            for (int i = 0; i < playData.size(); i++) {
                routePoint = playData.get(i);
                points.add(routePoint.getPoint());
            }

            smoothMoveMarker = new SmoothMarker(mAMap);
            smoothMoveMarker.setDescriptor(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), drawableId)));
            LatLng drivePoint = points.get(index);
            processPosition = index;
            Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
            points.set(pair.first, drivePoint);
            List<RoutePoint> subList = playData.subList(pair.first, points.size());
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
    private SmoothMarker.SmoothFinishMoveListener smoothFinishMoveListener = new SmoothMarker.SmoothFinishMoveListener() {
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
        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(i).setVisible(true);
        }
        isMakeAllVisual = true;
    }

    /**
     * 监听动画播放的过程
     */
    private SmoothMarker.SmoothMarkerMoveListener smoothListener = new SmoothMarker.SmoothMarkerMoveListener() {
        @Override
        public void move(final double v, final RoutePoint currentPoint, final RoutePoint nextPoint) {

            runOnUiThread(new Runnable() {
                @SuppressLint({"SetTextI18n", "DefaultLocale"})
                @Override
                public void run() {
                    try {
                        if (playData == null || playData.size() < 3) {
                            return;
                        }

                        if (smoothMoveMarker != null) {
                            int process = (processPosition + smoothMoveMarker.getIndex()) * 100 / playData.size();
                            seekbarProcess.setProgress(process);
                        }

                        if (!isMakeAllVisual) {
                            //如果有图标没有显示出来,让所有图标都显示
                            allMarkerVisual();
                        }

                        updateMapCenter(new LatLng(currentPoint.getPoint().latitude, currentPoint.getPoint().longitude));

                        // 判断当前点的经纬度是否在中国范维内的经纬度多边形内，判断国内和海外
                        boolean isChina = false; // 是否在国内
                        com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(
                                currentPoint.getLat(), currentPoint.getLon());
                        if (com.baidu.mapapi.utils.SpatialRelationUtil.isPolygonContainsPoint(PolygonalArea.getBaiduListPoint(), latLng)) {
                            isChina = true;
                        }
                        if (isChina) {
                            AddressParseUtil.getAmapAddress(TrackAmapActivity.this, tvAddress, currentPoint.getLat(), currentPoint.getLon(),
                                    getString(R.string.address) + "：");
                        } else {
                            AddressParseUtil.getBaiduAddress(TrackAmapActivity.this, tvAddress, currentPoint.getLat(), currentPoint.getLon(),
                                    getString(R.string.address) + "：");
                        }

                        tvLocationTime.setText(getString(R.string.time) + "：" + DateUtils.timedate(String.valueOf(currentPoint.getTime())));
                        tvSpeed.setText(getString(R.string.speed) + "：" + ((double) currentPoint.getSpeed() / 10) + "km/h");
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
                playIndex = playData.size() - 1;
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
