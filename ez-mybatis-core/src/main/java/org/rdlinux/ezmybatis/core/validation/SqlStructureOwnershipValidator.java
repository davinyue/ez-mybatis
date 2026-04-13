package org.rdlinux.ezmybatis.core.validation;

import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlstruct.Alias;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.GroupBy;
import org.rdlinux.ezmybatis.core.sqlstruct.Having;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.Keywords;
import org.rdlinux.ezmybatis.core.sqlstruct.ObjArg;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy;
import org.rdlinux.ezmybatis.core.sqlstruct.Select;
import org.rdlinux.ezmybatis.core.sqlstruct.TableColumn;
import org.rdlinux.ezmybatis.core.sqlstruct.Union;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ExistsCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.SqlCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FormulaElement;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FormulaOperandElement;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.GroupFormulaElement;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectAllItem;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectItem;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectOperand;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectTableAllItem;
import org.rdlinux.ezmybatis.core.sqlstruct.table.AbstractTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateColumnItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public final class SqlStructureOwnershipValidator {
    private SqlStructureOwnershipValidator() {
    }

    public static void validate(EzQuery<?> query) {
        validate(query, null, "query");
    }

    public static void validate(EzUpdate update) {
        Assert.notNull(update, "update can not be null");
        Set<Table> allowedTables = SqlTableScopeResolver.resolve(update.getTable(), update.getJoins());
        validateJoins(update.getJoins(), singletonScope(update.getTable()), "update.joins");
        validateUpdateItems(update.getSet() == null ? null : update.getSet().getItems(), allowedTables, "update.set.items");
        validateWhere(update.getWhere(), allowedTables, "update.where");
    }

    public static void validate(EzDelete delete) {
        Assert.notNull(delete, "delete can not be null");
        Set<Table> allowedTables = SqlTableScopeResolver.resolve(delete.getTable(), delete.getJoins());
        validateJoins(delete.getJoins(), singletonScope(delete.getTable()), "delete.joins");
        validateWhere(delete.getWhere(), allowedTables, "delete.where");
    }

    private static void validate(EzQuery<?> query, Set<Table> inheritedTables, String path) {
        Assert.notNull(query, "query can not be null");
        Set<Table> allowedTables = mergeScope(SqlTableScopeResolver.resolve(query.getTable(), query.getJoins()), inheritedTables);
        validateJoins(query.getJoins(), mergeScope(singletonScope(query.getTable()), inheritedTables), path + ".joins");
        validateSelect(query.getSelect(), allowedTables, path + ".select");
        validateWhere(query.getWhere(), allowedTables, path + ".where");
        validateGroupBy(query.getGroupBy(), allowedTables, path + ".groupBy");
        validateHaving(query.getHaving(), allowedTables, path + ".having");
        validateOrderBy(query.getOrderBy(), allowedTables, path + ".orderBy");
        validateUnions(query.getUnions(), path + ".unions");
    }

    private static void validateUnions(List<Union> unions, String path) {
        if (unions == null || unions.isEmpty()) {
            return;
        }
        for (int i = 0; i < unions.size(); i++) {
            Union union = unions.get(i);
            if (union != null) {
                validate(union.getQuery(), null, path + "[" + i + "].query");
            }
        }
    }

    private static void validateJoins(List<Join> joins, Set<Table> visibleTables, String path) {
        if (joins == null || joins.isEmpty()) {
            return;
        }
        for (int i = 0; i < joins.size(); i++) {
            validateJoin(joins.get(i), visibleTables, path + "[" + i + "]");
        }
    }

    private static void validateJoin(Join join, Set<Table> visibleTables, String path) {
        Assert.notNull(join, "join can not be null");
        Table joinTable = join.getJoinTable();
        ensureTableAllowed(joinTable, extendScope(visibleTables, joinTable), path + ".joinTable",
                "join table", describeTable(joinTable));
        Set<Table> currentScope = extendScope(visibleTables, joinTable);
        validateConditions(join.getOnConditions(), currentScope, path + ".onConditions");
        validateJoins(join.getJoins(), currentScope, path + ".joins");
    }

    private static void validateSelect(Select select, Set<Table> allowedTables, String path) {
        if (select == null || select.getSelectItems() == null) {
            return;
        }
        for (int i = 0; i < select.getSelectItems().size(); i++) {
            validateSelectItem(select.getSelectItems().get(i), allowedTables, path + ".selectItems[" + i + "]");
        }
    }

    private static void validateSelectItem(SelectItem selectItem, Set<Table> allowedTables, String path) {
        Assert.notNull(selectItem, "select item can not be null");
        if (selectItem instanceof SelectAllItem) {
            return;
        }
        if (selectItem instanceof SelectTableAllItem) {
            SelectTableAllItem item = (SelectTableAllItem) selectItem;
            ensureTableAllowed(item.getTable(), allowedTables, path,
                    "select table", describeTable(item.getTable()));
            return;
        }
        if (selectItem instanceof SelectOperand) {
            validateOperand(((SelectOperand) selectItem).getOperand(), allowedTables, path + ".operand");
        }
    }

    private static void validateGroupBy(GroupBy groupBy, Set<Table> allowedTables, String path) {
        if (groupBy == null || groupBy.getItems() == null) {
            return;
        }
        for (int i = 0; i < groupBy.getItems().size(); i++) {
            validateOperand(groupBy.getItems().get(i), allowedTables, path + ".items[" + i + "]");
        }
    }

    private static void validateHaving(Having having, Set<Table> allowedTables, String path) {
        if (having == null) {
            return;
        }
        validateConditions(having.getConditions(), allowedTables, path + ".conditions");
    }

    private static void validateOrderBy(OrderBy orderBy, Set<Table> allowedTables, String path) {
        if (orderBy == null || orderBy.getItems() == null) {
            return;
        }
        for (int i = 0; i < orderBy.getItems().size(); i++) {
            OrderBy.OrderItem item = orderBy.getItems().get(i);
            Assert.notNull(item, "order item can not be null");
            validateOperand(item.getValue(), allowedTables, path + ".items[" + i + "].value");
        }
    }

    private static void validateWhere(Where where, Set<Table> allowedTables, String path) {
        if (where == null) {
            return;
        }
        validateConditions(where.getConditions(), allowedTables, path + ".conditions");
    }

    private static void validateConditions(List<Condition> conditions, Set<Table> allowedTables, String path) {
        if (conditions == null || conditions.isEmpty()) {
            return;
        }
        for (int i = 0; i < conditions.size(); i++) {
            validateCondition(conditions.get(i), allowedTables, path + "[" + i + "]");
        }
    }

    private static void validateCondition(Condition condition, Set<Table> allowedTables, String path) {
        Assert.notNull(condition, "condition can not be null");
        if (condition instanceof SqlCondition) {
            return;
        }
        if (condition instanceof GroupCondition) {
            validateConditions(((GroupCondition) condition).getConditions(), allowedTables, path + ".conditions");
            return;
        }
        if (condition instanceof ExistsCondition) {
            validate(((ExistsCondition) condition).getQuery(), allowedTables, path + ".query");
            return;
        }
        if (condition instanceof ArgCompareArgCondition) {
            ArgCompareArgCondition compareCondition = (ArgCompareArgCondition) condition;
            validateOperand(compareCondition.getLeftValue(), allowedTables, path + ".leftValue");
            validateOperand(compareCondition.getRightValue(), allowedTables, path + ".rightValue");
            validateOperand(compareCondition.getMinValue(), allowedTables, path + ".minValue");
            validateOperand(compareCondition.getMaxValue(), allowedTables, path + ".maxValue");
            if (compareCondition.getRightValues() != null) {
                for (int i = 0; i < compareCondition.getRightValues().size(); i++) {
                    validateOperand(compareCondition.getRightValues().get(i), allowedTables, path + ".rightValues[" + i + "]");
                }
            }
        }
    }

    private static void validateUpdateItems(List<UpdateItem> items, Set<Table> allowedTables, String path) {
        if (items == null || items.isEmpty()) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            UpdateItem item = items.get(i);
            Assert.notNull(item, "update item can not be null");
            ensureTableAllowed(item.getTable(), allowedTables, path + "[" + i + "]",
                    "update target", describeUpdateItem(item));
            if (item instanceof UpdateFieldItem) {
                validateOperand(((UpdateFieldItem) item).getValue(), allowedTables, path + "[" + i + "].value");
                continue;
            }
            if (item instanceof UpdateColumnItem) {
                validateOperand(((UpdateColumnItem) item).getValue(), allowedTables, path + "[" + i + "].value");
            }
        }
    }

    private static void validateOperand(Operand operand, Set<Table> allowedTables, String path) {
        if (operand == null || operand instanceof ObjArg || operand instanceof Alias || operand instanceof Keywords) {
            return;
        }
        if (operand instanceof EntityField) {
            EntityField field = (EntityField) operand;
            ensureTableAllowed(field.getTable(), allowedTables, path, "operand", describeEntityField(field));
            return;
        }
        if (operand instanceof TableColumn) {
            TableColumn column = (TableColumn) operand;
            ensureTableAllowed(column.getTable(), allowedTables, path, "operand", describeTableColumn(column));
            return;
        }
        if (operand instanceof Function) {
            validateFunction((Function) operand, allowedTables, path);
            return;
        }
        if (operand instanceof Formula) {
            validateFormula((Formula) operand, allowedTables, path);
            return;
        }
        if (operand instanceof CaseWhen) {
            validateCaseWhen((CaseWhen) operand, allowedTables, path);
            return;
        }
        if (operand instanceof EzQuery) {
            validate((EzQuery<?>) operand, allowedTables, path);
        }
    }

    private static void validateFunction(Function function, Set<Table> allowedTables, String path) {
        if (function.getFunArgs() == null) {
            return;
        }
        for (int i = 0; i < function.getFunArgs().size(); i++) {
            Function.FunArg funArg = function.getFunArgs().get(i);
            if (funArg != null) {
                validateOperand(funArg.getArgValue(), allowedTables, path + ".funArgs[" + i + "].argValue");
            }
        }
    }

    private static void validateFormula(Formula formula, Set<Table> allowedTables, String path) {
        if (formula.getElements() == null) {
            return;
        }
        for (int i = 0; i < formula.getElements().size(); i++) {
            validateFormulaElement(formula.getElements().get(i), allowedTables, path + ".elements[" + i + "]");
        }
    }

    private static void validateFormulaElement(FormulaElement element, Set<Table> allowedTables, String path) {
        if (element == null) {
            return;
        }
        if (element instanceof FormulaOperandElement) {
            validateOperand(((FormulaOperandElement) element).getOperand(), allowedTables, path + ".operand");
            return;
        }
        if (element instanceof GroupFormulaElement) {
            List<FormulaElement> children = ((GroupFormulaElement) element).getElements();
            for (int i = 0; i < children.size(); i++) {
                validateFormulaElement(children.get(i), allowedTables, path + ".elements[" + i + "]");
            }
        }
    }

    private static void validateCaseWhen(CaseWhen caseWhen, Set<Table> allowedTables, String path) {
        if (caseWhen.getCaseWhenData() != null) {
            for (int i = 0; i < caseWhen.getCaseWhenData().size(); i++) {
                CaseWhen.CaseWhenData datum = caseWhen.getCaseWhenData().get(i);
                if (datum == null) {
                    continue;
                }
                validateConditions(datum.getConditions(), allowedTables, path + ".caseWhenData[" + i + "].conditions");
                validateOperand(datum.getValue(), allowedTables, path + ".caseWhenData[" + i + "].value");
            }
        }
        if (caseWhen.getEls() != null) {
            validateOperand(caseWhen.getEls().getValue(), allowedTables, path + ".els.value");
        }
    }

    private static void ensureTableAllowed(Table table, Set<Table> allowedTables, String path, String objectType, String detail) {
        Assert.notNull(table, "table can not be null");
        if (!allowedTables.contains(table)) {
            throw new IllegalArgumentException(path + ": " + objectType + " " + detail
                    + " is outside current scope, allowedTables=" + renderAllowedTables(allowedTables));
        }
    }

    private static String describeTable(Table table) {
        if (table == null) {
            return "Table(null)";
        }
        return "Table(" + describeTableCore(table) + ")";
    }

    private static String describeEntityField(EntityField field) {
        return "EntityField(" + describeTableCore(field.getTable()) + ", field=" + field.getField() + ")";
    }

    private static String describeTableColumn(TableColumn column) {
        return "TableColumn(" + describeTableCore(column.getTable()) + ", column=" + column.getColumn() + ")";
    }

    private static String describeUpdateItem(UpdateItem item) {
        if (item instanceof UpdateFieldItem) {
            UpdateFieldItem fieldItem = (UpdateFieldItem) item;
            return "UpdateFieldItem(" + describeTableCore(fieldItem.getTable()) + ", field=" + fieldItem.getField() + ")";
        }
        if (item instanceof UpdateColumnItem) {
            UpdateColumnItem columnItem = (UpdateColumnItem) item;
            return "UpdateColumnItem(" + describeTableCore(columnItem.getTable()) + ", column=" + columnItem.getColumn() + ")";
        }
        return item.getClass().getSimpleName() + "(" + describeTableCore(item.getTable()) + ")";
    }

    private static String renderAllowedTables(Set<Table> allowedTables) {
        List<String> tables = new ArrayList<>();
        if (allowedTables != null) {
            for (Table table : allowedTables) {
                tables.add(table == null ? "null" : describeTableCore(table));
            }
        }
        return tables.toString();
    }

    private static String describeTableCore(Table table) {
        if (table == null) {
            return "null";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("type=").append(table.getClass().getSimpleName());
        if (table instanceof EntityTable) {
            builder.append(", entity=").append(((EntityTable) table).getEtType().getSimpleName());
        }
        if (table instanceof AbstractTable) {
            String tableName = ((AbstractTable) table).getTableName();
            if (tableName != null && !tableName.isEmpty()) {
                builder.append(", table=").append(tableName);
            }
        }
        builder.append(", alias=").append(table.getAlias());
        return builder.toString();
    }

    private static Set<Table> singletonScope(Table table) {
        Set<Table> tables = Collections.newSetFromMap(new IdentityHashMap<Table, Boolean>());
        if (table != null) {
            tables.add(table);
        }
        return tables;
    }

    private static Set<Table> extendScope(Set<Table> visibleTables, Table table) {
        Set<Table> tables = Collections.newSetFromMap(new IdentityHashMap<Table, Boolean>());
        if (visibleTables != null) {
            tables.addAll(visibleTables);
        }
        if (table != null) {
            tables.add(table);
        }
        return tables;
    }

    private static Set<Table> mergeScope(Set<Table> primaryTables, Set<Table> secondaryTables) {
        Set<Table> tables = Collections.newSetFromMap(new IdentityHashMap<Table, Boolean>());
        if (primaryTables != null) {
            tables.addAll(primaryTables);
        }
        if (secondaryTables != null) {
            tables.addAll(secondaryTables);
        }
        return tables;
    }
}
