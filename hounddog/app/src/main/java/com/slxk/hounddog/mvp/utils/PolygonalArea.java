package com.slxk.hounddog.mvp.utils;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PolygonalArea {

    public static List<LatLng> baiduListPoint; // 百度地图，添加中国区域范围值，构建多边形区域
    public static List<LatLng> taiWanListPoint; // 百度地图，添加台湾区域范围值，构建多边形区域

    public static List<LatLng> hangKongListPoint; // 百度地图，添加香港区域范围值，构建多边形区域

    /**
     * 添加中国区域范围值，构建多边形区域
     */
    public static void onAddBaiduPoint() {
        baiduListPoint = new ArrayList<>();
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(21.528655, 107.399393));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(3.375179, 110.915018));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(24.36105, 122.868143));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(39.803422, 124.406229));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(41.404783, 128.317362));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(45.022246, 133.151346));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(48.424784, 135.128885));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(47.808684, 131.217752));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(53.169129, 125.636698));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(52.904887, 119.967752));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(52.33295, 120.530802));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(49.622413, 117.762247));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(49.892116, 116.729532));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(47.869521, 115.455118));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(47.61834, 117.388712));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(47.957884, 118.509317));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(46.72212, 119.717813));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(46.601481, 117.476603));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(45.008654, 111.681961));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(42.566385, 108.825515));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(42.921389, 96.564773));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(49.30825, 87.248367));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(47.32128, 82.985672));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(44.977577, 79.821609));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(42.111624, 79.99739));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(39.517976, 73.361648));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(29.856311, 81.227859));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(27.153442, 89.00618));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(26.486741, 98.542312));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(23.781767, 97.44368));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(20.896219, 101.75032));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(22.366781, 103.06868));
        baiduListPoint.add(new com.baidu.mapapi.model.LatLng(22.853592, 106.100906));
    }



    /**
     * 台湾多边形区域
     */
    public static void onTaiWanPoint() {
        taiWanListPoint = new ArrayList<>();
        taiWanListPoint.add(new com.baidu.mapapi.model.LatLng(25.767311, 121.56077));
        taiWanListPoint.add(new com.baidu.mapapi.model.LatLng(23.218193, 118.528543));
        taiWanListPoint.add(new com.baidu.mapapi.model.LatLng(20.588635, 120.714823));
        taiWanListPoint.add(new com.baidu.mapapi.model.LatLng(22.621177, 122.912088));
        taiWanListPoint.add(new com.baidu.mapapi.model.LatLng(25.182142, 123.428446));
    }


    /**
     * 香港多边形区域
     */
    public static void onHangKongPoint() {
        hangKongListPoint = new ArrayList<>();
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.434589, 113.881899));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.464518, 113.946289));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.511535, 114.01068));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.507262, 114.056673));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.541447, 114.116464));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.554265, 114.167057));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.554265, 114.236047));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.567081, 114.374026));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.549992, 114.466013));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.443141, 114.479811));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.37899, 114.512006));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.15637, 114.512006));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.143516, 113.900296));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.216342, 113.822108));
        hangKongListPoint.add(new com.baidu.mapapi.model.LatLng(22.30625, 113.868101));
    }

    /**
     * 获取中国范围经纬度多边形
     * @return
     */
    public static List<LatLng> getBaiduListPoint(){
        return baiduListPoint;
    }

    /**
     * 获取台湾范围经纬度多边形
     * @return
     */
    public static List<LatLng> getTaiWanListPoint(){
        return taiWanListPoint;
    }

    /**
     * 获取香港范围经纬度多边形
     * @return
     */
    public static List<LatLng> getHangKongListPoint(){
        return hangKongListPoint;
    }

    //0国外  1国内  2香港  3澳门  4台湾(本土） 5金门县(台湾)
    public byte IsNeedCorrectCoordinate(double lon, double lat) {
        //在中国区域画了13个标准多边形，大部分地区在多边形里面，少部分边角及香港、澳门、台湾（包括金门县）需要地理图形校验
        byte inChina = 0;
        //最大范围判断，不需要纠偏的
        if (lon < 73.000 || lon > 135.500)
            return inChina;
        if (lat < 18 || lat > 54.00)
            return inChina;

        inChina = 1;
        //较大范围判断，必须纠偏的
        //中原：[98.8, 41.65], [98.8, 25.35], [123.5, 25.35], [123.5, 41.65]
        if (lon >= 98.8 && lon <= 124.15 && lat >= 25.35 && lat <= 41.65) {
            return inChina;
        }
        //云南，广东，广西：[99.6, 25.35], [99.6, 23.4], [118.21, 23.4], [118.21, 25.35], [99.6, 25.35]
        if (lon >= 99.6 && lon <= 118.21 && lat >= 23.4 && lat <= 25.35) {
            return inChina;
        }
        //深圳东莞惠州： [113.54, 23.4], [113.54, 22.565], [118.21, 22.565], [118.21, 23.4], [113.54, 23.4]
        if (lon >= 113.54 && lon <= 118.21 && lat >= 22.565 && lat <= 23.4) {
            return inChina;
        }
        //海南：[108.0, 23.4], [108.0, 18.0], [113.54, 18.0], [113.54, 23.4], [108.0, 23.4]
        if (lon >= 108.0 && lon <= 113.54 && lat >= 18.0 && lat <= 23.4) {
            return inChina;
        }
        //西部新疆西藏青海三省交汇：[79.45, 41.82], [79.45, 31.05], [98.8, 31.05], [98.8, 41.82], [79.45, 41.82]
        if (lon >= 79.45 && lon <= 98.8 && lat >= 31.05 && lat <= 41.82) {
            return inChina;
        }
        //新疆喀什 [75.37, 40.28], [75.37, 36.7], [79.45, 36.7], [79.45, 40.28], [75.37, 40.28]
        if (lon >= 75.37 && lon <= 79.45 && lat >= 36.7 && lat <= 40.28) {
            return inChina;
        }
        //新疆乌鲁木齐[82.90, 46.88], [82.90, 44.6], [90.74, 44.6], [90.74, 46.88], [82.90, 46.88]
        if (lon >= 82.90 && lon <= 90.74 && lat >= 44.6 && lat <= 46.88) {
            return inChina;
        }
        //新疆克拉玛依[80.78, 44.6],[80.78, 41.82], [94.5, 41.82], [94.5, 44.6],[80.78, 44.6],
        if (lon >= 80.78 && lon <= 94.5 && lat >= 41.82 && lat <= 44.6) {
            return inChina;
        }
        //西藏拉萨 [84.68, 31.05], [84.68, 28.70], [98.8, 28.70], [98.8, 31.05], [84.68, 31.05]
        if (lon >= 84.68 && lon <= 98.8 && lat >= 28.70 && lat <= 31.05) {
            return inChina;
        }
        //东北中心 [120.05, 49.35], [120.05, 42.12], [129.15, 42.12], [129.15, 49.35], [120.05, 49.35]
        if (lon >= 120.05 && lon <= 129.15 && lat >= 42.12 && lat <= 49.35) {
            return inChina;
        }
        //东北西边，内蒙东 [111.95, 44.75], [111.95, 41.65], [120.05, 41.65], [120.05, 44.75], [111.95, 44.75]
        if (lon >= 111.95 && lon <= 120.05 && lat >= 41.65 && lat <= 44.75) {
            return inChina;
        }
        //东北东边 [129.15, 47.68], [129.15, 45.42], [133.35, 45.42], [133.35, 47.68], [129.15, 47.68]
        if (lon >= 129.15 && lon <= 133.35 && lat >= 45.42 && lat <= 47.68) {
            return inChina;
        }
        //东北北边 [120.80, 53.12], [120.80, 49.35], [125.65, 49.35], [125.65, 53.12], [120.80, 53.14]
        if (lon >= 120.80 && lon <= 125.65 && lat >= 49.35 && lat <= 53.12) {
            return inChina;
        }
        //香港 [113.93, 22.472], [113.93, 22.19], [114.40, 22.19], [114.40, 22.472], [113.93, 22.472]
        if (lon >= 113.93 && lon <= 114.40 && lat >= 22.19 && lat <= 22.472) {
            return 2;
        }
        //台湾 [120.00, 25.35], [120.00, 21.85], [122.08, 21.85], [122.08, 25.35], [120.00, 25.35]
        if (lon >= 120.00 && lon <= 122.08 && lat >= 21.85 && lat <= 25.35) {
            return 4;
        }
//
////        //其他边边角角需要精细判断
//        S2Point point = S2LatLng.FromDegrees(lat, lon).ToPoint();
//        bool atChina = ChinaBoundary.Contains(point);
//        if (atChina) {
//            inChina = (byte) CoordinateArea.China;
//            if (HongKongBoundary.Contains(point)) {
//                inChina = (byte) CoordinateArea.ChinaHongkong;
//            } else if (AoMenBoundary.Contains(point)) {
//                inChina = (byte) CoordinateArea.ChinaAomen;
//            } else if (TaiWanBoundary.Contains(point)) {
//                inChina = (byte) CoordinateArea.ChinaTaiwan;
//            } else if (JinMenBoundary.Contains(point)) {
//                inChina = (byte) CoordinateArea.ChinaJinmen;
//            }
//        } else {
//            inChina = (byte) CoordinateArea.Foreign;
//        }

        return inChina;
    }

}
