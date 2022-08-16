package com.slxk.hounddog.mvp.weiget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.LocationModeTime;
import com.slxk.hounddog.mvp.ui.adapter.LocationModeTimeAdapter;
import com.slxk.hounddog.mvp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 定位模式定位时间选择
 */
public class LocationTimeDialog extends DialogFragment {

    private TextView tvCancel;
    private RecyclerView recyclerView;
    private onLocationTimeChange mLocationTimeChange;
    private List<LocationModeTime.ListBean> mModeTimeBeans;
    private LocationModeTimeAdapter mAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_location_time, null);
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
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvCancel = dialog.findViewById(R.id.tv_cancel);
        recyclerView = dialog.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mModeTimeBeans == null){
            mModeTimeBeans = new ArrayList<>();
        }
        if (mModeTimeBeans.size() > 0){
            mModeTimeBeans.get(mModeTimeBeans.size() - 1).setEnd(true);
        }
        mAdapter = new LocationModeTimeAdapter(R.layout.item_location_mode_time, mModeTimeBeans);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Utils.isButtonQuickClick()){
                    if (mLocationTimeChange != null){
                        mLocationTimeChange.onTimeSelect(mModeTimeBeans.get(position).getV());
                    }
                    dismiss();
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show(FragmentManager manager, List<LocationModeTime.ListBean> list, onLocationTimeChange change){
        if (isAdded()){
            dismiss();
        }
        this.mModeTimeBeans = list;
        this.mLocationTimeChange = change;
        super.show(manager, "LocationTimeDialog");
    }

    public interface onLocationTimeChange{

        /**
         * 选择的时间
         * @param time
         */
        void onTimeSelect(int time);

    }

}
