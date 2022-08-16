package com.slxk.hounddog.mvp.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.di.component.DaggerGoogleLocationWirelessComponent;
import com.slxk.hounddog.mvp.contract.GoogleLocationWirelessContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.model.bean.BaseStationAddressBean;
import com.slxk.hounddog.mvp.model.bean.BluetoothInfoBean;
import com.slxk.hounddog.mvp.model.bean.ColorBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListForManagerResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceLocationInfoBean;
import com.slxk.hounddog.mvp.model.bean.DeviceLogBean;
import com.slxk.hounddog.mvp.model.bean.FunctionBean;
import com.slxk.hounddog.mvp.model.bean.MapOtherBean;
import com.slxk.hounddog.mvp.model.bean.MapSatelliteBean;
import com.slxk.hounddog.mvp.model.bean.MapTypeBean;
import com.slxk.hounddog.mvp.model.bean.RealTimeTrackGoogleBean;
import com.slxk.hounddog.mvp.model.bean.RealTimeTrackSwitchBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListForManagerPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListPutBean;
import com.slxk.hounddog.mvp.model.putbean.OnKeyFunctionPutBean;
import com.slxk.hounddog.mvp.presenter.GoogleLocationWirelessPresenter;
import com.slxk.hounddog.mvp.ui.activity.ConnectDeviceActivity;
import com.slxk.hounddog.mvp.ui.activity.DeviceDetailActivity;
import com.slxk.hounddog.mvp.ui.activity.FunctionMoreActivity;
import com.slxk.hounddog.mvp.ui.activity.LocationModeActivity;
import com.slxk.hounddog.mvp.ui.activity.MainActivity;
import com.slxk.hounddog.mvp.ui.activity.MainWirelessActivity;
import com.slxk.hounddog.mvp.ui.activity.ModifyNameActivity;
import com.slxk.hounddog.mvp.ui.activity.NavigationGoogleActivity;
import com.slxk.hounddog.mvp.ui.activity.OfflineMapActivity;
import com.slxk.hounddog.mvp.ui.activity.RecordActivity;
import com.slxk.hounddog.mvp.ui.activity.TrackGoogleWirelessActivity;
import com.slxk.hounddog.mvp.ui.adapter.FunctionAdapter;
import com.slxk.hounddog.mvp.ui.adapter.MapOtherAdapter;
import com.slxk.hounddog.mvp.ui.adapter.MapSatelliteAdapter;
import com.slxk.hounddog.mvp.ui.adapter.MapTypeAdapter;
import com.slxk.hounddog.mvp.utils.AddressParseUtil;
import com.slxk.hounddog.mvp.utils.BluetoothManagerUtil;
import com.slxk.hounddog.mvp.utils.BroadcastReceiverUtil;
import com.slxk.hounddog.mvp.utils.ColorUtil;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.DateUtils;
import com.slxk.hounddog.mvp.utils.DensityUtil;
import com.slxk.hounddog.mvp.utils.DeviceLogUtil;
import com.slxk.hounddog.mvp.utils.DeviceUtils;
import com.slxk.hounddog.mvp.utils.FileUtilApp;
import com.slxk.hounddog.mvp.utils.GoogleLocationUtils;
import com.slxk.hounddog.mvp.utils.GoogleMapUtils;
import com.slxk.hounddog.mvp.utils.GpsUtils;
import com.slxk.hounddog.mvp.utils.MyOrientationListener;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.AlertAppDialog;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.Gravity.END;
import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 谷歌离线地图定位
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 09:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class GoogleLocationWirelessFragment extends BaseFragment<GoogleLocationWirelessPresenter> implements GoogleLocationWirelessContract.View,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnCameraChangeListener {

    @BindView(R.id.tv_handheld_battery)
    TextView tvHandheldBattery;
    @BindView(R.id.tv_connected)
    TextView tvConnected;
    @BindView(R.id.tv_mobile_location_state)
    TextView tvMobileLocationState;
    @BindView(R.id.ll_handheld_battery)
    LinearLayout llHandheldBattery;
    @BindView(R.id.ll_bluetooth)
    LinearLayout llBluetooth;
    @BindView(R.id.ll_map_check)
    LinearLayout llMapCheck;
    @BindView(R.id.ll_download)
    LinearLayout llDownload;
    @BindView(R.id.iv_check_user)
    ImageView ivCheckUser;
    @BindView(R.id.iv_check_device)
    ImageView ivCheckDevice;
    @BindView(R.id.gridView_map_satellite)
    GridView gridViewMapSatellite;
    @BindView(R.id.gridView_map_type)
    GridView gridViewMapType;
    @BindView(R.id.gridView_other)
    GridView gridViewOther;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerlayout;
    @BindView(R.id.gridView_menu)
    GridView gridViewMenu;
    @BindView(R.id.ll_device_info)
    LinearLayout llDeviceInfo;
    @BindView(R.id.iv_up_down)
    ImageView ivUpDown;
    @BindView(R.id.btn_record)
    Button btnRecord;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_battery)
    TextView tvBattery;
    @BindView(R.id.iv_battery)
    ImageView ivBattery;
    @BindView(R.id.tv_gsm_number)
    TextView tvGsmNumber;
    @BindView(R.id.iv_gsm)
    ImageView ivGsm;
    @BindView(R.id.tv_bd_number)
    TextView tvBdNumber;
    @BindView(R.id.tv_bd_location)
    TextView tvBdLocation;
    @BindView(R.id.tv_gps_number)
    TextView tvGpsNumber;
    @BindView(R.id.tv_gps_location)
    TextView tvGpsLocation;
    @BindView(R.id.tv_galileo_number)
    TextView tvGalileoNumber;
    @BindView(R.id.iv_galileo_location)
    ImageView ivGalileoLocation;
    @BindView(R.id.iv_signal)
    ImageView ivSignal;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_mileage)
    TextView tvMileage;
    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.tv_static_time_hint)
    TextView tvStaticTimeHint;
    @BindView(R.id.tv_static_time)
    TextView tvStaticTime;
    @BindView(R.id.tv_communication_time)
    TextView tvCommunicationTime;
    @BindView(R.id.tv_location_time)
    TextView tvLocationTime;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_real_time_track)
    ImageView ivRealTimeTrack;
    @BindView(R.id.tv_other_hint)
    TextView tvOtherHint;
    @BindView(R.id.iv_bluetooth)
    ImageView ivBluetooth;
    @BindView(R.id.iv_check_device_top)
    ImageView ivCheckDeviceTop;
    @BindView(R.id.iv_check_user_top)
    ImageView ivCheckUserTop;

    private SupportMapFragment mapFragment; // 谷歌地图
    private GoogleMap mGoogleMap; // 谷歌地图

    // 蓝牙相关
    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
    // 设备信息处理Dao
    private MyDao mDeviceBeanDao;
    // 设备列表 - 数据库中的设备列表
    private ArrayList<DeviceModel> mDeviceModels;
    // 蓝牙工具类
    private BluetoothManagerUtil mBluetoothManagerUtil;
    // 蓝牙功能是否打开，是否需要重新扫码蓝牙
    private boolean isBluetoothOpen = false;

    // 地图：2D地图，卫星地图
    private ArrayList<MapSatelliteBean> mapSatelliteBeans;
    private MapSatelliteAdapter mapSatelliteAdapter;
    private boolean isSatelliteMap = true; // 是否是卫星地图
    private boolean isSwitchMapTraffic = false; // 是否开启了路况信息，false：否

    // 地图类型：高德地图，百度地图，谷歌地图
    private ArrayList<MapTypeBean> mapTypeBeans;
    private MapTypeAdapter mapTypeAdapter;

    // 地图的其他功能：路况，街景
    private ArrayList<MapOtherBean> mapOtherBeans;
    private MapOtherAdapter mapOtherAdapter;

    private int mapType = 0; // 地图类型,0：高德地图，1：百度地图，2：谷歌地图

    private ArrayList<FunctionBean> functionBeans; // 功能菜单
    private FunctionAdapter mAdapter;
    // 底部信息弹窗布局
    private BottomSheetBehavior<LinearLayout> mBehavior;

    private boolean isLocationChina = true; // 当前手机定位是否是在中国，在中国的话不显示3D地图建筑物名称，台湾显示建筑物名称
    private float mZoom = 17; // 缩放层级
    private LatLng centerPoint = new LatLng(39.90923, 116.397428);
    private double mLatitude = 0.0; // 定位获取到的纬度
    private double mLongitude = 0.0; // 定位获取到的经度
    private boolean isFirstLocation = true; // 是否是第一次定位，避免后续定位成功后，一直设置定位点为中心点
    private LatLngBounds.Builder mBuilder; // 所有设备可视化Builder
    private boolean isFirstData = true; // 是否是第一次加载数据
    private boolean isShowInfoWindow = false; // 是否有选中设备，显示详细信息悬浮框

    private HashMap<String, Marker> deviceMap; // 存储任务
    private String mCompassImei = "00000000"; // 罗盘虚拟imei号，用来判断是否点击了罗盘
    private String mUserImei = "11111111"; // 用户当前位置虚拟imei号，用来判断是否点击了用户位置

    private double selectLatForDevice; // 选中设备的纬度
    private double selectLonForDevice; // 选中设备的经度
    private DeviceModel selectCarBean; // 选中的设备

    // 设备列表 - 服务器中的设备列表
    private ArrayList<DeviceModel> mDeviceGetBeans;
    private static final int mGroupDeviceLimitSize = 1000; // 限制分组下设备获取数量
    private ArrayList<DeviceListForManagerResultBean.ItemsBean> mDeviceForAccountBeans; // 账号下的设备列表 - 账号登录
    private ArrayList<DeviceListResultBean.ItemsBean> mDeviceListBeans; // 获取到的服务器的设备列表-设备号登录
    private static final int mDeviceLimitSize = 100; // 限定设备数量,默认100

    // 同步服务器数据和本地数据
    private ArrayList<DeviceModel> mAddDeviceBeans;
    // 服务器没有的设备，需要从本地数据库中删除
    private ArrayList<DeviceModel> mDeleteDeviceBeans;
    // 判断本地数据和服务器数据，哪个为最新的定位数据，就是是用哪个
    private ArrayList<DeviceModel> mHasDeviceBeans;

    private static final int mCountDownTime = 5; // 倒计时固定时长
    private int mCountDown = mCountDownTime; // 倒计时递减时长
    private int fragmentPosition = 0; // 当前切换的fragment位置，只有在首页的时候，才开启请求数据

    private static final int Intent_Modify_Name = 3; // 修改设备名称

    // 广播服务
    private ChangePageReceiver receiver; // 注册广播接收器

    // 手机传感器方向
    private MyOrientationListener myOrientationListener;
    private float mXDirection = 0;

    // 设备日志上报操作
    private static String mDeviceLogPath = DeviceLogUtil.PATH;
    private ArrayList<File> mFileList; // 遍历文件夹下的文件
    private File mFile; // 当前正在上传的文件
    private ArrayList<DeviceLogBean> mDeviceLogBeans; // 设备日志临时存储
    private String mDeviceLogUrl = Api.Device_Log_Url; // 日志上传url

    private int switchPosition = 0; // 当前设备所在索引位置

    // 实时轨迹
    private ArrayList<RealTimeTrackSwitchBean> mTrackSwitchBeans; // 实时轨迹开关状态
    private ArrayList<RealTimeTrackGoogleBean> mTrackBeans; // 实时轨迹
    private int finalPosition; // 循环实时轨迹显示的索引下标
    // 设备颜色池
    private ArrayList<ColorBean> mColorLists; // 设备颜色池
    private Handler mTimerHandler; // 用于做延迟操作的
    private Marker selectMarker; //选中的设备

    private Timer mTimer;//定时检测设备连接状态
    private TimerTask mTask;

    public static GoogleLocationWirelessFragment newInstance() {
        GoogleLocationWirelessFragment fragment = new GoogleLocationWirelessFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerGoogleLocationWirelessComponent //如找不到该类,请编译一下项�?
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_google_location_wireless, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        functionBeans = new ArrayList<>();
        mDeviceModels = new ArrayList<>();
        mDeviceListBeans = new ArrayList<>();
        mDeviceForAccountBeans = new ArrayList<>();
        mDeviceGetBeans = new ArrayList<>();
        mAddDeviceBeans = new ArrayList<>();
        mDeleteDeviceBeans = new ArrayList<>();
        mHasDeviceBeans = new ArrayList<>();
        deviceMap = new HashMap<>();
        mapType = ConstantValue.getMapType();
        mFileList = new ArrayList<>();
        mDeviceLogBeans = new ArrayList<>();
        isSatelliteMap = ConstantValue.isMapSatelliteMap();
        mTimerHandler = new Handler();

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        try {
            mDeviceBeanDao = new MyDao(DeviceModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTrackSwitchBeans = new ArrayList<>();
        mTrackBeans = new ArrayList<>();
        mColorLists = new ArrayList<>();

        mColorLists.addAll(DeviceUtils.getColorList(getContext()));
        if (MyApplication.getMyApp().getTrackSwitchBeans() != null) {
            mTrackSwitchBeans.addAll(MyApplication.getMyApp().getTrackSwitchBeans());
        }

        if (Utils.isNetworkAvailable(getActivity())) {
            getDeviceLogFileToSDCard();
        }
        initOritationListener();
        getDeviceListForDB(false);
        onMapSatelliteData();
        onMapTypeData();
//        onMapOtherData();
        addFunctionData();
        onBottomSheetBehavior();
        initViewBluetooth();
        onDeviceListForService();
        checkPermissions(true);

        // 注册一个广播接收器，用于接收从首页跳转到报警消息页面的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("location_map");
        receiver = new ChangePageReceiver();
        //注册切换页面广播接收者
        LocalBroadcastManager.getInstance(MyApplication.getMyApp()).registerReceiver(receiver, filter);

        tvGpsNumber.setVisibility(View.GONE);
        tvGalileoNumber.setVisibility(View.GONE);
    }

    /**
     * 向服务器获取设备列表
     */
    private void onDeviceListForService() {
        // 判断是账号登录还是设备号登录，分开走不通的流程
        if (ConstantValue.isAccountLogin()) {
            getDeviceListForGroup();
        } else {
            getDeviceList();
        }
    }

    /**
     * 页面切换广播，广播接收器
     */
    private class ChangePageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                // 判断发送指令是做什么功能，0：切换页面，1：删除，添加设备，2：蓝牙列表操作
                int intentType = intent.getIntExtra("intent_type", 0);
                if (intentType == 0) {
                    fragmentPosition = intent.getIntExtra("type", 0);
                    if (fragmentPosition == 0) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.removeCallbacksAndMessages(null);
                    }
                } else if (intentType == 1) {
                    int type = 1; // 0：删除设备，1：添加设备
                    String imei = ""; // 设备imei号，可以为空
                    if (intent.hasExtra("type")) {
                        type = intent.getIntExtra("type", 0);
                    }
                    if (intent.hasExtra("imei")) {
                        imei = intent.getStringExtra("imei");
                    }
                    if (type == 0) {
                        onDeviceDelete(imei);
                    } else {
                        onDeviceListForService();
                    }
                } else if (intentType == 2) {
                    // 蓝牙操作类型，1：蓝牙配对，2：蓝牙断开，默认为0
                    int bleType = intent.getIntExtra("ble_type", 0);
                    BleDevice device = intent.getParcelableExtra("device");
                    switch (bleType) {
                        case 1:
                            if (isConnected(device)) {
                                BroadcastReceiverUtil.onBleDeviceState(getContext(), 1, device);
                            } else {
                                mBluetoothManagerUtil.connect(device);
                            }
                            break;
                        case 2:
                            mBluetoothManagerUtil.disConnected(device);
                            mBluetoothManagerUtil.onDeleteConnectBluetooth(device);
                            break;
                    }
                } else if (intentType == 3) { // 选中设备
                    String imei = ""; // 设备imei号
                    if (intent.hasExtra("imei")) {
                        imei = intent.getStringExtra("imei");
                    }
                    onSelectDevice(imei);
                }
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);//选择手势
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);//缩放按钮
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnMyLocationClickListener(this);
        mGoogleMap.setOnCameraChangeListener(this);
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            if (mGoogleMap != null) {
//                mGoogleMap.setMyLocationEnabled(true);
//            }
//        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(centerPoint)      // Sets the center of the map to Mountain View
                .zoom(mZoom)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                  // Sets the tilt of the camera to 30 degrees
                .build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        onMapSatelliteType();
        getLocation(true);
    }

    @Override
    public void onCameraChange(@NonNull CameraPosition cameraPosition) {
        mZoom = cameraPosition.zoom;
        centerPoint = cameraPosition.target;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (isShowInfoWindow) {
            isShowInfoWindow = false;
            selectCarBean = null;
            MyApplication.getMyApp().setDevice_imei("");
            setBottomSheetBehaviorHide();
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String imei = marker.getTitle();
        onSelectDevice(imei);
        return true;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    /**
     * 选中设备
     *
     * @param imei
     */
    private void onSelectDevice(String imei) {
        if (!TextUtils.isEmpty(imei) && !imei.equals(mCompassImei) && !imei.equals(mUserImei)) {
            selectCarBean = null;
            for (DeviceModel bean : mDeviceModels) {
                if (imei.equals(bean.getImei())) {
                    selectCarBean = bean;
                    break;
                }
            }

            if (selectCarBean != null) {
                if (selectCarBean.getLat() == 0 && selectCarBean.getLon() == 0) {
                    ToastUtils.show(getString(R.string.device_not_has_location_data));
                    isShowInfoWindow = false;
                    selectCarBean = null;
                    MyApplication.getMyApp().setDevice_imei("");
                    setBottomSheetBehaviorHide();
                } else {
                    isShowInfoWindow = true;
                    setDeviceDetailData();
                    MyApplication.getMyApp().setDevice_imei(selectCarBean.getImei());
                    selectLatForDevice = (double) selectCarBean.getLat() / 1000000;
                    selectLonForDevice = (double) selectCarBean.getLon() / 1000000;
                    centerPoint = new LatLng(selectLatForDevice, selectLonForDevice);
                    updateMapCenter(imei);
                    setBottomSheetBehaviorExpanded();
                }
            }
        } else {
            isShowInfoWindow = false;
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
    public void getLocation(boolean isFirst) {
        String ll = GoogleLocationUtils.getLocation(getActivity());
        if (!TextUtils.isEmpty(ll)) {
            String[] location = ll.split(",");
            mLatitude = Double.parseDouble(location[0]);
            mLongitude = Double.parseDouble(location[1]);
            // 我的位置
            LatLng myLocation = getLatLngChange(mLatitude, mLongitude);
            isLocationChina = GpsUtils.isChinaLocation(mLatitude, mLongitude);
            SPUtils.getInstance().put(ConstantValue.Is_In_China, isLocationChina);
            MyApplication.getMyApp().setLatitude(mLatitude);
            MyApplication.getMyApp().setLongitude(mLongitude);
            MyApplication.getMyApp().setMobile_location_state(getString(R.string.mobile_gps_location));
            addMyLocalMaker(myLocation, isFirst);
            isFirstLocation = false;
        }
    }

    /**
     * 将我的位置的坐标添加进去
     *
     * @param location 我的位置经纬度
     * @param isFirst  是否是首次定位手机位置
     */
    private void addMyLocalMaker(LatLng location, boolean isFirst) {
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

            if (isFirst && mGoogleMap != null) { //首次定位
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, mZoom));
            }
            onShowCompass(location);

            tvMobileLocationState.setText(getString(R.string.mobile_gps_location));
            MyApplication.getMyApp().setMobile_location_state(getString(R.string.mobile_gps_location));
            MyApplication.getMyApp().setLatitude(mLatitude);
            MyApplication.getMyApp().setLongitude(mLongitude);
        }
    }

    /**
     * 绘制设备图标等信息
     *
     * @return
     */
    private BitmapDescriptor getUserLocationMarkerIcon() {
        View view = View.inflate(getContext(), R.layout.layout_user_location_marker, null);
        return convertViewToBitmap(view);
    }

    /**
     * 绘制罗盘位置
     */
    private void onShowCompass(LatLng compassLng) {
        // 开始绘制设备marker
        if (mGoogleMap != null) {
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
        View view = View.inflate(getContext(), R.layout.layout_compass_marker, null);
        ImageView ivCompass = view.findViewById(R.id.iv_compass);
        ivCompass.setImageResource(Utils.isLocaleForCN() ? R.drawable.icon_compass_zh : R.drawable.icon_compass_es);
        return convertViewToBitmap(view);
    }

    /**
     * 卫星图需要做偏移
     *
     * @param lat
     * @param lon
     * @return
     */
    private LatLng getLatLngChange(double lat, double lon) {
        LatLng latLng;
        if (isSatelliteMap) {
            // 设备经纬度，切换为卫星地图之后，坐标系要转为WGS84坐标系
            double[] deviceWGS84 = GpsUtils.toWGS84Point(lat, lon);
            latLng = new LatLng(deviceWGS84[0], deviceWGS84[1]);
        } else {
            latLng = GpsUtils.googleGPSConverter(lat, lon);
        }
        return latLng;
    }

    /**
     * 设置当前点位于地图中心
     */
    private void updateMapCenter(String imei) {
        if (mGoogleMap != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, mZoom));
            if (imei.length() > 0) {
                Marker marker = deviceMap.get(imei);
                if (marker != null) {
                    if (marker != selectMarker) { //选择设备更换时，新选中的设备置为最高级
                        if (selectMarker != null) {
                            selectMarker.setZIndex(0.0f);
                        }
                        marker.setZIndex(999);
                        selectMarker = marker;
                    }
                }
            }
        }
    }

    /**
     * 判断设备是否已连接
     *
     * @param bleDevice
     * @return
     */
    private boolean isConnected(BleDevice bleDevice) {
        return BleManager.getInstance().isConnected(bleDevice);
    }

    /**
     * 删除设备
     *
     * @param imei
     */
    private void onDeviceDelete(String imei) {
        DeviceModel mModel = null;
        for (DeviceModel model : mDeviceModels) {
            if (model.getImei().equals(imei)) {
                mModel = model;
                break;
            }
        }
        if (mModel != null) {
            if (selectCarBean != null) {
                if (selectCarBean.getImei().equals(imei)) {
                    selectCarBean = null;
                    setBottomSheetBehaviorHide();
                }
            }
            mDeviceModels.remove(mModel);
            mDeviceGetBeans.remove(mModel);
            mBluetoothManagerUtil.onDeviceDelete(mModel);
            onResetValue();
            onDeviceToMapShow(true);
        }
    }

    /**
     * 从数据库查询设备列表数据
     */
    private void getDeviceListForDB(boolean isSynchronousData) {
        mDeviceModels.clear();
        if (getDeviceListDataBase() != null) {
            mDeviceModels.addAll(getDeviceListDataBase());
        }
        if (isSynchronousData) {
            mBluetoothManagerUtil.setAccountDeviceList(mDeviceModels);
        } else {
            initRealTimeTrackData();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onDeviceToMapShow(true);
            }
        }, 500);
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
     * 添加地图类型，2D地图，卫星地图
     */
    private void onMapSatelliteData() {
        mapSatelliteBeans = new ArrayList<>();
        mapSatelliteBeans.add(new MapSatelliteBean(0, getString(R.string.map_2d), false));
        mapSatelliteBeans.add(new MapSatelliteBean(1, getString(R.string.map_satellite), false));
        if (isSatelliteMap) {
            mapSatelliteBeans.get(1).setSelect(true);
        } else {
            mapSatelliteBeans.get(0).setSelect(true);
        }
        SPUtils.getInstance().put(ConstantValue.Is_Map_SatelliteMap, isSatelliteMap);

        mapSatelliteAdapter = new MapSatelliteAdapter(getContext(), R.layout.item_map_satellite, mapSatelliteBeans);
        gridViewMapSatellite.setAdapter(mapSatelliteAdapter);
        gridViewMapSatellite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (MapSatelliteBean bean : mapSatelliteBeans) {
                    bean.setSelect(false);
                }
                mapSatelliteBeans.get(position).setSelect(true);
                mapSatelliteAdapter.notifyDataSetChanged();
                onCheckMapType(mapSatelliteBeans.get(position).getId());
            }
        });
    }

    /**
     * 切换地图类型，2D地图，卫星地图
     *
     * @param id
     */
    @SuppressLint("WrongConstant")
    private void onCheckMapType(int id) {
        drawerlayout.closeDrawer(END);
        if (mGoogleMap != null) {
            isSatelliteMap = id == 1;
            SPUtils.getInstance().put(ConstantValue.Is_Map_SatelliteMap, isSatelliteMap);
            onMapSatelliteType();

            // 刷新数据
            mCountDown = 0;
//            getLocation();
        }
    }

    private void onMapSatelliteType() {
        if (isSatelliteMap) {
            if (isLocationChina) {
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else {
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        } else {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    /**
     * 添加地图类型，高德地图，百度地图，谷歌地图
     */
    private void onMapTypeData() {
        mapTypeBeans = new ArrayList<>();
        mapTypeBeans.add(new MapTypeBean(0, getString(R.string.map_amap), false));
        mapTypeBeans.add(new MapTypeBean(1, getString(R.string.map_baidu), false));
        mapTypeBeans.add(new MapTypeBean(2, getString(R.string.map_google), false));

        for (MapTypeBean bean : mapTypeBeans) {
            if (bean.getId() == mapType) {
                bean.setSelect(true);
                break;
            }
        }

        mapTypeAdapter = new MapTypeAdapter(getContext(), R.layout.item_map_type, mapTypeBeans);
        gridViewMapType.setAdapter(mapTypeAdapter);
        gridViewMapType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Utils.isButtonQuickClick() && mapType != mapTypeBeans.get(position).getId()) {
                    SPUtils.getInstance().put(ConstantValue.MAP_TYPE, mapTypeBeans.get(position).getId());
                    ActivityUtils.finishAllActivities();
                    if (ConstantValue.getAppLocationMode() == 0) {
                        launchActivity(MainWirelessActivity.newInstance());
                    } else {
                        launchActivity(MainActivity.newInstance());
                    }
                }
            }
        });
    }

    /**
     * 重置地图类型和卫星地图选择
     */
    private void onResetMapTypeData() {
        if (mapSatelliteBeans.size() > 0) {
            for (MapSatelliteBean bean : mapSatelliteBeans) {
                bean.setSelect(false);
            }
            if (isSatelliteMap) {
                mapSatelliteBeans.get(1).setSelect(true);
            } else {
                mapSatelliteBeans.get(0).setSelect(true);
            }
            mapSatelliteAdapter.notifyDataSetChanged();
        }

        if (mapTypeBeans.size() > 0) {
            for (MapTypeBean bean : mapTypeBeans) {
                if (bean.getId() == mapType) {
                    bean.setSelect(true);
                } else {
                    bean.setSelect(false);
                }
            }
            mapTypeAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加地图的其他一些功能
     */
    private void onMapOtherData() {
        tvOtherHint.setVisibility(View.VISIBLE);
        mapOtherBeans = new ArrayList<>();
        mapOtherBeans.add(new MapOtherBean(0, getString(R.string.road_conditions), false));
        mapOtherBeans.add(new MapOtherBean(1, getString(R.string.street_view), false));

        mapOtherAdapter = new MapOtherAdapter(getContext(), R.layout.item_map_type, mapOtherBeans);
        gridViewOther.setAdapter(mapOtherAdapter);
        gridViewOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Utils.isButtonQuickClick()) {
                    if (mapOtherBeans.get(position).getId() == 0) {
                        boolean isSelect = mapOtherBeans.get(position).isSelect();
                        mapOtherBeans.get(position).setSelect(!isSelect);
                        mapOtherAdapter.notifyDataSetChanged();
                        onSwitchMapTraffic(mapOtherBeans.get(position).isSelect());
                    } else {
                        onSeePanorama();
                    }
                }
            }
        });
    }

    /**
     * 查看全景地图
     */
    @SuppressLint("WrongConstant")
    private void onSeePanorama() {
        if (selectCarBean == null) {
            ToastUtils.show(getString(R.string.device_select_now));
            return;
        }

        drawerlayout.closeDrawer(END);

//        launchActivity(BaiduPanoramaActivity.newInstance());
    }

    /**
     * 是否开启了路况
     *
     * @param isSwitch
     */
    @SuppressLint("WrongConstant")
    private void onSwitchMapTraffic(boolean isSwitch) {
        drawerlayout.closeDrawer(END);
        if (mGoogleMap != null) {
            isSwitchMapTraffic = isSwitch;
            mGoogleMap.setTrafficEnabled(isSwitchMapTraffic);
        }
    }

    /**
     * 添加功能数据
     */
    private void addFunctionData() {
        functionBeans.clear();
        functionBeans.add(new FunctionBean(0, getString(R.string.menu_0), R.drawable.icon_menu_0));
        functionBeans.add(new FunctionBean(1, getString(R.string.menu_1), R.drawable.icon_menu_1));
        functionBeans.add(new FunctionBean(2, getString(R.string.menu_2), R.drawable.icon_menu_2));
        functionBeans.add(new FunctionBean(3, getString(R.string.menu_3), R.drawable.icon_menu_3));
        functionBeans.add(new FunctionBean(4, getString(R.string.menu_4), R.drawable.icon_menu_4));
        functionBeans.add(new FunctionBean(5, getString(R.string.menu_5), R.drawable.icon_menu_5));
        functionBeans.add(new FunctionBean(6, getString(R.string.menu_6), R.drawable.icon_menu_6));
        functionBeans.add(new FunctionBean(7, getString(R.string.menu_7), R.drawable.icon_menu_7));
        if (mAdapter == null) {
            mAdapter = new FunctionAdapter(getContext(), R.layout.item_menu_home, functionBeans);
            gridViewMenu.setAdapter(mAdapter);
            gridViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (Utils.isButtonQuickClick()) {
                        onMenuClick(functionBeans.get(position).getId());
                    }
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 菜单栏点击事件
     *
     * @param id
     */
    private void onMenuClick(int id) {
        if (selectCarBean != null) {
            switch (id) {
                case 0:
                    onDeviceNavigation();
                    break;
                case 1:
                    onModifyDeviceName();
                    break;
                case 2:
                    onConfirmOneKeyFunction(ResultDataUtils.Function_Restart);
                    break;
                case 3:
                    launchActivity(TrackGoogleWirelessActivity.newInstance(selectCarBean.getImei(), selectCarBean.getDevice_name()));
                    break;
                case 4:
                    onSeeDeviceDetail();
                    break;
                case 5:
                    onConfirmOneKeyFunction(ResultDataUtils.Function_Finddev);
                    break;
                case 6:
                    launchActivity(LocationModeActivity.newInstance(selectCarBean.getSimei()));
                    break;
                case 7:
                    launchActivity(FunctionMoreActivity.newInstance(selectCarBean.getImei(), selectCarBean.getSimei(), true));
                    break;
            }
        }
    }

    /**
     * 一键功能提交
     *
     * @param key 功能值
     */
    private void onConfirmOneKeyFunction(String key) {
        AlertBean bean = new AlertBean();
        bean.setTitle(getString(R.string.tip));
        switch (key) {
            case ResultDataUtils.Function_Restart:
                bean.setAlert(getString(R.string.restart_hint));
                break;
            case ResultDataUtils.Function_Finddev:
                bean.setAlert(getString(R.string.looking_for_equipment));
                break;
        }
        AlertAppDialog dialog = new AlertAppDialog();
        dialog.show(getFragmentManager(), bean, new AlertAppDialog.onAlertDialogChange() {
            @Override
            public void onConfirm() {
                submitOneKeyFunction(key);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 一键功能提交
     *
     * @param key 功能值
     */
    private void submitOneKeyFunction(String key) {
        OnKeyFunctionPutBean.ParamsBean paramsBean = new OnKeyFunctionPutBean.ParamsBean();
        paramsBean.setType(key);
        paramsBean.setSimei(selectCarBean.getSimei());

        OnKeyFunctionPutBean bean = new OnKeyFunctionPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_OnKey_Function);
        bean.setModule(ModuleValueService.Module_For_OnKey_Function);

        showProgressDialog();

        getPresenter().submitOneKeyFunction(bean);
    }

    /**
     * 导航
     */
    private void onDeviceNavigation() {
        int state = getDeviceState(selectCarBean);
        launchActivity(NavigationGoogleActivity.newInstance(selectCarBean.getLat(), selectCarBean.getLon(), state));
    }

    private int getDeviceState(DeviceModel model) {
        int state = 2;
        if (!TextUtils.isEmpty(model.getDev_state())) {
            String strFormat = DeviceUtils.onLocationType(model.getDev_state());
            char a6 = strFormat.charAt(1); // 设备状态
            if ("1".equals(String.valueOf(a6))) {
                state = 1;
            } else {
                state = 0;
            }
            if (DeviceUtils.isDeviceOnOff(model.getCommunication_time())) {
                state = 2;
            }
        }
        return state;
    }

    /**
     * 修改设备名称
     */
    private void onModifyDeviceName() {
        Intent intent = new Intent(getActivity(), ModifyNameActivity.class);
        intent.putExtra("imei", selectCarBean.getImei());
        intent.putExtra("name", selectCarBean.getDevice_name());
        intent.putExtra("simei", selectCarBean.getSimei());
        startActivityForResult(intent, Intent_Modify_Name);
    }

    /**
     * 查看设备详情
     */
    private void onSeeDeviceDetail() {
        Intent intent = new Intent(getActivity(), DeviceDetailActivity.class);
        intent.putExtra("bean", selectCarBean);
        startActivityForResult(intent, Intent_Modify_Name);
    }

    /**
     * 底部拖拉窗口布局
     */
    private void onBottomSheetBehavior() {
        //底部弹框初始化
        mBehavior = BottomSheetBehavior.from(llDeviceInfo);
        mBehavior.setHideable(true);
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBehavior.setBottomSheetCallback(mBottomSheetCallback);
        ((AnimationDrawable) ivUpDown.getDrawable()).start();
    }

    /**
     * 底部菜单显示回调
     */
    BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {
                case BottomSheetBehavior.STATE_COLLAPSED:
                    // LogUtil.e("关闭");
                    break;
                case BottomSheetBehavior.STATE_DRAGGING:
                    // LogUtil.e("过渡");
                    break;
                case BottomSheetBehavior.STATE_EXPANDED:
                    // LogUtil.e("展开");
                    break;
                case BottomSheetBehavior.STATE_HIDDEN:
                    // LogUtil.e("隐藏");
                    break;
                case BottomSheetBehavior.STATE_SETTLING:
                    // LogUtil.e("自动打开/关闭");
                    break;
            }
        }


        /**
         * 当bottom sheets拖拽时回调
         * @param slideOffset 滑动量;从0到1时向上移动
         */
        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            if (selectCarBean == null) {//防止取消选中，底部菜单不收回
                return;
            }
            if (llDeviceInfo != null) {
                float height = gridViewMenu.getHeight() * 1f / (llDeviceInfo.getHeight());
                if (slideOffset < height / 2) {
                    llDeviceInfo.post(new Runnable() {
                        @Override
                        public void run() {
                            int height = 0;
                            if (getContext() != null)
                                height = DensityUtil.dp2px(getContext(), 16); // gridViewMenu顶部和底部距离
                            mBehavior.setPeekHeight(llDeviceInfo.getHeight() - gridViewMenu.getHeight() - height);
                            mBehavior.setHideable(false);
                        }
                    });
                }
            }
            if (slideOffset >= 0) {
                // 向上箭头处理
                ivUpDown.setRotation(180 * slideOffset);
            } else {//向下滑动
                ivUpDown.setRotation(0);
            }
        }
    };

    /**
     * 显示底部信息弹窗
     */
    private void setBottomSheetBehaviorExpanded() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            if (llDeviceInfo != null) {
                llDeviceInfo.post(new Runnable() {
                    @Override
                    public void run() {
                        mBehavior.setPeekHeight(llDeviceInfo.getHeight());
                    }
                });
            }
            mBehavior.setHideable(true);
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            ivCheckDeviceTop.setVisibility(View.VISIBLE);
//            ivCheckUserTop.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏底部信息弹窗
     */
    private void setBottomSheetBehaviorHide() {
        //隐藏下弹框
        mBehavior.setHideable(true);
        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        ivCheckDeviceTop.setVisibility(View.GONE);
//        ivCheckUserTop.setVisibility(View.GONE);
    }

    /**
     * 获取设备列表 - 设备号登录
     */
    private void getDeviceList() {
        DeviceListPutBean.ParamsBean paramsBean = new DeviceListPutBean.ParamsBean();
        paramsBean.setLimit_size(mDeviceLimitSize);

        DeviceListPutBean bean = new DeviceListPutBean();
        bean.setFunc(ModuleValueService.Fuc_For_Device_List);
        bean.setModule(ModuleValueService.Module_For_Device_List);
        bean.setParams(paramsBean);

        getPresenter().getDeviceList(bean);
    }

    /**
     * 获取分组下的设备列表 - 账号登录
     */
    private void getDeviceListForGroup() {
        DeviceListForManagerPutBean.ParamsBean paramsBean = new DeviceListForManagerPutBean.ParamsBean();
        paramsBean.setLimit_size(mGroupDeviceLimitSize);
        paramsBean.setFamilyid(ConstantValue.getFamilySid());

        DeviceListForManagerPutBean bean = new DeviceListForManagerPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Device_List_For_Group);
        bean.setModule(ModuleValueService.Module_For_Device_List_For_Group);

        getPresenter().getDeviceListForGroup(bean);
    }

    /**
     * 倒计时刷新数据
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            mCountDown--;
            if (mCountDown <= 0) {
                mCountDown = mCountDownTime;
                onDeviceToMapShow(false);
                if (!isBluetoothOpen) {
                    checkPermissions(false);
                }
            }
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    };

    //收到消息回主UI刷新界面
    Handler myHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1000:
                    mBluetoothManagerUtil.onShowBluetoothInfo();
                    break;
            }

            super.handleMessage(msg);
        }
    };
    @Override
    public void onResume() {
        // 开启方向传感器
        if (myOrientationListener != null) {
            myOrientationListener.start();
        }
        if (fragmentPosition == 0 && mGoogleMap != null) {
            if (!isFirstData) {
                mCountDown = 0;
            }
            handler.sendEmptyMessage(1);
        }

        // 定时器
        mTimer = new Timer();
        // 定时任务
        mTask = new TimerTask() {
            @Override
            public void run() {
                if(myHandler != null) {
                    myHandler.sendEmptyMessage(1000);
                }
            }
        };
        mTimer.schedule(mTask, 1000, 1000);// 延迟1000毫秒后执行定时任务，并且每隔1000毫秒执行一次定时任务

        super.onResume();
    }

    @Override
    public void onPause() {
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public void onStop() {
        // 关闭方向传感器
        if (myOrientationListener != null) {
            myOrientationListener.stop();
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (mTimerHandler != null) {
            mTimerHandler.removeCallbacksAndMessages(null);
            mTimerHandler = null;
        }

        if (myHandler != null){
            myHandler.removeCallbacksAndMessages(null);
            myHandler = null;
        }

        if (mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }

        LocalBroadcastManager.getInstance(MyApplication.getMyApp()).unregisterReceiver(receiver);
        super.onDestroyView();
    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某�?Fragment 对象执行一些方�?
     * 建议在有多个需要与外界交互的方法时, 统一�?{@link Message}, 通过 what 字段来区分不同的方法, �?{@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操�? 可以起到分发的作�?     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周�? 如果调用 {@link #setData(Object)} 方法�?{@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用�?Presenter 的方�? 是会报空�? 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调�?{@link #setData(Object)}, �?{@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以�?{@code null}
     */
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

    @SuppressLint("WrongConstant")
    @OnClick({R.id.ll_bluetooth, R.id.ll_map_check, R.id.ll_download, R.id.iv_check_user, R.id.iv_check_device, R.id.btn_record,
            R.id.iv_real_time_track, R.id.iv_check_user_top, R.id.iv_check_device_top})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()) {
            switch (view.getId()) {
                case R.id.ll_bluetooth:
                    launchActivity(ConnectDeviceActivity.newInstance());
                    break;
                case R.id.ll_map_check:
                    onResetMapTypeData();
                    drawerlayout.openDrawer(END);
                    break;
                case R.id.ll_download:
                    launchActivity(OfflineMapActivity.newInstance());
                    break;
                case R.id.iv_check_user:
                case R.id.iv_check_user_top:
                    if (isFirstLocation) {
                        ToastUtils.show(getString(R.string.location_now));
                        return;
                    }
                    centerPoint = new LatLng(mLatitude, mLongitude);
                    updateMapCenter("");
                    ToastUtils.show(getString(R.string.check_location_man));
                    break;
                case R.id.iv_check_device:
                case R.id.iv_check_device_top:
                    if (isHasLocationDataForDevice()) {
                        onCheckNextDevice();
                    } else {
                        if (ConstantValue.isAccountLogin()) {
                            ToastUtils.show(getString(R.string.account_not_has_device));
                        } else {
                            ToastUtils.show(getString(R.string.device_not_has_location_data));
                        }
                    }
                    break;
                case R.id.btn_record:
                    if (selectCarBean != null) {
                        launchActivity(RecordActivity.newInstance(selectCarBean.getImei(), selectCarBean.getSimei()));
                    }
                    break;
                case R.id.iv_real_time_track:
                    if (selectCarBean != null) {
                        onOpenSwitchRealTimeTrack();
                    }
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Intent_Modify_Name) {
                if (data != null) {
                    String name = data.getStringExtra("name");
                    onSaveDeviceName(name);
                }
            }
        } else if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (checkGPSIsOpen()) {
                isBluetoothOpen = true;
                mBluetoothManagerUtil.startBluetoothService();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 保存设备名称，同时更新数据库
     *
     * @param name
     */
    private void onSaveDeviceName(String name) {
        selectCarBean.setDevice_name(name);
        for (DeviceModel bean : mDeviceModels) {
            if (selectCarBean.getImei().equals(bean.getImei())) {
                bean.setDevice_name(name);
                selectCarBean.setId(bean.getId());
                break;
            }
        }
        mBluetoothManagerUtil.onDeviceUpdate(selectCarBean);
        if (!TextUtils.isEmpty(selectCarBean.getDevice_name())) {
            tvDeviceName.setText(selectCarBean.getDevice_name());
        } else {
            tvDeviceName.setText(selectCarBean.getImei());
        }
    }

    /**
     * 切换到后一个设备
     */
    private void onCheckNextDevice() {
        switchPosition++;
        if (switchPosition > mDeviceModels.size() - 1) {
            switchPosition = 0;
        }
        if (mDeviceModels.size() > 0) {
//            onSelectDevice(mDeviceModels.get(switchPosition).getImei());

            DeviceModel model = mDeviceModels.get(switchPosition);
            String imei = model.getImei();
            if (model.getLat() == 0 && model.getLon() == 0) {
                onCheckNextDevice();
            } else {
                selectLatForDevice = (double) selectCarBean.getLat() / 1000000;
                selectLonForDevice = (double) selectCarBean.getLon() / 1000000;
                centerPoint = new LatLng(selectLatForDevice, selectLonForDevice);
                if (!TextUtils.isEmpty(imei))
                    updateMapCenter(imei);

                isShowInfoWindow = false;
                selectCarBean = null;
                MyApplication.getMyApp().setDevice_imei("");
                setBottomSheetBehaviorHide();

                ToastUtils.show(getString(R.string.check_location_device));
            }
        }
    }

    /**
     * 设备列表是否有有效定位数据的设备
     *
     * @return
     */
    private boolean isHasLocationDataForDevice() {
        boolean isHas = false;
        for (DeviceModel model : mDeviceModels) {
            if (model.getLat() != 0 && model.getLon() != 0) {
                isHas = true;
                break;
            }
        }
        return isHas;
    }

    @Override
    public void getDeviceListSuccess(DeviceListResultBean deviceListResultBean) {
        mDeviceListBeans.clear();
        mDeviceGetBeans.clear();
        if (deviceListResultBean.getItems() != null && deviceListResultBean.getItems().size() > 0) {
            mDeviceListBeans.addAll(deviceListResultBean.getItems());
        }

        if (mDeviceListBeans.size() > 0) {
            for (DeviceListResultBean.ItemsBean itemsBean : mDeviceListBeans) {
                DeviceLocationInfoBean bean = DeviceUtils.parseDeviceData(itemsBean.getLast_pos());
                DeviceModel model = new DeviceModel();
                model.setImei(String.valueOf(itemsBean.getImei()));
                model.setSimei(itemsBean.getSimei());
                model.setLat(bean.getLat());
                model.setLon(bean.getLon());
                model.setSpeed(bean.getSpeed());
                model.setTime(DateUtils.timeConversionDate_two(String.valueOf(bean.getTime())));
                model.setPower(itemsBean.getPower());
                model.setDevice_name(itemsBean.getCar_number());
                model.setCommunication_time(DateUtils.timeConversionDate_two(String.valueOf(bean.getTime())));
                mDeviceGetBeans.add(model);
            }

            onDeviceFilterForDB();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDeviceListForDB(true);
            }
        }, 1000);

        isFirstData = false;
    }

    @Override
    public void onError() {
        isFirstData = false;
        mBluetoothManagerUtil.setAccountDeviceList(mDeviceModels);
    }

    @Override
    public void getDeviceListForGroupSuccess(DeviceListForManagerResultBean deviceListForManagerResultBean) {
        mDeviceForAccountBeans.clear();
        mDeviceGetBeans.clear();
        if (deviceListForManagerResultBean.getItems() != null && deviceListForManagerResultBean.getItems().size() > 0) {
            mDeviceForAccountBeans.addAll(deviceListForManagerResultBean.getItems());
        }
        if (mDeviceForAccountBeans.size() > 0) {
            for (DeviceListForManagerResultBean.ItemsBean itemsBean : mDeviceForAccountBeans) {
                DeviceLocationInfoBean bean = DeviceUtils.parseDeviceData(itemsBean.getLast_pos());
                DeviceModel model = new DeviceModel();
                model.setImei(String.valueOf(itemsBean.getImei()));
                model.setSimei(itemsBean.getSimei());
                model.setLat(bean.getLat());
                model.setLon(bean.getLon());
                model.setSpeed(bean.getSpeed());
                model.setTime(DateUtils.timeConversionDate_two(String.valueOf(bean.getTime())));
                model.setPower(itemsBean.getPower());
                model.setDevice_name(itemsBean.getCar_number());
                model.setCommunication_time(DateUtils.timeConversionDate_two(String.valueOf(bean.getTime())));
                mDeviceGetBeans.add(model);
            }

            onDeviceFilterForDB();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDeviceListForDB(true);
            }
        }, 1000);

        isFirstData = false;
    }

    @Override
    public void onDismissProgress() {
        isFirstData = false;
        dismissProgressDialog();
    }

    @Override
    public void submitOneKeyFunctionSuccess(BaseBean baseBean) {
        ToastUtils.show(getString(R.string.operation_success));
    }

    @Override
    public void getBaseStationLocationSuccess(BaseStationAddressBean baseStationAddressBean, DeviceModel model) {
        if (baseStationAddressBean != null && baseStationAddressBean.getResult() != null) {
            if (!TextUtils.isEmpty(baseStationAddressBean.getResult().getLocation())) {
                String[] location = baseStationAddressBean.getResult().getLocation().split(",");
                if (location.length == 2) {
                    String strLat = location[1];
                    String strLon = location[0];
                    double lat = Double.parseDouble(strLat);
                    double lon = Double.parseDouble(strLon);
                    model.setLat((long) (lat * 1000000));
                    model.setLon((long) (lon * 1000000));

                }
            }
        }
    }

    /**
     * 同步服务器最新的设备列表到本地数据库
     */
    private void onDeviceFilterForDB() {
        mAddDeviceBeans.clear();
        mDeleteDeviceBeans.clear();
        mHasDeviceBeans.clear();
        for (DeviceModel model : mDeviceGetBeans) {
            boolean isHas = false; // 是否包含设备
            for (DeviceModel bean : mDeviceModels) {
                if (model.getImei().equals(bean.getImei())) {
                    isHas = true;
                    break;
                }
            }
            if (!isHas) {
                mAddDeviceBeans.add(model);
            }
        }

        for (DeviceModel model : mDeviceModels) {
            boolean isHas = false; // 是否包含设备
            for (DeviceModel bean : mDeviceGetBeans) {
                if (model.getImei().equals(bean.getImei())) {
                    isHas = true;
                    break;
                }
            }
            if (isHas) {
                mHasDeviceBeans.add(model);
            } else {
                mDeleteDeviceBeans.add(model);
            }
        }
        // 判断服务器数据与本地数据哪个最新
        if (mHasDeviceBeans.size() > 0) {
            ArrayList<DeviceModel> updateBeans = new ArrayList<>();
            for (DeviceModel bean : mHasDeviceBeans) {
                for (DeviceModel getBean : mDeviceGetBeans) {
                    if (bean.getImei().equals(getBean.getImei())) {
                        if (!TextUtils.isEmpty(bean.getCommunication_time()) && !TextUtils.isEmpty(getBean.getCommunication_time())) {
                            if (DateUtils.data_5(getBean.getCommunication_time()) > DateUtils.data_5(bean.getCommunication_time())) {
                                getBean.setId(bean.getId());
                                updateBeans.add(getBean);
                                break;
                            }
                        }
                    }
                }
            }
            if (updateBeans.size() > 0) {
                try {
                    mDeviceBeanDao.update(updateBeans);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (mDeleteDeviceBeans.size() > 0) {
            mDeviceModels.removeAll(mDeleteDeviceBeans);
            try {
                mDeviceBeanDao.delete(mDeleteDeviceBeans);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mAddDeviceBeans.size() > 0) {
            mDeviceModels.addAll(mAddDeviceBeans);
            // 更新新增的设备到数据库
            onUpdateDeviceListForDB();
        }

        selectCarBean = null;
        setBottomSheetBehaviorHide();
        onResetValue();
    }

    /**
     * 更新新增的设备到数据库
     */
    private void onUpdateDeviceListForDB() {
        if (mAddDeviceBeans.size() > 0) {
            try {
                mDeviceBeanDao.insert(mAddDeviceBeans);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重置一些参数
     */
    private void onResetValue() {
        isFirstData = false;
        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }
        deviceMap.clear();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLocation(false);
            }
        }, 1500);
    }

    /**
     * 接收机发送过来的设备信息，更新设备信息
     *
     * @param model
     */
    private void onRefreshDeviceInfo(DeviceModel model) {
        if (model.getLat() == 0 && model.getLon() == 0) {
            return;
        }
        double lat = (double) model.getLat() / 1000000;
        double lon = (double) model.getLon() / 1000000;

        int state = getDeviceState(model);

        // 开始绘制设备marker
        LatLng deviceLatLng = new LatLng(lat, lon);
        if (mGoogleMap != null) {
            if (deviceMap.get(model.getImei()) != null) {
                Objects.requireNonNull(deviceMap.get(model.getImei())).setPosition(deviceLatLng);
                Objects.requireNonNull(deviceMap.get(model.getImei())).setIcon(getMarkerIcon(model, deviceLatLng, state));
            } else {
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(deviceLatLng);
                markerOption.title(model.getImei());
                markerOption.icon(getMarkerIcon(model, deviceLatLng, state));
                markerOption.anchor(0.5f, 1);
                markerOption.draggable(false);
                markerOption.visible(true);
                Marker marker = mGoogleMap.addMarker(markerOption);
                deviceMap.put(model.getImei() + "", marker);
            }
        }

        if (selectCarBean != null) {
            if (selectCarBean.getImei().equals(model.getImei())) {
                selectCarBean = model;
                setDeviceDetailData();
            }
        }
    }

    /**
     * 在地图上绘制设备
     *
     * @param isInitiativeRefresh 是否主动刷新数据
     */
    private void onDeviceToMapShow(boolean isInitiativeRefresh) {
        mBuilder = null;
        mBuilder = new LatLngBounds.Builder();
        for (int i = 0; i < mDeviceModels.size(); i++) {
            DeviceModel model = mDeviceModels.get(i);
            if (model.getLat() == 0 && model.getLon() == 0) {
                continue;
            }
            double lat = (double) model.getLat() / 1000000;
            double lon = (double) model.getLon() / 1000000;

            // 更新选中设备信息
            if (selectCarBean != null) {
                if (selectCarBean.getImei().equals(model.getImei())) {
                    selectCarBean = model;
                    setDeviceDetailData();
                }
            }

            int state = getDeviceState(model);

            // 开始绘制设备marker
            LatLng deviceLatLng = new LatLng(lat, lon);
            if (mGoogleMap != null) {
                if (deviceMap.get(model.getImei()) != null) {
                    Objects.requireNonNull(deviceMap.get(model.getImei())).setPosition(deviceLatLng);
                    Objects.requireNonNull(deviceMap.get(model.getImei())).setIcon(getMarkerIcon(model, deviceLatLng, state));
                } else {
                    MarkerOptions markerOption = new MarkerOptions();
                    markerOption.position(deviceLatLng);
                    markerOption.title(model.getImei());
                    markerOption.icon(getMarkerIcon(model, deviceLatLng, state));
                    markerOption.anchor(0.5f, 1);
                    markerOption.draggable(false);
                    markerOption.visible(true);
                    Marker marker = mGoogleMap.addMarker(markerOption);
                    deviceMap.put(model.getImei() + "", marker);
                }
                mBuilder.include(deviceLatLng);
            }
        }
        //选中的设备设为中心
        if (selectCarBean != null) {
            centerPoint = new LatLng((double) selectCarBean.getLat() / 1000000, (double) selectCarBean.getLon() / 1000000);
            updateMapCenter(selectCarBean.getImei());
        }
        if (isInitiativeRefresh) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mDeviceModels.size() > 0) {
                        if (mGoogleMap != null && selectCarBean == null) {
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBuilder.build(), 200));
                            mZoom = mGoogleMap.getCameraPosition().zoom;
                        }
                    }
                }
            }, 500);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLocation(false);
            }
        }, 500);
    }

    /**
     * 绘制设备图标等信息
     *
     * @param theDevice
     * @return
     */
    private BitmapDescriptor getMarkerIcon(DeviceModel theDevice, LatLng deviceLocation, int state) {
        View view = View.inflate(getContext(), R.layout.layout_marker_item, null);
        TextView tvName = view.findViewById(R.id.tv_name);
        ImageView ivCar = view.findViewById(R.id.iv_car);
        String strShow = "";
        if (!TextUtils.isEmpty(theDevice.getDevice_name())) {
            strShow = theDevice.getDevice_name();
        } else {
            if (theDevice.getImei().length() > 5) {
                strShow = theDevice.getImei().substring(theDevice.getImei().length() - 5);
            } else {
                strShow = theDevice.getImei();
            }
        }
        if (mLatitude != 0 && mLongitude != 0) {
            //计算p1、p2两点之间的直线距离，单位：米
            double distance = GoogleMapUtils.toRadiusMeters(new LatLng(mLatitude, mLongitude), deviceLocation);
            strShow = strShow + DeviceUtils.getDeviceDistance(getContext(), distance);
        }
        tvName.setTextColor(getResources().getColor(DeviceUtils.getDeviceNameColor(theDevice.getPower())));
        tvName.setText(strShow);

        getDeviceState(state, ivCar);
        return convertViewToBitmap(view);
    }

    private static BitmapDescriptor convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return BitmapDescriptorFactory.fromBitmap(view.getDrawingCache());
    }

    /**
     * 绘制marker图标
     *
     * @param state  设备状态，0：静止，1：在线，2：离线
     * @param ivView
     */
    private void getDeviceState(int state, ImageView ivView) {
        switch (state) {
            case 0:
                ivView.setImageResource(R.drawable.icon_device_line_sleep);
                break;
            case 1:
                ivView.setImageResource(R.drawable.icon_device_line_on);
                break;
            case 2:
                ivView.setImageResource(R.drawable.icon_device_off);
                break;
        }
    }

    /**
     * 更新悬浮框数据
     */
    @SuppressLint({"SetTextI18n", "DefaultLocale", "SimpleDateFormat"})
    private void setDeviceDetailData() {
        if (selectCarBean == null) {
            return;
        }
        selectLatForDevice = (double) selectCarBean.getLat() / 1000000;
        selectLonForDevice = (double) selectCarBean.getLon() / 1000000;
        double speed = (double) selectCarBean.getSpeed() / 10;

        tvStaticTimeHint.setVisibility(View.INVISIBLE);
        tvStaticTime.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(selectCarBean.getDev_state())) {
            String strFormat = DeviceUtils.onLocationType(selectCarBean.getDev_state());
            char a7 = strFormat.charAt(0); // 定位方式
            char a6 = strFormat.charAt(1); // 设备状态
            char a5 = strFormat.charAt(2); // Galileo卫星定位
            char a4 = strFormat.charAt(3); // 北斗卫星定位
            char a3 = strFormat.charAt(4); // GPS卫星定位
            char a2 = strFormat.charAt(5); // 定位类型
            char a0 = strFormat.charAt(7); // 是否定位

            if ("1".equals(String.valueOf(a7))) {
                ivSignal.setImageResource(R.drawable.icon_base_station);
            } else {
                ivSignal.setImageResource(R.drawable.icon_gps);
            }
            if ("1".equals(String.valueOf(a6))) {
                tvState.setText(getString(R.string.state_line_on));
                tvState.setTextColor(mContext.getResources().getColor(R.color.color_2ABA5A));
                tvStaticTimeHint.setText(getString(R.string.online_time));
            } else {
                tvState.setText(getString(R.string.state_line_static));
                tvState.setTextColor(mContext.getResources().getColor(R.color.color_FF2F25));
                tvStaticTimeHint.setText(getString(R.string.static_time));
                tvStaticTimeHint.setVisibility(View.VISIBLE);
                tvStaticTime.setVisibility(View.VISIBLE);
            }

            tvBdNumber.setText(selectCarBean.getGps_satellite() + "");
            if ("1".equals(String.valueOf(a5))) {
                ivGalileoLocation.setVisibility(View.VISIBLE);
            } else {
                ivGalileoLocation.setVisibility(View.GONE);
            }
            if ("1".equals(String.valueOf(a4))) {
                tvBdLocation.setVisibility(View.VISIBLE);
            } else {
                tvBdLocation.setVisibility(View.GONE);
            }
            if ("1".equals(String.valueOf(a3))) {
                tvGpsLocation.setVisibility(View.VISIBLE);
            } else {
                tvGpsLocation.setVisibility(View.GONE);
            }
        } else {
            ivGalileoLocation.setVisibility(View.GONE);
            tvBdLocation.setVisibility(View.GONE);
            tvGpsLocation.setVisibility(View.VISIBLE);
            tvBdNumber.setText("1");
            tvState.setText(getString(R.string.state_line_down));
            tvState.setTextColor(getResources().getColor(R.color.color_999999));
            tvStaticTimeHint.setVisibility(View.VISIBLE);
            tvStaticTime.setVisibility(View.VISIBLE);
            tvStaticTimeHint.setText(getString(R.string.off_time));
        }

        if (!TextUtils.isEmpty(selectCarBean.getDevice_name())) {
            tvDeviceName.setText(selectCarBean.getDevice_name());
        } else {
            tvDeviceName.setText(selectCarBean.getImei());
        }
        tvSpeed.setText(speed + "km/h");

        if (DeviceUtils.isDeviceOnOff(selectCarBean.getCommunication_time())) {
            tvState.setText(getString(R.string.state_line_down));
            tvState.setTextColor(getResources().getColor(R.color.color_999999));
            tvStaticTimeHint.setText(getString(R.string.off_time));
            tvStaticTimeHint.setVisibility(View.VISIBLE);
            tvStaticTime.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(selectCarBean.getCommunication_time())) {
            tvStaticTime.setText(DeviceUtils.onDeviceOnOffTime(getActivity(), selectCarBean.getCommunication_time()));
        } else {
            tvStaticTime.setText(DeviceUtils.onDeviceOnOffTime(getActivity(), selectCarBean.getTime()));
        }
        if (!TextUtils.isEmpty(selectCarBean.getCommunication_time())) {
            tvCommunicationTime.setText(selectCarBean.getCommunication_time());
        } else {
            tvCommunicationTime.setText("");
        }

        onShowRealTimeTrackSwitch(selectCarBean.getImei());
        DeviceUtils.setLocationSignalData(selectCarBean.getSignal_rate(), ivGsm, tvGsmNumber);
        DeviceUtils.setElectricImageData(selectCarBean.getPower(), ivBattery, tvBattery);
        tvLocationTime.setText(selectCarBean.getTime());
        AddressParseUtil.getGoogleAddress(getContext(), tvAddress, selectLatForDevice, selectLonForDevice, "");
    }

    // ---------------------------------------------- 分隔符：初始化蓝牙 -------------------------------------------------------

    /**
     * 初始化蓝牙
     */
    private void initViewBluetooth() {
        mBluetoothManagerUtil = BluetoothManagerUtil.getInstance();
        mBluetoothManagerUtil.initBluetooth();
        mBluetoothManagerUtil.setBluetoothChange(new BluetoothManagerUtil.onBluetoothChange() {
            @Override
            public void onConnectBleDeviceState(BluetoothInfoBean bluetoothInfoBean) {
                if (getActivity() == null) {
                    return;
                }
                onShowBluetoothInfo(bluetoothInfoBean);
            }

            @Override
            public void onLocationDataParse(DeviceModel deviceModel) {
                if (getActivity() == null) {
                    return;
                }
                for (DeviceModel bean : mDeviceModels) {
                    if (bean.getImei().equals(deviceModel.getImei())) {
                        bean.setImei(deviceModel.getImei());
                        bean.setAlarm_type(deviceModel.getAlarm_type());
                        bean.setLat(deviceModel.getLat());
                        bean.setLon(deviceModel.getLon());
                        bean.setDev_state(deviceModel.getDev_state());
                        bean.setSpeed(deviceModel.getSpeed());
                        bean.setTime(deviceModel.getTime());
                        bean.setSignal_rate(deviceModel.getSignal_rate());
                        bean.setGps_satellite(deviceModel.getGps_satellite());
                        bean.setPower(deviceModel.getPower());
                        bean.setCommunication_time(deviceModel.getCommunication_time());
                        break;
                    }
                }
                if (selectCarBean != null) {
                    if (selectCarBean.getImei().equals(deviceModel.getImei())) {
                        selectCarBean.setImei(deviceModel.getImei());
                        selectCarBean.setAlarm_type(deviceModel.getAlarm_type());
                        selectCarBean.setLat(deviceModel.getLat());
                        selectCarBean.setLon(deviceModel.getLon());
                        selectCarBean.setDev_state(deviceModel.getDev_state());
                        selectCarBean.setSpeed(deviceModel.getSpeed());
                        selectCarBean.setTime(deviceModel.getTime());
                        selectCarBean.setSignal_rate(deviceModel.getSignal_rate());
                        selectCarBean.setGps_satellite(deviceModel.getGps_satellite());
                        selectCarBean.setPower(deviceModel.getPower());
                        selectCarBean.setCommunication_time(deviceModel.getCommunication_time());
                        onRefreshDeviceInfo(selectCarBean);
                    }
                }

                setLocationLatAndLon(0, deviceModel.getImei(),
                        DeviceUtils.getGoogleLatLng(deviceModel.getLat(), deviceModel.getLon()), false);
                onShowPolyline(0);
            }

        });

        mBluetoothManagerUtil.onShowBluetoothInfo();
    }

    /**
     * 显示连接蓝牙的信息
     */
    @SuppressLint("SetTextI18n")
    private void onShowBluetoothInfo(BluetoothInfoBean bluetoothInfoBean) {
        if (bluetoothInfoBean != null) {
            if (tvConnected != null && tvHandheldBattery != null) {
                tvConnected.setText(getString(R.string.connected));
                MyApplication.getMyApp().setHandheld_connected(getString(R.string.connected));
                tvHandheldBattery.setText(getString(R.string.handheld_battery) +
                        bluetoothInfoBean.getPower() + "%");
                MyApplication.getMyApp().setHandheld_battery(getString(R.string.handheld_battery) +
                        bluetoothInfoBean.getPower() + "%");
                ivBluetooth.setImageResource(R.drawable.icon_bluetooth);
            }

        } else {
            if(tvConnected != null && tvHandheldBattery !=null) {
                tvConnected.setText(getString(R.string.not_connected));
                tvHandheldBattery.setText(getString(R.string.handheld_battery));
                MyApplication.getMyApp().setHandheld_battery(getString(R.string.handheld_battery));
                MyApplication.getMyApp().setHandheld_connected(getString(R.string.not_connected));
                ivBluetooth.setImageResource(R.drawable.icon_bluetooth_disconnect);
            }
        }
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_LOCATION) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        onPermissionGranted(permissions[i]);
                    }
                }
            }
        }
    }

    private void checkPermissions(boolean isShowToast) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            if (isShowToast) {
                ToastUtils.show(getString(R.string.please_open_blue));
            }
            return;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(getActivity(), deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void onPermissionGranted(String permission) {
        if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.tip)
                        .setMessage(R.string.gpsNotifyMsg)
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                        .setPositiveButton(R.string.setting,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                    }
                                })

                        .setCancelable(false)
                        .show();
            } else {
                isBluetoothOpen = true;
                mBluetoothManagerUtil.startBluetoothService();
            }
        }
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // ------------------------------- 上报设备运行日志(存储，上传) ----------------------------------

    /**
     * 存储文件到本地
     */
    private void onWriterDeviceLogToSDCard(String device_number, String content, String time) {
        String logPath = DeviceLogUtil.getInstance().onWriterContentToSDCard(device_number, content, time);
        uploadDeviceLogFile(logPath, device_number, content);
    }

    // ------------------------------- 实时接收日志，有网的情况下实时上报删除日志文件 -------------------------------------------

    /**
     * 上传日志文件
     *
     * @param path          文件地址
     * @param device_number 设备名称
     */
    private void uploadDeviceLogFile(String path, String device_number, String content) {
        if (Utils.isNetworkAvailable(getActivity())) {
            mFile = new File(path);
            if (mFile.exists()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        onUploadDeviceLog(mDeviceLogUrl, mFile.getAbsolutePath(), content.getBytes(), device_number);
                    }
                }).start();
            }
        }
    }


    // ------------------------------- 每次进入首页，遍历一遍本地文件夹是否有未上传的日志文件 ------------------------------------

    /**
     * 遍历文件夹下的文件地址，获取文件地址，上传文件
     */
    private void getDeviceLogFileToSDCard() {
        // 文件夹地址
        mFileList.clear();
        File file = new File(mDeviceLogPath);
        List<File> files = FileUtilApp.getFile(file); // 查询文件夹下的文件
        if (files != null) {
            mFileList.addAll(files);
        }

        mFile = null;
        uploadDeviceLogFileForSDCard();
    }

    /**
     * 开始上传文件
     */
    private void uploadDeviceLogFileForSDCard() {
        if (mFileList.size() > 0) {
            mFile = mFileList.get(0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String content = onReadTxtFile(mFile.getAbsolutePath());
                    onUploadDeviceLog(mDeviceLogUrl, mFile.getAbsolutePath(), content.getBytes(), "");
                }
            }).start();
        }
    }

    /**
     * 删除日志，开启下一个日志上传
     *
     * @param isDelete
     */
    private void onDeleteDeviceLog(boolean isDelete) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDelete) {
                    if (mFile != null) {
                        mFileList.remove(mFile);
                        if (mFile.exists()) {
                            mFile.delete();
                        }
                        mFile = null;
                    }
                }
                uploadDeviceLogFileForSDCard();
            }
        }, 100);
    }

    /**
     * 设备运行日志上传
     *
     * @param uploadUrl 上传地址
     * @param fileName  文件路径
     * @param bytesData 文件内容
     */
    private void onUploadDeviceLog(String uploadUrl, String fileName, byte[] bytesData, String device_number) {
        try {
            final String newLine = "\r\n"; // 换行符
            final String boundaryPrefix = "--"; //边界前缀
            // 定义数据分隔线
            final String boundary = String.format("=========%s", System.currentTimeMillis());
            URL url = new URL(uploadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            String keyValue = "Content-Disposition: form-data; name=\"%s\"\r\n\r\n%s\r\n";
            byte[] parameterLine = (boundaryPrefix + boundary + newLine).getBytes();

            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(boundary);
            sb.append(newLine);
            // 文件参数
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"" + newLine);
            sb.append("Content-Type:text/plain");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);
            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
//            InputStream inputStream = FormatTools.getInstance().Byte2InputStream(bytesData);
//            // 数据输入流,用于读取文件数据
//            DataInputStream in = new DataInputStream(inputStream);
//            byte[] bufferOut = new byte[1024];
//            int bytes = 0;
//            // 每次读1KB数据,并且将文件数据写入到输出流中
//            while ((bytes = in.read(bufferOut)) != -1) {
//                out.write(bufferOut, 0, bytes);
//            }
            out.write(bytesData);
            // 最后添加换行
            out.write(newLine.getBytes());
//            in.close();
            // 定义最后数据分隔线，即--加上boundary再加上--。
            byte[] end_data = (newLine + boundaryPrefix + boundary + boundaryPrefix + newLine).getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();

            // 定义BufferedReader输入流来读取URL的响应
            StringBuffer sbOutPut = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sbOutPut.append(line).append(newLine);
            }
