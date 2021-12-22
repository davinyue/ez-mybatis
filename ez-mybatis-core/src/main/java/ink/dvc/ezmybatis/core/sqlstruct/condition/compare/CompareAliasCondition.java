package ink.dvc.ezmybatis.core.sqlstruct.condition.compare;

import ink.dvc.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Condition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

/**
 * 别名对比条件
 */
@Getter
public class CompareAliasCondition implements Condition {
    private LoginSymbol loginSymbol;
    private String alias;
    private Operator operator;
    private String otherAlias;

    public CompareAliasCondition(String alias, Operator operator, String otherAlias) {
        this.loginSymbol = LoginSymbol.AND;
        this.alias = alias;
        this.operator = operator;
        this.otherAlias = otherAlias;
    }

    public CompareAliasCondition(LoginSymbol loginSymbol, String alias, Operator operator, String otherAlias) {
        this.loginSymbol = loginSymbol;
        this.alias = alias;
        this.operator = operator;
        this.otherAlias = otherAlias;
    }


    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return " " + keywordQM + this.alias + keywordQM +
                " " +
                this.getOperator().getOperator() +
                " " +
                keywordQM + this.otherAlias + keywordQM +
                " ";
    }
}
