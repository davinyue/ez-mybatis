package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.EzQueryArg;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlEzQueryArgConverter extends AbstractConverter<EzQueryArg> implements Converter<EzQueryArg> {
    private static volatile MySqlEzQueryArgConverter instance;

    protected MySqlEzQueryArgConverter() {
    }

    public static MySqlEzQueryArgConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlEzQueryArgConverter.class) {
                if (instance == null) {
                    instance = new MySqlEzQueryArgConverter();
                }
            }
        }
        return instance;
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       EzQueryArg obj, MybatisParamHolder mybatisParamHolder) {
        Converter<? extends EzQuery> converter = EzMybatisContent.getConverter(configuration,
                obj.getEzQuery().getClass());
        converter.buildSql(type, sqlBuilder, configuration, obj.getEzQuery(), mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
