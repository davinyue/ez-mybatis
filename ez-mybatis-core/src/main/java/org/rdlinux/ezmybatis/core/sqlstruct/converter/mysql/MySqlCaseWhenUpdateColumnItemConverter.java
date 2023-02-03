package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.CaseWhenUpdateColumnItem;

public class MySqlCaseWhenUpdateColumnItemConverter extends AbstractConverter<CaseWhenUpdateColumnItem> implements Converter<CaseWhenUpdateColumnItem> {
    private static volatile MySqlCaseWhenUpdateColumnItemConverter instance;

    protected MySqlCaseWhenUpdateColumnItemConverter() {
    }

    public static MySqlCaseWhenUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlCaseWhenUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlCaseWhenUpdateColumnItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder dobuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       CaseWhenUpdateColumnItem obj, MybatisParamHolder mybatisParamHolder) {
        Converter<CaseWhen> converter = EzMybatisContent.getConverter(configuration, CaseWhen.class);
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);

        sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(obj.getColumn())
                .append(keywordQM).append(" = ");
        sqlBuilder = converter.buildSql(Converter.Type.UPDATE, sqlBuilder, configuration, obj.getCaseWhen(),
                mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
