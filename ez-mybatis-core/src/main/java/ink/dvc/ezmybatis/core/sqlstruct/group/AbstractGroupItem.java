package ink.dvc.ezmybatis.core.sqlstruct.group;

import ink.dvc.ezmybatis.core.sqlstruct.table.Table;
import lombok.Getter;

@Getter
public abstract class AbstractGroupItem implements GroupItem {
    protected Table table;

    public AbstractGroupItem(Table table) {
        this.table = table;
    }
}
