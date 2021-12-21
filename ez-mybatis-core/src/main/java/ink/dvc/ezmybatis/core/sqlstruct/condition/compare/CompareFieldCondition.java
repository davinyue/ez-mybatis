package ink.dvc.ezmybatis.core.sqlstruct.condition.compare;

import ink.dvc.ezmybatis.core.content.EzEntityClassInfoFactory;
import ink.dvc.ezmybatis.core.content.entityinfo.EntityClassInfo;
import ink.dvc.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Condition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * 表实体属性对比条件
 */
@Getter
public class CompareFieldCondition implements Condition {
    private LoginSymbol loginSymbol;
    private EntityTable table;
    private String field;
    private Operator operator;
    private EntityTable otherTable;
    private String otherField;

    public CompareFieldCondition(EntityTable table, String field, Operator operator,
                                 EntityTable otherTable, String otherField) {
        this.loginSymbol = LoginSymbol.AND;
        this.table = table;
        this.field = field;
        this.operator = operator;
        this.otherTable = otherTable;
        this.otherField = otherField;
    }

    public CompareFieldCondition(LoginSymbol loginSymbol, EntityTable table, String field, Operator operator,
                                 EntityTable otherTable, String otherField) {
        this.loginSymbol = loginSymbol;
        this.table = table;
        this.field = field;
        this.operator = operator;
        this.otherTable = otherTable;
        this.otherField = otherField;
    }


    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, this.getTable()
                .getEtType());
        EntityClassInfo oEtInfo = EzEntityClassInfoFactory.forClass(configuration,
                this.getOtherTable().getEtType());
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return " " + this.getTable().getAlias() + "." +
                keywordQM +
                etInfo.getFieldInfo(this.getField()).getColumnName() +
                keywordQM +
                " " +
                this.getOperator().getOperator() +
                " " +
                this.getOtherTable().getAlias() + "." +
                keywordQM +
                oEtInfo.getFieldInfo(this.getOtherField()).getColumnName() +
                keywordQM +
                " ";
    }
}
