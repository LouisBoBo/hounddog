package com.slxk.hounddog.mvp.utils;

import com.baidu.mapapi.utils.SpatialRelationUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 经纬度转换工具类
 */
public class GpsUtils {

    private final static double a = 6378245.0;
    private final static double pi = 3.1415926535897932384626;
    private final static double ee = 0.00669342162296594323;

    /**
     * 计算地球上任意两点(经纬度)距离
     *
     * @param lat1
     *            第一点纬度
     * @param lng1
     *            第一点经度
     * @param lat2
     *            第二点纬度
     * @param lng2
     *            第二点经度
     * @return 返回距离 单位：米
     */
    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        double a, b, R;
        R = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (lng1 - lng2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
        return d;
    }

    /**
     * Description: WGS-84 to GCJ-02 <BR>
     *
     * @author dsn
     * @date 2017年10月24日 下午2:09:27
     * @param latitude
     *            纬度
     * @param longitude
     *            经度
     * @return [纬度，经度]
     * @version 1.0
     */
    public static double[] toGCJ02Point(double latitude, double longitude) {
        double[] dev = calDev(latitude, longitude);
        double retLat = latitude + dev[0];
        double retLon = longitude + dev[1];
        return new double[] { retLat, retLon };
    }

    /**
     * Description: WGS-84 to GCJ-02 <BR>
     *
     * @author dsn
     * @date 2017年10月24日 下午2:09:27
     * @param latitude
     *            纬度
     * @param longitude
     *            经度
     * @param scale
     *            经纬度保留小数位数
     * @return [纬度，经度]
     * @version 1.0
     */
    public static double[] toGCJ02Point(double latitude, double longitude, int scale) {
        double[] dev = calDev(latitude, longitude);
        double retLat = latitude + dev[0];
        double retLon = longitude + dev[1];
        return new double[] { new BigDecimal(retLat).setScale(scale, RoundingMode.DOWN).doubleValue(),
                new BigDecimal(retLon).setScale(scale, RoundingMode.DOWN).doubleValue() };
    }

    /**
     * Description:GCJ-02 to WGS-84 <BR>
     *
     * @author dsn
     * @date 2017年10月24日 下午2:09:54
     * @param latitude
     *            纬度
     * @param longitude
     *            经度
     * @return [纬度，经度]
     * @version 1.0
     */
    public static double[] toWGS84Point(double latitude, double longitude) {
        double[] dev = calDev(latitude, longitude);
        double retLat = latitude - dev[0];
        double retLon = longitude - dev[1];
        dev = calDev(retLat, retLon);
        retLat = latitude - dev[0];
        retLon = longitude - dev[1];
        return new double[] { retLat, retLon };
    }

    private static double[] calDev(double wgLat, double wgLon) {
        if (isOutOfChina(wgLat, wgLon)) {
            return new double[] { 0, 0 };
        }
        double dLat = calLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = calLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        return new double[] { dLat, dLon };
    }

    private static boolean isOutOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    private static double calLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double calLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 是否在中国
     * @return
     */
    public static boolean isChinaLocation(double lat, double lon){
        // 判断当前点的经纬度是否在中国范维内的经纬度多边形内，判断国内和海外
        boolean isChina; // 是否在台湾
        com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(lat, lon);
        if (SpatialRelationUtil.isPolygonContainsPoint(PolygonalArea.getBaiduListPoint(), latLng)){
            isChina = true;
        }else{
            isChina = false;
        }
        return isChina;
    }

    /**
     * 是否在中国台湾
     * @return
     */
    public static boolean isChinaTaiWan(double lat, double lng){
        // 判断当前点的经纬度是否在中国范维内的经纬度多边形内，判断国内和海外
        boolean isTaiwan; // 是否在台湾
        com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(lat, lng);
        if (SpatialRelationUtil.isPolygonContainsPoint(PolygonalArea.getTaiWanListPoint(), latLng)) {
            isTaiwan = true;
        }else{
            isTaiwan = false;
        }
        return isTaiwan;
    }

    /**
     * 谷歌经纬度转换
     *
     * @param lat
     * @param lng
     * @return
     */
    public static com.google.android.gms.maps.model.LatLng googleGPSConverter(double lat, double lng) {
        if (!isChinaLocation(lat, lng)) { // 海外
            double[] deviceWGS84 = GpsUtils.toWGS84Point(lat, lng);
            return new com.google.android.gms.maps.model.LatLng(deviceWGS84[0], deviceWGS84[1]);
        } else { //中国
            return new com.google.android.gms.maps.model.LatLng(lat, lng);
        }
    }

    /**
     * 火星转 WGS84
     *
     * @param lat
     * @param lng
     * @return 0 国内 1 台湾   2 国外   3 香港
     */
    public static int PointType(double lat, double lng) {
        boolean isChina = isChinaLocation(lat, lng);
        if (!isChina) { // 海外
            return 2;
        } else { //中国
            if (isChinaTaiWan(lat, lng)) { //中国台湾
                return 1;
            } else if (isChinaHangKong(lat, lng)) { //中国香港
                return 3;
            } else {
                return 0;
            }
        }
    }

    /**
     * 是否在中国香港
     *
     * @return
     */
    public static boolean isChinaHangKong(double lat, double lng) {
        // 判断当前点的经纬度是否在中国范围内的经纬度多边形内，判断国内和海外
        boolean isHangKong; // 是否在香港
        com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(lat, lng);
        if (SpatialRelationUtil.isPolygonContainsPoint(PolygonalArea.getHangKongListPoint(), latLng)) {
            isHangKong = true;
        } else {
            isHangKong = false;
        }
        return isHangKong;
    }

}
