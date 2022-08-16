package com.slxk.hounddog.mvp.model.bean;

import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;

/**
 * 查询有轨迹的日期
 */
public class TrackHasDataResultBean extends BaseBean {

    private List<Long> date;

    public List<Long> getDate() {
        return date;
    }

    public void setDate(List<Long> date) {
        this.date = date;
    }
}
