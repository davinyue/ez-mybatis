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
    public String getDeleteByIdSql(SqlGenerateContext sqlGenerateContext, Table table,
                                   Class<?> ntClass, Object id) {
        Configuration configuration = sqlGenerateContext.getConfiguration();
        MybatisParamHolder paramHolder = sqlGenerateContext.getMybatisParamHolder();
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        String kwQM = EzMybatisContent.getKeywordQM(configuration);
        String tableName;
        if (table != null) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, table.getClass());
            converter.buildSql(Converter.Type.DELETE, table, sqlGenerateContext);
            tableName = sqlGenerateContext.getSqlBuilder().toString();
        } else {
            tableName = entityClassInfo.getTableNameWithSchema(kwQM);
        }

        String idColumn = entityClassInfo.getPrimaryKeyInfo().getColumnName();
        return "DELETE FROM " + tableName + " WHERE " + kwQM + idColumn + kwQM + " = " + paramHolder
                .getMybatisParamName(ntClass, entityClassInfo.getPrimaryKeyInfo().getField(), id);
    }


    @Override
    public String getBatchDeleteByIdSql(SqlGenerateContext sqlGenerateContext, Table table,
                                        Class<?> ntClass, Collection<?> ids) {
        Assert.notEmpty(ids, "ids cannot be empty");
        Configuration configuration = sqlGenerateContext.getConfiguration();
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        String kwQM = EzMybatisContent.getKeywordQM(configuration);

        String tableName;
        if (table != null) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, table.getClass());
            converter.buildSql(Converter.Type.DELETE, table, sqlGenerateContext);
            tableName = sqlGenerateContext.getSqlBuilder().toString();
        } else {
            tableName = entityClassInfo.getTableNameWithSchema(kwQM);
        }

        String idColumn = entityClassInfo.getPrimaryKeyInfo().getColumnName();
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM " + tableName + " WHERE " + kwQM +
                idColumn + kwQM + " IN ( ");
        int i = 0;
        MybatisParamHolder paramHolder = sqlGenerateContext.getMybatisParamHolder();
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
