package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.KeywordsUpdateColumnItem;

public class PostgreSqlKeywordsUpdateColumnItemConverter extends AbstractConverter<KeywordsUpdateColumnItem>
        implements Converter<KeywordsUpdateColumnItem> {
    private static volatile PostgreSqlKeywordsUpdateColumnItemConverter instance;

    protected PostgreSqlKeywordsUpdateColumnItemConverter() {
    }

    public static PostgreSqlKeywordsUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlKeywordsUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlKeywordsUpdateColumnItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       KeywordsUpdateColumnItem obj,
                                       MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        sqlBuilder.append(keywordQM).append(obj.getColumn()).append(keywordQM).append(" = ").append(obj.getKeywords()
                .getKeywords());
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
