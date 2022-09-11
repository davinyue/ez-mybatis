package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

public interface SelectSqlGenerate {
    String getSelectByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table, Class<?> ntClass,
                            Object id);

    String getSelectByIdsSql(Configuration configuration, MybatisParamHolder paramHolder, Table table, Class<?> ntClass,
                             Collection<?> ids);

    String getQuerySql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query);

    String getQueryCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query);
}
