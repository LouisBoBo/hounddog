package com.slxk.hounddog.mvp.utils;



import android.location.Address;
import android.location.Geocoder;

import com.slxk.hounddog.app.MyApplication;

import java.util.List;
import java.util.Locale;

/**
 * 获取地址 单例模式 - 百度地址
 */
public class LocationAddressParsedGoogle {
    private static volatile LocationAddressParsedGoogle locationParsed;
    private double lat, lon;
    private getAddressListener addressListener;
    // 百度SDK解析地址
    private Geocoder geocoder;

    private LocationAddressParsedGoogle() {
        geocoder = new Geocoder(MyApplication.getMyApp(), Locale.ENGLISH);
    }

    public static LocationAddressParsedGoogle getLocationParsedInstance() {
        if (locationParsed == null) {
            synchronized (LocationAddressParsedGoogle.class) {
                if (locationParsed == null) {
                    locationParsed = new LocationAddressParsedGoogle();
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

    public LocationAddressParsedGoogle Parsed(double Latitude, double longitude) {
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
        String addressText = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && addresses.size() > 0) {
                Address addressInfo = addresses.get(0);
                if (addressInfo.getMaxAddressLineIndex() >= 0) {
                    addressText += addressInfo.getAddressLine(0);
                }
                if (addressInfo.getMaxAddressLineIndex() >= 1) {
                    if (!"".equals(addressText))
                        addressText += ",";
                    addressText += addressInfo.getAddressLine(1);
                }
                if (addressInfo.getMaxAddressLineIndex() >= 2) {
                    if (!"".equals(addressText))
                        addressText += ",";
                    addressText += addressInfo.getAddressLine(2);
                }
            }
        }catch (Exception e){
            addressText = lat + "," + lon;
            e.printStackTrace();
        }
        addressListener.getAddress(addressText);
    }

}
