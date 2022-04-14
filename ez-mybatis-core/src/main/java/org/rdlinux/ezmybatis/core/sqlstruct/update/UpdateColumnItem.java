package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

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
        String paramName = mybatisParamHolder.getParamName(this.value, true);
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return this.table.getAlias() + "." + keywordQM + this.column + keywordQM + " = " + paramName;
    }
}
