package org.rdlinux;

import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

public class QuerySqlStructureOwnershipValidatorTest extends AbstractSqlStructureOwnershipValidatorTest {
    @Test
    public void shouldAllowQueryWhereUsingMainTable() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .where(w -> w.add(t.user.column("id").eq(1)))
                .build();
        Assert.assertNotNull(query);
    }

    @Test
    public void shouldAllowQueryWhereUsingJoinedTable() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .where(w -> w.add(t.dept.column("id").eq(t.user.column("dept_id"))))
                .build();
        Assert.assertNotNull(query);
    }

    @Test
    public void shouldRejectQueryWhereUsingTableOutsideScope() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .where(w -> w.add(t.role.column("id").eq(t.user.column("role_id"))))
                .build();
        assertQueryValidationFailure(OPERAND_SCOPE_MESSAGE, query);
    }

    @Test
    public void shouldAllowQuerySelectUsingMainAndJoinedTables() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .select(s -> s.add(t.user.column("id")).add(t.dept.column("name")))
                .build();
        Assert.assertNotNull(query);
    }

    @Test
    public void shouldAllowQuerySelectAllUsingJoinedTable() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .select(s -> s.addAll(t.dept))
                .build();
        Assert.assertNotNull(query);
    }

    @Test
    public void shouldRejectQuerySelectUsingTableOutsideScope() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .select(s -> s.add(t.role.column("name")))
                .build();
        assertQueryValidationFailure(OPERAND_SCOPE_MESSAGE, query);
    }

    @Test
    public void shouldRejectQuerySelectAllUsingTableOutsideScope() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .select(s -> s.addAll(t.role))
                .build();
        assertQueryValidationFailure("select table", query);
    }

    @Test
    public void shouldAllowQueryGroupByUsingMainAndJoinedTables() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .groupBy(g -> g.add(t.user.column("id")).add(t.dept.column("name")))
                .build();
        Assert.assertNotNull(query);
    }

    @Test
    public void shouldRejectQueryGroupByUsingTableOutsideScope() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .groupBy(g -> g.add(t.role.column("name")))
                .build();
        assertQueryValidationFailure(OPERAND_SCOPE_MESSAGE, query);
    }

    @Test
    public void shouldAllowQueryHavingUsingMainAndJoinedTables() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .having(h -> h.add(Function.build("COUNT", f -> f.addArg(t.dept.column("id"))).gt(0)))
                .build();
        Assert.assertNotNull(query);
    }

    @Test
    public void shouldRejectQueryHavingUsingTableOutsideScope() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .having(h -> h.add(Function.build("COUNT", f -> f.addArg(t.role.column("id"))).gt(0)))
                .build();
        assertQueryValidationFailure(OPERAND_SCOPE_MESSAGE, query);
    }

    @Test
    public void shouldAllowQueryOrderByUsingMainAndJoinedTables() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .orderBy(o -> o.add(t.user.column("id")).add(t.dept.column("name")))
                .build();
        Assert.assertNotNull(query);
    }

    @Test
    public void shouldRejectQueryOrderByUsingTableOutsideScope() {
        Tables t = new Tables();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .orderBy(o -> o.add(t.role.column("name")))
                .build();
        assertQueryValidationFailure(OPERAND_SCOPE_MESSAGE, query);
    }

    @Test
    public void shouldAllowQueryNestedFormulaAndCaseWhenUsingVisibleTables() {
        Tables t = new Tables();
        Formula formula = Formula.build(f -> f.with(t.user.column("age")).add(t.dept.column("level")));
        CaseWhen caseWhen = CaseWhen.build(c -> c.when(w -> w.add(t.dept.column("flag").eq(1)), t.dept.column("name"))
                .els(t.user.column("name")));
        EzQuery<Object> query = baseQueryWithJoin(t)
                .select(s -> s.add(formula, "score_formula").add(caseWhen, "name_case"))
                .build();
        Assert.assertNotNull(query);
    }

    @Test
    public void shouldRejectQueryNestedFormulaUsingTableOutsideScope() {
        Tables t = new Tables();
        Formula formula = Formula.build(f -> f.with(t.user.column("age")).add(t.role.column("level")));
        EzQuery<Object> query = baseQueryWithJoin(t)
                .select(s -> s.add(formula, "score_formula"))
                .build();
        assertQueryValidationFailure(OPERAND_SCOPE_MESSAGE, query);
    }

    @Test
    public void shouldRejectQueryNestedCaseWhenUsingTableOutsideScope() {
        Tables t = new Tables();
        CaseWhen caseWhen = CaseWhen.build(c -> c.when(w -> w.add(t.role.column("flag").eq(1)), t.dept.column("name"))
                .els(t.user.column("name")));
        EzQuery<Object> query = baseQueryWithJoin(t)
                .select(s -> s.add(caseWhen, "name_case"))
                .build();
        assertQueryValidationFailure(OPERAND_SCOPE_MESSAGE, query);
    }

    @Test
    public void shouldAllowExistsSubQueryUsingItsOwnVisibleTables() {
        Tables t = new Tables();
        Table subDept = DbTable.of("dept_info");
        EzQuery<Object> subQuery = EzQuery.builder(Object.class)
                .from(subDept)
                .where(w -> w.add(subDept.column("id").eq(1)))
                .build();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .where(w -> w.add(subQuery.exists()))
                .build();
        Assert.assertNotNull(query);
    }

    @Test
    public void shouldAllowExistsSubQueryUsingOuterQueryTable() {
        Tables t = new Tables();
        Table orderTable = DbTable.of("order_info");
        EzQuery<Object> subQuery = EzQuery.builder(Object.class)
                .from(orderTable)
                .where(w -> w.add(orderTable.column("user_id").eq(t.user.column("id"))))
                .build();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .where(w -> w.add(subQuery.exists()))
                .build();
        Assert.assertNotNull(query);
    }

    @Test
    public void shouldRejectExistsSubQueryUsingTableOutsideItsOwnScope() {
        Tables t = new Tables();
        Table subDept = DbTable.of("dept_info");
        EzQuery<Object> subQuery = EzQuery.builder(Object.class)
                .from(subDept)
                .where(w -> w.add(t.role.column("id").eq(subDept.column("id"))))
                .build();
        EzQuery<Object> query = baseQueryWithJoin(t)
                .where(w -> w.add(subQuery.exists()))
                .build();
        assertQueryValidationFailure(OPERAND_SCOPE_MESSAGE, query);
    }

    @Test
    public void shouldAllowJoinOnUsingCurrentAndAncestorTables() {
        Tables t = new Tables();
        EzQuery<Object> query = EzQuery.builder(Object.class)
                .from(t.user)
                .join(t.dept, j -> {
                    j.add(t.user.column("dept_id").eq(t.dept.column("id")));
                    j.join(t.role, jj -> jj.add(t.dept.column("role_id").eq(t.role.column("id"))));
                })
                .select(s -> s.add(t.user.column("id")))
                .build();
        Assert.assertNotNull(query);
    }

    @Test
    public void shouldRejectJoinOnUsingTableOutsideCurrentScope() {
        Tables t = new Tables();
        EzQuery<Object> query = EzQuery.builder(Object.class)
                .from(t.user)
                .join(t.dept, j -> j.add(t.role.column("id").eq(t.user.column("role_id"))))
                .select(s -> s.add(t.user.column("id")))
                .build();
        assertQueryValidationFailure(OPERAND_SCOPE_MESSAGE, query);
    }
}
