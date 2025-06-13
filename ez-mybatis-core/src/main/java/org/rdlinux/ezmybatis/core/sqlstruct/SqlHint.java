package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;

/**
 * sql提示
 */
@Getter
public class SqlHint implements SqlStruct {
    /**
     * 提示内容
     */
    private final String hint;

    public SqlHint(String hint) {
        this.hint = hint;
    }
}
