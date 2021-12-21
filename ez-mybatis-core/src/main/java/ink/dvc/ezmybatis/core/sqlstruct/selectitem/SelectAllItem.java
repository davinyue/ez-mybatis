package ink.dvc.ezmybatis.core.sqlstruct.selectitem;

import ink.dvc.ezmybatis.core.utils.Assert;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.sqlstruct.table.Table;

public class SelectAllItem implements SelectItem {
    private Table table;

    public SelectAllItem(Table table) {
        Assert.notNull(table, "table can not be null");
        this.table = table;
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(EntityTable table) {
        Assert.notNull(table, "table can not be null");
        this.table = table;
    }

    @Override
    public String toSqlPart(Configuration configuration) {
        return " " + this.table.getAlias() + ".* ";
    }
}
