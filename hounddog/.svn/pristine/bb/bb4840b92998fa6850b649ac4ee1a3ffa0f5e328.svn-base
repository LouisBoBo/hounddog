package com.slxk.hounddog.mvp.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;

import com.blankj.utilcode.util.LogUtils;

/**
 * 手机蓝牙开关监听工具类
 */
public class BlueToothStateUtil {

    private static BlueToothStateUtil INSTANCE;
    BlueToothStateReceiver blueToothStateReceiver;

    public static synchronized BlueToothStateUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BlueToothStateUtil();
        }
        return INSTANCE;
    }

    //注册广播接收器，用于监听蓝牙状态变化
    public void registerBlueToothStateReceiver(Activity activity) {
        //注册广播，蓝牙状态监听
        blueToothStateReceiver = new BlueToothStateReceiver();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        activity.registerReceiver(blueToothStateReceiver, filter);
        blueToothStateReceiver.setOnBlueToothStateListener(new BlueToothStateReceiver.OnBlueToothStateListener() {
            @Override
            public void onStateOff() {
                //do something
                LogUtils.e("蓝牙断开了");
            }

            @Override
            public void onStateOn() {
                //do something
                LogUtils.e("蓝牙开启了");
            }

            @Override
            public void onStateTurningOn() {
                //do something
            }

            @Override
            public void onStateTurningOff() {
                //do something
            }
        });
    }

    public void unregisterBlueToothStateReceiver(Activity activity) {
        activity.unregisterReceiver(blueToothStateReceiver);
    }

}
