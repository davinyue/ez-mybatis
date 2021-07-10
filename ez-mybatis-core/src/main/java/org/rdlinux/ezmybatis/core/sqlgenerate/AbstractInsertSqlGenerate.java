package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.content.EntityClassInfo;
import org.rdlinux.ezmybatis.core.content.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public abstract class AbstractInsertSqlGenerate implements InsertSqlGenerate {
    /**
     * 获取关键字引号
     */
    protected abstract String getKeywordQM();

    @Override
    public String getInsertSql(Configuration configuration, Object entity) {
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(entity.getClass(),
                configuration.isMapUnderscoreToCamelCase());
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
            String escape = MybatisParamEscape.getEscapeChar(ReflectionUtils.getFieldValue(entity, field));
            columnBuilder.append(keywordQM).append(column).append(keywordQM);
            paramBuilder.append(escape).append("{").append(EzMybatisConstant.MAPPER_PARAM_ENTITY).append(".")
                    .append(field.getName()).append("}");
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

    @Override
    public String getBatchInsertSql(Configuration configuration, List<Object> entitys) {
        String insertSql = this.getInsertSql(configuration, entitys.get(0));
        String flag = "VALUES ";
        int vIndex = insertSql.indexOf(flag);
        String valve = insertSql.substring(vIndex + flag.length());
        String prefix = insertSql.substring(0, vIndex + flag.length());
        StringBuilder sqlBuilder = new StringBuilder(prefix);
        for (int i = 0; i < entitys.size(); i++) {
            sqlBuilder.append(valve.replaceAll(EzMybatisConstant.MAPPER_PARAM_ENTITY + ".",
                    EzMybatisConstant.MAPPER_PARAM_ENTITYS + "[" + i + "]" + "."));
            if (i + 1 < entitys.size()) {
                sqlBuilder.append(", ");
            }
        }
        return sqlBuilder.toString();
    }
}
