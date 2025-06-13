package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 关键词
 */
@Getter
public class Keywords implements QueryRetOperand {
    private final String keywords;

    private Keywords(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            throw new IllegalArgumentException("keywords can not be emtpy");
        }
        this.keywords = keywords;
    }

    public static Keywords of(String keywords) {
        return new Keywords(keywords);
    }
}
