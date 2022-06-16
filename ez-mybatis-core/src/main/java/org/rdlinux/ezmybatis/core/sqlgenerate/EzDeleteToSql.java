package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;

import java.util.Collection;

public interface EzDeleteToSql {
    String toSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete);

    String toSql(Configuration configuration, MybatisParamHolder paramHolder, Collection<EzDelete> deletes);
}
