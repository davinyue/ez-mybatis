package org.rdlinux.ezmybatis.core.sqlstruct.selectitem;

import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SelectTableAllItem implements SelectItem, SqlStruct {
    private Table table;
    private Set<String> excludeField;

    /**
     * @param excludeField 排除的查询项, 只支持entityTable
     */
    public SelectTableAllItem(Table table, String... excludeField) {
        Assert.notNull(table, "table can not be null");
        this.table = table;
        if (excludeField != null && excludeField.length > 0) {
            if (!(table instanceof EntityTable)) {
                throw new IllegalArgumentException("excludeField only supports EntityTable");
            }
            this.excludeField = new HashSet<>(Arrays.asList(excludeField));
        }
    }

    public Table getTable() {
        return this.table;
    }

    public Set<String> getExcludeField() {
        return this.excludeField;
    }
}
