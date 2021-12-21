package ink.dvc.ezmybatis.core.sqlstruct.condition.nil;

import ink.dvc.ezmybatis.core.content.EzEntityClassInfoFactory;
import ink.dvc.ezmybatis.core.content.entityinfo.EntityClassInfo;
import ink.dvc.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * 是空条件
 */
@Getter
@Setter
public class IsNullFieldCondition extends IsNullCondition {
    protected EntityTable table;
    protected String field;

    public IsNullFieldCondition(LoginSymbol loginSymbol, EntityTable table, String field) {
        this.table = table;
        this.field = field;
        this.loginSymbol = loginSymbol;
    }


    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, this.getTable().getEtType());
        String column = etInfo.getFieldInfo(this.getField()).getColumnName();
        return this.getTable().getAlias() + "." + keywordQM + column + keywordQM;
    }
}
