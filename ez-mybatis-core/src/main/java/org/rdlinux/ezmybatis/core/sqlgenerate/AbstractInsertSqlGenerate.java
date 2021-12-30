package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;
import org.rdlinux.ezmybatis.core.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class AbstractInsertSqlGenerate implements InsertSqlGenerate {

    @Override
    public String getInsertSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Object entity) {
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, entity.getClass());
        String tableName = entityClassInfo.getTableName();
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        Map<String, EntityFieldInfo> columnMapFieldInfo = entityClassInfo.getColumnMapFieldInfo();
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(keywordQM).append(tableName)
                .append(keywordQM).append(" ");
        StringBuilder columnBuilder = new StringBuilder("( ");
        StringBuilder paramBuilder = new StringBuilder("( ");
        int i = 1;
        for (String column : columnMapFieldInfo.keySet()) {
            Field field = columnMapFieldInfo.get(column).getField();
            Object fieldValue = ReflectionUtils.getFieldValue(entity, field);
            columnBuilder.append(keywordQM).append(column).append(keywordQM);
            if (fieldValue == null) {
                paramBuilder.append("NULL");
            } else {
                paramBuilder.append(mybatisParamHolder.getParamName(fieldValue));
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
