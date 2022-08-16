package com.slxk.hounddog.mvp.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.slxk.hounddog.db.MapDownloadModel;
import com.slxk.hounddog.db.MapXYModel;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.mvp.model.api.Api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * 离线地图下载工具类
 */
public class OfflineMapDownLoadUtil {

    private static volatile OfflineMapDownLoadUtil mapDownLoadUtil;
    private ArrayList<MapXYModel> mMapXYModels;
    private MyDao mMapDownloadDao;
    private MyDao mMapXYDao;
    private int mDownloadIndex = 0; // 下载到的位置
    private int mImageTotalSize = 0; // 下载任务的图片总数
    private int mProgress = 0; // 下载任务总进度
    private String mDownloadTime = ""; // 当前下载任务的时间戳，判断哪个任务正在进行
    private MapDownloadModel mMapSelectModel; // 选中的任务

    private String ALBUM_PATH = FileUtilApp.File_Google_Map_SatelliteMap; // 存储路径
    private String mapUrl = Api.Map_SatelliteMap; // 请求地图url地址

    private onMapDownloadChange mapDownloadChange;

    public static OfflineMapDownLoadUtil getInstance(){
        if (mapDownLoadUtil == null) {
            synchronized (OfflineMapDownLoadUtil.class) {
                if (mapDownLoadUtil == null) {
                    mapDownLoadUtil = new OfflineMapDownLoadUtil();
                }
            }
        }
        return mapDownLoadUtil;
    }

