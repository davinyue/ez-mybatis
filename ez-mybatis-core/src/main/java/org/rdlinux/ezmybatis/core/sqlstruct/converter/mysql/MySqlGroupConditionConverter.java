package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.Assert;

public class MySqlGroupConditionConverter extends AbstractConverter<GroupCondition> implements Converter<GroupCondition> {
    private static volatile MySqlGroupConditionConverter instance;

    protected MySqlGroupConditionConverter() {
    }

    public static MySqlGroupConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlGroupConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlGroupConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       GroupCondition obj, MybatisParamHolder mybatisParamHolder) {
        if (!obj.isSure() || obj.getConditions() == null || obj.getConditions().isEmpty()) {
            return sqlBuilder;
        } else {
            StringBuilder sonSql = new StringBuilder(" ( ");
            for (int i = 0; i < obj.getConditions().size(); i++) {
                Condition condition = obj.getConditions().get(i);
                Assert.notNull(condition, "condition can not be null");
                if (i != 0) {
                    sonSql.append(" ").append(condition.getLogicalOperator().name()).append(" ");
                }
                Converter<? extends Condition> converter = EzMybatisContent.getConverter(configuration,
                        condition.getClass());
                StringBuilder conditionSql = converter.buildSql(type, new StringBuilder(), configuration, condition,
                        mybatisParamHolder);
                sonSql.append(conditionSql);
            }
            sonSql.append(" ) ");
            return sqlBuilder.append(sonSql);
        }
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
