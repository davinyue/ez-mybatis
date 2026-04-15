package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
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
    protected void doBuildSql(Type type, GroupCondition obj, SqlGenerateContext sqlGenerateContext) {
        if (!obj.isSure() || obj.getConditions() == null || obj.getConditions().isEmpty()) {
            return;
        }
        sqlGenerateContext.getSqlBuilder().append(" ( ");
        for (int i = 0; i < obj.getConditions().size(); i++) {
            Condition condition = obj.getConditions().get(i);
            Assert.notNull(condition, "condition can not be null");
            if (i != 0) {
                sqlGenerateContext.getSqlBuilder().append(" ").append(condition.getAndOr().name()).append(" ");
            }
            Converter<? extends Condition> converter = EzMybatisContent
                    .getConverter(sqlGenerateContext.getConfiguration(), condition.getClass());
            converter.buildSql(type, condition, sqlGenerateContext);
        }
        sqlGenerateContext.getSqlBuilder().append(" ) ");
    }

}
