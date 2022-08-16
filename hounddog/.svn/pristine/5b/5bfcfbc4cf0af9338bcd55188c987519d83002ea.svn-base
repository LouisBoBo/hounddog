package com.slxk.hounddog.mvp.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 手机蓝牙开关状态监听，广播监听蓝牙状态
 */
public class BlueToothStateReceiver extends BroadcastReceiver {

    public static int DEFAULT_VALUE_BULUETOOTH = 1000;
    public OnBlueToothStateListener onBlueToothStateListener;

    @SuppressLint("LogNotTimber")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, DEFAULT_VALUE_BULUETOOTH);
            switch (state) {
                case BluetoothAdapter.STATE_OFF://蓝牙已关闭
                    onBlueToothStateListener.onStateOff();
                    break;
                case BluetoothAdapter.STATE_ON://蓝牙已开启
                    onBlueToothStateListener.onStateOn();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON://蓝牙正在打开
                    onBlueToothStateListener.onStateTurningOn();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF://蓝牙正在关闭
                    onBlueToothStateListener.onStateTurningOff();
                    break;

                default:
                    Log.e("BlueToothError", "蓝牙状态未知");
                    break;
            }
        }
    }

    public interface OnBlueToothStateListener {
        void onStateOff();

        void onStateOn();

        void onStateTurningOn();

        void onStateTurningOff();
    }

    public void setOnBlueToothStateListener(OnBlueToothStateListener onBlueToothStateListener) {
        this.onBlueToothStateListener = onBlueToothStateListener;
    }

}
