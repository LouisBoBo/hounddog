package com.slxk.hounddog.mvp.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceControl;

import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ViewUtils;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.clj.fastble.scan.BleScanner;
import com.clj.fastble.utils.HexUtil;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.comm.ObserverManager;
import com.slxk.hounddog.db.BluetoothModel;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.db.DeviceTrackModel;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.bean.BluetoothInfoBean;
import com.slxk.hounddog.mvp.model.bean.DeviceLogBean;

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
import java.util.Map;
import java.util.Objects;

import androidx.annotation.RequiresApi;

/**
 * 蓝牙连接处理工具类
 */
public class BluetoothManagerUtil {

    private static final String TAG = "BluetoothManagerUtil";
    private static volatile BluetoothManagerUtil bluetoothUtil;
    private static final String BLUETOOTH_MAIN_SERVICE = "0000aaaa-0000-1000-8000-00805f9b34fb"; //主服务
    private static final String BLUETOOTH_FEATURE_ORDER = "0000bbb0-0000-1000-8000-00805f9b34fb";  //特性 下发指令
    private static final String BLUETOOTH_FEATURE_NOTIFY = "0000bbb1-0000-1000-8000-00805f9b34fb";  //特性 下发通知
    private BluetoothGattCharacteristic mCharacteristicBBB0;
    public static final String BLUE_NAME_START = "OPL"; // 蓝牙设备 开头名称
    private onBluetoothChange mBluetoothChange;

    // 蓝牙信息
    private BleDevice mBleDevice;
    // 扫描到的蓝牙列表
    private ArrayList<BleDevice> mBleDeviceList = new ArrayList<>();
    // 设备断开的蓝牙列表，需要发起重连
    private ArrayList<BleDevice> mBleDeviceReconnectionList = new ArrayList<>();
    // 设备信息处理Dao
    private MyDao mDeviceBeanDao;
    // 设备列表 - 数据库中的设备列表
    private ArrayList<DeviceModel> mDeviceModels = new ArrayList<>();
    // 设备轨迹数据处理
    private MyDao mDeviceTrackBeanDao;
    // 已配对过的蓝牙列表
    private ArrayList<BluetoothModel> mBluetoothModels = new ArrayList<>();
    // 蓝牙设备信息处理Dao
    private MyDao mBluetoothBeanDao;
    // 已连接的蓝牙bean
    public ArrayList<BluetoothInfoBean> mConnectBluetoothBeans = new ArrayList<>();

    private String mBluetoothHostNumber = ""; // 接收机设备号
    private ArrayList<String> mDeviceImeis = new ArrayList<>(); // 设备imei号列表-总列表
    private ArrayList<String> mSendDeviceImeis = new ArrayList<>(); // 设备imei号列表-当前已发送的列表
    private boolean isBluetoothConnect = false; // 是否连接成功了接收机

    // 设备日志上报操作
    private File mFile; // 当前正在上传的文件
    private ArrayList<DeviceLogBean> mDeviceLogBeans = new ArrayList<>(); // 设备日志临时存储
    private String mDeviceLogUrl = Api.Device_Log_Url; // 日志上传url
    private String mLogData = ""; // 蓝牙传输日志时，会出现一条数据分2段传输过来，用于保存和拼接数据

    private String mPassword; // 手机蓝牙约定的 发送数据 xckj+手机蓝牙名称 GB2312  xckj+ 最多10个字符

    public static BluetoothManagerUtil getInstance() {
        if (bluetoothUtil == null) {
            synchronized (BluetoothManagerUtil.class) {
                if (bluetoothUtil == null) {
                    bluetoothUtil = new BluetoothManagerUtil();
                }
            }
        }
        return bluetoothUtil;
    }

