package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.constant.DbType;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * DbDialectProvider 的 SPI 加载管理器
 * <p>
 * 集中管理 ServiceLoader 加载逻辑，避免各处重复加载。
 */

public class DbDialectProviderLoader {
    private static final Map<DbType, DbDialectProvider> PROVIDER_MAP = new HashMap<>();

    static {
        ServiceLoader<DbDialectProvider> loader = ServiceLoader.load(DbDialectProvider.class);
        for (DbDialectProvider provider : loader) {
            PROVIDER_MAP.put(provider.getDbType(), provider);
        }
    }

    /**
     * 按 DbType 查找 Provider
     */
    public static DbDialectProvider getProvider(DbType dbType) {
        DbDialectProvider provider = PROVIDER_MAP.get(dbType);
        if (provider == null) {
            throw new RuntimeException("Cannot find DbDialectProvider for DbType: " + dbType);
        }
        return provider;
    }

    /**
     * 根据驱动名匹配 DbType
     */
    public static DbType matchDbType(String driverClassName) {
        for (DbDialectProvider provider : PROVIDER_MAP.values()) {
            if (provider.matchDriver(driverClassName)) {
                return provider.getDbType();
            }
        }
        throw new RuntimeException("Cannot match any DbDialectProvider for driver: " + driverClassName);
    }

    /**
     * 获取所有已加载的 Provider
     */
    public static Map<DbType, DbDialectProvider> getAllProviders() {
        return PROVIDER_MAP;
    }
}
