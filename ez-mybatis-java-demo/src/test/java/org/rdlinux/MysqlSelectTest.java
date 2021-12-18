package org.rdlinux;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.linuxprobe.luava.json.JacksonUtils;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.entity.UserOrg;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MysqlSelectTest {
    public static SqlSession sqlSession;

    static {
        String resource = "mybatis-config.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(reader);
        sqlSession = sqlSessionFactory.openSession();
    }

    @Test
    public void selectById() {
        User user = BaseTest.sqlSession.getMapper(UserMapper.class).selectById("980e1f193035494198f90d24e01d6706");
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectByIds() {
        List<String> ids = new LinkedList<>();
        ids.add("980e1f193035494198f90d24e01d6706");
        ids.add("1s");
        List<User> users = BaseTest.sqlSession.getMapper(UserMapper.class).selectByIds(ids);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void selectBySql() {
        List<User> users = BaseTest.sqlSession.getMapper(UserMapper.class).selectBySql("select * from \"user\"");
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void queryTest() {
        EntityTable userTable = EntityTable.of(User.class);
        EntityTable userOrgTable = EntityTable.of(UserOrg.class);
        EzQuery<User> query = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .join(userOrgTable)
                .conditions().add("id", "userId")
                .add(userOrgTable, "orgId", 2)
                .done().done()
                .where().conditions().add(userTable, "userAge", Operator.gt, 20).done().done()
                .orderBy().add("name").done()
                .page(1, 2)
                .build();
        List<User> users = sqlSession.getMapper(UserMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        int i = sqlSession.getMapper(UserMapper.class).queryCount(query);
        System.out.println("总数" + i);
    }

    @Test
    public void groupTest() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().add("name").done()
                .where().conditions().addColumn("name", Operator.gt, 1).done().done()
                //.groupBy().add("name").done()
                //.having().conditions().add("name", Operator.more, 1).done().done()
                //.orderBy().add("name").done()
                .page(2, 5)
                .build();
        List<User> users = BaseTest.sqlSession.getMapper(UserMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        int i = BaseTest.sqlSession.getMapper(UserMapper.class).queryCount(query);
        System.out.println("总数" + i);
    }

    @Test
    public void normalQuery() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .build();
        List<User> users = BaseTest.sqlSession.getMapper(EzMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void normalQueryMap() {
        EzQuery<?> query = EzQuery.builder(Map.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .build();
        EzMapper ezMapper = BaseTest.sqlSession.getMapper(EzMapper.class);
        List<Map<String, Object>> users = (List<Map<String, Object>>) ezMapper.query(query);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void normalQueryOne() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().page(1, 1)
                .build();
        User user = BaseTest.sqlSession.getMapper(EzMapper.class).queryOne(query);
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void normalQueryCount() {
        EzQuery<Integer> query = EzQuery.builder(Integer.class).from(EntityTable.of(User.class))
                .select().addCount("id").done().page(1, 1)
                .build();
        int count = BaseTest.sqlSession.getMapper(EzMapper.class).queryOne(query);
        System.out.println(JacksonUtils.toJsonString(count));
    }


    @Test
    public void selectOneBySql() {
        User user = BaseTest.sqlSession.getMapper(UserMapper.class).selectOneBySql("select * from ez_user " +
                "where id = '2c50ee58773f468c82013f73c08e7bc8'");
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectOneMapBySql() {
        Map<String, Object> user = BaseTest.sqlSession.getMapper(EzMapper.class).selectOneMapBySql(
                "select * from ez_user where id = '1s'");
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectMapBySql() {
        List<Map<String, Object>> users = BaseTest.sqlSession.getMapper(EzMapper.class)
                .selectMapBySql("select * from ez_user");
        System.out.println(JacksonUtils.toJsonString(users));
    }
}
