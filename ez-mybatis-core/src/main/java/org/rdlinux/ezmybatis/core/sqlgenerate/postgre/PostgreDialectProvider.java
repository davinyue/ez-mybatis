package org.rdlinux.ezmybatis.core.sqlgenerate.postgre;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDbDialectProvider;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ExistsCondition;
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
 * PostgreSQL方言提供者
 */
public class PostgreDialectProvider extends AbstractDbDialectProvider {

    @Override
    public DbType getDbType() {
        return DbType.POSTGRE_SQL;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.toLowerCase().contains("postgresql");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return PostgreSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        addConverter(Where.class, MySqlWhereConverter.getInstance());
        addConverter(Having.class, MySqlHavingConverter.getInstance());
        addConverter(Join.class, MySqlJoinConverter.getInstance());
        addConverter(From.class, MySqlFromConverter.getInstance());
        addConverter(OrderBy.class, MySqlOrderByConverter.getInstance());
        addConverter(OrderBy.OrderItem.class, MySqlOrderItemConverter.getInstance());
        addConverter(Select.class, MySqlSelectConverter.getInstance());
        addConverter(GroupBy.class, MySqlGroupByConverter.getInstance());
        addConverter(Page.class, PostgreSqlPageConverter.getInstance());
        addConverter(NormalPartition.class, MySqlNormalPartitionConverter.getInstance());
        addConverter(SubPartition.class, MySqlSubPartitionConverter.getInstance());
        addConverter(DbTable.class, MySqlDbTableConverter.getInstance());
        addConverter(EntityTable.class, MySqlEntityTableConverter.getInstance());
        addConverter(EzQueryTable.class, MySqlEzQueryTableConverter.getInstance());
        addConverter(SqlTable.class, MySqlSqlTableConverter.getInstance());
        addConverter(CaseWhen.class, MySqlCaseWhenConverter.getInstance());
        addConverter(SelectAllItem.class, MySqlSelectAllItemConverter.getInstance());
        addConverter(SelectTableAllItem.class, MySqlSelectTableAllItemConverter.getInstance());
        addConverter(SelectOperand.class, MySqlSelectOperandConverter.getInstance());
        addConverter(UpdateColumnItem.class, PostgreSqlUpdateColumnItemConverter.getInstance());
        addConverter(UpdateFieldItem.class, PostgreSqlUpdateFieldItemConverter.getInstance());
        addConverter(SqlCondition.class, MySqlSqlConditionConverter.getInstance());
        addConverter(GroupCondition.class, MySqlGroupConditionConverter.getInstance());
        addConverter(ArgCompareArgCondition.class, PostgreSqlArgCompareArgConditionConverter.getInstance());
        addConverter(ExistsCondition.class, MySqlExistsConverter.getInstance());
        addConverter(EzQuery.class, MySqlEzQueryConverter.getInstance());
        addConverter(Function.class, MySqlFunctionConverter.getInstance());
        addConverter(Formula.class, MySqlFormulaConverter.getInstance());
        addConverter(GroupFormulaElement.class, MySqlGroupFormulaElementConverter.getInstance());
        addConverter(FormulaOperandElement.class, MySqlFormulaOperandElementConverter.getInstance());
        addConverter(WindowFunction.class, MySqlWindowFunctionConverter.getInstance());
        addConverter(Union.class, MySqlUnionConverter.getInstance());
        addConverter(Alias.class, MySqlAliasConverter.getInstance());
        addConverter(TableColumn.class, MySqlTableColumnConverter.getInstance());
        addConverter(EntityField.class, MySqlEntityFieldConverter.getInstance());
        addConverter(Keywords.class, MySqlKeywordsConverter.getInstance());
        addConverter(ObjArg.class, MySqlObjArgConverter.getInstance());
        addConverter(Sql.class, MySqlSqlConverter.getInstance());
        addConverter(Limit.class, PostgreSqlLimitConverter.getInstance());
        addConverter(SqlHint.class, PostgreSqlHintConverter.getInstance());
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }
}
