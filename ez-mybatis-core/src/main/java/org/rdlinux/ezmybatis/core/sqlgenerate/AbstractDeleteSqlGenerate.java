package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Collection;

public abstract class AbstractDeleteSqlGenerate implements DeleteSqlGenerate {
    @Override
    public String getDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                   Object id) {
        Assert.notNull(id, "id cannot be null");
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        String kwQM = EzMybatisContent.getKeywordQM(configuration);
        String table = entityClassInfo.getTableNameWithSchema(kwQM);
        String idColumn = entityClassInfo.getPrimaryKeyInfo().getColumnName();
        return "DELETE FROM " + table + " WHERE " + kwQM + idColumn + kwQM + " = " + paramHolder
                .getParamName(id, false);
    }


    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                        Collection<?> ids) {
        Assert.notEmpty(ids, "ids cannot be null");
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        String kwQM = EzMybatisContent.getKeywordQM(configuration);
        String table = entityClassInfo.getTableNameWithSchema(kwQM);
        String idColumn = entityClassInfo.getPrimaryKeyInfo().getColumnName();
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM " + table + " WHERE " + kwQM +
                idColumn + kwQM + " IN ( ");
        int i = 0;
        for (Object id : ids) {
            Assert.notNull(id, String.format("ids[%d] can not be null", i));
            sqlBuilder.append(paramHolder.getParamName(id, false));
            if (i + 1 != ids.size()) {
                sqlBuilder.append(", ");
            }
            i++;
        }
        sqlBuilder.append(" )");
        return sqlBuilder.toString();
    }
}
