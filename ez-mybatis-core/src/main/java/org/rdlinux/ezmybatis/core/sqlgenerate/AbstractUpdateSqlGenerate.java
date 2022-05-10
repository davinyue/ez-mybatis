package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.utils.Assert;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public abstract class AbstractUpdateSqlGenerate implements UpdateSqlGenerate {

    @Override
    public String getUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder, Object entity,
                               boolean isReplace) {
        if (entity instanceof Collection) {
            throw new IllegalArgumentException("entity can not instanceof Collection");
        }
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, entity.getClass());
        String tableName = entityClassInfo.getTableNameWithSchema(keywordQM);
        Map<String, EntityFieldInfo> columnMapFieldInfo = entityClassInfo.getColumnMapFieldInfo();
        EntityFieldInfo primaryKeyInfo = entityClassInfo.getPrimaryKeyInfo();
        String idColumn = primaryKeyInfo.getColumnName();
        Object idValue = ReflectionUtils.getFieldValue(entity, primaryKeyInfo.getField());
        Assert.notNull(idValue, primaryKeyInfo.getFieldName() + " cannot be null");
        StringBuilder sqlBuilder = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        boolean invalidSql = true;
        for (String column : columnMapFieldInfo.keySet()) {
            EntityFieldInfo entityFieldInfo = columnMapFieldInfo.get(column);
            Method fieldGetMethod = entityFieldInfo.getFieldGetMethod();
            Object fieldValue = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
            if ((!isReplace && fieldValue == null) || column.equals(idColumn)) {
                continue;
            }
            sqlBuilder.append(keywordQM).append(column).append(keywordQM).append(" = ");
            sqlBuilder.append(mybatisParamHolder.getParamName(fieldValue, true)).append(", ");
            //有字段更新, sql才有效
            invalidSql = false;
        }
        Assert.isTrue(!invalidSql, "cannot update empty entity");
        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
        sqlBuilder.append(" WHERE ").append(keywordQM).append(primaryKeyInfo.getColumnName()).append(keywordQM)
                .append(" = ").append(mybatisParamHolder.getParamName(idValue, false));
        return sqlBuilder.toString();
    }

    @Override
    public String getBatchUpdateSql(Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                    Collection<Object> entitys, boolean isReplace) {
        StringBuilder sqlBuilder = new StringBuilder();
        for (Object entity : entitys) {
            String sqlTmpl = this.getUpdateSql(configuration, mybatisParamHolder, entity, isReplace);
            sqlBuilder.append(sqlTmpl).append(";\n");
        }
        return sqlBuilder.toString();
    }
}
