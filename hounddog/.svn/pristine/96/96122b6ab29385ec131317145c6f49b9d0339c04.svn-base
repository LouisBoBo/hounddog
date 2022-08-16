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
 * 手机号已修改提示框
 */
public class PhoneModifyDialog extends DialogFragment implements View.OnClickListener {

    private Button btnConfirm;
    private String txt;
    private TextView tvPhone;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_phone_has_modify, null);
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

        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        tvPhone = dialog.findViewById(R.id.tv_phone);
        tvPhone.setText(txt);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();

    }

    public void show(FragmentManager manager, String text){
        if (isAdded()){
            dismiss();
        }
        this.txt = text;
        super.show(manager, "PhoneHasBindDialog");
    }

}
