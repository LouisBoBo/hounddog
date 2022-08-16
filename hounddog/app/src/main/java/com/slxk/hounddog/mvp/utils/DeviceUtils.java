package com.slxk.hounddog.mvp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.ColorBean;
import com.slxk.hounddog.mvp.model.bean.ContactBean;
import com.slxk.hounddog.mvp.model.bean.DeviceLocationInfoBean;
import com.slxk.hounddog.mvp.model.bean.LocationModeTime;
import com.slxk.hounddog.mvp.model.bean.PenetrateParamResultBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备数据解析工具类
 */
public class DeviceUtils {

    /**
     * 解析设备更多信息
     * @param locInfo
     * @return
     */
    public static DeviceLocationInfoBean parseDeviceData(String locInfo){
        DeviceLocationInfoBean bean;
        if (TextUtils.isEmpty(locInfo)){
            bean = new DeviceLocationInfoBean();
        }else{
            bean = new Gson().fromJson(locInfo,
                    DeviceLocationInfoBean.class);
        }
        return bean;
    }

    /**
     * 解析设备更多信息
     * @param location_mode
     * @return
     */
    public static ArrayList<LocationModeTime> parseLocationModeTime(String location_mode){
        ArrayList<LocationModeTime> beans;
        if (TextUtils.isEmpty(location_mode)){
            beans = new ArrayList<>();
        }else{
            beans = new Gson().fromJson(location_mode, new TypeToken<List<LocationModeTime>>(){}.getType());
        }
        return beans;
    }

    /**
     * 解析服务商联系方式
     * @param contact
     * @return
     */
    public static ContactBean parseContactData(String contact){
        ContactBean bean;
        if (TextUtils.isEmpty(contact)){
            bean = new ContactBean();
        }else{
            if (contact.contains("{") && contact.contains("}")){
                bean = new Gson().fromJson(contact, ContactBean.class);
            }else{
                bean = new ContactBean();
            }
        }
        return bean;
    }

    /**
     * 解析透传指令历史回复信息
     * @param param
     * @return
     */
    public static PenetrateParamResultBean paramPenetrateParamData(String param){
        PenetrateParamResultBean bean;
        if (TextUtils.isEmpty(param)){
            bean = new PenetrateParamResultBean();
        }else{
            bean = new Gson().fromJson(param, PenetrateParamResultBean.class);
        }
        return bean;
    }

    /**
     * 设备定位类型
     * @param type
     * @return
     */
    public static String onLocationType(Context context, String type){
        String str = "";
        switch (type){
            case "001":
                str = context.getString(R.string.location_gps);
                break;
            case "010":
                str = context.getString(R.string.location_beidou);
                break;
            case "011":
                str = context.getString(R.string.location_gps_beidou);
                break;
            case "100":
                str = context.getString(R.string.location_galileo);
                break;
            case "101":
                str = context.getString(R.string.location_gps_galileo);
                break;
            case "110":
                str = context.getString(R.string.location_beidou_galileo);
                break;
            case "111":
                str = context.getString(R.string.location_gps_beidou_galileo);
                break;
        }
        return str;
    }

    /**
     * 设备报警类型
     * @param alarm
     * @return
     */
    public static String onAlarmType(Context context, String alarm){
        String alarmStr = "";
        switch (alarm){
            case "00":
                alarmStr = context.getString(R.string.alarm_type_0);
                break;
            case "01":
                alarmStr = context.getString(R.string.alarm_type_1);
                break;
            case "02":
                alarmStr = context.getString(R.string.alarm_type_2);
                break;
            case "03":
                alarmStr = context.getString(R.string.alarm_type_3);
                break;
            case "04":
                alarmStr = context.getString(R.string.alarm_type_4);
                break;
            case "05":
                alarmStr = context.getString(R.string.alarm_type_5);
                break;
            case "06":
                alarmStr = context.getString(R.string.alarm_type_6);
                break;
            case "07":
                alarmStr = context.getString(R.string.alarm_type_7);
                break;
            case "08":
                alarmStr = context.getString(R.string.alarm_type_8);
                break;
            case "09":
                alarmStr = context.getString(R.string.alarm_type_9);
                break;
            case "10":
                alarmStr = context.getString(R.string.alarm_type_10);
                break;
            case "11":
                alarmStr = context.getString(R.string.alarm_type_11);
                break;
            case "12":
                alarmStr = context.getString(R.string.alarm_type_12);
                break;
            case "13":
                alarmStr = context.getString(R.string.alarm_type_13);
                break;
            case "14":
                alarmStr = context.getString(R.string.alarm_type_14);
                break;
            case "15":
                alarmStr = context.getString(R.string.alarm_type_15);
                break;
        }
        return alarmStr;
    }

    /**
     * 状态标志（4F）0100 1111
     *
     * 位
     *
     * 7     0： GPS ； 1:  基站  定位方式
     *
     * 6     0：静止；1：运动
     *
     * 5     0：未使用Galileo卫星定位；1：使用Galileo卫星定位
     *
     * 4     0：未使用北斗卫星定位；   1：使用北斗卫星定位
     *
     * 3     0：未使用GPS卫星定位；    1：使用GPS卫星定位
     *
     * 2     0：东经；  1：西经
     *
     * 1     1：北纬；  0：南纬
     *
     * 0    0：未定位； 1：定位
     */

