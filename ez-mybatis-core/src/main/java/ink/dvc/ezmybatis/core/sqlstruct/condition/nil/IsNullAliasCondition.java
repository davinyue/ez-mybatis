package ink.dvc.ezmybatis.core.sqlstruct.condition.nil;

import ink.dvc.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;

/**
 * 是空条件
 */
@Getter
@Setter
public class IsNullAliasCondition extends IsNullCondition {
    protected String alias;

    public IsNullAliasCondition(LoginSymbol loginSymbol, String alias) {
        this.alias = alias;
        this.loginSymbol = loginSymbol;
    }


    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return " " + keywordQM + this.alias + keywordQM + " ";
    }

    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
    }
}
