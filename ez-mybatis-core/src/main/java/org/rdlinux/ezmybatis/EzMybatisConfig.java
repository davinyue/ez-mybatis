package org.rdlinux.ezmybatis;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.MapRetKeyPattern;
import org.rdlinux.ezmybatis.constant.TableNamePattern;

/**
 * 配置
 */
@Getter
@Setter
public class EzMybatisConfig {
    private Configuration configuration;
    /**
     * 转义关键词
     */

    private boolean escapeKeyword = true;
    /**
     * 查询结果使用map接收的key格式
     */

    private MapRetKeyPattern mapRetKeyPattern;
    /**
     * 表名转换格式
     */

    private TableNamePattern tableNamePattern = TableNamePattern.ORIGINAL;
    /**
     * 启用oracle offset fetch分页
     */
    private boolean enableOracleOffsetFetchPage = false;

    public EzMybatisConfig(Configuration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("mybatis configuration can not be null");
        }
        this.configuration = configuration;
        this.mapRetKeyPattern = MapRetKeyPattern.HUMP;
    }
}
