package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.SqlCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.*;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre.*;
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
public class PostgreSqlConverterRegister {
    public static void register() {
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Where.class, MySqlWhereConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Having.class, MySqlHavingConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Join.class, MySqlJoinConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, From.class, MySqlFromConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, OrderBy.class, MySqlOrderByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, OrderBy.OrderItem.class, MySqlOrderItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Select.class, MySqlSelectConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, GroupBy.class, MySqlGroupByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Page.class, PostgreSqlPageConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, NormalPartition.class, MySqlNormalPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, SubPartition.class, MySqlSubPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, DbTable.class, MySqlDbTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, EntityTable.class, MySqlEntityTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, EzQueryTable.class, MySqlEzQueryTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, SqlTable.class, MySqlSqlTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, CaseWhen.class, MySqlCaseWhenConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, SelectAllItem.class, MySqlSelectAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, SelectTableAllItem.class, MySqlSelectTableAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, SelectOperand.class, MySqlSelectOperandConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, UpdateColumnItem.class, PostgreSqlUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, UpdateFieldItem.class, PostgreSqlUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, SqlCondition.class, MySqlSqlConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, GroupCondition.class, MySqlGroupConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, ArgCompareArgCondition.class, PostgreSqlArgCompareArgConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, EzQuery.class, MySqlEzQueryConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Function.class, MySqlFunctionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Formula.class, MySqlFormulaConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, GroupFormulaElement.class, MySqlGroupFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, FormulaOperandElement.class, MySqlFormulaOperandElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Union.class, MySqlUnionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Alias.class, MySqlAliasConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, TableColumn.class, MySqlTableColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, EntityField.class, MySqlEntityFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Keywords.class, MySqlKeywordsConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, ObjArg.class, MySqlObjArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Sql.class, MySqlSqlConverter.getInstance());
        EzMybatisContent.addConverter(DbType.POSTGRE_SQL, Limit.class, PostgreSqlLimitConverter.getInstance());
    }
}
