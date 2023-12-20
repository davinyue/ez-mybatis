package org.rdlinux.ezmybatis.constant;

/**
 * 常量
 */
public abstract class EzMybatisConstant {
    /**
     * 插入实体方法名
     */
    public static final String INSERT_METHOD_NAME = "insert";
    /**
     * 根据表插入实体方法名
     */
    public static final String INSERT_BY_TABLE_METHOD_NAME = "insertByTable";
    /**
     * 批量插入实体方法名
     */
    public static final String BATCH_INSERT_METHOD_NAME = "batchInsert";
    /**
     * 批量插入实体方法名
     */
    public static final String BATCH_INSERT_BY_TABLE_METHOD_NAME = "batchInsertByTable";
    /**
     * 更新实体方法名
     */
    public static final String UPDATE_METHOD_NAME = "update";
    /**
     * 批量更新实体方法名
     */
    public static final String BATCH_UPDATE_METHOD_NAME = "batchUpdate";

    /**
     * 替换实体方法名
     */
    public static final String REPLACE_METHOD_NAME = "replace";
    /**
     * 批量替换实体方法名
     */
    public static final String BATCH_REPLACE_METHOD_NAME = "batchReplace";

    /**
     * 删除实体方法名
     */
    public static final String DELETE_METHOD_NAME = "delete";
    /**
     * 批量删除实体方法名
     */
    public static final String BATCH_DELETE_METHOD_NAME = "batchDelete";
    /**
     * 根据id删除实体方法名
     */
    public static final String DELETE_BY_ID_METHOD_NAME = "deleteById";
    /**
     * 根据id批量删除实体方法名
     */
    public static final String BATCH_DELETE_BY_ID_METHOD_NAME = "batchDeleteById";
    /**
     * mapper参数, mapper的class参数名称
     */
    public static final String MAPPER_PARAM_MAPPER_CLASS = "mp_mapperClass";
    /**
     * mapper参数, 实体的class参数名称
     */
    public static final String MAPPER_PARAM_ENTITY_CLASS = "mp_ntClass";
    /**
     * mapper参数, mybatis配置参数名称
     */
    public static final String MAPPER_PARAM_CONFIGURATION = "mp_configuration";
    /**
     * mapper参数, id
     */
    public static final String MAPPER_PARAM_ID = "mp_id";
    /**
     * mapper参数, ids
     */
    public static final String MAPPER_PARAM_IDS = "mp_ids";
    /**
     * mapper参数, entity
     */
    public static final String MAPPER_PARAM_ENTITY = "mp_entity";
    /**
     * mapper参数, entitys
     */
    public static final String MAPPER_PARAM_ENTITYS = "mp_entitys";
    /**
     * mapper参数, 表
     */
    public static final String MAPPER_PARAM_TABLE = "mp_table";
    /**
     * mapper参数, sql
     */
    public static final String MAPPER_PARAM_SQL = "mp_sql";
    /**
     * mapper参数, sql
     */
    public static final String MAPPER_PARAM_SQLPARAM = "mp_sql_param";
    /**
     * mapper参数, ezParam
     */
    public static final String MAPPER_PARAM_EZPARAM = "mp";
    /**
     * mapper参数, 返回结果类型
     */
    public static final String MAPPER_PARAM_RET = "mp_ret";
    /**
     * mapper参数, 自定义更新扩展
     */
    public static final String MAPPER_PARAM_UPDATE_EXPAND = "mp_update_expand";
    /**
     * oracle行数别名
     */
    public static final String ORACLE_ROW_NUM_ALIAS = "ORA_ROWNUM__$";
}
