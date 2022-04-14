package org.rdlinux.ezmybatis.core.sqlstruct.order;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

@Getter
public class FieldOrderItem extends OrderItem {
    protected String field;

    public FieldOrderItem(EntityTable table, String field, OrderType type) {
        super(table, type);
        this.field = field;
    }

    public FieldOrderItem(EntityTable table, String field) {
        super(table);
        this.field = field;
    }

    @Override
    public String toSqlStruct(Configuration configuration) {
        Class<?> etType = ((EntityTable) this.getTable()).getEtType();
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, etType);
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return " " + this.getTable().getAlias() + "." +
                keywordQM + entityClassInfo.getFieldInfo(this.field).getColumnName() + keywordQM + " "
                + this.type.name() + " ";
    }
}
