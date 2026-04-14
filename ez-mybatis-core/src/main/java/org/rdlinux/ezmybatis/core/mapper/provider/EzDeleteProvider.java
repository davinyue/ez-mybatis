package org.rdlinux.ezmybatis.core.mapper.provider;

import org.rdlinux.ezmybatis.annotation.MethodName;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.validation.SqlStructureOwnershipValidator;
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
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.fromMyBatisParam(param);
        Class<?> ntClass = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object entity = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(sqlGenerateContext.getConfiguration(),
                ntClass);
        Method fieldGetMethod = entityClassInfo.getPrimaryKeyInfo().getFieldGetMethod();
        Object id = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
        param.put("id", id);
        return EzMybatisContent.getDbDialectProvider(sqlGenerateContext.getConfiguration())
                .getSqlGenerate().getDeleteByIdSql(sqlGenerateContext, null, ntClass, id);
    }

    @MethodName(DELETE_BY_TABLE_METHOD)
    public String deleteByTable(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.fromMyBatisParam(param);
        Class<?> ntClass = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object entity = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(sqlGenerateContext.getConfiguration(),
                ntClass);
        Method fieldGetMethod = entityClassInfo.getPrimaryKeyInfo().getFieldGetMethod();
        Object id = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
        param.put("id", id);
        Table table = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return EzMybatisContent.getDbDialectProvider(sqlGenerateContext.getConfiguration())
                .getSqlGenerate().getDeleteByIdSql(sqlGenerateContext, table, ntClass, id);
    }

    @MethodName(BATCH_DELETE_METHOD)
    public String batchDelete(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.fromMyBatisParam(param);
        Class<?> ntClass = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Collection<Object> models = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        Collection<Object> ids = new ArrayList<>(models.size());
        for (Object entity : models) {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(sqlGenerateContext.getConfiguration(),
                    ntClass);
            Method fieldGetMethod = entityClassInfo.getPrimaryKeyInfo().getFieldGetMethod();
            Object id = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
            ids.add(id);
        }
        param.put("ids", ids);

        return EzMybatisContent.getDbDialectProvider(sqlGenerateContext.getConfiguration())
                .getSqlGenerate().getBatchDeleteByIdSql(sqlGenerateContext, null, ntClass, ids);
    }

    @MethodName(BATCH_DELETE_BY_TABLE_METHOD)
    public String batchDeleteByTable(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.fromMyBatisParam(param);
        Class<?> ntClass = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Collection<Object> models = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        Collection<Object> ids = new ArrayList<>(models.size());
        for (Object entity : models) {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(sqlGenerateContext.getConfiguration(),
                    ntClass);
            Method fieldGetMethod = entityClassInfo.getPrimaryKeyInfo().getFieldGetMethod();
            Object id = ReflectionUtils.invokeMethod(entity, fieldGetMethod);
            ids.add(id);
        }
        param.put("ids", ids);
        Table table = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return EzMybatisContent.getDbDialectProvider(sqlGenerateContext.getConfiguration())
                .getSqlGenerate().getBatchDeleteByIdSql(sqlGenerateContext, table, ntClass, ids);
    }

    @MethodName(DELETE_BY_ID_METHOD)
    public String deleteById(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.fromMyBatisParam(param);
        Class<?> ntClass = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object id = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ID);
        return EzMybatisContent.getDbDialectProvider(sqlGenerateContext.getConfiguration())
                .getSqlGenerate().getDeleteByIdSql(sqlGenerateContext, null, ntClass, id);
    }

    @MethodName(DELETE_BY_TABLE_AND_ID_METHOD)
    public String deleteByTableAndId(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.fromMyBatisParam(param);
        Class<?> ntClass = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object id = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ID);
        Table table = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return EzMybatisContent.getDbDialectProvider(sqlGenerateContext.getConfiguration())
                .getSqlGenerate().getDeleteByIdSql(sqlGenerateContext, table, ntClass, id);
    }

    @MethodName(BATCH_DELETE_BY_ID_METHOD)
    public String batchDeleteById(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.fromMyBatisParam(param);
        Class<?> ntClass = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Collection<Object> ids = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_IDS);
        return EzMybatisContent.getDbDialectProvider(sqlGenerateContext.getConfiguration())
                .getSqlGenerate().getBatchDeleteByIdSql(sqlGenerateContext, null, ntClass, ids);
    }

    @MethodName(BATCH_DELETE_BY_TABLE_AND_ID_METHOD)
    public String batchDeleteByTableAndId(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.fromMyBatisParam(param);
        Class<?> ntClass = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Collection<Object> ids = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_IDS);
        Table table = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_TABLE);
        return EzMybatisContent.getDbDialectProvider(sqlGenerateContext.getConfiguration())
                .getSqlGenerate().getBatchDeleteByIdSql(sqlGenerateContext, table, ntClass, ids);
    }

    @MethodName(DELETE_BY_EZ_DELETE_METHOD)
    public String deleteByEzDelete(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.fromMyBatisParam(param);
        EzDelete delete = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        SqlStructureOwnershipValidator.validate(delete);
        return EzMybatisContent.getDbDialectProvider(sqlGenerateContext.getConfiguration())
                .getSqlGenerate().getDeleteSql(sqlGenerateContext, delete);
    }

    @MethodName(BATCH_DELETE_BY_EZ_DELETE_METHOD)
    public String batchDeleteByEzDelete(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.fromMyBatisParam(param);
        Collection<EzDelete> deletes = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        for (EzDelete delete : deletes) {
            SqlStructureOwnershipValidator.validate(delete);
        }
        return EzMybatisContent.getDbDialectProvider(sqlGenerateContext.getConfiguration())
                .getSqlGenerate().getDeleteSql(sqlGenerateContext, deletes);
    }

    @MethodName(DELETE_BY_SQL_METHOD)
    public String deleteBySql(Map<String, Object> param) {
        SqlGenerateContext sqlGenerateContext = SqlGenerateContext.fromMyBatisParam(param);
        String sql = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = sqlGenerateContext.getParam(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        return sql;
    }
}
