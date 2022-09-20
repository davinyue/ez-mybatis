package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

import java.util.List;

public class MySqlWhereConverter extends AbstractConverter<Where> implements Converter<Where> {
    private static volatile MySqlWhereConverter instance;

    protected MySqlWhereConverter() {
    }

    public static MySqlWhereConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlWhereConverter.class) {
                if (instance == null) {
                    instance = new MySqlWhereConverter();
                }
            }
        }
        return instance;
    }

    protected static StringBuilder conditionsToSqlPart(StringBuilder sqlBuilder, Configuration configuration,
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
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration, Where where,
                                        MybatisParamHolder mybatisParamHolder) {
        if (type == Type.INSERT) {
            throw new UnsupportedOperationException("INSERT model unsupported");
        }
        if (where == null || where.getConditions() == null || where.getConditions().isEmpty()) {
            return sqlBuilder;
        }
        sqlBuilder.append(" WHERE ");
        return conditionsToSqlPart(sqlBuilder, configuration, mybatisParamHolder, where.getConditions());
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
