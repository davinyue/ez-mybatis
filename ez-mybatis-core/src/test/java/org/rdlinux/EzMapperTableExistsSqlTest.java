package org.rdlinux;

import org.apache.ibatis.session.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.mapper.provider.EzSelectProvider;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;

import java.util.HashMap;
import java.util.Map;

public class EzMapperTableExistsSqlTest {

    @After
    public void tearDown() {
        EzMybatisContent.destroyAll();
    }

    @Test
    public void shouldGenerateTableExistsSqlForMySql() {
        Assert.assertEquals(
                "SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = #{mp_0[0]} " +
                        "AND table_name = #{mp_0[1]}",
                tableExistsSql(DbType.MYSQL, DbTable.of("app", "user_info")));
    }

    @Test
    public void shouldGenerateTableExistsSqlForPostgreSql() {
        Assert.assertEquals(
                "SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = #{mp_0[0]} " +
                        "AND table_name = #{mp_0[1]}",
                tableExistsSql(DbType.POSTGRE_SQL, DbTable.of("public", "user_info")));
    }

    @Test
    public void shouldGenerateTableExistsSqlForSqlServer() {
        Assert.assertEquals(
                "SELECT COUNT(1) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = #{mp_0[0]} " +
                        "AND TABLE_NAME = #{mp_0[1]}",
                tableExistsSql(DbType.SQL_SERVER, DbTable.of("dbo", "user_info")));
    }

    @Test
    public void shouldGenerateTableExistsSqlForOracle() {
        Assert.assertEquals(
                "SELECT COUNT(1) FROM ALL_TABLES WHERE OWNER = #{mp_0[0]} AND TABLE_NAME = #{mp_0[1]}",
                tableExistsSql(DbType.ORACLE, DbTable.of("APP", "USER_INFO")));
    }

    @Test
    public void shouldGenerateTableExistsSqlForDm() {
        Assert.assertEquals(
                "SELECT COUNT(1) FROM ALL_TABLES WHERE OWNER = #{mp_0[0]} AND TABLE_NAME = #{mp_0[1]}",
                tableExistsSql(DbType.DM, DbTable.of("APP", "USER_INFO")));
    }

    @Test
    public void shouldUseCurrentSchemaWhenSchemaIsMissing() {
        Assert.assertEquals(
                "SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() " +
                        "AND table_name = #{mp_0[0]}",
                tableExistsSql(DbType.MYSQL, DbTable.of("user_info")));
        Assert.assertEquals(
                "SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = CURRENT_SCHEMA() " +
                        "AND table_name = #{mp_0[0]}",
                tableExistsSql(DbType.POSTGRE_SQL, DbTable.of("user_info")));
        Assert.assertEquals(
                "SELECT COUNT(1) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA_NAME() " +
                        "AND TABLE_NAME = #{mp_0[0]}",
                tableExistsSql(DbType.SQL_SERVER, DbTable.of("user_info")));
        Assert.assertEquals(
                "SELECT COUNT(1) FROM ALL_TABLES WHERE OWNER = SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA') " +
                        "AND TABLE_NAME = #{mp_0[0]}",
                tableExistsSql(DbType.ORACLE, DbTable.of("USER_INFO")));
        Assert.assertEquals(
                "SELECT COUNT(1) FROM ALL_TABLES WHERE OWNER = SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA') " +
                        "AND TABLE_NAME = #{mp_0[0]}",
                tableExistsSql(DbType.DM, DbTable.of("USER_INFO")));
    }

    private String tableExistsSql(DbType dbType, DbTable table) {
        Configuration configuration = new Configuration();
        EzMybatisConfig config = new EzMybatisConfig(configuration);
        config.setDbType(dbType);
        EzMybatisContent.init(config);
        Map<String, Object> param = new HashMap<>();
        param.put(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION, configuration);
        param.put(EzMybatisConstant.MAPPER_PARAM_TABLE, table);
        return new EzSelectProvider().tableExists(param);
    }
}
