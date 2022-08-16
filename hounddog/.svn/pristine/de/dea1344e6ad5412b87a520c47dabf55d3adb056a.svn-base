package com.slxk.hounddog.mvp.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

/**
 * Created by Administrator on 2017\12\19 0019.
 */

public class GoogleLocationUtils {

    public static String getLocation(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
        //List<String> lp = lm.getAllProviders();
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        //设置位置服务免费
        criteria.setAccuracy(Criteria.ACCURACY_FINE); //设置水平位置精度
        //getBestProvider 只有允许访问调用活动的位置供应商将被返回
        String providerName = lm.getBestProvider(criteria, true);

        if (providerName != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                Location location = lm.getLastKnownLocation(providerName);
                if(location!=null){
                    //获取维度信息
                    double latitude = location.getLatitude();
                    //获取经度信息
                    double longitude = location.getLongitude();
                    return GpsCorrect.transform(latitude,longitude);
                }
            }
        }
        return "";
    }

}
