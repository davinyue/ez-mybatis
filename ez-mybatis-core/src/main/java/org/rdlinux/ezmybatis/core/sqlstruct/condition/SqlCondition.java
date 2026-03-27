package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 表列对比条件
 */
@Getter
public class SqlCondition extends AbstractCondition implements Condition, SqlStruct {
    private final String sql;
    
    public SqlCondition(AndOr andOr, String sql) {
        Assert.notNull(andOr, "andOr can not be null");
        Assert.notEmpty(sql, "sql can not be empty");
        this.andOr = andOr;
        sql = sql.trim();
        if (sql.startsWith("and") || sql.startsWith("AND") || sql.startsWith("aNd") || sql.startsWith("anD") ||
                sql.startsWith("ANd") || sql.startsWith("aND") || sql.startsWith("AnD") || sql.startsWith("And")) {
            sql = sql.substring("and".length());
        } else if (sql.startsWith("or") || sql.startsWith("OR") || sql.startsWith("Or") || sql.startsWith("oR")) {
            sql = sql.substring("or".length());
        }
        this.sql = sql;
    }
}
