package org.rdlinux.ezmybatis.core.sqlstruct.converter;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.SqlCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlArgCompareArgConditionConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg.*;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.*;
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
public class OracleConverterRegister {
    public static void register() {
        EzMybatisContent.addConverter(DbType.ORACLE, Where.class, OracleWhereConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Having.class, OracleHavingConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Join.class, OracleJoinConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, From.class, OracleFromConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, OrderBy.class, OracleOrderByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, OrderBy.OrderItem.class, OracleOrderItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Select.class, OracleSelectConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, GroupBy.class, OracleGroupByConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, GroupBy.GroupItem.class, OracleGroupItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Limit.class, OracleLimitConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, NormalPartition.class, OracleNormalPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SubPartition.class, OracleSubPartitionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, DbTable.class, OracleDbTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, EntityTable.class, OracleEntityTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, EzQueryTable.class, OracleEzQueryTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SqlTable.class, OracleSqlTableConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, CaseWhen.class, OracleCaseWhenConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectCaseWhen.class, OracleSelectCaseWhenConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectAllItem.class, OracleSelectAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectAvgColumn.class, OracleSelectAvgColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectColumn.class, OracleSelectColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectAvgField.class, OracleSelectAvgFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectCountField.class, OracleSelectCountFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectCountColumn.class, OracleSelectCountColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectField.class, OracleSelectFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectMaxColumn.class, OracleSelectMaxColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectMaxField.class, OracleSelectMaxFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectMinColumn.class, OracleSelectMinColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectMinField.class, OracleSelectMinFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectSumColumn.class, OracleSelectSumColumnConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectSumField.class, OracleSelectSumFieldConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectTableAllItem.class, OracleSelectTableAllItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectKeywords.class, OracleSelectKeywordsConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, CaseWhenUpdateColumnItem.class, OracleCaseWhenUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, CaseWhenUpdateFieldItem.class, OracleCaseWhenUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, UpdateColumnItem.class, OracleUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, UpdateFieldItem.class, OracleUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, FunctionUpdateFieldItem.class, OracleFunctionUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, FunctionUpdateColumnItem.class, OracleFunctionUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, FormulaUpdateFieldItem.class, OracleFormulaUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, FormulaUpdateColumnItem.class, OracleFormulaUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, KeywordsUpdateFieldItem.class, OracleKeywordsUpdateFieldItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, KeywordsUpdateColumnItem.class, OracleKeywordsUpdateColumnItemConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SqlCondition.class, OracleSqlConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, GroupCondition.class, OracleGroupConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, ArgCompareArgCondition.class, MySqlArgCompareArgConditionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, EzQuery.class, OracleEzQueryConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Function.class, OracleFunctionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Formula.class, OracleFormulaConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, GroupFormulaElement.class, OracleGroupFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, ColumnFormulaElement.class, OracleColumnFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, FieldFormulaElement.class, OracleFieldFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, FunFormulaElement.class, OracleFunFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, FormulaFormulaElement.class, OracleFormulaFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, CaseWhenFormulaElement.class, OracleCaseWhenFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, ValueFormulaElement.class, OracleValueFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, KeywordsFormulaElement.class, OracleKeywordsFormulaElementConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Union.class, OracleUnionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectFormula.class, OracleSelectFormulaConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectFunction.class, OracleSelectFunctionConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, SelectValue.class, OracleSelectValueConverter.getInstance());
        //argConverter
        EzMybatisContent.addConverter(DbType.ORACLE, Alias.class, MySqlAliasArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, TableColumn.class, MySqlColumnArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, EntityField.class, MySqlFieldArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Keywords.class, MySqlKeywordsConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, ObjArg.class, MySqlObjArgConverter.getInstance());
        EzMybatisContent.addConverter(DbType.ORACLE, Sql.class, MySqlSqlArgConverter.getInstance());

    }
}
