package com.slxk.hounddog.mvp.weiget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.mvp.utils.ConstantValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 合并账号
 */
public class MergeAccountDialog extends DialogFragment implements View.OnClickListener {

    private Button btnNoMoreReminders;
    private Button btnMerge;
    private ImageView ivClose;
    private TextView tvMobile,tvTitle;
    private onMergeAccountChange accountChange;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_merge_account, null);
        dialog.setContentView(viewGroup);
        setCancelable(false); // 点击屏幕或物理返回键，true：dialog消失，false：不消失
        dialog.setCanceledOnTouchOutside(false); // 点击屏幕，dialog不消失；点击物理返回键dialog消失
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

        btnNoMoreReminders = dialog.findViewById(R.id.btn_no_more_reminders);
        btnMerge = dialog.findViewById(R.id.btn_merge);
        ivClose = dialog.findViewById(R.id.iv_close);
        tvMobile = dialog.findViewById(R.id.tv_mobile);
        tvTitle = dialog.findViewById(R.id.tv_title);
        tvMobile.setText(getString(R.string.mobile) + "：" + ConstantValue.getAccount());
        btnNoMoreReminders.setOnClickListener(this);
        btnMerge.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_no_more_reminders:
                MyApplication.getMyApp().setBeforeAccount(false);
                MyApplication.getMyApp().setMergeAccount(false);
                SPUtils.getInstance().put(ConstantValue.Is_No_More_Reminders, true);
                dismiss();
                break;
            case R.id.btn_merge:
                onMergeAccount();
                break;
            case R.id.iv_close:
                MyApplication.getMyApp().setBeforeAccount(false);
                MyApplication.getMyApp().setMergeAccount(false);
                SPUtils.getInstance().put(ConstantValue.Is_No_More_Reminders, false);
                dismiss();
                break;
        }
    }


    /**
     /**
     * 合并账号
     */
    private void onMergeAccount(){
        if (accountChange != null){
            accountChange.onMergeAccount();
        }
        dismiss();
    }

    public void show(FragmentManager manager, onMergeAccountChange change){
        if (isAdded()){
            dismiss();
        }
        this.accountChange = change;
        super.show(manager, "MergeAccountDialog");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface onMergeAccountChange{

        /**
         * 发送验证码
         */
        void onSendCode();

        /**
         * 合并账号
         */
        void onMergeAccount();
    }

}
