package com.slxk.hounddog.mvp.weiget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.slxk.hounddog.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 录音删除处理进度条
 */
public class RecordTaskProgressDialog extends DialogFragment {

    private TextView tvTitle;
    private ProgressBar progressBar;
    private TextView tvProgress;
    private onProgressBarChange progressBarChange;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_task_progress, null);
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

        tvTitle = dialog.findViewById(R.id.tv_progress_title);
        progressBar = dialog.findViewById(R.id.progress_bar);
        tvProgress = dialog.findViewById(R.id.tv_progress);
    }

    public void show(FragmentManager manager, onProgressBarChange change){
        if (isAdded()){
            dismiss();
        }
        this.progressBarChange = change;
        super.show(manager, "RecordTaskProgressDialog");
    }

    /**
     * 上报进度
     * @param progress 任务进度
     * @param isFinish 是否完成
     */
    @SuppressLint("SetTextI18n")
    public void setProgressBar(int progress, boolean isFinish){
        progressBar.setProgress(progress);
        tvProgress.setText(progress + "%");
        if (progress >= 100 || isFinish){
            if (progressBarChange != null){
                progressBarChange.onTaskProgressFinish();
            }
            dismiss();
        }
    }

    public interface onProgressBarChange{

        /**
         * 处理完成
         */
        void onTaskProgressFinish();

    }

}
