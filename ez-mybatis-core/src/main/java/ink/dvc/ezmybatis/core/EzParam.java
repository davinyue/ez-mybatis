package ink.dvc.ezmybatis.core;

import ink.dvc.ezmybatis.core.sqlstruct.From;
import ink.dvc.ezmybatis.core.sqlstruct.Where;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;
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
