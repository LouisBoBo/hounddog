package com.slxk.hounddog.wxapi.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.slxk.hounddog.R;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXFileObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信分享
 * Created by Administrator on 2019\6\19 0019.
 */

public class WXShareHelper {

    private IWXAPI msgApi;
    private Context mContext;
    private int mTargetScene = SendMessageToWX.Req.WXSceneTimeline;

    public WXShareHelper(Context context){
        super();
        this.mContext = context;
        msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID_WX);
        msgApi.registerApp(Constants.APP_ID_WX);
    }

    /**
     *  分享
     * @param type 0：微信好友，1：朋友圈
     * @param shareUrl 分享链接
     * @param description 分享描述
     * @param title 分享标题
     */
    public void doShare(int type, String shareUrl, String description, String title){
        if (type == 1){
            mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
        }else{
            mTargetScene = SendMessageToWX.Req.WXSceneSession;
        }

        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareUrl;

        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
//        msg.title = mContext.getString(R.string.app_name);
        msg.title = title;
        msg.description = description;
        Bitmap thumbBmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_logo_400);
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message =msg;
        req.scene = mTargetScene;
//        req.userOpenId = getOpenId();

        //调用api接口，发送数据到微信
        msgApi.sendReq(req);
    }

    /**
     * 分享 录音
     *
     * @param shareUrl    分享链接
     * @param title       分享标题
     * @param description 分享描述
     */
    public void doShareRecord(String shareUrl, String title, String description) {
        //初始化一个WXWebpageObject，填写url
        WXFileObject fileObject = new WXFileObject();
        fileObject.setFilePath(shareUrl);

        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(fileObject);
        msg.title = title;
        msg.description = description;
        Bitmap thumbBmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_logo_400);
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("file");
        req.message = msg;
        req.scene = mTargetScene;
//        req.userOpenId = getOpenId();

        //调用api接口，发送数据到微信
        msgApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
