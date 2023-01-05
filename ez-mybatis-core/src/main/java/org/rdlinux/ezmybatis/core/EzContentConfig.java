package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.interceptor.EzMybatisUpdateInterceptor;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;

/**
 * content配置
 */
@Getter
@Setter
@Accessors(chain = true)
public class EzContentConfig {
    private EzMybatisConfig ezMybatisConfig;
    private Configuration configuration;
    private EzMybatisUpdateInterceptor updateInterceptor;
    private DbKeywordQMFactory dbKeywordQMFactory;
    private DbType dbType;
}
