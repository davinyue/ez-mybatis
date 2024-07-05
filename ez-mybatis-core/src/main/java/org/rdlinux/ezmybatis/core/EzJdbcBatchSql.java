package org.rdlinux.ezmybatis.core;

import java.util.List;

/**
 * jdbc批量执行sql
 */
public class EzJdbcBatchSql {
    /**
     * sql
     */
    private String sql;
    /**
     * 批量参数
     */
    private List<List<EzJdbcSqlParam>> batchParams;

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<List<EzJdbcSqlParam>> getBatchParams() {
        return this.batchParams;
    }

    public void setBatchParams(List<List<EzJdbcSqlParam>> batchParams) {
        this.batchParams = batchParams;
    }
}
