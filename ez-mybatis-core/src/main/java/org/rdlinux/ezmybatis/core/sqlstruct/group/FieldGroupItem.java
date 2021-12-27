package org.rdlinux.ezmybatis.core.sqlstruct.group;

import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

@Getter
public class FieldGroupItem extends AbstractGroupItem {
    protected String field;

    public FieldGroupItem(EntityTable table, String field) {
        super(table);
        this.field = field;
    }

    @Override
    public String toSqlStruct(Configuration configuration) {
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                ((EntityTable) this.table).getEtType());
        return " " + this.table.getAlias() + "." + keywordQM + entityClassInfo.getFieldInfo(this.field).getColumnName()
                + keywordQM + " ";
    }
}
