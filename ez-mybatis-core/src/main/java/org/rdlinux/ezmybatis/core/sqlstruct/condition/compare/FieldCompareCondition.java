package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

/**
 * 表实体属性对比条件
 */
@Getter
public class FieldCompareCondition implements Condition {
    private LogicalOperator logicalOperator;
    private EntityTable leftTable;
    private String leftField;
    private Operator operator;
    private EntityTable rightTable;
    private String rightField;

    public FieldCompareCondition(EntityTable leftTable, String leftField, Operator operator,
                                 EntityTable rightTable, String rightField) {
        this.logicalOperator = LogicalOperator.AND;
        this.leftTable = leftTable;
        this.leftField = leftField;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightField = rightField;
    }

    public FieldCompareCondition(LogicalOperator logicalOperator, EntityTable leftTable, String leftField, Operator operator,
                                 EntityTable rightTable, String rightField) {
        this.logicalOperator = logicalOperator;
        this.leftTable = leftTable;
        this.leftField = leftField;
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
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, this.getLeftTable()
                .getEtType());
        EntityClassInfo oEtInfo = EzEntityClassInfoFactory.forClass(configuration,
                this.getRightTable().getEtType());
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return " " + this.getLeftTable().getAlias() + "." +
                keywordQM +
                etInfo.getFieldInfo(this.getLeftField()).getColumnName() +
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
