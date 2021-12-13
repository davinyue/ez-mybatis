package org.rdlinux.ezmybatis.core.sqlstruct.condition.compare;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;

/**
 * 表实体属性对比条件
 */
@Getter
public class CompareFieldCondition implements Condition {
    private LoginSymbol loginSymbol;
    private Table table;
    private String field;
    private Operator operator;
    private Table otherTable;
    private String otherField;

    public CompareFieldCondition(Table table, String field, Operator operator,
                                 Table otherTable, String otherField) {
        this.loginSymbol = LoginSymbol.AND;
        this.table = table;
        this.field = field;
        this.operator = operator;
        this.otherTable = otherTable;
        this.otherField = otherField;
    }

    public CompareFieldCondition(LoginSymbol loginSymbol, Table table, String field, Operator operator,
                                 Table otherTable, String otherField) {
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
