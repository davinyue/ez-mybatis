package org.rdlinux.ezmybatis.core.content.entityinfo.build;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.constant.DbType;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;

public interface EntityInfoBuild {
    EntityClassInfo buildInfo(Configuration configuration, Class<?> ntClass);

    DbType getSupportedDbType();
}
