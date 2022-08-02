package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

/**
 * 表实体属性对比条件
 */
@Getter
public class ColumnCompareFieldCondition implements Condition {
    private LogicalOperator logicalOperator;
    private Table leftTable;
    private String leftColumn;
    private Operator operator;
    private EntityTable rightTable;
    private String rightField;

    public ColumnCompareFieldCondition(Table leftTable, String leftColumn, Operator operator,
                                       EntityTable rightTable, String rightField) {
        this.logicalOperator = LogicalOperator.AND;
        this.leftTable = leftTable;
        this.leftColumn = leftColumn;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightField = rightField;
    }

    public ColumnCompareFieldCondition(LogicalOperator logicalOperator, Table leftTable, String leftColumn, Operator operator,
                                       EntityTable rightTable, String rightField) {
        this.logicalOperator = logicalOperator;
        this.leftTable = leftTable;
        this.leftColumn = leftColumn;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightField = rightField;
    }


    @Override
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo oEtInfo = EzEntityClassInfoFactory.forClass(configuration,
                this.getRightTable().getEtType());
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return " " + this.getLeftTable().getAlias() + "." +
                keywordQM +
                this.leftColumn +
                keywordQM +
                " " +
                this.getOperator().getOperator() +
                " " +
                this.getRightTable().getAlias() + "." +
                keywordQM +
                oEtInfo.getFieldInfo(this.getRightField()).getColumnName() +
                keywordQM +
                " ";
    }
}
