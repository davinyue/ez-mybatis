package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

/**
 * 条件
 */
public interface Condition {
    static String valueToSqlStruct(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                   Object value) {
        if (value instanceof EzQuery) {
            String sql = " (" + SqlGenerateFactory.getSqlGenerate(DbTypeUtils.getDbType(configuration))
                    .getQuerySql(configuration,
                            mybatisParamHolder, (EzQuery<?>) value) + ") ";
            DbType dbType = DbTypeUtils.getDbType(configuration);
            if (dbType == DbType.MYSQL) {
                if (((EzQuery<?>) value).getLimit() != null) {
                    sql = " (SELECT * FROM " + sql + Alias.getAlias() + ") ";
                }
            }
            return sql;
        } else {
            return mybatisParamHolder.getParamName(value);
        }
    }

    /**
     * 获取逻辑运算符号
     */
    LogicalOperator getLogicalOperator();

    String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder);

    /**
     * 逻辑运算符
     */
    static enum LogicalOperator {
        OR,
        AND;
    }
}
