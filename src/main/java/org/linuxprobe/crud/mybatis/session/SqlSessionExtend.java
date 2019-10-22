package org.linuxprobe.crud.mybatis.session;

import org.linuxprobe.crud.core.query.BaseQuery;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SqlSessionExtend {
    /**
     * 根据查询对象查询符合条件的数据
     */
    public <T> List<T> universalSelect(BaseQuery param);

    /**
     * 根据查询对象查询符合条件的数据
     *
     * @param classLoader 代理对象的classloader
     */
    public <T> List<T> universalSelect(BaseQuery param, ClassLoader classLoader);

    /**
     * 查询符合条件的记录数
     */
    public long selectCount(BaseQuery param);

    /**
     * 根据主键查询数据
     */
    public <T> T selectByPrimaryKey(Serializable id, Class<T> type);

    /**
     * 根据主键查询数据
     *
     * @param classLoader 代理对象的classloader
     */
    public <T> T selectByPrimaryKey(Serializable id, Class<T> type, ClassLoader classLoader);

    /**
     * 根据列作为条件查询数据
     */
    public <T> List<T> selectByColumn(String column, Serializable columnValue, Class<T> type);

    /**
     * 根据列作为条件查询数据
     *
     * @param classLoader 代理对象的classloader
     */
    public <T> List<T> selectByColumn(String column, Serializable columnValue, Class<T> type, ClassLoader classLoader);

    /**
     * 根据类成员名称作为条件查询数据
     */
    public <T> List<T> selectByField(String fieldName, Serializable fieldValue, Class<T> type);

    /**
     * 根据类成员名称作为条件查询数据
     *
     * @param classLoader 代理对象的classloader
     */
    public <T> List<T> selectByField(String fieldName, Serializable fieldValue, Class<T> type, ClassLoader classLoader);

    /**
     * 根据列作为条件查询数据
     */
    public <T> T selectOneByColumn(String column, Serializable columnValue, Class<T> type);

    /**
     * 根据列作为条件查询数据
     *
     * @param classLoader 代理对象的classloader
     */
    public <T> T selectOneByColumn(String column, Serializable columnValue, Class<T> type, ClassLoader classLoader);

    /**
     * 根据类成员名称作为条件查询数据
     */
    public <T> T selectOneByField(String fieldName, Serializable fieldValue, Class<T> type);

    /**
     * 根据类成员名称作为条件查询数据
     *
     * @param classLoader 代理对象的classloader
     */
    public <T> T selectOneByField(String fieldName, Serializable fieldValue, Class<T> type, ClassLoader classLoader);

    /**
     * 插入
     */
    public <T> T insert(T record);

    /**
     * 插入
     *
     * @param classLoader 代理对象的classloader
     */
    public <T> T insert(T record, ClassLoader classLoader);

    /**
     * 批量插入, 循环插入模式在数据库自增主键模式下能够回写主键, 并且返回代理对象, 非循环插入不能回写主键, 不返回代理对象
     *
     * @param records 插入的数据
     * @param loop    是否使用循环插入
     */
    public <T> List<T> batchInsert(Collection<T> records, boolean loop);

    /**
     * 批量插入, 循环插入模式在数据库自增主键模式下能够回写主键, 并且返回代理对象, 非循环插入不能回写主键, 不返回代理对象
     *
     * @param records     插入的数据
     * @param loop        是否使用循环插入
     * @param classLoader 代理对象的classloader
     */
    public <T> List<T> batchInsert(Collection<T> records, boolean loop, ClassLoader classLoader);

    /**
     * 根据主键删除
     */
    public int deleteByPrimaryKey(Serializable id, Class<?> entityType);

    /**
     * 根据主键批量删除
     */
    public int batchDeleteByPrimaryKey(Collection<Serializable> ids, Class<?> entityType);

    /**
     * 删除
     */
    public int delete(Object record);

    /**
     * 批量删除
     */
    public int batchDelete(Collection<?> records);

    /**
     * 根据列名生成删除sql
     *
     * @param columnName  列名
     * @param columnValue 列值
     * @param modelType   model类型
     */
    public int deleteByColumnName(String columnName, Serializable columnValue, Class<?> modelType);

    /**
     * 根据列名生成删除sql，列名称与列值请一一对象
     *
     * @param columnNames  列名
     * @param columnValues 列值
     * @param modelType    model类型
     */
    public int deleteByColumnNames(String[] columnNames, Serializable[] columnValues, Class<?> modelType);

    /**
     * 根据类的成员名称生成删除sql
     *
     * @param fieldName  属性名称
     * @param fieldValue 属性值
     * @param modelType  model类型
     */
    public int deleteByFieldName(String fieldName, Serializable fieldValue, Class<?> modelType);

    /**
     * 根据属性删除，属性名称与属性值请一一对应
     *
     * @param fieldNames  属性名称
     * @param fieldValues 属性值
     * @param modelType   model类型
     */
    public int deleteByFieldNames(String[] fieldNames, Serializable[] fieldValues, Class<?> modelType);

    /**
     * 根据sql查询数据
     */
    public List<Map<String, Object>> selectBySql(String sql);

    /**
     * 根据sql查询唯一数据
     */
    public Map<String, Object> selectOneBySql(String sql);

    /**
     * 根据sql查询数据
     */
    public <T> List<T> selectBySql(String sql, Class<T> type);

    /**
     * 根据sql查询数据
     *
     * @param classLoader 代理对象的classloader
     */
    public <T> List<T> selectBySql(String sql, Class<T> type, ClassLoader classLoader);

    /**
     * 根据sql查询唯一数据
     */
    public <T> T selectOneBySql(String sql, Class<T> type);

    /**
     * 根据sql查询唯一数据
     *
     * @param classLoader 代理对象的classloader
     */
    public <T> T selectOneBySql(String sql, Class<T> type, ClassLoader classLoader);

    /**
     * 全字段更新
     */
    public <T> T globalUpdate(T record);

    /**
     * 全字段更新
     *
     * @param classLoader 代理对象的classloader
     */
    public <T> T globalUpdate(T record, ClassLoader classLoader);

    /**
     * 非空字段更新
     */
    public <T> T localUpdate(T record);

    /**
     * 非空字段更新
     *
     * @param classLoader 代理对象的classloader
     */
    public <T> T localUpdate(T record, ClassLoader classLoader);
}
