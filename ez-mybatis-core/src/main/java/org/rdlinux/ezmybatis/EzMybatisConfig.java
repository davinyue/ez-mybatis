package org.rdlinux.ezmybatis;

import org.apache.ibatis.session.Configuration;

/**
 * 配置
 */
public class EzMybatisConfig {
    private Configuration configuration;
    /**
     * 转义关键词
     */
    private boolean escapeKeyword = true;

    public EzMybatisConfig(Configuration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("mybatis configuration can not be null");
        }
        this.configuration = configuration;
    }

    public boolean isEscapeKeyword() {
        return this.escapeKeyword;
    }

    public void setEscapeKeyword(boolean escapeKeyword) {
        this.escapeKeyword = escapeKeyword;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }
}
