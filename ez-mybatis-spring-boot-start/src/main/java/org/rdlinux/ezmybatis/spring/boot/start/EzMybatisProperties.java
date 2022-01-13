package org.rdlinux.ezmybatis.spring.boot.start;

import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.rdlinux.ezmybatis.constant.DbType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
public class EzMybatisProperties {
    /**
     * 数据库类型
     */
    private DbType dbType;

    public DbType getDbType() {
        return this.dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }
}
