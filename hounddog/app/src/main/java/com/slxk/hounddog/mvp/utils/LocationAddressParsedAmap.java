package com.slxk.hounddog.mvp.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.RegeocodeRoad;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;

/**
 * 获取地址 单例模式 - 百度地址
 */
public class LocationAddressParsedAmap {
    private static volatile LocationAddressParsedAmap locationParsed;
    private double lat, lon;
    private getAddressListener addressListener;
    // 高德地图通过经纬度获取地址信息
    private RegeocodeQuery query;
    private GeocodeSearch geocoderSearch; // 地理编码，通过经纬度获取地址信息
    private Context mContext;

    private LocationAddressParsedAmap() {
        // 高德SDK解析经纬度地址
        geocoderSearch = new GeocodeSearch(MyApplication.getMyApp());
        geocoderSearch.setOnGeocodeSearchListener(aMaplistener);
    }

    public static LocationAddressParsedAmap getLocationParsedInstance() {
        if (locationParsed == null) {
            synchronized (LocationAddressParsedAmap.class) {
                if (locationParsed == null) {
                    locationParsed = new LocationAddressParsedAmap();
                }
            }
        }
        return locationParsed;
    }

    public interface getAddressListener {
        //返回地址
        void getAddress(String address);
    }

    public void setAddressListener(getAddressListener listener) {
        this.addressListener = listener;
    }

    public LocationAddressParsedAmap Parsed(Context context, double Latitude, double longitude) {
        mContext = context;
        lat = Latitude;
        lon = longitude;
        getAddressForLocation(lat, lon);
        return this;
    }

    /**
     * 百度API通过经纬度获取详细地址信息
     *
     * @return
     */
    private void getAddressForLocation(double lat, double lon) {
        LatLonPoint latLonPoint = new LatLonPoint(lat, lon);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        query = new RegeocodeQuery(latLonPoint, 500, GeocodeSearch.AMAP);
        query.setExtensions("all");
        geocoderSearch.getFromLocationAsyn(query);
    }

    /**
     * 高德地图-地理编码检索监听器，逆地理编码，通过经纬度获取地址信息
     */
    private GeocodeSearch.OnGeocodeSearchListener aMaplistener = new GeocodeSearch.OnGeocodeSearchListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            if (!TextUtils.isEmpty(regeocodeResult.getRegeocodeAddress().getFormatAddress())) {
                String address = regeocodeResult.getRegeocodeAddress().getFormatAddress().replace("(", "");
                address = address.replace(")", "");
                address = address.replace("null", "");
                // 判断是否获取到了方向和距离
                if (regeocodeResult.getRegeocodeAddress().getRoads() != null
                        && regeocodeResult.getRegeocodeAddress().getRoads().size() > 0) {
                    RegeocodeRoad road = regeocodeResult.getRegeocodeAddress().getRoads().get(0);
                    int distance = (int) road.getDistance();
                    address = address + "，" + road.getName() + road.getDirection() + distance
                            + mContext.getString(R.string.meter);
                } else {
                    if (regeocodeResult.getRegeocodeAddress().getPois().size() > 0) {
                        PoiItem poiItem = regeocodeResult.getRegeocodeAddress().getPois().get(0);
                        if (!TextUtils.isEmpty(poiItem.getSnippet())) {
                            address = address + "，" + poiItem.getSnippet();
                        } else {
                            address = address + poiItem.getDirection() + poiItem.getDistance() + mContext.getString(R.string.meter);
                        }
                    }
                }
                addressListener.getAddress(address);
            } else {
                addressListener.getAddress(lat + "," + lon);
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            addressListener.getAddress(lat + "," + lon);
        }
    };

}
