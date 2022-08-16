package com.slxk.hounddog.mvp.weiget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.model.bean.PhoneAreaResultBean;
import com.slxk.hounddog.mvp.ui.adapter.PhoneAreaAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 选择手机号码国际区号
 */
public class PhoneAreaDialog extends DialogFragment {

    private ImageView ivClose;
    private RecyclerView recyclerView;
    private List<PhoneAreaResultBean.ItemsBean> phoneAreaBeans; // 手机号码国际区号
    private PhoneAreaAdapter mAdapter;
    private onPhoneAreaChange areaChange;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup viewGroup = (ViewGroup) View.inflate(getActivity(), R.layout.dialog_phone_area, null);
        dialog.setContentView(viewGroup);
        dialog.setCanceledOnTouchOutside(false);
        initView(dialog);
        return dialog;
    }

    private void initView(Dialog dialog) {
        try {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ivClose = dialog.findViewById(R.id.iv_close);
        recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PhoneAreaAdapter(R.layout.item_phone_area, phoneAreaBeans);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (areaChange != null){
                    areaChange.onPhoneArea(phoneAreaBeans.get(position).getZone());
                }
                dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show(FragmentManager manager, List<PhoneAreaResultBean.ItemsBean> itemsBeans, onPhoneAreaChange change){
        if (isAdded()){
            dismiss();
        }
        this.phoneAreaBeans = itemsBeans;
        this.areaChange = change;
        super.show(manager, "PhoneAreaDialog");
    }

    public interface onPhoneAreaChange{

        /**
         * 选择的区号
         * @param zone 区号
         */
        void onPhoneArea(int zone);

    }

}
