package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;

/**
 * 条件
 */
public interface Condition extends SqlPart {
    static String valueToSqlStruct(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                   Object value) {
        if (value instanceof EzQuery) {
            String sql = " (" + SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                    .getQuerySql(configuration,
                            mybatisParamHolder, (EzQuery<?>) value) + ") ";
            DbType dbType = EzMybatisContent.getDbType(configuration);
            if (dbType == DbType.MYSQL) {
                if (((EzQuery<?>) value).getLimit() != null) {
                    sql = " (SELECT * FROM " + sql + Alias.getAlias() + ") ";
                }
            }
            return sql;
        } else {
            return mybatisParamHolder.getParamName(value, false);
        }
    }

    /**
     * 获取逻辑运算符号
     */
    LogicalOperator getLogicalOperator();

    String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder);
}
