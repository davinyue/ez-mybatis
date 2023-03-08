package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

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

public class MySqlKeywordsUpdateFieldItemConverter extends AbstractConverter<KeywordsUpdateFieldItem>
        implements Converter<KeywordsUpdateFieldItem> {
    private static volatile MySqlKeywordsUpdateFieldItemConverter instance;

    protected MySqlKeywordsUpdateFieldItemConverter() {
    }

    public static MySqlKeywordsUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlKeywordsUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlKeywordsUpdateFieldItemConverter();
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
        sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(fieldInfo.getColumnName())
                .append(keywordQM).append(" = ").append(obj.getKeywords());
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
