package com.slxk.hounddog.db;

import android.content.Context;

import java.util.List;

public class MyDbHelper extends OrmDatabaseHelper {

    private static final String DEF_DB_NAME = "sqlite.db";
    private static final int DB_VERSION = 1;


    public MyDbHelper(Context context) {
        super(context, DEF_DB_NAME, null, DB_VERSION);
    }

    @Override
    public void createTables(List tables) {
        tables.add(RecordModel.class);
        tables.add(RecordTimeModel.class);
        tables.add(LanguageModel.class);
        tables.add(BluetoothModel.class);
        tables.add(DeviceModel.class);
        tables.add(DeviceTrackModel.class);
        tables.add(MapDownloadModel.class);
        tables.add(MapXYModel.class);
    }

    @Override
    public void updateTables(List tables) {
        tables.add(RecordModel.class);
        tables.add(RecordTimeModel.class);
        tables.add(LanguageModel.class);
        tables.add(BluetoothModel.class);
        tables.add(DeviceModel.class);
        tables.add(DeviceTrackModel.class);
        tables.add(MapDownloadModel.class);
        tables.add(MapXYModel.class);
    }
}