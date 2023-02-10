package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.Assert;

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
                    "When using in query, the data cannot be empty");
            return (Collection<?>) obj.getValue();
        } else if (obj.getValue().getClass().isArray()) {
            Assert.isTrue(((Object[]) obj.getValue()).length > 0,
                    "When using in query, the data cannot be empty");
            return Arrays.asList((Object[]) obj.getValue());
        } else {
            return Collections.singleton(obj.getValue());
        }
    }

    private static StringBuilder inToSql(String filedName, StringBuilder sqlBuilder, Configuration configuration,
                                         NormalCondition obj, MybatisParamHolder mybatisParamHolder,
                                         String column) {
        sqlBuilder.append(" ").append(column).append(" ");
        Collection<?> valueCo = valueToCollection(obj);
        if (valueCo.size() == 1) {
            Object sValue = valueCo.iterator().next();
            if (sValue instanceof EzQuery) {
                sqlBuilder.append(obj.getOperator().getOperator());
            } else if (obj.getOperator() == Operator.in) {
                sqlBuilder.append(Operator.eq.getOperator());
            } else {
                sqlBuilder.append(Operator.ne.getOperator());
            }
            sqlBuilder.append(" ").append(Condition.valueToSqlStruct(filedName, configuration, mybatisParamHolder,
                    sValue))
                    .append(" ");
        } else {
            sqlBuilder.append(obj.getOperator().getOperator()).append(" (");
            int i = 0;
            for (Object valueItem : valueCo) {
                sqlBuilder.append(Condition.valueToSqlStruct(filedName, configuration, mybatisParamHolder, valueItem));
                if (i + 1 < valueCo.size()) {
                    sqlBuilder.append(", ");
                }
                i++;
            }
            sqlBuilder.append(" ) ");
        }
        return sqlBuilder;
    }

    protected static StringBuilder doBuildSql(String filedName, StringBuilder sqlBuilder, Configuration configuration,
                                              NormalCondition obj, MybatisParamHolder mybatisParamHolder,
                                              String column) {

        if (obj.getOperator() == Operator.in || obj.getOperator() == Operator.notIn) {
            return inToSql(filedName, sqlBuilder, configuration, obj, mybatisParamHolder, column);
        } else {
            return otherToSql(filedName, sqlBuilder, configuration, obj, mybatisParamHolder, column);
        }
    }

    private static StringBuilder otherToSql(String filedName, StringBuilder sqlBuilder, Configuration configuration,
                                            NormalCondition obj, MybatisParamHolder mybatisParamHolder,
                                            String column) {
        sqlBuilder.append(" ").append(column).append(" ").append(obj.getOperator().getOperator()).append(" ")
                .append(Condition.valueToSqlStruct(filedName, configuration, mybatisParamHolder, obj.getValue()))
                .append(" ");
        return sqlBuilder;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       NormalFieldCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getTable().getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(obj.getField());
        String column = fieldInfo.getColumnName();
        String sql = obj.getTable().getAlias() + "." + keywordQM + column + keywordQM;
        return this.doBuildSql(fieldInfo.getFieldName(), sqlBuilder, configuration, obj, mybatisParamHolder, sql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
