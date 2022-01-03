package ink.dvc.mysql;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.linuxprobe.luava.json.JacksonUtils;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.entity.UserOrg;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Log4j2
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
        User user = sqlSession.getMapper(UserMapper.class).selectById("01e3ff9339f2427d9a66d3a8799de2c9");
        System.out.println(JacksonUtils.toJsonString(user));
        user = sqlSession.getMapper(EzMapper.class).selectById(User.class, "01e3ff9339f2427d9a66d3a8799de2c9");
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectByIds() {
        List<String> ids = new LinkedList<>();
        ids.add("01e3ff9339f2427d9a66d3a8799de2c9");
        ids.add("1");
        List<User> users = sqlSession.getMapper(UserMapper.class).selectByIds(ids);
        System.out.println(JacksonUtils.toJsonString(users));
        users = sqlSession.getMapper(EzMapper.class).selectByIds(User.class, ids);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void selectBySql() {
        HashMap<String, Object> sqlParam = new HashMap<>();
        sqlParam.put("id", "1");
        List<User> users = sqlSession.getMapper(UserMapper.class).selectBySql("select name from ez_user" +
                " WHERE id = #{id}", sqlParam);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void queryTest() {
        EntityTable userTable = EntityTable.of(User.class);
        EzQuery<User> query = EzQuery.builder(User.class).from(userTable)
                .select().addAll().done()
                .join(EntityTable.of(UserOrg.class))
                .addFieldCompareCondition("id", "userId")
                .joinTableCondition()
                .addFieldCondition("orgId", "2")
                .done()
                .page(1, 2)
                .build();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        sqlSession.clearCache();
        users = userMapper.query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        EzMapper ezMapper = sqlSession.getMapper(EzMapper.class);
        users = ezMapper.query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        EzQuery<UserOrg> uosQuery = EzQuery.builder(UserOrg.class).from(EntityTable.of(UserOrg.class))
                .select().addAll().done().build();
        List<UserOrg> uos = ezMapper.query(uosQuery);
        System.out.println(JacksonUtils.toJsonString(uos));
    }

    @Test
    public void dbTableQueryTest() {
        EzQuery<User> query = EzQuery.builder(User.class).from(DbTable.of("ez_user"))
                .select().addAll().done()
                .where().addColumnCondition("id", "4").done()
                .having().addColumnCondition("name", "张三").done()
                .build();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.query(query);
        log.info(JacksonUtils.toJsonString(users));
    }

    @Test
    public void countTest() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().add("name").done()
                .build();
        int i = sqlSession.getMapper(UserMapper.class).queryCount(query);
        System.out.println("总数" + i);
    }

    @Test
    public void countTest1() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().add("name").done()
                //.where().addColumnCondition("name", Operator.gt, 1).done()
                .build();
        int i = sqlSession.getMapper(EzMapper.class).queryCount(query);
        System.out.println("总数" + i);
    }

    @Test
    public void normalQuery() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .build();
        List<User> users = sqlSession.getMapper(EzMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void normalQueryMap() {
        EzQuery<?> query = EzQuery.builder(Map.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .build();
        EzMapper ezMapper = sqlSession.getMapper(EzMapper.class);
        List<Map<String, Object>> users = (List<Map<String, Object>>) ezMapper.query(query);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void normalQueryOne() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().page(1, 1)
                .build();
        User user = sqlSession.getMapper(EzMapper.class).queryOne(query);
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void normalQueryCount() {
        EzQuery<Integer> query = EzQuery.builder(Integer.class).from(EntityTable.of(User.class))
                .select().addCount("id").done().page(1, 1)
                .build();
        Integer count = sqlSession.getMapper(EzMapper.class).queryOne(query);
        System.out.println(JacksonUtils.toJsonString(count));
    }


    @Test
    public void selectOneBySql() {
        User user = sqlSession.getMapper(UserMapper.class).selectOneBySql("select * from ez_user " +
                "where id = '2c50ee58773f468c82013f73c08e7bc8'", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectOneMapBySql() {
        Map<String, Object> user = sqlSession.getMapper(EzMapper.class).selectOneMapBySql(
                "select * from ez_user where id = '1s'", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectMapBySql() {
        List<Map<String, Object>> users = sqlSession.getMapper(EzMapper.class)
                .selectMapBySql("select * from ez_user", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(users));
    }
}
