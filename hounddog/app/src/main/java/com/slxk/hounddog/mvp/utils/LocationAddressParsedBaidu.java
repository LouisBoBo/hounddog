package com.slxk.hounddog.mvp.utils;



import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * 获取地址 单例模式 - 百度地址
 */
public class LocationAddressParsedBaidu {
    private static volatile LocationAddressParsedBaidu locationParsed;
    private double lat, lon;
    private getAddressListener addressListener;
    // 百度SDK解析地址
    private GeoCoder baiduCoder;

    private LocationAddressParsedBaidu() {
        baiduCoder = GeoCoder.newInstance();
        baiduCoder.setOnGetGeoCodeResultListener(baiduListener);
    }

    public static LocationAddressParsedBaidu getLocationParsedInstance() {
        if (locationParsed == null) {
            synchronized (LocationAddressParsedBaidu.class) {
                if (locationParsed == null) {
                    locationParsed = new LocationAddressParsedBaidu();
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

    public LocationAddressParsedBaidu Parsed(double Latitude, double longitude) {
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
        baiduCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(new com.baidu.mapapi.model.LatLng(lat, lon))
                // 设置是否返回新数据 默认值0不返回，1返回
                .newVersion(1)
                // POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
                .radius(500));
    }

    private OnGetGeoCoderResultListener baiduListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                addressListener.getAddress(lat + "," + lon);
            } else {
                //详细地址
                String address = reverseGeoCodeResult.getAddress() + reverseGeoCodeResult.getSematicDescription();
                addressListener.getAddress(address);
            }
        }
    };
}
