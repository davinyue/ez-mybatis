package org.rdlinux;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.EntityInfoBuilder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlDialectProvider;

public class EzMybatisContentIsolationTest {

    @After
    public void tearDown() {
        EzMybatisContent.destroyAll();
    }

    @Test
    public void shouldKeepProviderIsolationPerConfiguration() {
        Configuration configuration1 = new Configuration();
        Configuration configuration2 = new Configuration();
        EzMybatisContent.init(createConfig(configuration1, DbType.MYSQL));
        EzMybatisContent.init(createConfig(configuration2, DbType.MYSQL));

        TrackingMySqlDialectProvider provider1 = new TrackingMySqlDialectProvider("[cfg-1]");
        TrackingMySqlDialectProvider provider2 = new TrackingMySqlDialectProvider("[cfg-2]");
        Object globalProvider = EzMybatisContent.getDbDialectProvider(DbType.MYSQL);

        EzMybatisContent.setProvider(configuration1, provider1);
        EzMybatisContent.setProvider(configuration2, provider2);

        Assert.assertSame(provider1, EzMybatisContent.getDbDialectProvider(configuration1));
        Assert.assertSame(provider2, EzMybatisContent.getDbDialectProvider(configuration2));
        Assert.assertEquals("[cfg-1]", EzMybatisContent.getKeywordQuoteMark(configuration1));
        Assert.assertEquals("[cfg-2]", EzMybatisContent.getKeywordQuoteMark(configuration2));
        Assert.assertSame(globalProvider, EzMybatisContent.getDbDialectProvider(DbType.MYSQL));
        Assert.assertTrue(provider1.registerConvertersCalled);
        Assert.assertTrue(provider2.registerConvertersCalled);
    }

    @Test
    public void shouldDestroyOnlySpecifiedConfiguration() {
        Configuration configuration1 = new Configuration();
        Configuration configuration2 = new Configuration();
        EzMybatisContent.init(createConfig(configuration1, DbType.MYSQL));
        EzMybatisContent.init(createConfig(configuration2, DbType.MYSQL));

        EzMybatisContent.setProvider(configuration1, new TrackingMySqlDialectProvider("[cfg-1]"));
        EzMybatisContent.setProvider(configuration2, new TrackingMySqlDialectProvider("[cfg-2]"));

        EzMybatisContent.destroy(configuration1);

        try {
            EzMybatisContent.getContentConfig(configuration1);
            Assert.fail("Destroyed configuration should not be accessible");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("configurationConfig non-existent"));
        }
        Assert.assertEquals("[cfg-2]", EzMybatisContent.getKeywordQuoteMark(configuration2));
    }

    @Test
    public void shouldRequireManualDbTypeWhenDbTypeCannotBeRecognized() {
        try {
            EzMybatisContent.init(new EzMybatisConfig(new Configuration()));
            Assert.fail("Unrecognized database type should require manual configuration");
        } catch (RuntimeException e) {
            Assert.assertEquals("Cannot recognize database type automatically. Please set the database type first",
                    e.getMessage());
        }
    }

    @Test
    public void shouldRequireManualDbTypeWhenDriverCannotBeMatched() {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("org.example.UnknownDriver");
        Environment environment = new Environment("test", new JdbcTransactionFactory(), dataSource);
        Configuration configuration = new Configuration(environment);

        try {
            EzMybatisContent.init(new EzMybatisConfig(configuration));
            Assert.fail("Unmatched driver should require manual database type configuration");
        } catch (RuntimeException e) {
            Assert.assertEquals("Cannot recognize database type automatically. Please set the database type first",
                    e.getMessage());
        }
    }

    private EzMybatisConfig createConfig(Configuration configuration, DbType dbType) {
        EzMybatisConfig config = new EzMybatisConfig(configuration);
        config.setDbType(dbType);
        return config;
    }

    private static class TrackingMySqlDialectProvider extends MySqlDialectProvider {
        private final String keywordQuoteMark;
        private boolean registerConvertersCalled;

        private TrackingMySqlDialectProvider(String keywordQuoteMark) {
            this.keywordQuoteMark = keywordQuoteMark;
        }

        @Override
        public void registerConverters() {
            this.registerConvertersCalled = true;
            super.registerConverters();
        }

        @Override
        public String getKeywordQuoteMark() {
            return this.keywordQuoteMark;
        }

        @Override
        public EntityInfoBuilder getEntityInfoBuilder() {
            return super.getEntityInfoBuilder();
        }

        @Override
        public SqlGenerate getSqlGenerate() {
            return super.getSqlGenerate();
        }
    }
}
