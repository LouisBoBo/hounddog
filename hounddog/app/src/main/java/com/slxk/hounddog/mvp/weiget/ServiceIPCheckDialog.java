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
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 切换域名或ip
 */
public class ServiceIPCheckDialog extends DialogFragment {

    private EditText edtServiceIP;
    private Button btnCancel; // 取消
    private Button btnConfirm; // 确定
    private onServiceIPCheckChange checkChange;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_app_service_ip_check, null);
        dialog.setContentView(viewGroup);
        dialog.setCanceledOnTouchOutside(true);
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

        edtServiceIP = dialog.findViewById(R.id.edt_service_ip);
        btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        if (!TextUtils.isEmpty(SPUtils.getInstance().getString(ConstantValue.APP_Service_Ip, ""))){
            edtServiceIP.setText(SPUtils.getInstance().getString(ConstantValue.APP_Service_Ip, ""));
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirm();
            }
        });
    }

    /**
     * 确定提交
     */
    private void onConfirm(){
        String serviceIp = edtServiceIP.getText().toString().trim();
        if (!TextUtils.isEmpty(serviceIp)){
            if (serviceIp.contains("/mapi")){
                serviceIp = serviceIp.replace("/mapi", "");
            }
        }
        serviceIp = Utils.replaceBlank(serviceIp);
        SPUtils.getInstance().put(ConstantValue.APP_Service_Ip, serviceIp.trim());
        ToastUtils.show(getString(R.string.set_success));
        if (checkChange != null){
            checkChange.onCheck();
        }
        dismiss();
    }

    public void show(FragmentManager manager, onServiceIPCheckChange change){
        if (isAdded()){
            dismiss();
        }
        this.checkChange = change;
        super.show(manager, "ServiceIPCheckDialog");
    }

    public interface onServiceIPCheckChange{

        void onCheck();

    }

}
