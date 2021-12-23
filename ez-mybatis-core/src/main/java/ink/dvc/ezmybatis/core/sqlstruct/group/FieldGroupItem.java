package ink.dvc.ezmybatis.core.sqlstruct.group;

import ink.dvc.ezmybatis.core.content.EzEntityClassInfoFactory;
import ink.dvc.ezmybatis.core.content.entityinfo.EntityClassInfo;
import ink.dvc.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
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
