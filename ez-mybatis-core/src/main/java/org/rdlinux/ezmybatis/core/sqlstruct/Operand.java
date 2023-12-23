package org.rdlinux.ezmybatis.core.sqlstruct;

/**
 * 操作数, 左值/右值
 */
public interface Operand extends SqlStruct {
    static Operand objToOperand(Object object) {
        if (object instanceof Operand) {
            return (Operand) object;
        } else {
            return ObjArg.of(object);
        }
    }
}
