package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.LinkedList;
import java.util.List;

/**
 * CaseWhen条件
 */
@Getter
@Setter
public class CaseWhen implements SqlStruct {
    /**
     * 表
     */
    protected Table table;
    /**
     * CaseWhen条件数据
     */
    protected List<CaseWhenData> caseWhenData;
    /**
     * CaseWhen条件else数据
     */
    protected CaseWhenElse caseWhenElse;

    private CaseWhen(Table table) {
        this.table = table;
    }

    /**
     * 获取构造器
     */
    public static CaseWhenBuilder builder(Table table) {
        return new CaseWhenBuilder(table);
    }

    /**
     * CaseWhen条件数据
     */
    @Getter
    @Setter
    public static class CaseWhenData {
        /**
         * 条件
         */
        private List<Condition> conditions;
        /**
         * 值
         */
        private Object value;

        /**
         * CaseWhen条件数据构造器
         */
        public static class CaseWhenDataBuilder extends ConditionBuilder<CaseWhenBuilder, CaseWhenDataBuilder> {
            private CaseWhenData caseWhenData;

            public CaseWhenDataBuilder(CaseWhenBuilder caseWhenBuilder, CaseWhenData caseWhenData) {
                super(caseWhenBuilder, caseWhenData.getConditions(), caseWhenBuilder.getTable(),
                        caseWhenBuilder.getTable());
                this.sonBuilder = this;
                this.caseWhenData = caseWhenData;
            }

            /**
             * 条件匹配时的值
             */
            public CaseWhenBuilder then(Object value) {
                this.caseWhenData.setValue(value);
                return this.parentBuilder;
            }
        }

    }

    /**
     * CaseWhen条件else数据
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class CaseWhenElse {
        /**
         * 值
         */
        private Object value;
    }

    /**
     * CaseWhen构造器
     */
    @Getter
    public static class CaseWhenBuilder {
        protected Table table;
        protected CaseWhen caseWhen;

        private CaseWhenBuilder(Table table) {
            this.table = table;
            this.caseWhen = new CaseWhen(table);
        }

        /**
         * 添加case when条件
         */
        public CaseWhenData.CaseWhenDataBuilder when() {
            if (this.caseWhen.getCaseWhenData() == null) {
                this.caseWhen.setCaseWhenData(new LinkedList<>());
            }
            CaseWhenData caseWhenData = new CaseWhenData();
            caseWhenData.setConditions(new LinkedList<>());
            this.caseWhen.getCaseWhenData().add(caseWhenData);
            return new CaseWhenData.CaseWhenDataBuilder(this, caseWhenData);
        }

        /**
         * else, else将会构造结束
         */
        public CaseWhen els(Object value) {
            this.caseWhen.setCaseWhenElse(new CaseWhenElse(value));
            return this.caseWhen;
        }

        /**
         * 构造结束, 同build
         */
        public CaseWhen done() {
            return this.caseWhen;
        }

        /**
         * 构造结束, 同done
         */
        public CaseWhen build() {
            return this.caseWhen;
        }
    }
}
