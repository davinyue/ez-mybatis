package ink.dvc.ezmybatis.core.sqlstruct.update;

import ink.dvc.ezmybatis.core.content.EzEntityClassInfoFactory;
import ink.dvc.ezmybatis.core.content.entityinfo.EntityClassInfo;
import ink.dvc.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamEscape;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

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
        String paramName = mybatisParamHolder.getParamName(this.value);
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return this.table.getAlias() + "." + keywordQM + column + keywordQM + " = " +
                MybatisParamEscape.getEscapeChar(this.value) + "{" + paramName + "}";
    }
}
