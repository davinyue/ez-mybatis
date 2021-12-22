package ink.dvc.ezmybatis.core.sqlstruct;

import ink.dvc.ezmybatis.core.EzParam;
import ink.dvc.ezmybatis.core.EzQuery;
import ink.dvc.ezmybatis.core.content.EzEntityClassInfoFactory;
import ink.dvc.ezmybatis.core.content.entityinfo.EntityClassInfo;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.constant.DbType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GroupBy implements SqlStruct {
    private static final Map<DbType, SqlStruct> CONVERT = new HashMap<>();

    static {
        SqlStruct defaultConvert = (sqlBuilder, configuration, ezParam, mybatisParamHolder) ->
                GroupBy.defaultGroupByToSql(sqlBuilder, configuration, (EzQuery<?>) ezParam, mybatisParamHolder);
        CONVERT.put(DbType.MYSQL, defaultConvert);
        CONVERT.put(DbType.ORACLE, defaultConvert);
    }

    private List<GroupItem> items;

    public GroupBy(List<GroupItem> items) {
        this.items = items;
    }

    private static StringBuilder defaultGroupByToSql(StringBuilder sqlBuilder, Configuration configuration,
                                                     EzQuery<?> query, MybatisParamHolder mybatisParamHolder) {
        GroupBy group = query.getGroupBy();
        if (group == null || group.getItems() == null) {
            return sqlBuilder;
        } else {
            StringBuilder sql = new StringBuilder(" GROUP BY ");
            for (int i = 0; i < group.getItems().size(); i++) {
                GroupBy.GroupItem groupItem = group.getItems().get(i);
                EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                        groupItem.getTable().getEtType());
                sql.append(groupItem.getTable().getAlias()).append(".")
                        .append(entityClassInfo.getFieldInfo(groupItem.getField()).getColumnName());
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


    public static class GroupItem {
        private EntityTable table;
        private String field;

        public GroupItem(EntityTable table, String field) {
            this.table = table;
            this.field = field;
        }

        public EntityTable getTable() {
            return this.table;
        }

        public String getField() {
            return this.field;
        }
    }

    public static class GroupBuilder<T> {
        private T target;
        private EntityTable table;
        private GroupBy groupBy;

        public GroupBuilder(T target, GroupBy groupBy, EntityTable table) {
            this.target = target;
            this.groupBy = groupBy;
            this.table = table;
        }

        public GroupBuilder<T> add(String field) {
            this.groupBy.getItems().add(new GroupBy.GroupItem(this.table, field));
            return this;
        }

        public GroupBuilder<T> add(boolean sure, String field) {
            if (sure) {
                this.add(field);
            }
            return this;
        }

        public GroupBuilder<T> add(EntityTable table, String field) {
            this.groupBy.getItems().add(new GroupBy.GroupItem(table, field));
            return this;
        }

        public GroupBuilder<T> add(boolean sure, EntityTable table, String field) {
            if (sure) {
                this.add(table, field);
            }
            return this;
        }

        public T done() {
            return this.target;
        }
    }
}