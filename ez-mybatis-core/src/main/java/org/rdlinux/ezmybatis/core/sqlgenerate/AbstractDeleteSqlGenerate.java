package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.utils.Assert;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;

import java.util.List;

public abstract class AbstractDeleteSqlGenerate implements DeleteSqlGenerate {
    @Override
    public String getDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                   Object id) {
        Assert.notNull(id, "id cannot be null");
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        String table = entityClassInfo.getTableName();
        String idColumn = entityClassInfo.getPrimaryKeyInfo().getColumnName();
        String kwQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return "DELETE FROM " + kwQM + table + kwQM + " WHERE " + kwQM + idColumn + kwQM + " = " + paramHolder
                .getParamName(id);
    }


    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                        List<?> ids) {
        Assert.notEmpty(ids, "ids cannot be null");
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        String table = entityClassInfo.getTableName();
        String idColumn = entityClassInfo.getPrimaryKeyInfo().getColumnName();
        String kwQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM " + kwQM + table + kwQM + " WHERE " + kwQM +
                idColumn + kwQM + " IN ( ");
        for (int i = 0; i < ids.size(); i++) {
            Object id = ids.get(i);
            Assert.notNull(id, String.format("ids[%d] can not be null", i));
            sqlBuilder.append(paramHolder.getParamName(id));
            if (i + 1 != ids.size()) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(" )");
        return sqlBuilder.toString();
    }
}
