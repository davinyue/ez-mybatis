package org.linuxprobe.crud.mybatis.spring;

import org.apache.ibatis.session.ExecutorType;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSession;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSessionFactory;
import org.linuxprobe.crud.mybatis.session.defaults.UniversalCrudDefaultSqlSessionExtend;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.PersistenceExceptionTranslator;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UniversalCrudSqlSessionTemplate extends SqlSessionTemplate implements UniversalCrudSqlSession {
    private final UniversalCrudDefaultSqlSessionExtend sqlSessionExtend;

    public UniversalCrudSqlSessionTemplate(UniversalCrudSqlSessionFactory sqlSessionFactory, ExecutorType executorType,
                                           PersistenceExceptionTranslator exceptionTranslator) {
        super(sqlSessionFactory, executorType, exceptionTranslator);
        this.sqlSessionExtend = new UniversalCrudDefaultSqlSessionExtend(this);
    }

    public UniversalCrudSqlSessionTemplate(UniversalCrudSqlSessionFactory sqlSessionFactory,
                                           ExecutorType executorType) {
        super(sqlSessionFactory, executorType);
        this.sqlSessionExtend = new UniversalCrudDefaultSqlSessionExtend(this);
    }

    public UniversalCrudSqlSessionTemplate(UniversalCrudSqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
        this.sqlSessionExtend = new UniversalCrudDefaultSqlSessionExtend(this);
    }

    @Override
    public <T> T insert(T record) {
        return this.sqlSessionExtend.insert(record);
    }

    @Override
    public <T> T insert(T record, ClassLoader classLoader) {
        return this.sqlSessionExtend.insert(record, classLoader);
    }

    @Override
    public <T> List<T> batchInsert(Collection<T> records, boolean loop) {
        return this.sqlSessionExtend.batchInsert(records, loop);
    }

    @Override
    public <T> List<T> batchInsert(Collection<T> records, boolean loop, ClassLoader classLoader) {
        return this.sqlSessionExtend.batchInsert(records, loop, classLoader);
    }

    @Override
    public int deleteByPrimaryKey(Serializable id, Class<?> entityType) {
        return this.sqlSessionExtend.deleteByPrimaryKey(id, entityType);
    }

    @Override
    public int batchDeleteByPrimaryKey(Collection<Serializable> ids, Class<?> entityType) {
        return this.sqlSessionExtend.batchDeleteByPrimaryKey(ids, entityType);
    }

    @Override
    public int delete(Object record) {
        return this.sqlSessionExtend.delete(record);
    }

    @Override
    public int batchDelete(Collection<?> records) {
        return this.sqlSessionExtend.batchDelete(records);
    }

    @Override
    public int deleteByColumnName(String columnName, Serializable columnValue, Class<?> modelType) {
        return this.sqlSessionExtend.deleteByColumnName(columnName, columnValue, modelType);
    }

    @Override
    public int deleteByColumnNames(String[] columnNames, Serializable[] columnValues, Class<?> modelType) {
        return this.sqlSessionExtend.deleteByColumnNames(columnNames, columnValues, modelType);
    }

    @Override
    public int deleteByFieldName(String fieldName, Serializable fieldValue, Class<?> modelType) {
        return this.sqlSessionExtend.deleteByFieldName(fieldName, fieldValue, modelType);
    }

    @Override
    public int deleteByFieldNames(String[] fieldNames, Serializable[] fieldValues, Class<?> modelType) {
        return this.sqlSessionExtend.deleteByFieldNames(fieldNames, fieldValues, modelType);
    }

    @Override
    public <T> List<T> universalSelect(BaseQuery param) {
        return this.sqlSessionExtend.universalSelect(param);
    }

    @Override
    public <T> List<T> universalSelect(BaseQuery param, ClassLoader classLoader) {
        return this.sqlSessionExtend.universalSelect(param, classLoader);
    }

    @Override
    public long selectCount(BaseQuery param) {
        return this.sqlSessionExtend.selectCount(param);
    }

    @Override
    public List<Map<String, Object>> selectBySql(String sql) {
        return this.sqlSessionExtend.selectBySql(sql);
    }

    @Override
    public Map<String, Object> selectOneBySql(String sql) {
        return this.sqlSessionExtend.selectOneBySql(sql);
    }

    @Override
    public <T> List<T> selectBySql(String sql, Class<T> type) {
        return this.sqlSessionExtend.selectBySql(sql, type);
    }

    @Override
    public <T> List<T> selectBySql(String sql, Class<T> type, ClassLoader classLoader) {
        return this.sqlSessionExtend.selectBySql(sql, type, classLoader);
    }

    @Override
    public <T> T selectOneBySql(String sql, Class<T> type) {
        return this.sqlSessionExtend.selectOneBySql(sql, type);
    }

    @Override
    public <T> T selectOneBySql(String sql, Class<T> type, ClassLoader classLoader) {
        return null;
    }

    @Override
    public <T> T globalUpdate(T record) {
        return this.sqlSessionExtend.globalUpdate(record);
    }

    @Override
    public <T> T globalUpdate(T record, ClassLoader classLoader) {
        return this.sqlSessionExtend.globalUpdate(record, classLoader);
    }

    @Override
    public <T> T localUpdate(T record) {
        return this.sqlSessionExtend.localUpdate(record);
    }

    @Override
    public <T> T localUpdate(T record, ClassLoader classLoader) {
        return this.sqlSessionExtend.localUpdate(record, classLoader);
    }

    @Override
    public <T> T selectByPrimaryKey(Serializable id, Class<T> type) {
        return this.sqlSessionExtend.selectByPrimaryKey(id, type);
    }

    @Override
    public <T> T selectByPrimaryKey(Serializable id, Class<T> type, ClassLoader classLoader) {
        return this.sqlSessionExtend.selectByPrimaryKey(id, type, classLoader);
    }

    @Override
    public <T> List<T> selectByColumn(String column, Serializable columnValue, Class<T> type) {
        return this.sqlSessionExtend.selectByColumn(column, columnValue, type);
    }

    @Override
    public <T> List<T> selectByColumn(String column, Serializable columnValue, Class<T> type, ClassLoader classLoader) {
        return this.sqlSessionExtend.selectByColumn(column, columnValue, type, classLoader);
    }

    @Override
    public <T> List<T> selectByField(String fieldName, Serializable fieldValue, Class<T> type) {
        return this.sqlSessionExtend.selectByField(fieldName, fieldValue, type);
    }

    @Override
    public <T> List<T> selectByField(String fieldName, Serializable fieldValue, Class<T> type, ClassLoader classLoader) {
        return this.sqlSessionExtend.selectByField(fieldName, fieldValue, type, classLoader);
    }

    @Override
    public <T> T selectOneByColumn(String column, Serializable columnValue, Class<T> type) {
        return this.sqlSessionExtend.selectOneByColumn(column, columnValue, type);
    }

    @Override
    public <T> T selectOneByColumn(String column, Serializable columnValue, Class<T> type, ClassLoader classLoader) {
        return this.sqlSessionExtend.selectOneByColumn(column, columnValue, type, classLoader);
    }

    @Override
    public <T> T selectOneByField(String fieldName, Serializable fieldValue, Class<T> type) {
        return this.sqlSessionExtend.selectOneByField(fieldName, fieldValue, type);
    }

    @Override
    public <T> T selectOneByField(String fieldName, Serializable fieldValue, Class<T> type, ClassLoader classLoader) {
        return this.sqlSessionExtend.selectOneByField(fieldName, fieldValue, type, classLoader);
    }
}
