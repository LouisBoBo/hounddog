package com.slxk.hounddog.mvp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.aiyaapp.webrtc.ns.WebRTCNoiseSuppression;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SPUtils;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.db.RecordModel;
import com.slxk.hounddog.db.RecordTimeModel;
import com.slxk.hounddog.mvp.model.bean.RecordResultBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Semaphore;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 录音文件下载工具类
 */
public class RecordDownload {

    private int mRecordCos; // 录音持续时长
    private long mRecordTime; // 录音开始时间戳，下载完成之后，用于作为文件名称
    private String mUrl; // 录音下载地址
    private String packetFileName = "";     //文件名称
    private int currentIndex = 0; //当前请求的文件索引
    private List<RecordResultBean.DataBean> dataBeans; // 需要下载的录音数据
    private boolean isRefresh; // 是否是下拉加载历史录音数据
    private boolean isRecordActivity = true; // 是否是首次进入页面，是的话，存储历史录音时间，用来判断获取实时录音时是否存储历史录音时间

    private SimpleDateFormat dateFormat;
    private Context mContext;
    private String mImei;
    private Handler handler;
    // 使用信号量来
    private Semaphore semaphore;

    // http下载录音文件
    private OkHttpClient okHttpClient;
    // 开启线程下载数据
    private Thread runThread;

    private boolean isHasRecord = false; // 查询数据库是否包含了这条录音，如果包含了，则不需要存储下一次获取历史录音时间，下载历史录音时判断
    private boolean isRealTimeRecord = false; // 下载实时录音数据，判断第一条录音是否已存储在数据库
    private int recordTotal = 0; // 录音剩余总数
    private int limitSize = 0; // 每页请求数

    private MyDao recordBeanDao;
    private MyDao recordTimeBeanDao;

    public RecordDownload(Context context, String imei, Handler tmpHandler, MyDao recordBeanDao, MyDao recordTimeBeanDao) {
        this.mContext = context;
        this.mImei = imei;
        this.handler = tmpHandler;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        okHttpClient = new OkHttpClient();
        this.recordBeanDao = recordBeanDao;
        this.recordTimeBeanDao = recordTimeBeanDao;
    }

