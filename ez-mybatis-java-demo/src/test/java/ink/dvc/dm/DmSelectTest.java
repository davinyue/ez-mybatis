package ink.dvc.dm;

import ink.dvc.ezmybatis.core.EzQuery;
import ink.dvc.ezmybatis.core.mapper.EzMapper;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.java.entity.User;
import ink.dvc.ezmybatis.java.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.linuxprobe.luava.json.JacksonUtils;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Log4j2
public class DmSelectTest {
    public static SqlSession sqlSession;

    static {
        String resource = "mybatis-config-dm.xml";
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
        User user = sqlSession.getMapper(UserMapper.class).selectById("1");
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectByIds() {
        List<String> ids = new LinkedList<>();
        ids.add("980e1f193035494198f90d24e01d6706");
        ids.add("1s");
        List<User> users = sqlSession.getMapper(UserMapper.class).selectByIds(ids);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void selectBySql() {
        List<User> users = sqlSession.getMapper(UserMapper.class).selectBySql("select * from \"user\"", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void selectMapBySql() {
        EzMapper ezMapper = sqlSession.getMapper(EzMapper.class);
        List<Map<String, Object>> maps = ezMapper.selectMapBySql("SELECT banner as 版本信息 FROM v$version",
                new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(maps));
    }

    @Test
    public void queryTest() {
        EntityTable userTable = EntityTable.of(User.class);
        EzQuery<User> query = EzQuery.builder(User.class).from(userTable)
                .select().add("name").done()
                .groupBy().add("name").done()
                .page(1, 2)
                .build();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.query(query);
        log.info(JacksonUtils.toJsonString(users));
        int i = userMapper.queryCount(query);
        log.info("总数" + i);
    }

    @Test
    public void groupTest() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().add("name").done()
                .where().addColumn("name", Operator.gt, 1).done()
                //.groupBy().add("name").done()
                //.having().conditions().add("name", Operator.more, 1).done().done()
                //.orderBy().add("name").done()
                .page(2, 5)
                .build();
        List<User> users = sqlSession.getMapper(UserMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        int i = sqlSession.getMapper(UserMapper.class).queryCount(query);
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
        int count = sqlSession.getMapper(EzMapper.class).queryOne(query);
        System.out.println(JacksonUtils.toJsonString(count));
    }

    @Test
    public void selectOneBySql() {
        User user = sqlSession.getMapper(UserMapper.class).selectOneBySql("select * from \"user\" " +
                "where id = '2c50ee58773f468c82013f73c08e7bc8'", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectOneMapBySql() {
        Map<String, Object> user = sqlSession.getMapper(EzMapper.class).selectOneMapBySql(
                "select * from \"user\" " +
                        "where id = '1s'", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(user));
    }
}
