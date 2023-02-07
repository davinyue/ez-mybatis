package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;

/**
 * 转换器, 将sql组成部分转换为sql
 *
 * @param <Sp> 被转换对象类型
 */
public interface Converter<Sp extends SqlStruct> {
    /**
     * 转换为sql
     *
     * @param type               转换类型
     * @param sqlBuilder         当前sql
     * @param configuration      mybatis配置
     * @param sp                 被转换对象
     * @param mybatisParamHolder mybatis参数持有器
     */
    StringBuilder buildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Object sp,
                           MybatisParamHolder mybatisParamHolder);

    /**
     * 获取支持的数据库
     */
    DbType getSupportDbType();

    enum Type {
        SELECT,
        DELETE,
        INSERT,
        UPDATE;
    }
}
