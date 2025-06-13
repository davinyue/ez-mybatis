package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 限制操作数量
 */
@Getter
public class Limit implements SqlStruct {
    private final int size;

    public Limit(int size) {
        Assert.isTrue(size >= 1, "limit size must be greater than or equal 1");
        this.size = size;
    }
}
