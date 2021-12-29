package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EzEntityDeleteProvider {
    public String delete(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = (Class<?>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object entity = param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY);
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
        Field idField = entityClassInfo.getPrimaryKeyInfo().getField();
        Object id = ReflectionUtils.getFieldValue(entity, idField);
        param.put("id", id);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getDeleteByIdSql(configuration, paramHolder, ntClass,
                id);
    }

    @SuppressWarnings(value = {"rawtype", "unchecked"})
    public String batchDelete(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = (Class<?>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        List<Object> entitys = (List<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITYS);
        List<Object> ids = new ArrayList<>(entitys.size());
        for (Object entity : entitys) {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ntClass);
            Field idField = entityClassInfo.getPrimaryKeyInfo().getField();
            Object id = ReflectionUtils.getFieldValue(entity, idField);
            ids.add(id);
        }
        param.put("ids", ids);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getBatchDeleteByIdSql(configuration, paramHolder,
                ntClass, ids);
    }

    public String deleteById(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = (Class<?>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        Object id = param.get(EzMybatisConstant.MAPPER_PARAM_ID);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getDeleteByIdSql(configuration, paramHolder, ntClass,
                id);
    }

    @SuppressWarnings("unchecked")
    public String batchDeleteById(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        Class<?> ntClass = (Class<?>) param.get(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS);
        List<Object> ids = (List<Object>) param.get(EzMybatisConstant.MAPPER_PARAM_IDS);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getBatchDeleteByIdSql(configuration, paramHolder,
                ntClass, ids);
    }

    public String deleteByEzDelete(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzDelete delete = (EzDelete) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getDeleteSql(configuration, paramHolder, delete);
    }

    @SuppressWarnings("unchecked")
    public String batchDeleteByEzDelete(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        List<EzDelete> deletes = (List<EzDelete>) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        MybatisParamHolder paramHolder = new MybatisParamHolder(param);
        return SqlGenerateFactory.getSqlGenerate(configuration).getDeleteSql(configuration, paramHolder, deletes);
    }

    @SuppressWarnings("unchecked")
    public String deleteBySql(Map<String, Object> param) {
        String sql = (String) param.get(EzMybatisConstant.MAPPER_PARAM_SQL);
        Map<String, Object> sqlParam = (Map<String, Object>) param.get(EzMybatisConstant.MAPPER_PARAM_SQLPARAM);
        param.putAll(sqlParam);
        return sql;
    }
}
