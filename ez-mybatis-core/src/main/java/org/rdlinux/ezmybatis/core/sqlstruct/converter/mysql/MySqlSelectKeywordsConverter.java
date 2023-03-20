package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectKeywords;

public class MySqlSelectKeywordsConverter extends AbstractConverter<SelectKeywords> implements Converter<SelectKeywords> {
    private static volatile MySqlSelectKeywordsConverter instance;

    protected MySqlSelectKeywordsConverter() {
    }

    public static MySqlSelectKeywordsConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectKeywordsConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectKeywordsConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectKeywords obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        sqlBuilder.append(" ").append(obj.getKeywords()).append(" ");
        String alias = obj.getAlias();
        if (StringUtils.isNotBlank(alias)) {
            sqlBuilder.append(keywordQM).append(alias).append(keywordQM).append(" ");
        }
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
