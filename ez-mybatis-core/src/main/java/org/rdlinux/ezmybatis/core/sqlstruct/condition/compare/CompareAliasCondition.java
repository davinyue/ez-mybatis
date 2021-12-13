package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;

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
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return " " + keywordQM + this.alias + keywordQM +
                " " +
                this.getOperator().getOperator() +
                " " +
                keywordQM + this.otherAlias + keywordQM +
                " ";
    }
}
