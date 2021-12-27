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
 * 表实体属性对比条件
 */
@Getter
public class ColumnCompareFieldCondition implements Condition {
    private LoginSymbol loginSymbol;
    private Table leftTable;
    private String leftColumn;
    private Operator operator;
    private EntityTable rightTable;
    private String rightField;

    public ColumnCompareFieldCondition(Table leftTable, String leftColumn, Operator operator,
                                       EntityTable rightTable, String rightField) {
        this.loginSymbol = LoginSymbol.AND;
        this.leftTable = leftTable;
        this.leftColumn = leftColumn;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightField = rightField;
    }

    public ColumnCompareFieldCondition(LoginSymbol loginSymbol, Table leftTable, String leftColumn, Operator operator,
                                       EntityTable rightTable, String rightField) {
        this.loginSymbol = loginSymbol;
        this.leftTable = leftTable;
        this.leftColumn = leftColumn;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightField = rightField;
    }


    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo oEtInfo = EzEntityClassInfoFactory.forClass(configuration,
                this.getRightTable().getEtType());
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
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
