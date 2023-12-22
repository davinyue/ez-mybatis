package org.rdlinux.ezmybatis.core.sqlstruct.arg;

import org.rdlinux.ezmybatis.utils.Assert;

/**
 * Keywords参数
 */
public class KeywordsArg implements Arg {
    private String keywords;

    private KeywordsArg(String keywords) {
        Assert.notNull(keywords, "keywords can not be null");
        this.keywords = keywords;
    }

    public static KeywordsArg of(String keywords) {
        return new KeywordsArg(keywords);
    }

    public String getKeywords() {
        return this.keywords;
    }
}
