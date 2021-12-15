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
                Select.defaultQueryToSqlPart(sqlBuilder, configuration, (EzQuery) ezParam, mybatisParamHolder);
        CONVERT.put(DbType.MYSQL, defaultConvert);
        CONVERT.put(DbType.ORACLE, defaultConvert);
    }

    private List<SelectItem> selectFields;

    public Select(List<SelectItem> selectFields) {
        this.selectFields = selectFields;
    }

    private static StringBuilder defaultQueryToSqlPart(StringBuilder sqlBuilder, Configuration configuration,
                                                       EzQuery ezParam, MybatisParamHolder mybatisParamHolder) {
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
    public StringBuilder toSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzParam ezParam,
                                   MybatisParamHolder mybatisParamHolder) {
        return CONVERT.get(DbTypeUtils.getDbType(configuration)).toSqlPart(sqlBuilder, configuration, ezParam,
                mybatisParamHolder);
    }

    public static class EzSelectBuilder<T> {
        private List<SelectItem> selectFields;
        private T target;
        private EntityTable table;

        public EzSelectBuilder(T target, Select select, EntityTable table) {
            if (select.getSelectFields() == null) {
                select.setSelectFields(new LinkedList<>());
            }
            this.selectFields = select.getSelectFields();
            this.target = target;
            this.table = table;
        }

        public T done() {
            return this.target;
        }

        public EzSelectBuilder<T> addAll(Table table) {
            this.selectFields.add(new SelectAllItem(table));
            return this;
        }

        public EzSelectBuilder<T> addAll() {
            return this.addAll(this.table);
        }


        public EzSelectBuilder<T> add(String field) {
            this.selectFields.add(new SelectField(this.table, field));
            return this;
        }

        public EzSelectBuilder<T> add(EntityTable table, String field) {
            this.selectFields.add(new SelectField(table, field));
            return this;
        }

        public EzSelectBuilder<T> add(String field, String alias) {
            this.selectFields.add(new SelectField(this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> add(EntityTable table, String field, String alias) {
            this.selectFields.add(new SelectField(table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumn(String column) {
            this.selectFields.add(new SelectColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumn(Table table, String column) {
            this.selectFields.add(new SelectColumn(table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumn(String column, String alias) {
            this.selectFields.add(new SelectColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumn(Table table, String column, String alias) {
            this.selectFields.add(new SelectColumn(table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addMax(String field) {
            this.selectFields.add(new SelectMaxField(this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addMax(EntityTable table, String field) {
            this.selectFields.add(new SelectMaxField(table, field));
            return this;
        }

        public EzSelectBuilder<T> addMax(String field, String alias) {
            this.selectFields.add(new SelectMaxField(this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addMax(EntityTable table, String field, String alias) {
            this.selectFields.add(new SelectMaxField(table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(String column) {
            this.selectFields.add(new SelectMaxColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(Table table, String column) {
            this.selectFields.add(new SelectMaxColumn(table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(String column, String alias) {
            this.selectFields.add(new SelectMaxColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnMax(Table table, String column, String alias) {
            this.selectFields.add(new SelectMaxColumn(table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addCount(String field) {
            this.selectFields.add(new SelectCountField(this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addCount(EntityTable table, String field) {
            this.selectFields.add(new SelectCountField(table, field));
            return this;
        }

        public EzSelectBuilder<T> addCount(String field, String alias) {
            this.selectFields.add(new SelectCountField(this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addCount(EntityTable table, String field, String alias) {
            this.selectFields.add(new SelectCountField(table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(String column) {
            this.selectFields.add(new SelectCountColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(Table table, String column) {
            this.selectFields.add(new SelectCountColumn(table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(String column, String alias) {
            this.selectFields.add(new SelectCountColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnCount(Table table, String column, String alias) {
            this.selectFields.add(new SelectCountColumn(table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addMin(String field) {
            this.selectFields.add(new SelectMaxField(this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addMin(EntityTable table, String field) {
            this.selectFields.add(new SelectMaxField(table, field));
            return this;
        }

        public EzSelectBuilder<T> addMin(String field, String alias) {
            this.selectFields.add(new SelectMaxField(this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addMin(EntityTable table, String field, String alias) {
            this.selectFields.add(new SelectMaxField(table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(String column) {
            this.selectFields.add(new SelectMinColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(Table table, String column) {
            this.selectFields.add(new SelectMinColumn(table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(String column, String alias) {
            this.selectFields.add(new SelectMinColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnMin(Table table, String column, String alias) {
            this.selectFields.add(new SelectMinColumn(table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addAvg(String field) {
            this.selectFields.add(new SelectAvgField(this.table, field));
            return this;
        }

        public EzSelectBuilder<T> addAvg(EntityTable table, String field) {
            this.selectFields.add(new SelectAvgField(table, field));
            return this;
        }

        public EzSelectBuilder<T> addAvg(String field, String alias) {
            this.selectFields.add(new SelectAvgField(this.table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addAvg(EntityTable table, String field, String alias) {
            this.selectFields.add(new SelectAvgField(table, field, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(String column) {
            this.selectFields.add(new SelectAvgColumn(this.table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(Table table, String column) {
            this.selectFields.add(new SelectAvgColumn(table, column));
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(String column, String alias) {
            this.selectFields.add(new SelectAvgColumn(this.table, column, alias));
            return this;
        }

        public EzSelectBuilder<T> addColumnAvg(Table table, String column, String alias) {
            this.selectFields.add(new SelectAvgColumn(table, column, alias));
            return this;
        }
    }
}
