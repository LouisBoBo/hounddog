package com.slxk.hounddog.wxapi.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		final IWXAPI api = WXAPIFactory.createWXAPI(context, Constants.APP_ID_WX, true);
		// 将该app注册到微信
		api.registerApp(Constants.APP_ID_WX);
	}
}
