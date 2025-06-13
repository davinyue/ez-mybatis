package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * jdbc批量执行sql
 */
@Getter
@Setter
public class EzJdbcBatchSql {
    /**
     * sql
     */
    private String sql;
    /**
     * 批量参数
     */
    private List<List<EzJdbcSqlParam>> batchParams;
}
