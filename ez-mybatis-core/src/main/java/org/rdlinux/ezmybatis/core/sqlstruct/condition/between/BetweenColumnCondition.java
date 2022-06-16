package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

/**
 * between 条件
 */
public class BetweenColumnCondition extends BetweenCondition {
    @Getter
    @Setter
    protected Table table;
    @Getter
    @Setter
    protected String column;

    public BetweenColumnCondition(LogicalOperator logicalOperator, Table table, String column,
                                  Object minValue, Object maxValue) {
        super(logicalOperator, minValue, maxValue);
        this.table = table;
        this.column = column;
    }

    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return this.getTable().getAlias() + "." + keywordQM + this.column + keywordQM;
    }
}
