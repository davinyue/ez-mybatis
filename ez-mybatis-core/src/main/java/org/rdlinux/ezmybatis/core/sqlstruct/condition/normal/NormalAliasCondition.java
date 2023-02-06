package org.rdlinux.ezmybatis.core.sqlstruct.condition.normal;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

/**
 * 普通别名条件
 */
@Getter
public class NormalAliasCondition extends NormalCondition implements SqlPart {
    private String alias;

    public NormalAliasCondition(LogicalOperator logicalOperator, String alias, Operator operator, Object value) {
        super(logicalOperator, operator, value);
        this.alias = alias;
    }

    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return keywordQM + this.alias + keywordQM;
    }
}
