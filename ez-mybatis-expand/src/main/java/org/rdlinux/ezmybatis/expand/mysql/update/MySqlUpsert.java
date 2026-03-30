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

@Getter
public class MySqlUpsert implements SqlExpand {
    static {
        EzMybatisContent.addConverter(DbType.MYSQL, MySqlUpsert.class, MySqlUpsertConverter.getInstance());
    }

    private Table table;
    private Object insertEntity;
    private UpdateSet set;

    private MySqlUpsert() {
    }

    public static Builder into(Table table) {
        return new Builder(table, null);
    }

    public static Builder insert(Object entity) {
        return new Builder(null, entity);
    }

    public static class Builder {
        private final MySqlUpsert upsert;

        private Builder(Table table, Object entity) {
            this.upsert = new MySqlUpsert();
            this.upsert.table = table;
            this.upsert.insertEntity = entity;
            this.upsert.set = new UpdateSet();
        }

        public Builder into(Table table) {
            this.upsert.table = table;
            return this;
        }

        public Builder insert(Object entity) {
            this.upsert.insertEntity = entity;
            return this;
        }

        public Builder onDuplicateKeyUpdate() {
            return this;
        }

        public UpdateSetBuilder<Builder> set() {
            return new UpdateSetBuilder<>(this, this.resolveTable(), this.upsert.set);
        }

        public MySqlUpsert build() {
            this.validate();
            return this.upsert;
        }

        private Table resolveTable() {
            if (this.upsert.table != null) {
                return this.upsert.table;
            }
            if (this.upsert.insertEntity == null) {
                throw new IllegalStateException("MySqlUpsert target table can not be resolved without insert entity");
            }
            return EntityTable.of(this.upsert.insertEntity.getClass());
        }

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
