package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.SyntaxUpdateColumnItem;

public class MySqlSyntaxUpdateColumnItemConverter extends AbstractConverter<SyntaxUpdateColumnItem> implements Converter<SyntaxUpdateColumnItem> {
    private static volatile MySqlSyntaxUpdateColumnItemConverter instance;

    protected MySqlSyntaxUpdateColumnItemConverter() {
    }

    public static MySqlSyntaxUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSyntaxUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlSyntaxUpdateColumnItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder dobuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       SyntaxUpdateColumnItem obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String column = obj.getColumn();
        sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(column)
                .append(keywordQM).append(" = ").append(obj.getSyntax());
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
