package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;

import java.util.List;

public abstract class AbstractDeleteSqlGenerate extends AbstractSelectSqlGenerate implements DeleteSqlGenerate {
    /**
     * 获取关键字引号
     */
    @Override
    protected abstract String getKeywordQM();


    @Override
    public String getDeleteByIdSql(Configuration configuration, Class<?> ntClass, Object id) {
        return this.getSelectByIdSql(configuration, ntClass, id).replaceAll("SELECT \\*", "DELETE");
    }


    @Override
    public String getBatchDeleteByIdSql(Configuration configuration, Class<?> ntClass, List<?> ids) {
        return this.getSelectByIdsSql(configuration, ntClass, ids).replaceAll("SELECT \\*", "DELETE");
    }
}
