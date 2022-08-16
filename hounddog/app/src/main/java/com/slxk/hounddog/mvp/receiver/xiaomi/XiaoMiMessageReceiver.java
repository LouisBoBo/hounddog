package com.slxk.hounddog.mvp.receiver.xiaomi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.slxk.hounddog.mvp.receiver.DefineMessageBeanFactory;
import com.slxk.hounddog.mvp.utils.DateUtils;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 1、PushMessageReceiver 是个抽象类，该类继承了 BroadcastReceiver。<br/>
 * 2、需要将自定义的 DemoMessageReceiver 注册在 AndroidManifest.xml 文件中：
 * <pre>
 * {@code
 *  <receiver
 *      android:name="com.xiaomi.mipushdemo.DemoMessageReceiver"
 *      android:exported="true">
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.ERROR" />
 *      </intent-filter>
 *  </receiver>
 *  }</pre>
 * 3、DemoMessageReceiver 的 onReceivePassThroughMessage 方法用来接收服务器向客户端发送的透传消息。<br/>
 * 4、DemoMessageReceiver 的 onNotificationMessageClicked 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法会在用户手动点击通知后触发。<br/>
 * 5、DemoMessageReceiver 的 onNotificationMessageArrived 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数。<br/>
 * 6、DemoMessageReceiver 的 onCommandResult 方法用来接收客户端向服务器发送命令后的响应结果。<br/>
 * 7、DemoMessageReceiver 的 onReceiveRegisterResult 方法用来接收客户端向服务器发送注册命令后的响应结果。<br/>
 * 8、以上这些方法运行在非 UI 线程中。
 *
 * @author mayixiang
 */
public class XiaoMiMessageReceiver extends PushMessageReceiver {

    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

    /**
     * 接收服务器发送的透传消息
     * @param context
     * @param message
     */
    @SuppressLint({"LogNotTimber", "LongLogTag"})
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        LogUtils.e("onReceivePassThroughMessage", message.toString());
        mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
        Map<String, String> payloadExtra = message.getExtra();//透传信息
        String payloadMsg = payloadExtra.get("message");
        //提示透传信息
        if (payloadMsg != null && payloadMsg.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(payloadMsg);
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
    }

    /**
     * 接收服务器发来的通知栏消息（用户点击通知栏时触发）
     * @param context
     * @param message
     */
    @SuppressLint({"LogNotTimber", "LongLogTag"})
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        Log.e("onNotificationMessageClicked", message.toString());
        mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }

//        Intent alarm = new Intent(context, AlarmRecordActivity.class);
//        alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        context.startActivity(alarm);
    }

    /**
     * 接收服务器发来的通知栏消息（消息到达客户端时触发，并且可以接收应用在前台时不弹出通知的通知消息）
     * @param context
     * @param message
     */
    @SuppressLint({"LogNotTimber", "LongLogTag"})
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        Log.e("onNotificationMessageArrived", message.toString());
        mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
    }

    /**
     * 接收客户端向服务器发送命令消息后返回的响应
     * @param context
     * @param message
     */
    @SuppressLint("LogNotTimber")
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        Log.e("onCommandResult", message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log = "";
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                log = "注册推送成功。";
            } else {
                log = "注册推送失败。";
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                log = "设置别名" + mAlias + "成功";
            } else {
                log = message.getReason() + "设置别名失败";
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                log = "取消设置别名" + mAlias + "成功";
            } else {
                log = message.getReason() + "未设置别名失败";
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
                log = "订阅主题" + mTopic + "成功";
            } else {
                log = message.getReason() + "订阅主题失败";
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
                log = "取消订阅主题" + mTopic + "成功";
            } else {
                log = message.getReason() + "取消订阅主题失败";
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
                log = "设置接收时间" + mStartTime + "-" + mEndTime;
            } else {
                log = message.getReason() + "设置接收时间失败";
            }
        }
        LogUtils.e("onCommandResult", log);
    }

    /**
     * 接受客户端向服务器发送注册命令消息后返回的响应
     * @param context
     * @param message
     */
    @SuppressLint("LogNotTimber")
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        Log.e("onReceiveRegisterResult", message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log = "";
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                log = "注册推送成功。";
            }else{
                log = "注册推送失败。";
            }
        } else {
            log = message.getReason();
        }

        Log.e("onReceiveRegisterResult", log);
    }

}
