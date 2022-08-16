package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.di.component.DaggerNavigationGoogleComponent;
import com.slxk.hounddog.mvp.contract.NavigationGoogleContract;
import com.slxk.hounddog.mvp.presenter.NavigationGooglePresenter;
import com.slxk.hounddog.mvp.utils.AddressParseUtil;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.GoogleLocationUtils;
import com.slxk.hounddog.mvp.utils.GoogleMapUtils;
import com.slxk.hounddog.mvp.utils.GpsUtils;
import com.slxk.hounddog.mvp.utils.MyOrientationListener;
import com.slxk.hounddog.mvp.utils.Utils;

import java.util.HashMap;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 导航
 * <p>
 * Created by MVPArmsTemplate on 12/23/2021 14:43
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class NavigationGoogleActivity extends BaseActivity<NavigationGooglePresenter> implements NavigationGoogleContract.View,
        OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    @BindView(R.id.iv_navigation)
    ImageView ivNavigation;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_address_hint)
    TextView tvAddressHint;

    private long mDeviceLatitude = 0; // 设备纬度
    private long mDeviceLongitude = 0; // 设备经度
    private int mDeviceState = 2; // 设备状态
    private static final String Intent_Lat_Key = "lat_key";
    private static final String Intent_Lon_Key = "lon_key";
    private static final String Intent_State_Key = "state_key";

    private double mDeviceLat;
    private double mDeviceLon;

    private SupportMapFragment mapFragment; // 谷歌地图
    private GoogleMap mGoogleMap; // 谷歌地图

    private int size;
    private float mZoom = 15;
    private LatLng userLocation = new LatLng(39.90923, 116.397428);
    private LatLng carLocation;
    private LatLngBounds.Builder mBuilder; // 所有设备可视化Builder
    private boolean isSatelliteMap = true; // 是否是卫星地图
    private boolean isLocationChina = true; // 当前手机定位是否是在中国，在中国的话不显示3D地图建筑物名称，台湾显示建筑物名称

    // 手机传感器方向
    private MyOrientationListener myOrientationListener;
    private float mXDirection = 0;
    private double mLatitude = 0.0; // 定位获取到的纬度
    private double mLongitude = 0.0; // 定位获取到的经度

    private HashMap<String, Marker> deviceMap; // 存储任务
    private String mCompassImei = "00000000"; // 罗盘虚拟imei号，用来判断是否点击了罗盘
    private String mUserImei = "11111111"; // 用户当前位置虚拟imei号，用来判断是否点击了用户位置

    public static Intent newInstance(long lat, long lon, int state) {
        Intent intent = new Intent(MyApplication.getMyApp(), NavigationGoogleActivity.class);
        intent.putExtra(Intent_Lat_Key, lat);
        intent.putExtra(Intent_Lon_Key, lon);
        intent.putExtra(Intent_State_Key, state);
        return intent;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerNavigationGoogleComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_navigation_google;//setContentView(id);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.menu_0));
        deviceMap = new HashMap<>();
        mDeviceLatitude = getIntent().getLongExtra(Intent_Lat_Key, 0);
        mDeviceLongitude = getIntent().getLongExtra(Intent_Lon_Key, 0);
        mDeviceState = getIntent().getIntExtra(Intent_State_Key, 2);
//        isSatelliteMap = ConstantValue.isMapSatelliteMap(true);
        isLocationChina = ConstantValue.isInChina();
        mBuilder = new LatLngBounds.Builder();
        tvAddressHint.setText(getString(R.string.address) + "：");

        mDeviceLat = (double) mDeviceLatitude / 1000000;
        mDeviceLon = (double) mDeviceLongitude / 1000000;
        carLocation = new LatLng(mDeviceLat, mDeviceLon);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        initOritationListener();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);//选择手势
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);//缩放按钮
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setOnMyLocationClickListener(this);
        mGoogleMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            if (mGoogleMap != null) {
//                mGoogleMap.setMyLocationEnabled(true);
//            }
//        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userLocation)      // Sets the center of the map to Mountain View
                .zoom(mZoom)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                  // Sets the tilt of the camera to 30 degrees
                .build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (isSatelliteMap) {
            if (isLocationChina) {
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else {
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        } else {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        initViews();
        getLocation();
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return true;
    }

    private void initViews() {
        carLocation = GpsUtils.googleGPSConverter(mDeviceLat, mDeviceLon);
        mBuilder.include(carLocation);
        updateMapCenter(carLocation);

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(carLocation);
        markerOption.icon(getMarkerIcon());
        markerOption.anchor(0.5f, 1);
        markerOption.draggable(false);
        markerOption.visible(true);
        mGoogleMap.addMarker(markerOption);

        AddressParseUtil.getGoogleAddress(this, tvAddress, mDeviceLat, mDeviceLon, "");
    }

    /**
     * 绘制设备图标等信息
     *
     * @return
     */
    private BitmapDescriptor getMarkerIcon() {
        View view = View.inflate(this, R.layout.layout_marker_navigation, null);
        ImageView ivCar = view.findViewById(R.id.iv_car);
        ivCar.setImageResource(R.drawable.icon_device_line_on);

//        switch (mDeviceState) {
//            case 0:
//                ivCar.setImageResource(R.drawable.icon_device_static);
//                break;
//            case 1:
//                ivCar.setImageResource(R.drawable.icon_device_on);
//                break;
//            case 2:
//                ivCar.setImageResource(R.drawable.icon_device_off);
//                break;
//        }
        return convertViewToBitmap(view);
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
            userLocation = getLatLngChange(mLatitude, mLongitude);
            mBuilder.include(userLocation);
            addMyLocalMaker(userLocation);
        }
    }

    /**
     * 将我的位置的坐标添加进去
     *
     * @param location 我的位置经纬度
     */
    @SuppressLint("SetTextI18n")
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

            //计算p1、p2两点之间的直线距离，单位：米
            double distance = GoogleMapUtils.toRadiusMeters(userLocation, carLocation);
            calcuteSize(distance);
            if (distance >= 1000) {
                tvDistance.setText(getString(R.string.distance_between_people_and_equipment) + "：" + Utils.formatValue(distance / 1000) + "km");
            } else {
                tvDistance.setText(getString(R.string.distance_between_people_and_equipment) + "：" + Utils.formatValue(distance)
                        + getString(R.string.meter));
            }

            ivNavigation.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBuilder.build(), size));
                }
            }, 1000);
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
    private void updateMapCenter(LatLng latLng) {
        if (mGoogleMap != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mZoom));
        }
    }

    /**
     * 计算地图缩放距离边距距离
     *
     * @param distance 两经纬度之间的距离
     */
    private void calcuteSize(double distance) {
        if (distance <= 5000) {
            size = 400;
        } else if (distance > 5000 && distance <= 10000) {
            size = 430;
        } else if (distance > 10000 && distance < 1000000) {
            size = 450;
        } else if (distance > 100000 && distance < 500000) {
            size = 470;
        } else if (distance > 500000 && distance < 1000000) {
            size = 500;
        } else if (distance > 1000000) {
            size = 550;
        }
    }

    @Override
    protected void onResume() {
        // 开启方向传感器
        myOrientationListener.start();
        super.onResume();
    }

    @Override
    protected void onStop() {
        // 关闭方向传感器
        myOrientationListener.stop();
        super.onStop();
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

    @OnClick(R.id.iv_navigation)
    public void onViewClicked() {
    }

}
