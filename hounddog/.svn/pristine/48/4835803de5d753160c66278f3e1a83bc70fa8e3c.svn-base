package com.slxk.hounddog.db;

import com.slxk.hounddog.app.MyApplication;

import java.sql.SQLException;

public class MyDao extends OrmDaoUtils {
    private static MyDbHelper instance;

    public MyDao(Class cls) throws SQLException {
        super(cls);
    }

    /**
     * 单例获取该Helper
     *
     * @return
     */
    @Override
    public synchronized OrmDatabaseHelper getHelper() {
        if (instance == null) {
            synchronized (MyDao.class) {
                if (instance == null)
                    instance = new MyDbHelper(MyApplication.getMyApp());
            }
        }
        return instance;
    }
}