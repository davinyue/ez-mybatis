package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
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
        //这里必须使用方言提供者来获取关键词引号, 因为EzMybatisContent来获取的话，可能会返回空字符串，会导致别名丢失关键词引号，
        //在某些数据库中，比如oracle，指定别名是驼峰格式，会被转换成全大写，最终导致无法和结果集实体对应
        Configuration configuration = sqlGenerateContext.getConfiguration();
        String keywordQM = EzMybatisContent.getDbDialectProvider(configuration).getKeywordQuoteMark();
        String alias = obj.getAlias();
        alias = SqlEscaping.nameEscaping(alias);
        sqlGenerateContext.getSqlBuilder().append(keywordQM).append(alias).append(keywordQM);
    }
}
