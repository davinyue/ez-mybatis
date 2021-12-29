package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;

import java.util.List;

public interface InsertSqlGenerate {
    String getInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Object entity);

    String getBatchInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, List<Object> entitys);
}
