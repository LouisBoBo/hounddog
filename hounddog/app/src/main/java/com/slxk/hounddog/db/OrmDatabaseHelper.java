package com.slxk.hounddog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class OrmDatabaseHelper<T> extends OrmLiteSqliteOpenHelper {

    private List<Class<T>> DBtables = new ArrayList<Class<T>>();

    public OrmDatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            DBtables.clear();
            createTables(DBtables);
            for (Class table : DBtables) {
                TableUtils.createTable(connectionSource, table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, final ConnectionSource connectionSource, int i, int i1) {
        try {
            TransactionManager.callInTransaction(connectionSource, new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    DBtables.clear();
                    updateTables(DBtables);
                    try {
                        for (Class table : DBtables) {
                            TableUtils.dropTable(connectionSource, table, true);
                            TableUtils.createTable(connectionSource, table);
                        }
                    }catch (SQLException e){
                        return false;
                    }
                    return true;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public abstract void createTables(List<Class<T>> tables);

    public abstract void updateTables(List<Class<T>> tables);

}