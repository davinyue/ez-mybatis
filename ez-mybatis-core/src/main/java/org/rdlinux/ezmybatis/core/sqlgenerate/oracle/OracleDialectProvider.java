package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.EntityInfoBuilder;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.OracleEntityInfoBuilder;
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
        this.addConverter(Where.class, OracleWhereConverter.getInstance());
        this.addConverter(Having.class, OracleHavingConverter.getInstance());
        this.addConverter(Join.class, OracleJoinConverter.getInstance());
        this.addConverter(From.class, OracleFromConverter.getInstance());
        this.addConverter(OrderBy.class, OracleOrderByConverter.getInstance());
        this.addConverter(OrderBy.OrderItem.class, OracleOrderItemConverter.getInstance());
        this.addConverter(Select.class, OracleSelectConverter.getInstance());
        this.addConverter(GroupBy.class, OracleGroupByConverter.getInstance());
        this.addConverter(Page.class, OraclePageConverter.getInstance());
        this.addConverter(NormalPartition.class, OracleNormalPartitionConverter.getInstance());
        this.addConverter(SubPartition.class, OracleSubPartitionConverter.getInstance());
        this.addConverter(DbTable.class, OracleDbTableConverter.getInstance());
        this.addConverter(EntityTable.class, OracleEntityTableConverter.getInstance());
        this.addConverter(EzQueryTable.class, OracleEzQueryTableConverter.getInstance());
        this.addConverter(SqlTable.class, OracleSqlTableConverter.getInstance());
        this.addConverter(CaseWhen.class, OracleCaseWhenConverter.getInstance());
        this.addConverter(SelectAllItem.class, OracleSelectAllItemConverter.getInstance());
        this.addConverter(SelectTableAllItem.class, OracleSelectTableAllItemConverter.getInstance());
        this.addConverter(SelectOperand.class, MySqlSelectOperandConverter.getInstance());
        this.addConverter(UpdateColumnItem.class, OracleUpdateColumnItemConverter.getInstance());
        this.addConverter(UpdateFieldItem.class, OracleUpdateFieldItemConverter.getInstance());
        this.addConverter(SqlCondition.class, OracleSqlConditionConverter.getInstance());
        this.addConverter(GroupCondition.class, OracleGroupConditionConverter.getInstance());
        this.addConverter(ArgCompareArgCondition.class, OracleArgCompareArgConditionConverter.getInstance());
        this.addConverter(ExistsCondition.class, MySqlExistsConverter.getInstance());
        this.addConverter(EzQuery.class, OracleEzQueryConverter.getInstance());
        this.addConverter(Function.class, OracleFunctionConverter.getInstance());
        this.addConverter(Formula.class, OracleFormulaConverter.getInstance());
        this.addConverter(GroupFormulaElement.class, OracleGroupFormulaElementConverter.getInstance());
        this.addConverter(FormulaOperandElement.class, MySqlFormulaOperandElementConverter.getInstance());
        this.addConverter(WindowFunction.class, MySqlWindowFunctionConverter.getInstance());
        this.addConverter(Union.class, OracleUnionConverter.getInstance());
        this.addConverter(Alias.class, MySqlAliasConverter.getInstance());
        this.addConverter(TableColumn.class, MySqlTableColumnConverter.getInstance());
        this.addConverter(EntityField.class, MySqlEntityFieldConverter.getInstance());
        this.addConverter(Keywords.class, MySqlKeywordsConverter.getInstance());
        this.addConverter(ObjArg.class, MySqlObjArgConverter.getInstance());
        this.addConverter(Sql.class, MySqlSqlConverter.getInstance());
        this.addConverter(Limit.class, OracleLimitConverter.getInstance());
        this.addConverter(SqlHint.class, OracleSqlHintConverter.getInstance());
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }

    @Override
    public EntityInfoBuilder getEntityInfoBuilder() {
        return OracleEntityInfoBuilder.getInstance();
    }
}
