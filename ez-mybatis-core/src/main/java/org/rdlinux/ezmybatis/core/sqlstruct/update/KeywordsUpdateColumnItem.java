package org.rdlinux.ezmybatis.core.sqlstruct.update;

import org.rdlinux.ezmybatis.core.sqlstruct.Keywords;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

public class KeywordsUpdateColumnItem extends UpdateItem {
    private String column;
    private Keywords keywords;

    public KeywordsUpdateColumnItem(Table table, String column, Keywords keywords) {
        super(table);
        Assert.notEmpty(column, "column can not be null");
        Assert.notNull(keywords, "keywords can not be null");
        this.column = column;
        this.keywords = keywords;
    }

    public String getColumn() {
        return this.column;
    }

    public Keywords getKeywords() {
        return this.keywords;
    }
}
