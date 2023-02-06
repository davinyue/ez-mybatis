package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

/**
 * 是空条件
 */
@Getter
@Setter
public class IsNullColumnCondition extends IsNullCondition implements SqlPart {
    protected Table table;
    protected String column;

    public IsNullColumnCondition(LogicalOperator logicalOperator, Table table, String column) {
        this.table = table;
        this.column = column;
        this.logicalOperator = logicalOperator;
    }


    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return this.getTable().getAlias() + "." + keywordQM + this.column + keywordQM;
    }

    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
}
