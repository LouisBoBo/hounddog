package com.slxk.hounddog.mvp.receiver.huawei;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.slxk.hounddog.mvp.receiver.DefineMessageBeanFactory;
import com.slxk.hounddog.mvp.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 华为推送服务类
 */
public class HaodogHmsMessageService extends HmsMessageService {

    private static final String TAG = "HaodogHmsMessageService";
    private final static String CODELABS_ACTION = "com.slxk.hounddog.intent.action";

    /**
     * 服务端更新token回调方法
     *
     * @param token
     */
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
//        Log.e(TAG, "receive token:" + token);
        if(!TextUtils.isEmpty(token)) {
            sendTokenToDisplay(token);
        }

        Intent intent = new Intent();
        intent.setAction(CODELABS_ACTION);
        intent.putExtra("method", "onNewToken");
        intent.putExtra("msg", token);

        sendBroadcast(intent);
    }

    private void sendTokenToDisplay(String token) {
//        Intent intent = new Intent("com.huawei.codelabpush.ON_NEW_TOKEN");
//        intent.putExtra("token", token);
//        sendBroadcast(intent);
    }

    /**
     * 申请token失败回调方法。
     *
     * @param e
     */
    @SuppressLint("LogNotTimber")
    @Override
    public void onTokenError(Exception e) {
        super.onTokenError(e);
        Log.e(TAG, "receive token error");
    }

    /**
     * 接收透传消息方法。
     *
     * @param remoteMessage
     */
    @SuppressLint("LogNotTimber")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().length() > 0) {
            // {"msg_content":{"carno":"新款车载","imei":"8019370505","msg_content":"震动报警","msg_title":"震动报警(新款车载)","time":"1636789218"},"type":1}
            String payloadData = remoteMessage.getData();//透传信息
            try {
                JSONObject jsonObject = new JSONObject(payloadData);
                String msgData = jsonObject.getString("msg_content");
                Gson gson = new Gson();
                DefineMessageBeanFactory bean = gson.fromJson(msgData, DefineMessageBeanFactory.class);
                String content = "";
                String imei = String.valueOf(bean.getImei());
                if (TextUtils.isEmpty(bean.getCarno())) {
                    content = imei;
                } else {
                    content = bean.getCarno();
                }
                ToastUtils.showShort(DateUtils.timeConversionDate_two(String.valueOf(bean.getTime())) + "，" + content + "，" + bean.getMsg_content());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        if (remoteMessage.getNotification() != null) {
//            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
    }

    /**
     * 发送上行消息成功回调方法。
     *
     * @param s
     */
    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    /**
     * 发送上行消息失败回调方法。
     *
     * @param s
     * @param e
     */
    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    /**
     * 发送上行消息时如果使用了消息回执能力，消息到达App服务器后，App服务器的应答消息通过本方法回调给应用。
     *
     * @param s
     * @param e
     */
    @Override
    public void onMessageDelivered(String s, Exception e) {
        super.onMessageDelivered(s, e);
    }

}
