package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.KeywordsUpdateColumnItem;

public class MySqlKeywordsUpdateColumnItemConverter extends AbstractConverter<KeywordsUpdateColumnItem>
        implements Converter<KeywordsUpdateColumnItem> {
    private static volatile MySqlKeywordsUpdateColumnItemConverter instance;

    protected MySqlKeywordsUpdateColumnItemConverter() {
    }

    public static MySqlKeywordsUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlKeywordsUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlKeywordsUpdateColumnItemConverter();
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
        sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(obj.getColumn())
                .append(keywordQM).append(" = ").append(obj.getKeywords().getKeywords());
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
