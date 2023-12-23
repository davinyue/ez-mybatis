package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.SqlCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.*;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.*;
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
public class OracleConverterRegister {
    public static void register() {
        EzMybatisContent.addConverter(DbType.ORACLE, Where.class, OracleWhereConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Having.class, OracleHavingConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Join.class, OracleJoinConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, From.class, OracleFromConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, OrderBy.class, OracleOrderByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, OrderBy.OrderItem.class, OracleOrderItemConverter.getInstance());
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
        EzMybatisContent.addConverter(DbType.ORACLE, SelectTableAllItem.class, OracleSelectTableAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectOperand.class, MySqlSelectOperandConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, UpdateColumnItem.class, OracleUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, UpdateFieldItem.class, OracleUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SqlCondition.class, OracleSqlConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, GroupCondition.class, OracleGroupConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, ArgCompareArgCondition.class, MySqlArgCompareArgConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, EzQuery.class, OracleEzQueryConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Function.class, OracleFunctionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Formula.class, OracleFormulaConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, GroupFormulaElement.class, OracleGroupFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, FormulaOperandElement.class, MySqlFormulaOperandElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Union.class, OracleUnionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Alias.class, MySqlAliasConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, TableColumn.class, MySqlTableColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, EntityField.class, MySqlEntityFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Keywords.class, MySqlKeywordsConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, ObjArg.class, MySqlObjArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Sql.class, MySqlSqlConverter.getInstance());
    }
}
