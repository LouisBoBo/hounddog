package com.slxk.hounddog.mvp.weiget;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 隐私政策说明协议
 */
public class PrivacyPolicyDialog extends DialogFragment implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvPrivacyPolicyOne;
    private TextView tvPrivacyPolicy;
    private Button btnNonuse; // 暂不使用
    private Button btnAgree; // 同意
    private onPrivacyPolicyChange policyChange;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_privacy_policy, null);
        dialog.setContentView(viewGroup);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);
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
        tvPrivacyPolicyOne = dialog.findViewById(R.id.tv_privacy_policy_one);
        tvPrivacyPolicy = dialog.findViewById(R.id.tv_privacy_policy);
        btnNonuse = dialog.findViewById(R.id.btn_nonuse);
        btnAgree = dialog.findViewById(R.id.btn_agree);

        tvPrivacyPolicy.setText(Html.fromHtml(getString(R.string.privacy_policy_2)));
        tvPrivacyPolicy.setOnClickListener(this);
        btnNonuse.setOnClickListener(this);
        btnAgree.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_privacy_policy:
                if (policyChange != null){
                    policyChange.onSeePrivacyPolicy();
                }
                break;
            case R.id.btn_nonuse:
                if (policyChange != null){
                    policyChange.onPrivacyPolicy(false);
                }
                break;
            case R.id.btn_agree:
                if (policyChange != null){
                    policyChange.onPrivacyPolicy(true);
                }
                break;
        }
    }

    public void show(FragmentManager manager, onPrivacyPolicyChange change){
        if (isAdded()){
            dismiss();
        }
        this.policyChange = change;
        super.show(manager, "PrivacyPolicyDialog");
    }

    public interface onPrivacyPolicyChange{

        /**
         * 选择
         * @param isPrivacy false：不同意，true：同意
         */
        void onPrivacyPolicy(boolean isPrivacy);

        /**
         * 查看隐私政策协议
         */
        void onSeePrivacyPolicy();

    }

}
