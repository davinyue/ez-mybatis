package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;

/**
 * 对象参数
 */
@Getter
public class ObjArg implements QueryRetNeedAlias {
    private final Object arg;

    private ObjArg(Object arg) {
        this.arg = arg;
    }

    public static ObjArg of(Object arg) {
        return new ObjArg(arg);
    }
}
