package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

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
 * Oracle方言提供者
 */
public class OracleDialectProvider extends AbstractDbDialectProvider {

    @Override
    public DbType getDbType() {
        return DbType.ORACLE;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.contains("oracle");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return OracleSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        addConverter(Where.class, OracleWhereConverter.getInstance());
        addConverter(Having.class, OracleHavingConverter.getInstance());
        addConverter(Join.class, OracleJoinConverter.getInstance());
        addConverter(From.class, OracleFromConverter.getInstance());
        addConverter(OrderBy.class, OracleOrderByConverter.getInstance());
        addConverter(OrderBy.OrderItem.class, OracleOrderItemConverter.getInstance());
        addConverter(Select.class, OracleSelectConverter.getInstance());
        addConverter(GroupBy.class, OracleGroupByConverter.getInstance());
        addConverter(Page.class, OraclePageConverter.getInstance());
        addConverter(NormalPartition.class, OracleNormalPartitionConverter.getInstance());
        addConverter(SubPartition.class, OracleSubPartitionConverter.getInstance());
        addConverter(DbTable.class, OracleDbTableConverter.getInstance());
        addConverter(EntityTable.class, OracleEntityTableConverter.getInstance());
        addConverter(EzQueryTable.class, OracleEzQueryTableConverter.getInstance());
        addConverter(SqlTable.class, OracleSqlTableConverter.getInstance());
        addConverter(CaseWhen.class, OracleCaseWhenConverter.getInstance());
        addConverter(SelectAllItem.class, OracleSelectAllItemConverter.getInstance());
        addConverter(SelectTableAllItem.class, OracleSelectTableAllItemConverter.getInstance());
        addConverter(SelectOperand.class, MySqlSelectOperandConverter.getInstance());
        addConverter(UpdateColumnItem.class, OracleUpdateColumnItemConverter.getInstance());
        addConverter(UpdateFieldItem.class, OracleUpdateFieldItemConverter.getInstance());
        addConverter(SqlCondition.class, OracleSqlConditionConverter.getInstance());
        addConverter(GroupCondition.class, OracleGroupConditionConverter.getInstance());
        addConverter(ArgCompareArgCondition.class, OracleArgCompareArgConditionConverter.getInstance());
        addConverter(ExistsCondition.class, MySqlExistsConverter.getInstance());
        addConverter(EzQuery.class, OracleEzQueryConverter.getInstance());
        addConverter(Function.class, OracleFunctionConverter.getInstance());
        addConverter(Formula.class, OracleFormulaConverter.getInstance());
        addConverter(GroupFormulaElement.class, OracleGroupFormulaElementConverter.getInstance());
        addConverter(FormulaOperandElement.class, MySqlFormulaOperandElementConverter.getInstance());
        addConverter(Union.class, OracleUnionConverter.getInstance());
        addConverter(Alias.class, MySqlAliasConverter.getInstance());
        addConverter(TableColumn.class, MySqlTableColumnConverter.getInstance());
        addConverter(EntityField.class, MySqlEntityFieldConverter.getInstance());
        addConverter(Keywords.class, MySqlKeywordsConverter.getInstance());
        addConverter(ObjArg.class, MySqlObjArgConverter.getInstance());
        addConverter(Sql.class, MySqlSqlConverter.getInstance());
        addConverter(Limit.class, OracleLimitConverter.getInstance());
        addConverter(SqlHint.class, OracleSqlHintConverter.getInstance());
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }
}
