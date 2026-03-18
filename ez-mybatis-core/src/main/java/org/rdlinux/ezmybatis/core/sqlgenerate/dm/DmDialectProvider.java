package org.rdlinux.ezmybatis.core.sqlgenerate.dm;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzQuery;
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
        addConverter(Where.class, DmWhereConverter.getInstance());
        addConverter(Having.class, DmHavingConverter.getInstance());
        addConverter(Join.class, DmJoinConverter.getInstance());
        addConverter(From.class, DmFromConverter.getInstance());
        addConverter(OrderBy.class, DmOrderByConverter.getInstance());
        addConverter(OrderBy.OrderItem.class, DmOrderItemConverter.getInstance());
        addConverter(Select.class, DmSelectConverter.getInstance());
        addConverter(GroupBy.class, DmGroupByConverter.getInstance());
        addConverter(Page.class, DmPageConverter.getInstance());
        addConverter(NormalPartition.class, DmNormalPartitionConverter.getInstance());
        addConverter(SubPartition.class, DmSubPartitionConverter.getInstance());
        addConverter(DbTable.class, DmDbTableConverter.getInstance());
        addConverter(EntityTable.class, DmEntityTableConverter.getInstance());
        addConverter(EzQueryTable.class, DmEzQueryTableConverter.getInstance());
        addConverter(SqlTable.class, DmSqlTableConverter.getInstance());
        addConverter(CaseWhen.class, DmCaseWhenConverter.getInstance());
        addConverter(SelectAllItem.class, DmSelectAllItemConverter.getInstance());
        addConverter(SelectTableAllItem.class, DmSelectTableAllItemConverter.getInstance());
        addConverter(SelectOperand.class, MySqlSelectOperandConverter.getInstance());
        addConverter(UpdateColumnItem.class, DmUpdateColumnItemConverter.getInstance());
        addConverter(UpdateFieldItem.class, DmUpdateFieldItemConverter.getInstance());
        addConverter(SqlCondition.class, DmSqlConditionConverter.getInstance());
        addConverter(GroupCondition.class, DmGroupConditionConverter.getInstance());
        addConverter(ArgCompareArgCondition.class, OracleArgCompareArgConditionConverter.getInstance());
        addConverter(ExistsCondition.class, MySqlExistsConverter.getInstance());
        addConverter(EzQuery.class, DmEzQueryConverter.getInstance());
        addConverter(Function.class, DmFunctionConverter.getInstance());
        addConverter(Formula.class, DmFormulaConverter.getInstance());
        addConverter(GroupFormulaElement.class, DmGroupFormulaElementConverter.getInstance());
        addConverter(FormulaOperandElement.class, MySqlFormulaOperandElementConverter.getInstance());
        addConverter(WindowFunction.class, MySqlWindowFunctionConverter.getInstance());
        addConverter(Union.class, DmUnionConverter.getInstance());
        addConverter(Alias.class, MySqlAliasConverter.getInstance());
        addConverter(TableColumn.class, MySqlTableColumnConverter.getInstance());
        addConverter(EntityField.class, MySqlEntityFieldConverter.getInstance());
        addConverter(Keywords.class, MySqlKeywordsConverter.getInstance());
        addConverter(ObjArg.class, MySqlObjArgConverter.getInstance());
        addConverter(Sql.class, MySqlSqlConverter.getInstance());
        addConverter(Limit.class, DmLimitConverter.getInstance());
        addConverter(SqlHint.class, DmSqlHintConverter.getInstance());
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }
}
