package org.rdlinux.ezmybatis.core.sqlstruct.convert;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;

/**
 * 转换器, 将sql组成部分转换为sql
 *
 * @param <SqlStruct> sql部分
 */
public interface Convert<SqlStruct> {
    /**
     * 转换为sql
     *
     * @param type               转换类型
     * @param sqlBuilder         当前sql
     * @param configuration      mybatis配置
     * @param sqlStruct          被转换对象
     * @param mybatisParamHolder mybatis参数持有器
     */
    StringBuilder toSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration, SqlStruct sqlStruct,
                            MybatisParamHolder mybatisParamHolder);

    /**
     * 获取支持的数据库
     */
    DbType getSupportDbType();

    public static enum Type {
        SELECT,
        DELETE,
        INSERT,
        UPDATE;
    }
}
