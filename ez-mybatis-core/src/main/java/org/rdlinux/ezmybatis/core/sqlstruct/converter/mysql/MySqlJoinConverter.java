package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.commons.lang3.StringUtils;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.enumeration.JoinType;

public class MySqlJoinConverter extends AbstractConverter<Join> implements Converter<Join> {
    private static volatile MySqlJoinConverter instance;

    protected MySqlJoinConverter() {
    }

    public static MySqlJoinConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlJoinConverter.class) {
                if (instance == null) {
                    instance = new MySqlJoinConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, Join join, SqlGenerateContext sqlGenerateContext) {
        if (join == null) {
            return;
        }
        if (!join.isSure()) {
            return;
        }
        String sonSql = "";
        if (join.getJoinType() != JoinType.CrossJoin) {
            sonSql = MySqlWhereConverter.conditionsToSql(type, sqlGenerateContext, join.getOnConditions()).toString();
            if (StringUtils.isBlank(sonSql)) {
                return;
            }
        }
        Converter<?> joinTableConverter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                join.getJoinTable().getClass());
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlBuilder.append(join.getJoinType().toSqlStruct());
        joinTableConverter.buildSql(type, join.getJoinTable(), sqlGenerateContext);
        if (join.getJoinType() != JoinType.CrossJoin) {
            sqlBuilder.append(" ON ");
        }
        sqlBuilder.append(sonSql);
        if (join.getJoins() != null && !join.getJoins().isEmpty()) {
            for (Join sonJoin : join.getJoins()) {
                this.doBuildSql(type, sonJoin, sqlGenerateContext);
            }
        }
    }

}
