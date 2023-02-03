package org.rdlinux.ezmybatis.core.sqlstruct.converter.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.SqlTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.SubPartition;
import org.rdlinux.ezmybatis.core.sqlstruct.update.*;

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
        EzMybatisContent.addConverter(DbType.DM, CaseWhen.class, DmCaseWhenConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectAllItem.class, DmSelectAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectAvgColumn.class, DmSelectAvgColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectColumn.class, DmSelectColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectAvgField.class, DmSelectAvgFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectCountField.class, DmSelectCountFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectCountColumn.class, DmSelectCountColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectField.class, DmSelectFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectMaxColumn.class, DmSelectMaxColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectMaxField.class, DmSelectMaxFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectMinColumn.class, DmSelectMinColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectMinField.class, DmSelectMinFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectSumColumn.class, DmSelectSumColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectSumField.class, DmSelectSumFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectTableAllItem.class, DmSelectTableAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, CaseWhenUpdateColumnItem.class, DmCaseWhenUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, CaseWhenUpdateFieldItem.class, DmCaseWhenUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SyntaxUpdateColumnItem.class, DmSyntaxUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SyntaxUpdateFieldItem.class, DmSyntaxUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, UpdateColumnItem.class, DmUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, UpdateFieldItem.class, DmUpdateFieldItemConverter.getInstance());
    }
}
