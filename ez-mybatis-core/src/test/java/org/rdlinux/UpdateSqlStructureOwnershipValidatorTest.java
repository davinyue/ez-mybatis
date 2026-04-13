package org.rdlinux;

import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzUpdate;

public class UpdateSqlStructureOwnershipValidatorTest extends AbstractSqlStructureOwnershipValidatorTest {
    @Test
    public void shouldAllowUpdateSetAndWhereUsingMainAndJoinedTables() {
        Tables t = new Tables();
        EzUpdate update = EzUpdate.update(t.user)
                .join(t.dept, j -> j.add(t.user.column("dept_id").eq(t.dept.column("id"))))
                .set(s -> s.add(t.dept.column("name").set("tech"))
                        .add(t.user.column("dept_name").set(t.dept.column("name"))))
                .where(w -> w.add(t.user.column("dept_id").eq(t.dept.column("id"))))
                .build();
        Assert.assertNotNull(update);
    }

    @Test
    public void shouldRejectUpdateSetTargetUsingTableOutsideScope() {
        Tables t = new Tables();
        EzUpdate update = EzUpdate.update(t.user)
                .join(t.dept, j -> {
                })
                .set(s -> s.add(t.role.column("name").set("bad")))
                .build();
        assertUpdateValidationFailure(UPDATE_SCOPE_MESSAGE, update);
    }

    @Test
    public void shouldRejectUpdateSetValueUsingTableOutsideScope() {
        Tables t = new Tables();
        EzUpdate update = EzUpdate.update(t.user)
                .join(t.dept, j -> {
                })
                .set(s -> s.add(t.user.column("dept_name").set(t.role.column("name"))))
                .build();
        assertUpdateValidationFailure(OPERAND_SCOPE_MESSAGE, update);
    }

    @Test
    public void shouldRejectUpdateJoinOnUsingTableOutsideScope() {
        Tables t = new Tables();
        EzUpdate update = EzUpdate.update(t.user)
                .join(t.dept, j -> j.add(t.role.column("id").eq(t.user.column("role_id"))))
                .set(s -> s.add(t.user.column("name").set("ok")))
                .build();
        assertUpdateValidationFailure(OPERAND_SCOPE_MESSAGE, update);
    }
}
