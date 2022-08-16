package com.slxk.hounddog.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 设备信息类-账号下的设备列表
 * Created by Administrator on 2017\6\29 0029.
 */


@DatabaseTable(tableName = "db_device_bean")
public class DeviceModel implements Serializable {

    /**
     * 主键key，imei号
     */
    public static final String IMEI = "imei";

    @DatabaseField(generatedId = true)
    private int id;
    //设备号
    @DatabaseField(columnName = "imei", unique = true)
    private String imei;
    //报警类型
    @DatabaseField(columnName = "alarm_type")
    private String alarm_type;
    //纬度
    @DatabaseField(columnName = "lat")
    private long  lat;
    //经度
    @DatabaseField(columnName = "lon")
    private long  lon;
    //速度
    @DatabaseField(columnName = "speed")
    private int  speed;
    //定位时间-年月日 时分秒
    @DatabaseField(columnName = "time")
    private String time;
    //信号值
    @DatabaseField(columnName = "signal_rate")
    private int  signal_rate;
    //卫星数
    @DatabaseField(columnName = "gps_satellite")
    private int  gps_satellite;
    //电量
    @DatabaseField(columnName = "power")
    private int  power;
    //状态标志位
    @DatabaseField(columnName = "dev_state")
    private String dev_state;
    //设备名称
    @DatabaseField(columnName = "device_name")
    private String device_name;
    //设备simei号
    @DatabaseField(columnName = "simei")
    private String simei;
    //通讯时间-手机当前时间
    @DatabaseField(columnName = "communication_time")
    private String communication_time;

    @DatabaseField(columnName = "sgid")
    private String sgid;


    private String location_type; // 定位类型，网络模式下使用
    private String color; // 颜色值代码
    private String state; // 设备状态-在线模式下使用
    private float distance; // 设备与手机的相对距离
    private boolean is_select; // 设备是否勾选

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(String alarm_type) {
        this.alarm_type = alarm_type;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSignal_rate() {
        return signal_rate;
    }

    public void setSignal_rate(int signal_rate) {
        this.signal_rate = signal_rate;
    }

    public int getGps_satellite() {
        return gps_satellite;
    }

    public void setGps_satellite(int gps_satellite) {
        this.gps_satellite = gps_satellite;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getDev_state() {
        return dev_state;
    }

    public void setDev_state(String dev_state) {
        this.dev_state = dev_state;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getSimei() {
        return simei;
    }

    public void setSimei(String simei) {
        this.simei = simei;
    }

    public String getCommunication_time() {
        return communication_time;
    }

    public void setCommunication_time(String communication_time) {
        this.communication_time = communication_time;
    }

    public boolean isIs_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public String getSgid() {
        return sgid;
    }

    public void setSgid(String sgid) {
        this.sgid = sgid;
    }

    @Override
    public String toString() {
        return "DeviceModel{" +
                "id=" + id +
                ", imei='" + imei + '\'' +
                ", alarm_type='" + alarm_type + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", speed=" + speed +
                ", time=" + time +
                ", signal_rate=" + signal_rate +
                ", gps_satellite=" + gps_satellite +
                ", power=" + power +
                ", dev_state='" + dev_state + '\'' +
                ", device_name='" + device_name + '\'' +
                ", simei='" + simei + '\'' +
                ", sgid='" + sgid + '\'' +
                '}';
    }
}
