package com.slxk.hounddog.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2017\6\29 0029.
 */


@DatabaseTable(tableName = "db_record_time")
public class RecordTimeModel {

    /**
     * 主键key
     */
    public static final String KEY = "key";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "key")
    private String key;

    @DatabaseField(columnName = "recordTime", unique = true)
    private long recordTime; // 录音时间的时间戳，秒级

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

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

}