    /**
     * 初始化蓝牙
     */
    public void initBluetooth() {
        try {
            if (mDeviceBeanDao == null){
                mDeviceBeanDao = new MyDao(DeviceModel.class);
            }
            if (mDeviceTrackBeanDao == null){
                mDeviceTrackBeanDao = new MyDao(DeviceTrackModel.class);
            }
            if (mBluetoothBeanDao == null){
                mBluetoothBeanDao = new MyDao(BluetoothModel.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BleManager.getInstance().init(MyApplication.getMyApp());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(100, 5000)
                .setConnectOverTime(15000)
                .setOperateTimeout(5000);

        onBluetoothInit();

        try{
            String phoneBluetoothName = BluetoothAdapter.getDefaultAdapter().getName();
            String str = "";
            if (phoneBluetoothName != null && phoneBluetoothName.length() > 0) {
                str = phoneBluetoothName;
            }
            String strPsd = "xckj+" + str;
            String gbHex = BluetoothUtils.toGBHex(strPsd);
            mPassword = gbHex.length() >= 60 ? gbHex.substring(0, 60) : gbHex;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 开始蓝牙服务  1、设置规则  2、开始扫描
     */
    public void startBluetoothService() {
        setScanRule();
        ViewUtils.runOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                startScan();
            }
        }, 300); // 延迟100ms
    }

    /**
     * 蓝牙扫描规则
     */
    public void setScanRule() {
        String[] names = {BLUE_NAME_START};
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setDeviceName(true, names)   //只扫描指定广播名的设备，可选
                .setScanTimeOut(5 * 60 * 60 * 1000)  // 扫描时间，可选，默认10秒 /5小时   扫描时间配置为小于等于0，会实现无限扫描，直至调用BleManger.getInstance().cancelScan()来中止扫描。
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
        BleManager.getInstance().setSplitWriteNum(180); // 发送的最大长度
    }

    /**
     * 获取账号登录 已连接的设备
     */
    private void onBluetoothInit(){
        mBluetoothModels.clear();
        if (getBluetoothListDataBase() != null) {
            mBluetoothModels.addAll(getBluetoothListDataBase());
        }
    }

    /**
     * 同步设备列表
     * @param deviceList
     */
    public void setAccountDeviceList(ArrayList<DeviceModel> deviceList){
        mDeviceModels.clear();
        mDeviceModels.addAll(deviceList);
        if (mDeviceModels.size() == 0){
            onDeviceListInit();
        }
        ViewUtils.runOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                if (isBluetoothConnect) {
                    onSendDeviceListToBluetooth();
                }
            }
        }, 200); // 延迟100ms
    }

    /**
     * 删除设备
     * @param model
     */
    public void onDeviceDelete(DeviceModel model){
        if (model != null){
            try {
                mDeviceBeanDao.delete(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mDeviceModels.size() > 0){
                mDeviceModels.remove(model);
            }
        }
    }

    /**
     * 更新设备信息
     * @param model
     */
    public void onDeviceUpdate(DeviceModel model){
        if (model != null){
            if (mDeviceModels.size() > 0){
                DeviceModel updateModel = null;
                for (DeviceModel bean : mDeviceModels) {
                    if (model.getImei().equals(bean.getImei())) {
                        bean.setDevice_name(model.getDevice_name());
                        updateModel = bean;
                        updateModel.setDevice_name(model.getDevice_name());
                        break;
                    }
                }
                if (updateModel != null){
                    try {
                        mDeviceBeanDao.update(updateModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 获取数据库设备列表
     */
    private void onDeviceListInit(){
        mDeviceModels.clear();
        if (getDeviceListDataBase() != null) {
            mDeviceModels.addAll(getDeviceListDataBase());
        }
    }

    /**
     * 判断设备是否已连接
     *
     * @param bleDevice
     * @return
     */
    public boolean isConnected(BleDevice bleDevice) {
        return BleManager.getInstance().isConnected(bleDevice);
    }

    /**
     * 断开连接
     *
     * @param bleDevice
     */
    public void disConnected(BleDevice bleDevice) {
        if (isConnected(bleDevice)){
            BleManager.getInstance().disconnect(bleDevice);
        }
    }

    /**
     * 蓝牙扫描状态
     */
    private void startScan() {
        if (BleScanner.getInstance().getScanState() != BleScanState.STATE_IDLE) { //扫描中
            BleScanner.getInstance().stopLeScan();
        }
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                // 开始扫描
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                // 扫描成功
                LogUtils.e("扫描到设备=" + bleDevice.getName() + ", " + bleDevice.getMac());
                if (!isHasBleDevice(bleDevice)) {
                    mBleDeviceList.add(bleDevice);
                    MyApplication.getMyApp().setBleDeviceList(mBleDeviceList);
                    BroadcastReceiverUtil.onBleDeviceState(MyApplication.getMyApp(), 2, bleDevice);
                }

                //在数据库中匹配是否是手动断开连接设备，如果是不再自动连接
                boolean is_activity_disconnect = false;
                for (BluetoothModel bean : mBluetoothModels) {
                    if (bean.getMac().equals(bleDevice.getMac())) {
                        is_activity_disconnect = bean.isDisconnected();
                        break;
                    }
                }

                if (!isConnected(bleDevice) && !is_activity_disconnect){
                    connect(bleDevice);
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                // 扫描结束
//                onConnectBleDevice();
            }
        });
    }

    /**
     * 判断列表里是否已经有这个设备了
     * @param bleDevice
     * @return
     */
    private boolean isHasBleDevice(BleDevice bleDevice){
        boolean isHas = false;
        for (BleDevice device : mBleDeviceList){
            if (bleDevice.getName().equals(device.getName())){
                isHas = true;
                break;
            }
        }
        return isHas;
    }

    /**
     * 连接蓝牙设备
     *
     * @param bleDevice
     */
    public void connect(final BleDevice bleDevice) {
        if (bleDevice == null || bleDevice.getName() == null || !ConstantValue.isLogin()) {
            return;
        }
        LogUtils.e("发起连接=" + bleDevice.getName() + ", " + bleDevice.getMac());
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {

            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                BroadcastReceiverUtil.onBleDeviceState(MyApplication.getMyApp(), 0, bleDevice);
                LogUtils.e("连接失败：" + bleDevice.getName() + ", " + bleDevice.getMac() + ", " + exception.getDescription());
                onStartReconnectionBleDevice();
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                LogUtils.e("连接成功=" + bleDevice.getName() + ", " + bleDevice.getMac());

                //连接成功之后，选择0xaaaa 服务做以下操作
                /**
                 * 1、打开通知
                 * 2、设置MTU 180
                 * 3、同步当前时间 7e 83 04 06 05 11 60 00 35 40 21 12 11 14 43 1e 7e
                 */
                //0000aaaa-0000-1000-8000-00805f9b34fb   主服务
                //0000bbb0-0000-1000-8000-00805f9b34fb   特性 下发指令
                //0000bbb1-0000-1000-8000-00805f9b34fb   特性 通知
                BluetoothGattService serviceSelect = null;
                BluetoothGattCharacteristic characteristicNotify = null;
//                mCharacteristicBBB0 = null;
                for (BluetoothGattService service : gatt.getServices()) {
                    String uuid = service.getUuid().toString();
//                    Log.e("uuid ", uuid);
                    if (BLUETOOTH_MAIN_SERVICE.equals(uuid)) {
                        serviceSelect = service;
                    }
                }
                if (serviceSelect != null) {
                    for (BluetoothGattCharacteristic characteristic : serviceSelect.getCharacteristics()) {
                        String uuid = characteristic.getUuid().toString();
//                        Log.e("characteristic uuid ", uuid);
                        if (BLUETOOTH_FEATURE_NOTIFY.equals(uuid)) {
                            characteristicNotify = characteristic;
                        }
                        if (BLUETOOTH_FEATURE_ORDER.equals(uuid)) {
                            mCharacteristicBBB0 = characteristic;
                        }
                    }
                    final BluetoothGattCharacteristic characteristicSelect = characteristicNotify;
                    setNotify(bleDevice, characteristicSelect, mCharacteristicBBB0);
                }

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                ObserverManager.getInstance().notifyObserver(bleDevice);
                BroadcastReceiverUtil.onBleDeviceState(MyApplication.getMyApp(), 0, bleDevice);
                onDeleteConnectBluetooth(bleDevice);

                if (status == 0) { // 主动操作断开
                    if (isActiveDisConnected) {
                        LogUtils.e("主动操作设备断开");
                        onBluetoothDeviceSave(bleDevice, true);
                    } else {
                        LogUtils.e("手机蓝牙功能关闭");
                        if (!isHasReconnectionBleDevice(bleDevice)){
                            mBleDeviceReconnectionList.add(bleDevice);
                        }
                        onStartReconnectionBleDevice();
                    }
                } else if (status == 8) { //连接的设备断开
                    if (!isHasReconnectionBleDevice(bleDevice)){
                        mBleDeviceReconnectionList.add(bleDevice);
                    }
                    onStartReconnectionBleDevice();
                }
                LogUtils.e("isActiveDisConnected=" + isActiveDisConnected + ", status=" + status);
            }
        });
    }

    /**
     * 判断重连列表里是否已经有这个设备了
     * @param bleDevice
     * @return
     */
    private boolean isHasReconnectionBleDevice(BleDevice bleDevice){
        boolean isHas = false;
        for (BleDevice device : mBleDeviceReconnectionList){
            if (bleDevice.getName().equals(device.getName())){
                isHas = true;
                break;
            }
        }
        return isHas;
    }

    /**
     * 开始重连设备-被动断开连接的设备
     */
    private void onStartReconnectionBleDevice(){
        if (mBleDeviceReconnectionList.size() > 0){
            BleDevice bleDevice = mBleDeviceReconnectionList.get(0);
            // 每个设备都循环连接一次，避免一个设备一直重连不上，陷入死循环，导致后面需要重连的设备无法重连
            mBleDeviceReconnectionList.remove(bleDevice);
            mBleDeviceReconnectionList.add(bleDevice);
            ViewUtils.runOnUiThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    connect(bleDevice);
                }
            }, 500); // 连接成功后 延迟500ms 下发通知
        }
    }

    /**
     * 断开的蓝牙，从已连接列表中删除
     *
     * @param bleDevice
     */
    @SuppressLint("SetTextI18n")
    public void onDeleteConnectBluetooth(BleDevice bleDevice) {
        BluetoothInfoBean bean = null;
        if (bleDevice != null) {
            for (BluetoothInfoBean model : mConnectBluetoothBeans) {
                if (bleDevice.getMac().equals(model.getMac())) {
                    bean = model;
                    break;
                }
            }
        }
        if (bean != null) {
            mConnectBluetoothBeans.remove(bean);
        }
        onShowBluetoothInfo();
    }

    /**
     * 显示连接蓝牙的信息
     */
    @SuppressLint("SetTextI18n")
    public void onShowBluetoothInfo() {
        if (mConnectBluetoothBeans.size() > 0) {
            if (mBluetoothChange != null){
                mBluetoothChange.onConnectBleDeviceState(mConnectBluetoothBeans.get(0));
            }
        } else {
            isBluetoothConnect = false;
            if (mBluetoothChange != null){
                mBluetoothChange.onConnectBleDeviceState(null);
            }
        }
    }

    /**
     * 保存已连接的设备到数据库/更新已连接过的设备到数据库
     *
     * @param bleDevice 蓝牙信息
     * @param isActivityDisconnect 设备连接是否手动断开           
     */
    private void onBluetoothDeviceSave(BleDevice bleDevice,boolean isActivityDisconnect) {

        boolean isHasDevice = false;
        for (BluetoothModel bean : mBluetoothModels) {
            if (bean.getMac().equals(bleDevice.getMac())) {
                isHasDevice = true;
                break;
            }
        }
        try {
            if (!isHasDevice) {
                //插入设备
                BluetoothModel model = new BluetoothModel();
                model.setMac(bleDevice.getMac());
                model.setAccount(ConstantValue.getAccount());
                model.setName(bleDevice.getName());
                mBluetoothBeanDao.insert(model);
            } else {
                //更新设备
                Map<String, String> updateValue = new HashMap<String, String>();
                updateValue.put("mac",bleDevice.getMac());
                mBluetoothBeanDao.update(updateValue,"disconnected",isActivityDisconnect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ViewUtils.runOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                onBluetoothInit();
            }
        }, 100); // 延迟100ms
    }

    /**
     * 开启通知，解析数据
     *
     * @param bleDevice
     * @param characteristicNotify
     * @param characteristicBBB0
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void setNotify(final BleDevice bleDevice, final BluetoothGattCharacteristic characteristicNotify, final BluetoothGattCharacteristic characteristicBBB0) {
        BleManager.getInstance().notify(
                bleDevice,
                characteristicNotify.getService().getUuid().toString(),
                characteristicNotify.getUuid().toString(),
                new BleNotifyCallback() {

                    @Override
                    public void onNotifySuccess() {
                        ViewUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //设置 MTU 180
                                setMtu(bleDevice, 180);
                            }
                        });
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                        ViewUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LogUtils.e("exception=" + exception.toString());
                            }
                        });
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        ViewUtils.runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                String receive = HexUtil.formatHexString(characteristicNotify.getValue(), false);
                                onShowLogInfo("msg=" + receive);
                                if (!TextUtils.isEmpty(receive)){
                                    if (receive.length() > 0 && receive.startsWith("7e") && (receive.endsWith("7e") || receive.endsWith("7e0d0a"))) {
                                        onParseDeviceReceive(receive, bleDevice, characteristicBBB0);
                                    }else{
                                        onAddContent(receive, bleDevice, characteristicBBB0);
                                    }
                                }
                            }
                        });
                    }
                });
    }

    /**
     * 拼接指令
     * @param receive
     */
    private void onAddContent(String receive, BleDevice bleDevice, BluetoothGattCharacteristic characteristicBBB0){
        if (receive.length() > 8){
            //消息内容大于8 至少包含消息头，消息头2个字节
            if (receive.startsWith("7e") && (!receive.endsWith("7e") || !receive.endsWith("7e0d0a"))) {
                mLogData = receive;
            } else if (!receive.startsWith("7e") && (!receive.endsWith("7e") || !receive.endsWith("7e0d0a"))){
                mLogData = mLogData + receive;
            } else if (!receive.startsWith("7e") && (receive.endsWith("7e") || receive.endsWith("7e0d0a"))) {
                mLogData = mLogData + receive;
                onParseDeviceReceive(mLogData, bleDevice, characteristicBBB0);
            }
        }
    }

    /**
     * 显示打印信息
     * @param message
     */
    private void onShowLogInfo(String message){
        LogUtils.e(message);
    }

    /**
     * APP发送指令给接收机
     *
     * @param bleDevice
     * @param characteristicBBB0
     * @param hex
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void setAppMessageToBlue(BleDevice bleDevice, final BluetoothGattCharacteristic characteristicBBB0, String hex) {
        BleManager.getInstance().write(
                bleDevice,
                characteristicBBB0.getService().getUuid().toString(),
                characteristicBBB0.getUuid().toString(),
                HexUtil.hexStringToBytes(hex),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        ViewUtils.runOnUiThread(new Runnable() {
                            @SuppressLint("LogNotTimber")
                            @Override
                            public void run() {
                                Log.e("kang", "write success, current: " + current
                                        + " total: " + total
                                        + " justWrite: " + HexUtil.formatHexString(justWrite, true));
                            }
                        });
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        ViewUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                addText(txt, exception.toString());
                            }
                        });
                    }
                });
    }

    /**
     * 上报MTU给主机
     *
     * @param bleDevice
     * @param mtu
     */
    @SuppressLint("LongLogTag")
    private void setMtu(BleDevice bleDevice, int mtu) {
        BleManager.getInstance().setMtu(bleDevice, mtu, new BleMtuChangedCallback() {
            @Override
            public void onSetMTUFailure(BleException exception) {
                LogUtils.e(TAG, "onsetMTUFailure" + exception.toString());
            }

            @Override
            public void onMtuChanged(int mtu) {
                LogUtils.e(TAG, "onMtuChanged: " + mtu);
                // 1，把设备加到已连接的列表中去
                // 2，发送连接密钥
                if (bleDevice != null) {
                    BroadcastReceiverUtil.onBleDeviceState(MyApplication.getMyApp(), 1, bleDevice);
                    onShowBluetoothInfo();

                    mBleDevice = bleDevice;
                    onBluetoothDeviceSave(bleDevice, false);

                    // 判断已连接的设备里面是否包含了当前设备
                    boolean isHasDevice = false;
                    for (BluetoothInfoBean bean : mConnectBluetoothBeans) {
                        if (bean.getMac().equals(bleDevice.getMac())) {
                            isHasDevice = true;
                            break;
                        }
                    }
                    if (!isHasDevice){
                        // 添加已连接蓝牙设备
                        BluetoothInfoBean bean = new BluetoothInfoBean();
                        bean.setMac(bleDevice.getMac());
                        bean.setName(bleDevice.getName());
                        mConnectBluetoothBeans.add(bean);
                    }

                    // 判断重连列表里面有没有这个设备，有的话就删除
                    if (isHasReconnectionBleDevice(bleDevice)){
                        mBleDeviceReconnectionList.remove(bleDevice);
                    }

                    setAppMessageToBlue(bleDevice, mCharacteristicBBB0, mPassword); // 发送连接密钥
                    onShowLogInfo("password=" + mPassword);
                }
            }
        });
    }

    /**
     * 解析设备发送过来的数据
     *
     * @param receive
     */
    @SuppressLint("SetTextI18n")
    private void onParseDeviceReceive(String receive, BleDevice bleDevice, BluetoothGattCharacteristic characteristicBBB0) {
        if (receive == null) {
            return;
        }
        if (receive.length() > 0 && receive.endsWith("7e0d0a")) {
            receive = receive.substring(0, receive.length() - 4);
        }
        String receiveBody = BluetoothUtils.MsgEscaping(receive);
        //7e 03 02 03 05 11 60 00 35 40 64 01 00 66 7e  连接蓝牙成功之后 上传自身信息（主到APP）
        // 得到主设备号 6个字节 051160003540  7e030203051160003540640100667e
        if (receiveBody.length() > 0) {
            if (!receiveBody.startsWith("0300")){
                mLogData = "";
            }
            if (receiveBody.startsWith("0302")) { //连接蓝牙成功之后 上传自身信息（主到APP） 得到主号
                mBleDevice = bleDevice;
                isBluetoothConnect = true;
                mBluetoothHostNumber = receiveBody.substring(6, 18); //主号
                // 开始计算电量
                String power = receiveBody.substring(18, 20); // 电量值

                // 更新已连接蓝牙的设备号和电量
                for (BluetoothInfoBean bean : mConnectBluetoothBeans) {
                    if (bleDevice.getMac().equals(bean.getMac())) {
                        bean.setNumber(mBluetoothHostNumber);
                        bean.setPower(BluetoothUtils.onAppendValue_2(power));
                        break;
                    }
                }
                onShowBluetoothInfo();

                //同步手机时间
                //7e 83 04 06 05 11 60 00 35 40 21 12 11 14 43 54 1e 7e
                String timeMsg = "830406" + mBluetoothHostNumber + BluetoothUtils.getTodayDateTime();
                String check = checkCode(timeMsg);
                if (check.equals("7e")) check = "7d02";
                if (check.equals("7d")) check = "7d01";
                // 对消息体内的7e和7d进行转义
                if (timeMsg.contains("7e")) {
                    timeMsg = timeMsg.replace("7e", "7d02");
                }
                if (timeMsg.contains("7d")) {
                    timeMsg = timeMsg.replace("7d", "7d01");
                }

                String sendMobileTime = "7e" + timeMsg + check + "7e";
                onShowLogInfo("同步时间=" + sendMobileTime);
                // 同步APP时间到接收机
                setAppMessageToBlue(bleDevice, characteristicBBB0, sendMobileTime);
            } else if (receiveBody.startsWith("0305")) {
                //7e030503051160003540830400837e 通用回复
                onAnswerMessage(receiveBody, bleDevice);
            } else if (receiveBody.startsWith("0300")) { //位置信息
                parseLocation(receive);
            } else if (receiveBody.startsWith("0308")) { //设备询问是否可以上报日志
                onParseDeviceLogData(receive);
            } else if (receiveBody.startsWith("0310")) { //接收设备日志
                mBleDevice = bleDevice;
                onReceiveDeviceLogContent(receive);
            } else if (receiveBody.startsWith("0306")) { //设备询问是否可以上报日志
                onParseBluetoothUpgrade(receive, bleDevice);
            } else if (receiveBody.startsWith("0313")) { // 蓝牙升级中断后，返回续传包的序号（类似于断点续传）
                BroadcastReceiverUtil.onBleDeviceUpgrade(MyApplication.getMyApp(), 6, receive, bleDevice);
            } else if (receiveBody.startsWith("0314")) { // 设备心跳包
                parseHeartBeat(receive);
            }
        }else{
            mLogData = "";
        }
    }

    /**
     * 接收机回复APP的应答消息
     *
     * @param message 去掉开头和结尾的标识位和校验位的消息体
     */
    private void onAnswerMessage(String message, BleDevice bleDevice) {
        // 消息体
        String messageBody = message.substring(18);
        // 应答ID
        String messageId = messageBody.substring(0, 4);
        // 应答结果
        String result = messageBody.substring(4);
        switch (messageId) {
            case "8304": // 同步时间应答ID
                if (mDeviceModels.size() > 0) {
                    onSendDeviceListToBluetooth();
                }
                break;
            case "8303":
                //应答结果成功才删除已发送的数据
                if(result.equals("00")) {
                    mDeviceImeis.removeAll(mSendDeviceImeis);
                }
                onSendDeviceMessage(mDeviceImeis, true);
                break;
            case "0101":
                BroadcastReceiverUtil.onBleDeviceUpgrade(MyApplication.getMyApp(), 4, "", bleDevice);
                break;
        }
    }

    /**
     * 发送设备列表消息
     */
    private void onSendDeviceListToBluetooth() {
        mDeviceImeis.clear();
        if (mDeviceModels.size() > 0) {
            for (DeviceModel bean : mDeviceModels) {
                mDeviceImeis.add(bean.getImei());
            }
        }
        onSendDeviceMessage(mDeviceImeis, false);
    }

    /**
     * 发送设备列表消息
     *
     * @param devices
     * @param isSecondSend 是否第二次，第N次发送
     */
    private void onSendDeviceMessage(List<String> devices, boolean isSecondSend) {
        if (devices.size() == 0) {
            return;
        }
        mSendDeviceImeis.clear();
        // 分包发送，每次最多发送20个
        if (devices.size() > 20) {
            mSendDeviceImeis.addAll(devices.subList(0, 20));
        } else {
            mSendDeviceImeis.addAll(devices);
        }
        // 拼接设备号
        StringBuilder deviceImei = new StringBuilder();
        for (String str : mSendDeviceImeis) {
            if (str.length() == 11){
                deviceImei.append("0").append(str);
            }
        }
        // 计算设备数量，判断是否需要分包发送
        // 1.不需要分包发送，二进制字节高位补0
        // 2.需要分包发送，分包第一次发送，二进制字节高位补0，分包第二次发送，第N次发送，二进制字节高位补1
        int deviceNumber = mSendDeviceImeis.size();
        // 十进制转二进制字节
        String a = BluetoothUtils.onTypeConversion_2(deviceNumber);
        // 二进制字节，判断补位方式，补位完成，转成十六进制
        String b;
        if (isSecondSend) {
            b = BluetoothUtils.onAppendValue_1(a);
        } else {
            b = BluetoothUtils.onAppendValue_0(a);
        }
        // 判断协议长度
        int byteLength = 1 + 1 + 6 * deviceNumber;
        String strByteLength = BluetoothUtils.onTypeConversion_3(byteLength);
        if (strByteLength.length() == 1) {
            strByteLength = "0" + strByteLength;
        }

        //7E 83 03 3F 05 11 60 00 35 40 01 0A 11 11 11 11 11 11 22 22 22 22 22 22 33 33 33 33 33 33 44 44 44 44 44 44 55 55 55 55 55 55 66 66 66 66 66 66 77 77 77 77 77 77 88 88 88 88 88 88 99 99 99 99 99 99 00 00 00 00 00 00 1E 7E
        String timeMsg = "8303" + strByteLength + mBluetoothHostNumber + "01" + b + deviceImei.toString();
        String check = checkCode(timeMsg);
        if (check.equals("7e")) check = "7d02";
        if (check.equals("7d")) check = "7d01";
        // 对消息体内的7e和7d进行转义
        if (timeMsg.contains("7e")) {
            timeMsg = timeMsg.replace("7e", "7d02");
        }
        if (timeMsg.contains("7d")) {
            timeMsg = timeMsg.replace("7d", "7d01");
        }

        String sendDeviceMessage = "7e" + timeMsg + check + "7e";
        onShowLogInfo("同步白名单=" + sendDeviceMessage);
        // 同步白名单到接收机
        setAppMessageToBlue(mBleDevice, mCharacteristicBBB0, sendDeviceMessage);
    }

    /**
     * 设备询问是否可以上报日志
     *
     * @param data 指令数据
     */
    private void onParseDeviceLogData(String data) {
        String device_number = data.substring(8, 20); // 接收机设备号
        String log_time = data.substring(20, 32); // 日志时间
        // 判断是否已经有了对应设备号的日志了，如果有，就删除，重新接收
        ArrayList<DeviceLogBean> deviceHasBeans = new ArrayList<>();
        for (int i = 0; i < mDeviceLogBeans.size(); i++) {
            if (device_number.equals(mDeviceLogBeans.get(i).getDevice_number())) {
                deviceHasBeans.add(mDeviceLogBeans.get(i));
            }
        }
        // 判断是否已经有了对应设备号的日志了，如果有，就删除，重新接收
        if (deviceHasBeans.size() > 0) {
            mDeviceLogBeans.removeAll(deviceHasBeans);
        }

        // 创建实体类，等待接收日志
        DeviceLogBean bean = new DeviceLogBean();
        bean.setDevice_number(device_number);
        bean.setLog_time(log_time);
        mDeviceLogBeans.add(bean);

        mLogData = "";
        onSendDeviceLogData(device_number, "04");
    }

    /**
     * 下发设备日志接收指令
     *
     * @param device_number 接收机号码
     * @param value         01：向APP上报日志内容 02：删除设备存储日志 04：发送日志内容
     */
    private void onSendDeviceLogData(String device_number, String value) {
        // 7E 83 09 01 05 11 60 00 35 40 01 1E 7E
        String timeMsg = "830901" + device_number + value;
        String check = checkCode(timeMsg);
        if (check.equals("7e")) check = "7d02";
        if (check.equals("7d")) check = "7d01";
        // 对消息体内的7e和7d进行转义
        if (timeMsg.contains("7e")) {
            timeMsg = timeMsg.replace("7e", "7d02");
        }
        if (timeMsg.contains("7d")) {
            timeMsg = timeMsg.replace("7d", "7d01");
        }

        String sendDeviceMessage = "7e" + timeMsg + check + "7e";
        onShowLogInfo("接收设备日志指令=" + sendDeviceMessage);
        // 同步白名单到接收机
        setAppMessageToBlue(mBleDevice, mCharacteristicBBB0, sendDeviceMessage);
    }

    /**
     * 开始接收设备日志数据
     *
     * @param data
     */
    private void onReceiveDeviceLogContent(String data) {
        String device_number = data.substring(8, 20); // 接收机设备号
        String logEnd = data.substring(20, 22); // 日志信息是否结束（0 未结束 1 结束）
        String content = data.substring(22, data.length() - 4); // 消息体内容
        boolean isEnd = BluetoothUtils.onTypeConversion_4(logEnd) == 1; // 日志消息是否结束
        for (DeviceLogBean bean : mDeviceLogBeans) {
            if (bean.getDevice_number().equals(device_number)) {
                bean.addContent(content);
                bean.setComplete(isEnd);
                break;
            }
        }

        if (isEnd) {
            onSendDeviceLogData(device_number, "02");
            DeviceLogBean logBean = null; // 待解析日志数据
            for (DeviceLogBean bean : mDeviceLogBeans) {
                if (bean.getDevice_number().equals(device_number)) {
                    logBean = bean;
                    break;
                }
            }
            if (logBean != null && !TextUtils.isEmpty(logBean.getLog_content().toString())) {
                String parseContent = StringToSixthUtils.decode(logBean.getLog_content().toString());
                onWriterDeviceLogToSDCard(logBean.getDevice_number(), parseContent, logBean.getLog_time());
            }
        }
    }

    /**
     * 解析消息，存储到数据库
     *
     * @param strLocation
     */
    //7e030012051160003322008001cc00262c009a6743001427461500e4097e0d0a
    //030012051160003322008001cc00262c009a6743001427461500e409
    private void parseLocation(String strLocation) {
        //0、消息转义
        String strResult = strLocation.substring(2, strLocation.length() - 2);
        //2、获取消息body // 包含消息头，消息体， 不包含校验位
        // 051160003789 00 4f 0159885e 06cbf344 0123 180722 15 05 64
        String body = strResult.substring(6, strResult.length() - 2);
        //051160003789
        String imei = body.substring(1, 12);
        String alarmType = body.substring(12, 14);
        String state = body.substring(14, 16);
        String time = body.substring(36, 42);
        int rate = Integer.parseInt(body.substring(42, 44), 16);
        int gps = Integer.parseInt(body.substring(44, 46), 16);
        int power = BluetoothUtils.onPowerParse(body.substring(46));

        // 是否是用户设备列表下存在的设备
        boolean isUserDevice = false;
        if (mDeviceModels.size() > 0) {
            for (DeviceModel bean : mDeviceModels) {
                if (bean.getImei().equals(imei)) {
                    isUserDevice = true;
                    break;
                }
            }
        }

        // 存在的设备，才开始处理数据
        if (isUserDevice) {
            // 拼接时分秒
            String Hour = time.substring(0, 2);
            String Min = time.substring(2, 4);
            String Second = time.substring(4);

            // 开始纠偏，判断是否是国内的定位数据点，是的话开启纠偏模式
            String strFormat = DeviceUtils.onLocationType(state);
            char a7 = strFormat.charAt(0); // 定位方式
            if ("0".equals(String.valueOf(a7))) {
                // GPS定位
                long lat = Integer.parseInt(body.substring(16, 24), 16);
                long lon = Integer.parseInt(body.substring(24, 32), 16);
                int speed = Integer.parseInt(body.substring(32, 36), 16);

                double deviceLat = (double) lat / 1000000;
                double deviceLon = (double) lon / 1000000;
                // 判断经纬度是否在中国，在中国的话进行纠偏
                com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(
                        deviceLat, deviceLon);
                if (SpatialRelationUtil.isPolygonContainsPoint(PolygonalArea.getBaiduListPoint(), latLng)) {
                    double[] gpsLatAndLon = GPSConverterNewUtil.toGCJ02Point(deviceLat, deviceLon);
                    lat = (long) (gpsLatAndLon[0] * 1000000);
                    lon = (long) (gpsLatAndLon[1] * 1000000);
                }

                onShowLogInfo("parse --> GPS：" + imei + "," + alarmType + "," + state + "," + lat + "," + lon + "," + speed + ", time=" + time + "," + rate + "," +
                        gps + "," + power);

                if (lat == 0 && lon == 0){
                    return;
                }
                // 判断速度是否大于 180km/h，大于的话，就丢弃， 速度=speed/10
                if (speed > 1800){
                    return;
                }

                // 设备信息
                DeviceModel model = new DeviceModel();
                model.setImei(imei);
                model.setAlarm_type(alarmType);
                model.setLat(lat);
                model.setLon(lon);
                model.setDev_state(state);
                model.setSpeed(speed);
                model.setTime(BluetoothUtils.getTodayDateYear() + " " + Hour + ":" + Min + ":" + Second);
                model.setSignal_rate(rate);
                model.setGps_satellite(gps);
                model.setPower(power);
                model.setCommunication_time(DateUtils.getTodayDateTime());

                // 轨迹信息
                DeviceTrackModel modelTrack = new DeviceTrackModel();
                modelTrack.setImei(imei);
                modelTrack.setAlarm_type(alarmType);
                modelTrack.setLat(lat);
                modelTrack.setLon(lon);
                modelTrack.setDev_state(state);
                modelTrack.setSpeed(speed);
                modelTrack.setTime(BluetoothUtils.getTodayDateYear() + " " + Hour + ":" + Min + ":" + Second);
                modelTrack.setTimestamp(DateUtils.data_5(BluetoothUtils.getTodayDateYear() + " " + Hour + ":" + Min + ":" + Second));
                modelTrack.setSignal_rate(rate);
                modelTrack.setGps_satellite(gps);
                modelTrack.setPower(power);

                try {
                    mDeviceTrackBeanDao.insert(modelTrack);
                    if (mDeviceModels.size() == 0) {
                        mDeviceBeanDao.insert(model);
                    } else {
                        boolean isHasDevice = false; // 设备列表是否已有这个设备的信息
                        for (DeviceModel bean : mDeviceModels) {
                            if (bean.getImei().equals(model.getImei())) {
                                isHasDevice = true;
                                bean.setImei(imei);
                                bean.setAlarm_type(alarmType);
                                bean.setLat(lat);
                                bean.setLon(lon);
                                bean.setDev_state(state);
                                bean.setSpeed(speed);
                                bean.setTime(BluetoothUtils.getTodayDateYear() + " " + Hour + ":" + Min + ":" + Second);
                                bean.setSignal_rate(rate);
                                bean.setGps_satellite(gps);
                                bean.setPower(power);
                                bean.setCommunication_time(DateUtils.getTodayDateTime());

                                // 本地设备名称带到新的数据model里面
                                if (!TextUtils.isEmpty(bean.getDevice_name())) {
                                    model.setDevice_name(bean.getDevice_name());
                                }
                                model.setSimei(bean.getSimei());
                                model.setId(bean.getId());
                                break;
                            }
                        }
                        if (!isHasDevice) {
                            mDeviceBeanDao.insert(model);
                        } else {
                            mDeviceBeanDao.update(model);
                            if (mBluetoothChange != null){
                                mBluetoothChange.onLocationDataParse(model);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // 基站定位
//                long mcc = Integer.parseInt(body.substring(16, 20), 16);
//                long mnc = Integer.parseInt(body.substring(20, 22), 16);
//                long lac = Integer.parseInt(body.substring(22, 26), 16);
//                long cellid = Integer.parseInt(body.substring(26, 34), 16);
//                long signal = Integer.parseInt(body.substring(34, 36), 16);
//
//                // 设备信息
//                DeviceModel model = new DeviceModel();
//                model.setImei(imei);
//                model.setAlarm_type(alarmType);
//                model.setLat(0);
//                model.setLon(0);
//                model.setDev_state(state);
//                model.setSpeed(0);
//                model.setTime(BluetoothUtils.getTodayDateYear() + " " + Hour + ":" + Min + ":" + Second);
//                model.setSignal_rate(rate);
//                model.setGps_satellite(gps);
//                model.setPower(power);
//                model.setCommunication_time(DateUtils.getTodayDateTime());
//
//                if (Utils.isNetworkAvailable(getContext())) {
//                    String bts = mcc + "," + mnc + "," + lac + "," + cellid + "," + signal;
//                    getPresenter().getBaseStationLocation(bts, model);
//                }
            }
        }
    }


    /**
     * 设备心跳包
     * @param beat
     * 7e03140405116000770014151061607e0d0a
     */
    private void parseHeartBeat(String beat) {
        //0、消息转义 031404 051160007700 141510 61 607e0d
        String strResult = beat.substring(2, beat.length() - 2);
        //2、获取消息body // 包含消息头，消息体， 不包含校验位
        // 05116000770014151061607e
        String body = strResult.substring(6, strResult.length() - 2);
        //051160003789
        String imei = body.substring(1, 12);
        String time = body.substring(12, 18);
        int power = BluetoothUtils.onPowerParse(body.substring(18));
        LogUtils.e("power=" + power);

        // 是否是用户设备列表下存在的设备
        boolean isUserDevice = false;
        if (mDeviceModels.size() > 0) {
            for (DeviceModel bean : mDeviceModels) {
                if (bean.getImei().equals(imei)) {
                    isUserDevice = true;
                    break;
                }
            }
        }

        // 存在的设备，才开始处理数据
        if (isUserDevice) {
            // 拼接时分秒
            String Hour = time.substring(0, 2);
            String Min = time.substring(2, 4);
            String Second = time.substring(4);
            // 设备信息
            DeviceModel model = new DeviceModel();
            model.setImei(imei);
            model.setPower(power);
            model.setCommunication_time(BluetoothUtils.getTodayDateYear() + " " + Hour + ":" + Min + ":" + Second);

            try {
                if (mDeviceModels.size()> 0) {
                    boolean isHasDevice = false; // 设备列表是否已有这个设备的信息
                    for (DeviceModel bean : mDeviceModels) {
                        if (bean.getImei().equals(model.getImei())) {
                            isHasDevice = true;
                            bean.setImei(imei);
                            bean.setPower(power);
                            bean.setCommunication_time(BluetoothUtils.getTodayDateYear() + " " + Hour + ":" + Min + ":" + Second);
                            // 本地设备名称带到新的数据model里面
                            if (!TextUtils.isEmpty(bean.getDevice_name())) {
                                model.setDevice_name(bean.getDevice_name());
                            }
                            model.setSimei(bean.getSimei());
                            model.setId(bean.getId());
                            model.setLat(bean.getLat());
                            model.setLon(bean.getLon());
                            if (bean.getDev_state() == null || bean.getDev_state().length() == 0) {
                                model.setDev_state("08"); // 默认设置为静止
                            } else {
                                model.setDev_state(bean.getDev_state());
                            }
                            model.setSpeed(bean.getSpeed());
                            model.setTime(bean.getTime());
                            model.setSignal_rate(bean.getSignal_rate());
                            model.setGps_satellite(bean.getGps_satellite());
                            break;
                        }
                    }
                    if (isHasDevice) {
                        mDeviceBeanDao.update(model);
                        if (mBluetoothChange != null){
                            mBluetoothChange.onLocationDataParse(model);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 验证消息中的检验码
     *
     * @param para
     * @return
     */
    public static String checkCode(String para) {
        int length = para.length() / 2;
        String[] dateArr = new String[length];

        for (int i = 0; i < length; i++) {
            dateArr[i] = para.substring(i * 2, i * 2 + 2);
        }
        String code = "00";
        for (String s : dateArr) {
            code = xor(code, s);
        }
        if (code.length() == 1){
            code = "0" + code;
        }
        return code;
    }

    /**
     * 异或运算，判断校验码是否正确
     *
     * @param strHex_X
     * @param strHex_Y
     * @return
     */
    private static String xor(String strHex_X, String strHex_Y) {
        //将x、y转成二进制形式
        String anotherBinary = Integer.toBinaryString(Integer.valueOf(strHex_X, 16));
        String thisBinary = Integer.toBinaryString(Integer.valueOf(strHex_Y, 16));
        String result = "";
        //判断是否为8位二进制，否则左补零
        if (anotherBinary.length() != 8) {
            for (int i = anotherBinary.length(); i < 8; i++) {
                anotherBinary = "0" + anotherBinary;
            }
        }
        if (thisBinary.length() != 8) {
            for (int i = thisBinary.length(); i < 8; i++) {
                thisBinary = "0" + thisBinary;
            }
        }
        //异或运算
        for (int i = 0; i < anotherBinary.length(); i++) {
            //如果相同位置数相同，则补0，否则补1
            if (thisBinary.charAt(i) == anotherBinary.charAt(i))
                result += "0";
            else {
                result += "1";
            }
        }
//        Log.e("code", result);
        return Integer.toHexString(Integer.parseInt(result, 2));
    }

    /**
     * onDestroy 数据重置处理
     */
    public void onDestroy() {
        if (BleScanner.getInstance().getScanState() != BleScanState.STATE_IDLE) { //扫描中
            BleScanner.getInstance().stopLeScan();
        }
        if (BleManager.getInstance().getBluetoothAdapter() != null) {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                BleManager.getInstance().disconnectAllDevice();
                BleManager.getInstance().destroy();
            }
        }
        mBleDeviceList.clear();
        mDeviceModels.clear();
        mBluetoothModels.clear();
        mConnectBluetoothBeans.clear();
        mDeviceImeis.clear();
        mSendDeviceImeis.clear();
        mDeviceLogBeans.clear();
        mLogData = "";
        isBluetoothConnect = false;
        MyApplication.getMyApp().clearBleDeviceList();
    }

    // ------------------------------------- 数据库操作 --------------------------------------

    /**
     * 从数据库get数据-已配对过的蓝牙设备列表
     *
     * @return
     */
    public List<BluetoothModel> getBluetoothListDataBase() {
        try {
            Map<String, String> map1 = new HashMap<>();
            map1.put(BluetoothModel.ACCOUNT, ConstantValue.getAccount());
//            return (List<BluetoothModel>) mBluetoothBeanDao.query(mBluetoothBeanDao.queryBuilder(map1));

            return (List<BluetoothModel>) mBluetoothBeanDao.queryAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        if (Utils.isNetworkAvailable(MyApplication.getMyApp())) {
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
//            onShowLogInfo(sbOutPut.toString());
            onDeleteDeviceLog(device_number, true);
        } catch (Exception e) {
            onDeleteDeviceLog(device_number, false);
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

    // ------------------------------------- 蓝牙升级相关 ------------------------------------

    /**
     * 查询蓝牙升级
     * @param device_number 设备号
     * @param value 指令 00 无固件更新， 01 有更新，2分钟后再次请求（不需要处理）， 02 升级固件包， 03查询版本内容  4 获取设备版本（该指令只需要设备返回版本即可）
     */
    public void onCheckBluetoothUpgrade(String device_number, String value){
        for (BleDevice bleDevice : mBleDeviceList){
            if (bleDevice.getName().contains(device_number)){
                mBleDevice = bleDevice;
                break;
            }
        }
        if (device_number.length() == 11){
            device_number = "0" + device_number;
        }
        // 7E 83 07 01 05 11 60 00 35 40 02 1E 7E
        String timeMsg = "830701" + device_number + value;
        String check = checkCode(timeMsg);
        if (check.equals("7e")) check = "7d02";
        if (check.equals("7d")) check = "7d01";
        // 对消息体内的7e和7d进行转义
        if (timeMsg.contains("7e")) {
            timeMsg = timeMsg.replace("7e", "7d02");
        }
        if (timeMsg.contains("7d")) {
            timeMsg = timeMsg.replace("7d", "7d01");
        }

        String sendDeviceMessage = "7e" + timeMsg + check + "7e";
        onShowLogInfo("查询蓝牙升级=" + sendDeviceMessage);
        // 同步白名单到接收机
        setAppMessageToBlue(mBleDevice, mCharacteristicBBB0, sendDeviceMessage);
    }

    /**
     * 解析蓝牙升级相关指令
     * @param receive
     */
    public void onParseBluetoothUpgrade(String receive, BleDevice bleDevice){
        // 指令7E 03 06 0F 05 11 60 00 35 40 01 4D 33 31 5F 56 30 2E 32 01 00 01 00 01 00 1E 7E
        // 7e标识位 0306协议命令字 0F协议长度 051160003540主设备号
        // 01是否需要更新版本(0 不需要连接服务器更新  1需要 2升级完成成功（不需要回复）3升级完成失败（不需要回复))
        // 4D33315F56302E32版本内容 0100项目ID 0100芯片ID 0100固件ID 1e校正 7e标识位

        //0、获取消息body // 包含消息头，消息体， 不包含校验位
        String strResult = receive.substring(8, receive.length() - 4);
        // 主机设备名称
        String upgrade_number = strResult.substring(1, 12);
        // 升级结果：成功还是失败
        String upgrade_result = strResult.substring(12, 14);
        // 版本内容，设备型号_当前版本
        String version_info = strResult.substring(14, 30);
        // 项目ID
        String project_id = strResult.substring(30, 34);
        // 芯片ID
        String chip_id = strResult.substring(34, 38);
        // 固件ID
        String firmware_id = strResult.substring(38);
        // 开始执行结果
        switch (upgrade_result){
            case "00":
            case "02":
            case "03":
            case "04":
                BroadcastReceiverUtil.onBleDeviceUpgrade(MyApplication.getMyApp(), 3, receive, bleDevice);
                break;
        }
    }

    /**
     * 通知蓝牙设备，要开始传输蓝牙升级包
     * @param value 下发指令
     * @param device_number 设备号
     */
    public void onSendUpgradeMessage(String value, String device_number){
        // 01011A00050001000000e8030100010001005ce7010018afa30001000000  --> 通知蓝牙设备，要开始传输蓝牙升级包
        // 0301010000  --> 通知蓝牙设备，传输蓝牙升级包完成了
        // 0301010101  --> 通知蓝牙设备，意外断开没有传完
        for (BleDevice bleDevice : mBleDeviceList){
            if (bleDevice.getName().contains(device_number)){
                mBleDevice = bleDevice;
                break;
            }
        }
        onShowLogInfo("通知设备开始蓝牙升级=" + value);
        // 同步白名单到接收机
        setAppMessageToBlue(mBleDevice, mCharacteristicBBB0, value);
    }

    /**
     * 查询蓝牙升级
     * @param value 升级包内容，每次传输150字节内容
     * @param device_number 设备号
     */
    public void onSendUpgradeContent(String value, String device_number,int packageIndex) {
        for (BleDevice bleDevice : mBleDeviceList){
            if (bleDevice.getName().contains(device_number)){
                mBleDevice = bleDevice;
                break;
            }
        }
        if (device_number.length() == 11){
            device_number = "0" + device_number;
        }
        // 计算升级包内容的长度，补足2字节，不足的后面补0
        int length = value.length() / 2 + 2; // 新增两个字节，包序号
        String valueLength = BluetoothUtils.onTypeConversion_3(length);
        if (length > 15) {
            valueLength = valueLength + "00";
        } else {
            valueLength = "0" + valueLength + "00";
        }
        String index = BluetoothUtils.onTypeConversion_3(packageIndex); //总包个数+1 并转16进制

        int difference = 4 - index.length(); // 需要补多少位
        for (int i = 0; i < difference; i++) {
            index = "0" + index;
        }
        LogUtils.e("index=" + index);
        // 开始拼接命令
        String sendMessage = device_number + "0201" + valueLength + index + value;
        onShowLogInfo("蓝牙升级包内容=" + sendMessage);
        // 同步白名单到接收机
        setAppMessageToBlue(mBleDevice, mCharacteristicBBB0, sendMessage);
    }

    // ------------------------------------- 回调接口 ------------------------------------

    public void setBluetoothChange(onBluetoothChange change){
        this.mBluetoothChange = change;
    }

    public interface onBluetoothChange{

        /**
         * 已连接的蓝牙设备的连接状态
         * @param bluetoothInfoBean
         */
        void onConnectBleDeviceState(BluetoothInfoBean bluetoothInfoBean);

        /**
         * 定位数据解析，更新
         * @param deviceModel
         */
        void onLocationDataParse(DeviceModel deviceModel);

    }

}
