package org.rdlinux.ezmybatis.core.sqlstruct.arg;

import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 别名参数
 */
public class AliasArg implements Arg {
    private String alias;

    private AliasArg(String alias) {
        Assert.notNull(alias, "alias can not be null");
        this.alias = alias;
    }

    public static AliasArg of(String alias) {
        return new AliasArg(alias);
    }

    public String getAlias() {
        return this.alias;
    }
}
