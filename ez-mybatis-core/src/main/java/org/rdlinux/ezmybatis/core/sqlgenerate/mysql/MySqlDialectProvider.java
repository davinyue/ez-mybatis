package org.rdlinux.ezmybatis.core.sqlgenerate.mysql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.MySqlEntityInfoBuilder;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDbDialectProvider;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ExistsCondition;
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
 * MySQL方言提供者
 */
public class MySqlDialectProvider extends AbstractDbDialectProvider {
    public MySqlDialectProvider() {
        this.setEntityInfoBuilder(MySqlEntityInfoBuilder.getInstance());
    }

    @Override
    public DbType getDbType() {
        return DbType.MYSQL;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.contains("mysql");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return MySqlSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        this.addConverter(Where.class, MySqlWhereConverter.getInstance());
        this.addConverter(Having.class, MySqlHavingConverter.getInstance());
        this.addConverter(Join.class, MySqlJoinConverter.getInstance());
        this.addConverter(From.class, MySqlFromConverter.getInstance());
        this.addConverter(OrderBy.class, MySqlOrderByConverter.getInstance());
        this.addConverter(OrderBy.OrderItem.class, MySqlOrderItemConverter.getInstance());
        this.addConverter(Select.class, MySqlSelectConverter.getInstance());
        this.addConverter(GroupBy.class, MySqlGroupByConverter.getInstance());
        this.addConverter(Page.class, MySqlPageConverter.getInstance());
        this.addConverter(NormalPartition.class, MySqlNormalPartitionConverter.getInstance());
        this.addConverter(SubPartition.class, MySqlSubPartitionConverter.getInstance());
        this.addConverter(DbTable.class, MySqlDbTableConverter.getInstance());
        this.addConverter(EntityTable.class, MySqlEntityTableConverter.getInstance());
        this.addConverter(EzQueryTable.class, MySqlEzQueryTableConverter.getInstance());
        this.addConverter(SqlTable.class, MySqlSqlTableConverter.getInstance());
        this.addConverter(CaseWhen.class, MySqlCaseWhenConverter.getInstance());
        this.addConverter(SelectAllItem.class, MySqlSelectAllItemConverter.getInstance());
        this.addConverter(SelectTableAllItem.class, MySqlSelectTableAllItemConverter.getInstance());
        this.addConverter(SelectOperand.class, MySqlSelectOperandConverter.getInstance());
        this.addConverter(UpdateColumnItem.class, MySqlUpdateColumnItemConverter.getInstance());
        this.addConverter(UpdateFieldItem.class, MySqlUpdateFieldItemConverter.getInstance());
        this.addConverter(SqlCondition.class, MySqlSqlConditionConverter.getInstance());
        this.addConverter(GroupCondition.class, MySqlGroupConditionConverter.getInstance());
        this.addConverter(ArgCompareArgCondition.class, MySqlArgCompareArgConditionConverter.getInstance());
        this.addConverter(ExistsCondition.class, MySqlExistsConverter.getInstance());
        this.addConverter(EzQuery.class, MySqlEzQueryConverter.getInstance());
        this.addConverter(Function.class, MySqlFunctionConverter.getInstance());
        this.addConverter(Formula.class, MySqlFormulaConverter.getInstance());
        this.addConverter(GroupFormulaElement.class, MySqlGroupFormulaElementConverter.getInstance());
        this.addConverter(FormulaOperandElement.class, MySqlFormulaOperandElementConverter.getInstance());
        this.addConverter(WindowFunction.class, MySqlWindowFunctionConverter.getInstance());
        this.addConverter(Union.class, MySqlUnionConverter.getInstance());
        this.addConverter(Alias.class, MySqlAliasConverter.getInstance());
        this.addConverter(TableColumn.class, MySqlTableColumnConverter.getInstance());
        this.addConverter(EntityField.class, MySqlEntityFieldConverter.getInstance());
        this.addConverter(Keywords.class, MySqlKeywordsConverter.getInstance());
        this.addConverter(ObjArg.class, MySqlObjArgConverter.getInstance());
        this.addConverter(Sql.class, MySqlSqlConverter.getInstance());
        this.addConverter(Limit.class, MySqlLimitConverter.getInstance());
        this.addConverter(SqlHint.class, MySqlSqlHintConverter.getInstance());
    }

    @Override
    public String getKeywordQuoteMark() {
        return "`";
    }
}
