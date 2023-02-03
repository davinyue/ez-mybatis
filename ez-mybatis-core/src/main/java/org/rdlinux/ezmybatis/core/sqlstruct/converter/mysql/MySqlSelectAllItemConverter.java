package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectAllItem;

public class MySqlSelectAllItemConverter extends AbstractConverter<SelectAllItem> implements Converter<SelectAllItem> {
    private static volatile MySqlSelectAllItemConverter instance;

    protected MySqlSelectAllItemConverter() {
    }

    public static MySqlSelectAllItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectAllItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectAllItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder dobuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectAllItem ojb, MybatisParamHolder mybatisParamHolder) {
        return sqlBuilder.append(" * ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
