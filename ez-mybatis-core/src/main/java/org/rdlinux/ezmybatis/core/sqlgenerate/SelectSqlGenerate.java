package org.rdlinux.ezmybatis.core.sqlgenerate;

import java.util.List;

public interface SelectSqlGenerate {
    String getSelectByIdSql(Class<?> ntClass, Object id);

    String getSelectByIdsSql(Class<?> ntClass, List<?> ids);
}
