package org.rdlinux.ezmybatis.core.classinfo.entityinfo.build;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;

public interface EntityInfoBuild {
    EntityClassInfo buildInfo(Configuration configuration, Class<?> ntClass);

    DbType getSupportedDbType();
}
