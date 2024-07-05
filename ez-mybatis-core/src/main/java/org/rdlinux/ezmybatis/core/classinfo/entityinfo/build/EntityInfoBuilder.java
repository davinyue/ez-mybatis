package org.rdlinux.ezmybatis.core.classinfo.entityinfo.build;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzContentConfig;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;

public interface EntityInfoBuilder {
    EntityClassInfo buildInfo(EzContentConfig ezContentConfig, Class<?> ntClass);

    DbType getSupportedDbType();
}
