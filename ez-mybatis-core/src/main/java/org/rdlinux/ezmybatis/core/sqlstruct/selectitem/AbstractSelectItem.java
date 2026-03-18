package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractSelectItem implements SelectItem {
    private String alias;
}
