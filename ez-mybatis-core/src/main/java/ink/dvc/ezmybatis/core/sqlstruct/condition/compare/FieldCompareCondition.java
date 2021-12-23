package ink.dvc.ezmybatis.core.sqlstruct.condition.compare;

import ink.dvc.ezmybatis.core.content.EzEntityClassInfoFactory;
import ink.dvc.ezmybatis.core.content.entityinfo.EntityClassInfo;
import ink.dvc.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Condition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

/**
 * 表实体属性对比条件
 */
@Getter
public class FieldCompareCondition implements Condition {
    private LoginSymbol loginSymbol;
    private EntityTable leftTable;
    private String leftField;
    private Operator operator;
    private EntityTable rightTable;
    private String rightField;

    public FieldCompareCondition(EntityTable leftTable, String leftField, Operator operator,
                                 EntityTable rightTable, String rightField) {
        this.loginSymbol = LoginSymbol.AND;
        this.leftTable = leftTable;
        this.leftField = leftField;
        this.operator = operator;
        this.rightTable = rightTable;
        this.rightField = rightField;
    }

    public FieldCompareCondition(LoginSymbol loginSymbol, EntityTable leftTable, String leftField, Operator operator,
                                 EntityTable rightTable, String rightField) {
        this.loginSymbol = loginSymbol;
        this.leftTable = leftTable;
        this.leftField = leftField;
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