    public void initMapDownLoad(){
        if (mMapXYModels == null){
            mMapXYModels = new ArrayList<>();
        }
        try{
            if (mMapDownloadDao == null){
                mMapDownloadDao = new MyDao(MapDownloadModel.class);
            }
            if (mMapXYDao == null){
                mMapXYDao = new MyDao(MapXYModel.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 同步信息
     */
    public void onSynchronizeInfo(){
        if (!TextUtils.isEmpty(mDownloadTime)){
            if (mapDownloadChange != null){
                mapDownloadChange.onSynchronizeProgress(mDownloadTime);
            }
        }
    }

    /**
     * 开始下载地图
     * @param xyModels
     * @param selectModel
     */
    public void startMapDownload(ArrayList<MapXYModel> xyModels, MapDownloadModel selectModel){
        mMapXYModels.clear();
        mMapXYModels.addAll(xyModels);
        mMapSelectModel = selectModel;
        mImageTotalSize = mMapXYModels.size();
        mDownloadIndex = mMapSelectModel.getPosition();
        mDownloadTime = mMapSelectModel.getTime();
        if (mHandler != null){
            mHandler.sendEmptyMessageDelayed(1, 0);
        }
    }

    /**
     * 停止下载任务，停止某一个下载任务
     */
    public void stopMapDownload(){
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mMapXYModels != null){
            mMapXYModels.clear();
        }
        mDownloadIndex = 0;
        mImageTotalSize = 0;
        mProgress = 0;
        mDownloadTime = "";
        mMapSelectModel = null;
    }

    /**
     * 退出登录，停止所有任务下载
     */
    public void onDestroy(){
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mMapXYModels != null){
            mMapXYModels.clear();
        }
        mDownloadIndex = 0;
        mImageTotalSize = 0;
        mProgress = 0;
        mDownloadTime = "";
        mMapSelectModel = null;
    }

    /**
     * 倒计时刷新数据
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            startDownloadAction();
        }
    };

    /**
     * 开始下载地图
     */
    private void startDownloadAction(){
        if (mMapXYModels.size() > 0){
            if (mDownloadIndex < mMapXYModels.size()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        onMapXYNext();
                    }
                }).start();
            } else {
                if (mHandler != null){
                    mHandler.removeCallbacksAndMessages(null);
                }
                // 下载完成
                onMapDownloadSuccess();
            }
        }
    }

    /**
     * 下载完成
     */
    private void onMapDownloadSuccess(){
        try{
            if (!TextUtils.isEmpty(mDownloadTime)){
                mMapXYDao.deleteMapXYData(mDownloadTime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (mMapSelectModel != null){
            mMapSelectModel.setProgress(100);
            mMapSelectModel.setPosition(mDownloadIndex - 1);
            mMapSelectModel.setDownload(false);
            try{
                mMapDownloadDao.update(mMapSelectModel);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (mapDownloadChange != null){
            mapDownloadChange.onDownloadSuccess(mMapSelectModel);
        }
        stopMapDownload();
    }


    /**
     * 下载图片
     */
    @SuppressLint("DefaultLocale")
    private void onMapXYNext(){
        try {
            MapXYModel model = mMapXYModels.get(mDownloadIndex);
            String mFileDirName;
            String mFileName;
            mFileDirName = String.format("L%02d/", model.getZoom() + 1);
            mFileName = String.format("%s", TileXYToQuadKey((int) model.getXtile(), (int) model.getYtile(), model.getZoom()));//为了不在手机的图片中显示,取消jpg后缀,文件名自己定义,写入和读取一致即可
            if (MapImageCache.getInstance().isBitmapExit(mFileDirName + mFileName, ALBUM_PATH)) {//判断本地是否有图片文件
                onDownloadProgressShow();
            }else{
                String filePath = String.format(mapUrl, (int) model.getXtile(), (int) model.getYtile(), model.getZoom());
                Bitmap mBitmap = getImageBitmap(getImageStream(filePath));
                try {
                    saveFile(mBitmap, mFileName, mFileDirName, model);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 瓦片数据坐标转换
     */
    private String TileXYToQuadKey(int tileX, int tileY, int levelOfDetail) {
        StringBuilder quadKey = new StringBuilder();
        for (int i = levelOfDetail; i > 0; i--) {
            char digit = '0';
            int mask = 1 << (i - 1);
            if ((tileX & mask) != 0) {
                digit++;
            }
            if ((tileY & mask) != 0) {
                digit++;
                digit++;
            }
            quadKey.append(digit);
        }
        return quadKey.toString();
    }

    public Bitmap getImageBitmap(InputStream imputStream) {
        // 将所有InputStream写到byte数组当中
        byte[] targetData = null;
        byte[] bytePart = new byte[4096];
        while (true) {
            try {
                int readLength = imputStream.read(bytePart);
                if (readLength == -1) {
                    break;
                } else {
                    byte[] temp = new byte[readLength + (targetData == null ? 0 : targetData.length)];
                    if (targetData != null) {
                        System.arraycopy(targetData, 0, temp, 0, targetData.length);
                        System.arraycopy(bytePart, 0, temp, targetData.length, readLength);
                    } else {
                        System.arraycopy(bytePart, 0, temp, 0, readLength);
                    }
                    targetData = temp;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 指使Bitmap通过byte数组获取数据
        return BitmapFactory.decodeByteArray(targetData, 0, targetData.length);
    }

    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * 保存文件
     */
    public void saveFile(final Bitmap bm, final String fileName, final String fileDirName, MapXYModel model) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (bm != null) {
                        File dirFile = new File(getMapPath(ALBUM_PATH) + fileDirName);
                        if (!dirFile.exists()) {
                            dirFile.mkdir();
                        }
                        File myCaptureFile = new File(getMapPath(ALBUM_PATH) + fileDirName + fileName);
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                        bos.flush();
                        bos.close();
                    }

                    onDownloadProgressShow();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 显示下载进度
     */
    private void onDownloadProgressShow(){
        if (mImageTotalSize != 0 && mMapSelectModel != null){
            mProgress = mDownloadIndex * 100 / mImageTotalSize;
            if (mProgress != mMapSelectModel.getProgress()){
                mMapSelectModel.setProgress(mProgress);
                if (mapDownloadChange != null){
                    mapDownloadChange.onDownloadProgress(mDownloadIndex, mProgress);
                }
            }
            mMapSelectModel.setPosition(mDownloadIndex);

            try{
                mMapDownloadDao.update(mMapSelectModel);
            }catch (Exception e){
                e.printStackTrace();
            }

            if (mHandler != null && mMapSelectModel != null && mMapSelectModel.isDownload()){
                mDownloadIndex++;
                mHandler.sendEmptyMessageDelayed(1, 0);
            }
        }
    }

    /**
     * 获取文件夹路径
     *
     * @param path
     * @return
     */
    private String getMapPath(String path) {
        return Environment.getExternalStorageDirectory() + path + "/";
    }

    public void setMapDownloadChange(onMapDownloadChange change){
        this.mapDownloadChange = change;
    }

    public interface onMapDownloadChange{

        /**
         * 更新下载进度
         * @param downloadIndex
         * @param progress
         */
        void onDownloadProgress(int downloadIndex, int progress);

        /**
         * 同步当前下载进度，当前下载的任务
         * @param downloadTime
         */
        void onSynchronizeProgress(String downloadTime);

        /**
         * 下载完成
         * @param model
         */
        void onDownloadSuccess(MapDownloadModel model);

    }

}
