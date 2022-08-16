package com.slxk.hounddog.mvp.utils;
import io.microshow.rxffmpeg.RxFFmpegInvoke;

/**
 * 录音转换
 */
public class AmrToWavNSUtil {
    private static volatile AmrToWavNSUtil amrToWavNSUtil;
    private getResultListener resultListener;

    private AmrToWavNSUtil() {
    }

    public static AmrToWavNSUtil getAmrToWavNSUtilInstance() {
        if (amrToWavNSUtil == null) {
            synchronized (AmrToWavNSUtil.class) {
                if (amrToWavNSUtil == null) {
                    amrToWavNSUtil = new AmrToWavNSUtil();
                }
            }
        }
        return amrToWavNSUtil;
    }

    public AmrToWavNSUtil onAmrToWavConversion(String path, String targetPath) {
        try {
            String ffmpegCommand = "ffmpeg -y -i " + path
                    + " -ar 8000 -ac 1 -ab 5.15k -f wav " + targetPath;
            String[] commands = ffmpegCommand.split(" ");
            RxFFmpegInvoke.getInstance()
                    .runCommand(commands,new myIFFmpegListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public interface getResultListener {
        //返回转换结果
        void setResult(String str);
    }

    public AmrToWavNSUtil setResultListener(getResultListener listener) {
        this.resultListener = listener;
        return this;

    }


    class myIFFmpegListener implements RxFFmpegInvoke.IFFmpegListener {

        @Override
        public void onFinish() {
            resultListener.setResult("0");
        }

        @Override
        public void onProgress(int progress, long progressTime) {
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(String message) {
            resultListener.setResult("1");
        }
    }
}

