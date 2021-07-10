package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;

import java.util.List;

public interface UpdateSqlGenerate {
    String getUpdateSql(Configuration configuration, Object entity, boolean isReplace);

    String getBatchUpdateSql(Configuration configuration, List<Object> entitys, boolean isReplace);
}
