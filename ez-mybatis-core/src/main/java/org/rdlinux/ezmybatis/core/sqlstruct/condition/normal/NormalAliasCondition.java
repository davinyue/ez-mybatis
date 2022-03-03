package org.rdlinux.ezmybatis.core.sqlstruct.condition.normal;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

/**
 * 普通别名条件
 */
@Getter
public class NormalAliasCondition extends NormalCondition {
    private String alias;

    public NormalAliasCondition(LogicalOperator logicalOperator, String alias, Operator operator, Object value) {
        super(logicalOperator, operator, value);
        this.alias = alias;
    }

    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return keywordQM + this.alias + keywordQM;
    }
}
