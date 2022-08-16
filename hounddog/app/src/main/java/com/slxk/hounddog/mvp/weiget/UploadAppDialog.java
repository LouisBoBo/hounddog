package com.slxk.hounddog.mvp.weiget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.mvp.model.bean.CheckAppUpdateBean;
import com.slxk.hounddog.mvp.utils.DateUtils;
import com.slxk.hounddog.mvp.utils.DownloadUtil;
import com.slxk.hounddog.mvp.utils.FileUtilApp;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 版本更新提示
 * Created by Administrator on 2019\6\21 0021.
 */

public class UploadAppDialog extends DialogFragment {

    private TextView mTvVersion; // 提示
    private TextView mTvAlert; // 提示语
    private Button mBtnCancel; // 取消
    private Button mBtnConfirm; // 确定
    private TextView tvTitle;

    private CheckAppUpdateBean updateBean;
    private onAlertDialogChange alertDialogChange;
    private int mUploadProgress = 0; // 下载进度

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_app_upload, null);
        dialog.setContentView(viewGroup);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false); // 点击屏幕或物理返回键，true：dialog消失，false：不消失
        initView(dialog);
        return dialog;
    }

    @SuppressLint("SetTextI18n")
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

        tvTitle = dialog.findViewById(R.id.tv_alert_title);
        mTvAlert = dialog.findViewById(R.id.tv_alert);
        mBtnCancel = dialog.findViewById(R.id.btn_cancel);
        mBtnConfirm = dialog.findViewById(R.id.btn_confirm);
        mTvVersion = dialog.findViewById(R.id.tv_version_name);

        if (updateBean == null){
            updateBean = new CheckAppUpdateBean();
        }
        mTvVersion.setText("V" + updateBean.getName());
        mTvAlert.setText(updateBean.getInfo());

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadUtil.getInstance().downLoadCancel();
                if (alertDialogChange != null){
                    alertDialogChange.onCancel();
                }
                dismiss();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(updateBean.getType()) && !TextUtils.isEmpty(updateBean.getUrl())){
                    if (updateBean.getType().equals(ResultDataUtils.App_Update_Interior)){
                        onAppDownLoadUrl();
                    }else{
                        if (alertDialogChange != null){
                            alertDialogChange.onThreeDownLoad();
                        }
                    }
                }else{
                    ToastUtils.showShort(getString(R.string.download_falied));
                    dismiss();
                }
            }
        });

    }

    /**
     * app下载apk
     */
    private void onAppDownLoadUrl(){
        if (updateBean.getUrl().startsWith("http") || updateBean.getUrl().startsWith("https")){
            mBtnConfirm.setText(getString(R.string.soft_updating));
            mBtnConfirm.setEnabled(false);
            //储存下载文件的目录
            try{
                DownloadUtil.getInstance().download(updateBean.getUrl(),
                        FileUtilApp.getSDPath(MyApplication.getMyApp()) + FileUtilApp.FileDownLoad,
                        MyApplication.getMyApp().getString(R.string.app_name)
                                + "_" + DateUtils.getTodayDateTime(),
                        new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(String path) {
                        if (isAdded()){
//                            ToastUtil.showShort("安装包已保存到" + FileUtil.FileRecordName);
                            if (mBtnConfirm != null){
                                mBtnConfirm.setEnabled(true);
                                if (alertDialogChange != null){
                                    alertDialogChange.onAppDownLoad(path);
                                }
                            }
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDownloading(int progress) {
                        if (isAdded()){
                            if (mBtnConfirm != null){
                                if (progress >= mUploadProgress){
                                    mUploadProgress = progress;
                                }
                                mBtnConfirm.setText(getString(R.string.soft_updating) + "：" + mUploadProgress + "%");
                                if (mUploadProgress == 100){
                                    mBtnConfirm.setText(getString(R.string.soft_updating_success));
                                }
                            }
                        }
                    }

                    @Override
                    public void onDownloadFailed() {
                        if (isAdded()){
                            ToastUtils.showShort(getString(R.string.download_falied));
                            if (mBtnConfirm != null){
                                mBtnConfirm.setEnabled(true);
                                mBtnConfirm.setText(getString(R.string.update_app));
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            ToastUtils.showShort(getString(R.string.download_falied));
        }
    }

    public void show(FragmentManager manager, CheckAppUpdateBean bean, onAlertDialogChange change){
        if (isAdded()){
            dismiss();
        }
        this.updateBean = bean;
        this.alertDialogChange = change;
        super.show(manager, "UploadAppDialog");
    }

    public interface onAlertDialogChange{

        /**
         * app下载
         */
        void onAppDownLoad(String path);

        /**
         * 第三方下载
         */
        void onThreeDownLoad();

        /**
         * 取消
         */
        void onCancel();

    }

}
