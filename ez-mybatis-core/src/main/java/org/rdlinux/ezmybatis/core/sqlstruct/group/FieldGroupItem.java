package org.rdlinux.ezmybatis.core.sqlstruct.group;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

@Getter
public class FieldGroupItem extends AbstractGroupItem {
    protected String field;

    public FieldGroupItem(EntityTable table, String field) {
        super(table);
        this.field = field;
    }

    @Override
    public String toSqlStruct(Configuration configuration) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                ((EntityTable) this.table).getEtType());
        return " " + this.table.getAlias() + "." + keywordQM + entityClassInfo.getFieldInfo(this.field).getColumnName()
                + keywordQM + " ";
    }
}
