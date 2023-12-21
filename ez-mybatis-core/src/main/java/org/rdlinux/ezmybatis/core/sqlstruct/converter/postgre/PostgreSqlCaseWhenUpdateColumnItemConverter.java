package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.CaseWhenUpdateColumnItem;

public class PostgreSqlCaseWhenUpdateColumnItemConverter extends AbstractConverter<CaseWhenUpdateColumnItem> implements Converter<CaseWhenUpdateColumnItem> {
    private static volatile PostgreSqlCaseWhenUpdateColumnItemConverter instance;

    protected PostgreSqlCaseWhenUpdateColumnItemConverter() {
    }

    public static PostgreSqlCaseWhenUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlCaseWhenUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlCaseWhenUpdateColumnItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       CaseWhenUpdateColumnItem obj, MybatisParamHolder mybatisParamHolder) {
        Converter<CaseWhen> converter = EzMybatisContent.getConverter(configuration, CaseWhen.class);
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);

        sqlBuilder.append(keywordQM).append(obj.getColumn()).append(keywordQM).append(" = ");
        sqlBuilder = converter.buildSql(Type.UPDATE, sqlBuilder, configuration, obj.getCaseWhen(),
                mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
