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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.DeviceGroupResultBean;
import com.slxk.hounddog.mvp.ui.adapter.GroupListAdapter;
import com.slxk.hounddog.mvp.utils.ToastUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 选择分组
 */
public class GroupSelectDialog extends DialogFragment {

    private RecyclerView recyclerView;
    private Button btnCancel;
    private Button btnConfirm;
    private TextView tvTitle;
    private onGroupSelectferDeviceChange transferDeviceChange;
    private List<DeviceGroupResultBean.GaragesBean> groupBeans; // 设备分组
    private GroupListAdapter mAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CenterInDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_device_group_select, null);
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
            params.height = ScreenUtils.getScreenHeight() / 2;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = dialog.findViewById(R.id.recyclerView);
        btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        tvTitle = dialog.findViewById(R.id.tv_title);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new GroupListAdapter(R.layout.item_group_list, groupBeans);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (DeviceGroupResultBean.GaragesBean bean : groupBeans){
                    bean.setSelect(false);
                }
                groupBeans.get(position).setSelect(true);
                mAdapter.notifyDataSetChanged();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transferDeviceChange != null){
                    transferDeviceChange.onCancel();
                }
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
     * 确认提交
     */
    private void onConfirm(){
        String sid = "";
        for (DeviceGroupResultBean.GaragesBean bean : groupBeans){
            if (bean.isSelect()){
                sid = bean.getSid();
                break;
            }
        }
        if (TextUtils.isEmpty(sid)){
            ToastUtils.show(getString(R.string.group_select_hint));
            return;
        }

        if (transferDeviceChange != null){
            transferDeviceChange.onGroupSelect(sid);
        }
        dismiss();
    }

    /**
     *
     * @param manager
     * @param datas
     * @param change
     */
    public void show(FragmentManager manager, List<DeviceGroupResultBean.GaragesBean> datas, onGroupSelectferDeviceChange change){
        if (isAdded()){
            dismiss();
        }
        this.groupBeans = datas;
        this.transferDeviceChange = change;
        super.show(manager, "GroupSelectDialog");
    }

    public interface onGroupSelectferDeviceChange{

        /**
         * 转移设备
         * @param sid 分组名称
         */
        void onGroupSelect(String sid);

        /**
         * 取消
         */
        void onCancel();

    }

}
