package org.rdlinux.ezmybatis.core.sqlstruct.condition.normal;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;

/**
 * 普通别名条件
 */
@Getter
public class NormalAliasCondition extends NormalCondition {
    private String alias;

    public NormalAliasCondition(LoginSymbol loginSymbol, String alias, Operator operator, Object value) {
        this.loginSymbol = loginSymbol;
        this.alias = alias;
        this.operator = operator;
        this.value = value;
    }

    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return keywordQM + this.alias + keywordQM;
    }
}
