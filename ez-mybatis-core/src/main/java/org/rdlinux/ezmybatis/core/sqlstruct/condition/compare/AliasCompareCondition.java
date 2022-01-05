package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

/**
 * 别名对比条件
 */
@Getter
public class AliasCompareCondition implements Condition {
    private LoginSymbol loginSymbol;
    private String leftAlias;
    private Operator operator;
    private String rightAlias;

    public AliasCompareCondition(String leftAlias, Operator operator, String rightAlias) {
        this.loginSymbol = LoginSymbol.AND;
        this.leftAlias = leftAlias;
        this.operator = operator;
        this.rightAlias = rightAlias;
    }

    public AliasCompareCondition(LoginSymbol loginSymbol, String leftAlias, Operator operator, String rightAlias) {
        this.loginSymbol = loginSymbol;
        this.leftAlias = leftAlias;
        this.operator = operator;
        this.rightAlias = rightAlias;
    }


    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return " " + keywordQM + this.leftAlias + keywordQM +
                " " +
                this.getOperator().getOperator() +
                " " +
                keywordQM + this.rightAlias + keywordQM +
                " ";
    }
}
