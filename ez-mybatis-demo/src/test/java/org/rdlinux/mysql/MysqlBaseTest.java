package org.rdlinux.mysql;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisDeleteListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisOnBuildSqlGetFieldListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.rdlinux.ezmybatis.core.sqlstruct.ObjArg;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;
import org.rdlinux.ezmybatis.demo.entity.BaseEntity;
import org.rdlinux.ezmybatis.demo.entity.User;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;

@Slf4j
public class MysqlBaseTest {
    protected static SqlSessionFactory sqlSessionFactory;

    static {

        String resource = "mybatis-config-mysql.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        XMLConfigBuilder parser = new XMLConfigBuilder(reader, null, null);
        Configuration configuration = parser.parse();
        EzMybatisConfig ezMybatisConfig = new EzMybatisConfig(configuration);
        ezMybatisConfig.setEscapeKeyword(true);
        EzMybatisContent.init(ezMybatisConfig);
        EzMybatisContent.addOnBuildSqlGetFieldListener(ezMybatisConfig, new EzMybatisOnBuildSqlGetFieldListener() {
            @Override
            public Object onGet(boolean isSimple, Class<?> ntType, Field field, Object value) {
                if (ntType == User.class && field.getName().equals(User.Fields.name)) {
                    log.info("处理参数{}的加密", field.getName());
                    return "enc$" + value;
                }
                return value;
            }

            @Override
            public int order() {
                return 1;
            }
        });
        EzMybatisContent.addInsertListener(ezMybatisConfig, new EzMybatisInsertListener() {
            @Override
            public void onInsert(Object entity) {
                if (entity instanceof BaseEntity) {
                    MysqlBaseTest.log.info("插入事件");
                    ((BaseEntity) entity).setUpdateTime(new Date());
                    ((BaseEntity) entity).setCreateTime(new Date());
                }
            }

            @Override
            public void onBatchInsert(Collection<?> models) {
                models.forEach(this::onInsert);
            }
        });
        EzMybatisContent.addDeleteListener(ezMybatisConfig, new EzMybatisDeleteListener() {

            @Override
            public void onDelete(Object entity) {
                MysqlBaseTest.log.info("删除事件");
            }

            @Override
            public void onBatchDelete(Collection<Object> models) {
                models.forEach(this::onDelete);
            }

            @Override
            public void onDeleteById(Object id, Class<?> ntClass) {
                MysqlBaseTest.log.info("删除事件");
            }

            @Override
            public void onBatchDeleteById(Collection<Object> ids, Class<?> ntClass) {
                for (Object id : ids) {
                    this.onDeleteById(id, ntClass);
                }
            }

            @Override
            public void onEzDelete(EzDelete ezDelete) {
                MysqlBaseTest.log.info("ez_delete删除:{}", ezDelete.getTable().getTableName(configuration));
            }
        });
        EzMybatisContent.addUpdateListener(ezMybatisConfig, new EzMybatisUpdateListener() {
            @Override
            public void onUpdate(Object entity) {
                MysqlBaseTest.log.info("更新事件");
            }

            @Override
            public void onBatchUpdate(Collection<?> models) {
                MysqlBaseTest.log.info("更新事件");
            }

            @Override
            public void onReplace(Object entity) {
                MysqlBaseTest.log.info("替换事件");
            }

            @Override
            public void onBatchReplace(Collection<?> models) {
                MysqlBaseTest.log.info("替换事件");
            }

            @Override
            public void onEzUpdate(EzUpdate ezUpdate) {
                Table table = ezUpdate.getTable();
                if (table instanceof EntityTable) {
                    Class<?> etType = ((EntityTable) table).getEtType();
                    if (BaseEntity.class.isAssignableFrom(etType)) {
                        ezUpdate.getSet().getItems().add(new UpdateFieldItem(((EntityTable) table),
                                BaseEntity.Fields.updateTime, ObjArg.of(new Date())));
                    }
                }

            }

            @Override
            public void onEzBatchUpdate(Collection<EzUpdate> ezUpdates) {
                ezUpdates.forEach(this::onEzUpdate);
            }
        });
        EzMybatisContent.addOnBuildSqlGetFieldListener(ezMybatisConfig, (originObj, ntType,
                                                                         field, value) -> {
            log.info("构建sql时获取{}类的{}属性值为{}", ntType.getSimpleName(), field.getName(), value);
            return value;
        });
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }
}
