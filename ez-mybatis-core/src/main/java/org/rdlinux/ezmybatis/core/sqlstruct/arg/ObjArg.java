package org.rdlinux.ezmybatis.core.sqlstruct.arg;

/**
 * 对象参数
 */
public class ObjArg implements Arg {
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
