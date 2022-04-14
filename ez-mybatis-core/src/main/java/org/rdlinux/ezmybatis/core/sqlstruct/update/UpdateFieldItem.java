package org.rdlinux.ezmybatis.core.sqlstruct.update;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

@Getter
public class UpdateFieldItem extends UpdateItem {
    private EntityTable entityTable;
    private String field;
    private Object value;

    public UpdateFieldItem(EntityTable table, String field, Object value) {
        super(table);
        this.entityTable = table;
        this.field = field;
        this.value = value;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, this.entityTable.getEtType());
        String column = etInfo.getFieldInfo(this.getField()).getColumnName();
        String paramName = mybatisParamHolder.getParamName(this.value, true);
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return this.table.getAlias() + "." + keywordQM + column + keywordQM + " = " + paramName;
    }
}