    public static String onLocationType(String str){
        //十六进制转成二进制
        String bin = Integer.toBinaryString(Integer.valueOf(str, 16));
        DecimalFormat df = new DecimalFormat("00000000");
        return df.format(Integer.parseInt(bin));
    }

    /**
     * 判断设备是否离线, 大于6分钟，判定为离线
     * @param locationTime 设备最后定位时间
     * @return
     */
    public static boolean isDeviceOnOff(String locationTime){
        if (TextUtils.isEmpty(locationTime)){
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis(); // 当前时间
        long time = DateUtils.data_5(locationTime);
        return currentTimeMillis - time > 360000;
    }

    /**
     * 设备离线时长
     * @param locationTime 设备最后定位时间
     * @return
     */
    public static String onDeviceOnOffTime(Context context, String locationTime){
        if (TextUtils.isEmpty(locationTime)){
            return "";
        }
        long currentTimeMillis = System.currentTimeMillis(); // 当前时间
        long time = DateUtils.data_5(locationTime);
        return onConvertTimemills(context, currentTimeMillis - time);
    }

    /**
     * 离线时长
     *
     * @param comTime 最后通讯时间
     * @param context
     * @return
     */
    public static String onDeviceOnOffTime_2(Context context, long comTime) {
        if (comTime == 0){
            return "";
        }else{
            long staticTime = comTime * 1000;
            return onConvertTimemills(context, staticTime);
        }
    }

    /**
     * 离线时长
     *
     * @param comTime 最后通讯时间
     * @param context
     * @return
     */
    public static String getOfflineTime(long comTime, Context context) {
        if (comTime == 0){
            return "";
        }else{
            long staticTime = comTime * 1000;
            return onConvertTimemills(context, staticTime);
        }
    }

    /**
     * 在线时长
     *
     * @param time    最后定位时间
     * @param context
     * @return
     */
    public static String getParkingTime(long time, Context context) {
        if (time == 0){
            return "";
        }else{
            long staticTime = time * 1000;
            return onConvertTimemills(context, staticTime);
        }
    }

    /**
     * 静止时长
     *
     * @param time    最后定位时间
     * @param context
     * @return
     */
    public static String getParkingStaticTime(long time, Context context) {
        if (time == 0){
            return "";
        }else{
            long staticTime = time * 1000;
            return onConvertTimemills(context, staticTime);
        }
    }

    /**
     * 将日期格式的字符串转换为长整型
     *
     * @param diff
     * @return
     */
    private static String onConvertTimemills(Context context, long diff) {
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        return days + context.getString(R.string.day)
                + hours + context.getString(R.string.hour_two)
                + minutes + context.getString(R.string.minute_two);
    }

    /**
     * 定位类型，型号强度
     */
    public static void setLocationSignalData(int signal_rate, ImageView ivSignal, TextView tvSignal) {
        if (signal_rate <= 6) {
            ivSignal.setImageResource(R.drawable.icon_gsm_20);
        } else if (signal_rate <= 12) {
            ivSignal.setImageResource(R.drawable.icon_gsm_40);
        } else if (signal_rate <= 18) {
            ivSignal.setImageResource(R.drawable.icon_gsm_60);
        } else if (signal_rate <= 24) {
            ivSignal.setImageResource(R.drawable.icon_gsm_80);
        } else if (signal_rate <= 31) {
            ivSignal.setImageResource(R.drawable.icon_gsm_100);
        } else {
            ivSignal.setImageResource(R.drawable.icon_gsm_100);
        }
        if (signal_rate > 0){
            tvSignal.setText(String.valueOf(signal_rate));
        }else{
            tvSignal.setText("1");
        }
    }

    /**
     * 根据剩余电量设置电量图标
     *
     * @param power
     * @param ivEle
     */
    @SuppressLint("SetTextI18n")
    public static void setElectricImageData(int power, ImageView ivEle, TextView tvEle) {
        if (power <= 5) {
            ivEle.setImageResource(R.drawable.icon_battery_0);
        } else if (power <= 25) {
            ivEle.setImageResource(R.drawable.icon_battery_20);
        } else if (power <= 50) {
            ivEle.setImageResource(R.drawable.icon_battery_40);
        } else if (power <= 75) {
            ivEle.setImageResource(R.drawable.icon_battery_60);
        } else if (power <= 95) {
            ivEle.setImageResource(R.drawable.icon_battery_80);
        } else {
            ivEle.setImageResource(R.drawable.icon_battery_100);
        }
        tvEle.setText(power + "%");
    }

    /**
     * 计算百度经纬度
     * @param lat
     * @param lon
     * @return
     */
    public static LatLng getBaiduLatLng(long lat, long lon){
        return new LatLng((double) lat / 1000000, (double) lon / 1000000);
    }

    /**
     * 计算高德经纬度
     * @param lat
     * @param lon
     * @return
     */
    public static com.amap.api.maps.model.LatLng getAmapLatLng(long lat, long lon){
        return new com.amap.api.maps.model.LatLng((double) lat / 1000000, (double) lon / 1000000);
    }

    /**
     * 计算谷歌经纬度
     * @param lat
     * @param lon
     * @return
     */
    public static com.google.android.gms.maps.model.LatLng getGoogleLatLng(long lat, long lon){
        return new com.google.android.gms.maps.model.LatLng((double) lat / 1000000, (double) lon / 1000000);
    }

    /**
     * 颜色池
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    public static ArrayList<ColorBean> getColorList(Context context){
        ArrayList<ColorBean> colorLists = new ArrayList<>();
        colorLists.add(new ColorBean(R.color.color_FF4A21, "#FF4A21"));
        colorLists.add(new ColorBean(R.color.color_00C8F8, "#00C8F8"));
        colorLists.add(new ColorBean(R.color.color_318AFE, "#318AFE"));
        colorLists.add(new ColorBean(R.color.hotpink, "#FF69B4"));
        colorLists.add(new ColorBean(R.color.tomato, "#FF6347"));
        colorLists.add(new ColorBean(R.color.color_00B4B7, "#00B4B7"));
        colorLists.add(new ColorBean(R.color.deeppink, "#FF1493"));
        colorLists.add(new ColorBean(R.color.red, "#FF0000"));
        colorLists.add(new ColorBean(R.color.magenta, "#FF00FF"));
        colorLists.add(new ColorBean(R.color.indianred, "#CD5C5C"));
        colorLists.add(new ColorBean(R.color.mediumvioletred, "#C71585"));
        colorLists.add(new ColorBean(R.color.firebrick, "#B22222"));
        colorLists.add(new ColorBean(R.color.mediumorchid, "#BA55D3"));
        colorLists.add(new ColorBean(R.color.purple, "#800080"));
        colorLists.add(new ColorBean(R.color.sienna, "#A0522D"));
        colorLists.add(new ColorBean(R.color.brown, "#A52A2A"));
        colorLists.add(new ColorBean(R.color.cornflowerblue, "#6495ED"));
        colorLists.add(new ColorBean(R.color.orangered, "#FF4500"));
        colorLists.add(new ColorBean(R.color.darkviolet, "#9400D3"));
        colorLists.add(new ColorBean(R.color.mediumpurple, "#9370DB"));
        colorLists.add(new ColorBean(R.color.dodgerblue, "#1E90FF"));
        colorLists.add(new ColorBean(R.color.darkred, "#8B0000"));
        colorLists.add(new ColorBean(R.color.lightskyblue, "#87CEFA"));
        colorLists.add(new ColorBean(R.color.salmon, "#FA8072"));
        colorLists.add(new ColorBean(R.color.darkmagenta, "#8B008B"));
        colorLists.add(new ColorBean(R.color.mediumslateblue, "#7B68EE"));
        colorLists.add(new ColorBean(R.color.royalblue, "#4169E1"));
        colorLists.add(new ColorBean(R.color.midnightblue, "#191970"));
        colorLists.add(new ColorBean(R.color.blue, "#0000FF"));
        colorLists.add(new ColorBean(R.color.navy, "#000080"));
        return colorLists;
    }

    /**
     * 转换定位模式的值为显示内容
     *
     * @param value
     * @return
     */
    public static String onLocationModeTimeShow(Context context, String value) {
        if (TextUtils.isEmpty(value)){
            return "";
        }
        int time = Integer.parseInt(value);
        String showTime = "";
        if (time >= 3600){
            int hours = time / 60 / 60;
            showTime = hours + context.getString(R.string.hour_two);
        }else if (time >= 60){
            int min = time / 60;
            showTime = min + context.getString(R.string.minute_two);
        }else{
            showTime = time + context.getString(R.string.second);
        }
        return showTime;
    }

    /**
     * 文字的显示颜色
     * @param power
     * @return
     */
    public static int getDeviceNameColor(int power){
        int color = R.color.color_000000;
        if (power < 20){
            color = R.color.color_FF4545;
        }
        return color;
    }

    /**
     * 设备与当前手机的距离
     * @param distance
     * @return
     */
    public static String getDeviceDistance(Context context, float distance){
        String strDistance = "";
        if (distance > 10000) {
            double distance_km = (double) distance / 1000;
            strDistance = Utils.formatValue_2(distance_km) + context.getString(R.string.kilometer);
        } else if (distance > 10) {
            strDistance = ((int) distance) + context.getString(R.string.meter);
        }else{
            strDistance = context.getString(R.string.nearby);
        }
        return "[" + strDistance + "]";
    }

    /**
     * 设备与当前手机的距离
     * @param distance
     * @return
     */
    public static String getDeviceDistance(Context context, double distance){
        String strDistance = "";
        if (distance > 10000) {
            double distance_km = distance / 1000;
            strDistance = Utils.formatValue_2(distance_km) + context.getString(R.string.kilometer);
        } else if (distance > 10) {
            strDistance = ((int) distance) + context.getString(R.string.meter);
        }else{
            strDistance = context.getString(R.string.nearby);
        }
        return "[" + strDistance + "]";
    }

}
