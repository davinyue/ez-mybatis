package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
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

    protected static StringBuilder conditionsToSql(Type type, SqlGenerateContext sqlGenerateContext,
                                                   List<Condition> conditions) {
        //循环中当前条件的前面是否已经有其它条件
        boolean beforeHasCondition = false;
        Configuration configuration = sqlGenerateContext.getConfiguration();
        StringBuilder sqlBuilder = new StringBuilder();
        for (Condition condition : conditions) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, condition.getClass());
            SqlGenerateContext sqlPartCt = SqlGenerateContext.copyOf(sqlGenerateContext);
            converter.buildSql(type, condition, sqlPartCt);
            String sqlPart = sqlPartCt.getSqlBuilder().toString();
            boolean emptySql = sqlPart.trim().isEmpty();
            if (!emptySql) {
                if (beforeHasCondition) {
                    sqlBuilder.append(condition.getAndOr().name()).append(" ");
                }
                sqlBuilder.append(sqlPart);
                //如果当前条件不为空, 则将后续循环的“循环中当前条件的前面是否已经有其它条件”设置为true
                beforeHasCondition = true;
            }
        }
        return sqlBuilder;
    }

    @Override
    protected void doBuildSql(Type type, Where where, SqlGenerateContext sqlGenerateContext) {
        if (type == Type.INSERT) {
            throw new UnsupportedOperationException("INSERT model unsupported");
        }
        if (where == null) {
            return;
        }
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        if (where.getConditions() == null || where.getConditions().isEmpty()) {
            sqlBuilder.append(" WHERE 1 = 1 ");
            return;
        }
        String sonSql = conditionsToSql(type, sqlGenerateContext, where.getConditions()).toString();
        if (StringUtils.isBlank(sonSql)) {
            sonSql = " 1 = 1 ";
        }
        sqlBuilder.append(" WHERE ").append(sonSql);
    }

}
