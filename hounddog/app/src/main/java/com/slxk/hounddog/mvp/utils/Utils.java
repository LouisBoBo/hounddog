package com.slxk.hounddog.mvp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.AppUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 * Created by Kang on 2019/1/9.
 */
public class Utils {

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailableTwo(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                // 当前所连接的网络可用
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    /**
     * 检查当前网络是否可用，WLAN、3G/2G状态
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo.length > 0) {
                for (NetworkInfo info : networkInfo) {
                    // 判断当前网络状态是否为连接状态
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否快速点击
     *
     * @return
     */
    public static boolean isButtonQuickClick() {
        if (System.currentTimeMillis() - MyApplication.getMyApp().getSystemTime() > 600) {
            MyApplication.getMyApp().setSystemTime(System.currentTimeMillis());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 格式化价格，强制保留2位小数
     *
     * @param value
     * @return
     */
    public static String formatValue(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }

    /**
     * 格式化价格，强制保留1位小数
     *
     * @param value
     * @return
     */
    public static String formatValue_2(double value) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(value);
    }

    /**
     * 格式化经纬度，强制保留6位小数
     *
     * @param locaition
     * @return
     */
    public static String formatLatLng(double locaition) {
        DecimalFormat df = new DecimalFormat("0.000000");
        return df.format(locaition);
    }

    /**
     * 经纬度除法运算，除以1000000，得到正确的经纬度格式
     *
     * @param latlon
     * @return
     */
    public static double formatLatLngForDivisionOperation(int latlon) {
        return (double) latlon / 1000000;
    }

    /**
     * 判断字符串是否是字母与数组组合
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }

    /**
     * 通过uri获取路径
     *
     * @param uri
     */
    public static String handleImageOnKitKat(Uri uri, Context context) {
        String imagePath = null;

        if (DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, context);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null, context);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //Log.d(TAG, "content: " + uri.toString());
            imagePath = getImagePath(uri, null, context);
        }
        return imagePath;
    }

    private static String getImagePath(Uri uri, String selection, Context context) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

