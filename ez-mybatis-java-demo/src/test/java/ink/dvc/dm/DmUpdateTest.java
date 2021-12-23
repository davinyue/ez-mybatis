package ink.dvc.dm;

import ink.dvc.ezmybatis.core.EzUpdate;
import ink.dvc.ezmybatis.core.mapper.EzMapper;
import ink.dvc.ezmybatis.core.sqlstruct.table.EntityTable;
import ink.dvc.ezmybatis.java.entity.User;
import ink.dvc.ezmybatis.java.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

@Log4j2
public class DmUpdateTest {
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
    public void update() {
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        user.setName("王");
        user.setUserAge(27);
        user.setSex(User.Sex.MAN);
        int insert = sqlSession.getMapper(UserMapper.class).update(user);
        sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchUpdate() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b");
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setUserAge(i);
            }
            users.add(user);
        }
        int insert = sqlSession.getMapper(UserMapper.class).batchUpdate(users);
        sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void replace() {
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        user.setName("王二");
        int insert = sqlSession.getMapper(UserMapper.class).replace(user);
        sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchReplace() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b");
            user.setName("芳" + i + 1);
            if (i == 0) {
                user.setSex(User.Sex.MAN);
            } else {
                user.setUserAge(i);
            }
            users.add(user);
        }
        int insert = sqlSession.getMapper(UserMapper.class).batchReplace(users);
        sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void updateByEzParam() {
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class)).set("userAge", 1)
                .where().addFieldCondition("id", "1").done()
                .build();
        int ret = mapper.update(ezUpdate);
        sqlSession.commit();
        log.info("更新条数{}", ret);
    }

    @Test
    public void batchUpdateByEzParam() {
        List<EzUpdate> updates = new LinkedList<>();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        EzUpdate ezUpdate = EzUpdate.update(EntityTable.of(User.class)).set("userAge", 1)
                .where().addFieldCondition("id", "1").done()
                .build();
        updates.add(ezUpdate);
        ezUpdate = EzUpdate.update(EntityTable.of(User.class)).set("userAge", 2)
                .where().addFieldCondition("id", "2").done()
                .build();
        updates.add(ezUpdate);
        mapper.batchUpdate(updates);
        sqlSession.commit();
    }
}
