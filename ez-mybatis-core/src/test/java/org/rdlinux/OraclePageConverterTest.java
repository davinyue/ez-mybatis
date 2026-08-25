package org.rdlinux;

import org.apache.ibatis.session.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Page;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.OraclePageConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * Oracle ROWNUM 分页转换器测试。
 */
public class OraclePageConverterTest {
    /**
     * 清理测试使用的 Ez-MyBatis 配置。
     */
    @After
    public void tearDown() {
        EzMybatisContent.destroyAll();
    }

    /**
     * 验证非第一页的无排序分页会将原 SQL 放入内层查询。
     */
    @Test
    public void shouldWrapOriginalSqlForRowNumPage() {
        Configuration configuration = new Configuration();
        EzMybatisConfig config = new EzMybatisConfig(configuration);
        config.setDbType(DbType.ORACLE);
        config.setEnableOracleOffsetFetchPage(false);
        EzMybatisContent.init(config);

        Map<String, Object> params = new HashMap<>();
        params.put(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION, configuration);
        SqlGenerateContext context = SqlGenerateContext.fromMyBatisParam(params);
        String originalSql = "SELECT u.* , ROWNUM \"ORA_ROWNUM__$\" FROM users u WHERE ROWNUM <= 50";
        context.getSqlBuilder().append(originalSql);

        OraclePageConverter.getInstance().buildSql(Converter.Type.SELECT, new Page(25, 25), context);

        String sql = context.getSqlBuilder().toString();
        int projectionEnd = sql.indexOf(".*  FROM ( ");
        Assert.assertTrue(projectionEnd > 0);
        String bodyAlias = sql.substring("SELECT ".length(), projectionEnd);
        String expectedSql = "SELECT " + bodyAlias + ".*  FROM ( " + originalSql + " ) " + bodyAlias
                + " WHERE " + bodyAlias + ".\"ORA_ROWNUM__$\" > 25";
        Assert.assertEquals(expectedSql, sql);
    }
}
