package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzUpdate;

import java.util.Collection;

public interface EzUpdateToSql {
    String toSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, EzUpdate update);

    String toSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Collection<EzUpdate> updates);
}
