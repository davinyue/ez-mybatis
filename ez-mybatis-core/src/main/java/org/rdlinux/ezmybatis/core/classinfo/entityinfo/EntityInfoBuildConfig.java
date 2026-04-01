package org.rdlinux.ezmybatis.core.classinfo.entityinfo;

import lombok.Getter;
import org.rdlinux.ezmybatis.constant.NameCasePolicy;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 实体信息构建配置
 */
@Getter
public class EntityInfoBuildConfig {
    /**
     * 表名解析成功后处理策略
     */
    private final NameCasePolicy tableNameCasePolicy;
    /**
     * 列名构造策略
     */
    private final ColumnNameBuildPolicy columnNameBuildPolicy;
    /**
     * 列名解析成功后处理策略
     */
    private final NameCasePolicy columnNameCasePolicy;


    /**
     * 实体信息构造配置
     *
     * @param tableNameCasePolicy   表名解析成功后处理策略
     * @param columnNameBuildPolicy 列名构造策略
     * @param columnNameCasePolicy  列名解析成功后处理策略
     */
    public EntityInfoBuildConfig(NameCasePolicy tableNameCasePolicy, ColumnNameBuildPolicy columnNameBuildPolicy,
                                 NameCasePolicy columnNameCasePolicy) {
        Assert.notNull(tableNameCasePolicy, "tableNamePattern can not be null");
        Assert.notNull(columnNameBuildPolicy, "columnNameBuildPolicy can not be null");
        Assert.notNull(columnNameCasePolicy, "columnNamePattern can not be null");
        this.tableNameCasePolicy = tableNameCasePolicy;
        this.columnNameBuildPolicy = columnNameBuildPolicy;
        this.columnNameCasePolicy = columnNameCasePolicy;
    }


    /**
     * 列构建方式
     */
    public enum ColumnNameBuildPolicy {
        /**
         * 原始的
         */
        ORIGINAL,
        /**
         * 转下划线
         */
        TO_UNDER,
        /**
         * 转下划线并大写
         */
        TO_UNDER_AND_UPPER
    }
}
