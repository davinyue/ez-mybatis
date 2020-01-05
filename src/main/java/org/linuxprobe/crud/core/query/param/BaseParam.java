package org.linuxprobe.crud.core.query.param;

import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Getter
public abstract class BaseParam<T extends Serializable> {
    private Operator operator = Operator.equal;
    private Condition condition = Condition.and;

    public boolean isEmpty() {
        Operator operator = this.getOperator();
        if (Operator.isNull.equals(operator) || Operator.isNotNull.equals(operator)) {
            return false;
        } else if (Operator.between.equals(operator) || Operator.notBetween.equals(operator)) {
            return this.getMinValue() == null || this.getMaxValue() == null;
        } else if (Operator.in.equals(operator) || Operator.notIn.equals(operator)) {
            return this.getMultiValues() == null || this.getMultiValues().isEmpty();
        } else {
            return this.getValue() == null;
        }
    }

    public abstract T getValue();

    public abstract T getMinValue();

    public abstract T getMaxValue();

    /**
     * in or not in用的多值查询
     */
    public abstract List<T> getMultiValues();

    public Class<?> getParamCalss() {
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Class<?> modelClass = null;
        try {
            modelClass = Class.forName(type.getTypeName());
        } catch (ClassNotFoundException ignored) {
        }
        return modelClass;
    }

    public void setOperator(Operator operator) {
        if (operator == null) {
            this.operator = Operator.equal;
        } else {
            this.operator = operator;
        }
    }

    public void setCondition(Condition condition) {
        if (condition == null) {
            this.condition = Condition.and;
        } else {
            this.condition = condition;
        }
    }

    /**
     * 操作符 =,!=,like...
     */
    public static class Operator {
        private Operator(String operator) {
            if ("equal".equals(operator)) {
                this.operator = "=";
            } else if ("unequal".equals(operator)) {
                this.operator = "!=";
            } else if ("more".equals(operator)) {
                this.operator = ">";
            } else if ("less".equals(operator)) {
                this.operator = "<";
            } else if ("moreOrEqual".equals(operator)) {
                this.operator = ">=";
            } else if ("lessOrEqual".equals(operator)) {
                this.operator = "<=";
            } else if ("between".equals(operator)) {
                this.operator = "BETWEEN";
            } else if ("notBetween".equals(operator)) {
                this.operator = "NOT BETWEEN";
            } else if ("in".equals(operator)) {
                this.operator = "IN";
            } else if ("notIn".equals(operator)) {
                this.operator = "NOT IN";
            } else if ("like".equals(operator)) {
                this.operator = "LIKE";
            } else if ("unlike".equals(operator)) {
                this.operator = "NOT LIKE";
            } else if ("isNull".equals(operator)) {
                this.operator = "IS";
            } else if ("isNotNull".equals(operator)) {
                this.operator = "IS NOT";
            } else if ("regexp".equals(operator)) {
                this.operator = "REGEXP";
            } else {
                throw new IllegalArgumentException("can't support operator '" + operator + "'");
            }
        }

        @Getter
        private String operator = "=";
        public static final Operator equal = new Operator("equal");
        public static final Operator unequal = new Operator("unequal");
        public static final Operator more = new Operator("more");
        public static final Operator less = new Operator("less");
        public static final Operator moreOrEqual = new Operator("moreOrEqual");
        public static final Operator lessOrEqual = new Operator("lessOrEqual");
        public static final Operator between = new Operator("between");
        public static final Operator notBetween = new Operator("notBetween");
        public static final Operator in = new Operator("in");
        public static final Operator notIn = new Operator("notIn");
        public static final Operator like = new Operator("like");
        public static final Operator unlike = new Operator("unlike");
        public static final Operator isNull = new Operator("isNull");
        public static final Operator isNotNull = new Operator("isNotNull");
        /**
         * 正则,只有字符串类,时间类支持
         */
        public static final Operator regexp = new Operator("regexp");

        @Override
        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = prime * result + ((this.operator == null) ? 0 : this.operator.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            Operator other = (Operator) obj;
            if (this.operator == null) {
                return other.operator == null;
            } else {
                return this.operator.equals(other.operator);
            }
        }
    }

    /**
     * 条件,and or
     */
    public static class Condition {
        private Condition(String condition) {
            this.condition = condition;
        }

        @Override
        public String toString() {
            return this.condition;
        }

        @Getter
        private String condition;
        public static final Condition and = new Condition("AND");
        public static final Condition or = new Condition("OR");
    }
}
