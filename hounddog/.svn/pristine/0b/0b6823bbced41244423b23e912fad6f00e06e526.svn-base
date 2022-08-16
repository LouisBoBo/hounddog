package com.slxk.hounddog.mvp.weiget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.slxk.hounddog.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 传输蓝牙升级包进度
 */
public class UpgradeProgressDialog extends DialogFragment {

    private TextView tvProgress;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_upgrade_progress, null);
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
        setUpgradeProgress(0);
    }

    public void show(FragmentManager manager){
        if (isAdded()){
            dismiss();
        }
        super.show(manager, "UpgradeProgressDialog");
    }

    /**
     * 设置进度
     * @param progress
     */
    @SuppressLint("SetTextI18n")
    public void setUpgradeProgress(int progress){
        tvProgress.setText(getString(R.string.progress) + "：" + progress + "%");
    }

}
