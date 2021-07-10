package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;

import java.util.List;

public interface SelectSqlGenerate {
    String getSelectByIdSql(Configuration configuration, Class<?> ntClass, Object id);

    String getSelectByIdsSql(Configuration configuration, Class<?> ntClass, List<?> ids);
}
