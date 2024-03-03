package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ExistsCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.SqlCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.dm.*;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.*;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OracleArgCompareArgConditionConverter;
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
 * 达梦转换器注册
 */
public class DmConverterRegister {
    public static void register() {
        EzMybatisContent.addConverter(DbType.DM, Where.class, DmWhereConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Having.class, DmHavingConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Join.class, DmJoinConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, From.class, DmFromConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, OrderBy.class, DmOrderByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, OrderBy.OrderItem.class, DmOrderItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Select.class, DmSelectConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, GroupBy.class, DmGroupByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Page.class, DmPageConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, NormalPartition.class, DmNormalPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SubPartition.class, DmSubPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, DbTable.class, DmDbTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, EntityTable.class, DmEntityTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, EzQueryTable.class, DmEzQueryTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SqlTable.class, DmSqlTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, CaseWhen.class, DmCaseWhenConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectAllItem.class, DmSelectAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectTableAllItem.class, DmSelectTableAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SelectOperand.class, MySqlSelectOperandConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, UpdateColumnItem.class, DmUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, UpdateFieldItem.class, DmUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, SqlCondition.class, DmSqlConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, GroupCondition.class, DmGroupConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, ArgCompareArgCondition.class, OracleArgCompareArgConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, ExistsCondition.class, MySqlExistsConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, EzQuery.class, DmEzQueryConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Function.class, DmFunctionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Formula.class, DmFormulaConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, GroupFormulaElement.class, DmGroupFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, FormulaOperandElement.class, MySqlFormulaOperandElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Union.class, DmUnionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Alias.class, MySqlAliasConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, TableColumn.class, MySqlTableColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, EntityField.class, MySqlEntityFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Keywords.class, MySqlKeywordsConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, ObjArg.class, MySqlObjArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Sql.class, MySqlSqlConverter.getInstance());
        EzMybatisContent.addConverter(DbType.DM, Limit.class, DmLimitConverter.getInstance());
    }
}
