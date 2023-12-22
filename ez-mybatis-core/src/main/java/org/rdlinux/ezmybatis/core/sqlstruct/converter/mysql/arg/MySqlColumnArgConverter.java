package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.ColumnArg;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlColumnArgConverter extends AbstractConverter<ColumnArg> implements Converter<ColumnArg> {
    private static volatile MySqlColumnArgConverter instance;

    protected MySqlColumnArgConverter() {
    }

    public static MySqlColumnArgConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlColumnArgConverter.class) {
                if (instance == null) {
                    instance = new MySqlColumnArgConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       ColumnArg obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(obj.getColumn())
                .append(keywordQM);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
