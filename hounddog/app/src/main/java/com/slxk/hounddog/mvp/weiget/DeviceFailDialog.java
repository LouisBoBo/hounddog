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
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.DeviceBaseResultBean;
import com.slxk.hounddog.mvp.ui.adapter.DeviceFailAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 添加失败的设备
 */
public class DeviceFailDialog extends DialogFragment {

    private TextView tvTitle;
    private RecyclerView recyclerView;
    private Button btnCancel;
    private Button btnConfirm;
    private List<DeviceBaseResultBean.FailItemsBean> failItemsBeans;
    private DeviceFailAdapter mAdapter;
    // 提示类型,0:添加设备失败,1:转移设备失败,2:围栏相关-修改/增加失败, 3:删除设备失败
    // 4:冻结设备失败, 5:恢复至原账号失败的设备, 6:删除账号失败, 7:设备解绑电话号码失败
    private int mType = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_add_device_fail, null);
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
            params.height = ScreenUtils.getScreenHeight() / 2;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (failItemsBeans == null){
            failItemsBeans = new ArrayList<>();
        }

        tvTitle = dialog.findViewById(R.id.tv_title);
        recyclerView = dialog.findViewById(R.id.recyclerView);
        btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new DeviceFailAdapter(R.layout.item_add_device_fail, failItemsBeans);
        recyclerView.setAdapter(mAdapter);
        switch (mType){
            case 0:
                tvTitle.setText(getString(R.string.add_device_fail) + " (" + failItemsBeans.size() + ")");
                break;
            case 7:
                tvTitle.setText(getString(R.string.unbind_phone_fail) + " (" + failItemsBeans.size() + ")");
                break;
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
                dismiss();
            }
        });
    }

    public void show(FragmentManager manager, List<DeviceBaseResultBean.FailItemsBean> datas, int type){
        if (isAdded()){
            dismiss();
        }
        this.failItemsBeans = datas;
        this.mType = type;
        super.show(manager, "DeviceFailDialog");
    }

}
