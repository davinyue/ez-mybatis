package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.Assert;

public class MySqlEntityFieldConverter extends AbstractConverter<EntityField> implements Converter<EntityField> {
    private static volatile MySqlEntityFieldConverter instance;

    protected MySqlEntityFieldConverter() {
    }

    public static MySqlEntityFieldConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlEntityFieldConverter.class) {
                if (instance == null) {
                    instance = new MySqlEntityFieldConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, EntityField obj, SqlGenerateContext sqlGenerateContext) {
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(sqlGenerateContext.getConfiguration(),
                obj.getTable().getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(obj.getField());
        Assert.notNull(fieldInfo, "Class " + etInfo.getEntityClass().getName() + "cannot find the filed "
                + obj.getField());
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(sqlGenerateContext.getConfiguration());
        sqlGenerateContext.getSqlBuilder().append(obj.getTable().getAlias()).append(".").append(keywordQM)
                .append(fieldInfo.getColumnName()).append(keywordQM);
    }

}
