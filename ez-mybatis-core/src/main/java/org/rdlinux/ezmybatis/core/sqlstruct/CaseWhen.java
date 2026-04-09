package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * CASE WHEN 条件表达式，用于在 SQL 中构建条件分支逻辑。
 * <p>
 * 与 Function / Formula 一样，CaseWhen 不绑定任何 Table，是纯粹的无状态结构节点。
 * 构建条件时需要显式传入 Table 参数。
 * </p>
 */
@Getter
@Setter
public class CaseWhen implements QueryRetNeedAlias {
    /**
     * CaseWhen条件数据
     */
    private List<CaseWhenData> caseWhenData;
    /**
     * CaseWhen条件else数据
     */
    private CaseWhenData els;

    private CaseWhen() {
    }

    /**
     * 通过闭包 Lambda 直接构建出 CaseWhen 结构
     */
    public static CaseWhen build(Consumer<CaseWhenBuilder> consumer) {
        CaseWhenBuilder builder = new CaseWhenBuilder();
        consumer.accept(builder);
        return builder.build();
    }


    /**
     * CaseWhen条件数据（单个 WHEN ... THEN 分支）
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class CaseWhenData {
        /**
         * 条件
         */
        private List<Condition> conditions;
        /**
         * 值
         */
        private Operand value;

        public CaseWhenData setValue(Operand value) {
            if (value == null) {
                value = ObjArg.of(null);
            }
            this.value = value;
            return this;
        }

        /**
         * CaseWhen 条件数据构造器（对应单个 WHEN ... THEN 分支）
         * <p>
         * 继承自 ConditionBuilder，但不绑定任何默认 Table。
         * 使用 addFieldCondition 等方法时需要显式传入 Table 参数。
         * </p>
         */
        public static class CaseWhenDataBuilder extends ConditionBuilder<CaseWhenDataBuilder> {
            private final CaseWhenData caseWhenData;
            private final CaseWhenBuilder caseWhenBuilder;

            public CaseWhenDataBuilder(CaseWhenBuilder caseWhenBuilder, CaseWhenData caseWhenData) {
                super(caseWhenData.getConditions());
                this.caseWhenBuilder = caseWhenBuilder;
                this.caseWhenData = caseWhenData;
            }

            /**
             * 当条件匹配时返回的通用对象值
             * <p>
             * 支持自动包装为对应的 Operand 结构节点（如常量、Function、Formula、CaseWhen 等）
             * </p>
             *
             * @param value 返回的通用对象
             * @return 上级 CaseWhenBuilder 构造器
             */
            private CaseWhenBuilder then(Object value) {
                this.caseWhenData.setValue(Operand.objToOperand(value));
                return this.caseWhenBuilder;
            }

            /**
             * 当条件匹配时返回的标准结构化 Operand 节点
             *
             * @param value 结果操作数抽象节点
             * @return 上级 CaseWhenBuilder 构造器
             */
            private CaseWhenBuilder then(Operand value) {
                this.caseWhenData.setValue(value);
                return this.caseWhenBuilder;
            }
        }

    }

    /**
     * CaseWhen 构造器
     * <p>
     * 无状态设计，不绑定任何 Table。条件构建时需通过显式传参指定表。
     * </p>
     */
    public static class CaseWhenBuilder {
        protected CaseWhen caseWhen;


        private CaseWhenBuilder() {
            this.caseWhen = new CaseWhen();
        }

        /**
         * 开启一个 WHEN 条件分支（开始拼接 WHEN ... THEN）
         *
         * @return 条件数据构造器
         */
        private CaseWhenData.CaseWhenDataBuilder when() {
            if (this.caseWhen.getCaseWhenData() == null) {
                this.caseWhen.setCaseWhenData(new LinkedList<>());
            }
            CaseWhenData caseWhenData = new CaseWhenData();
            caseWhenData.setConditions(new LinkedList<>());
            this.caseWhen.getCaseWhenData().add(caseWhenData);
            return new CaseWhenData.CaseWhenDataBuilder(this, caseWhenData);
        }

        /**
         * 闭包模式的 WHEN 条件流以及配套的 THEN 返回执行值
         * 简化形如 `.when().condition(...).then(val)` 的冗长调用
         */
        public CaseWhenBuilder when(Consumer<CaseWhenData.CaseWhenDataBuilder> consumer, Object thenValue) {
            CaseWhenData.CaseWhenDataBuilder whenBuilder = this.when();
            consumer.accept(whenBuilder);
            return whenBuilder.then(thenValue);
        }

        /**
         * 闭包模式的 WHEN 条件流以及配套的标准操作数 THEN 返回执行值
         */
        public CaseWhenBuilder when(Consumer<CaseWhenData.CaseWhenDataBuilder> consumer, Operand thenValue) {
            CaseWhenData.CaseWhenDataBuilder whenBuilder = this.when();
            consumer.accept(whenBuilder);
            return whenBuilder.then(thenValue);
        }

        /**
         * 默认的 ELSE 分支，传入通用对象（支持自动包装为 Operand，构建结束）
         *
         * @param value 通用包装对象或常量值
         * @return 最终构建的 CaseWhen 结构
         */
        public CaseWhen els(Object value) {
            this.caseWhen.setEls(new CaseWhenData().setValue(Operand.objToOperand(value)));
            return this.caseWhen;
        }

        /**
         * 默认的 ELSE 分支，传入标准的结构化 Operand 节点（构建结束）
         *
         * @param value 结果操作数抽象节点
         * @return 最终构建的 CaseWhen 结构
         */
        public CaseWhen els(Operand value) {
            this.caseWhen.setEls(new CaseWhenData().setValue(value));
            return this.caseWhen;
        }

        /**
         * 完成 CaseWhen 的构造（没有 ELSE 分支时调用）
         *
         * @return 最终构建的 CaseWhen 结构
         */
        private CaseWhen build() {
            return this.caseWhen;
        }
    }
}
