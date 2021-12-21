package ink.dvc.ezmybatis.core.sqlstruct.update;

import ink.dvc.ezmybatis.core.content.entityinfo.EntityClassInfo;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.content.EzEntityClassInfoFactory;
import ink.dvc.ezmybatis.core.sqlgenerate.KeywordQMFactory;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.core.utils.DbTypeUtils;

@Getter
public class SyntaxUpdateFieldItem extends UpdateItem {
    private EntityTable entityTable;
    private String field;
    private String syntax;

    public SyntaxUpdateFieldItem(EntityTable table, String field, String syntax) {
        super(table);
        this.entityTable = table;
        this.field = field;
        this.syntax = syntax;
    }

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, this.entityTable.getEtType());
        String column = etInfo.getFieldInfo(this.field).getColumnName();
        String keywordQM = KeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return this.table.getAlias() + "." + keywordQM + column + keywordQM + " = " + this.syntax;
    }
}