    /**
     * 设置录音数据
     *
     * @param data 需要下载的录音数据
     * @param refresh 是否是加载历史录音数据
     * @param isActivity 是否是首次进入页面，是的话，存储历史录音时间，用来判断获取实时录音时是否存储历史录音时间
     * @param total 符合条件的录音总数
     * @param limit_size 每页请求数
     */
    public void setRecordData(List<RecordResultBean.DataBean> data, boolean refresh, boolean isActivity, int total, int limit_size) {
        this.dataBeans = data;
        this.isRefresh = refresh;
        this.isRecordActivity = isActivity;
        this.recordTotal = total;
        this.limitSize = limit_size;

        try {
            this.semaphore = new Semaphore(1, true);
            runThread = new Thread(new Runnable() {
                @SuppressLint("LogNotTimber")
                @Override
                public void run() {
                    try {
//                        Log.e("录音时间节点", "节点总数：" + requestFileList.size());
                        currentIndex = 0;
                        while (true) {
                            semaphore.acquire();
                            if (currentIndex < dataBeans.size()) {
                                mUrl = dataBeans.get(currentIndex).getUrl();
                                packetFileName = mImei + "_" + dataBeans.get(currentIndex).getRecord_time() + ".amr";
//                                Log.e("获取录音节点下的数据", "当前索引：" + currentIndex + ",  节点时间：" + dataBeans.get(currentIndex).getRecord_time()
//                                        + ", 时间：" + DateUtils.timeConversionDate_two(String.valueOf(mRecordTime))
//                                        + ", 录音时长=" + dataBeans.get(currentIndex).getRecord_cos()+ ", url=" + mUrl);
                                onDownLoadRecord(dataBeans.get(currentIndex));
                            } else {
//                                Log.e("录音节点循环结束", "循环结束，最后一个节点：" + packetFileName);
                                if (isRefresh) {
                                    //历史录音下次请求时间
//                                    SPUtils.getInstance().put(ConstantValue.Record_Before_Time + mImei, mRecordTime - 1);

                                    onSaveHistoryTime(mRecordTime - 1);
                                } else {
                                    //实时录音下次请求时间
                                    SPUtils.getInstance().put(ConstantValue.Record_Last_Time + mImei, mRecordTime + 1);

                                    onRealTimeData();
                                }

                                //消息通知实时录音与历史录音下载完成，实时录音情况下启动下一次实时录音获取流程
                                Message msg = new Message();
                                msg.what = isRefresh ? 0x1000 : 0x2000;
                                handler.sendMessage(msg);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            runThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存历史录音时间到数据库，下载实时录音
     */
    @SuppressLint("LogNotTimber")
    private void onRealTimeData(){
        if (dataBeans.size() > 0 && !isRealTimeRecord && isRecordActivity && recordTotal > limitSize){
            RecordTimeModel model = new RecordTimeModel();
            model.setKey("time" + mImei);
            model.setRecordTime(dataBeans.get(0).getRecord_time() - 1);
            try {
                recordTimeBeanDao.insert(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存历史录音时间到数据库，下载历史录音
     * @param recordTime 录音时间
     */
    private void onSaveHistoryTime(long recordTime){
        if (!isHasRecord){
            RecordTimeModel model = new RecordTimeModel();
            model.setKey("time" + mImei);
            model.setRecordTime(recordTime);
            try {
                recordTimeBeanDao.insert(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开始下载录音数据
     *
     * @param bean
     */
    @SuppressLint("LogNotTimber")
    private void onDownLoadRecord(final RecordResultBean.DataBean bean) {
        String fileName = packetFileName.replace(".amr", ".wav");
        List<RecordModel> listRecord = getRecordDataBaseName(fileName);
        if (listRecord != null && listRecord.size() > 0) {
            isHasRecord = true;
            if (currentIndex == 0 && !isRefresh){
                isRealTimeRecord = true;
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentIndex = currentIndex + 1;
                    semaphore.release(1);
                }
            }, 100);
        } else {
            isHasRecord = false;
            if (currentIndex == 0 && !isRefresh){
                isRealTimeRecord = false;
            }

            Request request = new Request.Builder().url(bean.getUrl()).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    onDownLoadFail();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    //储存下载文件的目录
                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        File file = new FileUtilApp().createFileSD(FileUtilApp.getSDPath(mContext) + FileUtilApp.FileDownLoad,
                                String.valueOf(dataBeans.get(currentIndex).getRecord_time()));
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
//                        int progress = (int) (sum * 1.0f / total * 100);
                            //下载中
                        }
                        fos.flush();
                        //下载完成
                        onDownLoadRecordSuccess(readStream(file.getAbsolutePath()), bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                        onDownLoadFail();
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    /**
     * 下载成功，把文件变成二进制流
     *
     * @param filePath 文件路径
     * @return
     * @throws Exception
     */
    public static byte[] readStream(String filePath) throws Exception {
        FileInputStream fs = new FileInputStream(filePath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        FileUtils.delete(filePath);
        return outStream.toByteArray();
    }

    /**
     * 下载录音数据成功
     *
     * @param byteArray 二进制录音数据
     */
    private void onDownLoadRecordSuccess(byte[] byteArray, RecordResultBean.DataBean bean) {
        mRecordCos = bean.getRecord_cos();
        mRecordTime = bean.getRecord_time();
        try {
            if (byteArray != null) {
                if (byteArray.length >= 7020) {
                    //写入文件
                    File file = new FileUtilApp().createFileSD(FileUtilApp.getSDPath(mContext) + FileUtilApp.FileRecordName, packetFileName);
                    if (file != null) {
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(byteArray, 0, byteArray.length);
                        fos.close();

                        //降噪 因OPPO 部分5.1.1手机降噪出错，做以下容错处理
                        String targetPath = "";
                        if (Build.VERSION.SDK_INT >= 23) {
                            targetPath = AmrToWavConversionNoiseSuppression(file.getPath(), true);
                        } else {
                            targetPath = AmrToWavConversion(file.getPath(), true);
                        }
                        String fileName = packetFileName.replace(".amr", ".wav");

                        if (mRecordCos > 0) {
                            //插入数据库
                            RecordModel record = new RecordModel();
                            record.setKey("record" + mImei);
                            record.setSecond(mRecordCos);
                            record.setPlayed(false);
                            record.setPath(targetPath);
                            record.setName(fileName);
                            record.setRecordTimeShow(DateUtils.timeConversionDate_two(String.valueOf(mRecordTime)));
                            record.setRecordTime(mRecordTime);
                            try {
                                recordBeanDao.insert(record);

                                Message msg = new Message();
                                //消息通知实时录音数据与历史录音数据文件写入，刷新列表
                                msg.what = isRefresh ? 0x0100 : 0x0200;
                                msg.obj = record;
                                handler.sendMessage(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentIndex = currentIndex + 1;
                        semaphore.release(1);
                    }
                }, 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 不降噪，只是转换amr格式为wav格式
     * @param path          源文件
     * @param deleteAmrFile true 需要删除源文件， false 分享录音时无需删除
     * @return
     */
    private String AmrToWavConversion(final String path, final boolean deleteAmrFile) {
        final String targetPath = path.replace(".amr", ".wav");
        File target = new File(targetPath);
        //已经存在，表示已经降噪过，直接返回文件路径
        if (target.exists()) {
            return targetPath;
        }
        AmrToWavNSUtil.getAmrToWavNSUtilInstance().setResultListener(
                new AmrToWavNSUtil.getResultListener() {
                    @Override
                    public void setResult(String str) {
                        File fileAmr = new File(path);
                        if (fileAmr.exists() && deleteAmrFile) {
                            fileAmr.delete();
                        }
                    }
                }).onAmrToWavConversion(path, targetPath);
        return targetPath;
    }

    /**
     * 降噪
     * @param path 源文件
     * @param deleteAmrFile true 需要删除源文件， false 无需删除
     * @return
     */
    private String AmrToWavConversionNoiseSuppression(final String path, final boolean deleteAmrFile) {
        final String targetPath = path.replace(".amr", ".wav");
        File target = new File(targetPath);
        //已经存在，表示已经降噪过，直接返回文件路径
        if (target.exists()) {
            return targetPath;
        }
        AmrToWavNSUtil.getAmrToWavNSUtilInstance().setResultListener(
                new AmrToWavNSUtil.getResultListener() {
                    @Override
                    public void setResult(String str) {
                        File fileAmr = new File(path);
                        if (fileAmr.exists() && deleteAmrFile) {
                            fileAmr.delete();
                        }
                        noiseSuppression(targetPath); //降噪
                    }
                }).onAmrToWavConversion(path, targetPath);
        return targetPath;
    }

    private void noiseSuppression(final String input) {
        WebRTCNoiseSuppression.process(input, input, 3);
    }

    /**
     * 下载失败
     */
    @SuppressLint("LogNotTimber")
    private void onDownLoadFail() {
        Log.e("kang", "请求录音数据失败，失败数据为：" + packetFileName);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentIndex = currentIndex + 1;
                semaphore.release(1);
            }
        }, 100);
    }

    /**
     * 从数据库get数据-根据名字name
     * @param name
     * @return
     */
    private List<RecordModel> getRecordDataBaseName(String name) {
        try {
            Map<String, String> map1 = new HashMap<>();
            map1.put(RecordModel.NAME, name);
            return (List<RecordModel>) recordBeanDao.query(recordBeanDao.queryBuilder(map1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭下载对象
     */
    public void cancelDownload() {
        try {
            if (runThread != null){
                runThread.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
