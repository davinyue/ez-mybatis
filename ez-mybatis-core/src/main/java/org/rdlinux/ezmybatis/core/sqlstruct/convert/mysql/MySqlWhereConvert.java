package org.rdlinux.ezmybatis.core.sqlstruct.convert.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.convert.Convert;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

public class MySqlWhereConvert implements Convert<Where> {
    protected StringBuilder conditionsToSqlPart(StringBuilder sqlBuilder, Configuration configuration,
                                                MybatisParamHolder mybatisParamHolder,
                                                List<Condition> conditions) {
        boolean lastConditionEmpty = true;
        for (Condition condition : conditions) {
            String sqlPart = condition.toSqlPart(configuration, mybatisParamHolder);
            boolean emptySql = sqlPart.trim().isEmpty();
            if (!lastConditionEmpty && !emptySql) {
                sqlBuilder.append(condition.getLogicalOperator().name()).append(" ");
            }
            if (!emptySql) {
                lastConditionEmpty = false;
                sqlBuilder.append(sqlPart);
            } else {
                lastConditionEmpty = true;
            }
        }
        return sqlBuilder;
    }

    @Override
    public StringBuilder toSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration, Where where,
                                   MybatisParamHolder mybatisParamHolder) {
        Assert.notNull(type, "type can not be null");
        Assert.notNull(sqlBuilder, "sqlBuilder can not be null");
        Assert.notNull(configuration, "configuration can not be null");
        Assert.notNull(mybatisParamHolder, "mybatisParamHolder can not be null");
        if (type == Type.INSERT) {
            throw new UnsupportedOperationException("INSERT MODEL Unsupported");
        }
        if (where == null || where.getConditions() == null || where.getConditions().isEmpty()) {
            return sqlBuilder;
        }
        sqlBuilder.append(" WHERE ");
        return this.conditionsToSqlPart(sqlBuilder, configuration, mybatisParamHolder, where.getConditions());
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
