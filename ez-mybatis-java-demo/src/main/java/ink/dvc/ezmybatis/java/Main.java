package ink.dvc.ezmybatis.java;

import ink.dvc.ezmybatis.java.entity.User;
import ink.dvc.ezmybatis.java.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.linuxprobe.luava.json.JacksonUtils;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private static SqlSession sqlSession;

    static {
        String resource = "mybatis-config-oracle.xml";
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

    public static void main(String[] args) {
//        sqlSession.insert("org.rdlinux.ezmybatis.java.mapper.UserMapper.insert", new User());
//        sqlSession.commit();
        //sqlSession.selectList("", "");
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectById("1s");
        System.out.println(JacksonUtils.toJsonString(user));
        List<String> ids = new LinkedList<>();
        ids.add("1s");
        ids.add("3s");
        List<User> users = mapper.selectByIds(ids);
        System.out.println(JacksonUtils.toJsonString(users));
    }
}
