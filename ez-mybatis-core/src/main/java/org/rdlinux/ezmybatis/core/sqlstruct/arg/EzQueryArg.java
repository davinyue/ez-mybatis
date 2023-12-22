package org.rdlinux.ezmybatis.core.sqlstruct.arg;

import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 函数参数
 */
public class EzQueryArg implements Arg {
    private EzQuery<?> ezQuery;

    private EzQueryArg(EzQuery<?> ezQuery) {
        Assert.notNull(ezQuery, "ezQuery can not be null");
        this.ezQuery = ezQuery;
    }

    public static EzQueryArg of(EzQuery<?> ezQuery) {
        return new EzQueryArg(ezQuery);
    }

    public EzQuery<?> getEzQuery() {
        return this.ezQuery;
    }
}
