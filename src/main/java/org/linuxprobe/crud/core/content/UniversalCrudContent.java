package org.linuxprobe.crud.core.content;

import org.linuxprobe.crud.core.annoatation.Entity;
import org.linuxprobe.crud.core.annoatation.Query;
import org.linuxprobe.crud.core.sql.generator.DeleteSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.InsertSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.SelectSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.UpdateSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.impl.mysql.MysqlDeleteSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.impl.mysql.MysqlInsertSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.impl.mysql.MysqlSelectSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.impl.mysql.MysqlUpdateSqlGenerator;
import org.linuxprobe.crud.mybatis.session.UniversalCrudConfiguration;
import org.linuxprobe.luava.reflection.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UniversalCrudContent {
    private static Logger logger = LoggerFactory.getLogger(UniversalCrudContent.class);
    private static Map<String, EntityInfo> entityInfos = new HashMap<>();
    private static Map<String, QueryInfo> queryInfos = new HashMap<>();
    private static UniversalCrudConfiguration universalCrudConfiguration;
    private static InsertSqlGenerator mysqlInsertSqlGenerator;
    private static DeleteSqlGenerator mysqlDeleteSqlGenerator;
    private static SelectSqlGenerator mysqlSelectSqlGenerator;
    private static UpdateSqlGenerator mysqlUpdateSqlGenerator;

    /**
     * 初始化content
     */
    public static void init(UniversalCrudConfiguration universalCrudConfiguration) {
        UniversalCrudContent.universalCrudConfiguration = universalCrudConfiguration;
        /** 扫描类信息 */
        String[] scans = universalCrudConfiguration.getUniversalCrudScan().split(";");
        /** 必须先扫描实体信息 */
        for (String scan : scans) {
            Reflections reflections = new Reflections(scan);
            Set<Class<?>> entityClasss = reflections.getTypesAnnotatedWith(Entity.class);
            for (Class<?> entityClass : entityClasss) {
                UniversalCrudContent.logger.debug("scan entity class " + entityClass.getName());
                UniversalCrudContent.addEntityInfo(entityClass);
            }
        }
        /** 实体信息扫描完后扫描搜索类信息，因为搜索类需要读取实体类信息 */
        for (String scan : scans) {
            Reflections reflections = new Reflections(scan);
            Set<Class<?>> queryClasss = reflections.getTypesAnnotatedWith(Query.class);
            for (Class<?> queryClass : queryClasss) {
                UniversalCrudContent.logger.debug("scan query class " + queryClass.getName());
                UniversalCrudContent.addQueryInfo(queryClass);
            }
        }
    }

    public static InsertSqlGenerator getInsertSqlGenerator() {
        String driverClassType = UniversalCrudContent.universalCrudConfiguration.getDriverClassName();
        if (driverClassType.indexOf("mysql") != -1) {
            if (UniversalCrudContent.mysqlInsertSqlGenerator == null) {
                UniversalCrudContent.mysqlInsertSqlGenerator = new MysqlInsertSqlGenerator();
            }
            return UniversalCrudContent.mysqlInsertSqlGenerator;
        } else {
            return null;
        }
    }

    public static DeleteSqlGenerator getDeleteSqlGenerator() {
        String driverClassType = UniversalCrudContent.universalCrudConfiguration.getDriverClassName();
        if (driverClassType.indexOf("mysql") != -1) {
            if (UniversalCrudContent.mysqlDeleteSqlGenerator == null) {
                UniversalCrudContent.mysqlDeleteSqlGenerator = new MysqlDeleteSqlGenerator();
            }
            return UniversalCrudContent.mysqlDeleteSqlGenerator;
        } else {
            return null;
        }
    }

    public static SelectSqlGenerator getSelectSqlGenerator() {
        String driverClassType = UniversalCrudContent.universalCrudConfiguration.getDriverClassName();
        if (driverClassType.indexOf("mysql") != -1) {
            if (UniversalCrudContent.mysqlSelectSqlGenerator == null) {
                UniversalCrudContent.mysqlSelectSqlGenerator = new MysqlSelectSqlGenerator();
            }
            return UniversalCrudContent.mysqlSelectSqlGenerator;
        } else {
            return null;
        }
    }

    public static UpdateSqlGenerator getUpdateSqlGenerator() {
        String driverClassType = UniversalCrudContent.universalCrudConfiguration.getDriverClassName();
        if (driverClassType.indexOf("mysql") != -1) {
            if (UniversalCrudContent.mysqlUpdateSqlGenerator == null) {
                UniversalCrudContent.mysqlUpdateSqlGenerator = new MysqlUpdateSqlGenerator();
            }
            return UniversalCrudContent.mysqlUpdateSqlGenerator;
        } else {
            return null;
        }
    }

    private static void addEntityInfo(Class<?> entityType) {
        UniversalCrudContent.entityInfos.put(entityType.getName(), new EntityInfo(entityType));
    }

    public static EntityInfo getEntityInfo(Class<?> entityType) {
        entityType = ReflectionUtils.getRealCalssOfProxyClass(entityType);
        UniversalCrudContent.logger.trace("get entityInfo of " + entityType.getName());
        EntityInfo result = UniversalCrudContent.entityInfos.get(entityType.getName());
        if (result == null) {
            throw new IllegalArgumentException("can't fond entityInfo of " + entityType.getName());
        }
        return result;
    }

    public static EntityInfo getEntityInfo(String entityType) {
        UniversalCrudContent.logger.trace("get entityInfo of " + entityType);
        EntityInfo result = UniversalCrudContent.entityInfos.get(entityType);
        if (result == null) {
            throw new IllegalArgumentException("can't fond entityInfo of " + entityType);
        }
        return result;
    }

    private static void addQueryInfo(Class<?> queryType) {
        UniversalCrudContent.queryInfos.put(queryType.getName(), new QueryInfo(queryType));
    }

    public static QueryInfo getQueryInfo(Class<?> queryType) {
        UniversalCrudContent.logger.trace("get queryInfo of " + queryType.getName());
        QueryInfo queryInfo = UniversalCrudContent.queryInfos.get(queryType.getName());
        if (queryInfo == null) {
            throw new IllegalArgumentException("can't fond queryInfo of " + queryType.getName());
        }
        return queryInfo;
    }
}
