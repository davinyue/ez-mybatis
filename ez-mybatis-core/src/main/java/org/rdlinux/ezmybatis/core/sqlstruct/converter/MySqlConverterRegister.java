package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.SqlCondition;
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
        EzMybatisContent.addConverter(DbType.MYSQL, SqlCondition.class, MySqlSqlConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, GroupCondition.class, MySqlGroupConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, ArgCompareArgCondition.class, MySqlArgCompareArgConditionConverter.getInstance());
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
        EzMybatisContent.addConverter(DbType.MYSQL, Alias.class, MySqlAliasArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, TableColumn.class, MySqlColumnArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, EntityField.class, MySqlFieldArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Keywords.class, MySqlKeywordsConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, ObjArg.class, MySqlObjArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.MYSQL, Sql.class, MySqlSqlArgConverter.getInstance());
    }
}
