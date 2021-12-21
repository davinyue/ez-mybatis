package ink.dvc.oracle;

import ink.dvc.ezmybatis.core.EzDelete;
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
public class OracleDeleteTest {

    public static SqlSession sqlSession;

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

    @Test
    public void delete() {
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int delete = sqlSession.getMapper(UserMapper.class).delete(user);
        System.out.println(delete);
    }

    @Test
    public void batchDelete() {
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setId("016cdcdd76f94879ab3d24850514812b");
            users.add(user);
        }
        int insert = sqlSession.getMapper(UserMapper.class).batchDelete(users);
        sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void deleteById() {
        int insert = sqlSession.getMapper(UserMapper.class)
                .deleteById("016cdcdd76f94879ab3d24850514812b");
        sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchDeleteById() {
        List<String> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            users.add("016cdcdd76f94879ab3d24850514812b");
        }
        int insert = sqlSession.getMapper(UserMapper.class).batchDeleteById(users);
        sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void deleteByParam() {
        EzDelete delete = EzDelete.delete(EntityTable.of(User.class))
                .where().conditions().add("id", "56").done().done()
                .build();
        int ret = sqlSession.getMapper(EzMapper.class).delete(delete);
        sqlSession.commit();
        log.info("删除{}条", ret);
    }

    @Test
    public void batchDeleteByParam() {
        List<EzDelete> deletes = new LinkedList<>();
        EzDelete delete = EzDelete.delete(EntityTable.of(User.class))
                .where().conditions().add("id", "56").done().done()
                .build();
        deletes.add(delete);
        delete = EzDelete.delete(EntityTable.of(User.class))
                .where().conditions().add("id", "23").done().done()
                .build();
        deletes.add(delete);
        sqlSession.getMapper(EzMapper.class).batchDelete(deletes);
        sqlSession.commit();
    }
}
