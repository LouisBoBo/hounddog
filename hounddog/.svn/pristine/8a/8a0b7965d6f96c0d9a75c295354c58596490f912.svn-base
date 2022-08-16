package com.slxk.hounddog.mvp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * 手机信息工具类，获取手机运营商，有无sim卡等信息
 */
public class PhoneSimInfoUtils {

    /**
     * 获取蜂窝网络运营商
     * @param context
     * @return
     */
    public static String getSubscriptionSimType(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperator();
    }

    /**
     * 获取设备拨号运营商
     *
     * @return ["中国电信CTCC":3]["中国联通CUCC:2]["中国移动CMCC":1]["other":0]["无sim卡":-1]
     */
    public static int getSubscriptionOperatorType(Context context) {
        int opeType = -1;
        // No sim
        if (!hasSim(context)) {
            return opeType;
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm.getSimOperator();
        if (operator.startsWith("460")){
            opeType = 1; // 中国内地
        }else{
            opeType = 0;
        }

        // 中国联通
//        if ("46001".equals(operator) || "46006".equals(operator) || "46009".equals(operator) || "46010".equals(operator)) {
//            opeType = 2;
//            // 中国移动
//        } else if ("46000".equals(operator) || "46002".equals(operator) || "46004".equals(operator) || "46007".equals(operator) || "46020".equals(operator)) {
//            opeType = 1;
//            // 中国电信
//        } else if ("46003".equals(operator) || "46005".equals(operator) || "46011".equals(operator)) {
//            opeType = 3;
//        } else {
//            opeType = 0;
//        }
        return opeType;
    }

    /**
     * 获取设备蜂窝网络运营商
     *
     * @return ["中国电信CTCC":3]["中国联通CUCC:2]["中国移动CMCC":1]["other":0]["无sim卡":-1]["数据流量未打开":-2]
     */
    public static int getCellularOperatorType(Context context) {
        int opeType = -1;
        // No sim
        if (!hasSim(context)) {
            return opeType;
        }
        // Check cellular operator
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm.getSimOperator();
        // 中国联通
        if ("46001".equals(operator) || "46006".equals(operator) || "46009".equals(operator)) {
            opeType = 2;
            // 中国移动
        } else if ("46000".equals(operator) || "46002".equals(operator) || "46004".equals(operator) || "46007".equals(operator)) {
            opeType = 1;
            // 中国电信
        } else if ("46003".equals(operator) || "46005".equals(operator) || "46011".equals(operator)) {
            opeType = 3;
        } else {
            opeType = 0;
        }
        return opeType;
    }

    /**
     * 判断数据流量开关是否打开
     *
     * @param context
     * @return
     */
    @SuppressLint("DiscouragedPrivateApi")
    public static boolean isMobileDataEnabled(Context context) {
        try {
            Method method = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return (Boolean) method.invoke(connectivityManager);
        } catch (Throwable t) {
            Log.d("isMobileDataEnabled", "Check mobile data encountered exception");
            return false;
        }
    }

    /**
     * 检查手机是否有sim卡
     */
    public static boolean hasSim(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
        }else{
            String operator = tm.getSimOperator();
            return !TextUtils.isEmpty(operator);
        }
    }

}
