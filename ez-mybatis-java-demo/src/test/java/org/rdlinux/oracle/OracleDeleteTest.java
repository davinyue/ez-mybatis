package org.rdlinux.oracle;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.util.LinkedList;
import java.util.List;

@Log4j2
public class OracleDeleteTest extends OracleBaseTest {

    @Test
    public void delete() {
        User user = new User();
        user.setId("016cdcdd76f94879ab3d24850514812b");
        int delete = OracleBaseTest.sqlSession.getMapper(UserMapper.class).delete(user);
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
        int insert = OracleBaseTest.sqlSession.getMapper(UserMapper.class).batchDelete(users);
        OracleBaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void deleteById() {
        int insert = OracleBaseTest.sqlSession.getMapper(UserMapper.class)
                .deleteById("016cdcdd76f94879ab3d24850514812b");
        OracleBaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void batchDeleteById() {
        List<String> users = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            users.add("016cdcdd76f94879ab3d24850514812b");
        }
        int insert = OracleBaseTest.sqlSession.getMapper(UserMapper.class).batchDeleteById(users);
        OracleBaseTest.sqlSession.commit();
        System.out.println(insert);
    }

    @Test
    public void deleteByParam() {
        EzDelete delete = EzDelete.delete(EntityTable.of(User.class))
                .where().addFieldCondition("id", "56").done()
                .build();
        int ret = OracleBaseTest.sqlSession.getMapper(EzMapper.class).ezDelete(delete);
        OracleBaseTest.sqlSession.commit();
        log.info("删除{}条", ret);
    }

    @Test
    public void batchDeleteByParam() {
        List<EzDelete> deletes = new LinkedList<>();
        EzDelete delete = EzDelete.delete(EntityTable.of(User.class))
                .where().addFieldCondition("id", "56").done()
                .build();
        deletes.add(delete);
        delete = EzDelete.delete(EntityTable.of(User.class))
                .where().addFieldCondition("id", "23").done()
                .build();
        deletes.add(delete);
        OracleBaseTest.sqlSession.getMapper(EzMapper.class).ezBatchDelete(deletes);
        OracleBaseTest.sqlSession.commit();
    }
}
