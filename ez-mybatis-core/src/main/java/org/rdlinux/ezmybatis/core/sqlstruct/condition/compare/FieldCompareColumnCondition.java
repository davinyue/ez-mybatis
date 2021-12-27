package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

/**
 * 表实体属性与表列对比条件
 */
@Getter
public class FieldCompareColumnCondition implements Condition {
    private LoginSymbol loginSymbol;
    private EntityTable leftTable;
    private String leftField;
    private Operator operator;
    private Table rightTable;
    private String rightColumn;

    public FieldCompareColumnCondition(EntityTable leftTable, String leftField, Operator operator,
                                       Table rightTable, String rightColumn) {
        this.loginSymbol = LoginSymbol.AND;
        this.leftTable = leftTable;
        this.leftField = leftField;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightColumn = rightColumn;
    }

    public FieldCompareColumnCondition(LoginSymbol loginSymbol, EntityTable leftTable, String leftField, Operator operator,
                                       Table rightTable, String rightColumn) {
        this.loginSymbol = loginSymbol;
        this.leftTable = leftTable;
        this.leftField = leftField;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightColumn = rightColumn;
    }


    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, this.getLeftTable()
                .getEtType());
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
                this.rightColumn +
                keywordQM +
                " ";
    }
}
