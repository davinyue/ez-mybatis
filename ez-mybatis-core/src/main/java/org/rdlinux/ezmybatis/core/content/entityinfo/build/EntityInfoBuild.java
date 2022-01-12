package org.rdlinux.ezmybatis.core.content.entityinfo.build;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;

public interface EntityInfoBuild {
    EntityClassInfo buildInfo(Configuration configuration, Class<?> ntClass);

    /**
     * 根据列名称计算java属性
     */
    String computeFieldNameByColumn(Configuration configuration, String column);

    DbType getSupportedDbType();
}
