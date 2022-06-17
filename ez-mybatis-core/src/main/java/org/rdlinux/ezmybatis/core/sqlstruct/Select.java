package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzParam;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

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

    /**
     * 是否去重
     */
    private boolean distinct = false;
    /**
     * 查询项
     */
    private List<SelectItem> selectFields;

    public Select(List<SelectItem> selectFields) {
        this.selectFields = selectFields;
    }

    private static StringBuilder defaultQueryToSqlPart(StringBuilder sqlBuilder, Configuration configuration,
                                                       EzQuery<?> ezParam) {
        From from = ezParam.getFrom();
        Select select = ezParam.getSelect();
        sqlBuilder.append("SELECT ");
        if (select != null && select.distinct) {
            sqlBuilder.append("DISTINCT ");
        }
        if (select == null || select.getSelectFields() == null || select.getSelectFields().isEmpty()) {
            sqlBuilder.append(from.getTable().getAlias()).append(".* ");
        } else {
            List<SelectItem> selectFields = select.getSelectFields();
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
        private Select select;

        public EzSelectBuilder(T target, Select select, Table table) {
            if (select.getSelectFields() == null) {
                select.setSelectFields(new LinkedList<>());
            }
            this.select = select;
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

        /**
         * 去重
         */
        public EzSelectBuilder<T> distinct() {
            this.select.distinct = true;
            return this;
        }

        /**
         * 不去重
         */
        public EzSelectBuilder<T> notDistinct() {
            this.select.distinct = false;
            return this;
        }

        public EzSelectBuilder<T> addAllTable() {
            this.selectFields.add(new SelectAllItem());
            return this;
        }

        public EzSelectBuilder<T> addAllTable(boolean sure) {
            if (sure) {
                return this.addAllTable();
            }
            return this;
        }

        public EzSelectBuilder<T> addAll() {
            this.selectFields.add(new SelectTableAllItem(this.table));
            return this;
        }

        public EzSelectBuilder<T> addAll(boolean sure) {
            if (sure) {
                return this.addAll();
            }
            return this;
        }

        /**
         * please use {@link #addField(String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> add(String field) {
            return this.addField(field);
        }

        public EzSelectBuilder<T> addField(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectField((EntityTable) this.table, field));
            return this;
        }

        /**
         * please use {@link #addField(boolean, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> add(boolean sure, String field) {
            return this.addField(sure, field);
        }

        public EzSelectBuilder<T> addField(boolean sure, String field) {
            if (sure) {
                return this.addField(field);
            }
            return this;
        }

        /**
         * please use {@link #addField(String, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> add(String field, String alias) {
            return this.addField(field, alias);
        }

        public EzSelectBuilder<T> addField(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectField((EntityTable) this.table, field, alias));
            return this;
        }

        /**
         * please use {@link #addField(boolean, String, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> add(boolean sure, String field, String alias) {
            return this.addField(sure, field, alias);
        }

        public EzSelectBuilder<T> addField(boolean sure, String field, String alias) {
            if (sure) {
                return this.addField(field, alias);
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

        /**
         * please use {@link #addFieldMax(String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addMax(String field) {
            return this.addFieldMax(field);
        }

        public EzSelectBuilder<T> addFieldMax(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectMaxField((EntityTable) this.table, field));
            return this;
        }

        /**
         * please use {@link #addFieldMax(boolean, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addMax(boolean sure, String field) {
            return this.addFieldMax(sure, field);
        }

        public EzSelectBuilder<T> addFieldMax(boolean sure, String field) {
            if (sure) {
                return this.addFieldMax(field);
            }
            return this;
        }

        /**
         * please use {@link #addFieldMax(String, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addMax(String field, String alias) {
            return this.addFieldMax(field, alias);
        }

        public EzSelectBuilder<T> addFieldMax(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectMaxField((EntityTable) this.table, field, alias));
            return this;
        }

        /**
         * please use {@link #addFieldMax(boolean, String, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addMax(boolean sure, String field, String alias) {
            return this.addFieldMax(sure, field, alias);
        }

        public EzSelectBuilder<T> addFieldMax(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldMax(field, alias);
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

        /**
         * please use {@link #addFieldCount(String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addCount(String field) {
            return this.addFieldCount(field);
        }

        public EzSelectBuilder<T> addFieldCount(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectCountField((EntityTable) this.table, field));
            return this;
        }

        /**
         * please use {@link #addFieldCount(boolean, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addCount(boolean sure, String field) {
            return this.addFieldCount(sure, field);
        }

        public EzSelectBuilder<T> addFieldCount(boolean sure, String field) {
            if (sure) {
                return this.addFieldCount(field);
            }
            return this;
        }

        /**
         * please use {@link #addFieldCount(String, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addCount(String field, String alias) {
            return this.addFieldCount(field, alias);
        }

        public EzSelectBuilder<T> addFieldCount(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectCountField((EntityTable) this.table, field, alias));
            return this;
        }

        /**
         * please use {@link #addFieldCount(boolean, String, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addCount(boolean sure, String field, String alias) {
            return this.addFieldCount(sure, field, alias);
        }

        public EzSelectBuilder<T> addFieldCount(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldCount(field, alias);
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

        /**
         * please use {@link #addFieldMin(String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addMin(String field) {
            return this.addFieldMin(field);
        }

        public EzSelectBuilder<T> addFieldMin(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectMaxField((EntityTable) this.table, field));
            return this;
        }

        /**
         * please use {@link #addFieldMin(boolean, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addMin(boolean sure, String field) {
            return this.addFieldMin(sure, field);
        }

        public EzSelectBuilder<T> addFieldMin(boolean sure, String field) {
            if (sure) {
                return this.addFieldMin(field);
            }
            return this;
        }

        /**
         * please use {@link #addFieldMin(String, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addMin(String field, String alias) {
            return this.addFieldMin(field, alias);
        }

        public EzSelectBuilder<T> addFieldMin(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectMaxField((EntityTable) this.table, field, alias));
            return this;
        }

        /**
         * please use {@link #addFieldMin(boolean, String, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addMin(boolean sure, String field, String alias) {
            return this.addFieldMin(sure, field, alias);
        }

        public EzSelectBuilder<T> addFieldMin(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldMin(field, alias);
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

        /**
         * please use {@link #addFieldAvg(String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addAvg(String field) {
            return this.addFieldAvg(field);
        }

        public EzSelectBuilder<T> addFieldAvg(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectAvgField((EntityTable) this.table, field));
            return this;
        }

        /**
         * please use {@link #addFieldAvg(boolean, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addAvg(boolean sure, String field) {
            return this.addFieldAvg(sure, field);
        }

        public EzSelectBuilder<T> addFieldAvg(boolean sure, String field) {
            if (sure) {
                return this.addFieldAvg(field);
            }
            return this;
        }

        /**
         * please use {@link #addFieldAvg(String, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addAvg(String field, String alias) {
            return this.addFieldAvg(field, alias);
        }

        public EzSelectBuilder<T> addFieldAvg(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectAvgField((EntityTable) this.table, field, alias));
            return this;
        }

        /**
         * please use {@link #addFieldAvg(boolean, String, String)} replace
         */
        @Deprecated
        public EzSelectBuilder<T> addAvg(boolean sure, String field, String alias) {
            return this.addFieldAvg(sure, field, alias);
        }

        public EzSelectBuilder<T> addFieldAvg(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldAvg(field, alias);
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

        public EzSelectBuilder<T> addFieldSum(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectSumField((EntityTable) this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addFieldSum(boolean sure, String field, String alias) {
            if (sure) {
                return this.addFieldSum(field, alias);
            }
            return this;
        }

        public EzSelectBuilder<T> addFieldSum(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectSumField((EntityTable) this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addFieldSum(boolean sure, String field) {
            if (sure) {
                return this.addFieldSum(field);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnSum(String column) {
            this.selectFields.add(new SelectSumColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnSum(boolean sure, String column) {
            if (sure) {
                return this.addColumnSum(column);
            }
            return this;
        }

        public EzSelectBuilder<T> addColumnSum(String column, String alias) {
            this.selectFields.add(new SelectSumColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnSum(boolean sure, String column, String alias) {
            if (sure) {
                return this.addColumnSum(column, alias);
            }
            return this;
        }
    }
}
