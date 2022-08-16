package com.slxk.hounddog.app;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.Utils;
import com.clj.fastble.data.BleDevice;
import com.heytap.msp.push.HeytapPushManager;
import com.jess.arms.base.BaseApplication;
import com.slxk.hounddog.mvp.model.bean.RealTimeTrackSwitchBean;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.CrashHandlerUtil;
import com.slxk.hounddog.mvp.utils.MyDisplayMetrics;
import com.slxk.hounddog.mvp.utils.PolygonalArea;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.vivo.push.util.VivoPushException;
import com.vondear.rxtool.RxTool;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends BaseApplication {

    private static MyApplication myApp;
    private static Context mContext;

    private long system_time = 0; // 点击时间，用于处理快速点击

    private String handheld_battery = ""; // 掌机电量
    private String handheld_connected = ""; // 掌机连接状态
    private String mobile_location_state = ""; // 手机定位状态
    private double latitude = 0; // 手机定位
    private double longitude = 0;
    private String device_imei; // 选中的设备号
    // 扫描到的蓝牙列表
    private ArrayList<BleDevice> bleDeviceList;
    private boolean isBeforeAccount = false; // 当前登录账号是否和上一个登录账号相同
    private boolean isMergeAccount = false; // 是否需要合并账号

    private int check_fragment = 0; // 首页切换的fragment位置索引

    // 实时轨迹开关状态
    private ArrayList<RealTimeTrackSwitchBean> trackSwitchBeans; // 实时轨迹开关状态

    // 小米推送
    // user your appid the key.
    private static final String APP_ID = "2882303761520136604";
    // user your appid the key.
    private static final String APP_KEY = "5142013642604";
    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    // com.xiaomi.mipushdemo
    public static final String TAG = "com.slxk.hounddog";

    public static MyApplication getMyApp() {
        return myApp;
    }

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        mContext = this;
        RxTool.init(this);
        Utils.init(this);

        // crash日志
        CrashHandlerUtil.getInstance().init(this);

        MyDisplayMetrics.init(this);

        // 友盟注册
        UMConfigure.preInit(this,"623bcbd96de90f4810da27d0","Umeng");
        if (ConstantValue.getUmengInit()){
            UMConfigure.init(this, "623bcbd96de90f4810da27d0", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
            // 选用AUTO页面采集模式
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        }

        // 判断用户是否同意了隐私政策协议
        if (ConstantValue.isAgreePrivacy()){
            //在使用SDK各组件之前初始化context信息，传入ApplicationContext
            SDKInitializer.initialize(this);
            //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
            //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
            SDKInitializer.setCoordType(CoordType.GCJ02);

            initForXiaoMiPush();
            initForVIVOPush();
            HeytapPushManager.init(this, true);
        }

        PolygonalArea.onAddBaiduPoint();
        PolygonalArea.onTaiWanPoint();
        PolygonalArea.onHangKongPoint();
    }

    private void initForVIVOPush(){
        //初始化push
        try {
            PushClient.getInstance(getApplicationContext()).initialize();
        } catch (VivoPushException e) {
            e.printStackTrace();
        }

        // 打开push开关, 关闭为turnOffPush，详见api接入文档
        PushClient.getInstance(getApplicationContext()).turnOnPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int state) {
                // TODO: 开关状态处理， 0代表成功
            }
        });
    }

    /**
     * 初始化小米推送
     */
    private void initForXiaoMiPush(){
        //初始化push推送服务
        if(shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @SuppressLint("LogNotTimber")
            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @SuppressLint("LogNotTimber")
            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getApplicationInfo().processName;
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public String getAppId(){
        return APP_ID;
    }

    public String getAppKey(){
        return APP_KEY;
    }

    /**
     * 获取当前切换的fragment位置索引
     * @param position
     */
    public void setCheck_fragment(int position){
        this.check_fragment = position;
    }

    public int getCheck_fragment(){
        return check_fragment;
    }

    /**
     * 是否是上一个账号登录
     * @param isAccount
     */
    public void setBeforeAccount(boolean isAccount){
        this.isBeforeAccount = isAccount;
    }

    public boolean isBeforeAccount(){
        return isBeforeAccount;
    }

    /**
     * 是否需要合并账号
     * @param mergeAccount
     */
    public void setMergeAccount(boolean mergeAccount){
        this.isMergeAccount = mergeAccount;
    }

    public boolean isMergeAccount(){
        return isMergeAccount;
    }

    /**
     * 获取点击保存的时间戳
     * @return
     */
    public long getSystemTime(){
        return system_time;
    }

    /**
     * 设置当前点击的时间戳
     * @param time
     */
    public void setSystemTime(long time){
        system_time = time;
    }

    /**
     * 获取选中的imei好
     * @return
     */
    public String getDevice_imei(){
        return device_imei;
    }

    /**
     * 保存选中的imei号
     * @param imei
     */
    public void setDevice_imei(String imei){
        this.device_imei = imei;
    }

    /**
     * 掌机电量
     * @return
     */
    public String getHandheld_battery(){
        return handheld_battery;
    }

    public void setHandheld_battery(String battery){
        this.handheld_battery = battery;
    }

    /**
     * 掌机连接状态
     * @return
     */
    public String getHandheld_connected(){
        return handheld_connected;
    }

    public void setHandheld_connected(String connected){
        this.handheld_connected = connected;
    }

    /**
     * 手机定位状态
     * @return
     */
    public String getMobile_location_state(){
        return mobile_location_state;
    }

    public void setMobile_location_state(String state){
        this.mobile_location_state = state;
    }

    /**
     * 扫描的蓝牙列表
     * @return
     */
    public ArrayList<BleDevice> getBleDeviceList(){
        return bleDeviceList;
    }

    public void setBleDeviceList(ArrayList<BleDevice> list){
        if (bleDeviceList == null){
            bleDeviceList = new ArrayList<>();
        }else{
            bleDeviceList.clear();
        }
        bleDeviceList.addAll(list);
    }

    public void clearBleDeviceList(){
        if (bleDeviceList == null){
            bleDeviceList = new ArrayList<>();
        }else{
            bleDeviceList.clear();
        }
    }

    /**
     * 手机定位经纬度
     * @return
     */
    public double getLatitude(){
        return latitude;
    }

    public void setLatitude(double lat){
        this.latitude = lat;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude(double lon){
        this.longitude = lon;
    }

    /**
     * 实时轨迹开关列表
     * @return
     */
    public ArrayList<RealTimeTrackSwitchBean> getTrackSwitchBeans(){
        return trackSwitchBeans;
    }

    public void setTrackSwitchBeans(ArrayList<RealTimeTrackSwitchBean> datas){
        if (trackSwitchBeans == null){
            trackSwitchBeans = new ArrayList<>();
        }else{
            trackSwitchBeans.clear();
        }
        trackSwitchBeans.addAll(datas);
    }

    public void clearTrackSwitchBeans(){
        if (trackSwitchBeans == null){
            trackSwitchBeans = new ArrayList<>();
        }else{
            trackSwitchBeans.clear();
        }
    }

    public void clearData(){
        handheld_battery = "";
        handheld_connected = "";
        mobile_location_state = "";
        device_imei = "";
        latitude = 0;
        longitude = 0;
        check_fragment = 0;
        if (bleDeviceList != null){
            bleDeviceList.clear();
        }
        if (trackSwitchBeans != null){
            trackSwitchBeans.clear();
        }
    }

}
