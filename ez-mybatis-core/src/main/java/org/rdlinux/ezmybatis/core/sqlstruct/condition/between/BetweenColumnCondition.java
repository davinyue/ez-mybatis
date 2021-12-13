package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.Table;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;

/**
 * between 条件
 */
public class BetweenColumnCondition extends BetweenCondition {
    @Getter
    @Setter
    protected Table table;
    @Getter
    @Setter
    protected String column;

    public BetweenColumnCondition(LoginSymbol loginSymbol, Table table, String column,
                                  Object minValue, Object maxValue) {
        super(loginSymbol, minValue, maxValue);
        this.table = table;
        this.column = column;
    }

    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return this.getTable().getAlias() + "." + keywordQM + this.column + keywordQM;
    }
}
