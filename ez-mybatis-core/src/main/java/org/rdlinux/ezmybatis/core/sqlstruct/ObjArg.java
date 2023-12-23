package org.rdlinux.ezmybatis.core.sqlstruct;

/**
 * 对象参数
 */
public class ObjArg implements Operand, QueryRetNeedAlias {
    private Object arg;

    private ObjArg(Object arg) {
        this.arg = arg;
    }

    public static ObjArg of(Object arg) {
        return new ObjArg(arg);
    }

    public Object getArg() {
        return this.arg;
    }
}
