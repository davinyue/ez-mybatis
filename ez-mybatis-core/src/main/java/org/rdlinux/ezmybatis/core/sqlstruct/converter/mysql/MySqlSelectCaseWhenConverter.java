package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectCaseWhen;

public class MySqlSelectCaseWhenConverter extends AbstractConverter<SelectCaseWhen> implements Converter<SelectCaseWhen> {
    private static volatile MySqlSelectCaseWhenConverter instance;

    protected MySqlSelectCaseWhenConverter() {
    }

    public static MySqlSelectCaseWhenConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectCaseWhenConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectCaseWhenConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectCaseWhen obj, MybatisParamHolder mybatisParamHolder) {
        Converter<? extends CaseWhen> converter = EzMybatisContent.getConverter(configuration, obj.getCaseWhen()
                .getClass());
        converter.buildSql(type, sqlBuilder, configuration, obj.getCaseWhen(), mybatisParamHolder);
        sqlBuilder.append(" ").append(obj.getAlias()).append(" ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
