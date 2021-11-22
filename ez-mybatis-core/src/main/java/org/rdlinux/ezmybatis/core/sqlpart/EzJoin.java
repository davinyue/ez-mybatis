package org.rdlinux.ezmybatis.core.sqlpart;

import org.rdlinux.ezmybatis.core.sqlpart.condition.EzCondition;

import java.util.List;

public class EzJoin {
    /**
     * 主表
     */
    private EzTable table;
    /**
     * 关联类型
     */
    private JoinType joinType;
    /**
     * 被join表
     */
    private EzTable joinTable;
    /**
     * 链表条件
     */
    private List<EzCondition> onConditions;
    /**
     * 关联表
     */
    private EzJoin[] joins;

    public EzTable getTable() {
        return this.table;
    }

    public void setTable(EzTable table) {
        this.table = table;
    }

    public JoinType getJoinType() {
        return this.joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public EzTable getJoinTable() {
        return this.joinTable;
    }

    public void setJoinTable(EzTable joinTable) {
        this.joinTable = joinTable;
    }

    public List<EzCondition> getOnConditions() {
        return this.onConditions;
    }

    public void setOnConditions(List<EzCondition> onConditions) {
        this.onConditions = onConditions;
    }

    public EzJoin[] getJoins() {
        return this.joins;
    }

    public void setJoins(EzJoin[] joins) {
        this.joins = joins;
    }

    /**
     * join类型枚举
     */
    public static enum JoinType {
        LeftJoin, RightJoin, FullJoin, InnerJoin, CrossJoin;
    }
}
