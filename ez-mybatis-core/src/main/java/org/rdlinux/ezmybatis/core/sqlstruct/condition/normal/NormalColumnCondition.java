package org.rdlinux.ezmybatis.core.sqlstruct.condition.normal;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

/**
 * 普通列条件
 */
@Getter
public class NormalColumnCondition extends NormalCondition {
    private Table table;
    private String column;

    public NormalColumnCondition(LogicalOperator logicalOperator, Table table, String column, Operator operator,
                                 Object value) {
        super(logicalOperator, operator, value);
        this.table = table;
        this.column = column;
    }

    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return this.getTable().getAlias() + "." + keywordQM + this.column + keywordQM;
    }
}
