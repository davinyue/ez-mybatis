package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;

public interface EzQueryToSql {
    String toSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query);

    String toCountSql(Configuration configuration, MybatisParamHolder paramHolder, EzQuery<?> query);
}
