package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

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
public class OracleConverterRegister {
    public static void register() {
        EzMybatisContent.addConverter(DbType.ORACLE, Where.class, OracleWhereConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Having.class, OracleHavingConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Join.class, OracleJoinConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, From.class, OracleFromConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, OrderBy.class, OracleOrderByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Select.class, OracleSelectConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, GroupBy.class, OracleGroupByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Limit.class, OracleLimitConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, NormalPartition.class, OracleNormalPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SubPartition.class, OracleSubPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, DbTable.class, OracleDbTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, EntityTable.class, OracleEntityTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, EzQueryTable.class, OracleEzQueryTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SqlTable.class, OracleSqlTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, CaseWhen.class, OracleCaseWhenConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectAllItem.class, OracleSelectAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectAvgColumn.class, OracleSelectAvgColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectColumn.class, OracleSelectColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectAvgField.class, OracleSelectAvgFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectCountField.class, OracleSelectCountFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectCountColumn.class, OracleSelectCountColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectField.class, OracleSelectFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectMaxColumn.class, OracleSelectMaxColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectMaxField.class, OracleSelectMaxFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectMinColumn.class, OracleSelectMinColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectMinField.class, OracleSelectMinFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectSumColumn.class, OracleSelectSumColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectSumField.class, OracleSelectSumFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectTableAllItem.class, OracleSelectTableAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, CaseWhenUpdateColumnItem.class, OracleCaseWhenUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, CaseWhenUpdateFieldItem.class, OracleCaseWhenUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SyntaxUpdateColumnItem.class, OracleSyntaxUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SyntaxUpdateFieldItem.class, OracleSyntaxUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, UpdateColumnItem.class, OracleUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, UpdateFieldItem.class, OracleUpdateFieldItemConverter.getInstance());
    }
}
