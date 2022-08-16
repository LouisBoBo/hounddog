package com.slxk.hounddog.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 设备信息类-轨迹
 * Created by Administrator on 2017\6\29 0029.
 */


@DatabaseTable(tableName = "db_device_track_bean")
public class DeviceTrackModel {

    /**
     * 主键key，imei号
     */
    public static final String IMEI = "imei";

    @DatabaseField(generatedId = true)
    private int id;
    //设备号
    @DatabaseField(columnName = "imei")
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
    //时间-年月日 时分秒
    @DatabaseField(columnName = "time")
    private String time;
    //定位时间的时间戳
    @DatabaseField(columnName = "timestamp")
    private long timestamp;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
                ", timestamp=" + timestamp +
                ", signal_rate=" + signal_rate +
                ", gps_satellite=" + gps_satellite +
                ", power=" + power +
                ", dev_state='" + dev_state + '\'' +
                '}';
    }
}
