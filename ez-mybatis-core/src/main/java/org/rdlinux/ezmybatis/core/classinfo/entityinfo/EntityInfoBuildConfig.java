package org.rdlinux.ezmybatis.core.classinfo.entityinfo;

import org.rdlinux.ezmybatis.constant.TableNamePattern;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 实体信息构建配置
 */
public class EntityInfoBuildConfig {
    /**
     * 表名转换格式
     */
    private TableNamePattern tableNamePattern;
    /**
     * 列名处理方式
     */
    private ColumnHandle columnHandle;

    public EntityInfoBuildConfig(TableNamePattern tableNamePattern, ColumnHandle columnHandle) {
        Assert.notNull(tableNamePattern, "tableNamePattern can not be null");
        Assert.notNull(columnHandle, "columnHandle can not be null");
        this.tableNamePattern = tableNamePattern;
        this.columnHandle = columnHandle;
    }

    public ColumnHandle getColumnHandle() {
        return this.columnHandle;
    }

    public TableNamePattern getTableNamePattern() {
        return this.tableNamePattern;
    }


    /**
     * 列构建方式
     */
    public enum ColumnHandle {
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
