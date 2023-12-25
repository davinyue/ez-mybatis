package org.rdlinux.ezmybatis.core.sqlstruct;

import org.apache.commons.lang3.StringUtils;

/**
 * 关键词
 */
public class Keywords implements QueryRetOperand {
    private String keywords;

    private Keywords(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            throw new IllegalArgumentException("keywords can not be emtpy");
        }
        this.keywords = keywords;
    }

    public static Keywords of(String keywords) {
        return new Keywords(keywords);
    }

    public String getKeywords() {
        return this.keywords;
    }
}
