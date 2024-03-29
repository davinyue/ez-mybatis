package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
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
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       OrderBy orderBy, MybatisParamHolder mybatisParamHolder) {
        if (orderBy == null || orderBy.getItems() == null || orderBy.getItems().isEmpty()) {
            return sqlBuilder;
        } else {
            StringBuilder sql = new StringBuilder(" ORDER BY ");
            Converter<OrderBy.OrderItem> converter = EzMybatisContent.getConverter(configuration,
                    OrderBy.OrderItem.class);
            for (int i = 0; i < orderBy.getItems().size(); i++) {
                OrderBy.OrderItem orderItem = orderBy.getItems().get(i);
                converter.buildSql(type, sql, configuration, orderItem, mybatisParamHolder);
                if (i + 1 < orderBy.getItems().size()) {
                    sql.append(", ");
                } else {
                    sql.append(" ");
                }
            }
            return sqlBuilder.append(sql);
        }
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
