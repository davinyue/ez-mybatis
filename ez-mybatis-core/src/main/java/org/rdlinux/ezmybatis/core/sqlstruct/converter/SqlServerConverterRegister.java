package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ExistsCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.SqlCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql.*;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.*;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FormulaOperandElement;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.GroupFormulaElement;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectAllItem;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectOperand;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectTableAllItem;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.SqlTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.SubPartition;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateColumnItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;

/**
 * mysql转换器注册
 */
public class SqlServerConverterRegister {
    public static void register() {
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Where.class, MySqlWhereConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Having.class, MySqlHavingConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Join.class, MySqlJoinConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, From.class, MySqlFromConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, OrderBy.class, SqlServerOrderByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, OrderBy.OrderItem.class, MySqlOrderItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Select.class, MySqlSelectConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, GroupBy.class, MySqlGroupByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Page.class, SqlServerPageConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, NormalPartition.class, MySqlNormalPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, SubPartition.class, MySqlSubPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, DbTable.class, SqlServerDbTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, EntityTable.class, SqlServerEntityTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, EzQueryTable.class, MySqlEzQueryTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, SqlTable.class, SqlServerSqlTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, CaseWhen.class, MySqlCaseWhenConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, SelectAllItem.class, MySqlSelectAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, SelectTableAllItem.class, MySqlSelectTableAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, SelectOperand.class, MySqlSelectOperandConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, UpdateColumnItem.class, SqlServerUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, UpdateFieldItem.class, SqlServerUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, SqlCondition.class, MySqlSqlConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, GroupCondition.class, MySqlGroupConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, ArgCompareArgCondition.class, SqlServerArgCompareArgConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, ExistsCondition.class, MySqlExistsConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, EzQuery.class, MySqlEzQueryConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Function.class, MySqlFunctionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Formula.class, MySqlFormulaConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, GroupFormulaElement.class, MySqlGroupFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, FormulaOperandElement.class, MySqlFormulaOperandElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Union.class, MySqlUnionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Alias.class, MySqlAliasConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, TableColumn.class, SqlServerTableColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, EntityField.class, SqlServerEntityFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Keywords.class, MySqlKeywordsConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, ObjArg.class, MySqlObjArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Sql.class, MySqlSqlConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, Limit.class, SqlServerLimitConverter.getInstance());
        EzMybatisContent.addConverter(DbType.SQL_SERVER, SqlHint.class, MySqlSqlHintConverter.getInstance());
    }
}
