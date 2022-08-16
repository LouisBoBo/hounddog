package com.slxk.hounddog.mvp.weiget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.blankj.utilcode.util.ScreenUtils;
import com.slxk.hounddog.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 绑定手机号，手机号已绑定其他账号或设备号
 */
public class PhoneBindAccountDialog extends DialogFragment implements View.OnClickListener {

    private Button btnCancel;
    private Button btnRegister;
    private onPhoneHasBindChange phoneHasBindChange;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_phone_bind_account, null);
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
            params.width = ScreenUtils.getScreenWidth() * 4 / 5;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnRegister = dialog.findViewById(R.id.btn_register);
        btnCancel.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                if (phoneHasBindChange != null){
                    phoneHasBindChange.onLogin();
                }
                dismiss();
                break;
            case R.id.btn_register:
                if (phoneHasBindChange != null){
                    phoneHasBindChange.onRegister();
                }
                dismiss();
                break;
        }
    }

    public void show(FragmentManager manager, onPhoneHasBindChange change){
        if (isAdded()){
            dismiss();
        }
        this.phoneHasBindChange = change;
        super.show(manager, "PhoneBindAccountDialog");
    }

    public interface onPhoneHasBindChange{

        void onRegister();

        void onLogin();

    }

}
