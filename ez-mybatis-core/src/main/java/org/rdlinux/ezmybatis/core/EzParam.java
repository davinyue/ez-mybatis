package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

@Getter
public abstract class EzParam<Rt> {
    /**
     * 返回结果
     */
    protected Class<Rt> retType;
    protected EntityTable table;
    protected From from;
    protected Where where;

    public EzParam(Class<Rt> retType) {
        this.retType = retType;
    }
}
