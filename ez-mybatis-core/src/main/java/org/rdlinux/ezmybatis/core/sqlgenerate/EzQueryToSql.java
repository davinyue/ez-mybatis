package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;

import java.util.Map;

public interface EzQueryToSql {
    /**
     * @param mybatisParam mybatis参数
     */
    String toSql(Configuration configuration, EzQuery query, Map<String, Object> mybatisParam);

    /**
     * @param mybatisParam mybatis参数
     */
    String toCountSql(Configuration configuration, EzQuery query, Map<String, Object> mybatisParam);
}
