package org.rdlinux.ezmybatis.core.sqlstruct;

import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * 对象参数
 */
public class ObjArg implements Operand, QueryRetNeedAlias {
    private Object arg;
    private EntityTable entityTable;
    private String field;

    private ObjArg(Object arg) {
        this.arg = arg;
    }

    public static ObjArg of(Object arg) {
        return new ObjArg(arg);
    }

    public Object getArg() {
        return this.arg;
    }

    public EntityTable getEntityTable() {
        return this.entityTable;
    }

    public ObjArg setEntityTable(EntityTable entityTable) {
        this.entityTable = entityTable;
        return this;
    }

    public String getField() {
        return this.field;
    }

    public ObjArg setField(String field) {
        this.field = field;
        return this;
    }
}
