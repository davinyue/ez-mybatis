package ink.dvc.ezmybatis.core.sqlstruct.condition;

import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.utils.Assert;
import org.apache.ibatis.session.Configuration;

import java.util.List;

/**
 * 条件分组
 */
public class GroupCondition implements Condition {
    private List<Condition> conditions;
    private LoginSymbol loginSymbol;

    public GroupCondition(List<Condition> conditions, LoginSymbol loginSymbol) {
        Assert.notNull(conditions, "conditions can not be empty");
        Assert.notNull(loginSymbol, "loginSymbol can not be null");
        this.conditions = conditions;
        this.loginSymbol = loginSymbol;
    }

    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
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
                    sql.append(" ").append(condition.getLoginSymbol().name()).append(" ");
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
