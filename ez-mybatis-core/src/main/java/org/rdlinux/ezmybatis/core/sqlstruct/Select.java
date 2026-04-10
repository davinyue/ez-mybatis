package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectAllItem;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectItem;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectOperand;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectTableAllItem;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * SELECT 子句结构节点。
 *
 * <p>该类表示一次查询中的投影部分，负责维护查询项、是否去重以及 SQL Hint 等信息。
 * 它与 {@link EzQuery} 绑定，属于查询 AST 中描述“返回什么”的核心节点。</p>
 */
@Getter
@Setter
public class Select implements SqlStruct {
    /**
     * 当前 SELECT 所属的查询对象。
     */
    private EzQuery<?> query;
    /**
     * 作用于当前 SELECT 的 SQL Hint。
     */
    private SqlHint sqlHint;
    /**
     * 是否生成 DISTINCT 关键字。
     */
    private boolean distinct = false;
    /**
     * 当前 SELECT 中包含的查询项列表。
     */
    private List<SelectItem> selectItems;

    /**
     * 使用查询对象和查询项初始化 SELECT 结构。
     *
     * @param query       所属查询对象
     * @param selectItems 查询项列表
     */
    private Select(EzQuery<?> query, List<SelectItem> selectItems) {
        Assert.notNull(query, "query can not be null");
        this.query = query;
        this.selectItems = selectItems;
    }

    /**
     * 使用回调快速构建一个新的 SELECT 结构。
     *
     * @param query 所属查询对象
     * @param sc    查询项构建回调
     * @return 构建完成的 SELECT 结构
     */
    public static Select build(EzQuery<?> query, Consumer<EzSelectBuilder> sc) {
        EzSelectBuilder builder = new EzSelectBuilder(query);
        sc.accept(builder);
        return builder.build();
    }

    /**
     * 基于已有查询项并结合回调继续构建 SELECT 结构。
     *
     * @param query       所属查询对象
     * @param selectItems 初始查询项列表
     * @param sc          查询项构建回调
     * @return 构建完成的 SELECT 结构
     */
    public static Select build(EzQuery<?> query, List<SelectItem> selectItems, Consumer<EzSelectBuilder> sc) {
        EzSelectBuilder builder = new EzSelectBuilder(query, selectItems);
        sc.accept(builder);
        return builder.build();
    }

    /**
     * {@link Select} 构造器。
     */
    public static class EzSelectBuilder {
        /**
         * 当前 SELECT 绑定的查询项列表。
         */
        private final List<SelectItem> selectItems;
        /**
         * 当前构建中的 SELECT 结构。
         */
        private final Select select;
        /**
         * 当前 SELECT 所属查询对象。
         */
        private final EzQuery<?> query;

        /**
         * 使用查询对象和已有查询项初始化构造器。
         *
         * @param query       所属查询对象
         * @param selectItems 查询项列表
         */
        private EzSelectBuilder(EzQuery<?> query, List<SelectItem> selectItems) {
            this.select = new Select(query, selectItems);
            this.selectItems = selectItems;
            this.query = query;
        }

        /**
         * 使用查询对象创建空的查询列构造器。
         *
         * @param query 所属查询对象
         */
        private EzSelectBuilder(EzQuery<?> query) {
            this.select = new Select(query, new ArrayList<>());
            this.selectItems = this.select.getSelectItems();
            this.query = query;
        }

        /**
         * 开启查询去重 (DISTINCT)
         */
        public EzSelectBuilder distinct() {
            this.select.distinct = true;
            return this;
        }

        /**
         * 取消查询去重
         */
        public EzSelectBuilder notDistinct() {
            this.select.distinct = false;
            return this;
        }

        /**
         * 添加所有表的所有列查询项 (如 SELECT *)
         */
        public EzSelectBuilder addAllTable() {
            this.selectItems.add(new SelectAllItem());
            return this;
        }

        /**
         * 根据条件添加所有表的所有列查询项
         *
         * @param sure 是否满足条件，为 true 才会添加
         */
        public EzSelectBuilder addAllTable(boolean sure) {
            if (sure) {
                return this.addAllTable();
            }
            return this;
        }


        /**
         * 添加指定表的所有属性查询项，并支持排除个别属性
         *
         * @param table        指定表
         * @param sure         是否满足条件，为 true 才会添加
         * @param excludeField 仅在指定表为 EntityTable 时受支持
         */
        public EzSelectBuilder addAll(boolean sure, Table table, String... excludeField) {
            if (sure) {
                this.selectItems.add(new SelectTableAllItem(table, excludeField));
            }
            return this;
        }

