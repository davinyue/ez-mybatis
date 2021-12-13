package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;

/**
 * 表列对比条件
 */
@Getter
public class CompareColumnCondition implements Condition {
    private LoginSymbol loginSymbol;
    private Table table;
    private String column;
    private Operator operator;
    private Table otherTable;
    private String otherColumn;

    public CompareColumnCondition(Table table, String column, Operator operator,
                                  Table otherTable, String otherColumn) {
        this.loginSymbol = LoginSymbol.AND;
        this.table = table;
        this.column = column;
        this.operator = operator;
        this.otherTable = otherTable;
        this.otherColumn = otherColumn;
    }

    public CompareColumnCondition(LoginSymbol loginSymbol, Table table, String column, Operator operator,
                                  Table otherTable, String otherColumn) {
        this.loginSymbol = loginSymbol;
        this.table = table;
        this.column = column;
        this.operator = operator;
        this.otherTable = otherTable;
        this.otherColumn = otherColumn;
    }


    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return " " + this.getTable().getAlias() + "." +
                keywordQM + this.column + keywordQM +
                " " +
                this.getOperator().getOperator() +
                " " +
                this.getOtherTable().getAlias() + "." +
                keywordQM + this.otherColumn + keywordQM +
                " ";
    }
}
