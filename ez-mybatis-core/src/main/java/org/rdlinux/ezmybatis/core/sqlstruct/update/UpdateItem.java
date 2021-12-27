package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

@Getter
public abstract class UpdateItem {
    protected Table table;

    public UpdateItem(Table table) {
        this.table = table;
    }

    public abstract String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder);
}
