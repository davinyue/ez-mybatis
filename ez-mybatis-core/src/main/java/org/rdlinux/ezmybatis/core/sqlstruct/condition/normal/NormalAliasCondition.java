package org.rdlinux.ezmybatis.core.sqlstruct.condition.normal;

import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

/**
 * 普通别名条件
 */
@Getter
public class NormalAliasCondition extends NormalCondition {
    private String alias;

    public NormalAliasCondition(LoginSymbol loginSymbol, String alias, Operator operator, Object value) {
        super(loginSymbol, operator, value);
        this.alias = alias;
    }

    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return keywordQM + this.alias + keywordQM;
    }
}
