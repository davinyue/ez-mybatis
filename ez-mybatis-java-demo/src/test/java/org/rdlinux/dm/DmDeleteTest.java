package org.rdlinux.dm;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.LinkedList;
import java.util.List;

@Log4j2
public class DmDeleteTest extends DmBaseTest {
    @Test
    public void delete() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int delete = sqlSession.getMapper(UserMapper.class).delete(user);
        System.out.println(delete);
    }

    @Test
    public void batchDelete() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
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
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        int insert = sqlSession.getMapper(UserMapper.class)
                .deleteById("016cdcdd76f94879ab3d24850514812b");
        sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchDeleteById() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
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
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzDelete delete = EzDelete.delete(EntityTable.of(User.class))
                .where().addFieldCondition("id", "56").done()
                .build();
        int ret = sqlSession.getMapper(EzMapper.class).ezDelete(delete);
        sqlSession.commit();
        log.info("删除{}条", ret);
    }

    @Test
    public void batchDeleteByParam() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<EzDelete> deletes = new LinkedList<>();
        EzDelete delete = EzDelete.delete(EntityTable.of(User.class))
                .where().addFieldCondition("id", "56").done()
                .build();
        deletes.add(delete);
        delete = EzDelete.delete(EntityTable.of(User.class))
                .where().addFieldCondition("id", "23").done()
                .build();
        deletes.add(delete);
        sqlSession.getMapper(EzMapper.class).ezBatchDelete(deletes);
        sqlSession.commit();
    }

    @Test
    public void limitDelete() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzDelete ezDelete = EzDelete.delete(DbTable.of("xxx"))
//                .where()
//                .addColumnCondition("xxx", "XX")
//                .done()
                .limit(2)
                .build();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        mapper.ezDelete(ezDelete);
        sqlSession.rollback();
    }
}

