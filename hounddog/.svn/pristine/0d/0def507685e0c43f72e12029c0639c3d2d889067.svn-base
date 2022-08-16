package com.slxk.hounddog.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public abstract class OrmDaoUtils<T> {

    private static final int INSERT = 1;
    private static final int UPDATE = 2;
    private static final int DELETE = 3;


    private OrmDatabaseHelper mHelper;
    private Dao mDao;

    protected abstract OrmDatabaseHelper getHelper();



    public OrmDaoUtils(Class cls) throws SQLException {
        if (mHelper == null) {
            mHelper = getHelper();
        }
        this.mDao = mHelper.getDao(cls);
    }

    /**
     * 查询，通过ID
     * @param objId 查询数据的ID
     * @return 返回查询 Bean 数据，如果未查询到则返回 null
     * @throws SQLException
     */
    public T queryById(Integer objId) throws SQLException {
        return (T) mDao.queryForId(objId);
    }

    /**
     * 查询指定 Bean 所有数据
     * @return 返回查询 Bean 数据，如果未查询到则返回 null
     * @throws SQLException
     */
    public List<T> queryAll() throws SQLException {
        return mDao.queryForAll();
    }

    /**
     * 查询，条件查询
     * <br/> 说明： 该方法要和 {@link OrmDaoUtils#query(QueryBuilder)} 方法组合使用
     * <br/> 在查询操作中，查询的条件最后都要 通过 {@link OrmDaoUtils#query(QueryBuilder)} 方法执行查询
     * <br/> 此方法 对应SQL ：SELECT * FROM `t_person` WHERE `id` = 2
     * @param whereMap
     * @return
     * @throws SQLException
     */
    public QueryBuilder queryBuilder(Map<String, Object> whereMap) throws SQLException {

        QueryBuilder<T, Integer> queryBuilder = mDao.queryBuilder();
        if (whereMap != null && !whereMap.isEmpty()) {
            Where<T, Integer> wheres = queryBuilder.where();
            Set<String> keys = whereMap.keySet();
            ArrayList<String> keyss = new ArrayList<String>();
            keyss.addAll(keys);
            for (int i = 0; i < keyss.size(); i++) {
                if (i == 0) {
                    wheres.eq(keyss.get(i), whereMap.get(keyss.get(i)));
                } else {
                    wheres.and().eq(keyss.get(i), whereMap.get(keyss.get(i)));
                }
            }
        }

        return queryBuilder;
    }

    /**
     * 升序或降序
     * @param orderColumn 升序、降序字段
     * @param isASC true 升序 ，false 降序
     * @return
     * @throws SQLException
     */
    public QueryBuilder queryOrderBy(String orderColumn ,boolean isASC) throws SQLException {
        return queryOrderBy(null,orderColumn,isASC);
    }

    /**
     *
     * @param isASC 参数 false 表示降序，true 表示升序。
     * @return
     * @throws SQLException
     */

    /**
     * 带条件 升序或降序查询
     * @param queryBuilder 查询条件
     * @param orderColumn 升序、降序字段
     * @param isASC true 升序 ，false 降序
     * @return
     * @throws SQLException
     */
    public QueryBuilder queryOrderBy(QueryBuilder queryBuilder, String orderColumn , boolean isASC) throws SQLException {
        if(queryBuilder == null){
            queryBuilder = mDao.queryBuilder();
        }

        if(orderColumn != null){
            queryBuilder.orderBy(orderColumn,isASC);
        }
        return queryBuilder;
    }

    public List<T> query(QueryBuilder builder) throws SQLException{
        PreparedQuery<T> preparedQuery = builder.prepare();
        return mDao.query(preparedQuery);
    }



    /**
     * 新增数据
     *
     * @param obj
     * @throws SQLException
     */
    public int insert(T obj) throws SQLException {
        return mDao.create(obj);
    }

    /**
     * 新增数据
     *
     * @param obj
     * @throws SQLException
     */
    public T insertIfNotExists(T obj) throws SQLException {
        return (T) mDao.createIfNotExists(obj);
    }

    /**
     * 批量添加数据
     * @param objs
     * @return
     * @throws SQLException
     */
    public int insert(Collection<T> objs) throws SQLException {
        return mDao.create(objs);
    }

    /**
     * 使用事务批量添加数据
     * @param objs
     * @return
     * @throws SQLException
     */
    public boolean insertInTransaction(final List<T> objs) throws SQLException {
        return baseTransaction(INSERT, objs);
    }


    /**
     * 删除数据
     * @param obj
     * @throws SQLException
     */
    public void delete(T obj) throws SQLException {
        mDao.delete(obj);
    }

    /**
     * 批量删除数据
     * @param objs
     * @return
     * @throws SQLException
     */
    public int delete(Collection<T> objs) throws SQLException {
        return mDao.delete(objs);
    }

    /**
     * 通过ID 删除数据
     * @param objId
     * @throws SQLException
     */
    public void deleteById(Integer objId) throws SQLException {
        mDao.deleteById(objId);
    }

    /**
     * 使用事务批量删除事务（实体）
     * @param objs
     * @return
     * @throws SQLException
     */
    public boolean deleteInTransaction(final List<T> objs) throws SQLException {
        return baseTransaction(DELETE, objs);
    }

    /**
     * 使用事务批量删除事务（ID）
     * @param objIds
     * @return
     * @throws SQLException
     */
    public boolean deleteInTransactionById(final List<Integer> objIds) throws SQLException {
        Boolean b = TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    for (Integer objId : objIds) {
                        mDao.deleteById(objId);
                    }
                } catch (SQLException e) {
                    return false;
                }
                return true;
            }
        });
        return b;
    }


    /**
     * 修改数据
     * @param obj
     * @return
     * @throws SQLException
     */
    public int update(T obj) throws SQLException {
        return mDao.update(obj);
    }

    /**
     * 条件修改数据
     * @param whereMap 修改条件
     * @param columnName 修改字段名
     * @param columnValue 修改内容
     * @return
     * @throws SQLException
     */
    public int update(Map<String, Object> whereMap, String columnName, Object columnValue) throws SQLException {
        Map<String, Object> updateValue = new HashMap<String, Object>();
        updateValue.put(columnName, columnValue);
        return this.update(whereMap, updateValue);
    }


    /**
     * 条件修改数据
     * @param whereMap
     * @param updateValue
     * @return
     * @throws SQLException
     */
    public int update(Map<String, Object> whereMap, Map<String, Object> updateValue) throws SQLException {
        UpdateBuilder updateBuilder = mDao.updateBuilder();
        if (updateValue != null && !updateValue.isEmpty()) {
            Set<String> valueSet = updateValue.keySet();
            for (String valueKey : valueSet) {
                updateBuilder.updateColumnValue(valueKey, updateValue.get(valueKey));
            }
        } else {
            return -1;
        }

        if (whereMap != null && !whereMap.isEmpty()) {
            Where<T, Long> where = updateBuilder.where();
            Set<String> whereKeySet = whereMap.keySet();
            List<String> keys = new ArrayList<String>();
            keys.addAll(whereKeySet);
            for (int i = 0; i < keys.size(); i++) {
                if (i == 0) {
                    where.eq(keys.get(i), whereMap.get(keys.get(i)));
                } else {
                    where.and().eq(keys.get(i), whereMap.get(keys.get(i)));
                }
            }
        }
        return mDao.update(updateBuilder.prepare());
    }

    /**
     * 创建或修改数据
     * <br/> 如果没有数据就添加，如果数据存在就修改
     * @param obj
     * @return
     * @throws SQLException
     */
    public Dao.CreateOrUpdateStatus createOrUpdate(T obj) throws SQLException {
        return mDao.createOrUpdate(obj);
    }

    /**
     * 批量创建或修改数据
     * <br/> 如果没有数据就添加，如果数据存在就修改
     * @param objs
     * @return
     * @throws SQLException
     */
    public Dao.CreateOrUpdateStatus createOrUpdate(Collection<T> objs) throws SQLException {
        return mDao.createOrUpdate(objs);
    }

    /**
     * 在事务中批量修改数据
     * @param objs
     * @return
     * @throws SQLException
     */
    public boolean updateInTransaction(final List<T> objs) throws SQLException {
        return baseTransaction(UPDATE, objs);
    }

    private boolean baseTransaction(final int type, final List<T> objs) throws SQLException {
        Boolean b = TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    switch (type) {
                        case INSERT:
                        case UPDATE:
                            for (T obj : objs) {
                                mDao.createOrUpdate(obj);
                            }
                            break;

                        case DELETE:
                            for (T obj : objs) {
                                mDao.delete(obj);
                            }
                            break;
                    }
                } catch (SQLException e) {
                    return false;
                }
                return true;
            }
        });
        return b;
    }


    /**
     * 删除所有数据
     *
     * @return
     */
    public int deleteAll(String tableName) {
        try {
            return mDao.executeRaw("delete from "+ tableName + " where 1=1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 分页获取设备轨迹数据
     * @param imei
     * @param start_time 开始时间戳
     * @param end_time 结束时间戳
     * @param number 请求数量
     * @param offset_number 从第几条开始获取数据
     * @return
     */
    public List<DeviceTrackModel> getDeviceTrackPagination(String imei, long start_time, long end_time, long number, long offset_number) {
        try {
//            ("select * from "+ tableName + " where imei=" + imei + "and timestamp > " + start_time
//                    + " and timestamp < " + end_time);
            return mDao.queryBuilder()
                    .orderBy("timestamp", true) // 以什么条件为排序基准，参数false表示降序，true表示升序。
                    .limit(number) // 查询条数
                    .offset(offset_number) // 从第几条开始查询，索引0开始
                    .where()
                    .eq("imei", imei)
                    .and()
                    .le("timestamp", end_time) // 查询条件，lt：小于什么条件
                    .and()
                    .ge("timestamp", start_time) // 查询条件，gt：大于什么条件
                    .query();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除指定数据
     * @param imei 设备号
     * @throws SQLException
     */
    public void deleteDeviceData(String imei) throws SQLException {
        try{
            DeleteBuilder<DeviceTrackModel, Integer> deleteBuilder = mDao.deleteBuilder();
            deleteBuilder.where().eq(DeviceTrackModel.IMEI, imei);
            deleteBuilder.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 删除离线地图XY坐标数据
     * @param time 时间戳
     * @throws SQLException
     */
    public void deleteMapXYData(String time) throws SQLException {
        try{
            DeleteBuilder<MapXYModel, Integer> deleteBuilder = mDao.deleteBuilder();
            deleteBuilder.where().eq(MapXYModel.TIME, time);
            deleteBuilder.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
