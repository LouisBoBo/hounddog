package com.slxk.hounddog.wxapi.helper;

import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付工具类
 */
public class WXPayHelper {

	IWXAPI msgApi;

	public WXPayHelper(Context context) {
		super();
		msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID_WX);
		msgApi.registerApp(Constants.APP_ID_WX);
	}

	/**
	 * 使用后台返回的支付参数支付
	 * 
	 * @param weChatPayBean
	 */
	public void doPay(WeChatPayBean weChatPayBean) {

		PayReq request = new PayReq();
		request.appId = weChatPayBean.getData().getAppid();
		request.partnerId = weChatPayBean.getData().getPartnerid();
		request.prepayId = weChatPayBean.getData().getPrepayid();
		request.packageValue = weChatPayBean.getData().getPackageX();
		request.nonceStr = weChatPayBean.getData().getNoncestr();
		request.timeStamp = weChatPayBean.getData().getTimestamp();
		request.sign = weChatPayBean.getData().getSign();

		// 发起支付
		msgApi.sendReq(request);
	}

}
