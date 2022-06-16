package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;

/**
 * between 条件
 */
public class BetweenAliasCondition extends BetweenCondition {
    @Getter
    @Setter
    protected String alias;

    public BetweenAliasCondition(LogicalOperator logicalOperator, String alias, Object minValue, Object maxValue) {
        super(logicalOperator, minValue, maxValue);
        this.alias = alias;
    }

    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return " " + keywordQM + this.alias + keywordQM + " ";
    }
}
