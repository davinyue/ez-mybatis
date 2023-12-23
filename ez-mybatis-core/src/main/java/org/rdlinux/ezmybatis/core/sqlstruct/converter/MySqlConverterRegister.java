package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.SqlCondition;
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
public class MySqlConverterRegister {
    public static void register() {
        EzMybatisContent.addConverter(DbType.MYSQL, Where.class, MySqlWhereConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Having.class, MySqlHavingConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Join.class, MySqlJoinConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, From.class, MySqlFromConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, OrderBy.class, MySqlOrderByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, OrderBy.OrderItem.class, MySqlOrderItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Select.class, MySqlSelectConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, GroupBy.class, MySqlGroupByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Limit.class, MySqlLimitConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, NormalPartition.class, MySqlNormalPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SubPartition.class, MySqlSubPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, DbTable.class, MySqlDbTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, EntityTable.class, MySqlEntityTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, EzQueryTable.class, MySqlEzQueryTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SqlTable.class, MySqlSqlTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, CaseWhen.class, MySqlCaseWhenConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectAllItem.class, MySqlSelectAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectTableAllItem.class, MySqlSelectTableAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectOperand.class, MySqlSelectOperandConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, UpdateColumnItem.class, MySqlUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, UpdateFieldItem.class, MySqlUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SqlCondition.class, MySqlSqlConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, GroupCondition.class, MySqlGroupConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, ArgCompareArgCondition.class, MySqlArgCompareArgConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, EzQuery.class, MySqlEzQueryConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Function.class, MySqlFunctionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Formula.class, MySqlFormulaConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, GroupFormulaElement.class, MySqlGroupFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FormulaOperandElement.class, MySqlFormulaOperandElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Union.class, MySqlUnionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Alias.class, MySqlAliasConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, TableColumn.class, MySqlTableColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, EntityField.class, MySqlEntityFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Keywords.class, MySqlKeywordsConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, ObjArg.class, MySqlObjArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Sql.class, MySqlSqlConverter.getInstance());
    }
}
