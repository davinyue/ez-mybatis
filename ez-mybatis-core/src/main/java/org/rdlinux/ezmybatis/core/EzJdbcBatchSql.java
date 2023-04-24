package org.rdlinux.ezmybatis.core;

import java.util.List;

/**
 * jdbc批量执行sql
 */
public class EzJdbcBatchSql {
    private String sql;
    private List<List<Object>> params;

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<List<Object>> getParams() {
        return this.params;
    }

    public void setParams(List<List<Object>> params) {
        this.params = params;
    }
}
