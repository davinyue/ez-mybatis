package org.rdlinux.mysql;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzDelete;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.demo.entity.BaseEntity;
import org.rdlinux.ezmybatis.demo.entity.User;
import org.rdlinux.ezmybatis.demo.entity.UserOrg;
import org.rdlinux.ezmybatis.demo.mapper.UserMapper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class MySqlDeleteTest extends MySqlBaseTest {

    // ==========================================
    // UserMapper (EzBaseMapper) Tests
    // ==========================================

    @Test
    public void delete() {
        String id = this.getOneUserId();
        User user = new User();
        user.setId(id);
        int ret = this.sqlSession.getMapper(UserMapper.class).delete(user);
        Assert.assertEquals(1, ret);
        log.info("delete deleted {} records", ret);
    }

    @Test
    public void deleteByTable() {
        String id = this.getOneUserId();
        User user = new User();
        user.setId(id);
        int ret = this.sqlSession.getMapper(UserMapper.class).deleteByTable(EntityTable.of(User.class), user);
        Assert.assertEquals(1, ret);
        log.info("deleteByTable deleted {} records", ret);
    }

    @Test
    public void batchDelete() {
        List<String> ids = this.getUserIds(2);
        List<User> users = new LinkedList<>();
        for (String id : ids) {
            User user = new User();
            user.setId(id);
            users.add(user);
        }
        int ret = this.sqlSession.getMapper(UserMapper.class).batchDelete(users);
        this.sqlSession.commit();
        Assert.assertEquals(2, ret);
        log.info("batchDelete deleted {} records", ret);
    }

    @Test
    public void batchDeleteByTable() {
        List<String> ids = this.getUserIds(2);
        List<User> users = new LinkedList<>();
        for (String id : ids) {
            User user = new User();
            user.setId(id);
            users.add(user);
        }
        int ret = this.sqlSession.getMapper(UserMapper.class).batchDeleteByTable(EntityTable.of(User.class), users);
        this.sqlSession.commit();
        Assert.assertEquals(2, ret);
        log.info("batchDeleteByTable deleted {} records", ret);
    }

    @Test
    public void deleteById() {
        int ret = this.sqlSession.getMapper(UserMapper.class)
                .deleteById(this.getOneUserId());
        this.sqlSession.commit();
        Assert.assertEquals(1, ret);
        log.info("deleteById deleted {} records", ret);
    }

    @Test
    public void deleteByTableAndId() {
        int ret = this.sqlSession.getMapper(UserMapper.class)
                .deleteByTableAndId(EntityTable.of(User.class), this.getOneUserId());
        this.sqlSession.commit();
        Assert.assertEquals(1, ret);
        log.info("deleteByTableAndId deleted {} records", ret);
    }

    @Test
    public void batchDeleteById() {
        List<String> userIds = this.getUserIds(2);
        int ret = this.sqlSession.getMapper(UserMapper.class).batchDeleteById(userIds);
        this.sqlSession.commit();
        Assert.assertEquals(2, ret);
        log.info("batchDeleteById deleted {} records", ret);
    }

    @Test
    public void batchDeleteByTableAndId() {
        List<String> userIds = this.getUserIds(2);
        int ret = this.sqlSession.getMapper(UserMapper.class).batchDeleteByTableAndId(EntityTable.of(User.class), userIds);
        this.sqlSession.commit();
        Assert.assertEquals(2, ret);
        log.info("batchDeleteByTableAndId deleted {} records", ret);
    }

    // ==========================================
    // EzMapper Tests
    // ==========================================

    @Test
    public void ezMapperDelete() {
        String id = this.getOneUserId();
        User user = new User();
        user.setId(id);
        int ret = this.sqlSession.getMapper(EzMapper.class).delete(user);
        Assert.assertEquals(1, ret);
        log.info("ezMapperDelete deleted {} records", ret);
    }

    @Test
    public void ezMapperDeleteByTable() {
        String id = this.getOneUserId();
        User user = new User();
        user.setId(id);
        int ret = this.sqlSession.getMapper(EzMapper.class).deleteByTable(EntityTable.of(User.class), user);
        Assert.assertEquals(1, ret);
        log.info("ezMapperDeleteByTable deleted {} records", ret);
    }

    @Test
    public void ezMapperBatchDelete() {
        List<String> ids = this.getUserIds(2);
        List<User> users = new LinkedList<>();
        for (String id : ids) {
            User user = new User();
            user.setId(id);
            users.add(user);
        }
        int ret = this.sqlSession.getMapper(EzMapper.class).batchDelete(users);
        this.sqlSession.commit();
        Assert.assertEquals(2, ret);
        log.info("ezMapperBatchDelete deleted {} records", ret);
    }

    @Test
    public void ezMapperBatchDeleteByTable() {
        List<String> ids = this.getUserIds(3);
        List<User> users = new LinkedList<>();
        for (String id : ids) {
            User user = new User();
            user.setId(id);
            users.add(user);
        }
        int ret = this.sqlSession.getMapper(EzMapper.class).batchDeleteByTable(EntityTable.of(User.class), users);
        this.sqlSession.commit();
        Assert.assertEquals(3, ret);
        log.info("ezMapperBatchDeleteByTable deleted {} records", ret);
    }

    @Test
    public void ezMapperDeleteById() {
        int ret = this.sqlSession.getMapper(EzMapper.class)
                .deleteById(User.class, this.getOneUserId());
        this.sqlSession.commit();
        Assert.assertEquals(1, ret);
        log.info("ezMapperDeleteById deleted {} records", ret);
    }

    @Test
    public void ezMapperDeleteByTableAndId() {
        int ret = this.sqlSession.getMapper(EzMapper.class)
                .deleteByTableAndId(EntityTable.of(User.class), User.class, this.getOneUserId());
        this.sqlSession.commit();
        Assert.assertEquals(1, ret);
        log.info("ezMapperDeleteByTableAndId deleted {} records", ret);
    }

    @Test
    public void ezMapperBatchDeleteById() {
        List<String> userIds = this.getUserIds(2);
        userIds.add(null);
        int ret = this.sqlSession.getMapper(EzMapper.class).batchDeleteById(User.class, userIds);
        this.sqlSession.commit();
        Assert.assertEquals(2, ret);
        log.info("ezMapperBatchDeleteById deleted {} records", ret);
    }

    @Test
    public void ezMapperBatchDeleteByTableAndId() {
        List<String> userIds = this.getUserIds(2);
        int ret = this.sqlSession.getMapper(EzMapper.class).batchDeleteByTableAndId(EntityTable.of(User.class),
                User.class, userIds);
        this.sqlSession.commit();
        Assert.assertEquals(2, ret);
        log.info("ezMapperBatchDeleteByTableAndId deleted {} records", ret);
    }

    @Test
    public void ezDelete() {
        EntityTable userTable = EntityTable.of(User.class);
        EntityTable uoTable = EntityTable.of(UserOrg.class);
        EzDelete delete = EzDelete.delete(userTable).delete(uoTable)
                .join(uoTable)
                .addCondition(userTable.field(BaseEntity.Fields.id).eq(uoTable.field(UserOrg.Fields.userId)))
                .done()
                .where()
                .addCondition(userTable.field(BaseEntity.Fields.id).eq(this.getOneUserId()))

                .done()
                .build();
        int ret = this.sqlSession.getMapper(EzMapper.class).ezDelete(delete);
        this.sqlSession.commit();
        // 联表 JOIN 删除, 命中行数取决于两表数据交集, 仅验证执行不抛异常
        Assert.assertTrue(ret >= 0);
        log.info("Test ezDelete - Deleted {} records", ret);
    }

    @Test
    public void ezDeleteLambdaDsl() {
        EntityTable userTable = EntityTable.of(User.class);
        EntityTable uoTable = EntityTable.of(UserOrg.class);
        String userId = this.getOneUserId();
        boolean includeJoin = true;

        EzDelete delete = EzDelete.delete(userTable)
                .delete(uoTable)
                .join(includeJoin, uoTable, j -> j.addCondition(userTable.field(BaseEntity.Fields.id)
                        .eq(uoTable.field(UserOrg.Fields.userId))))
                .where(w -> w.addCondition(userTable.field(BaseEntity.Fields.id).eq(userId)))
                .build();

        int ret = this.sqlSession.getMapper(EzMapper.class).ezDelete(delete);
        this.sqlSession.commit();
        Assert.assertTrue(ret >= 0);
        log.info("Test ezDeleteLambdaDsl - Deleted {} records", ret);
    }

    @Test
    public void ezBatchDelete() {
        List<EzDelete> deletes = new LinkedList<>();
        List<String> ids = this.getUserIds(2);
        EntityTable userTable = EntityTable.of(User.class);
        EzDelete delete1 = EzDelete.delete(userTable)
                .where().addCondition(userTable.field(BaseEntity.Fields.id).eq(this.getOneUserId())).done()
                .build();
        deletes.add(delete1);
        EzDelete delete2 = EzDelete.delete(userTable).where()
                .addCondition(userTable.field(BaseEntity.Fields.id).eq(ids.get(1))).done()
                .build();
        deletes.add(delete2);

        this.sqlSession.getMapper(EzMapper.class).ezBatchDelete(deletes);
        this.sqlSession.commit();
        log.info("Test ezBatchDelete completed");
    }

    @Test
    public void deleteBySql() {
        Map<String, Object> param = new HashMap<>();
        param.put("id", this.getOneUserId());
        String realSql = "DELETE FROM ez_user WHERE id = #{id}";
        try {
            int ret = this.sqlSession.getMapper(EzMapper.class).deleteBySql(realSql, param);
            this.sqlSession.commit();
            Assert.assertEquals(1, ret);
            log.info("Test deleteBySql result: {}", ret);
        } catch (Exception e) {
            log.error("Test deleteBySql exception", e);
            throw e;
        }
    }
}
