package com.slxk.hounddog.mvp.model.bean;

/**
 * 设备位置信息等
 */
public class DeviceLocationInfoBean {

    /**
     * Time : 2020-01-17 10:41:10
     * Lon : 114.03361124
     * Lat : 22.64252376
     * Addr : 114.033611,22.642524
     * Type : 4
     * Devspeed : 0.000
     * Direction : 196
     */

    private long time = 0;// 定位时间
    private long lon = 0;// 经度
    private long lat = 0;// 纬度
    private String type = "e_gps";//定位类型 基站 GPS WIFI 静止基站 静止GPS 静止WIFI
    private int speed = 0;// 速度
    private float direction = 0;// 方向
    private int distance = 0; // 里程


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
