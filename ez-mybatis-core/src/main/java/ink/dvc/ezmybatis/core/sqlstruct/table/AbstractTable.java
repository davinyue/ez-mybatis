package ink.dvc.ezmybatis.core.sqlstruct.table;

import lombok.Getter;

@Getter
public abstract class AbstractTable implements Table {
    protected String alias;
    protected String partition;

    public AbstractTable(String alias) {
        this.alias = alias;
    }

    public AbstractTable(String alias, String partition) {
        this.alias = alias;
        this.partition = partition;
    }

    @Override
    public String getPartition() {
        return this.partition;
    }
}
