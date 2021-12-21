package ink.dvc.ezmybatis.core.sqlstruct.condition.compare;

import ink.dvc.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Condition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * 表列对比条件
 */
@Getter
public class CompareColumnCondition implements Condition {
    private LoginSymbol loginSymbol;
    private EntityTable table;
    private String column;
    private Operator operator;
    private EntityTable otherTable;
    private String otherColumn;

    public CompareColumnCondition(EntityTable table, String column, Operator operator,
                                  EntityTable otherTable, String otherColumn) {
        this.loginSymbol = LoginSymbol.AND;
        this.table = table;
        this.column = column;
        this.operator = operator;
        this.otherTable = otherTable;
        this.otherColumn = otherColumn;
    }

    public CompareColumnCondition(LoginSymbol loginSymbol, EntityTable table, String column, Operator operator,
                                  EntityTable otherTable, String otherColumn) {
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
