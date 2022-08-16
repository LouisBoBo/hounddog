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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.di.component.DaggerAmapLocationComponent;
import com.slxk.hounddog.mvp.contract.AmapLocationContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.model.bean.BaseStationAddressBean;
import com.slxk.hounddog.mvp.model.bean.BluetoothInfoBean;
import com.slxk.hounddog.mvp.model.bean.ColorBean;
import com.slxk.hounddog.mvp.model.bean.DeviceConfigResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceDetailResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListForManagerResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceLocationInfoBean;
import com.slxk.hounddog.mvp.model.bean.DeviceLogBean;
import com.slxk.hounddog.mvp.model.bean.FunctionBean;
import com.slxk.hounddog.mvp.model.bean.MapOtherBean;
import com.slxk.hounddog.mvp.model.bean.MapSatelliteBean;
import com.slxk.hounddog.mvp.model.bean.MapTypeBean;
import com.slxk.hounddog.mvp.model.bean.RealTimeTrackBean;
import com.slxk.hounddog.mvp.model.bean.RealTimeTrackSwitchBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceConfigPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceDetailPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListForManagerPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListPutBean;
import com.slxk.hounddog.mvp.model.putbean.OnKeyFunctionPutBean;
import com.slxk.hounddog.mvp.presenter.AmapLocationPresenter;
import com.slxk.hounddog.mvp.ui.activity.ConnectDeviceActivity;
import com.slxk.hounddog.mvp.ui.activity.DeviceDetailActivity;
import com.slxk.hounddog.mvp.ui.activity.FunctionMoreActivity;
import com.slxk.hounddog.mvp.ui.activity.LocationModeActivity;
import com.slxk.hounddog.mvp.ui.activity.MainActivity;
import com.slxk.hounddog.mvp.ui.activity.MainWirelessActivity;
import com.slxk.hounddog.mvp.ui.activity.ModifyNameActivity;
import com.slxk.hounddog.mvp.ui.activity.NavigationAmapActivity;
import com.slxk.hounddog.mvp.ui.activity.OfflineMapActivity;
import com.slxk.hounddog.mvp.ui.activity.RecordActivity;
import com.slxk.hounddog.mvp.ui.activity.TrackAmapActivity;
import com.slxk.hounddog.mvp.ui.adapter.FunctionAdapter;
import com.slxk.hounddog.mvp.ui.adapter.MapOtherAdapter;
import com.slxk.hounddog.mvp.ui.adapter.MapSatelliteAdapter;
import com.slxk.hounddog.mvp.ui.adapter.MapTypeAdapter;
import com.slxk.hounddog.mvp.utils.AddressParseUtil;
import com.slxk.hounddog.mvp.utils.BitmapHelperAmap;
import com.slxk.hounddog.mvp.utils.BluetoothManagerUtil;
import com.slxk.hounddog.mvp.utils.BroadcastReceiverUtil;
import com.slxk.hounddog.mvp.utils.ColorUtil;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.DateUtils;
import com.slxk.hounddog.mvp.utils.DensityUtil;
import com.slxk.hounddog.mvp.utils.DeviceLogUtil;
import com.slxk.hounddog.mvp.utils.DeviceUtils;
import com.slxk.hounddog.mvp.utils.FileUtilApp;
import com.slxk.hounddog.mvp.utils.GpsUtils;
import com.slxk.hounddog.mvp.utils.MapImageCache;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.AlertAppDialog;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
 * Description: 高德地图首页-在线模式
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class AmapLocationFragment extends BaseFragment<AmapLocationPresenter> implements AmapLocationContract.View,
        AMapLocationListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener, AMap.OnCameraChangeListener,
        AMap.InfoWindowAdapter {

    @BindView(R.id.mapView)
    MapView mapView;
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
    @BindView(R.id.tv_handheld_battery)
    TextView tvHandheldBattery;
    @BindView(R.id.tv_connected)
    TextView tvConnected;
    @BindView(R.id.tv_mobile_location_state)
    TextView tvMobileLocationState;
    @BindView(R.id.tv_other_hint)
    TextView tvOtherHint;
    @BindView(R.id.iv_bluetooth)
    ImageView ivBluetooth;
    @BindView(R.id.iv_check_device_top)
    ImageView ivCheckDeviceTop;
    @BindView(R.id.iv_check_user_top)
    ImageView ivCheckUserTop;

    private AMap mAMap;
    private LatLng centerPoint = new LatLng(39.90923, 116.397428);
    private float mZoom = 16;
    private HashMap<String, Marker> deviceMap; // 存储任务
    private LatLng mLatLngLocation; // 设备的经纬度
    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption;
    private MyLocationStyle myLocationStyle;
    private BitmapHelperAmap bitmapHelperAmap;
    private LatLngBounds.Builder mBuilder; // 所有设备可视化Builder
    private boolean isFirstData = true; // 是否是第一次加载数据

    private double mLatitude = 0.0; // 定位获取到的纬度
    private double mLongitude = 0.0; // 定位获取到的经度

    // 蓝牙相关
    private static final String TAG = "AmapLocationFragment";
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

    private ArrayList<FunctionBean> functionBeans; // 功能菜单
    private FunctionAdapter mAdapter;
    // 底部信息弹窗布局
    private BottomSheetBehavior<LinearLayout> mBehavior;

    private double selectLatForDevice; // 选中设备的纬度
    private double selectLonForDevice; // 选中设备的经度
    private DeviceModel selectCarBean; // 选中的设备
    private DeviceDetailResultBean selectDeviceDetailBean; // 选中的设备的详细信息

    private boolean isFirstLocation = true; // 是否是第一次定位，避免后续定位成功后，一直设置定位点为中心点
    private String mCompassImei = "00000000"; // 罗盘虚拟imei号，用来判断是否点击了罗盘

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

    // 瓦片地图
    // 判断当前使用高德地图，还是高德地图的谷歌瓦片地图
    private TileOverlay mtileOverlay;
    private int mapType = 0; // 地图类型,0：高德地图，1：百度地图，2：谷歌地图
    private boolean isChina = false; // 是否在中国
    private String ALBUM_PATH = FileUtilApp.File_Google_Map; // 存储路径
    private String mapUrl = Api.Map_2D; // 请求地图url地址

    // 设备日志上报操作
    private static String mDeviceLogPath = DeviceLogUtil.PATH;
    private ArrayList<File> mFileList; // 遍历文件夹下的文件
    private File mFile; // 当前正在上传的文件
    private ArrayList<DeviceLogBean> mDeviceLogBeans; // 设备日志临时存储
    private String mDeviceLogUrl = Api.Device_Log_Url; // 日志上传url

    private boolean isDeviceReset = false; // 是否有恢复出厂设置功能
    private int switchPosition = 0; // 当前设备所在索引位置

    // 实时轨迹
    private ArrayList<RealTimeTrackSwitchBean> mTrackSwitchBeans; // 实时轨迹开关状态
    private ArrayList<RealTimeTrackBean> mTrackBeans; // 实时轨迹
    private int finalPosition; // 循环实时轨迹显示的索引下标
    private int finalColorPosition; // 用来循环颜色池的索引下标
    private int finalLocationPosition; // 用来循环定位数据的索引下标
    // 设备颜色池
    private ArrayList<ColorBean> mColorLists; // 设备颜色池
    private Handler mTimerHandler; // 用于做延迟操作的
    private boolean isFirstSetColor = true; // 是否是首次设置颜色
    private Marker selectMarker; //选中的设备

    private Timer mTimer;//定时检测设备连接状态
    private TimerTask mTask;
    private boolean isChangePage = false;

    public static AmapLocationFragment newInstance() {
        AmapLocationFragment fragment = new AmapLocationFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerAmapLocationComponent //如找不到该类,请编译一下项�?
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_amap_location, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        functionBeans = new ArrayList<>();
        mDeviceModels = new ArrayList<>();
        mDeviceListBeans = new ArrayList<>();
        mDeviceForAccountBeans = new ArrayList<>();
        mDeviceGetBeans = new ArrayList<>();
        mAddDeviceBeans = new ArrayList<>();
        mDeleteDeviceBeans = new ArrayList<>();
        mHasDeviceBeans = new ArrayList<>();
        deviceMap = new HashMap<>();
        bitmapHelperAmap = new BitmapHelperAmap(getContext());
        mapType = ConstantValue.getMapType();
        isChina = ConstantValue.isInChina();
        mFileList = new ArrayList<>();
        mDeviceLogBeans = new ArrayList<>();
        isSatelliteMap = ConstantValue.isMapSatelliteMap();
        mTimerHandler = new Handler();

        try {
            mDeviceBeanDao = new MyDao(DeviceModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTrackSwitchBeans = new ArrayList<>();
        mTrackBeans = new ArrayList<>();
        mColorLists = new ArrayList<>();

        isFirstSetColor = true;
        mColorLists.addAll(DeviceUtils.getColorList(getContext()));
        if (MyApplication.getMyApp().getTrackSwitchBeans() != null) {
            mTrackSwitchBeans.addAll(MyApplication.getMyApp().getTrackSwitchBeans());
        }
        initRealTimeTrackData();

        getDeviceLogFileToSDCard();
        initMaps();
        getDeviceListForDB(false);
        onMapSatelliteData();
        onMapTypeData();
//        onMapOtherData();
        addFunctionData();
        onBottomSheetBehavior();
        initViewBluetooth();
        if (Utils.isNetworkAvailable(getActivity())) {
            onDeviceListForService(true, true);
        }
        checkPermissions(true);

        // 注册一个广播接收器，用于接收从首页跳转到报警消息页面的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("location_map");
        receiver = new ChangePageReceiver();
        //注册切换页面广播接收者
        LocalBroadcastManager.getInstance(MyApplication.getMyApp()).registerReceiver(receiver, filter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 向服务器获取设备列表
     */
    private void onDeviceListForService(boolean isShow, boolean isInitiativeRefresh) {
        // 判断是账号登录还是设备号登录，分开走不通的流程
        if (ConstantValue.isAccountLogin()) {
            getDeviceListForGroup(isShow, isInitiativeRefresh);
        } else {
            getDeviceList(isShow, isInitiativeRefresh);
        }
    }

    /**
     * 页面切换广播，广播接收器
     */
    private class ChangePageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                // 判断发送指令是做什么功能，0：切换页面，1：删除，添加设备，2：蓝牙列表操作，3：选中设备
                int intentType = intent.getIntExtra("intent_type", 0);
                if (intentType == 0) {
                    fragmentPosition = intent.getIntExtra("type", 0);
                    if (fragmentPosition == 0) {
                        isChangePage = true;
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
                        mCountDown = mCountDownTime;
                        onDeviceListForService(false, true);
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
            mDeviceModels.remove(mModel);
            mDeviceGetBeans.remove(mModel);
            mBluetoothManagerUtil.onDeviceDelete(mModel);
        }
    }

    /**
     * 从数据库查询设备列表数据
     *
     * @param isSynchronousData 是否同步数据给接收机
     */
    private void getDeviceListForDB(boolean isSynchronousData) {
        mDeviceModels.clear();
        if (getDeviceListDataBase() != null) {
            mDeviceModels.addAll(getDeviceListDataBase());
        }

        if (isSynchronousData) {
            mBluetoothManagerUtil.setAccountDeviceList(mDeviceModels);
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

    private void initMaps() {
        if (mAMap == null) {
            mAMap = mapView.getMap();
            mAMap.getUiSettings().setScaleControlsEnabled(true);
            mAMap.getUiSettings().setRotateGesturesEnabled(false);
            mAMap.getUiSettings().setZoomControlsEnabled(true);
            mAMap.getUiSettings().setMyLocationButtonEnabled(false);
            mAMap.setOnMarkerClickListener(this);
            mAMap.setOnMapClickListener(this);
            mAMap.setOnCameraChangeListener(this);
            mAMap.setInfoWindowAdapter(this);

            if (mapType == 2 && isChina) {
                mAMap.showMapText(false);
                //设置Logo下边界距离屏幕底部的边距,设置为负值即可
                mAMap.getUiSettings().setLogoBottomMargin(-150);
            }
            onShowMapType();

            mlocationClient = new AMapLocationClient(getContext());
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
            myLocationStyle.myLocationIcon(bitmapHelperAmap.getBitmapZoomForUserLocation(mZoom));
            myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
            myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
            // 设置定位监听
            mAMap.setMyLocationStyle(myLocationStyle);
            mAMap.setMyLocationEnabled(true);

            mZoom = 16;
            updateMapCenter("");
        }
    }

    /**
     * 地图中心点位置
     */
    private void updateMapCenter(String imei) {
        if (mAMap != null) {
            mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(centerPoint, mZoom, 0, 0)));
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
                tvMobileLocationState.setText(getString(R.string.mobile_gps_location));
                MyApplication.getMyApp().setLatitude(mLatitude);
                MyApplication.getMyApp().setLongitude(mLongitude);
                MyApplication.getMyApp().setMobile_location_state(getString(R.string.mobile_gps_location));

                SPUtils.getInstance().put(ConstantValue.Is_In_China, GpsUtils.isChinaLocation(mLatitude, mLongitude));

                // 定位成功之后，判断是否有设备，没有设备就跳转到手机定位位置
                if (isFirstLocation && mDeviceModels.size() == 0) {
                    centerPoint = new LatLng(mLatitude, mLongitude);
                    updateMapCenter("");
                }
                isFirstLocation = false;
                onShowCompass(mLatitude, mLongitude);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
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
        mZoom = cameraPosition.zoom;
        centerPoint = cameraPosition.target;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (selectCarBean != null) {
            selectCarBean = null;
            MyApplication.getMyApp().setDevice_imei("");
            setBottomSheetBehaviorHide();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String imei = marker.getTitle();
        onSelectDevice(imei);
        return true;
    }

    /**
     * 选中设备
     *
     * @param imei
     */
    private void onSelectDevice(String imei) {
        if (!TextUtils.isEmpty(imei) && !imei.equals(mCompassImei)) {
            selectCarBean = null;
            for (DeviceModel bean : mDeviceGetBeans) {
                if (imei.equals(bean.getImei())) {
                    selectCarBean = bean;
                    break;
                }
            }

            if (selectCarBean != null) {
                if (selectCarBean.getLat() == 0 && selectCarBean.getLon() == 0) {
                    ToastUtils.show(getString(R.string.device_not_has_location_data));
                    selectCarBean = null;
                    MyApplication.getMyApp().setDevice_imei("");
                    setBottomSheetBehaviorHide();
                } else {
                    getDeviceDetailInfo();
                    getDeviceConfig();
                    MyApplication.getMyApp().setDevice_imei(selectCarBean.getImei());
                    selectLatForDevice = (double) selectCarBean.getLat() / 1000000;
                    selectLonForDevice = (double) selectCarBean.getLon() / 1000000;
                    centerPoint = new LatLng(selectLatForDevice, selectLonForDevice);
                    updateMapCenter(selectCarBean.getImei());
                    setBottomSheetBehaviorExpanded();
                }
            }
        }
    }

    /**
     * 获取设备的配置信息，支持的功能等
     */
    private void getDeviceConfig() {
        DeviceConfigPutBean.ParamsBean paramsBean = new DeviceConfigPutBean.ParamsBean();
        if (!TextUtils.isEmpty(selectCarBean.getSimei())) {
            paramsBean.setSimei(selectCarBean.getSimei());
        }
        paramsBean.setType(ResultDataUtils.Config_Other);

        DeviceConfigPutBean bean = new DeviceConfigPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Config_Get);
        bean.setModule(ModuleValueService.Module_For_Config_Get);

        getPresenter().getDeviceConfig(bean);
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
        View view = View.inflate(getContext(), R.layout.layout_compass_marker, null);
        ImageView ivCompass = view.findViewById(R.id.iv_compass);
        ivCompass.setImageResource(Utils.isLocaleForCN() ? R.drawable.icon_compass_zh : R.drawable.icon_compass_es);
        return convertViewToBitmap(view);
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
        isSatelliteMap = id == 1;
        SPUtils.getInstance().put(ConstantValue.Is_Map_SatelliteMap, isSatelliteMap);
        onShowMapType();
    }

    /**
     * 显示地图类型
     */
    private void onShowMapType() {
        if (mapType == 2 && isChina) {
            //在线瓦片数据
            onModifyMapType(isSatelliteMap);
        } else {
            mAMap.setMapType(isSatelliteMap ? AMap.MAP_TYPE_SATELLITE : AMap.MAP_TYPE_NORMAL);
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
        isSwitchMapTraffic = isSwitch;
        mAMap.setTrafficEnabled(isSwitchMapTraffic);
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
                    launchActivity(TrackAmapActivity.newInstance(selectCarBean.getImei(), selectCarBean.getSimei(), selectCarBean.getDevice_name()));
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
                    launchActivity(FunctionMoreActivity.newInstance(selectCarBean.getImei(), selectCarBean.getSimei(), isDeviceReset));
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
        int state = 2; // 设备状态，0：静止，1：在线，2：离线
        if (!TextUtils.isEmpty(selectCarBean.getState())) {
            switch (selectCarBean.getState()) {
                case ResultDataUtils.Device_State_Line_Sleep:
                    state = 2;
                    break;
                case ResultDataUtils.Device_State_Line_On:
                    state = 1;
                    break;
                case ResultDataUtils.Device_State_Line_Down:
                    state = 0;
                    break;
            }
        }
        launchActivity(NavigationAmapActivity.newInstance(selectCarBean.getLat(), selectCarBean.getLon(), state));
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
    private void getDeviceList(boolean isShow, boolean isInitiativeRefresh) {
        DeviceListPutBean.ParamsBean paramsBean = new DeviceListPutBean.ParamsBean();
        paramsBean.setLimit_size(mDeviceLimitSize);

        DeviceListPutBean bean = new DeviceListPutBean();
        bean.setFunc(ModuleValueService.Fuc_For_Device_List);
        bean.setModule(ModuleValueService.Module_For_Device_List);
        bean.setParams(paramsBean);

        if (isShow) {
            showProgressDialog();
        }

        getPresenter().getDeviceList(bean, isInitiativeRefresh);
    }

    /**
     * 获取分组下的设备列表 - 账号登录
     */
    private void getDeviceListForGroup(boolean isShow, boolean isInitiativeRefresh) {
        DeviceListForManagerPutBean.ParamsBean paramsBean = new DeviceListForManagerPutBean.ParamsBean();
        paramsBean.setLimit_size(mGroupDeviceLimitSize);
        paramsBean.setFamilyid(ConstantValue.getFamilySid());

        DeviceListForManagerPutBean bean = new DeviceListForManagerPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Device_List_For_Group);
        bean.setModule(ModuleValueService.Module_For_Device_List_For_Group);

        if (isShow) {
            showProgressDialog();
        }

        getPresenter().getDeviceListForGroup(bean, isInitiativeRefresh);
    }

    /**
     * 获取设备详细信息
     */
    private void getDeviceDetailInfo() {
        if (selectCarBean != null) {
            selectDeviceDetailBean = null;

            DeviceDetailPutBean.ParamsBean paramsBean = new DeviceDetailPutBean.ParamsBean();
            paramsBean.setSimei(selectCarBean.getSimei());
            paramsBean.setGet_user(true);

            DeviceDetailPutBean bean = new DeviceDetailPutBean();
            bean.setFunc(ModuleValueService.Fuc_For_Device_Detail);
            bean.setModule(ModuleValueService.Module_For_Device_Detail);
            bean.setParams(paramsBean);

            getPresenter().getDeviceDetailInfo(bean);
        }
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
                if (mlocationClient != null) {
                    mlocationClient.startLocation();
                }
                onDeviceListForService(false, false);
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
        mapView.onResume();
        if (fragmentPosition == 0 && mapView != null) {
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
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
        mapView.onPause();
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
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
    @OnClick({R.id.ll_map_check, R.id.iv_check_user, R.id.iv_check_device, R.id.btn_record, R.id.iv_real_time_track, R.id.ll_download,
            R.id.ll_bluetooth, R.id.iv_check_user_top, R.id.iv_check_device_top})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()) {
            switch (view.getId()) {
                case R.id.ll_map_check:
                    onResetMapTypeData();
                    drawerlayout.openDrawer(END);
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
                case R.id.ll_download:
                    launchActivity(OfflineMapActivity.newInstance());
                    break;
                case R.id.ll_bluetooth:
                    launchActivity(ConnectDeviceActivity.newInstance());
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
            if (bean.getImei().equals(selectCarBean.getImei())) {
                bean.setDevice_name(name);
                break;
            }
        }
        for (DeviceModel bean : mDeviceGetBeans) {
            if (selectCarBean.getImei().equals(bean.getImei())) {
                bean.setDevice_name(name);
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
        if (switchPosition > mDeviceGetBeans.size() - 1) {
            switchPosition = 0;
        }
        if (mDeviceGetBeans.size() > 0) {
//            onSelectDevice(mDeviceGetBeans.get(switchPosition).getImei());

            DeviceModel model = mDeviceGetBeans.get(switchPosition);
            String imei = model.getImei();
            if (model.getLat() == 0 && model.getLon() == 0) {
                onCheckNextDevice();
            } else {
                selectLatForDevice = (double) model.getLat() / 1000000;
                selectLonForDevice = (double) model.getLon() / 1000000;
                centerPoint = new LatLng(selectLatForDevice, selectLonForDevice);
                if (!TextUtils.isEmpty(imei))
                    updateMapCenter(imei);

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
        for (DeviceModel model : mDeviceGetBeans) {
            if (model.getLat() != 0 && model.getLon() != 0) {
                isHas = true;
                break;
            }
        }
        return isHas;
    }

    @Override
    public void getDeviceListSuccess(DeviceListResultBean deviceListResultBean, boolean isInitiativeRefresh) {
        mCountDown = mCountDownTime;
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
                model.setState(itemsBean.getState());
                model.setLocation_type(bean.getType());
                mDeviceGetBeans.add(model);
            }

            //如果是切换页面则同步数据库数据
            if (isInitiativeRefresh || isChangePage) {
                isChangePage = false;
                onDeviceFilterForDB();
            }
        }
        if (isFirstSetColor) {
            onRealTimeTrackColorId(0);
        }
        if (isInitiativeRefresh) {
            onResetValue();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDeviceListForDB(true);
                }
            }, 1000);
        }
        isFirstData = false;
        isFirstSetColor = false;

        onAddLocationLatAndLon(0);
        onDeviceToMapShow(isInitiativeRefresh);

        mTimerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onShowGoogleMap();
                onShowPolyline(0);
            }
        }, 550);
    }

    /**
     * 显示谷歌瓦片地图
     */
    private void onShowGoogleMap() {
        if (mapType == 2 && isChina) {
            //在线瓦片数据
            useOMCMap();
        }
    }

    @Override
    public void onDismissProgress() {
        isFirstData = false;
        dismissProgressDialog();
    }

    @Override
    public void getDeviceListForGroupSuccess(DeviceListForManagerResultBean deviceListForManagerResultBean, boolean isInitiativeRefresh) {
        mCountDown = mCountDownTime;
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
                model.setState(itemsBean.getState());
                model.setLocation_type(bean.getType());
                mDeviceGetBeans.add(model);
            }

            //如果是切换页面则同步数据库数据
            if (isInitiativeRefresh || isChangePage) {
                isChangePage = false;
                onDeviceFilterForDB();
            }
        }
        if (isFirstSetColor) {
            onRealTimeTrackColorId(0);
        }
        if (isInitiativeRefresh) {
            onResetValue();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDeviceListForDB(true);
                }
            }, 1000);
        }
        isFirstData = false;
        isFirstSetColor = false;

        onAddLocationLatAndLon(0);
        onDeviceToMapShow(isInitiativeRefresh);

        mTimerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onShowGoogleMap();
                onShowPolyline(0);
            }
        }, 550);
    }

    @Override
    public void getDeviceDetailError() {
        isFirstData = false;
        mCountDown = mCountDownTime;
        onDeviceListForService(true, true);
    }

    @Override
    public void getDeviceDetailInfoSuccess(DeviceDetailResultBean deviceDetailResultBean) {
        if(selectCarBean != null && !TextUtils.isEmpty(deviceDetailResultBean.getState())) {
            selectDeviceDetailBean = deviceDetailResultBean;
            selectCarBean.setState(selectDeviceDetailBean.getState());
            setDeviceDetailData();
        }
    }

    @Override
    public void submitOneKeyFunctionSuccess(BaseBean baseBean) {
        ToastUtils.show(getString(R.string.operation_success));
    }

    @Override
    public void getDeviceConfigSuccess(DeviceConfigResultBean deviceConfigResultBean) {
        if (deviceConfigResultBean.getRecord() == 1) {
            btnRecord.setVisibility(View.VISIBLE);
        } else {
            btnRecord.setVisibility(View.GONE);
        }
        functionBeans.clear();
        functionBeans.add(new FunctionBean(0, getString(R.string.menu_0), R.drawable.icon_menu_0));
        functionBeans.add(new FunctionBean(1, getString(R.string.menu_1), R.drawable.icon_menu_1));
        for (String string : deviceConfigResultBean.getCmd_type()) {
            if (string.equals(ResultDataUtils.Function_Restart)) {
                functionBeans.add(new FunctionBean(2, getString(R.string.menu_2), R.drawable.icon_menu_2));
            }
        }
        if (deviceConfigResultBean.getHistory() == 1) {
            functionBeans.add(new FunctionBean(3, getString(R.string.menu_3), R.drawable.icon_menu_3));
        }

        functionBeans.add(new FunctionBean(4, getString(R.string.menu_4), R.drawable.icon_menu_4));
        for (String string : deviceConfigResultBean.getCmd_type()) {
            if (string.equals(ResultDataUtils.Function_Finddev)) {
                functionBeans.add(new FunctionBean(5, getString(R.string.menu_5), R.drawable.icon_menu_5));
            }
        }
        if (!deviceConfigResultBean.getMode_fun().equals(ResultDataUtils.Mode_Stand_By_Invalid)) {
            functionBeans.add(new FunctionBean(6, getString(R.string.menu_6), R.drawable.icon_menu_6));
        }
        functionBeans.add(new FunctionBean(7, getString(R.string.menu_7), R.drawable.icon_menu_7));
        mAdapter.notifyDataSetChanged();

        isDeviceReset = false;
        for (String string : deviceConfigResultBean.getCmd_type()) {
            if (string.equals(ResultDataUtils.Function_Reset)) {
                isDeviceReset = true;
                break;
            }
        }
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
                        if (!TextUtils.isEmpty(bean.getTime()) && !TextUtils.isEmpty(getBean.getTime())) {
                            if (DateUtils.data_5(getBean.getTime()) > DateUtils.data_5(bean.getTime())) {
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
        mAMap.clear();
        deviceMap.clear();
        selectCarBean = null;
        setBottomSheetBehaviorHide();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mlocationClient != null) {
                    mlocationClient.startLocation();
                }
            }
        }, 1500);
    }

    /**
     * 在地图上绘制设备
     *
     * @param isInitiativeRefresh 是否主动刷新数据
     */
    private void onDeviceToMapShow(boolean isInitiativeRefresh) {
        mBuilder = null;
        mBuilder = new LatLngBounds.Builder();
        for (int i = 0; i < mDeviceGetBeans.size(); i++) {
            DeviceModel model = mDeviceGetBeans.get(i);
            if (model.getLat() == 0 && model.getLon() == 0) {
                continue;
            }
            double lat = (double) model.getLat() / 1000000;
            double lon = (double) model.getLon() / 1000000;

            // 更新选中设备信息
            if (selectCarBean != null) {
                if (selectCarBean.getImei().equals(model.getImei())) {
                    selectCarBean = model;
                    getDeviceDetailInfo();
                }
            }

            // 开始绘制设备marker
            LatLng deviceLatLng = new LatLng(lat, lon);
            if (deviceMap.get(model.getImei()) != null) {
                Objects.requireNonNull(deviceMap.get(model.getImei())).setPosition(deviceLatLng);
                Objects.requireNonNull(deviceMap.get(model.getImei())).setIcon(getMarkerIcon(model, deviceLatLng));
            } else {
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(deviceLatLng);
                markerOption.title(model.getImei());
                markerOption.icon(getMarkerIcon(model, deviceLatLng));
                markerOption.anchor(0.5f, 0.8f);
                markerOption.draggable(false);
                markerOption.visible(true);
                Marker marker = mAMap.addMarker(markerOption);
                deviceMap.put(model.getImei(), marker);
            }
            mBuilder.include(deviceLatLng);
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
                    if (mDeviceGetBeans.size() > 0) {
                        if (selectCarBean == null) {
                            mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBuilder.build(), 300));
                            mZoom = mAMap.getCameraPosition().zoom;
                        }
                    }
                }
            }, 500);
        }
    }

    /**
     * 绘制设备图标等信息
     *
     * @param theDevice
     * @return
     */
    private BitmapDescriptor getMarkerIcon(DeviceModel theDevice, LatLng deviceLocation) {
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
            float distance = AMapUtils.calculateLineDistance(new LatLng(mLatitude, mLongitude), deviceLocation);
            strShow = strShow + DeviceUtils.getDeviceDistance(getContext(), distance);
        }
        tvName.setTextColor(getResources().getColor(DeviceUtils.getDeviceNameColor(theDevice.getPower())));
        tvName.setText(strShow);

        getDeviceState(theDevice.getState(), ivCar);
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
    private void getDeviceState(String state, ImageView ivView) {
        switch (state) {
            case ResultDataUtils.Device_State_Line_Sleep:
                ivView.setImageResource(R.drawable.icon_device_line_sleep);
                break;
            case ResultDataUtils.Device_State_Line_On:
                ivView.setImageResource(R.drawable.icon_device_line_on);
                break;
            case ResultDataUtils.Device_State_Line_Down:
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

        ivSignal.setImageResource(ResultDataUtils.getLocationType(selectCarBean.getLocation_type()));
        switch (selectDeviceDetailBean.getState()) {
            case ResultDataUtils.Device_State_Line_Down:
                tvState.setText(getString(R.string.state_line_down));
                tvState.setTextColor(getResources().getColor(R.color.color_999999));
                tvStaticTimeHint.setText(getString(R.string.off_time));
                break;
            case ResultDataUtils.Device_State_Line_On:
                tvState.setText(getString(R.string.state_line_on));
                tvState.setTextColor(mContext.getResources().getColor(R.color.color_2ABA5A));
                tvStaticTimeHint.setText(getString(R.string.online_time));
                break;
            case ResultDataUtils.Device_State_Line_Sleep:
                tvState.setText(getString(R.string.state_line_static));
                tvState.setTextColor(mContext.getResources().getColor(R.color.color_FF2F25));
                tvStaticTimeHint.setText(getString(R.string.static_time));
                break;
        }
        tvStaticTimeHint.setVisibility(View.VISIBLE);
        tvStaticTime.setVisibility(View.VISIBLE);
        if (selectDeviceDetailBean.getState_begin_time() == 0) {
            tvStaticTime.setText("");
        } else {
            if (selectDeviceDetailBean.getState_time() == 0) {
                tvStaticTime.setText("");
            } else {
                tvStaticTime.setText(DeviceUtils.getParkingTime(selectDeviceDetailBean.getState_time(), getContext()));
            }
        }
        tvCommunicationTime.setText(DateUtils.timeConversionDate_two(String.valueOf(selectDeviceDetailBean.getLast_com_time())));

        if (selectDeviceDetailBean.getBd_satellite() == 0) {
            tvBdNumber.setVisibility(View.GONE);
            tvBdLocation.setVisibility(View.GONE);
        } else {
            tvBdNumber.setVisibility(View.VISIBLE);
            tvBdLocation.setVisibility(View.VISIBLE);
            tvBdNumber.setText(selectDeviceDetailBean.getBd_satellite() + "");
        }
        if (selectDeviceDetailBean.getGps_satellite() == 0) {
            tvGpsLocation.setVisibility(View.GONE);
            tvGpsNumber.setVisibility(View.GONE);
        } else {
            tvGpsLocation.setVisibility(View.VISIBLE);
            tvGpsNumber.setVisibility(View.VISIBLE);
            tvGpsNumber.setText(selectDeviceDetailBean.getGps_satellite() + "");
        }
        tvGalileoNumber.setText(selectDeviceDetailBean.getSigna_val() + "");

        if (!TextUtils.isEmpty(selectCarBean.getDevice_name())) {
            tvDeviceName.setText(selectCarBean.getDevice_name());
        } else {
            tvDeviceName.setText(selectCarBean.getImei());
        }
        tvSpeed.setText(speed + "km/h");
        tvMode.setText(selectDeviceDetailBean.getDetail().getMode_name());

        onShowRealTimeTrackSwitch(selectCarBean.getImei());
        DeviceUtils.setLocationSignalData(selectDeviceDetailBean.getSignal_rate(), ivGsm, tvGsmNumber);
        DeviceUtils.setElectricImageData(selectCarBean.getPower(), ivBattery, tvBattery);
        tvLocationTime.setText(selectCarBean.getTime());
        AddressParseUtil.getAmapAddress(getActivity(), tvAddress, selectLatForDevice, selectLonForDevice, "");
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

    // ------------------------------------------ 谷歌瓦片地图 -------------------------------------------

    /**
     * 修改地图类型
     *
     * @param isSatelliteMap 是否是卫星地图
     */
    private void onModifyMapType(boolean isSatelliteMap) {
        if (mtileOverlay != null) {
            mtileOverlay.remove();
        }
        if (isSatelliteMap) {
            // 卫星地图
            mapUrl = Api.Map_SatelliteMap;
            ALBUM_PATH = FileUtilApp.File_Google_Map_SatelliteMap;
        } else {
            // 2D地图
            mapUrl = Api.Map_2D;
            ALBUM_PATH = FileUtilApp.File_Google_Map;
        }

        useOMCMap();
    }

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
                RealTimeTrackBean trackBean = new RealTimeTrackBean();
                trackBean.setImei(bean.getImei());
                trackBean.setRealTimeTrack(bean.isRealTimeTrack());
                mTrackBeans.add(trackBean);
            }
        }
        if (mDeviceGetBeans.size() > 0 && mTrackBeans.size() > 0) {
            onRealTimeTrackColorId(0);
        }
    }

    /**
     * 设置对应实时轨迹的设备的轨迹线颜色
     */
    private void onRealTimeTrackColorId(int position) {
        if (position < mDeviceGetBeans.size() && mTrackBeans.size() > 0) {
            setDeviceColorId(position, mDeviceGetBeans.get(position).getImei(), true);
        }
    }

    /**
     * 单个设备设置轨迹线的颜色
     *
     * @param imei
     */
    private void onScreenDeviceColorId(String imei) {
        for (int i = 0; i < mDeviceGetBeans.size(); i++) {
            if (imei.equals(mDeviceGetBeans.get(i).getImei())) {
                setDeviceColorId(i, mDeviceGetBeans.get(i).getImei(), false);
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
        for (RealTimeTrackBean bean : mTrackBeans) {
            if (bean.getImei().equals(imei)) {
                bean.setColorId(ColorUtil.colorIdToRGBA(mColorLists.get(index).getColor()));
                break;
            }
        }

        if (isNext) {
            position++;
            finalColorPosition = position;
            if (mTimerHandler != null) {
                mTimerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRealTimeTrackColorId(finalColorPosition);
                    }
                }, 20);
            }
        }
    }

    /**
     * 添加轨迹线的下一个点坐标
     *
     * @param position
     */
    private void onAddLocationLatAndLon(int position) {
        if (mDeviceGetBeans != null && position < mDeviceGetBeans.size()) {
            setLocationLatAndLon(position, mDeviceGetBeans.get(position).getImei(),
                    DeviceUtils.getAmapLatLng(mDeviceGetBeans.get(position).getLat(), mDeviceGetBeans.get(position).getLon()));
        }
    }

    /**
     * 添加轨迹线的下一个点坐标
     *
     * @param latLng
     */
    private void setLocationLatAndLon(int position, String imei, LatLng latLng) {
        LatLng lastLat = null;
        if (mTrackBeans != null) {
            if (mTrackBeans.size() > 0) {
                RealTimeTrackBean bean = null;
                for (RealTimeTrackBean trackBean : mTrackBeans) {
                    if (trackBean.getImei().equals(imei)) {
                        bean = trackBean;
                    }
                }
                if (bean != null && bean.getFollowLatLists() != null) {
                    lastLat = bean.getFollowLatLists().get(bean.getFollowLatLists().size() - 1);
                }
            }
            for (RealTimeTrackBean bean : mTrackBeans) {
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

            position++;
            finalLocationPosition = position;
            if (mTimerHandler != null) {
                mTimerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onAddLocationLatAndLon(finalLocationPosition);
                    }
                }, 20);
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

                RealTimeTrackBean trackBean = new RealTimeTrackBean();
                trackBean.setImei(selectCarBean.getImei());
                trackBean.setRealTimeTrack(true);
                trackBean.setFollowLatAndLon(DeviceUtils.getAmapLatLng(selectCarBean.getLat(), selectCarBean.getLon()));
                mTrackBeans.add(trackBean);

                onScreenDeviceColorId(selectCarBean.getImei());
                ivRealTimeTrack.setImageResource(R.drawable.icon_real_track_on);
            } else {
                mTrackSwitchBeans.remove(switchBean);
                RealTimeTrackBean bean = null;
                for (RealTimeTrackBean trackBean : mTrackBeans) {
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

                    for (RealTimeTrackBean trackBean : mTrackBeans) {
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
                    mTrackBeans.get(position).setPolyline(mAMap.addPolyline(new PolylineOptions()
                            .addAll(mTrackBeans.get(position).getFollowLatLists())
                            .width(5)
                            .color(mTrackBeans.get(position).getColorId())));
                    mTrackBeans.get(position).getPolyline().setZIndex(9999);
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
                }, 20);
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
        for (RealTimeTrackBean bean : mTrackBeans) {
            if (bean.getImei().equals(imei)) {
                isHas = true;
                break;
            }
        }
        ivRealTimeTrack.setImageResource(isHas ? R.drawable.icon_real_track_on : R.drawable.icon_real_track_off);
    }

}
