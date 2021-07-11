package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;

import java.util.List;

public interface DeleteSqlGenerate {
    String getDeleteByIdSql(Configuration configuration, Class<?> ntClass, Object id);

    String getBatchDeleteByIdSql(Configuration configuration, Class<?> ntClass, List<?> ids);
}
