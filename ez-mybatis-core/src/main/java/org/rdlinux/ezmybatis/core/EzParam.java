package org.rdlinux.ezmybatis.core;

import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import lombok.Getter;

@Getter
public abstract class EzParam<Rt> {
    /**
     * 返回结果
     */
    protected Class<Rt> retType;
    protected Table table;
    protected From from;
    protected Where where;

    public EzParam(Class<Rt> retType) {
        this.retType = retType;
    }
}
