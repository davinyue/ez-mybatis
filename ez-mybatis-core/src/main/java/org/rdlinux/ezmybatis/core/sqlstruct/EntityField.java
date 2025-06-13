package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 属性参数
 */
@Getter
public class EntityField implements QueryRetOperand {
    private final EntityTable table;
    private final String field;

    private EntityField(EntityTable table, String field) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(field, "field can not be null");
        this.table = table;
        this.field = field;
    }

    public static EntityField of(EntityTable table, String field) {
        return new EntityField(table, field);
    }

}
