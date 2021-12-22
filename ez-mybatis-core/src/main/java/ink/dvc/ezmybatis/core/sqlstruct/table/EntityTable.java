package ink.dvc.ezmybatis.core.sqlstruct.table;

import ink.dvc.ezmybatis.core.content.EzEntityClassInfoFactory;
import ink.dvc.ezmybatis.core.sqlstruct.Alias;
import org.apache.ibatis.session.Configuration;

public class EntityTable extends AbstractTable {
    private Class<?> etType;

    private EntityTable(Class<?> etType) {
        super(Alias.getAlias(), null);
        this.etType = etType;
    }

    private EntityTable(Class<?> etType, String partition) {
        super(Alias.getAlias(), partition);
        this.etType = etType;
    }

    public static EntityTable of(Class<?> etType) {
        return new EntityTable(etType);
    }

    public static EntityTable of(Class<?> etType, String partition) {
        return new EntityTable(etType, partition);
    }

    public Class<?> getEtType() {
        return this.etType;
    }

    @Override
    public String getTableName(Configuration configuration) {
        return EzEntityClassInfoFactory.forClass(configuration, this.etType).getTableName();
    }
}
