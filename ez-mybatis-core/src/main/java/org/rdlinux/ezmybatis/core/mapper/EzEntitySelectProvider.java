package org.rdlinux.ezmybatis.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;

public class EzEntitySelectProvider {
    public String generate(@Param("ntClass") Class<?> ntClass, @Param("id") Object id) {
        return SqlGenerateFactory.getSqlGenerate().getSelectByPrimaryKeySql(ntClass, id);
    }
}
