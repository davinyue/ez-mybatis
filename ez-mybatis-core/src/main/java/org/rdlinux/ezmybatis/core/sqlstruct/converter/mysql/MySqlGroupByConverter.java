package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.GroupBy;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;


public class MySqlGroupByConverter extends AbstractConverter<GroupBy> implements Converter<GroupBy> {
    private static volatile MySqlGroupByConverter instance;

    protected MySqlGroupByConverter() {
    }

    public static MySqlGroupByConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlGroupByConverter.class) {
                if (instance == null) {
                    instance = new MySqlGroupByConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, GroupBy groupBy, SqlGenerateContext sqlGenerateContext) {
        if (groupBy == null || groupBy.getItems() == null || groupBy.getItems().isEmpty()) {
            return;
        }
        sqlGenerateContext.getSqlBuilder().append(" GROUP BY ");
        for (int i = 0; i < groupBy.getItems().size(); i++) {
            Operand groupItem = groupBy.getItems().get(i);
            Converter<? extends Operand> converter = EzMybatisContent
                    .getConverter(sqlGenerateContext.getConfiguration(), groupItem.getClass());
            converter.buildSql(type, groupItem, sqlGenerateContext);
            if (i + 1 < groupBy.getItems().size()) {
                sqlGenerateContext.getSqlBuilder().append(", ");
            } else {
                sqlGenerateContext.getSqlBuilder().append(" ");
            }
        }
    }

}
