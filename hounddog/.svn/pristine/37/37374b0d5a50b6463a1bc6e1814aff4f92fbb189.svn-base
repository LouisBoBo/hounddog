package com.slxk.hounddog.mvp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

/**
 * 获取手机ip地址工具类
 */
public class IPUtils {

    private static String mobileIPAddress; // 手机当前ip地址

    /**
     * 获取当前手机网络ip地址
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                return intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return "";
    }

    /**
     * 获取wifi状态下，局域网的ip地址
     * @param context
     * @return
     */
    public static String getWifiIPAddress(Context context){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 获取外网地址
     * @return
     */
    public static String getNetIp(Context context) {
        mobileIPAddress = "";
        new Thread(new Runnable(){
            @Override
            public void run() {
                URL infoUrl = null;
                InputStream inStream = null;
                try {
                    infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
                    URLConnection connection = infoUrl.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    httpConnection.setReadTimeout(5000);//读取超时
                    httpConnection.setConnectTimeout(5000);//连接超时
                    httpConnection.setDoInput(true);
                    httpConnection.setUseCaches(false);

                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inStream = httpConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                        StringBuilder strber = new StringBuilder();
                        while ((mobileIPAddress = reader.readLine()) != null)
                            strber.append(mobileIPAddress + "\n");
                        inStream.close();
                        // 从反馈的结果中提取出IP地址
                        int start = strber.indexOf("{");
                        int end = strber.indexOf("}");
                        String json = strber.substring(start, end + 1);
                        if (json != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                mobileIPAddress = jsonObject.optString("cip");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("kang", "ip=" + mobileIPAddress);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        if (TextUtils.isEmpty(mobileIPAddress)){
            mobileIPAddress = getWifiIPAddress(context);
        }
        return mobileIPAddress;
    }

    /**
     * 获取外网地址
     */
    private static String[] platforms = {
            "http://pv.sohu.com/cityjson",
            "http://pv.sohu.com/cityjson?ie=utf-8",
            "http://ip.chinaz.com/getip.aspx"
    };

    public static String getOutNetIP(Context context, int index) {
        if (index < platforms.length) {
            BufferedReader buff = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(platforms[index]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(5000);//读取超时
                urlConnection.setConnectTimeout(5000);//连接超时
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {//找到服务器的情况下,可能还会找到别的网站返回html格式的数据
                    InputStream is = urlConnection.getInputStream();
                    buff = new BufferedReader(new InputStreamReader(is, "UTF-8"));//注意编码，会出现乱码
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    while ((line = buff.readLine()) != null) {
                        builder.append(line);
                    }

                    buff.close();//内部会关闭 InputStream
                    urlConnection.disconnect();

                    Log.e("kang", builder.toString());
                    if (index == 0 || index == 1) {
                        //截取字符串
                        int satrtIndex = builder.indexOf("{");//包含[
                        int endIndex = builder.indexOf("}");//包含]
                        String json = builder.substring(satrtIndex, endIndex + 1);//包含[satrtIndex,endIndex)
                        JSONObject jo = new JSONObject(json);
                        String ip = jo.getString("cip");

                        return ip;
                    } else if (index == 2) {
                        JSONObject jo = new JSONObject(builder.toString());
                        return jo.getString("ip");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 获取局域网Ip地址
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
        }
        return getOutNetIP(context, ++index);
    }

}
