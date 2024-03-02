package org.rdlinux.ezmybatis.core.sqlstruct;

import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 限制操作数量
 */
public class Limit implements SqlStruct {
    private int size;

    public Limit(int size) {
        Assert.isTrue(size >= 1, "limit size must be greater than or equal 1");
        this.size = size;
    }


    public int getSize() {
        return this.size;
    }
}
