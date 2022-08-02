package org.rdlinux.ezmybatis.core.sqlstruct.condition.normal;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 表列对比条件
 */
@Getter
public class SqlCondition implements Condition {
    private LogicalOperator logicalOperator;
    private String sql;


    public SqlCondition(LogicalOperator logicalOperator, String sql) {
        Assert.notNull(logicalOperator, "logicalOperator can not be null");
        Assert.notEmpty(sql, "sql can not be empty");
        this.logicalOperator = logicalOperator;
        sql = sql.trim();
        if (sql.startsWith("and") || sql.startsWith("AND") || sql.startsWith("aNd") || sql.startsWith("anD") ||
                sql.startsWith("ANd") || sql.startsWith("aND") || sql.startsWith("AnD") || sql.startsWith("And")) {
            sql = sql.substring("and".length());
        } else if (sql.startsWith("or") || sql.startsWith("OR") || sql.startsWith("Or") || sql.startsWith("oR")) {
            sql = sql.substring("or".length());
        }
        this.sql = sql;
    }


    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        return " " + this.sql + " ";
    }
}
