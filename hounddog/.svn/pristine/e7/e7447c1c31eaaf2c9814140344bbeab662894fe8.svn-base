package com.slxk.hounddog.mvp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.slxk.hounddog.R;

/**
 * 高德地图marker图标工具类
 * Created by Kangyu on 2020/11/11.
 */

public class BitmapHelperGoogle {

    // 起点图标
    private BitmapDescriptor start;
    private BitmapDescriptor start_08;
    private BitmapDescriptor start_07;
    private BitmapDescriptor start_06;
    private BitmapDescriptor start_05;
    private BitmapDescriptor start_04;
    private BitmapDescriptor start_03;

    // 终点图标
    private BitmapDescriptor end;
    private BitmapDescriptor end_08;
    private BitmapDescriptor end_07;
    private BitmapDescriptor end_06;
    private BitmapDescriptor end_05;
    private BitmapDescriptor end_04;
    private BitmapDescriptor end_03;

    // 方向箭头图标
    private BitmapDescriptor direction;
    private BitmapDescriptor direction_08;
    private BitmapDescriptor direction_07;
    private BitmapDescriptor direction_06;
    private BitmapDescriptor direction_05;
    private BitmapDescriptor direction_04;
    private BitmapDescriptor direction_03;

    // P点-GPS类型
    private BitmapDescriptor parking_gps;
    private BitmapDescriptor parking_gps_08;
    private BitmapDescriptor parking_gps_07;
    private BitmapDescriptor parking_gps_06;
    private BitmapDescriptor parking_gps_05;
    private BitmapDescriptor parking_gps_04;
    private BitmapDescriptor parking_gps_03;

    // P点-基站类型
    private BitmapDescriptor parking_base_station;
    private BitmapDescriptor parking_base_station_08;
    private BitmapDescriptor parking_base_station_07;
    private BitmapDescriptor parking_base_station_06;
    private BitmapDescriptor parking_base_station_05;
    private BitmapDescriptor parking_base_station_04;
    private BitmapDescriptor parking_base_station_03;

    // P点-wifi类型
    private BitmapDescriptor parking_wifi;
    private BitmapDescriptor parking_wifi_08;
    private BitmapDescriptor parking_wifi_07;
    private BitmapDescriptor parking_wifi_06;
    private BitmapDescriptor parking_wifi_05;
    private BitmapDescriptor parking_wifi_04;
    private BitmapDescriptor parking_wifi_03;

    // 用户定位图标
    private BitmapDescriptor user_location;
    private BitmapDescriptor user_location_08;
    private BitmapDescriptor user_location_07;
    private BitmapDescriptor user_location_06;
    private BitmapDescriptor user_location_05;
    private BitmapDescriptor user_location_04;
    private BitmapDescriptor user_location_03;

    // 基站红点-圆形
    private BitmapDescriptor base_station_circle;
    private BitmapDescriptor base_station_circle_08;
    private BitmapDescriptor base_station_circle_07;
    private BitmapDescriptor base_station_circle_06;
    private BitmapDescriptor base_station_circle_05;
    private BitmapDescriptor base_station_circle_04;
    private BitmapDescriptor base_station_circle_03;

    // wifi蓝点-圆形
    private BitmapDescriptor wifi_circle;
    private BitmapDescriptor wifi_circle_08;
    private BitmapDescriptor wifi_circle_07;
    private BitmapDescriptor wifi_circle_06;
    private BitmapDescriptor wifi_circle_05;
    private BitmapDescriptor wifi_circle_04;
    private BitmapDescriptor wifi_circle_03;

    // 显示比例，按照原图缩放比例
    float f_08size = 0.8f;
    float f_07size = 0.7f;
    float f_06size = 0.6f;
    float f_05size = 0.5f;
    float f_04size = 0.4f;
    float f_03size = 0.3f;
    float f_02size = 0.2f;
    float f_01size = 0.1f;

