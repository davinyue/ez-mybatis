package ink.dvc.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamEscape;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;

@Getter
public class UpdateColumnItem extends UpdateItem {
    private String column;
    private Object value;

    public UpdateColumnItem(Table table, String column, Object value) {
        super(table);
        this.column = column;
        this.value = value;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        String paramName = mybatisParamHolder.getParamName(this.value);
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return this.table.getAlias() + "." + keywordQM + this.column + keywordQM + " = " +
                MybatisParamEscape.getEscapeChar(this.value) + "{" + paramName + "}";
    }
}
