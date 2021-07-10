package org.rdlinux.ezmybatis.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;

import java.util.List;

public class EzEntitySelectProvider {
    public String selectById(@Param("ntClass") Class<?> ntClass, @Param("id") Object id) {
        return SqlGenerateFactory.getSqlGenerate().getSelectByIdSql(ntClass, id);
    }

    public String selectByIds(@Param("ntClass") Class<?> ntClass, @Param("ids") List<Object> ids) {
        return SqlGenerateFactory.getSqlGenerate().getSelectByIdsSql(ntClass, ids);
    }
}
