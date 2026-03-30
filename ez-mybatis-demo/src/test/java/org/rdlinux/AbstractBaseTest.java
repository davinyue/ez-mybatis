package org.rdlinux;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.MapRetKeyPattern;
import org.rdlinux.ezmybatis.constant.TableNamePattern;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.EzUpdate;
import org.rdlinux.ezmybatis.core.dao.JdbcInsertDao;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisDeleteListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisUpdateListener;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.ObjArg;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;
import org.rdlinux.ezmybatis.demo.entity.BaseEntity;
import org.rdlinux.ezmybatis.demo.entity.User;
import org.rdlinux.ezmybatis.demo.entity.UserOrg;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * 所有数据库测试的公共基类。
 * 子类只需在自己的 static 块中调用 {@link #initSqlSessionFactory(String, boolean)} 即可完成初始化。
 */
@Slf4j
public abstract class AbstractBaseTest {

    protected static SqlSessionFactory sqlSessionFactory;

    protected SqlSession sqlSession;

    /**
     * 初始化 SqlSessionFactory 并注册通用监听器。
     *
     * @param configResource MyBatis 配置文件资源路径
     * @param escapeKeyword  是否转义关键字
     */
    protected static void initSqlSessionFactory(String configResource, boolean escapeKeyword,
                                                MapRetKeyPattern mapRetKeyPattern,
                                                TableNamePattern tableNamePattern) {
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(configResource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        XMLConfigBuilder parser = new XMLConfigBuilder(reader, null, null);
        Configuration configuration = parser.parse();
        EzMybatisConfig ezMybatisConfig = new EzMybatisConfig(configuration);
        ezMybatisConfig.setEscapeKeyword(escapeKeyword);
        ezMybatisConfig.setMapRetKeyPattern(mapRetKeyPattern);
        ezMybatisConfig.setTableNamePattern(tableNamePattern);
        EzMybatisContent.init(ezMybatisConfig);

        // 插入监听器
        EzMybatisContent.addInsertListener(ezMybatisConfig, new EzMybatisInsertListener() {
            @Override
            public void onInsert(Object entity) {
                if (entity instanceof BaseEntity) {
                    AbstractBaseTest.log.info("插入事件");
                    ((BaseEntity) entity).setUpdateTime(new Date());
                    ((BaseEntity) entity).setCreateTime(new Date());
                }
            }

            @Override
            public void onBatchInsert(Collection<?> models) {
                models.forEach(this::onInsert);
            }
        });

        // 删除监听器
        EzMybatisContent.addDeleteListener(ezMybatisConfig, new EzMybatisDeleteListener() {
            @Override
            public void onDelete(Object entity) {
                AbstractBaseTest.log.info("删除事件");
            }

            @Override
            public void onBatchDelete(Collection<Object> models) {
                models.forEach(this::onDelete);
            }

            @Override
            public void onDeleteById(Object id, Class<?> ntClass) {
                AbstractBaseTest.log.info("删除事件");
            }

            @Override
            public void onBatchDeleteById(Collection<Object> ids, Class<?> ntClass) {
                for (Object id : ids) {
                    this.onDeleteById(id, ntClass);
                }
            }

            @Override
            public void onEzDelete(EzDelete ezDelete) {
                AbstractBaseTest.log.info("ez_delete删除:{}", ezDelete.getTable().getTableName(configuration));
            }
        });

        // 更新监听器
        EzMybatisContent.addUpdateListener(ezMybatisConfig, new EzMybatisUpdateListener() {
            @Override
            public void onUpdate(Object entity) {
                AbstractBaseTest.log.info("更新事件");
                if (entity instanceof BaseEntity) {
                    ((BaseEntity) entity).setUpdateTime(new Date());
                }
            }

            @Override
            public void onBatchUpdate(Collection<?> models) {
                models.forEach(this::onUpdate);
            }

            @Override
            public void onReplace(Object entity) {
                AbstractBaseTest.log.info("替换事件");
                if (entity instanceof BaseEntity) {
                    ((BaseEntity) entity).setCreateTime(new Date());
                    ((BaseEntity) entity).setUpdateTime(new Date());
                }
            }

            @Override
            public void onBatchReplace(Collection<?> models) {
                models.forEach(this::onReplace);
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

        // SQL 构建字段获取监听器
        EzMybatisContent.addOnBuildSqlGetFieldListener(ezMybatisConfig, (originObj, ntType, field, value) -> {
            log.info("构建sql时获取{}类的{}属性值为{}", ntType.getSimpleName(), field.getName(), value);
            return value;
        });

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    @Before
    public void setUp() {
        this.sqlSession = sqlSessionFactory.openSession();
        this.ensureData();
    }

    @After
    public void tearDown() {
        if (this.sqlSession != null) {
            this.sqlSession.close();
        }
    }

    protected String getOneUserId() {
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        EzQuery<User> query = EzQuery.builder(User.class)
                .from(EntityTable.of(User.class))
                .select().addAll().done().limit(1).build();
        User user = mapper.queryOne(query);
        return user != null ? user.getId() : "0";
    }

    protected List<String> getUserIds(int limit) {
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        EzQuery<User> query = EzQuery.builder(User.class)
                .from(EntityTable.of(User.class))
                .select().addAll().done().limit(limit).build();
        List<User> users = mapper.query(query);
        List<String> ids = new ArrayList<>();
        for (User user : users) {
            ids.add(user.getId());
        }
        return ids;
    }

    protected void ensureData() {
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        EzDelete deleteUser = EzDelete.delete(EntityTable.of(User.class)).build();
        mapper.ezDelete(deleteUser);

        EzDelete deleteOrg = EzDelete.delete(EntityTable.of(UserOrg.class)).build();
        mapper.ezDelete(deleteOrg);

        JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(this.sqlSession);
        // 插入足够多元的数据以满足严谨断言
        User u1 = new User();
        u1.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        u1.setName("TestUser1");
        u1.setAge(18);
        u1.setSex(User.Sex.MAN);
        u1.setCreateTime(new Date());
        u1.setUpdateTime(new Date());

        User u2 = new User();
        u2.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        u2.setName("TestUser2");
        u2.setAge(20);
        u2.setSex(User.Sex.WOMAN);
        u2.setCreateTime(new Date());
        u2.setUpdateTime(new Date());

        User u3 = new User();
        u3.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        u3.setName("TestUser3");
        u3.setAge(30);
        u3.setSex(User.Sex.MAN);
        u3.setCreateTime(new Date());
        u3.setUpdateTime(new Date());

        UserOrg uo1 = new UserOrg();
        uo1.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        uo1.setUserId(u1.getId());
        uo1.setOrgId("ORG-1001");
        uo1.setCreateTime(new Date());
        uo1.setUpdateTime(new Date());

        UserOrg uo2 = new UserOrg();
        uo2.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        uo2.setUserId(u2.getId());
        uo2.setOrgId("ORG-1002");
        uo2.setCreateTime(new Date());
        uo2.setUpdateTime(new Date());

        jdbcInsertDao.insert(u1);
        jdbcInsertDao.insert(u2);
        jdbcInsertDao.insert(u3);
        jdbcInsertDao.insert(uo1);
        jdbcInsertDao.insert(uo2);
        this.sqlSession.commit();
    }
}
