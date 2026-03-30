package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlOrderByConverter extends AbstractConverter<OrderBy> implements Converter<OrderBy> {
    private static volatile MySqlOrderByConverter instance;

    protected MySqlOrderByConverter() {
    }

    public static MySqlOrderByConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlOrderByConverter.class) {
                if (instance == null) {
                    instance = new MySqlOrderByConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, OrderBy orderBy, SqlGenerateContext sqlGenerateContext) {
        if (orderBy == null || orderBy.getItems() == null || orderBy.getItems().isEmpty()) {
            return;
        }
        sqlGenerateContext.getSqlBuilder().append(" ORDER BY ");
        Converter<OrderBy.OrderItem> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                OrderBy.OrderItem.class);
        for (int i = 0; i < orderBy.getItems().size(); i++) {
            OrderBy.OrderItem orderItem = orderBy.getItems().get(i);
            converter.buildSql(type, orderItem, sqlGenerateContext);
            if (i + 1 < orderBy.getItems().size()) {
                sqlGenerateContext.getSqlBuilder().append(", ");
            } else {
                sqlGenerateContext.getSqlBuilder().append(" ");
            }
        }
    }

}
