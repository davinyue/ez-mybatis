package ink.dvc.ezmybatis.core.sqlgenerate;

import ink.dvc.ezmybatis.core.content.EzEntityClassInfoFactory;
import ink.dvc.ezmybatis.core.content.entityinfo.EntityClassInfo;
import ink.dvc.ezmybatis.core.content.entityinfo.EntityFieldInfo;
import org.apache.ibatis.session.Configuration;
import ink.dvc.ezmybatis.core.constant.EzMybatisConstant;
import ink.dvc.ezmybatis.core.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class AbstractInsertSqlGenerate implements InsertSqlGenerate, KeywordQM {

    @Override
    public String getInsertSql(Configuration configuration, Object entity) {
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, entity.getClass());
        String tableName = entityClassInfo.getTableName();
        String keywordQM = this.getKeywordQM();
        Map<String, EntityFieldInfo> columnMapFieldInfo = entityClassInfo.getColumnMapFieldInfo();
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(keywordQM).append(tableName)
                .append(keywordQM).append(" ");
        StringBuilder columnBuilder = new StringBuilder("( ");
        StringBuilder paramBuilder = new StringBuilder("( ");
        int i = 1;
        for (String column : columnMapFieldInfo.keySet()) {
            Field field = columnMapFieldInfo.get(column).getField();
            Object fieldValue = ReflectionUtils.getFieldValue(entity, field);
            String escape = MybatisParamEscape.getEscapeChar(ReflectionUtils.getFieldValue(entity, field));
            columnBuilder.append(keywordQM).append(column).append(keywordQM);
            if (fieldValue == null) {
                paramBuilder.append("NULL");
            } else {
                paramBuilder.append(escape).append("{").append(EzMybatisConstant.MAPPER_PARAM_ENTITY).append(".")
                        .append(field.getName()).append("}");
            }
            if (i < columnMapFieldInfo.size()) {
                columnBuilder.append(", ");
                paramBuilder.append(", ");
            } else {
                columnBuilder.append(" )");
                paramBuilder.append(" )");
            }
            i++;
        }
        sqlBuilder.append(columnBuilder).append(" VALUES ").append(paramBuilder);
        return sqlBuilder.toString();
    }
}
