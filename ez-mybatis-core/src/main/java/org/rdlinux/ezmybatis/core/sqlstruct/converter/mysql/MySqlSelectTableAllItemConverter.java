package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectTableAllItem;

public class MySqlSelectTableAllItemConverter extends AbstractConverter<SelectTableAllItem> implements Converter<SelectTableAllItem> {
    private static volatile MySqlSelectTableAllItemConverter instance;

    protected MySqlSelectTableAllItemConverter() {
    }

    public static MySqlSelectTableAllItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectTableAllItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectTableAllItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectTableAllItem ojb, MybatisParamHolder mybatisParamHolder) {
        return sqlBuilder.append(" ").append(ojb.getTable().getAlias()).append(".* ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
