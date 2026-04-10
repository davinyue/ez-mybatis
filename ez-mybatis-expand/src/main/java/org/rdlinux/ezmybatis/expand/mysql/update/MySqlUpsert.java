package org.rdlinux.ezmybatis.expand.mysql.update;

import lombok.Getter;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlExpand;
import org.rdlinux.ezmybatis.core.sqlstruct.UpdateSet;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateSetBuilder;
import org.rdlinux.ezmybatis.expand.mysql.converter.MySqlUpsertConverter;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * MySQL `INSERT ... ON DUPLICATE KEY UPDATE` 扩展结构。
 *
 * <p>该类用于描述一次 upsert 操作，既包含插入实体，也包含主键或唯一键冲突时的更新集合。
 * 构建时至少需要能够确定目标表和插入实体，且更新集合不能为空。</p>
 */
@Getter
public class MySqlUpsert implements SqlExpand {
    /**
     * 注册 MySQL 方言下的 upsert 转换器。
     */
    static {
        EzMybatisContent.addConverter(DbType.MYSQL, MySqlUpsert.class, MySqlUpsertConverter.getInstance());
    }

    /**
     * upsert 目标表。
     */
    private Table table;
    /**
     * 要执行插入的实体对象。
     */
    private Object insertEntity;
    /**
     * 唯一键冲突时执行的更新集合。
     */
    private UpdateSet set;

    /**
     * 创建空的 upsert 结构。
     */
    private MySqlUpsert() {
    }

    /**
     * 以目标表作为起点创建 upsert 构造器。
     *
     * @param table upsert 目标表
     * @return upsert 构造器
     */
    public static Builder into(Table table) {
        return new Builder(table, null);
    }

    /**
     * 以插入实体作为起点创建 upsert 构造器。
     *
     * @param entity 插入实体
     * @return upsert 构造器
     */
    public static Builder insert(Object entity) {
        return new Builder(null, entity);
    }

    /**
     * {@link MySqlUpsert} 构造器。
     */
    public static class Builder {
        /**
         * 当前构建中的 upsert 结构。
         */
        private final MySqlUpsert upsert;

        /**
         * 使用目标表和插入实体初始化 upsert 构造器。
         *
         * @param table  目标表
         * @param entity 插入实体
         */
        private Builder(Table table, Object entity) {
            this.upsert = new MySqlUpsert();
            this.upsert.table = table;
            this.upsert.insertEntity = entity;
            this.upsert.set = new UpdateSet();
        }

        /**
         * 设置 upsert 目标表。
         *
         * @param table 目标表
         * @return 当前构造器
         */
        public Builder into(Table table) {
            this.upsert.table = table;
            return this;
        }

        /**
         * 设置待插入实体。
         *
         * @param entity 插入实体
         * @return 当前构造器
         */
        public Builder insert(Object entity) {
            this.upsert.insertEntity = entity;
            return this;
        }

        /**
         * 语义化占位方法，用于增强链式调用的可读性。
         *
         * @return 当前构造器
         */
        public Builder onDuplicateKeyUpdate() {
            return this;
        }

        /**
         * 获取冲突时更新集合的构造器。
         *
         * @return 更新集合构造器
         */
        public UpdateSetBuilder<Builder> set() {
            return new UpdateSetBuilder<>(this, this.resolveTable(), this.upsert.set);
        }

        /**
         * 完成构建并执行必要的结构校验。
         *
         * @return 构建完成的 upsert 结构
         */
        public MySqlUpsert build() {
            this.validate();
            return this.upsert;
        }

        /**
         * 推断当前 upsert 应使用的目标表。
         *
         * @return 目标表
         */
        private Table resolveTable() {
            if (this.upsert.table != null) {
                return this.upsert.table;
            }
            if (this.upsert.insertEntity == null) {
                throw new IllegalStateException("MySqlUpsert target table can not be resolved without insert entity");
            }
            return EntityTable.of(this.upsert.insertEntity.getClass());
        }

        /**
         * 校验 upsert 结构的必要条件。
         */
        private void validate() {
            Object entity = this.upsert.insertEntity;
            if (this.upsert.table == null && entity == null) {
                throw new IllegalStateException("MySqlUpsert table and insert entity can not both be null");
            }
            if (entity == null) {
                throw new IllegalStateException("MySqlUpsert insert entity can not be null");
            }
            if (entity instanceof Collection) {
                throw new IllegalStateException("MySqlUpsert insert entity can not be a collection");
            }
            if (entity instanceof Map) {
                throw new IllegalStateException("MySqlUpsert insert entity can not be a map");
            }
            if (entity.getClass().isArray() || Array.class.isAssignableFrom(entity.getClass())) {
                throw new IllegalStateException("MySqlUpsert insert entity can not be an array");
            }
            if (this.upsert.set == null || this.upsert.set.getItems() == null || this.upsert.set.getItems().isEmpty()) {
                throw new IllegalStateException("MySqlUpsert ON DUPLICATE KEY UPDATE items can not be empty");
            }
            if (this.upsert.table == null) {
                this.upsert.table = this.resolveTable();
            }
        }
    }
}
