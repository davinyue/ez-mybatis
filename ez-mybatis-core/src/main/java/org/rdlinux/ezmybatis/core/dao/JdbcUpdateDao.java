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
 * 使用jdbc批量更新
 */
public class JdbcUpdateDao {
    private static final Log log = LogFactory.getLog(JdbcUpdateDao.class);

    private SqlSession sqlSession;

    public JdbcUpdateDao(SqlSession sqlSession) {
        Assert.notNull(sqlSession, "sqlSession can not be null");
        this.sqlSession = sqlSession;
    }

    /**
     * 单条更新
     */
    public int update(Object model) {
        return this.updateByTable(null, model);
    }

    /**
     * 单条更新, 指定表
     */
    public int updateByTable(Table table, Object model) {
        return this.doUpdate(table, Collections.singleton(model), false);
    }


    /**
     * 批量更新
     */
    public int batchUpdate(Collection<?> models) {
        return this.doUpdate(null, models, false);
    }

    /**
     * 批量更新, 指定表
     */
    public int batchUpdateByTable(Table table, Collection<?> models) {
        return this.doUpdate(table, models, false);
    }


    /**
     * 单条替换
     */
    public int replace(Object model) {
        return this.replaceByTable(null, model);
    }


    /**
     * 单条替换, 指定表
     */
    public int replaceByTable(Table table, Object model) {
        return this.doUpdate(table, Collections.singleton(model), true);
    }

    /**
     * 批量替换
     */
    public int batchReplace(Collection<?> models) {
        return this.doUpdate(null, models, true);
    }

    /**
     * 批量替换, 指定表
     */
    public int batchReplaceByTable(Table table, Collection<?> models) {
        return this.doUpdate(table, models, true);
    }


    /**
     * 批量更新, 指定表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private int doUpdate(Table table, Collection<?> models, boolean isReplace) {
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
                .getJdbcBatchUpdateSql(configuration, table, models, isReplace);
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
                intRets = new int[]{1};
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
            throw new RuntimeException(e);
        }
    }
}
