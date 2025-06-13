package org.rdlinux.ezmybatis.enumeration;

public enum Operator {
    eq("="),
    ne("!="),
    gt(">"),
    ge(">="),
    lt("<"),
    le("<="),
    isNull("IS NULL"),
    isNotNull("IS NOT NULL"),
    in("IN"),
    notIn("NOT IN"),
    like("LIKE"),
    unlike("NOT LIKE"),
    between("BETWEEN"),
    notBetween("NOT BETWEEN"),
    regexp("REGEXP");
    private final String operator;

    Operator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }
}
