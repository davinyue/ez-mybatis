package ink.dvc.ezmybatis.core.sqlstruct.condition.between;

import ink.dvc.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;

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
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return this.getTable().getAlias() + "." + keywordQM + this.column + keywordQM;
    }
}
