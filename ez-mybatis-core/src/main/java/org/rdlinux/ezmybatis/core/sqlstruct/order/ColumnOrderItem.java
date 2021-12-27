package org.rdlinux.ezmybatis.core.sqlstruct.order;

import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

@Getter
public class ColumnOrderItem extends OrderItem {
    protected String column;

    public ColumnOrderItem(Table table, String column, OrderType type) {
        super(table, type);
        this.column = column;
    }

    public ColumnOrderItem(Table table, String column) {
        super(table);
        this.column = column;
    }

    @Override
    public String toSqlStruct(Configuration configuration) {
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return " " + this.getTable().getAlias() + "." +
                keywordQM + this.column + keywordQM + " "
                + this.type.name() + " ";
    }
}
