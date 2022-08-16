package com.slxk.hounddog.mvp.utils;

import android.location.Location;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;


/**
 * Google Map Public Class
 * Created by Administrator on 2017\12\23 0023.
 */
public class GoogleMapUtils {

    /**
     * 经纬度转换
     */
    public static LatLng getGoogleLocation(String lat, String lon) {
        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lon)) {
            return null;
        } else {
            return new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        }
    }

    /**
     * 根据经纬度计算需要偏转的角度
     */
    public static float getGoogleRotate(LatLng curPos, LatLng nextPos) {
        double x1 = curPos.latitude;
        double x2 = nextPos.latitude;
        double y1 = curPos.longitude;
        double y2 = nextPos.longitude;
        float rotate = (float) (Math.atan2(y2 - y1, x2 - x1) / Math.PI * 180);
        return rotate;
    }

    /**
     * 计算距离
     */
    public static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
    }

    /// <summary> 通过缩放级别获取Google地图的缩放比例。
/// </summary>
    public static double GetScale(int zoom) {
        //region Google map (in meters/pixel) 的比例值。scale =π R / (256 * 2^(z-1)),π = 3.1415926536, R = 6378137(地球半径)。

        switch (zoom) {
            case 0:
                return 156543.03392;
            case 1:
                return 78271.51696;
            case 2:
                return 39135.75848;
            case 3:
                return 19567.87924;
            case 4:
                return 9783.93962;
            case 5:
                return 4891.96981;
            case 6:
                return 2445.98490;
            case 7:
                return 1222.99245;
            case 8:
                return 611.49622;
            case 9:
                return 305.74811;
            case 10:
                return 152.87405;
            case 11:
                return 76.43702;
            case 12:
                return 38.21851;
            case 13:
                return 19.10925;
            case 14:
                return 9.55462;
            case 15:
                return 4.77731;
            case 16:
                return 2.38865;
            case 17:
                return 1.19432;
            case 18:
                return 0.59716;
            case 19:
                return 0.29858;
            default:
                return 19.10925;//默认的话按13算
        }

    }

    /**
     * 地图放大缩小
     * enlarge  是否放大
     * target   地图中心点
     * zoom     地图比例
     */
    public static CameraUpdate setScaleZoomGoogle(boolean enlarge, LatLng target, float zoom, ImageView ivenlarge, ImageView ivnarrow) {
        float mZoom = zoom;
        if (enlarge) {
            // 放大
            if (zoom >= 19) {
                mZoom = 19;
                ivenlarge.setEnabled(false);
            } else {
                mZoom++;
                ivenlarge.setEnabled(true);
                ivnarrow.setEnabled(true);
            }
            return CameraUpdateFactory.newLatLngZoom(target, mZoom);
        } else {
            //缩小
            if (zoom <= 4) {
                mZoom = 4;
                ivnarrow.setEnabled(false);
            } else {
                mZoom--;
                ivenlarge.setEnabled(true);
                ivnarrow.setEnabled(true);
            }
            return CameraUpdateFactory.newLatLngZoom(target, mZoom);
        }
    }

    /**
     * 计算缩放级别
     * @param range
     * @return
     */
    public static int getZoom(int range) {

        if (range <= 2000) {
            return 14;
        } else if (range <= 5000) {
            return 13;
        } else if (range <= 10000) {
            return 12;
        } else if (range <= 20000) {
            return 10;
        } else if (range <= 50000) {
            return 9;
        } else if (range <= 80000) {
            return 8;
        } else if (range <= 150000) {
            return 7;
        } else if (range <= 300000) {
            return 6;
        } else {
            return 5;
        }
    }


}
