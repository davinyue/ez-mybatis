package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.SyntaxUpdateFieldItem;

public class MySqlSyntaxUpdateFieldItemConverter extends AbstractConverter<SyntaxUpdateFieldItem> implements Converter<SyntaxUpdateFieldItem> {
    private static volatile MySqlSyntaxUpdateFieldItemConverter instance;

    protected MySqlSyntaxUpdateFieldItemConverter() {
    }

    public static MySqlSyntaxUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSyntaxUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlSyntaxUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                        SyntaxUpdateFieldItem obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getEntityTable().getEtType());
        String column = etInfo.getFieldInfo(obj.getField()).getColumnName();
        sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(column)
                .append(keywordQM).append(" = ").append(obj.getSyntax());
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