        /**
         * 添加指定表的所有属性查询项，并支持排除个别属性
         *
         * @param table        指定表
         * @param excludeField 仅在指定表为 EntityTable 时受支持
         */
        public EzSelectBuilder addAll(Table table, String... excludeField) {
            return this.addAll(Boolean.TRUE, table, excludeField);
        }

        /**
         * 根据条件添加当前实体表的所有属性查询项
         *
         * @param sure         是否满足条件，为 true 才会添加
         * @param excludeField 需要排除的实体属性名列表, 仅在查询主表为 EntityTable 时受支持
         */
        public EzSelectBuilder addAll(boolean sure, String... excludeField) {
            if (sure) {
                this.selectItems.add(new SelectTableAllItem(this.query.getFrom().getTable(), excludeField));
            }
            return this;
        }

        /**
         * 添加当前实体表的所有属性查询项，并支持排除个别属性
         *
         * @param excludeField 需要排除的实体属性名列表, 仅在查询主表为 EntityTable 时受支持
         */
        public EzSelectBuilder addAll(String... excludeField) {
            return this.addAll(Boolean.TRUE, excludeField);
        }

        /**
         * 根据条件添加具有返回结果能力的底层抽象节点作为查询项（如 Function, Formula 等类型），并可以设置别名
         *
         * @param sure    是否添加
         * @param operand 具备结果返回特征的操作数节点
         * @param alias   别名
         */
        public EzSelectBuilder add(boolean sure, QueryRetOperand operand, String alias) {
            if (sure) {
                this.selectItems.add(new SelectOperand(operand, alias));
            }
            return this;
        }

        /**
         * 根据条件添加具有返回结果能力的底层抽象节点作为查询项
         *
         * @param sure    是否添加
         * @param operand 具备结果返回特征的操作数节点
         */
        public EzSelectBuilder add(boolean sure, QueryRetOperand operand) {
            return this.add(sure, operand, null);
        }

        /**
         * 添加具有返回结果能力的底层抽象节点作为查询项，并指定别名
         *
         * @param operand 具备结果返回特征的操作数节点
         * @param alias   别名
         */
        public EzSelectBuilder add(QueryRetOperand operand, String alias) {
            return this.add(true, operand, alias);
        }

        /**
         * 添加具有返回结果能力的底层抽象节点作为查询项
         *
         * @param operand 具备结果返回特征的操作数节点
         */
        public EzSelectBuilder add(QueryRetOperand operand) {
            return this.add(true, operand, null);
        }

        /**
         * 根据条件添加最外层的查询包装项
         *
         * @param sure    是否添加
         * @param operand 已被包装的查询项
         */
        public EzSelectBuilder add(boolean sure, SelectOperand operand) {
            if (sure) {
                this.selectItems.add(operand);
            }
            return this;
        }

        /**
         * 添加最外层的查询包装项
         *
         * @param operand 已被包装的查询项
         */
        public EzSelectBuilder add(SelectOperand operand) {
            this.selectItems.add(operand);
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
        public EzSelectBuilder add(boolean sure, Object operand, String alias) {
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
        public EzSelectBuilder add(boolean sure, Object operand) {
            return this.add(sure, operand, null);
        }

        /**
         * 添加通用的对象节点作为查询项，并指定别名<br>
         * 常量会自动被转换为普通实值结构节点参与执行
         *
         * @param operand 任意目标对象
         * @param alias   别名
         */
        public EzSelectBuilder add(Object operand, String alias) {
            return this.add(true, operand, alias);
        }

        /**
         * 添加通用的对象节点作为查询项
         *
         * @param operand 任意目标对象
         */
        public EzSelectBuilder add(Object operand) {
            return this.add(true, operand, null);
        }

        /**
         * 指定sql提示
         *
         * @param hint Hint 原文
         * @return 当前构造器
         */
        public EzSelectBuilder withHint(String hint) {
            return this.withHint(true, hint);
        }

        /**
         * 按条件指定 SQL Hint。
         *
         * @param sure 是否启用当前 Hint
         * @param hint Hint 原文
         * @return 当前构造器
         */
        public EzSelectBuilder withHint(boolean sure, String hint) {
            if (sure && StringUtils.isNotEmpty(hint)) {
                this.select.sqlHint = new SqlHint(hint);
            }
            return this;
        }

        /**
         * 完成构建并返回 SELECT 结构。
         *
         * @return 构建完成的 SELECT 结构
         */
        private Select build() {
            return this.select;
        }
    }
}
