package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class SelectFunction extends AbstractSelectItem implements SqlStruct {
    private Function function;

    public SelectFunction(Function function, String alias) {
        Assert.notNull(alias, "alias can not be null");
        this.setAlias(alias);
        Assert.notNull(function, "function can not be null");
        this.function = function;
    }
}
