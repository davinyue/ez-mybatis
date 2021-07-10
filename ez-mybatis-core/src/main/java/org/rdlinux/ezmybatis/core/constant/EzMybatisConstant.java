package org.rdlinux.ezmybatis.core.constant;

/**
 * 常量
 */
public abstract class EzMybatisConstant {
    /**
     * 插入实体方法名
     */
    public static final String INSERT_METHOD_NAME = "insert";
    /**
     * 批量插入实体方法名
     */
    public static final String BATCH_INSERT_METHOD_NAME = "batchInsert";
    /**
     * 更新实体方法名
     */
    public static final String UPDATE_METHOD_NAME = "update";
    /**
     * 批量更新实体方法名
     */
    public static final String BATCH_UPDATE_METHOD_NAME = "batchUpdate";
    /**
     * 删除实体方法名
     */
    public static final String DELETE_METHOD_NAME = "delete";
    /**
     * 批量删除实体方法名
     */
    public static final String BATCH_DELETE_METHOD_NAME = "batchDelete";
    /**
     * mapper参数, mapper的class参数名称
     */
    public static final String MAPPER_PARAM_MAPPER_CLASS = "mapperClass";
    /**
     * mapper参数, 实体的class参数名称
     */
    public static final String MAPPER_PARAM_ENTITY_CLASS = "ntClass";
    /**
     * mapper参数, mybatis配置参数名称
     */
    public static final String MAPPER_PARAM_CONFIGURATION = "configuration";
    /**
     * mapper参数, id
     */
    public static final String MAPPER_PARAM_ID = "id";
    /**
     * mapper参数, ids
     */
    public static final String MAPPER_PARAM_IDS = "ids";
    /**
     * mapper参数, entity
     */
    public static final String MAPPER_PARAM_ENTITY = "entity";
    /**
     * mapper参数, entitys
     */
    public static final String MAPPER_PARAM_ENTITYS = "entitys";
}
