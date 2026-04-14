package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlgenerate.oracle.OracleSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

public class DmSqlGenerate extends OracleSqlGenerate {
    private static volatile DmSqlGenerate instance;

    private DmSqlGenerate() {
    }

    public static DmSqlGenerate getInstance() {
        if (instance == null) {
            synchronized (DmSqlGenerate.class) {
                if (instance == null) {
                    instance = new DmSqlGenerate();
                }
            }
        }
        return instance;
    }

    @Override
    public String getQuerySql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return DmEzQueryToSql.getInstance().toSql(sqlGenerateContext, query);
    }

    @Override
    public String getQueryCountSql(SqlGenerateContext sqlGenerateContext, EzQuery<?> query) {
        return DmEzQueryToSql.getInstance().toCountSql(sqlGenerateContext, query);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, Table table,
                               Object entity, boolean isReplace) {
        return DmUpdateSqlGenerate.getInstance().getUpdateSql(sqlGenerateContext, table, entity,
                isReplace);
    }

    @Override
    public String getBatchUpdateSql(SqlGenerateContext sqlGenerateContext,
                                    Table table, Collection<Object> models, boolean isReplace) {
        return DmUpdateSqlGenerate.getInstance().getBatchUpdateSql(sqlGenerateContext, table,
                models, isReplace);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext, EzUpdate update) {
        return DmUpdateSqlGenerate.getInstance().getUpdateSql(sqlGenerateContext, update);
    }

    @Override
    public String getUpdateSql(SqlGenerateContext sqlGenerateContext,
                               Collection<EzUpdate> updates) {
        return DmUpdateSqlGenerate.getInstance().getUpdateSql(sqlGenerateContext, updates);
    }

    @Override
    public String getDeleteByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass, Object id) {
        return DmDeleteSqlGenerate.getInstance().getDeleteByIdSql(sqlGenerateContext, table, ntClass, id);
    }

    @Override
    public String getBatchDeleteByIdSql(SqlGenerateContext sqlGenerateContext, Table table, Class<?> ntClass,
                                        Collection<?> ids) {
        return DmDeleteSqlGenerate.getInstance().getBatchDeleteByIdSql(sqlGenerateContext, table, ntClass,
                ids);
    }

    @Override
    public String getDeleteSql(SqlGenerateContext sqlGenerateContext, EzDelete delete) {
        return DmDeleteSqlGenerate.getInstance().getDeleteSql(sqlGenerateContext, delete);
    }

    @Override
    public String getDeleteSql(SqlGenerateContext sqlGenerateContext, Collection<EzDelete> deletes) {
        return DmDeleteSqlGenerate.getInstance().getDeleteSql(sqlGenerateContext, deletes);
    }
}
