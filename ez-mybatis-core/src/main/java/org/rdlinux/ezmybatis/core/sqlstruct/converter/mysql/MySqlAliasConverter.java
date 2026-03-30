package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

public class MySqlAliasConverter extends AbstractConverter<Alias> implements Converter<Alias> {
    private static volatile MySqlAliasConverter instance;

    protected MySqlAliasConverter() {
    }

    public static MySqlAliasConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlAliasConverter.class) {
                if (instance == null) {
                    instance = new MySqlAliasConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, Alias obj, SqlGenerateContext sqlGenerateContext) {
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(sqlGenerateContext.getConfiguration());
        String alias = obj.getAlias();
        alias = SqlEscaping.nameEscaping(alias);
        sqlGenerateContext.getSqlBuilder().append(keywordQM).append(alias).append(keywordQM);
    }

}
