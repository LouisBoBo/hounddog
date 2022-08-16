package com.slxk.hounddog.mvp.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.baidu.mapapi.utils.SpatialRelationUtil;

/**
 * 地址解析工具类
 */
public class AddressParseUtil {

    /**
     * 解析地址，以高德API解析为主
     * @param context
     * @param lat
     * @param lon
     * @param addressTip 提示语
     * @return
     */
    public static void getAmapAddress(Context context, TextView tvAddress, double lat, double lon, String addressTip){
        if (GpsUtils.isChinaTaiWan(lat, lon)) {
            LocationAddressParsedBaidu.getLocationParsedInstance()
                    .Parsed(lat, lon)
                    .setAddressListener(new LocationAddressParsedBaidu.getAddressListener() {
                        @Override
                        public void getAddress(String address) {
                            if (!TextUtils.isEmpty(addressTip)){
                                address = addressTip + address;
                            }
                            tvAddress.setText(address);
                        }
                    });
        }else{
            LocationAddressParsedAmap.getLocationParsedInstance()
                    .Parsed(context, lat, lon)
                    .setAddressListener(new LocationAddressParsedAmap.getAddressListener() {
                        @Override
                        public void getAddress(String address) {
                            if (!TextUtils.isEmpty(addressTip)){
                                address = addressTip + address;
                            }
                            tvAddress.setText(address);
                        }
                    });
        }
    }

    /**
     * 地址解析，百度API解析
     * @param context
     * @param tvAddress
     * @param lat
     * @param lon
     */
    public static void getBaiduAddress(Context context, TextView tvAddress, double lat, double lon, String addressTip){
        LocationAddressParsedBaidu.getLocationParsedInstance()
                .Parsed(lat, lon)
                .setAddressListener(new LocationAddressParsedBaidu.getAddressListener() {
                    @Override
                    public void getAddress(String address) {
                        if (!TextUtils.isEmpty(addressTip)){
                            address = addressTip + address;
                        }
                        tvAddress.setText(address);
                    }
                });
    }

    /**
     * 地址解析，谷歌API解析
     * @param context
     * @param tvAddress
     * @param lat
     * @param lon
     */
    public static void getGoogleAddress(Context context, TextView tvAddress, double lat, double lon, String addressTip){
        // 判断当前点的经纬度是否在中国范维内的经纬度多边形内，判断国内和海外
        com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(lat, lon);
        boolean isChina = SpatialRelationUtil.isPolygonContainsPoint(PolygonalArea.getBaiduListPoint(), latLng); // 是否在国内
        if (isChina) {
            getAmapAddress(context, tvAddress, lat, lon, addressTip);
        } else {
            LocationAddressParsedGoogle.getLocationParsedInstance()
                    .Parsed(lat, lon)
                    .setAddressListener(new LocationAddressParsedGoogle.getAddressListener() {
                        @Override
                        public void getAddress(String address) {
                            if (!TextUtils.isEmpty(addressTip)){
                                address = addressTip + address;
                            }
                            tvAddress.setText(address);
                        }
                    });
        }
    }

}
