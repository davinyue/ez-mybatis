package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

import java.lang.reflect.Field;

public class MySqlBetweenFieldConditionConverter extends AbstractConverter<BetweenFieldCondition>
        implements Converter<BetweenFieldCondition> {
    private static volatile MySqlBetweenFieldConditionConverter instance;

    protected MySqlBetweenFieldConditionConverter() {
    }

    public static MySqlBetweenFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlBetweenFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlBetweenFieldConditionConverter();
                }
            }
        }
        return instance;
    }

    protected static String betweenConditionValueToSql(Configuration configuration, BetweenFieldCondition obj,
                                                       MybatisParamHolder mybatisParamHolder, Object value) {
        String sql;
        if (value instanceof EzQuery) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, EzQuery.class);
            sql = converter.buildSql(Converter.Type.SELECT, new StringBuilder(), configuration,
                    value, mybatisParamHolder).toString();
        } else {
            Class<?> enType;
            Field field;
            EntityTable table = obj.getTable();
            EntityClassInfo classInfo = EzEntityClassInfoFactory.forClass(configuration, table.getEtType());
            EntityFieldInfo fieldInfo = classInfo.getFieldInfo(obj.getField());
            enType = classInfo.getEntityClass();
            field = fieldInfo.getField();
            sql = mybatisParamHolder.getMybatisParamName(enType, field, value);
        }
        return sql;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       BetweenFieldCondition obj, MybatisParamHolder mybatisParamHolder) {
        if (obj.getMinValue() == null || obj.getMaxValue() == null) {
            return sqlBuilder;
        }
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getTable().getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(obj.getField());
        String column = fieldInfo.getColumnName();
        sqlBuilder.append(" ")
                .append(obj.getTable().getAlias()).append(".").append(keywordQM).append(column).append(keywordQM)
                .append(" ")
                .append(obj.getOperator().getOperator()).append(" ")
                .append(betweenConditionValueToSql(configuration, obj, mybatisParamHolder, obj.getMinValue()))
                .append(" AND ")
                .append(betweenConditionValueToSql(configuration, obj, mybatisParamHolder, obj.getMaxValue()))
                .append(" ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
