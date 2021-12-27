package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

/**
 * 表列对比条件
 */
@Getter
public class ColumnCompareCondition implements Condition {
    private LoginSymbol loginSymbol;
    private Table leftTable;
    private String leftColumn;
    private Operator operator;
    private Table rightTable;
    private String rightColumn;

    public ColumnCompareCondition(Table leftTable, String leftColumn, Operator operator,
                                  Table rightTable, String rightColumn) {
        this.loginSymbol = LoginSymbol.AND;
        this.leftTable = leftTable;
        this.leftColumn = leftColumn;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightColumn = rightColumn;
    }

    public ColumnCompareCondition(LoginSymbol loginSymbol, Table leftTable, String leftColumn, Operator operator,
                                  Table rightTable, String rightColumn) {
        this.loginSymbol = loginSymbol;
        this.leftTable = leftTable;
        this.leftColumn = leftColumn;
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
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return " " + this.getLeftTable().getAlias() + "." +
                keywordQM + this.leftColumn + keywordQM +
                " " +
                this.getOperator().getOperator() +
                " " +
                this.getRightTable().getAlias() + "." +
                keywordQM + this.rightColumn + keywordQM +
                " ";
    }
}
