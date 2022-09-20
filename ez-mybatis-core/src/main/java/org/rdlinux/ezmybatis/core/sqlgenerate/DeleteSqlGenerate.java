package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collection;

public interface DeleteSqlGenerate {
    String getDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table, Class<?> ntClass,
                            Object id);

    String getBatchDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Table table,
                                 Class<?> ntClass, Collection<?> ids);

    String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete);

    String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, Collection<EzDelete> deletes);
}
