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
import org.rdlinux.ezmybatis.constant.MapRetKeyCasePolicy;
import org.rdlinux.ezmybatis.constant.NameCasePolicy;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisDeleteListener;
import org.rdlinux.ezmybatis.demo.ezlistener.ModelInsertListener;
import org.rdlinux.ezmybatis.demo.ezlistener.ModelUpdateListener;
import org.rdlinux.ezmybatis.demo.ezlistener.QueryRetDecrypt;
import org.rdlinux.ezmybatis.demo.ezlistener.SqlParamEncrypt;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

/**
 * 所有数据库测试的公共基类。
 * 子类只需在自己的 static 块中调用 {@link #initSqlSessionFactory(String, boolean, MapRetKeyCasePolicy, NameCasePolicy, NameCasePolicy)} 即可完成初始化。
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
                                                MapRetKeyCasePolicy mapRetKeyCasePolicy,
                                                NameCasePolicy tableNameCasePolicy,
                                                NameCasePolicy columnNameCasePolicy) {
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
        ezMybatisConfig.setMapRetKeyCasePolicy(mapRetKeyCasePolicy);
        ezMybatisConfig.setTableNameCasePolicy(tableNameCasePolicy);
        ezMybatisConfig.setColumnNameCasePolicy(columnNameCasePolicy);
        EzMybatisContent.init(ezMybatisConfig);

        // 插入监听器
        EzMybatisContent.addInsertListener(ezMybatisConfig, new ModelInsertListener(configuration));

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
        EzMybatisContent.addUpdateListener(ezMybatisConfig, new ModelUpdateListener(configuration));

        // SQL 构建字段获取监听器
        EzMybatisContent.addOnBuildSqlGetFieldListener(ezMybatisConfig, new SqlParamEncrypt());
        EzMybatisContent.addQueryRetListener(ezMybatisConfig, new QueryRetDecrypt(configuration));

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    @Before
    public void setUp() {
        this.sqlSession = sqlSessionFactory.openSession();
    }

    @After
    public void tearDown() {
        if (this.sqlSession != null) {
            this.sqlSession.close();
        }
    }
}
