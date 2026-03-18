package org.rdlinux.dm;

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
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.rdlinux.ezmybatis.demo.entity.BaseEntity;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Date;

@Slf4j
public class DmBaseTest {
    protected static SqlSessionFactory sqlSessionFactory;

    static {
        String resource = "mybatis-config-dm.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XMLConfigBuilder parser = new XMLConfigBuilder(reader, null, null);
        Configuration configuration = parser.parse();
        EzMybatisConfig ezMybatisConfig = new EzMybatisConfig(configuration);
        ezMybatisConfig.setEscapeKeyword(false);
        EzMybatisContent.init(ezMybatisConfig);
        EzMybatisContent.addInsertListener(ezMybatisConfig, new EzMybatisInsertListener() {
            @Override
            public void onInsert(Object entity) {
                if (entity instanceof BaseEntity) {
                    DmBaseTest.log.info("插入事件");
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
                DmBaseTest.log.info("删除事件");
            }

            @Override
            public void onBatchDelete(Collection<Object> models) {
                models.forEach(this::onDelete);
            }

            @Override
            public void onDeleteById(Object id, Class<?> ntClass) {
                DmBaseTest.log.info("删除事件");
            }

            @Override
            public void onBatchDeleteById(Collection<Object> ids, Class<?> ntClass) {
                for (Object id : ids) {
                    this.onDeleteById(id, ntClass);
                }
            }

            @Override
            public void onEzDelete(EzDelete ezDelete) {
                DmBaseTest.log.info("ez_delete删除:{}", ezDelete.getTable().getTableName(configuration));
            }
        });
        EzMybatisContent.addUpdateListener(ezMybatisConfig, new EzMybatisUpdateListener() {
            @Override
            public void onUpdate(Object entity) {
                DmBaseTest.log.info("更新事件");
            }

            @Override
            public void onBatchUpdate(Collection<?> models) {
                DmBaseTest.log.info("更新事件");
            }

            @Override
            public void onReplace(Object entity) {
                DmBaseTest.log.info("替换事件");
            }

            @Override
            public void onBatchReplace(Collection<?> models) {
                DmBaseTest.log.info("替换事件");
            }

            @Override
            public void onEzUpdate(EzUpdate ezUpdate) {
                DmBaseTest.log.info("ezUpdate事件");
            }

            @Override
            public void onEzBatchUpdate(Collection<EzUpdate> ezUpdates) {
                ezUpdates.forEach(this::onEzUpdate);
            }
        });
        EzMybatisContent.addOnBuildSqlGetFieldListener(ezMybatisConfig, (originObj, ntType, field, value) -> {
            log.info("构建sql时获取{}类的{}属性值为{}", ntType.getSimpleName(), field.getName(), value);
            return value;
        });
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }
}
