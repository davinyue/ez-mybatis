package org.rdlinux;

import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzDelete;

public class DeleteSqlStructureOwnershipValidatorTest extends AbstractSqlStructureOwnershipValidatorTest {
    @Test
    public void shouldAllowDeleteWhereUsingMainAndJoinedTables() {
        Tables t = new Tables();
        EzDelete delete = EzDelete.delete(t.user)
                .join(t.dept, j -> j.add(t.user.column("dept_id").eq(t.dept.column("id"))))
                .where(w -> w.add(t.dept.column("id").eq(t.user.column("dept_id"))))
                .build();
        Assert.assertNotNull(delete);
    }

    @Test
    public void shouldRejectDeleteWhereUsingTableOutsideScope() {
        Tables t = new Tables();
        EzDelete delete = EzDelete.delete(t.user)
                .join(t.dept, j -> {
                })
                .where(w -> w.add(t.role.column("id").eq(t.user.column("role_id"))))
                .build();
        assertDeleteValidationFailure(OPERAND_SCOPE_MESSAGE, delete);
    }

    @Test
    public void shouldRejectDeleteJoinOnUsingTableOutsideScope() {
        Tables t = new Tables();
        EzDelete delete = EzDelete.delete(t.user)
                .join(t.dept, j -> j.add(t.role.column("id").eq(t.user.column("role_id"))))
                .build();
        assertDeleteValidationFailure(OPERAND_SCOPE_MESSAGE, delete);
    }
}
