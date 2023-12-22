package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalAliasCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.SqlCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.*;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg.*;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.*;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.SqlTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.SubPartition;
import org.rdlinux.ezmybatis.core.sqlstruct.update.*;

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
        EzMybatisContent.addConverter(DbType.MYSQL, GroupBy.GroupItem.class, MySqlGroupItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Limit.class, MySqlLimitConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, NormalPartition.class, MySqlNormalPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SubPartition.class, MySqlSubPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, DbTable.class, MySqlDbTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, EntityTable.class, MySqlEntityTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, EzQueryTable.class, MySqlEzQueryTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SqlTable.class, MySqlSqlTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, CaseWhen.class, MySqlCaseWhenConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectCaseWhen.class, MySqlSelectCaseWhenConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectAllItem.class, MySqlSelectAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectAvgColumn.class, MySqlSelectAvgColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectColumn.class, MySqlSelectColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectAvgField.class, MySqlSelectAvgFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectCountField.class, MySqlSelectCountFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectCountColumn.class, MySqlSelectCountColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectField.class, MySqlSelectFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectMaxColumn.class, MySqlSelectMaxColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectMaxField.class, MySqlSelectMaxFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectMinColumn.class, MySqlSelectMinColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectMinField.class, MySqlSelectMinFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectSumColumn.class, MySqlSelectSumColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectSumField.class, MySqlSelectSumFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectTableAllItem.class, MySqlSelectTableAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectKeywords.class, MySqlSelectKeywordsConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, CaseWhenUpdateColumnItem.class, MySqlCaseWhenUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, CaseWhenUpdateFieldItem.class, MySqlCaseWhenUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, UpdateColumnItem.class, MySqlUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, UpdateFieldItem.class, MySqlUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FunctionUpdateFieldItem.class, MySqlFunctionUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FunctionUpdateColumnItem.class, MySqlFunctionUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FormulaUpdateFieldItem.class, MySqlFormulaUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FormulaUpdateColumnItem.class, MySqlFormulaUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, KeywordsUpdateFieldItem.class, MySqlKeywordsUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, KeywordsUpdateColumnItem.class, MySqlKeywordsUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, BetweenAliasCondition.class, MySqlBetweenAliasConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, NotBetweenAliasCondition.class, MySqlNotBetweenAliasConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, BetweenColumnCondition.class, MySqlBetweenColumnConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, NotBetweenColumnCondition.class, MySqlNotBetweenColumnConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, BetweenFieldCondition.class, MySqlBetweenFieldConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, NotBetweenFieldCondition.class, MySqlNotBetweenFieldConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, AliasCompareCondition.class, MySqlAliasCompareConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, ColumnCompareCondition.class, MySqlColumnCompareConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FieldCompareCondition.class, MySqlFieldCompareConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, ColumnCompareFieldCondition.class, MySqlColumnCompareFieldConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FieldCompareColumnCondition.class, MySqlFieldCompareColumnConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SqlCondition.class, MySqlSqlConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, GroupCondition.class, MySqlGroupConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, IsNullAliasCondition.class, MySqlIsNullAliasConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, IsNotNullAliasCondition.class, MySqlIsNotNullAliasConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, IsNullFieldCondition.class, MySqlIsNullFieldConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, IsNotNullFiledCondition.class, MySqlIsNotNullFieldConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, IsNullColumnCondition.class, MySqlIsNullColumnConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, IsNotNullColumnCondition.class, MySqlIsNotNullColumnConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, NormalFieldCondition.class, MySqlNormalFieldConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, NormalColumnCondition.class, MySqlNormalColumnConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, NormalAliasCondition.class, MySqlNormalAliasConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, EzQuery.class, MySqlEzQueryConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Function.class, MySqlFunctionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Formula.class, MySqlFormulaConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, GroupFormulaElement.class, MySqlGroupFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, ColumnFormulaElement.class, MySqlColumnFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FieldFormulaElement.class, MySqlFieldFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FunFormulaElement.class, MySqlFunFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FormulaFormulaElement.class, MySqlFormulaFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, CaseWhenFormulaElement.class, MySqlCaseWhenFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, ValueFormulaElement.class, MySqlValueFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, KeywordsFormulaElement.class, MySqlKeywordsFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Union.class, MySqlUnionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectFormula.class, MySqlSelectFormulaConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectFunction.class, MySqlSelectFunctionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SelectValue.class, MySqlSelectValueConverter.getInstance());
        //argConverter
        EzMybatisContent.addConverter(DbType.MYSQL, AliasArg.class, MySqlAliasArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, CaseWhenArg.class, MySqlCaseWhenArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, ColumnArg.class, MySqlColumnArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FieldArg.class, MySqlFieldArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FormulaArg.class, MySqlFormulaArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FunctionArg.class, MySqlFunctionArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, KeywordsArg.class, MySqlKeywordsArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, ObjArg.class, MySqlObjArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, SqlArg.class, MySqlSqlArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, EzQueryArg.class, MySqlEzQueryArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FormulaCompareArgCondition.class, MySqlFormulaCompareArgConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, FunctionCompareArgCondition.class, MySqlFunctionCompareArgConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, CaseWhenCompareArgCondition.class, MySqlCaseWhenCompareArgConditionConverter.getInstance());
    }
}
