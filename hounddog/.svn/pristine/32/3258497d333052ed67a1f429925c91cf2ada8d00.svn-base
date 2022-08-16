package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.UrlTileProvider;
import com.amap.api.maps.model.VisibleRegion;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.db.MapDownloadModel;
import com.slxk.hounddog.db.MapXYModel;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.di.component.DaggerOfflineMapDownLoadComponent;
import com.slxk.hounddog.mvp.contract.OfflineMapDownLoadContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.model.bean.OfflineMapDownLoadBean;
import com.slxk.hounddog.mvp.presenter.OfflineMapDownLoadPresenter;
import com.slxk.hounddog.mvp.utils.BitmapHelperAmap;
import com.slxk.hounddog.mvp.utils.FileUtilApp;
import com.slxk.hounddog.mvp.utils.MapDownloadUtil;
import com.slxk.hounddog.mvp.utils.MapImageCache;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.AlertAppTipDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 地图选择下载页
 * <p>
 * Created by MVPArmsTemplate on 12/24/2021 15:04
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class OfflineMapDownLoadActivity extends BaseActivity<OfflineMapDownLoadPresenter> implements OfflineMapDownLoadContract.View,
        AMap.OnCameraChangeListener, AMapLocationListener {

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_map_size)
    TextView tvMapSize;
    @BindView(R.id.tv_map_number)
    TextView tvMapNumber;
    @BindView(R.id.tv_map_flow)
    TextView tvMapFlow;
    @BindView(R.id.tv_map_sd)
    TextView tvMapSd;
    @BindView(R.id.tv_map_hd)
    TextView tvMapHd;
    @BindView(R.id.btn_start_download)
    Button btnStartDownload;
    @BindView(R.id.edt_map_name)
    EditText edtMapName;

    private AMap mAMap;
    private LatLng centerPoint = new LatLng(39.90923, 116.397428);
    private float mZoom = 16;
    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption;
    private MyLocationStyle myLocationStyle;
    private BitmapHelperAmap bitmapHelperAmap;
    private boolean isFirstLocation = true; // 是否是第一次定位，避免后续定位成功后，一直设置定位点为中心点
    private double mLatitude = 0.0; // 定位获取到的纬度
    private double mLongitude = 0.0; // 定位获取到的经度

    // 谷歌瓦片地图
    // 判断当前使用高德地图，还是高德地图的谷歌瓦片地图
    private TileOverlay mtileOverlay;
    private String ALBUM_PATH = FileUtilApp.File_Google_Map_SatelliteMap; // 存储路径
    private String mapUrl = Api.Map_SatelliteMap; // 请求地图url地址

    private static final int mMapCountDownTime = 2; // 倒计时固定时长
    private int mMapCountDown = mMapCountDownTime; // 倒计时递减时长

    private int mMapType = 0; // 地图类型，0：省流量，1：标清，2：高清
    private int mMapZoomMax = 18; // 地图最大层级
    private int mMapFlowZoomMin = 16; // 省流量地图最小缩放层级
    private int mMapSDZoomMin = 15; // 标清地图最小缩放层级
    private int mMapHDZoomMin = 14; // 高清量地图最小缩放层级
    private int mImageSize = 20; // 定义每张图片30kb，省流量20kb每张，标清25kb，高清30kb
    private int mZoomValue = 16; // 判断地图层级之后拿到的当前开始下载地图的层级
    private long mUploadMapNumber = 0; // 需要下载的地图总张数
    private long mOne_G = 1024 * 1024; // 1G的大小
    private String mTotalSize = ""; // 总下载大小
    private long mMapFlowMapNumber = 100 * 1024; // 省流量地图最大下载大小，单位M
    private long mMapSDMapNumber = 150 * 1024; // 标清地图最大下载大小，单位M
    private long mMapHDMapNumber = 200 * 1024; // 高清量地图最大下载大小，单位M
    private long mUploadTotalSize = 0; // 需要下载的总下载大小

    private ArrayList<OfflineMapDownLoadBean> mOfflineMapBeans;
    private String mMapName; // 地图名称
    private ArrayList<MapXYModel> mMapXYModels;
    private MyDao mMapDownloadDao;
    private MyDao mMapXYDao;

    // 使用信号量来
    private Semaphore semaphore;
    // 开启线程下载数据
    private Thread runThread;
    private int mDownloadIndex = 0; // 下载到的位置
    private String mCurrentTime = ""; // 当前时间

    private boolean isChangeZoom = true; // 是否动了地图层级
    private static final int mMessageId = 10;
    private String mAllMapNames = ""; // 所有地图的名称，用,号隔开，用来避免出现重复名称的
    private String[] mMapHasNames;
    private Handler mWhenHandler; // 用于循环

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerOfflineMapDownLoadComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_offline_map_down_load;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        setTitle(getString(R.string.map_add));
        mOfflineMapBeans = new ArrayList<>();
        mMapXYModels = new ArrayList<>();
        try {
            mMapDownloadDao = new MyDao(MapDownloadModel.class);
            mMapXYDao = new MyDao(MapXYModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmapHelperAmap = new BitmapHelperAmap(this);
        mAllMapNames = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(mAllMapNames)){
            mMapHasNames = mAllMapNames.split(",");
        }
        mWhenHandler = new Handler();

        initMaps();
        mapHandler.sendEmptyMessage(mMessageId);
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
            mAMap.getUiSettings().setZoomControlsEnabled(true);
            mAMap.getUiSettings().setMyLocationButtonEnabled(false);
            mAMap.setOnCameraChangeListener(this);

            mZoom = 16;
            updateMapCenter();

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
            myLocationStyle.myLocationIcon(bitmapHelperAmap.getBitmapZoomForUserLocationMap(mZoom));
            myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
            myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
            // 设置定位监听
            mAMap.setMyLocationStyle(myLocationStyle);
            mAMap.setMyLocationEnabled(true);
        }
    }

    /**
     * 地图中心点位置
     */
    private void updateMapCenter() {
        if (mAMap != null) {
            mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(centerPoint, mZoom, 0, 0)));
        }
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        if (mlocationClient != null) {
            mlocationClient.startLocation();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
        }
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
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
        if (runThread != null){
            runThread.interrupt();
        }
        if (semaphore != null){
            semaphore = null;
        }
        if (mapHandler != null){
            mapHandler.removeCallbacksAndMessages(null);
            mapHandler = null;
        }
        if (mWhenHandler != null){
            mWhenHandler.removeCallbacksAndMessages(null);
            mWhenHandler = null;
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

                // 定位成功之后，判断是否有设备，没有设备就跳转到手机定位位置
                if (isFirstLocation) {
                    centerPoint = new LatLng(mLatitude, mLongitude);
                    updateMapCenter();
                }
                isFirstLocation = false;
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        mZoom = cameraPosition.zoom;
        centerPoint = cameraPosition.target;
        isChangeZoom = true;
        mMapCountDown = mMapCountDownTime;
    }

    @SuppressLint("HandlerLeak")
    private Handler mapHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mMapCountDown--;
            if (mMapCountDown <= 0) {
                mMapCountDown = mMapCountDownTime;
                if (isChangeZoom){
                    getVisibleRegion();
                }
            }
            mapHandler.sendEmptyMessageDelayed(mMessageId, 1000);
        }
    };

    /**
     * 获取可见范围内四个角度的经纬度
     */
    private void getVisibleRegion() {
        VisibleRegion visibleRegion = mAMap.getProjection().getVisibleRegion();
        LatLng farLeft = visibleRegion.farLeft;     //可视区域的左上角。
        LatLng farRight = visibleRegion.farRight;   //可视区域的右上角。
        LatLng nearLeft = visibleRegion.nearLeft;   //可视区域的左下角。
        LatLng nearRight = visibleRegion.nearRight; //可视区域的右下角。

        mOfflineMapBeans.clear();
        mDownloadIndex = 0;
        mUploadMapNumber = 0;
        mUploadTotalSize = 0;
        mZoomValue = (int) mZoom;
        // 0：省流量，1：标清，2：高清
        switch (mMapType) {
            case 0:
                if (mZoomValue < mMapFlowZoomMin) {
                    mZoomValue = mMapFlowZoomMin;
                }
                mImageSize = 20;
                break;
            case 1:
                if (mZoomValue < mMapSDZoomMin) {
                    mZoomValue = mMapSDZoomMin;
                }
                mImageSize = 25;
                break;
            case 2:
                if (mZoomValue < mMapHDZoomMin) {
                    mZoomValue = mMapHDZoomMin;
                }
                mImageSize = 30;
                break;
        }

        if (mZoom >= 19){
            onCalculateMapXYAndNumberFor19_20(farLeft, nearRight);
        }else{
            onCalculateMapXYAndNumber(farLeft, nearRight);
        }
        isChangeZoom = false;
    }

    /**
     * 第19级，20级地图下载
     * @param farLeft
     * @param nearRight
     */
    private void onCalculateMapXYAndNumberFor19_20(LatLng farLeft, LatLng nearRight){
        String farLeftXY = MapDownloadUtil.getMapXY(farLeft.latitude, farLeft.longitude, mZoomValue);
//        String farRightXY = MapDownloadUtil.getMapXY(farRight.latitude, farRight.longitude, mZoomValue);
//        String nearLeftXY = MapDownloadUtil.getMapXY(nearLeft.latitude, nearLeft.longitude, mZoomValue);
        String nearRightXY = MapDownloadUtil.getMapXY(nearRight.latitude, nearRight.longitude, mZoomValue);

        if (!TextUtils.isEmpty(farLeftXY) && !TextUtils.isEmpty(nearRightXY)) {
            String[] farLeftXYs = farLeftXY.split(",");
            String[] nearRightXYs = nearRightXY.split(",");
            long farLeftX = Long.parseLong(farLeftXYs[0]);
            long farLeftY = Long.parseLong(farLeftXYs[1]);
            long nearRightX = Long.parseLong(nearRightXYs[0]);
            long nearRightY = Long.parseLong(nearRightXYs[1]);
            // 右下角的xy坐标 - 左上角的xy坐标，得出差值计算图片总数，计算每张图片经纬度需要从左上角往右下角加阀值
            long x_difference = nearRightX - farLeftX;
            long y_difference = nearRightY - farLeftY;
            if (x_difference == 0){
                x_difference = 1;
            }
            if (y_difference == 0){
                y_difference = 1;
            }
            // 取差值的绝对值，避免出现负数，方便计算
            long x_absolute_value = Math.abs(x_difference);
            long y_absolute_value = Math.abs(y_difference);
            // 计算每次相加的阀值
            long x_add_value = x_difference / x_absolute_value;
            long y_add_value = y_difference / y_absolute_value;
            // 计算当前层级需要下载的张数
            mUploadMapNumber = x_absolute_value * y_absolute_value + mUploadMapNumber;

            OfflineMapDownLoadBean bean = new OfflineMapDownLoadBean();
            bean.setFarLeftXY(farLeftXY);
            bean.setNearRightXY(nearRightXY);
            bean.setFarLeftX(farLeftX);
            bean.setFarLeftY(farLeftY);
            bean.setNearRightX(nearRightX);
            bean.setNearRightY(nearRightY);
            bean.setX_add_value(x_add_value);
            bean.setY_add_value(y_add_value);
            bean.setX_difference(x_difference);
            bean.setY_difference(y_difference);
            bean.setX_absolute_value(x_absolute_value);
            bean.setY_absolute_value(y_absolute_value);
            bean.setZoom(mZoomValue);
            mOfflineMapBeans.add(bean);
        }
    }

    /**
     * 根据经纬度计算xy轴坐标系，计算总图片张数
     *
     * @param farLeft   左上角经纬度
     * @param nearRight 右下角经纬度
     */
    @SuppressLint("SetTextI18n")
    private void onCalculateMapXYAndNumber(LatLng farLeft, LatLng nearRight) {
        if (mZoomValue > mMapZoomMax) {
            if (tvMapNumber != null) {
                tvMapNumber.setText(getString(R.string.about) + mUploadMapNumber + getString(R.string.tile));
            }
            mUploadTotalSize = mUploadMapNumber * mImageSize;
            double showSize = 0;
            if (tvMapSize != null) {
                if (mUploadTotalSize > mOne_G) {
                    showSize = (double) mUploadTotalSize / mOne_G;
                    tvMapSize.setText(getString(R.string.about) + Utils.formatValue_2(showSize) + "G");
                    mTotalSize = Utils.formatValue_2(showSize) + "G";
                } else if (mUploadTotalSize > 1024) {
                    showSize = (double) mUploadTotalSize / 1024;
                    tvMapSize.setText(getString(R.string.about) + Utils.formatValue_2(showSize) + "M");
                    mTotalSize = Utils.formatValue_2(showSize) + "M";
                } else {
                    tvMapSize.setText(getString(R.string.about) + mUploadTotalSize + "KB");
                    mTotalSize = mUploadTotalSize + "KB";
                }
            }
        } else {
            String farLeftXY = MapDownloadUtil.getMapXY(farLeft.latitude, farLeft.longitude, mZoomValue);
//        String farRightXY = MapDownloadUtil.getMapXY(farRight.latitude, farRight.longitude, mZoomValue);
//        String nearLeftXY = MapDownloadUtil.getMapXY(nearLeft.latitude, nearLeft.longitude, mZoomValue);
            String nearRightXY = MapDownloadUtil.getMapXY(nearRight.latitude, nearRight.longitude, mZoomValue);

            if (!TextUtils.isEmpty(farLeftXY) && !TextUtils.isEmpty(nearRightXY)) {
                String[] farLeftXYs = farLeftXY.split(",");
                String[] nearRightXYs = nearRightXY.split(",");
                long farLeftX = Long.parseLong(farLeftXYs[0]);
                long farLeftY = Long.parseLong(farLeftXYs[1]);
                long nearRightX = Long.parseLong(nearRightXYs[0]);
                long nearRightY = Long.parseLong(nearRightXYs[1]);
                // 右下角的xy坐标 - 左上角的xy坐标，得出差值计算图片总数，计算每张图片经纬度需要从左上角往右下角加阀值
                long x_difference = nearRightX - farLeftX;
                long y_difference = nearRightY - farLeftY;
                if (x_difference == 0){
                    x_difference = 1;
                }
                if (y_difference == 0){
                    y_difference = 1;
                }
                // 取差值的绝对值，避免出现负数，方便计算
                long x_absolute_value = Math.abs(x_difference);
                long y_absolute_value = Math.abs(y_difference);
                // 计算每次相加的阀值
                long x_add_value = x_difference / x_absolute_value;
                long y_add_value = y_difference / y_absolute_value;
                // 计算当前层级需要下载的张数
                mUploadMapNumber = x_absolute_value * y_absolute_value + mUploadMapNumber;

                OfflineMapDownLoadBean bean = new OfflineMapDownLoadBean();
                bean.setFarLeftXY(farLeftXY);
                bean.setNearRightXY(nearRightXY);
                bean.setFarLeftX(farLeftX);
                bean.setFarLeftY(farLeftY);
                bean.setNearRightX(nearRightX);
                bean.setNearRightY(nearRightY);
                bean.setX_add_value(x_add_value);
                bean.setY_add_value(y_add_value);
                bean.setX_difference(x_difference);
                bean.setY_difference(y_difference);
                bean.setX_absolute_value(x_absolute_value);
                bean.setY_absolute_value(y_absolute_value);
                bean.setZoom(mZoomValue);
                mOfflineMapBeans.add(bean);
            }

            mZoomValue++;
            onCalculateMapXYAndNumber(farLeft, nearRight);
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

    @OnClick({R.id.tv_map_flow, R.id.tv_map_sd, R.id.tv_map_hd, R.id.btn_start_download})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()) {
            switch (view.getId()) {
                case R.id.tv_map_flow:
                    onMapTypeSelect(0);
                    break;
                case R.id.tv_map_sd:
                    onMapTypeSelect(1);
                    break;
                case R.id.tv_map_hd:
                    onMapTypeSelect(2);
                    break;
                case R.id.btn_start_download:
                    onConfirmMapDownload();
                    break;
            }
        }
    }

    /**
     * 地图类型选择
     *
     * @param type 类型，0：省流量，1：标清，2：高清
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void onMapTypeSelect(int type) {
        mMapType = type;
        tvMapFlow.setBackground(getResources().getDrawable(R.drawable.bg_ffffff_fillet_00c8f8_4_left));
        tvMapFlow.setTextColor(getResources().getColor(R.color.color_00C8F8));
        tvMapSd.setBackground(getResources().getDrawable(R.drawable.bg_ffffff_00c8f8_4));
        tvMapSd.setTextColor(getResources().getColor(R.color.color_00C8F8));
        tvMapHd.setBackground(getResources().getDrawable(R.drawable.bg_ffffff_fillet_00c8f8_4_right));
        tvMapHd.setTextColor(getResources().getColor(R.color.color_00C8F8));
        switch (type) {
            case 0:
                tvMapFlow.setBackground(getResources().getDrawable(R.drawable.bg_00c8f8_fillet_4_left));
                tvMapFlow.setTextColor(getResources().getColor(R.color.color_FFFFFF));
                break;
            case 1:
                tvMapSd.setBackground(getResources().getDrawable(R.drawable.bg_00c8f8_fillet));
                tvMapSd.setTextColor(getResources().getColor(R.color.color_FFFFFF));
                break;
            case 2:
                tvMapHd.setBackground(getResources().getDrawable(R.drawable.bg_00c8f8_fillet_4_right));
                tvMapHd.setTextColor(getResources().getColor(R.color.color_FFFFFF));
                break;
        }

        getVisibleRegion();
    }

    /**
     * 提交下载地图
     */
    private void onConfirmMapDownload() {
        mMapName = edtMapName.getText().toString().trim();
        if (TextUtils.isEmpty(mMapName)){
            ToastUtils.show(getString(R.string.map_name_hint));
            return;
        }
        if (mMapHasNames != null){
            boolean isHasName = false;
            for (String name : mMapHasNames) {
                if (mMapName.equals(name)) {
                    isHasName = true;
                    break;
                }
            }
            if (isHasName){
                ToastUtils.show(getString(R.string.map_name_has_error));
                return;
            }
        }
        if (mOfflineMapBeans.size() == 0){
            ToastUtils.show(getString(R.string.download_error));
            return;
        }
        switch (mMapType) {
            case 0:
                if (mUploadTotalSize > mMapFlowMapNumber){
                    onShowMapNumberError();
                    return;
                }
                break;
            case 1:
                if (mUploadTotalSize > mMapSDMapNumber){
                    onShowMapNumberError();
                    return;
                }
                break;
            case 2:
                if (mUploadTotalSize > mMapHDMapNumber){
                    onShowMapNumberError();
                    return;
                }
                break;
        }
        showProgressDialog();
        mapHandler.removeCallbacksAndMessages(null);
        mMapXYModels.clear();
        mCurrentTime = String.valueOf(System.currentTimeMillis());
        startDownloadAction();
    }

    /**
     * 提示下载错误
     */
    private void onShowMapNumberError(){
        AlertBean bean = new AlertBean();
        bean.setTitle(getString(R.string.tip));
        bean.setAlert(getString(R.string.map_download_number_error));
        bean.setConfirmTip(getString(R.string.confirm));
        AlertAppTipDialog dialog = new AlertAppTipDialog();
        dialog.show(getSupportFragmentManager(), bean, new AlertAppTipDialog.onAlertDialogChange() {

            @Override
            public void onConfirm() {

            }

        });
    }

    /**
     * 开始下载地图
     */
    private void startDownloadAction(){
        try {
            this.semaphore = new Semaphore(1, true);
            runThread = new Thread(new Runnable() {
                @SuppressLint("LogNotTimber")
                @Override
                public void run() {
                    try {
                        mDownloadIndex = 0;
                        while (true) {
                            semaphore.acquire();
                            if (mDownloadIndex < mOfflineMapBeans.size()) {
                                OfflineMapDownLoadBean bean = mOfflineMapBeans.get(mDownloadIndex);
                                onObtainImageXYTile(bean.getFarLeftX(), bean.getFarLeftY(), bean);
                            } else {
                                // 完成轮循
                                onSubmitDownload();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            runThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始计算y轴的起点坐标
     *
     * @param x_value
     * @param y_value
     */
    private void onObtainImageYTile(long x_value, long y_value, OfflineMapDownLoadBean bean) {
        long value = y_value + bean.getY_add_value();
        boolean isBoundary = false; // 是否超过边界
        if (bean.getY_add_value() < 0) {
            if (value < bean.getNearRightY()) {
                isBoundary = true;
            }
        } else {
            if (value > bean.getNearRightY()) {
                isBoundary = true;
            }
        }
        if (isBoundary) {
            mDownloadIndex++;
            semaphore.release(1);
        } else {
            onObtainImageXYTile(x_value, value, bean);
        }
    }

    /**
     * 计算每张图的xy坐标
     *
     * @param x_value 当前点的x坐标
     * @param y_value 当前点的y坐标
     */
    private void onObtainImageXYTile(long x_value, long y_value, OfflineMapDownLoadBean bean) {
        boolean isBoundary = false; // 是否超过边界
        if (bean.getX_add_value() < 0) {
            if (x_value < bean.getNearRightX()) {
                isBoundary = true;
            }
        } else {
            if (x_value > bean.getNearRightX()) {
                isBoundary = true;
            }
        }
        if (isBoundary) {
            mWhenHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onObtainImageYTile(bean.getFarLeftX(), y_value, bean);
                }
            }, 0);
        } else {
            MapXYModel model = new MapXYModel();
            model.setTime(mCurrentTime);
            model.setXtile(x_value);
            model.setYtile(y_value);
            model.setZoom(bean.getZoom());
            mMapXYModels.add(model);

            long value = x_value + bean.getX_add_value();
            onMapNextXY(value, y_value, bean);
        }
    }

    private void onMapNextXY(long value, long y_value, OfflineMapDownLoadBean bean){
        mWhenHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onObtainImageXYTile(value, y_value, bean);
            }
        }, 0);
    }

    /**
     * 提交下载
     */
    private void onSubmitDownload() {
        if (mUploadMapNumber > 0) {
            MapDownloadModel model = new MapDownloadModel();
            model.setTime(mCurrentTime);
            model.setImage_size(mUploadMapNumber);
            model.setTotal_size(mTotalSize);
            model.setPosition(0);
            model.setProgress(0);
            model.setMap_name(mMapName);
            try {
                mMapDownloadDao.insert(model);
                mMapXYDao.insert(mMapXYModels);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 延迟时间
            long postTime = 5000;
            if (mZoom >= 19){
                postTime = 1000;
            }else if (mZoom >= 16){
                postTime = 3000;
            }else if (mZoom >= 14){
                postTime = 4000;
            }
            btnStartDownload.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onDownloadResult();
                }
            }, postTime);
        } else {
            ToastUtils.show(getString(R.string.download_error));
            dismissProgressDialog();
        }
    }

    /**
     * 存储结束，返回下载页面开始下载
     */
    private void onDownloadResult() {
        dismissProgressDialog();
        Intent intent = new Intent();
        intent.putExtra("time", mCurrentTime);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

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
                                Bitmap mBitmap = getImageBitmap(getImageStream(filePath));
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
        return BitmapFactory.decodeByteArray(targetData, 0, targetData.length);
    }

    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
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

}
