package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
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

    protected static StringBuilder conditionsToSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                                   MybatisParamHolder mybatisParamHolder,
                                                   List<Condition> conditions) {
        boolean lastConditionEmpty = true;
        for (Condition condition : conditions) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, condition.getClass());
            String sqlPart = converter.buildSql(type, new StringBuilder(), configuration, condition, mybatisParamHolder)
                    .toString();
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
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Where where,
                                       MybatisParamHolder mybatisParamHolder) {
        if (type == Type.INSERT) {
            throw new UnsupportedOperationException("INSERT model unsupported");
        }
        if (where == null || where.getConditions() == null || where.getConditions().isEmpty()) {
            return sqlBuilder;
        }
        sqlBuilder.append(" WHERE ");
        return conditionsToSql(type, sqlBuilder, configuration, mybatisParamHolder, where.getConditions());
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
