package org.rdlinux.ezmybatis.core.sqlstruct.formula;

import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.LinkedList;
import java.util.List;

/**
 * 计算公式
 */
public class Formula {
    /**
     * 表
     */
    private Table table;
    /**
     * 公式要素
     */
    private List<FormulaElement> elements;

    public static FormulaBuilder builder(Table table) {
        return new FormulaBuilder(table);
    }

    public static class FormulaBuilder {
        private Formula formula;

        private FormulaBuilder(Table table) {
            this.formula = new Formula();
            this.formula.table = table;
            this.formula.elements = new LinkedList<>();
        }

        public Formula build() {
            return this.formula;
        }
    }
}
