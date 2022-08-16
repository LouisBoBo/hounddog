package com.slxk.hounddog.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2017\6\29 0029.
 */


@DatabaseTable(tableName = "db_language_yuanjin")
public class LanguageModel {

    /**
     * 主键key
     */
    public static final String KEY = "keyId";

    @DatabaseField(generatedId = true)
    private int id;

    // 提示语id
    @DatabaseField(columnName = "keyId", unique = true)
    private String keyId;

    // 提示语
    @DatabaseField(columnName = "keyValue")
    private String keyValue; // 提示语

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
}
