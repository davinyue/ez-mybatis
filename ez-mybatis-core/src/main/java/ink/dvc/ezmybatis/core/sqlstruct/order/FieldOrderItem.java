package ink.dvc.ezmybatis.core.sqlstruct.order;

import ink.dvc.ezmybatis.core.content.EzEntityClassInfoFactory;
import ink.dvc.ezmybatis.core.content.entityinfo.EntityClassInfo;
import ink.dvc.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

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
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return " " + this.getTable().getAlias() + "." +
                keywordQM + entityClassInfo.getFieldInfo(this.field).getColumnName() + keywordQM + " "
                + this.type.name() + " ";
    }
}
