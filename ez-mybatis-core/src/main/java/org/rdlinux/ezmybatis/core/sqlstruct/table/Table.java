package org.rdlinux.ezmybatis.core.sqlstruct.table;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.Partition;

public interface Table extends SqlPart {
    default Partition getPartition() {
        return null;
    }

    String getAlias();

    String getTableName(Configuration configuration);

    /**
     * 获取数据库模式
     */
    String getSchema(Configuration configuration);

    String toSqlStruct(Converter.Type type, Configuration configuration, MybatisParamHolder paramHolder);
}
