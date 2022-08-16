package com.slxk.hounddog.mvp.weiget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.mvp.utils.DownloadUtil;
import com.slxk.hounddog.mvp.utils.FileUtilApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 下载蓝牙升级包
 */
public class BluetoothUpgradeDownloadDialog extends DialogFragment {

    private TextView tvProgress;
    private String mDownloadUrl; // 下载的url地址
    private String mVersionName; // 版本名称
    private onBluetoothUpgradeChange upgradeChange;
    private int mUploadProgress = 0; // 下载进度

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_bluetooth_upgrade_download, null);
        dialog.setContentView(viewGroup);
        setCancelable(false); // 点击屏幕或物理返回键，true：dialog消失，false：不消失
        dialog.setCanceledOnTouchOutside(false); // 点击屏幕，dialog不消失；点击物理返回键dialog消失
        initView(dialog);
        return dialog;
    }

    private void initView(Dialog dialog) {
        try {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = ScreenUtils.getScreenWidth() * 3 / 4;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvProgress = dialog.findViewById(R.id.tv_progress);

        if (!TextUtils.isEmpty(mDownloadUrl)){
            onStartDownLoadUrl();
        }else{
            ToastUtils.showShort(getString(R.string.upload_url_error));
        }
    }

    /**
     * 开始下载
     */
    private void onStartDownLoadUrl(){
        if (mDownloadUrl.startsWith("http") || mDownloadUrl.startsWith("https")){
            tvProgress.setText(getString(R.string.download_progress));
            //储存下载文件的目录
            try{
                DownloadUtil.getInstance().download(mDownloadUrl,
                        FileUtilApp.getSDPath(MyApplication.getMyApp()) + FileUtilApp.FileDownLoad,
                        mVersionName,
                        new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(String path) {
                                if (isAdded()){
//                            ToastUtil.showShort("安装包已保存到" + FileUtil.FileRecordName);
                                    onDownloadAccept(path);
                                }
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDownloading(int progress) {
                                if (isAdded()){
                                    if (tvProgress != null){
                                        if (progress >= mUploadProgress){
                                            mUploadProgress = progress;
                                        }
                                        tvProgress.setText(getString(R.string.download_progress) + "：" + mUploadProgress + "%");
                                    }
                                }
                            }

                            @Override
                            public void onDownloadFailed() {
                                if (isAdded()){
                                    ToastUtils.showShort(getString(R.string.download_falied));
                                    dismiss();
                                }
                            }
                        });
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            ToastUtils.showShort(getString(R.string.upload_url_error));
        }
    }

    private void onDownloadAccept(String path){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tvProgress != null){
                    if (upgradeChange != null){
                        upgradeChange.onDownLoadAccept(path);
                    }
                    dismiss();
                }
            }
        }, 1000);
    }

    public void show(FragmentManager manager, String url, String version_name, onBluetoothUpgradeChange change){
        if (isAdded()){
            dismiss();
        }
        this.mVersionName = version_name;
        this.mDownloadUrl = url;
        this.upgradeChange = change;
        super.show(manager, "BluetoothUpgradeDownloadDialog");
    }

    public interface onBluetoothUpgradeChange{

        /**
         * 下载完成
         * @param path 本地文件链接
         */
        void onDownLoadAccept(String path);

        /**
         * 取消
         */
        void onCancel();

    }

}