    /**
     * 初始化图标的bitmap
     */
    public BitmapHelperGoogle(Context context) {

        Bitmap startBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_start);
        Bitmap endBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_end);
        Bitmap parkingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_key_point);
        Bitmap userBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_user_location);
        Bitmap directionBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_direction);

        Bitmap baseStationBitmap = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888);
        baseStationBitmap.eraseColor(Color.parseColor("#FE1515"));
        baseStationBitmap = ImageUtils.getRoundedCornerBitmap(baseStationBitmap, 40);

        Bitmap wifiBitmap = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888);
        wifiBitmap.eraseColor(Color.parseColor("#3385FF"));
        wifiBitmap = ImageUtils.getRoundedCornerBitmap(wifiBitmap, 40);

        start = BitmapDescriptorFactory.fromBitmap(startBitmap);
        start_08 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(startBitmap, f_08size));
        start_07 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(startBitmap, f_07size));
        start_06 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(startBitmap, f_06size));
        start_05 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(startBitmap, f_05size));
        start_04 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(startBitmap, f_04size));
        start_03 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(startBitmap, f_03size));

        end = BitmapDescriptorFactory.fromBitmap(endBitmap);
        end_08 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(endBitmap, f_08size));
        end_07 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(endBitmap, f_07size));
        end_06 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(endBitmap, f_06size));
        end_05 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(endBitmap, f_05size));
        end_04 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(endBitmap, f_04size));
        end_03 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(endBitmap, f_03size));

        direction = BitmapDescriptorFactory.fromBitmap(directionBitmap);
        direction_08 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(directionBitmap, f_08size));
        direction_07 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(directionBitmap, f_07size));
        direction_06 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(directionBitmap, f_06size));
        direction_05 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(directionBitmap, f_05size));
        direction_04 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(directionBitmap, f_04size));
        direction_03 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(directionBitmap, f_03size));

        parking_gps = BitmapDescriptorFactory.fromBitmap(parkingBitmap);
        parking_gps_08 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(parkingBitmap, f_08size));
        parking_gps_07 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(parkingBitmap, f_07size));
        parking_gps_06 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(parkingBitmap, f_06size));
        parking_gps_05 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(parkingBitmap, f_05size));
        parking_gps_04 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(parkingBitmap, f_04size));
        parking_gps_03 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(parkingBitmap, f_03size));

        user_location = BitmapDescriptorFactory.fromBitmap(userBitmap);
        user_location_08 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(userBitmap, f_08size));
        user_location_07 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(userBitmap, f_07size));
        user_location_06 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(userBitmap, f_06size));
        user_location_05 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(userBitmap, f_05size));
        user_location_04 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(userBitmap, f_04size));
        user_location_03 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(userBitmap, f_03size));

        base_station_circle = BitmapDescriptorFactory.fromBitmap(baseStationBitmap);
        base_station_circle_08 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(baseStationBitmap, f_08size));
        base_station_circle_07 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(baseStationBitmap, f_07size));
        base_station_circle_06 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(baseStationBitmap, f_06size));
        base_station_circle_05 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(baseStationBitmap, f_05size));
        base_station_circle_04 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(baseStationBitmap, f_04size));
        base_station_circle_03 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(baseStationBitmap, f_03size));

        wifi_circle = BitmapDescriptorFactory.fromBitmap(wifiBitmap);
        wifi_circle_08 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(wifiBitmap, f_08size));
        wifi_circle_07 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(wifiBitmap, f_07size));
        wifi_circle_06 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(wifiBitmap, f_06size));
        wifi_circle_05 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(wifiBitmap, f_05size));
        wifi_circle_04 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(wifiBitmap, f_04size));
        wifi_circle_03 = BitmapDescriptorFactory.fromBitmap(ImageUtils.scaleBitmapBySize(wifiBitmap, f_03size));

    }

    /**
     * 基站红点
     * @param zoom
     * @return
     */
    public BitmapDescriptor getBitmapZoomForBaseStation(float zoom){
//        if (zoom >= 17) {
//            return base_station_circle;
//        } else if (zoom < 17 && zoom >= 15) {
//            return base_station_circle_08;
//        } else if (zoom < 15 && zoom >= 13) {
//            return base_station_circle_07;
//        } else if (zoom < 13 && zoom >= 11) {
//            return base_station_circle_06;
//        } else if (zoom < 11 && zoom >= 9) {
//            return base_station_circle_05;
//        } else if (zoom < 9 && zoom >= 7) {
//            return base_station_circle_05;
//        } else {
//            return base_station_circle_05;
//        }

        return base_station_circle_08;
    }

    /**
     * wifi蓝点
     * @param zoom
     * @return
     */
    public BitmapDescriptor getBitmapZoomForWifi(float zoom){
//        if (zoom >= 17) {
//            return wifi_circle;
//        } else if (zoom < 17 && zoom >= 15) {
//            return wifi_circle_08;
//        } else if (zoom < 15 && zoom >= 13) {
//            return wifi_circle_07;
//        } else if (zoom < 13 && zoom >= 11) {
//            return wifi_circle_06;
//        } else if (zoom < 11 && zoom >= 9) {
//            return wifi_circle_05;
//        } else if (zoom < 9 && zoom >= 7) {
//            return wifi_circle_05;
//        } else {
//            return wifi_circle_05;
//        }

        return wifi_circle_08;
    }

    /**
     * 起点图标
     * @param zoom
     * @return
     */
    public BitmapDescriptor getBitmapZoomForStart(float zoom) {
//        if (zoom >= 17) {
//            return start;
//        } else if (zoom < 17 && zoom >= 15) {
//            return start_08;
//        } else if (zoom < 15 && zoom >= 13) {
//            return start_07;
//        } else if (zoom < 13 && zoom >= 11) {
//            return start_06;
//        } else if (zoom < 11 && zoom >= 9) {
//            return start_05;
//        } else if (zoom < 9 && zoom >= 7) {
//            return start_05;
//        } else {
//            return start_05;
//        }

        return start;
    }

    /**
     * 终点图标
     * @param zoom
     * @return
     */
    public BitmapDescriptor getBitmapZoomForEnd(float zoom) {
//        if (zoom >= 17) {
//            return end;
//        } else if (zoom < 17 && zoom >= 15) {
//            return end_08;
//        } else if (zoom < 15 && zoom >= 13) {
//            return end_07;
//        } else if (zoom < 13 && zoom >= 11) {
//            return end_06;
//        } else if (zoom < 11 && zoom >= 9) {
//            return end_05;
//        } else if (zoom < 9 && zoom >= 7) {
//            return end_05;
//        } else {
//            return end_05;
//        }

        return end;
    }

    /**
     * 方向箭头图标
     * @param zoom
     * @return
     */
    public BitmapDescriptor getBitmapZoomForDirection(float zoom) {
//        if (zoom >= 17) {
//            return direction;
//        } else if (zoom < 17 && zoom >= 15) {
//            return direction_08;
//        } else if (zoom < 15 && zoom >= 13) {
//            return direction_07;
//        } else if (zoom < 13 && zoom >= 11) {
//            return direction_06;
//        } else if (zoom < 11 && zoom >= 9) {
//            return direction_05;
//        } else if (zoom < 9 && zoom >= 7) {
//            return direction_05;
//        } else {
//            return direction_05;
//        }

        return direction_08;
    }

    /**
     * 基站 P点显示
     *
     * @param zoom
     * @return
     */
    public BitmapDescriptor getBitmapZoomParkingForBaseStation(float zoom) {
//        if (zoom >= 17) {
//            return parking_base_station;
//        } else if (zoom < 17 && zoom >= 15) {
//            return parking_base_station_08;
//        } else if (zoom < 15 && zoom >= 13) {
//            return parking_base_station_07;
//        } else if (zoom < 13 && zoom >= 11) {
//            return parking_base_station_06;
//        } else if (zoom < 11 && zoom >= 9) {
//            return parking_base_station_05;
//        } else if (zoom < 9 && zoom >= 7) {
//            return parking_base_station_05;
//        } else {
//            return parking_base_station_05;
//        }

        return parking_base_station;
    }

    /**
     * wifi P点显示
     *
     * @param zoom
     * @return
     */
    public BitmapDescriptor getBitmapZoomParkingForWifi(float zoom) {
//        if (zoom >= 17) {
//            return parking_wifi;
//        } else if (zoom < 17 && zoom >= 15) {
//            return parking_wifi_08;
//        } else if (zoom < 15 && zoom >= 13) {
//            return parking_wifi_07;
//        } else if (zoom < 13 && zoom >= 11) {
//            return parking_wifi_06;
//        } else if (zoom < 11 && zoom >= 9) {
//            return parking_wifi_05;
//        } else if (zoom < 9 && zoom >= 7) {
//            return parking_wifi_05;
//        } else {
//            return parking_wifi_05;
//        }

        return parking_wifi;
    }

    /**
     * GPS P点显示
     *
     * @param zoom
     * @return
     */
    public BitmapDescriptor getBitmapZoomParkingForGPS(float zoom) {
//        if (zoom >= 17) {
//            return parking_gps;
//        } else if (zoom < 17 && zoom >= 15) {
//            return parking_gps_08;
//        } else if (zoom < 15 && zoom >= 13) {
//            return parking_gps_07;
//        } else if (zoom < 13 && zoom >= 11) {
//            return parking_gps_06;
//        } else if (zoom < 11 && zoom >= 9) {
//            return parking_gps_05;
//        } else if (zoom < 9 && zoom >= 7) {
//            return parking_gps_05;
//        } else {
//            return parking_gps_05;
//        }

        return parking_gps;
    }

    /**
     * 用户定位图标
     *
     * @param zoom
     * @return
     */
    public BitmapDescriptor getBitmapZoomForUserLocation(float zoom) {
//        if (zoom >= 17) {
//            return user_location;
//        } else if (zoom < 17 && zoom >= 15) {
//            return user_location_08;
//        } else if (zoom < 15 && zoom >= 13) {
//            return user_location_07;
//        } else if (zoom < 13 && zoom >= 11) {
//            return user_location_06;
//        } else if (zoom < 11 && zoom >= 9) {
//            return user_location_05;
//        } else if (zoom < 9 && zoom >= 7) {
//            return user_location_05;
//        } else {
//            return user_location_05;
//        }

        return user_location;
    }

    /**
     * 根据地图缩放范围，计算缩放层级
     * @param range
     * @return
     */
    public static int getZoomLevel(int range) {
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
