package org.rdlinux.ezmybatis;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.MapRetKeyCasePolicy;
import org.rdlinux.ezmybatis.constant.NameCasePolicy;

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
     * 查询结果使用map接收的key转换策略
     */

    private MapRetKeyCasePolicy mapRetKeyCasePolicy;
    /**
     * 表名解析成功后二次转换处理策略
     */
    private NameCasePolicy tableNameCasePolicy = NameCasePolicy.ORIGINAL;
    /**
     * 列名解析成功后二次转换处理策略
     */
    private NameCasePolicy columnNameCasePolicy = NameCasePolicy.ORIGINAL;
    /**
     * 启用oracle offset fetch分页
     */
    private boolean enableOracleOffsetFetchPage = false;

    public EzMybatisConfig(Configuration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("mybatis configuration can not be null");
        }
        this.configuration = configuration;
        this.mapRetKeyCasePolicy = MapRetKeyCasePolicy.HUMP;
    }
}
