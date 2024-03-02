package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
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
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, OrderItem obj,
                                       MybatisParamHolder mybatisParamHolder) {
        if (obj.getOrderType() == null) {
            obj.setOrderType(OrderType.ASC);
        }
        Operand operand = obj.getValue();
        Converter<? extends Operand> converter = EzMybatisContent.getConverter(configuration, operand.getClass());
        converter.buildSql(type, sqlBuilder, configuration, operand, mybatisParamHolder);
        sqlBuilder.append(" ").append(obj.getOrderType().name()).append(" ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
