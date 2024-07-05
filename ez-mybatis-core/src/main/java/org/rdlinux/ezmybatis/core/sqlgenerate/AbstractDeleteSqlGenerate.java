package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collection;

public abstract class AbstractDeleteSqlGenerate implements DeleteSqlGenerate {
    @Override
    public String getDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                   Class<?> ntClass, Object id) {
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        String kwQM = EzMybatisContent.getKeywordQM(configuration);

        String tableName;
        if (table != null) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, table.getClass());
            tableName = converter.buildSql(Converter.Type.DELETE, new StringBuilder(), configuration, table,
                    paramHolder).toString();
        } else {
            tableName = entityClassInfo.getTableNameWithSchema(kwQM);
        }

        String idColumn = entityClassInfo.getPrimaryKeyInfo().getColumnName();
        return "DELETE FROM " + tableName + " WHERE " + kwQM + idColumn + kwQM + " = " + paramHolder
                .getMybatisParamName(ntClass, entityClassInfo.getPrimaryKeyInfo().getField(), id);
    }


    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                        Class<?> ntClass, Collection<?> ids) {
        Assert.notEmpty(ids, "ids cannot be empty");
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        String kwQM = EzMybatisContent.getKeywordQM(configuration);

        String tableName;
        if (table != null) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, table.getClass());
            tableName = converter.buildSql(Converter.Type.DELETE, new StringBuilder(), configuration, table,
                    paramHolder).toString();
        } else {
            tableName = entityClassInfo.getTableNameWithSchema(kwQM);
        }

        String idColumn = entityClassInfo.getPrimaryKeyInfo().getColumnName();
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM " + tableName + " WHERE " + kwQM +
                idColumn + kwQM + " IN ( ");
        int i = 0;
        for (Object id : ids) {
            sqlBuilder.append(paramHolder.getMybatisParamName(ntClass, entityClassInfo.getPrimaryKeyInfo().getField(),
                    id));
            if (i + 1 != ids.size()) {
                sqlBuilder.append(", ");
            }
            i++;
        }
        sqlBuilder.append(" )");
        return sqlBuilder.toString();
    }
}
