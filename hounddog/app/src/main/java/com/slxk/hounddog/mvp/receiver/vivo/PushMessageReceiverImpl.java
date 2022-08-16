package com.slxk.hounddog.mvp.receiver.vivo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.model.UnvarnishedMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

public class PushMessageReceiverImpl extends OpenClientPushMessageReceiver {

    @SuppressLint("LogNotTimber")
    @Override
    public void onReceiveRegId(Context context, String s) {
        Log.d("tanliang", " onReceiveRegId= " + s);
    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onTransmissionMessage(Context context, UnvarnishedMessage unvarnishedMessage) {
        super.onTransmissionMessage(context, unvarnishedMessage);
        Toast.makeText(context, " 收到透传通知： " + unvarnishedMessage.getMessage(), Toast.LENGTH_LONG).show();
        Log.e("onTransmissionMessage", " onTransmissionMessage= " + unvarnishedMessage.getMessage());
    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage unvarnishedMessage) {
        super.onNotificationMessageClicked(context, unvarnishedMessage);
        Toast.makeText(context, " 收到通知点击回调： " + unvarnishedMessage.toString(), Toast.LENGTH_LONG).show();
        Log.e("onNotificationMessage", " onTransmissionMessage= " + unvarnishedMessage.toString());
    }

}
