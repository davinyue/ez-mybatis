package ink.dvc.ezmybatis.core.sqlstruct.condition.between;

import ink.dvc.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * between 条件
 */
public class BetweenColumnCondition extends BetweenCondition {
    @Getter
    @Setter
    protected EntityTable table;
    @Getter
    @Setter
    protected String column;

    public BetweenColumnCondition(LoginSymbol loginSymbol, EntityTable table, String column,
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
