package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzParam;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Select implements SqlStruct {
    private static final Map<DbType, SqlStruct> CONVERT = new HashMap<>();

    static {
        SqlStruct defaultConvert = (sqlBuilder, configuration, ezParam, mybatisParamHolder) ->
                Select.defaultQueryToSqlPart(sqlBuilder, configuration, (EzQuery<?>) ezParam);
        CONVERT.put(DbType.MYSQL, defaultConvert);
        CONVERT.put(DbType.ORACLE, defaultConvert);
        CONVERT.put(DbType.DM, defaultConvert);
    }

    private List<SelectItem> selectFields;

    public Select(List<SelectItem> selectFields) {
        this.selectFields = selectFields;
    }

    private static StringBuilder defaultQueryToSqlPart(StringBuilder sqlBuilder, Configuration configuration,
                                                       EzQuery<?> ezParam) {
        From from = ezParam.getFrom();
        Select select = ezParam.getSelect();
        if (select == null || select.getSelectFields() == null || select.getSelectFields().isEmpty()) {
            sqlBuilder.append("SELECT ").append(from.getTable().getAlias()).append(".* ");
        } else {
            List<SelectItem> selectFields = select.getSelectFields();
            sqlBuilder.append("SELECT ");
            for (int i = 0; i < selectFields.size(); i++) {
                sqlBuilder.append(selectFields.get(i).toSqlPart(configuration));
                if (i + 1 < selectFields.size()) {
                    sqlBuilder.append(", ");
                }
            }
        }
        return sqlBuilder;
    }

    @Override
    public StringBuilder toSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzParam<?> ezParam,
                                   MybatisParamHolder mybatisParamHolder) {
        return CONVERT.get(DbTypeUtils.getDbType(configuration)).toSqlPart(sqlBuilder, configuration, ezParam,
                mybatisParamHolder);
    }

    public static class EzSelectBuilder<T> {
        private List<SelectItem> selectFields;
        private T target;
        private Table table;

        public EzSelectBuilder(T target, Select select, Table table) {
            if (select.getSelectFields() == null) {
                select.setSelectFields(new LinkedList<>());
            }
            this.selectFields = select.getSelectFields();
            this.target = target;
            this.table = table;
        }

        private void checkEntityTable() {
            if (!(this.table instanceof EntityTable)) {
                throw new IllegalArgumentException("Only EntityTable is supported");
            }
        }

        public T done() {
            return this.target;
        }

        public EzSelectBuilder<T> addAll() {
            this.selectFields.add(new SelectAllItem(this.table));
            return this;
        }

        public EzSelectBuilder<T> addAll(boolean sure) {
            if (sure) {
                return this.addAll();
            }
            return this;
        }

        public EzSelectBuilder<T> add(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectField((EntityTable) this.table, field));
            return this;
        }

        public EzSelectBuilder<T> add(boolean sure, String field) {
            if (sure) {
                return this.add(field);
            }
            return this;
        }

        public EzSelectBuilder<T> add(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectField((EntityTable) this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> add(boolean sure, String field, String alias) {
            if (sure) {
                return this.add(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumn(String column) {
            this.selectFields.add(new SelectColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumn(boolean sure, String column) {
            if (sure) {
                return this.addColumn(column);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumn(String column, String alias) {
            this.selectFields.add(new SelectColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumn(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumn(column, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addMax(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectMaxField((EntityTable) this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addMax(boolean sure, String field) {
            if (sure) {
                return this.addMax(field);
            }
            return this;
        }

        public EzSelectBuilder<T> addMax(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectMaxField((EntityTable) this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addMax(boolean sure, String field, String alias) {
            if (sure) {
                return this.addMax(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(String column) {
            this.selectFields.add(new SelectMaxColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(boolean sure, String column) {
            if (sure) {
                return this.addColumnMax(column);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(String column, String alias) {
            this.selectFields.add(new SelectMaxColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnMax(column, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addCount(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectCountField((EntityTable) this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addCount(boolean sure, String field) {
            if (sure) {
                return this.addCount(field);
            }
            return this;
        }

        public EzSelectBuilder<T> addCount(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectCountField((EntityTable) this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addCount(boolean sure, String field, String alias) {
            if (sure) {
                return this.addCount(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(String column) {
            this.selectFields.add(new SelectCountColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(boolean sure, String column) {
            if (sure) {
                return this.addColumnCount(column);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(String column, String alias) {
            this.selectFields.add(new SelectCountColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnCount(column, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addMin(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectMaxField((EntityTable) this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addMin(boolean sure, String field) {
            if (sure) {
                return this.addMin(field);
            }
            return this;
        }

        public EzSelectBuilder<T> addMin(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectMaxField((EntityTable) this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addMin(boolean sure, String field, String alias) {
            if (sure) {
                return this.addMin(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(String column) {
            this.selectFields.add(new SelectMinColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(boolean sure, String column) {
            if (sure) {
                return this.addColumnMin(column);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(String column, String alias) {
            this.selectFields.add(new SelectMinColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnMin(column, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addAvg(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectAvgField((EntityTable) this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addAvg(boolean sure, String field) {
            if (sure) {
                return this.addAvg(field);
            }
            return this;
        }

        public EzSelectBuilder<T> addAvg(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectAvgField((EntityTable) this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addAvg(boolean sure, String field, String alias) {
            if (sure) {
                return this.addAvg(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(String column) {
            this.selectFields.add(new SelectAvgColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(boolean sure, String column) {
            if (sure) {
                return this.addColumnAvg(column);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(String column, String alias) {
            this.selectFields.add(new SelectAvgColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnAvg(column, alias);
            }
            return this;
        }
    }
}
