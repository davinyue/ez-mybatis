package org.rdlinux.ezmybatis.core.sqlpart.condition;

import org.rdlinux.ezmybatis.core.utils.Assert;

import java.util.List;

/**
 * 条件分组
 */
public class EzGroupCondition implements EzCondition {
    private List<EzCondition> conditions;
    private LoginSymbol loginSymbol;

    public EzGroupCondition(List<EzCondition> conditions, LoginSymbol loginSymbol) {
        Assert.notNull(conditions, "conditions can not be empty");
        Assert.notNull(loginSymbol, "loginSymbol can not be null");
        this.conditions = conditions;
        this.loginSymbol = loginSymbol;
    }

//    @Override
//    public String toSqlPart(Configuration configuration) {
//        StringBuilder builder = new StringBuilder("( ");
//        for (int i = 0; i < this.conditions.size(); i++) {
//            EzCondition condition = this.conditions.get(i);
//            if (i == 0) {
//                builder.append(condition.toSqlPart());
//            } else {
//                builder.append(condition.getLoginSymbol()).append(" ").append(condition.toSqlPart());
//            }
//        }
//        return builder.toString();
//    }

    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
    }

    public List<EzCondition> getConditions() {
        return this.conditions;
    }
}
