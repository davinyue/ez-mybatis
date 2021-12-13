package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.Table;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;

/**
 * between 条件
 */
public class BetweenFieldCondition extends BetweenCondition {
    @Getter
    @Setter
    protected Table table;
    @Getter
    @Setter
    protected String field;

    public BetweenFieldCondition(LoginSymbol loginSymbol, Table table, String field,
                                 Object minValue, Object maxValue) {
        super(loginSymbol, minValue, maxValue);
        this.table = table;
        this.field = field;
    }

    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, this.getTable().getEtType());
        String column = etInfo.getFieldInfo(this.getField()).getColumnName();
        return this.getTable().getAlias() + "." + keywordQM + column + keywordQM;
    }
}
