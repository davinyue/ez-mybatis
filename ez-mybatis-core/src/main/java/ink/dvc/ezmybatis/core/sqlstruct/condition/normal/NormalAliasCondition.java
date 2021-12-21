package ink.dvc.ezmybatis.core.sqlstruct.condition.normal;

import ink.dvc.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;

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
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return keywordQM + this.alias + keywordQM;
    }
}
