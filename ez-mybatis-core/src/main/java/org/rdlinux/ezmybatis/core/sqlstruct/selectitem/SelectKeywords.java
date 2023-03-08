package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.utils.Assert;

@Getter
public class SelectKeywords extends AbstractSelectItem implements SqlStruct {
    private String keywords;

    public SelectKeywords(String keywords, String alias) {
        this.setAlias(alias);
        Assert.notEmpty(keywords, "keywords can not be null");
        this.keywords = keywords;
    }
}
