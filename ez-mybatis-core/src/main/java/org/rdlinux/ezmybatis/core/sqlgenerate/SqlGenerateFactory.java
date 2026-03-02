package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.constant.DbType;

/**
 * SQL生成器工厂
 * <p>
 * 通过SPI机制动态发现并注册各数据库方言的SQL生成器。
 */
public class SqlGenerateFactory {

    public static SqlGenerate getSqlGenerate(DbType dbType) {
        DbDialectProvider provider = DbDialectProviderLoader.getProvider(dbType);
        if (provider == null) {
            throw new RuntimeException("Cannot find DbDialectProvider for DbType: " + dbType);
        }
        return provider.getSqlGenerate();
    }
}
