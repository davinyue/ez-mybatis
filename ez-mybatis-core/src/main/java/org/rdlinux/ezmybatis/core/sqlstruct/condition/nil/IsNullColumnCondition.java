package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;

/**
 * 是空条件
 */
@Getter
@Setter
public class IsNullColumnCondition extends IsNullCondition {
    protected Table table;
    protected String column;

    public IsNullColumnCondition(LoginSymbol loginSymbol, Table table, String column) {
        this.table = table;
        this.column = column;
        this.loginSymbol = loginSymbol;
    }


    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return this.getTable().getAlias() + "." + keywordQM + this.column + keywordQM;
    }

    @Override
    public LoginSymbol getLoginSymbol() {
        return this.loginSymbol;
    }
}
