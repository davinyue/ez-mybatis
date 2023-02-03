package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.GroupBy;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.group.GroupItem;

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
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       GroupBy groupBy, MybatisParamHolder mybatisParamHolder) {
        if (groupBy == null || groupBy.getItems() == null || groupBy.getItems().isEmpty()) {
            return sqlBuilder;
        } else {
            StringBuilder sql = new StringBuilder(" GROUP BY ");
            for (int i = 0; i < groupBy.getItems().size(); i++) {
                GroupItem groupItem = groupBy.getItems().get(i);
                sql.append(groupItem.toSqlStruct(configuration));
                if (i + 1 < groupBy.getItems().size()) {
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
