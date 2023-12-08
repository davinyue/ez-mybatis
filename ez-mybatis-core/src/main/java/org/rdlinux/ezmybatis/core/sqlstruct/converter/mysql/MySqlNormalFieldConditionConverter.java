package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.Assert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class MySqlNormalFieldConditionConverter extends AbstractConverter<NormalFieldCondition> implements Converter<NormalFieldCondition> {
    private static volatile MySqlNormalFieldConditionConverter instance;

    protected MySqlNormalFieldConditionConverter() {
    }

    public static MySqlNormalFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlNormalFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlNormalFieldConditionConverter();
                }
            }
        }
        return instance;
    }

    private static Collection<?> valueToCollection(NormalCondition obj) {
        if (obj.getValue() instanceof Collection) {
            Assert.isTrue(((Collection<?>) obj.getValue()).size() > 0,
                    "When using 'IN' query, the data cannot be empty");
            return (Collection<?>) obj.getValue();
        } else if (obj.getValue().getClass().isArray()) {
            Assert.isTrue(((Object[]) obj.getValue()).length > 0,
                    "When using 'IN' query, the data cannot be empty");
            return Arrays.asList((Object[]) obj.getValue());
        } else {
            return Collections.singleton(obj.getValue());
        }
    }

    private static StringBuilder inToSql(Class<?> modelType, Field field, StringBuilder sqlBuilder,
                                         Configuration configuration, NormalCondition obj,
                                         MybatisParamHolder mybatisParamHolder, String column) {
        sqlBuilder.append(" ").append(column).append(" ");
        Collection<?> valueCo = valueToCollection(obj);
        Converter<?> ezQueryConverter = EzMybatisContent.getConverter(configuration, EzQuery.class);
        boolean isSingleValue = false;
        if (valueCo.size() == 1 && !(valueCo.iterator().next() instanceof EzQuery)) {
            isSingleValue = true;
        }
        if (!isSingleValue) {
            sqlBuilder.append(obj.getOperator().getOperator()).append(" ( ");
        } else {
            if (obj.getOperator() == Operator.in) {
                sqlBuilder.append(Operator.eq.getOperator()).append(" ");
            } else {
                sqlBuilder.append(Operator.ne.getOperator()).append(" ");
            }
        }
        int i = 0;
        for (Object valueItem : valueCo) {
            if (valueItem instanceof EzQuery) {
                sqlBuilder.append(ezQueryConverter.buildSql(Converter.Type.SELECT, new StringBuilder()
                        , configuration, valueItem, mybatisParamHolder)).append(" ");
            } else {
                sqlBuilder.append(mybatisParamHolder.getMybatisParamName(modelType, field, valueItem)).append(" ");
            }
            if (i + 1 < valueCo.size()) {
                sqlBuilder.append(", ");
            }
            i++;
        }
        if (!isSingleValue) {
            sqlBuilder.append(" ) ");
        }
        return sqlBuilder;
    }

    private static StringBuilder otherToSql(Class<?> modelType, Field field, StringBuilder sqlBuilder,
                                            Configuration configuration, NormalCondition obj,
                                            MybatisParamHolder mybatisParamHolder, String column) {
        sqlBuilder.append(" ").append(column).append(" ").append(obj.getOperator().getOperator()).append(" ");
        if (obj.getValue() instanceof EzQuery) {
            Converter<?> ezQueryConverter = EzMybatisContent.getConverter(configuration, EzQuery.class);
            sqlBuilder.append(ezQueryConverter.buildSql(Converter.Type.SELECT, new StringBuilder(),
                    configuration, obj.getValue(), mybatisParamHolder));
        } else {
            sqlBuilder.append(mybatisParamHolder.getMybatisParamName(modelType, field, obj.getValue()));
        }
        sqlBuilder.append(" ");
        return sqlBuilder;
    }

    protected static StringBuilder doBuildSql(Class<?> modelType, Field field, StringBuilder sqlBuilder,
                                              Configuration configuration, NormalCondition obj,
                                              MybatisParamHolder mybatisParamHolder, String column) {
        if (obj.getOperator() == Operator.in || obj.getOperator() == Operator.notIn) {
            return inToSql(modelType, field, sqlBuilder, configuration, obj, mybatisParamHolder, column);
        } else {
            return otherToSql(modelType, field, sqlBuilder, configuration, obj, mybatisParamHolder, column);
        }
    }


    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       NormalFieldCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getTable().getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(obj.getField());
        String column = fieldInfo.getColumnName();
        String sql = obj.getTable().getAlias() + "." + keywordQM + column + keywordQM;
        return doBuildSql(etInfo.getEntityClass(), fieldInfo.getField(), sqlBuilder, configuration, obj,
                mybatisParamHolder, sql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
