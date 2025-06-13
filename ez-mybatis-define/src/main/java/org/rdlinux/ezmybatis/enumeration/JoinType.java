package org.rdlinux.ezmybatis.enumeration;

/**
 * join类型枚举
 */
public enum JoinType {
    LeftJoin(" LEFT JOIN "),
    RightJoin(" RIGHT JOIN "),
    FullJoin(" FULL JOIN "),
    InnerJoin(" INNER JOIN "),
    CrossJoin(", ");
    private final String sqlStruct;

    JoinType(String sqlStruct) {
        this.sqlStruct = sqlStruct;
    }

    public String toSqlStruct() {
        return this.sqlStruct;
    }
}