//            LogUtils.e(sbOutPut.toString());
            if (TextUtils.isEmpty(device_number)) {
                onDeleteDeviceLog(true);
            } else {
                onDeleteDeviceLog(device_number, true);
            }
        } catch (Exception e) {
            if (TextUtils.isEmpty(device_number)) {
                onDeleteDeviceLog(false);
            } else {
                onDeleteDeviceLog(device_number, false);
            }
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
    }

    /**
     * 删除日志文件
     *
     * @param device_number  设备名称
     * @param isDeleteSDCard 是否删除sd卡的文件
     */
    private void onDeleteDeviceLog(String device_number, boolean isDeleteSDCard) {
        if (isDeleteSDCard) {
            if (mFile.exists()) {
                mFile.delete();
            }
        }
        mFile = null;
        DeviceLogBean logBean = null; // 待解析日志数据
        for (DeviceLogBean bean : mDeviceLogBeans) {
            if (bean.getDevice_number().equals(device_number)) {
                logBean = bean;
                break;
            }
        }
        if (logBean != null) {
            mDeviceLogBeans.remove(logBean);
        }
    }

    /**
     * 读取文本文件中的内容
     *
     * @param strFilePath 文件路径
     * @return
     */
    @SuppressLint("LogNotTimber")
    public static String onReadTxtFile(String strFilePath) {
        StringBuilder content = new StringBuilder(); //文件内容字符串
        //打开文件
        File file = new File(strFilePath);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "文件不存在或被删除");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                instream.close();
            } catch (FileNotFoundException e) {
                Log.d("TestFile", "文件不存在或被删除");
            } catch (IOException e) {
                Log.d("TestFile", Objects.requireNonNull(e.getMessage()));
            }
        }
        return content.toString();
    }


    // --------------------------------------- 实时轨迹 --------------------------------------

    /**
     * 初始化实时轨迹实体类
     */
    private void initRealTimeTrackData() {
        mTrackBeans.clear();
        if (mTrackSwitchBeans.size() > 0) {
            for (RealTimeTrackSwitchBean bean : mTrackSwitchBeans) {
                RealTimeTrackGoogleBean trackBean = new RealTimeTrackGoogleBean();
                trackBean.setImei(bean.getImei());
                trackBean.setRealTimeTrack(bean.isRealTimeTrack());
                mTrackBeans.add(trackBean);
            }
        }
        if (mDeviceModels.size() > 0 && mTrackBeans.size() > 0) {
            onRealTimeTrackColorId(0);
            onAddLocationLatAndLon(0);
        }
    }

    /**
     * 设置对应实时轨迹的设备的轨迹线颜色
     */
    private void onRealTimeTrackColorId(int position) {
        if (position < mDeviceModels.size() && mTrackBeans.size() > 0) {
            setDeviceColorId(position, mDeviceModels.get(position).getImei(), true);
        }
    }

    /**
     * 单个设备设置轨迹线的颜色
     *
     * @param imei
     */
    private void onScreenDeviceColorId(String imei) {
        for (int i = 0; i < mDeviceModels.size(); i++) {
            if (imei.equals(mDeviceModels.get(i).getImei())) {
                setDeviceColorId(i, mDeviceModels.get(i).getImei(), false);
                break;
            }
        }
    }

    /**
     * 设置轨迹线的颜色
     *
     * @param position
     * @param imei
     */
    private void setDeviceColorId(int position, String imei, boolean isNext) {
        int index = 0;
        if (position < mColorLists.size()) {
            index = position;
        } else {
            double a = (double) (position / mColorLists.size());
            if (a >= 1) {
                index = position - (((int) a) * mColorLists.size());
            }
        }
        if (index < 0) {
            index = 0;
        } else if (index > mColorLists.size()) {
            index = mColorLists.size() - 1;
        }
        for (RealTimeTrackGoogleBean bean : mTrackBeans) {
            if (bean.getImei().equals(imei)) {
                bean.setColorId(ColorUtil.colorIdToRGBA(mColorLists.get(index).getColor()));
                break;
            }
        }

        if (isNext) {
            position++;
            onRealTimeTrackColorId(position);
        }
    }

    /**
     * 添加轨迹线的下一个点坐标
     *
     * @param position
     */
    private void onAddLocationLatAndLon(int position) {
        if (mDeviceModels != null && position < mDeviceModels.size()) {
            setLocationLatAndLon(position, mDeviceModels.get(position).getImei(),
                    DeviceUtils.getGoogleLatLng(mDeviceModels.get(position).getLat(), mDeviceModels.get(position).getLon()), true);
        }
    }

    /**
     * 添加轨迹线的下一个点坐标
     *
     * @param latLng
     * @param isAllAdd 是否所有设备添加下一个点坐标，false：添加单个设备的下一个点坐标
     */
    private void setLocationLatAndLon(int position, String imei, LatLng latLng, boolean isAllAdd) {
        LatLng lastLat = null;
        if (mTrackBeans != null) {
            if (mTrackBeans.size() > 0) {
                RealTimeTrackGoogleBean bean = null;
                for (RealTimeTrackGoogleBean trackBean : mTrackBeans) {
                    if (trackBean.getImei().equals(imei)) {
                        bean = trackBean;
                    }
                }
                if (bean != null && bean.getFollowLatLists() != null) {
                    lastLat = bean.getFollowLatLists().get(bean.getFollowLatLists().size() - 1);
                }
            }
            for (RealTimeTrackGoogleBean bean : mTrackBeans) {
                if (bean.getImei().equals(imei)) {
                    if (lastLat == null) {
                        bean.setFollowLatAndLon(latLng);
                    } else {
                        if (lastLat.latitude == latLng.latitude && lastLat.longitude == latLng.longitude) {
                            LogUtils.e(imei + ":经纬度与上一个点相同");
                        } else {
                            bean.setFollowLatAndLon(latLng);
                        }
                    }
                    break;
                }
            }

            if (isAllAdd) {
                position++;
                onAddLocationLatAndLon(position);
            }
        }
    }

    /**
     * 打开 or 关闭实时追踪
     */
    private void onOpenSwitchRealTimeTrack() {
        if (selectCarBean != null) {
            RealTimeTrackSwitchBean switchBean = null;
            for (RealTimeTrackSwitchBean bean : mTrackSwitchBeans) {
                if (bean.getImei().equals(selectCarBean.getImei())) {
                    switchBean = bean;
                    break;
                }
            }
            if (switchBean == null) {
                RealTimeTrackSwitchBean bean = new RealTimeTrackSwitchBean();
                bean.setImei(selectCarBean.getImei());
                bean.setRealTimeTrack(true);
                mTrackSwitchBeans.add(bean);

                RealTimeTrackGoogleBean trackBean = new RealTimeTrackGoogleBean();
                trackBean.setImei(selectCarBean.getImei());
                trackBean.setRealTimeTrack(true);
                trackBean.setFollowLatAndLon(DeviceUtils.getGoogleLatLng(selectCarBean.getLat(), selectCarBean.getLon()));
                mTrackBeans.add(trackBean);

                onScreenDeviceColorId(selectCarBean.getImei());
                ivRealTimeTrack.setImageResource(R.drawable.icon_real_track_on);
            } else {
                mTrackSwitchBeans.remove(switchBean);
                RealTimeTrackGoogleBean bean = null;
                for (RealTimeTrackGoogleBean trackBean : mTrackBeans) {
                    if (trackBean.getImei().equals(selectCarBean.getImei())) {
                        bean = trackBean;
                        break;
                    }
                }
                if (bean != null) {
                    if (bean.getPolyline() != null) {
                        bean.getPolyline().remove();
                        bean.setPolyline(null);
                    }
                    bean.clearFollowLatAndLon();

                    for (RealTimeTrackGoogleBean trackBean : mTrackBeans) {
                        if (trackBean.getImei().equals(bean.getImei())) {
                            trackBean.clearFollowLatAndLon();
                            trackBean.setPolyline(null);
                            break;
                        }
                    }

                    mTrackBeans.remove(bean);
                    ivRealTimeTrack.setImageResource(R.drawable.icon_real_track_off);
                }
            }
            MyApplication.getMyApp().setTrackSwitchBeans(mTrackSwitchBeans);
        }
    }

    /**
     * 显示轨迹线
     */
    private void onShowPolyline(int position) {
        if (position < mTrackBeans.size()) {
            if (mTrackBeans.get(position).getFollowLatLists().size() >= 2) {
                if (mTrackBeans.get(position).getPolyline() == null) {
                    mTrackBeans.get(position).setPolyline(mGoogleMap.addPolyline(new PolylineOptions()
                            .addAll(mTrackBeans.get(position).getFollowLatLists())
                            .width(5)
                            .color(mTrackBeans.get(position).getColorId())));
//                    mTrackBeans.get(position).getPolyline().setZIndex(9999);
                } else {
                    mTrackBeans.get(position).getPolyline().setPoints(mTrackBeans.get(position).getFollowLatLists());
                }
            }

            position++;
            finalPosition = position;
            if (mTimerHandler != null) {
                mTimerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onShowPolyline(finalPosition);
                    }
                }, 10);
            }
        }
    }

    /**
     * 显示对应设备的实时轨迹开关
     *
     * @param imei
     */
    private void onShowRealTimeTrackSwitch(String imei) {
        boolean isHas = false;
        for (RealTimeTrackGoogleBean bean : mTrackBeans) {
            if (bean.getImei().equals(imei)) {
                isHas = true;
                break;
            }
        }
        ivRealTimeTrack.setImageResource(isHas ? R.drawable.icon_real_track_on : R.drawable.icon_real_track_off);
    }

}
