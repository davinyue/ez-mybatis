package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

import java.lang.reflect.Field;

public class MySqlBetweenColumnConditionConverter extends AbstractConverter<BetweenColumnCondition>
        implements Converter<BetweenColumnCondition> {
    private static volatile MySqlBetweenColumnConditionConverter instance;

    protected MySqlBetweenColumnConditionConverter() {
    }

    public static MySqlBetweenColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlBetweenColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlBetweenColumnConditionConverter();
                }
            }
        }
        return instance;
    }

    protected static String betweenConditionValueToSql(Configuration configuration, BetweenColumnCondition obj,
                                                       MybatisParamHolder mybatisParamHolder, Object value) {
        String sql;
        if (value instanceof EzQuery) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, EzQuery.class);
            sql = converter.buildSql(Converter.Type.SELECT, new StringBuilder(), configuration,
                    value, mybatisParamHolder).toString();
        } else {
            Class<?> enType = null;
            Field field = null;
            if (obj.getTable() instanceof EntityTable) {
                EntityClassInfo classInfo = EzEntityClassInfoFactory.forClass(configuration,
                        ((EntityTable) obj.getTable()).getEtType());
                EntityFieldInfo fieldInfo = classInfo.getColumnMapFieldInfo().get(obj.getColumn());
                enType = classInfo.getEntityClass();
                if (fieldInfo != null) {
                    field = fieldInfo.getField();
                }
            }
            sql = mybatisParamHolder.getMybatisParamName(enType, field, value);
        }
        return sql;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       BetweenColumnCondition obj, MybatisParamHolder mybatisParamHolder) {
        if (obj.getMinValue() == null || obj.getMaxValue() == null) {
            return sqlBuilder;
        }
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        sqlBuilder.append(" ")
                .append(obj.getTable().getAlias()).append(".")
                .append(keywordQM).append(obj.getColumn()).append(keywordQM)
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
