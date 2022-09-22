package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.SqlTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.SubPartition;

/**
 * mysql转换器注册
 */
public class DmConverterRegister {
    public static void register() {
        EzMybatisContent.addConverter(DbType.DM, Where.class, DmWhereConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Having.class, DmHavingConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Join.class, DmJoinConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, From.class, DmFromConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, OrderBy.class, DmOrderByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Select.class, DmSelectConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, GroupBy.class, DmGroupByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Limit.class, DmLimitConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, NormalPartition.class, DmNormalPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SubPartition.class, DmSubPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, DbTable.class, DmDbTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, EntityTable.class, DmEntityTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, EzQueryTable.class, DmEzQueryTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SqlTable.class, DmSqlTableConverter.getInstance());
    }
}
