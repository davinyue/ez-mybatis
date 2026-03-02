package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

public class MySqlUpdateFieldItemConverter extends AbstractConverter<UpdateFieldItem> implements Converter<UpdateFieldItem> {
    private static volatile MySqlUpdateFieldItemConverter instance;

    protected MySqlUpdateFieldItemConverter() {
    }

    public static MySqlUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    protected boolean appendAlias() {
        return true;
    }

    @Override
    protected void doBuildSql(Type type, UpdateFieldItem obj, SqlGenerateContext sqlGenerateContext) {
        Configuration configuration = sqlGenerateContext.getConfiguration();
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getEntityTable().getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(obj.getField());
        sqlGenerateContext.pushAccessField(EntityField.of(obj.getEntityTable(), obj.getField()));
        Converter<? extends Operand> argConverter = EzMybatisContent.getConverter(configuration,
                obj.getValue().getClass());
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        if (this.appendAlias()) {
            sqlBuilder.append(obj.getTable().getAlias()).append(".");
        }
        String column = fieldInfo.getColumnName();
        sqlBuilder.append(keywordQM).append(SqlEscaping.nameEscaping(column))
                .append(keywordQM).append(" = ");
        argConverter.buildSql(type, obj.getValue(), sqlGenerateContext);
        sqlGenerateContext.popAccessField();
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
