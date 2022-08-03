package org.rdlinux.ezmybatis.utils;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlConverterRegister;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DbTypeUtils {
    /**
     * 配置与数据库类型映射
     */
    private static final ConcurrentMap<Configuration, DbType> DB_TYPE_MAP = new ConcurrentHashMap<>();

    public static void setDbType(Configuration configuration, DbType dbType) {
        Assert.notNull(configuration, "configuration can not be null");
        Assert.notNull(dbType, "dbType can not be null");
        DB_TYPE_MAP.put(configuration, dbType);
        if (dbType == DbType.MYSQL) {
            MySqlConverterRegister.register();
        }
    }

    public static DbType getDbType(Configuration configuration) {
        DbType dbType = DB_TYPE_MAP.get(configuration);
        if (dbType == null) {
            synchronized (configuration) {
                dbType = DB_TYPE_MAP.get(configuration);
                if (dbType == null) {
                    Environment environment = configuration.getEnvironment();
                    if (environment == null) {
                        throw new RuntimeException("Unsupported db type");
                    }
                    DataSource dataSource = environment.getDataSource();
                    if (dataSource == null) {
                        throw new RuntimeException("Unsupported db type");
                    }
                    String driver;
                    if (PooledDataSource.class.isAssignableFrom(dataSource.getClass())) {
                        driver = ((PooledDataSource) dataSource).getDriver();
                    } else {
                        if (dataSource.getClass().getName().contains("druid")) {
                            driver = ReflectionUtils.getFieldValue(dataSource, "driverClass");
                        } else {
                            driver = ReflectionUtils.getFieldValue(dataSource, "driverClassName");
                        }
                    }
                    Assert.notEmpty(driver, "Unsupported db type");
                    if (driver.contains("mysql")) {
                        dbType = DbType.MYSQL;
                    } else if (driver.contains("oracle")) {
                        dbType = DbType.ORACLE;
                    } else if (driver.toLowerCase().contains("dmdriver")) {
                        dbType = DbType.DM;
                    } else {
                        throw new RuntimeException("Unsupported db type");
                    }
                    setDbType(configuration, dbType);
                }
            }
        }
        return dbType;
    }
}
