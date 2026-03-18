package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

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
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateColumnItem;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

public class MySqlUpdateColumnItemConverter extends AbstractConverter<UpdateColumnItem> implements Converter<UpdateColumnItem> {
    private static volatile MySqlUpdateColumnItemConverter instance;

    protected MySqlUpdateColumnItemConverter() {
    }

    public static MySqlUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlUpdateColumnItemConverter();
                }
            }
        }
        return instance;
    }

    protected boolean appendAlias() {
        return true;
    }

    @Override
    protected void doBuildSql(Type type, UpdateColumnItem obj, SqlGenerateContext sqlGenerateContext) {
        String column = obj.getColumn();
        EntityFieldInfo entityFieldInfo = null;
        if (obj.getTable() instanceof EntityTable) {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(sqlGenerateContext.getConfiguration(),
                    ((EntityTable) obj.getTable()).getEtType());
            entityFieldInfo = entityClassInfo.getColumnMapFieldInfo().get(column);
            if (entityFieldInfo != null) {
                sqlGenerateContext.pushAccessField(EntityField.of((EntityTable) obj.getTable(),
                        entityFieldInfo.getFieldName()));
            }
        }
        Converter<? extends Operand> argConverter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                obj.getValue().getClass());

        String keywordQM = EzMybatisContent.getKeywordQuoteMark(sqlGenerateContext.getConfiguration());
        if (this.appendAlias()) {
            sqlGenerateContext.getSqlBuilder().append(obj.getTable().getAlias()).append(".");
        }
        sqlGenerateContext.getSqlBuilder().append(keywordQM).append(SqlEscaping.nameEscaping(column))
                .append(keywordQM).append(" = ");
        argConverter.buildSql(type, obj.getValue(), sqlGenerateContext);
        if (entityFieldInfo != null) {
            sqlGenerateContext.popAccessField();
        }
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
