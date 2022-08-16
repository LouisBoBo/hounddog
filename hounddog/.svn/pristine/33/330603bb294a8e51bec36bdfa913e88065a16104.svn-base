package com.slxk.hounddog.mvp.utils;

import android.os.Environment;

import java.io.File;

public class MapImageCache {

    private static MapImageCache mNetImageViewCache = new MapImageCache();

    private MapImageCache() {

    }

    public static MapImageCache getInstance() {
        return mNetImageViewCache;
    }
    /**
     * 判断图片是否存在首先判断内存中是否存在然后判断本地是否存在
     *
     * @param url
     * @return
     */
    public boolean isBitmapExit(String url, String path) {
        //boolean isExit = containsKey(url);
        boolean isExit = false;
        isExit = isLocalHasBmp(url, path);
        return isExit;
    }

    /*
     * 判断本地有没有
     */
    private boolean isLocalHasBmp(String url, String path) {
        boolean isExit = true;
        String name = url;
        String filePath = isCacheFileIsExit(path);
        File file = new File(filePath, name);
        if (file.exists()) {
        } else {
            isExit = false;
        }
        return isExit;
    }

    /*
     * 判断缓存文件夹是否存在如果存在怎返回文件夹路径，如果不存在新建文件夹并返回文件夹路径
     */
    private String isCacheFileIsExit(String path) {
        String filePath = "";
        String rootpath = "";

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            rootpath = Environment.getExternalStorageDirectory().toString();
        }
        filePath = rootpath + path;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return filePath;
    }
}
