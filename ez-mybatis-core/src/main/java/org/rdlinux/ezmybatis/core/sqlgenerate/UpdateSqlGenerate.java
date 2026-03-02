package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.core.EzJdbcBatchSql;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

public interface UpdateSqlGenerate {
    String getUpdateSql(SqlGenerateContext sqlGenerateContext, Table table, Object model,
                        boolean isReplace);

    String getBatchUpdateSql(SqlGenerateContext sqlGenerateContext,
                             Table table, Collection<Object> models, boolean isReplace);

    String getUpdateSql(SqlGenerateContext sqlGenerateContext, EzUpdate update);

    String getUpdateSql(SqlGenerateContext sqlGenerateContext,
                        Collection<EzUpdate> updates);

    /**
     * 获取jdbc批量更新sql
     */
    EzJdbcBatchSql getJdbcBatchUpdateSql(SqlGenerateContext sqlGenerateContext, Table table, Collection<?> models,
                                         Collection<String> updateFields, boolean isReplace);
}
