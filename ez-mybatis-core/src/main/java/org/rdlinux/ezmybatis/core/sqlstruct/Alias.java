package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 别名参数
 */
@Getter
public class Alias implements Operand {
    private final String alias;

    private Alias(String alias) {
        Assert.notNull(alias, "alias can not be null");
        this.alias = alias;
    }

    public static Alias of(String alias) {
        return new Alias(alias);
    }
}
