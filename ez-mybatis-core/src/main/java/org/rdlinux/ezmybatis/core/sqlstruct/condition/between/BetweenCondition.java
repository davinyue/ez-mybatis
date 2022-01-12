package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

/**
 * between 条件
 */
public abstract class BetweenCondition implements Condition {
    @Getter
    protected Operator operator = Operator.between;
    @Getter
    @Setter
    protected LoginSymbol loginSymbol;
    @Getter
    @Setter
    protected Object minValue;
    @Getter
    @Setter
    protected Object maxValue;

    public BetweenCondition(LoginSymbol loginSymbol, Object minValue, Object maxValue) {
        this.loginSymbol = loginSymbol;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    protected abstract String getSqlField(Configuration configuration);

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        if (this.minValue == null || this.maxValue == null) {
            return "";
        }
        return " " + this.getSqlField(configuration) +
                " " + this.getOperator().getOperator() + " " +
                Condition.valueToSqlStruct(configuration, mybatisParamHolder, this.minValue) + " AND " +
                Condition.valueToSqlStruct(configuration, mybatisParamHolder, this.maxValue) + " ";
    }
}
