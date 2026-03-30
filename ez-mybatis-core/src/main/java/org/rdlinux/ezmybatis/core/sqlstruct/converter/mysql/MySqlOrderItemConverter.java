package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy.OrderItem;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.enumeration.OrderType;

public class MySqlOrderItemConverter extends AbstractConverter<OrderItem> implements Converter<OrderItem> {
    private static volatile MySqlOrderItemConverter instance;

    protected MySqlOrderItemConverter() {
    }

    public static MySqlOrderItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlOrderItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlOrderItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, OrderItem obj, SqlGenerateContext sqlGenerateContext) {
        if (obj.getOrderType() == null) {
            obj.setOrderType(OrderType.ASC);
        }
        Operand operand = obj.getValue();
        Configuration configuration = sqlGenerateContext.getConfiguration();
        Converter<? extends Operand> converter = EzMybatisContent.getConverter(configuration, operand.getClass());
        converter.buildSql(type, operand, sqlGenerateContext);
        sqlGenerateContext.getSqlBuilder().append(" ").append(obj.getOrderType().name()).append(" ");
    }

}
