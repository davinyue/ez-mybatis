package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 选择一个自定义值
 */
@Getter
public class SelectValue extends AbstractSelectItem implements SqlStruct {
    private Object value;

    public SelectValue(String value, String alias) {
        Assert.notNull(alias, "alias can not be null");
        this.setAlias(alias);
        this.value = value;
    }

    public SelectValue(Number value, String alias) {
        Assert.notNull(alias, "alias can not be null");
        this.setAlias(alias);
        this.value = value;
    }
}
