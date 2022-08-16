package com.slxk.hounddog.mvp.ui.adapter;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * 登录账号列表
 */
public class LoginAccountListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public LoginAccountListAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        TextView tvAccount = helper.getView(R.id.tv_account);
        switch (item){
            case ResultDataUtils.Login_type_Account:
            case ResultDataUtils.Login_type_Phone_Account:
                tvAccount.setText(mContext.getString(R.string.account_login));
                break;
            case ResultDataUtils.Login_type_Device:
            case ResultDataUtils.Login_type_Phone_Device:
                tvAccount.setText(mContext.getString(R.string.device_login));
                break;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
    }
}
