package org.rdlinux.ezmybatis.core.mapper.provider;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
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
        return SqlGenerateFactory.getSqlGenerate(configuration).getDeleteByIdSql(configuration, ntClass, id);
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
        return SqlGenerateFactory.getSqlGenerate(configuration).getBatchDeleteByIdSql(configuration, ntClass, ids);
    }

    public String deleteById(@Param(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION) Configuration configuration,
                             @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> ntClass,
                             @Param(EzMybatisConstant.MAPPER_PARAM_ID) Object id) {
        return SqlGenerateFactory.getSqlGenerate(configuration).getDeleteByIdSql(configuration, ntClass, id);
    }

    public String batchDeleteById(@Param(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION) Configuration configuration,
                                  @Param(EzMybatisConstant.MAPPER_PARAM_ENTITY_CLASS) Class<?> ntClass,
                                  @Param(EzMybatisConstant.MAPPER_PARAM_IDS) List<Object> ids) {
        return SqlGenerateFactory.getSqlGenerate(configuration).getBatchDeleteByIdSql(configuration, ntClass, ids);
    }

    public String deleteByEzDelete(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        EzDelete delete = (EzDelete) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(configuration).getDeleteSql(configuration, delete, param);
    }

    @SuppressWarnings("unchecked")
    public String batchDeleteByEzDelete(Map<String, Object> param) {
        Configuration configuration = (Configuration) param.get(EzMybatisConstant.MAPPER_PARAM_CONFIGURATION);
        List<EzDelete> deletes = (List<EzDelete>) param.get(EzMybatisConstant.MAPPER_PARAM_EZPARAM);
        return SqlGenerateFactory.getSqlGenerate(configuration).getDeleteSql(configuration, deletes, param);
    }
}