            cursor.close();
        }
        return path;
    }

    /**
     * 根据包名判断软件是否存在
     */
    public static boolean isPkgInstalled(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return (packageInfo != null);
    }

    public static void hideKeyboard(MotionEvent event, View view, Activity activity) {
        try {
            if (view != null && view instanceof EditText) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    IBinder token = view.getWindowToken();
                    InputMethodManager inputMethodManager = (InputMethodManager) activity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getAutoStatusBarHeight() {
        int result = 0;
        try {
            int resourceId = Resources.getSystem()
                    .getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = Resources.getSystem().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int getStatusBarHeight(Context context) {
        if (context == null) {
            return 48;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 获取手机的一些信息，上传给服务器
     *
     * @param context 上下文
     * @return
     */
    public static String getMobilePackageInfo(Context context) {
        return ";AppName:" + context.getString(R.string.app_name) + ";AppVersionName:" + AppUtils.getAppVersionName() + ";PhoneInfo:"
                + "手机型号-" + Build.MODEL + ",Android-" + Build.VERSION.RELEASE;
//        return ",手机型号:" + Build.MODEL + ",手机版本:Android " + Build.VERSION.RELEASE + ",手机制造商:" + Build.MANUFACTURER
//                + ",AppName:" + context.getString(R.string.app_name) + ",AppVersionName:" + AppUtils.getAppVersionName();
    }

    /**
     * 限制只能输入英文数字
     *
     * @param content
     * @return
     */
    public static String compileExChar(String content) {
        String reg = "[^a-zA-Z0-9]";
        return content.replaceAll(reg, "");
    }

    /**
     * 限制只能输入中英文数字
     *
     * @param content
     * @return
     */
    public static String compileExChar2(String content) {
        String reg = "[^a-zA-Z0-9\u4E00-\u9FA5]";
        return content.replaceAll(reg, "");
    }

    /**
     * 根据经纬度计算需要偏转的角度
     */
    public static float getRotate(LatLng curPos, LatLng nextPos) {
        double x1 = curPos.latitude;
        double x2 = nextPos.latitude;
        double y1 = curPos.longitude;
        double y2 = nextPos.longitude;
        return (float) (Math.atan2(y2 - y1, x2 - x1) / Math.PI * 180);
    }

    /**
     * 根据经纬度计算需要偏转的角度
     */
    public static float getRotateBaidu(com.baidu.mapapi.model.LatLng curPos, com.baidu.mapapi.model.LatLng nextPos) {
        double x1 = curPos.latitude;
        double x2 = nextPos.latitude;
        double y1 = curPos.longitude;
        double y2 = nextPos.longitude;
        return (float) (Math.atan2(y2 - y1, x2 - x1) / Math.PI * 180);
    }

    /**
     * 计算停车停留时长
     *
     * @param time
     * @return
     */
    public static String getParkingTime(Context context, int time) {
        String parkingTime = "";
        int oneDay = 60 * 60 * 24;
        int day = 0;
        int hour = 0;
        int min = 0;
        hour = time / 3600;
        if (hour > 0) {
            min = (time - hour * 3600) / 60;
            day = hour / 24;
            if (day > 0) {
                hour = hour - day * 24;
            }
        } else {
            min = time / 60;
        }
        if (day > 0) {
            parkingTime = day + context.getString(R.string.day);
        }
        if (hour > 0) {
            parkingTime = parkingTime + hour + context.getString(R.string.hour_two);
        }
        if (min > 0) {
            parkingTime = parkingTime + min + context.getString(R.string.minute);
        }
        return parkingTime;
    }

    /**
     * @return
     */
    public static String convertCalendar2TimeString(Calendar calendar) {
        if (calendar != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            return format.format(calendar.getTime());
        }
        return null;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPenGPS(Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 判断邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
//        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 是否显示的是中文
     *
     * @return
     */
    public static boolean isLocaleForCN() {
        return Locale.getDefault().toString().toLowerCase().contains("zh");
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context mContext) {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) mContext.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = mContext.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转换成当地时间格式
     *
     * @param chinaTimeZone
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String chinaTimeZoneToLocal(String chinaTimeZone) {
        String localTime = "";
        try {
            SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//UTC时间格式
            utcFormater.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

            SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//当地时间格式
            localFormater.setTimeZone(TimeZone.getDefault());
            localTime = localFormater.format(utcFormater.parse(chinaTimeZone).getTime());
        } catch (Exception e) {
            localTime = chinaTimeZone;
        }
        return localTime;
    }

    /**
     * 去除字符串中间的空格
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        dest = dest.replace(" ", "");
        return dest;
    }

    /**
     * 当前语言，默认英文
     *
     * @return
     */
    public static String localeLanguage() {
        if (Locale.getDefault().toString().toLowerCase().contains("zh")) {
            return "zh";
        } else {
            return "en";
        }
    }

    /**
     * 当前语言，简写，用于多语言接口和帮助中心请求
     *
     * @return
     */
    public static String getLocaleLanguageShorthand() {
        String language = Locale.getDefault().toString();//手机内语言设置
        if (language.contains("#Hant") || language.equals("zh_TW") || language.equals("zh_HK")) {
            language = "hk";
        } else {
            String[] languages = language.split("_");
            language = languages[0];
        }
        return language.toLowerCase();
    }

    /**
     * 判断ip地址是否是中国的
     *
     * @param name
     * @return
     */
    public static boolean checkNameIsChina(String name) {
        boolean isTrue = false;
        if (TextUtils.isEmpty(name)) {
            //获取网络ip所属区域失败 按本地语言判断
            if (isLocaleForCN()) {
                isTrue = true;
            }
        } else {
            //中国31个省市自治区 不包括 港、澳、台
            String[] names = new String[]{"北京市", "天津市", "上海市", "重庆市", "内蒙古自治区", "广西壮族自治区", "西藏自治区",
                    "宁夏回族自治区", "新疆维吾尔自治区", "河北省", "山西省", "辽宁省", "吉林省", "黑龙江省", "江苏省", "浙江省", "安徽省", "福建省", "江西省",
                    "山东省", "河南省", "湖北省", "湖南省", "广东省", "海南省", "四川省", "贵州省", "云南省", "陕西省", "甘肃省", "青海省"};
            for (String proName : names) {
                if (name.contains(proName)) {
                    isTrue = true;
                    break;
                }
            }
        }
        return isTrue;
    }

    /**
     * 将px值转换为sp值
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将px值转换为dip或dp值
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断数组是否为空
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

}
