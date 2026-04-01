package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.DmEntityInfoBuilder;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.EntityInfoBuilder;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractDbDialectProvider;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
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
 * 达梦数据库方言提供者
 */
public class DmDialectProvider extends AbstractDbDialectProvider {

    @Override
    public DbType getDbType() {
        return DbType.DM;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.toLowerCase().contains("dmdriver");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return DmSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        this.addConverter(Where.class, DmWhereConverter.getInstance());
        this.addConverter(Having.class, DmHavingConverter.getInstance());
        this.addConverter(Join.class, DmJoinConverter.getInstance());
        this.addConverter(From.class, DmFromConverter.getInstance());
        this.addConverter(OrderBy.class, DmOrderByConverter.getInstance());
        this.addConverter(OrderBy.OrderItem.class, DmOrderItemConverter.getInstance());
        this.addConverter(Select.class, DmSelectConverter.getInstance());
        this.addConverter(GroupBy.class, DmGroupByConverter.getInstance());
        this.addConverter(Page.class, DmPageConverter.getInstance());
        this.addConverter(NormalPartition.class, DmNormalPartitionConverter.getInstance());
        this.addConverter(SubPartition.class, DmSubPartitionConverter.getInstance());
        this.addConverter(DbTable.class, DmDbTableConverter.getInstance());
        this.addConverter(EntityTable.class, DmEntityTableConverter.getInstance());
        this.addConverter(EzQueryTable.class, DmEzQueryTableConverter.getInstance());
        this.addConverter(SqlTable.class, DmSqlTableConverter.getInstance());
        this.addConverter(CaseWhen.class, DmCaseWhenConverter.getInstance());
        this.addConverter(SelectAllItem.class, DmSelectAllItemConverter.getInstance());
        this.addConverter(SelectTableAllItem.class, DmSelectTableAllItemConverter.getInstance());
        this.addConverter(SelectOperand.class, MySqlSelectOperandConverter.getInstance());
        this.addConverter(UpdateColumnItem.class, DmUpdateColumnItemConverter.getInstance());
        this.addConverter(UpdateFieldItem.class, DmUpdateFieldItemConverter.getInstance());
        this.addConverter(SqlCondition.class, DmSqlConditionConverter.getInstance());
        this.addConverter(GroupCondition.class, DmGroupConditionConverter.getInstance());
        this.addConverter(ArgCompareArgCondition.class, OracleArgCompareArgConditionConverter.getInstance());
        this.addConverter(ExistsCondition.class, MySqlExistsConverter.getInstance());
        this.addConverter(EzQuery.class, DmEzQueryConverter.getInstance());
        this.addConverter(Function.class, DmFunctionConverter.getInstance());
        this.addConverter(Formula.class, DmFormulaConverter.getInstance());
        this.addConverter(GroupFormulaElement.class, DmGroupFormulaElementConverter.getInstance());
        this.addConverter(FormulaOperandElement.class, MySqlFormulaOperandElementConverter.getInstance());
        this.addConverter(WindowFunction.class, MySqlWindowFunctionConverter.getInstance());
        this.addConverter(Union.class, DmUnionConverter.getInstance());
        this.addConverter(Alias.class, MySqlAliasConverter.getInstance());
        this.addConverter(TableColumn.class, MySqlTableColumnConverter.getInstance());
        this.addConverter(EntityField.class, MySqlEntityFieldConverter.getInstance());
        this.addConverter(Keywords.class, MySqlKeywordsConverter.getInstance());
        this.addConverter(ObjArg.class, MySqlObjArgConverter.getInstance());
        this.addConverter(Sql.class, MySqlSqlConverter.getInstance());
        this.addConverter(Limit.class, DmLimitConverter.getInstance());
        this.addConverter(SqlHint.class, DmSqlHintConverter.getInstance());
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }

    @Override
    public EntityInfoBuilder getEntityInfoBuilder() {
        return DmEntityInfoBuilder.getInstance();
    }
}
