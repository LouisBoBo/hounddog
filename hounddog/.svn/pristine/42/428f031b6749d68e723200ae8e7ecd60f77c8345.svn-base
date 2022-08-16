package com.slxk.hounddog.mvp.model.bean;

import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;

/**
 * 版本更新
 */
public class CheckAppUpdateBean extends BaseBean {

    /**
     * info : 631c2a6d-7239-434d-9673-57b30e718b46
     * is_force : false
     * name : 56438a3b-1456-4761-baf5-37e2c299a5b5
     * url : 093d1446-d6fd-47db-b8d1-c70999dcf7a7
     * version : 1232991701
     */

    private String info = ""; // 需要展示的新版本信息
    private boolean is_force; // 是否强制升级，false-不强制更新 true-强制更新
    private String name = ""; // 新版本名称
    private String url = ""; // 如果需要升级，这里展示需要升级的url
    private int version; // 新版本号
    private String type = ResultDataUtils.App_Update_Three; // 下载方式，e_dt_interior 内部下载 , e_dt_th3 第三方

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isIs_force() {
        return is_force;
    }

    public void setIs_force(boolean is_force) {
        this.is_force = is_force;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
