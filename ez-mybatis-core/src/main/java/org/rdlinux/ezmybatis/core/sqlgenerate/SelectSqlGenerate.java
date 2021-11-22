package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;

import java.util.List;
import java.util.Map;

public interface SelectSqlGenerate {
    String getSelectByIdSql(Configuration configuration, Class<?> ntClass, Object id);

    String getSelectByIdsSql(Configuration configuration, Class<?> ntClass, List<?> ids);

    String getQuerySql(Configuration configuration, Class<?> ntClass, EzQuery query, Map<String, Object> mybatisParam);

    String getQueryCountSql(Configuration configuration, Class<?> ntClass, EzQuery query,
                            Map<String, Object> mybatisParam);
}
