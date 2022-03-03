package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

/**
 * 是空条件
 */
@Getter
@Setter
public class IsNullAliasCondition extends IsNullCondition {
    protected String alias;

    public IsNullAliasCondition(LogicalOperator logicalOperator, String alias) {
        this.alias = alias;
        this.logicalOperator = logicalOperator;
    }


    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return " " + keywordQM + this.alias + keywordQM + " ";
    }

    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
}
