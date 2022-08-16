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
 * 取消蓝牙配对确认
 */
public class BlueDisconnectDialog extends DialogFragment {

    private Button btnNo;
    private Button btnYes;
    private TextView tvTip; // 提示语
    private String mTip; // 提示语
    private onBlueDisconnectChange blueDisconnectChange;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_blue_disconnect, null);
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

        btnNo = dialog.findViewById(R.id.btn_no);
        btnYes = dialog.findViewById(R.id.btn_yes);
        tvTip = dialog.findViewById(R.id.tv_tip);
        tvTip.setText(mTip);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blueDisconnectChange != null){
                    blueDisconnectChange.onBlueDisconnect();
                }
                dismiss();
            }
        });
    }

    public void show(FragmentManager manager, String tip, onBlueDisconnectChange change){
        if (isAdded()){
            dismiss();
        }
        this.mTip = tip;
        this.blueDisconnectChange = change;
        super.show(manager, "BlueDisconnectDialog");
    }

    public interface onBlueDisconnectChange{

        /**
         * 确认取消配对
         */
        void onBlueDisconnect();

    }

}
