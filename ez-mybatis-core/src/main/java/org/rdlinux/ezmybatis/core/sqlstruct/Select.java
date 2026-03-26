package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectAllItem;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectItem;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectOperand;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectTableAllItem;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.LinkedList;
import java.util.List;

/**
 * 查询结构体
 */
@Getter
@Setter
public class Select implements SqlStruct {
    /**
     * 查询
     */
    private EzQuery<?> query;
    /**
     * sql提示
     */
    private SqlHint sqlHint;
    /**
     * 是否去重
     */
    private boolean distinct = false;
    /**
     * 查询项
     */
    private List<SelectItem> selectFields;

    public Select(EzQuery<?> query, List<SelectItem> selectFields) {
        Assert.notNull(query, "query can not be null");
        this.query = query;
        this.selectFields = selectFields;
    }

    /**
     * 查询构造器
     */
    public static class EzSelectBuilder<T> {
        private final List<SelectItem> selectFields;
        private final T target;
        private final Table table;
        private final Select select;

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
         * 开启查询去重 (DISTINCT)
         */
        public EzSelectBuilder<T> distinct() {
            this.select.distinct = true;
            return this;
        }

        /**
         * 取消查询去重
         */
        public EzSelectBuilder<T> notDistinct() {
            this.select.distinct = false;
            return this;
        }

        /**
         * 添加所有表的所有列查询项 (如 SELECT *)
         */
        public EzSelectBuilder<T> addAllTable() {
            this.selectFields.add(new SelectAllItem());
            return this;
        }

        /**
         * 根据条件添加所有表的所有列查询项
         *
         * @param sure 是否满足条件，为 true 才会添加
         */
        public EzSelectBuilder<T> addAllTable(boolean sure) {
            if (sure) {
                return this.addAllTable();
            }
            return this;
        }

        /**
         * 添加当前实体表的所有属性查询项，并支持排除个别属性
         * 仅在查询主表为 EntityTable 时受支持
         *
         * @param excludeField 需要排除的实体属性名列表
         */
        public EzSelectBuilder<T> addAll(String... excludeField) {
            this.selectFields.add(new SelectTableAllItem(this.table, excludeField));
            return this;
        }

        /**
         * 根据条件添加当前实体表的所有属性查询项
         *
         * @param sure         是否满足条件，为 true 才会添加
         * @param excludeField 需要排除的实体属性名列表
         */
        public EzSelectBuilder<T> addAll(boolean sure, String... excludeField) {
            if (sure) {
                return this.addAll(excludeField);
            }
            return this;
        }

        /**
         * 添加当前实体表的单个属性查询项（保留用作语法糖）
         *
         * @param field 实体类属性名称
         */
        public EzSelectBuilder<T> addField(String field) {
            this.checkEntityTable();
            this.selectFields.add(new SelectOperand(EntityField.of((EntityTable) this.table, field), null));
            return this;
        }

        /**
         * 根据条件添加单个属性查询项
         *
         * @param sure  是否添加
         * @param field 实体类属性名称
         */
        public EzSelectBuilder<T> addField(boolean sure, String field) {
            if (sure) {
                return this.addField(field);
            }
            return this;
        }

        /**
         * 添加单个属性查询项，并指定别名
         *
         * @param field 实体类属性名称
         * @param alias 别名
         */
        public EzSelectBuilder<T> addField(String field, String alias) {
            this.checkEntityTable();
            this.selectFields.add(new SelectOperand(EntityField.of((EntityTable) this.table, field), alias));
            return this;
        }

        /**
         * 根据条件添加单个属性查询项并指定别名
         *
         * @param sure  是否添加
         * @param field 实体类属性名称
         * @param alias 别名
         */
        public EzSelectBuilder<T> addField(boolean sure, String field, String alias) {
            if (sure) {
                return this.addField(field, alias);
            }
            return this;
        }

        /**
         * 根据条件添加具有返回结果能力的底层抽象节点作为查询项（如 Function, Formula 等类型），并可以设置别名
         *
         * @param sure    是否添加
         * @param operand 具备结果返回特征的操作数节点
         * @param alias   别名
         */
        public EzSelectBuilder<T> add(boolean sure, QueryRetOperand operand, String alias) {
            if (sure) {
                this.selectFields.add(new SelectOperand(operand, alias));
            }
            return this;
        }

        /**
         * 根据条件添加具有返回结果能力的底层抽象节点作为查询项
         *
         * @param sure    是否添加
         * @param operand 具备结果返回特征的操作数节点
         */
        public EzSelectBuilder<T> add(boolean sure, QueryRetOperand operand) {
            return this.add(sure, operand, null);
        }

        /**
         * 添加具有返回结果能力的底层抽象节点作为查询项，并指定别名
         *
         * @param operand 具备结果返回特征的操作数节点
         * @param alias   别名
         */
        public EzSelectBuilder<T> add(QueryRetOperand operand, String alias) {
            return this.add(true, operand, alias);
        }

        /**
         * 添加具有返回结果能力的底层抽象节点作为查询项
         *
         * @param operand 具备结果返回特征的操作数节点
         */
        public EzSelectBuilder<T> add(QueryRetOperand operand) {
            return this.add(true, operand, null);
        }

        /**
         * 根据条件添加最外层的查询包装项
         *
         * @param sure    是否添加
         * @param operand 已被包装的查询项
         */
        public EzSelectBuilder<T> add(boolean sure, SelectOperand operand) {
            if (sure) {
                this.selectFields.add(operand);
            }
            return this;
        }

        /**
         * 添加最外层的查询包装项
         *
         * @param operand 已被包装的查询项
         */
        public EzSelectBuilder<T> add(SelectOperand operand) {
            this.selectFields.add(operand);
            return this;
        }

        /**
         * 根据条件添加通用的对象节点作为查询项，并指定别名<br>
         * 常量或是普通的包装类会自动被转换为合适的 SqlStruct 结构节点
         *
         * @param sure    是否添加
         * @param operand 任意目标对象（支持如 String 等常量类型或高级实现类）
         * @param alias   别名
         */
        public EzSelectBuilder<T> add(boolean sure, Object operand, String alias) {
            if (operand instanceof QueryRetOperand) {
                return this.add(sure, (QueryRetOperand) operand, alias);
            }
            return this.add(sure, (QueryRetOperand) Operand.objToOperand(operand), alias);
        }

        /**
         * 根据条件添加通用的对象节点作为查询项
         *
         * @param sure    是否添加
         * @param operand 任意目标对象（支持如 String 等常量类型）
         */
        public EzSelectBuilder<T> add(boolean sure, Object operand) {
            return this.add(sure, operand, null);
        }

        /**
         * 添加通用的对象节点作为查询项，并指定别名<br>
         * 常量会自动被转换为普通实值结构节点参与执行
         *
         * @param operand 任意目标对象
         * @param alias   别名
         */
        public EzSelectBuilder<T> add(Object operand, String alias) {
            return this.add(true, operand, alias);
        }

        /**
         * 添加通用的对象节点作为查询项
         *
         * @param operand 任意目标对象
         */
        public EzSelectBuilder<T> add(Object operand) {
            return this.add(true, operand, null);
        }

        /**
         * 指定sql提示
         */
        public EzSelectBuilder<T> withHint(String hint) {
            return this.withHint(true, hint);
        }

        /**
         * 指定sql提示
         */
        public EzSelectBuilder<T> withHint(boolean sure, String hint) {
            if (sure && StringUtils.isNotEmpty(hint)) {
                this.select.sqlHint = new SqlHint(hint);
            }
            return this;
        }
    }
}
