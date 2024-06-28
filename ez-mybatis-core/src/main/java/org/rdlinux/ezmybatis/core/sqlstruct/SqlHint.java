package org.rdlinux.ezmybatis.core.sqlstruct;

/**
 * sql提示
 */
public class SqlHint implements SqlStruct {
    /**
     * 提示内容
     */
    private String hint;

    public SqlHint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return this.hint;
    }
}
