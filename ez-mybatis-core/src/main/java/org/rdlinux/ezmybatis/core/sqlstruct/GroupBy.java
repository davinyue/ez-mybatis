package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzParam;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.group.ColumnGroupItem;
import org.rdlinux.ezmybatis.core.sqlstruct.group.FieldGroupItem;
import org.rdlinux.ezmybatis.core.sqlstruct.group.GroupItem;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GroupBy implements SqlStruct {
    private static final Map<DbType, SqlStruct> CONVERT = new HashMap<>();

    static {
        SqlStruct defaultConvert = (sqlBuilder, configuration, ezParam, mybatisParamHolder) ->
                GroupBy.defaultGroupByToSql(sqlBuilder, configuration, (EzQuery<?>) ezParam);
        CONVERT.put(DbType.MYSQL, defaultConvert);
        CONVERT.put(DbType.ORACLE, defaultConvert);
        CONVERT.put(DbType.DM, defaultConvert);
    }

    private List<GroupItem> items;

    public GroupBy(List<GroupItem> items) {
        this.items = items;
    }

    private static StringBuilder defaultGroupByToSql(StringBuilder sqlBuilder, Configuration configuration,
                                                     EzQuery<?> query) {
        GroupBy group = query.getGroupBy();
        if (group == null || group.getItems() == null) {
            return sqlBuilder;
        } else {
            StringBuilder sql = new StringBuilder(" GROUP BY ");
            for (int i = 0; i < group.getItems().size(); i++) {
                GroupItem groupItem = group.getItems().get(i);
                sql.append(groupItem.toSqlStruct(configuration));
                if (i + 1 < group.getItems().size()) {
                    sql.append(", ");
                } else {
                    sql.append(" ");
                }
            }
            return sqlBuilder.append(sql);
        }
    }

    @Override
    public StringBuilder toSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzParam<?> ezParam,
                                   MybatisParamHolder mybatisParamHolder) {
        return CONVERT.get(DbTypeUtils.getDbType(configuration)).toSqlPart(sqlBuilder, configuration, ezParam,
                mybatisParamHolder);
    }

    public static class GroupBuilder<T> {
        private T target;
        private Table table;
        private GroupBy groupBy;

        public GroupBuilder(T target, GroupBy groupBy, Table table) {
            this.target = target;
            this.groupBy = groupBy;
            this.table = table;
        }

        private void checkEntityTable() {
            if (!(this.table instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        /**
         * please use {@link #addField(String)} replace
         */
        @Deprecated
        public GroupBuilder<T> add(String field) {
            return this.addField(field);
        }

        public GroupBuilder<T> addField(String field) {
            this.checkEntityTable();
            this.groupBy.getItems().add(new FieldGroupItem((EntityTable) this.table, field));
            return this;
        }

        /**
         * please use {@link #addField(boolean, String)} replace
         */
        @Deprecated
        public GroupBuilder<T> add(boolean sure, String field) {
            return this.addField(sure, field);
        }

        public GroupBuilder<T> addField(boolean sure, String field) {
            if (sure) {
                return this.addField(field);
            }
            return this;
        }

        public GroupBuilder<T> addColumn(String column) {
            this.groupBy.getItems().add(new ColumnGroupItem(this.table, column));
            return this;
        }

        public GroupBuilder<T> addColumn(boolean sure, String column) {
            if (sure) {
                return this.addColumn(column);
            }
            return this;
        }

        public T done() {
            return this.target;
        }
    }
}
