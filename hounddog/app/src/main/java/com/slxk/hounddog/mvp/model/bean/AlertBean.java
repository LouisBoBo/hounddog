package com.slxk.hounddog.mvp.model.bean;

/**
 * 提示警告bean
 * Created by Administrator on 2019\6\21 0021.
 */

public class AlertBean {

    private String title = ""; // 标题
    private String alert = ""; // 提示警告语
    private String alertSmall = ""; // 副提示语，可选显示
    private String confirmTip = ""; // 确定按钮文字
    private String cancelTip = ""; // 取消按钮文字

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getAlertSmall() {
        return alertSmall;
    }

    public void setAlertSmall(String alertSmall) {
        this.alertSmall = alertSmall;
    }

    public String getConfirmTip() {
        return confirmTip;
    }

    public void setConfirmTip(String confirmTip) {
        this.confirmTip = confirmTip;
    }

    public String getCancelTip() {
        return cancelTip;
    }

    public void setCancelTip(String cancelTip) {
        this.cancelTip = cancelTip;
    }
}
