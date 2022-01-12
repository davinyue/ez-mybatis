package org.rdlinux.ezmybatis.core.content.entityinfo;

/**
 * 实体信息构建配置
 */
public class EntityInfoBuildConfig {
    /**
     * 列名处理方式
     */
    private ColumnHandle columnHandle;

    public EntityInfoBuildConfig(ColumnHandle columnHandle) {
        this.columnHandle = columnHandle;
    }

    public ColumnHandle getColumnHandle() {
        return this.columnHandle;
    }

    public void setColumnHandle(ColumnHandle columnHandle) {
        this.columnHandle = columnHandle;
    }

    /**
     * 列构建方式
     */
    public static enum ColumnHandle {
        /**
         * 原始的
         */
        ORIGINAL,
        /**
         * 转下划线
         */
        ToUnder,
        /**
         * 转下划线并大写
         */
        ToUnderAndUpper
    }
}
