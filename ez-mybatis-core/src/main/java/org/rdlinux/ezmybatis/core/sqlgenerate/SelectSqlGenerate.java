package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;

import java.util.List;

public interface SelectSqlGenerate {
    String getSelectByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass, Object id);

    String getSelectByIdsSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                             List<?> ids);

    String getQuerySql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query);

    String getQueryCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query);
}
