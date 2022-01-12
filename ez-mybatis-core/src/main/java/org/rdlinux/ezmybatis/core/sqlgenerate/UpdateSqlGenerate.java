package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzUpdate;

import java.util.List;

public interface UpdateSqlGenerate {
    String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Object entity,
                        boolean isReplace);

    String getBatchUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, List<Object> entitys,
                             boolean isReplace);

    String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, EzUpdate update);

    String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, List<EzUpdate> updates);
}
