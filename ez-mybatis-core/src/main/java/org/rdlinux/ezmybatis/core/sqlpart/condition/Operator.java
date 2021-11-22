package org.rdlinux.ezmybatis.core.sqlpart.condition;

public class Operator {
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
    private String operator;

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

    public String getOperator() {
        return this.operator;
    }
}
