package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

public interface InsertSqlGenerate {
    String getInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Table table,
                        Object entity);

    String getBatchInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                             Table table, Collection<Object> entitys);
}
