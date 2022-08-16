package com.slxk.hounddog.mvp.model.bean;

/**
 * 设备上报日志内容bean
 */
public class DeviceLogBean {

    private String device_number = ""; // 设备号码（接收机）
    private String log_time = ""; // 日志时间，文件名后半部分
    private StringBuilder log_content = new StringBuilder(); // 日志内容(分包上传，需要拼接)
    private boolean isComplete = false; // 是否接受完整

    public void addContent(String content){
        if (log_content == null){
            log_content = new StringBuilder();
        }
        log_content.append(content);
    }

    public String getDevice_number() {
        return device_number;
    }

    public void setDevice_number(String device_number) {
        this.device_number = device_number;
    }

    public String getLog_time() {
        return log_time;
    }

    public void setLog_time(String log_time) {
        this.log_time = log_time;
    }

    public StringBuilder getLog_content() {
        return log_content;
    }

    public void setLog_content(StringBuilder log_content) {
        this.log_content = log_content;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
