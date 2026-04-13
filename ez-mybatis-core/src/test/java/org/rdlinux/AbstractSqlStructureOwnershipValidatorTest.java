package org.rdlinux;

import org.junit.Assert;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.validation.SqlStructureOwnershipValidator;

public abstract class AbstractSqlStructureOwnershipValidatorTest {
    protected static final String OPERAND_SCOPE_MESSAGE = "is outside current scope";
    protected static final String UPDATE_SCOPE_MESSAGE = "update target";

    protected EzQuery.EzQueryBuilder<Object> baseQueryWithJoin(Tables t) {
        return EzQuery.builder(Object.class)
                .from(t.user)
                .join(t.dept, j -> j.add(t.user.column("dept_id").eq(t.dept.column("id"))));
    }

    protected void assertIllegalArgument(String expectedMessage, Runnable runnable) {
        try {
            runnable.run();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Expected message to contain: " + expectedMessage + ", but was: " + e.getMessage(),
                    e.getMessage() != null && e.getMessage().contains(expectedMessage));
        }
    }

    protected void assertQueryValidationFailure(String expectedMessage, EzQuery<?> query) {
        assertIllegalArgument(expectedMessage, new Runnable() {
            @Override
            public void run() {
                SqlStructureOwnershipValidator.validate(query);
            }
        });
    }

    protected void assertUpdateValidationFailure(String expectedMessage, EzUpdate update) {
        assertIllegalArgument(expectedMessage, new Runnable() {
            @Override
            public void run() {
                SqlStructureOwnershipValidator.validate(update);
            }
        });
    }

    protected void assertDeleteValidationFailure(String expectedMessage, EzDelete delete) {
        assertIllegalArgument(expectedMessage, new Runnable() {
            @Override
            public void run() {
                SqlStructureOwnershipValidator.validate(delete);
            }
        });
    }

    protected static class Tables {
        protected final Table user = DbTable.of("user_info");
        protected final Table dept = DbTable.of("dept_info");
        protected final Table role = DbTable.of("role_info");
    }
}
