package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 多查询结果连接
 */
@Getter
public class Union implements SqlStruct {
    private boolean all;
    private EzQuery<?> query;

    public Union(boolean all, EzQuery<?> query) {
        Assert.notNull(query, "query can not be null");
        this.all = all;
        this.query = query;
    }
}
