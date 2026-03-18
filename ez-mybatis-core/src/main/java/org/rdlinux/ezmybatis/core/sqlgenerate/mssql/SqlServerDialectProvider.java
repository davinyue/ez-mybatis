package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDbDialectProvider;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
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
 * SQL Server方言提供者
 */
public class SqlServerDialectProvider extends AbstractDbDialectProvider {

    @Override
    public DbType getDbType() {
        return DbType.SQL_SERVER;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.toLowerCase().contains("sqlserver");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return SqlServerSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        addConverter(Where.class, MySqlWhereConverter.getInstance());
        addConverter(Having.class, MySqlHavingConverter.getInstance());
        addConverter(Join.class, MySqlJoinConverter.getInstance());
        addConverter(From.class, MySqlFromConverter.getInstance());
        addConverter(OrderBy.class, SqlServerOrderByConverter.getInstance());
        addConverter(OrderBy.OrderItem.class, MySqlOrderItemConverter.getInstance());
        addConverter(Select.class, MySqlSelectConverter.getInstance());
        addConverter(GroupBy.class, MySqlGroupByConverter.getInstance());
        addConverter(Page.class, SqlServerPageConverter.getInstance());
        addConverter(NormalPartition.class, MySqlNormalPartitionConverter.getInstance());
        addConverter(SubPartition.class, MySqlSubPartitionConverter.getInstance());
        addConverter(DbTable.class, SqlServerDbTableConverter.getInstance());
        addConverter(EntityTable.class, SqlServerEntityTableConverter.getInstance());
        addConverter(EzQueryTable.class, MySqlEzQueryTableConverter.getInstance());
        addConverter(SqlTable.class, SqlServerSqlTableConverter.getInstance());
        addConverter(CaseWhen.class, MySqlCaseWhenConverter.getInstance());
        addConverter(SelectAllItem.class, MySqlSelectAllItemConverter.getInstance());
        addConverter(SelectTableAllItem.class, MySqlSelectTableAllItemConverter.getInstance());
        addConverter(SelectOperand.class, MySqlSelectOperandConverter.getInstance());
        addConverter(UpdateColumnItem.class, SqlServerUpdateColumnItemConverter.getInstance());
        addConverter(UpdateFieldItem.class, SqlServerUpdateFieldItemConverter.getInstance());
        addConverter(SqlCondition.class, MySqlSqlConditionConverter.getInstance());
        addConverter(GroupCondition.class, MySqlGroupConditionConverter.getInstance());
        addConverter(ArgCompareArgCondition.class, SqlServerArgCompareArgConditionConverter.getInstance());
        addConverter(ExistsCondition.class, MySqlExistsConverter.getInstance());
        addConverter(EzQuery.class, MySqlEzQueryConverter.getInstance());
        addConverter(Function.class, MySqlFunctionConverter.getInstance());
        addConverter(Formula.class, MySqlFormulaConverter.getInstance());
        addConverter(GroupFormulaElement.class, MySqlGroupFormulaElementConverter.getInstance());
        addConverter(FormulaOperandElement.class, MySqlFormulaOperandElementConverter.getInstance());
        addConverter(WindowFunction.class, MySqlWindowFunctionConverter.getInstance());
        addConverter(Union.class, MySqlUnionConverter.getInstance());
        addConverter(Alias.class, MySqlAliasConverter.getInstance());
        addConverter(TableColumn.class, SqlServerTableColumnConverter.getInstance());
        addConverter(EntityField.class, SqlServerEntityFieldConverter.getInstance());
        addConverter(Keywords.class, MySqlKeywordsConverter.getInstance());
        addConverter(ObjArg.class, MySqlObjArgConverter.getInstance());
        addConverter(Sql.class, MySqlSqlConverter.getInstance());
        addConverter(Limit.class, SqlServerLimitConverter.getInstance());
        addConverter(SqlHint.class, MySqlSqlHintConverter.getInstance());
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }
}
