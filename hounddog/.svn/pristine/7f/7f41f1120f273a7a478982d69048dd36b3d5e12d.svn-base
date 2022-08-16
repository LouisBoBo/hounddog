package com.slxk.hounddog.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2017\6\29 0029.
 */


@DatabaseTable(tableName = "db_record_bean")
public class RecordModel {

    /**
     * 主键key
     */
    public static final String KEY = "key";

    /**
     * 名字，查询条件
     */
    public static final String NAME = "name";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "path")
    private String path;

    @DatabaseField(columnName = "second")
    private int second;

    @DatabaseField(columnName = "isPlayed")
    private boolean isPlayed; // 是否已播放过了

    @DatabaseField(columnName = "key")
    private String key;

    @DatabaseField(columnName = "name", unique = true)
    private String name;

    @DatabaseField(columnName = "recordTime")
    private long recordTime; // 录音时间的时间戳，秒级

    @DatabaseField(columnName = "recordTimeShow")
    private String recordTimeShow; // 用于显示的录音时间

    private boolean isDelete; // 是否是删除编辑状态

    private boolean isSelect; // 是否选中

    private boolean isPlayNow; // 是否正在播放

    public boolean isPlayNow() {
        return isPlayNow;
    }

    public void setPlayNow(boolean playNow) {
        isPlayNow = playNow;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public String getRecordTimeShow() {
        return recordTimeShow;
    }

    public void setRecordTimeShow(String recordTimeShow) {
        this.recordTimeShow = recordTimeShow;
    }
}
