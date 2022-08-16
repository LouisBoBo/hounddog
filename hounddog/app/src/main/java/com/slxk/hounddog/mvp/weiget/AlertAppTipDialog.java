package com.slxk.hounddog.mvp.weiget;

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
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.AlertBean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 一般提示警告操作dialog - 提示文字居中对齐
 * Created by Administrator on 2019\6\21 0021.
 */

public class AlertAppTipDialog extends DialogFragment {

    private TextView mTvTitle; // 提示
    private TextView mTvAlert; // 提示语
    private Button mBtnConfirm; // 确定
    private TextView tvAlertHint; // 副标题

    private AlertBean alertBean;
    private onAlertDialogChange alertDialogChange;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_alert_tip, null);
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

        mTvAlert = dialog.findViewById(R.id.tv_alert);
        mBtnConfirm = dialog.findViewById(R.id.btn_confirm);
        mTvTitle = dialog.findViewById(R.id.tv_alert_title);
        tvAlertHint = dialog.findViewById(R.id.tv_alert_hint);

        if (alertBean == null){
            alertBean = new AlertBean();
        }
        if (!TextUtils.isEmpty(alertBean.getAlert())){
            mTvAlert.setText(alertBean.getAlert());
        }
        if (!TextUtils.isEmpty(alertBean.getTitle())){
            mTvTitle.setText(alertBean.getTitle());
        }
        if (!TextUtils.isEmpty(alertBean.getAlertSmall())){
            tvAlertHint.setVisibility(View.VISIBLE);
            tvAlertHint.setText(alertBean.getAlertSmall());
        }else{
            tvAlertHint.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(alertBean.getConfirmTip())){
            mBtnConfirm.setText(alertBean.getConfirmTip());
        }

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialogChange != null){
                    alertDialogChange.onConfirm();
                }
                dismiss();
            }
        });

    }

    public void show(FragmentManager manager, AlertBean bean, onAlertDialogChange change){
        if (isAdded()){
            dismiss();
        }
        this.alertBean = bean;
        this.alertDialogChange = change;
        super.show(manager, "AlertAppDialog");
    }

    public interface onAlertDialogChange{

        void onConfirm();

    }

}
