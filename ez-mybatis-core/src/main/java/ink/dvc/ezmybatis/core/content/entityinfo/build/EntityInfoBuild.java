package ink.dvc.ezmybatis.core.content.entityinfo.build;

import ink.dvc.ezmybatis.core.constant.DbType;
import ink.dvc.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.apache.ibatis.session.Configuration;

public interface EntityInfoBuild {
    EntityClassInfo buildInfo(Configuration configuration, Class<?> ntClass);

    /**
     * 根据列名称计算java属性
     */
    String computeFieldNameByColumn(Configuration configuration, String column);

    DbType getSupportedDbType();
}
