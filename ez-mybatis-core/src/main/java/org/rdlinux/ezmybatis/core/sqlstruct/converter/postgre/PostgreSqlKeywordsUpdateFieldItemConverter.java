package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.update.KeywordsUpdateFieldItem;

public class PostgreSqlKeywordsUpdateFieldItemConverter extends AbstractConverter<KeywordsUpdateFieldItem>
        implements Converter<KeywordsUpdateFieldItem> {
    private static volatile PostgreSqlKeywordsUpdateFieldItemConverter instance;

    protected PostgreSqlKeywordsUpdateFieldItemConverter() {
    }

    public static PostgreSqlKeywordsUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlKeywordsUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlKeywordsUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       KeywordsUpdateFieldItem obj,
                                       MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, ((EntityTable) obj.getTable())
                .getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(obj.getField());
        sqlBuilder.append(keywordQM).append(fieldInfo.getColumnName()).append(keywordQM).append(" = ")
                .append(obj.getKeywords().getKeywords());
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
