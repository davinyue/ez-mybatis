package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

import java.lang.reflect.Field;

public class MySqlBetweenAliasConditionConverter extends AbstractConverter<BetweenAliasCondition> implements Converter<BetweenAliasCondition> {
    private static volatile MySqlBetweenAliasConditionConverter instance;

    protected MySqlBetweenAliasConditionConverter() {
    }

    public static MySqlBetweenAliasConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlBetweenAliasConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlBetweenAliasConditionConverter();
                }
            }
        }
        return instance;
    }


    protected static String betweenConditionValueToSql(Configuration configuration, BetweenCondition obj,
                                                       MybatisParamHolder mybatisParamHolder, Object value) {
        String sql = null;
        if (value instanceof EzQuery) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, EzQuery.class);
            sql = converter.buildSql(Converter.Type.SELECT, new StringBuilder(), configuration,
                    value, mybatisParamHolder).toString();
        } else {
            Class<?> enType = null;
            Field field = null;
            if (obj instanceof BetweenFieldCondition) {
                BetweenFieldCondition bft = (BetweenFieldCondition) obj;
                EntityTable table = bft.getTable();
                EntityClassInfo classInfo = EzEntityClassInfoFactory.forClass(configuration, table.getEtType());
                EntityFieldInfo fieldInfo = classInfo.getFieldInfo(bft.getField());
                enType = classInfo.getEntityClass();
                field = fieldInfo.getField();
            } else if (obj instanceof BetweenColumnCondition) {
                BetweenColumnCondition bft = (BetweenColumnCondition) obj;
                if (bft.getTable() instanceof EntityTable) {
                    EntityClassInfo classInfo = EzEntityClassInfoFactory.forClass(configuration,
                            ((EntityTable) bft.getTable()).getEtType());
                    EntityFieldInfo fieldInfo = classInfo.getColumnMapFieldInfo().get(bft.getColumn());
                    enType = classInfo.getEntityClass();
                    if (fieldInfo != null) {
                        field = fieldInfo.getField();
                    }
                }
            }
            sql = mybatisParamHolder.getMybatisParamName(enType, field, value);
        }
        return sql;
    }

    protected static StringBuilder doBuildSql(StringBuilder sqlBuilder, Configuration configuration,
                                              BetweenCondition obj, MybatisParamHolder mybatisParamHolder,
                                              String column) {
        if (obj.getMinValue() == null || obj.getMaxValue() == null) {
            return sqlBuilder;
        }
        String sql = " " + column + " " + obj.getOperator().getOperator() + " " +
                betweenConditionValueToSql(configuration, obj, mybatisParamHolder, obj.getMinValue()) + " AND " +
                betweenConditionValueToSql(configuration, obj, mybatisParamHolder, obj.getMaxValue()) + " ";
        return sqlBuilder.append(sql);
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       BetweenAliasCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String column = " " + keywordQM + obj.getAlias() + keywordQM + " ";
        return doBuildSql(sqlBuilder, configuration, obj, mybatisParamHolder, column);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
