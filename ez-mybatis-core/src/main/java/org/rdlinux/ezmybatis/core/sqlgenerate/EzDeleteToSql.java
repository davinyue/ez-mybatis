package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;

import java.util.List;

public interface EzDeleteToSql {
    String toSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete);

    String toSql(Configuration configuration, MybatisParamHolder paramHolder, List<EzDelete> deletes);
}
