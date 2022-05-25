package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

/**
 * 是空条件
 */
public abstract class IsNullCondition implements Condition {
    @Getter
    protected Operator operator = Operator.isNull;
    @Getter
    @Setter
    protected LogicalOperator logicalOperator;

    protected abstract String getSqlField(Configuration configuration);

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        return " " + this.getSqlField(configuration) + " " + this.operator.getOperator() + " ";
    }
}
