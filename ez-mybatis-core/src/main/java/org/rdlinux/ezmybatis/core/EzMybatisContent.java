package org.rdlinux.ezmybatis.core;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

public class EzMybatisContent {
    public static void setDbType(Configuration configuration, DbType dbType) {
        DbTypeUtils.setDbType(configuration, dbType);
    }
}
