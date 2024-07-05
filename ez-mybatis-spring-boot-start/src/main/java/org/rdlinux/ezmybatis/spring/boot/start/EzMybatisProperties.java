package org.rdlinux.ezmybatis.spring.boot.start;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.constant.MapRetKeyPattern;
import org.rdlinux.ezmybatis.constant.TableNamePattern;
import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
@ConfigurationProperties(prefix = EzMybatisProperties.EZ_MYBATIS_PREFIX)
public class EzMybatisProperties {
    public static final String EZ_MYBATIS_PREFIX = "ez-mybatis";
    /**
     * 数据库类型
     */
    private DbType dbType;
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
    private Boolean enableOracleOffsetFetchPage = false;

    public DbType getDbType() {
        return this.dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public boolean isEscapeKeyword() {
        return this.escapeKeyword;
    }

    public void setEscapeKeyword(boolean escapeKeyword) {
        this.escapeKeyword = escapeKeyword;
    }

    public MapRetKeyPattern getMapRetKeyPattern() {
        return this.mapRetKeyPattern;
    }

    public void setMapRetKeyPattern(MapRetKeyPattern mapRetKeyPattern) {
        this.mapRetKeyPattern = mapRetKeyPattern;
    }

    public TableNamePattern getTableNamePattern() {
        return this.tableNamePattern;
    }

    public void setTableNamePattern(TableNamePattern tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    public Boolean getEnableOracleOffsetFetchPage() {
        return this.enableOracleOffsetFetchPage;
    }

    public void setEnableOracleOffsetFetchPage(Boolean enableOracleOffsetFetchPage) {
        this.enableOracleOffsetFetchPage = enableOracleOffsetFetchPage;
    }
}
