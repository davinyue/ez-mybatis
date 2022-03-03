package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;

/**
 * 条件分组
 */
public class GroupCondition implements Condition {
    private List<Condition> conditions;
    private LogicalOperator logicalOperator;

    public GroupCondition(List<Condition> conditions, LogicalOperator logicalOperator) {
        Assert.notNull(conditions, "conditions can not be empty");
        Assert.notNull(logicalOperator, "loginSymbol can not be null");
        this.conditions = conditions;
        this.logicalOperator = logicalOperator;
    }

    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        if (this.conditions == null || this.conditions.isEmpty()) {
            return "";
        } else {
            StringBuilder sql = new StringBuilder("( ");
            for (int i = 0; i < this.conditions.size(); i++) {
                Condition condition = this.conditions.get(i);
                Assert.notNull(condition, "condition can not be null");
                if (i != 0) {
                    sql.append(" ").append(condition.getLogicalOperator().name()).append(" ");
                }
                sql.append(condition.toSqlPart(configuration, mybatisParamHolder));
            }
            sql.append(" )");
            return sql.toString();
        }
    }


    public List<Condition> getConditions() {
        return this.conditions;
    }
}
