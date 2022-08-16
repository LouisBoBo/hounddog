package com.slxk.hounddog.mvp.model.bean;

import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.slxk.hounddog.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * 地图页实时轨迹 - 高德地图
 */
public class RealTimeTrackBaiduBean {

    private String imei; // 设备imei号
    private boolean isRealTimeTrack = false; // 实时轨迹开关是否打开
    private Polyline polyline; // 轨迹线
    private ArrayList<LatLng> followLatLists; // 轨迹点
    private int colorId = R.color.color_00C8F8; // 颜色id

    public void setFollowLatAndLon(LatLng latLng){
        if (followLatLists == null){
            followLatLists = new ArrayList<>();
        }
        followLatLists.add(latLng);
    }

    public void clearFollowLatAndLon(){
        if (followLatLists != null){
            followLatLists.clear();
            followLatLists = null;
        }
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public boolean isRealTimeTrack() {
        return isRealTimeTrack;
    }

    public void setRealTimeTrack(boolean realTimeTrack) {
        isRealTimeTrack = realTimeTrack;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public ArrayList<LatLng> getFollowLatLists() {
        return followLatLists;
    }

    public void setFollowLatLists(ArrayList<LatLng> followLatLists) {
        this.followLatLists = followLatLists;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    @NotNull
    public String toString(){
        return "imei=" + imei
                + ", isRealTimeTrack=" + isRealTimeTrack
                + ", size=" + followLatLists.size();
    }

}
