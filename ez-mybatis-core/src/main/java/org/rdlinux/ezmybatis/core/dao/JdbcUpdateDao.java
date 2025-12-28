package org.rdlinux.ezmybatis.core.dao;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.TypeHandler;
import org.rdlinux.ezmybatis.core.EzJdbcBatchSql;
import org.rdlinux.ezmybatis.core.EzJdbcSqlParam;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * JDBC-based update operations.
 * This class provides efficient batch update and replace functionality using
 * native JDBC PreparedStatement,
 * bypassing MyBatis XML mapping for better performance with large datasets.
 */
public class JdbcUpdateDao {
    private static final Log log = LogFactory.getLog(JdbcUpdateDao.class);

    private final SqlSession sqlSession;

    public JdbcUpdateDao(SqlSession sqlSession) {
        Assert.notNull(sqlSession, "sqlSession can not be null");
        this.sqlSession = sqlSession;
    }

    /**
     * Update a single record. This method retrieves all property values of the
     * object,
     * determines which properties are not null, and then updates them.
     */
    public int update(Object model) {
        return this.updateByTable(null, model);
    }

    /**
     * Update a single record with specified fields
     *
     * @param updateFields Fields to be updated
     */
    public int update(Object model, Collection<String> updateFields) {
        Assert.notEmpty(updateFields, "updateFields can not be empty");
        return this.doUpdate(null, Collections.singleton(model), updateFields, Boolean.FALSE);
    }

    /**
     * Update a single record in the specified table
     */
    public int updateByTable(Table table, Object model) {
        return this.doUpdate(table, Collections.singleton(model), null, Boolean.FALSE);
    }

    /**
     * Update a single record in the specified table with specified fields
     *
     * @param updateFields Fields to be updated
     */
    public int updateByTable(Table table, Object model, Collection<String> updateFields) {
        Assert.notEmpty(updateFields, "updateFields can not be empty");
        return this.doUpdate(table, Collections.singleton(model), updateFields, Boolean.FALSE);
    }

    /**
     * Batch update records. Iterates through each element in the collection and
     * uses non-null fields
     * as update fields for each row. If a field is not null in some elements but
     * null in others,
     * all elements will still have that field updated.
     */
    public int batchUpdate(Collection<?> models) {
        return this.doUpdate(null, models, null, Boolean.FALSE);
    }

    /**
     * Batch update records with specified fields
     *
     * @param updateFields Fields to be updated
     */
    public int batchUpdate(Collection<?> models, Collection<String> updateFields) {
        Assert.notEmpty(updateFields, "updateFields can not be empty");
        return this.doUpdate(null, models, updateFields, Boolean.FALSE);
    }

    /**
     * Batch update records in the specified table. Iterates through each element in
     * the collection
     * and uses non-null fields as update fields for each row. If a field is not
     * null in some elements
     * but null in others, all elements will still have that field updated.
     */
    public int batchUpdateByTable(Table table, Collection<?> models) {
        return this.doUpdate(table, models, null, Boolean.FALSE);
    }

    /**
     * Batch update records in the specified table with specified fields
     *
     * @param updateFields Fields to be updated
     */
    public int batchUpdateByTable(Table table, Collection<?> models, Collection<String> updateFields) {
        Assert.notEmpty(updateFields, "updateFields can not be empty");
        return this.doUpdate(table, models, updateFields, Boolean.FALSE);
    }

    /**
     * Replace a single record
     */
    public int replace(Object model) {
        return this.replaceByTable(null, model);
    }

    /**
     * Replace a single record in the specified table
     */
    public int replaceByTable(Table table, Object model) {
        return this.doUpdate(table, Collections.singleton(model), null, Boolean.TRUE);
    }

    /**
     * Batch replace records
     */
    public int batchReplace(Collection<?> models) {
        return this.doUpdate(null, models, null, Boolean.TRUE);
    }

    /**
     * Batch replace records in the specified table
     */
    public int batchReplaceByTable(Table table, Collection<?> models) {
        return this.doUpdate(table, models, null, Boolean.TRUE);
    }

    /**
     * Batch update records in the specified table
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private int doUpdate(Table table, Collection<?> models, Collection<String> updateFields, boolean isReplace) {
        Connection connection = this.sqlSession.getConnection();
        Configuration configuration = this.sqlSession.getConfiguration();
        List<EzMybatisUpdateListener> listeners = EzMybatisContent.getUpdateListeners(configuration);
        if (listeners != null) {
            for (EzMybatisUpdateListener listener : listeners) {
                if (isReplace) {
                    listener.onBatchReplace(models);
                } else {
                    listener.onBatchUpdate(models);
                }
            }
        }
        long start = System.currentTimeMillis();
        EzJdbcBatchSql jdbcBatchSql = SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getJdbcBatchUpdateSql(configuration, table, models, updateFields, isReplace);
        long end = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("SQL construction takes: " + (end - start) + "ms");
        }
        try (PreparedStatement statement = connection.prepareStatement(jdbcBatchSql.getSql())) {
            start = System.currentTimeMillis();
            for (List<EzJdbcSqlParam> batchParam : jdbcBatchSql.getBatchParams()) {
                for (int i = 0; i < batchParam.size(); i++) {
                    EzJdbcSqlParam param = batchParam.get(i);
                    TypeHandler typeHandler = param.getTypeHandler();
                    typeHandler.setParameter(statement, i + 1, param.getValue(), param.getJdbcType());
                }
                if (jdbcBatchSql.getBatchParams().size() > 1) {
                    statement.addBatch();
                }
            }
            if (log.isDebugEnabled()) {
                end = System.currentTimeMillis();
                log.debug("SQL parameter setting takes: " + (end - start) + "ms");
            }
            start = System.currentTimeMillis();
            int[] intRets;
            if (jdbcBatchSql.getBatchParams().size() == 1) {
                statement.execute();
                intRets = new int[] { 1 };
            } else {
                intRets = statement.executeBatch();
            }
            if (log.isDebugEnabled()) {
                end = System.currentTimeMillis();
                log.debug("SQL execution takes: " + (end - start) + "ms");
            }
            int ret = 0;
            for (int intRet : intRets) {
                if (intRet == -2) {
                    ret = models.size();
                    break;
                }
                ret = ret + intRet;
            }
            if (log.isDebugEnabled()) {
                String msg = "==>  Preparing: " + jdbcBatchSql.getSql();
                msg = msg + "\n" + "==> Parameters: *";
                msg = msg + "\n" + "<==    Updates: " + ret;
                log.debug(msg);
            }
            return ret;
        } catch (SQLException e) {
            log.error(String.format("SQL execution failed,  the SQL statement is \"%s\"," +
                    " the error message is \"%s\", the error code is %d", jdbcBatchSql.getSql(), e.getMessage(),
                    e.getErrorCode()));
            throw new RuntimeException(e);
        }
    }
}
