package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.utils.Assert;
import org.rdlinux.ezmybatis.core.utils.DbTypeUtils;
import org.rdlinux.ezmybatis.core.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public abstract class AbstractUpdateSqlGenerate implements UpdateSqlGenerate {

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Object entity,
                               boolean isReplace) {
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, entity.getClass());
        String tableName = entityClassInfo.getTableName();
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        Map<String, EntityFieldInfo> columnMapFieldInfo = entityClassInfo.getColumnMapFieldInfo();
        EntityFieldInfo primaryKeyInfo = entityClassInfo.getPrimaryKeyInfo();
        Field idField = primaryKeyInfo.getField();
        String idColumn = primaryKeyInfo.getColumnName();
        Object idValue = ReflectionUtils.getFieldValue(entity, primaryKeyInfo.getField());
        Assert.notNull(idValue, primaryKeyInfo.getFieldName() + " cannot be null");
        StringBuilder sqlBuilder = new StringBuilder("UPDATE ").append(keywordQM).append(tableName)
                .append(keywordQM).append(" SET ");
        boolean invalidSql = true;
        for (String column : columnMapFieldInfo.keySet()) {
            EntityFieldInfo entityFieldInfo = columnMapFieldInfo.get(column);
            Field field = entityFieldInfo.getField();
            Object fieldValue = ReflectionUtils.getFieldValue(entity, field);
            if ((!isReplace && fieldValue == null) || column.equals(idColumn)) {
                continue;
            }
            sqlBuilder.append(keywordQM).append(column).append(keywordQM).append(" = ");
            if (fieldValue == null) {
                sqlBuilder.append("NULL, ");
            } else {
                String paramName = mybatisParamHolder.getParamName(fieldValue);
                sqlBuilder.append(MybatisParamEscape.getEscapeChar(fieldValue)).append("{").append(paramName)
                        .append("}, ");
            }
            //有字段更新, sql才有效
            invalidSql = false;
        }
        Assert.isTrue(!invalidSql, "cannot update empty entity");
        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
        sqlBuilder.append(" WHERE ").append(keywordQM).append(primaryKeyInfo.getColumnName()).append(keywordQM)
                .append(" = ").append(MybatisParamEscape.getEscapeChar(idValue)).append("{")
                .append(mybatisParamHolder.getParamName(idValue)).append("}");
        return sqlBuilder.toString();
    }

    @Override
    public String getBatchUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    List<Object> entitys, boolean isReplace) {
        StringBuilder sqlBuilder = new StringBuilder();
        for (Object entity : entitys) {
            String sqlTmpl = this.getUpdateSql(configuration, mybatisParamHolder, entity, isReplace);
            sqlBuilder.append(sqlTmpl).append(";");
        }
        return sqlBuilder.toString();
    }
}
