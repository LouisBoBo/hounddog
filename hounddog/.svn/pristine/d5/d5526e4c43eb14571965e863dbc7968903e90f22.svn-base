package com.slxk.hounddog.mvp.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.slxk.hounddog.app.MyApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Create by ChenHao on 2018/6/299:30
 * use : 应用异常处理类
 * 使用方式： 在Application 中初始化  CrashHandler.getInstance().init(this);
 */
public class DeviceLogUtil {
    private static final String TAG = "CrashHandler";
    public static final boolean DEBUG = true;
    /**
     * 文件名
     */
    private String FILE_NAME = "";
    /**
     * 设备运行日志 存储位置为根目录下的 device_log文件夹
     */
    public static final String PATH = FileUtilApp.getSDPath(MyApplication.getMyApp()) +
            FileUtilApp.FileDeviceLogName;
    /**
     * 文件名后缀
     */
    private static final String FILE_NAME_SUFFIX = ".txt";

    private static DeviceLogUtil sInstance = new DeviceLogUtil();

    private DeviceLogUtil() {

    }

    public static DeviceLogUtil getInstance() {
        return sInstance;
    }

    /**
     * 将日志信息写入SD卡
     *
     * @param imei 设备号
     * @param content 日志内容
     */
    @SuppressLint({"SimpleDateFormat", "LogNotTimber"})
    public String onWriterContentToSDCard(String imei, String content, String time){
        //如果SD卡不存在或无法使用，则无法将日志信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.e(TAG, "sdcard unmounted,skip dump exception");
                return "";
            }
        }
        File dir = new File(PATH);
        if (!dir.exists()) {
            /** 注意这里是 mkdirs()方法 可以创建多个文件夹 */
            dir.mkdirs();
        }
        //得到当前年月日时分秒
        FILE_NAME = imei + "_" + time;
        //在定义的Crash文件夹下创建文件
        File file = new File(PATH + FILE_NAME + FILE_NAME_SUFFIX);
        if (!file.exists()) {
            try {
                //在指定的文件夹中创建文件
                file.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try{
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            //写入时间
            pw.println(content);
            pw.close();//关闭输入流
            LogUtils.e("path=" + PATH + FILE_NAME + FILE_NAME_SUFFIX);
        } catch (Exception e) {
            Log.e(TAG,"dump crash info failed");
        }
        return PATH + FILE_NAME + FILE_NAME_SUFFIX;
    }

}
