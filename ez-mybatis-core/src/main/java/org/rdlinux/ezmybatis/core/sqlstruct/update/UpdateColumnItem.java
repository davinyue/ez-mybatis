package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

@Getter
public class UpdateColumnItem extends UpdateItem {
    private String column;
    private Object value;

    public UpdateColumnItem(Table table, String column, Object value) {
        super(table);
        this.column = column;
        this.value = value;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        String paramName = mybatisParamHolder.getParamName(this.value);
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return this.table.getAlias() + "." + keywordQM + this.column + keywordQM + " = " + paramName;
    }
}
