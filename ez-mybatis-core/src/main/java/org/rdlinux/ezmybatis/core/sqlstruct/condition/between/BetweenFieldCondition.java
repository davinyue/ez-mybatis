package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * between 条件
 */
public class BetweenFieldCondition extends BetweenCondition implements SqlPart {
    @Getter
    @Setter
    protected EntityTable table;
    @Getter
    @Setter
    protected String field;

    public BetweenFieldCondition(LogicalOperator logicalOperator, EntityTable table, String field,
                                 Object minValue, Object maxValue) {
        super(logicalOperator, minValue, maxValue);
        this.table = table;
        this.field = field;
    }

    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, this.getTable().getEtType());
        String column = etInfo.getFieldInfo(this.getField()).getColumnName();
        return this.getTable().getAlias() + "." + keywordQM + column + keywordQM;
    }
}
