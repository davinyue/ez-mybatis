package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.annotation.MethodName;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class EzDeleteProvider {
    public static final String DELETE_METHOD = "delete";
    public static final String DELETE_BY_TABLE_METHOD = "deleteByTable";
    public static final String BATCH_DELETE_METHOD = "batchDelete";
    public static final String BATCH_DELETE_BY_TABLE_METHOD = "batchDeleteByTable";
    public static final String DELETE_BY_ID_METHOD = "deleteById";
    public static final String DELETE_BY_TABLE_AND_ID_METHOD = "deleteByTableAndId";
    public static final String BATCH_DELETE_BY_ID_METHOD = "batchDeleteById";
    public static final String BATCH_DELETE_BY_TABLE_AND_ID_METHOD = "batchDeleteByTableAndId";
    public static final String DELETE_BY_EZ_DELETE_METHOD = "deleteByEzDelete";
    public static final String BATCH_DELETE_BY_EZ_DELETE_METHOD = "batchDeleteByEzDelete";
    public static final String DELETE_BY_SQL_METHOD = "deleteBySql";

    @MethodName(DELETE_METHOD)
    public String delete(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object entity = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        Method fieldGetMethod = entityClassInfo.getPrimaryKeyInfo().getFieldGetMethod();
        Object id = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
        param.put("id", id);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getDeleteByIdSql(configuration, paramHolder, null, ntClass, id);
    }

    @MethodName(DELETE_BY_TABLE_METHOD)
    public String deleteByTable(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object entity = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        Method fieldGetMethod = entityClassInfo.getPrimaryKeyInfo().getFieldGetMethod();
        Object id = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
        param.put("id", id);
        Table table = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getDeleteByIdSql(configuration, paramHolder, table, ntClass, id);
    }

    @MethodName(BATCH_DELETE_METHOD)
    public String batchDelete(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Collection<Object> entitys = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        Collection<Object> ids = new ArrayList<>(entitys.size());
        for (Object entity : entitys) {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
            Method fieldGetMethod = entityClassInfo.getPrimaryKeyInfo().getFieldGetMethod();
            Object id = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
            ids.add(id);
        }
        param.put("ids", ids);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getBatchDeleteByIdSql(configuration, paramHolder, null, ntClass, ids);
    }

    @MethodName(BATCH_DELETE_BY_TABLE_METHOD)
    public String batchDeleteByTable(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Collection<Object> entitys = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        Collection<Object> ids = new ArrayList<>(entitys.size());
        for (Object entity : entitys) {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
            Method fieldGetMethod = entityClassInfo.getPrimaryKeyInfo().getFieldGetMethod();
            Object id = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
            ids.add(id);
        }
        param.put("ids", ids);
        Table table = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getBatchDeleteByIdSql(configuration, paramHolder, table, ntClass, ids);
    }

    @MethodName(DELETE_BY_ID_METHOD)
    public String deleteById(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object id = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ID);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getDeleteByIdSql(configuration, paramHolder, null, ntClass, id);
    }

    @MethodName(DELETE_BY_TABLE_AND_ID_METHOD)
    public String deleteByTableAndId(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object id = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ID);
        Table table = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getDeleteByIdSql(configuration, paramHolder, table, ntClass, id);
    }

    @MethodName(BATCH_DELETE_BY_ID_METHOD)
    public String batchDeleteById(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Collection<Object> ids = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_IDS);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getBatchDeleteByIdSql(configuration, paramHolder, null, ntClass, ids);
    }

    @MethodName(BATCH_DELETE_BY_TABLE_AND_ID_METHOD)
    public String batchDeleteByTableAndId(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Collection<Object> ids = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_IDS);
        Table table = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getBatchDeleteByIdSql(configuration, paramHolder, table, ntClass, ids);
    }

    @MethodName(DELETE_BY_EZ_DELETE_METHOD)
    public String deleteByEzDelete(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzDelete delete = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getDeleteSql(configuration, paramHolder, delete);
    }

    @MethodName(BATCH_DELETE_BY_EZ_DELETE_METHOD)
    public String batchDeleteByEzDelete(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        Configuration configuration = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Collection<EzDelete> deletes = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getDeleteSql(configuration, paramHolder, deletes);
    }

    @MethodName(DELETE_BY_SQL_METHOD)
    public String deleteBySql(Map<String, Object> param) {
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        String sql = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = paramHolder.get(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        return sql;
    }
}
