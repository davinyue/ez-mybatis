package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
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
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       UpdateColumnItem obj, MybatisParamHolder mybatisParamHolder) {
        String column = obj.getColumn();
        EntityFieldInfo entityFieldInfo = null;
        if (obj.getTable() instanceof EntityTable) {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                    ((EntityTable) obj.getTable()).getEtType());
            entityFieldInfo = entityClassInfo.getColumnMapFieldInfo().get(column);
            if (entityFieldInfo != null) {
                EzMybatisContent.setCurrentAccessField(EntityField.of((EntityTable) obj.getTable(),
                        entityFieldInfo.getFieldName()));
            }
        }
        Converter<? extends Operand> argConverter = EzMybatisContent.getConverter(configuration,
                obj.getValue().getClass());
        StringBuilder valueSql = argConverter.buildSql(type, new StringBuilder(), configuration, obj.getValue(),
                mybatisParamHolder);
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        if (this.appendAlias()) {
            sqlBuilder.append(obj.getTable().getAlias()).append(".");
        }
        sqlBuilder.append(keywordQM).append(SqlEscaping.nameEscaping(column))
                .append(keywordQM).append(" = ").append(valueSql);
        if (entityFieldInfo != null) {
            EzMybatisContent.cleanCurrentAccessField();
        }
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
