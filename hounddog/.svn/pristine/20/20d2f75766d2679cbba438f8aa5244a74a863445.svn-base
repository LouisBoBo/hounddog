package com.slxk.hounddog.mvp.utils;

/**
 * 地图下载工具类
 */
public class MapDownloadUtil {

    /**
     * 通过经纬度获取坐标点
     * @param lat
     * @param lon
     * @param zoom 层级
     * @return
     */
    public static String getMapXY(double lat, double lon, double zoom) {
        String xy_value = ""; // xy的值，用，号分割
        try {
            double lat_rad = Math.toRadians(lat);
            double n = Math.pow(2.0, zoom);
            long xtile = Math.round((lon + 180.0) / 360.0 * n);
            long ytile = Math.round((1.0 - Math.log(Math.tan(lat_rad) + (1 / Math.cos(lat_rad))) / Math.PI) / 2.0 * n);
            xy_value = xtile + "," + ytile;
        } catch (Exception e){
            e.printStackTrace();
        }
        return xy_value;
    }

}
