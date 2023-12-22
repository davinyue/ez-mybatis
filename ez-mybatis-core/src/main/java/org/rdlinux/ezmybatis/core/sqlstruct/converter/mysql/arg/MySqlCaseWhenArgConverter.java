package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.CaseWhenArg;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlCaseWhenArgConverter extends AbstractConverter<CaseWhenArg> implements Converter<CaseWhenArg> {
    private static volatile MySqlCaseWhenArgConverter instance;

    protected MySqlCaseWhenArgConverter() {
    }

    public static MySqlCaseWhenArgConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlCaseWhenArgConverter.class) {
                if (instance == null) {
                    instance = new MySqlCaseWhenArgConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       CaseWhenArg obj, MybatisParamHolder mybatisParamHolder) {
        Converter<? extends CaseWhen> converter = EzMybatisContent.getConverter(configuration,
                obj.getCaseWhen().getClass());
        converter.buildSql(type, sqlBuilder, configuration, obj.getCaseWhen(), mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
