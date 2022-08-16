package com.slxk.hounddog.mvp.weiget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.slxk.hounddog.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 绑定手机号码
 */
public class BindMobileDialog extends DialogFragment {

    private TextView tvTitle; // 标题
    private TextView tvTip; // 提示语
    private Button mBtnCancel; // 取消
    private Button mBtnConfirm; // 确定
    private onBindMobileChange bindMobileChange;
    private boolean isForeignDevice = false; // 是否是国外的设备号，国外的设备不需要绑定手机号
    boolean isModifyPassword = false; // 是否需要修改密码
    boolean isBindMobile = false; // 是否需要绑定手机号码

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_bind_mobile, null);
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

        tvTitle = dialog.findViewById(R.id.tv_title);
        tvTip = dialog.findViewById(R.id.tv_tip);
        mBtnCancel = dialog.findViewById(R.id.btn_cancel);
        mBtnConfirm = dialog.findViewById(R.id.btn_confirm);

        if (isForeignDevice){
            tvTitle.setText(getString(R.string.modify_password));
            tvTip.setText(getString(R.string.change_password_hint));
            mBtnCancel.setText(getString(R.string.not_modify));
            mBtnConfirm.setText(getString(R.string.modify));
        }else{
            if (isModifyPassword && !isBindMobile){
                tvTitle.setText(getString(R.string.modify_password));
                tvTip.setText(getString(R.string.change_password_hint));
                mBtnCancel.setText(getString(R.string.not_modify));
                mBtnConfirm.setText(getString(R.string.modify));
            }else{
                tvTitle.setText(getString(R.string.bind_mobile));
                tvTip.setText(getString(R.string.not_bind_mobile));
                mBtnCancel.setText(getString(R.string.not_band));
                mBtnConfirm.setText(getString(R.string.bind));
            }
        }

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bindMobileChange != null){
                    bindMobileChange.onCancel();
                }
                dismiss();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bindMobileChange != null){
                    bindMobileChange.onConfirm();
                }
                dismiss();
            }
        });
    }

    public void show(FragmentManager manager, boolean foreignDevice, boolean modifyPassword, boolean bindMobile, onBindMobileChange change){
        if (isAdded()){
            dismiss();
        }
        this.isForeignDevice = foreignDevice;
        this.isModifyPassword = modifyPassword;
        this.isBindMobile = bindMobile;
        this.bindMobileChange = change;
        super.show(manager, "BindMobileDialog");
    }

    public interface onBindMobileChange{

        void onConfirm();

        void onCancel();

    }

}
