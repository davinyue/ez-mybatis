package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 多查询结果连接
 */
@Getter
public class Union implements SqlStruct {
    /**
     * 是否union all
     */
    private final boolean all;
    /**
     * union的查询
     */
    private final EzQuery<?> query;

    /**
     * @param all   是否union all
     * @param query union的查询
     */
    public Union(boolean all, EzQuery<?> query) {
        Assert.notNull(query, "query can not be null");
        this.all = all;
        this.query = query;
    }
}
