package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzUpdate;

import java.util.List;
import java.util.Map;

public interface UpdateSqlGenerate {
    String getUpdateSql(Configuration configuration, Object entity, boolean isReplace);

    String getBatchUpdateSql(Configuration configuration, List<Object> entitys, boolean isReplace);

    String getUpdateSql(Configuration configuration, EzUpdate update, Map<String, Object> mybatisParam);

    String getUpdateSql(Configuration configuration, List<EzUpdate> updates, Map<String, Object> mybatisParam);
}
