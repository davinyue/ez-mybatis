package org.rdlinux;

import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.validation.SqlStructureOwnershipValidator;

public class EzUpdateTest {
    @Test
    public void shouldAllowUpdateItemsFromSameTableInstance() {
        Table table = DbTable.of("user_info");

        EzUpdate update = EzUpdate.update(table)
                .set(s -> s.add(table.column("username").set("alice")))
                .build();

        Assert.assertNotNull(update);
    }

    @Test
    public void shouldRejectUpdateItemsFromDifferentTableInstance() {
        Table updateTable = DbTable.of("user_info");
        Table anotherTable = DbTable.of("user_info");

        EzUpdate update = EzUpdate.update(updateTable)
                .set(s -> s.add(anotherTable.column("username").set("alice")))
                .build();
        try {
            SqlStructureOwnershipValidator.validate(update);
            Assert.fail("Expected validation to reject update item from another table instance");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("update target"));
        }
    }

    @Test
    public void shouldAllowUpdateItemsFromJoinedTableInstance() {
        Table updateTable = DbTable.of("user_info");
        Table joinedTable = DbTable.of("department_info");

        EzUpdate update = EzUpdate.update(updateTable)
                .join(joinedTable, j -> {
                })
                .set(s -> s.add(joinedTable.column("name").set("tech")))
                .build();

        Assert.assertNotNull(update);
    }

    @Test
    public void shouldSkipLazyUpdateItemWhenSureIsFalse() {
        Table table = DbTable.of("user_info");
        String fieldName = null;

        EzUpdate update = EzUpdate.update(table)
                .set(s -> s.add(fieldName != null, ss -> ss.add(table.column(fieldName).set("alice"))))
                .build();

        Assert.assertNotNull(update);
        Assert.assertTrue(update.getSet().getItems().isEmpty());
    }
}
