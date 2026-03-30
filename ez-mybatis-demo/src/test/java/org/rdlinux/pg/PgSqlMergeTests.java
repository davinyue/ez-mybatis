package org.rdlinux.pg;

import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.dao.JdbcInsertDao;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EzQueryTable;
import org.rdlinux.ezmybatis.demo.entity.BaseEntity;
import org.rdlinux.ezmybatis.demo.entity.User;
import org.rdlinux.ezmybatis.demo.entity.UserOrg;
import org.rdlinux.ezmybatis.expand.oracle.update.Merge;

import java.util.Date;
import java.util.UUID;

public class PgSqlMergeTests extends PgSqlBaseTest {
    @Test
    public void mergeMatchedUpdateTest() {
        String oneUserId = this.getOneUserId();
        EntityTable userTable = EntityTable.of(User.class);
        EzQuery<User> useQuery = EzQuery.builder(User.class).from(userTable)
                .select()
                .addAll()
                .done()
                .where()
                .addCondition(userTable.field(BaseEntity.Fields.id), oneUserId)
                .done()
                .build();
        EzQueryTable useTable = EzQueryTable.of(useQuery);
        EntityTable mergeTable = EntityTable.of(User.class);
        User user = new User();
        user.setId(oneUserId);
        user.setName("merge_name_insert_test");
        user.setAge(18);
        user.setSex(User.Sex.MAN);
        Merge merge = Merge.into(mergeTable)
                .using(useTable)
                .on()
                .addCondition(mergeTable.field(BaseEntity.Fields.id), useTable.column("ID"))
                .done()
                .set()
                .add(mergeTable.field(User.Fields.name).set("merge_name_test"))
                .done()
                .whenNotMatchedThenInsert(user)
                .build();
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        mapper.expandUpdate(merge);
        this.sqlSession.commit();
        EzQuery<User> verifyQuery = EzQuery.builder(User.class)
                .from(mergeTable)
                .select()
                .addAll()
                .done()
                .where()
                .addCondition(mergeTable.field(BaseEntity.Fields.id), oneUserId)
                .done()
                .build();
        User updated = mapper.queryOne(verifyQuery);
        Assert.assertNotNull(updated);
        Assert.assertEquals("merge_name_test", updated.getName());
        this.sqlSession.close();
    }

    @Test
    public void mergeNotMatchedInsertTest() {
        String sourceUserId = UUID.randomUUID().toString().replaceAll("-", "");

        JdbcInsertDao jdbcInsertDao = new JdbcInsertDao(this.sqlSession);
        UserOrg userOrg = new UserOrg();
        userOrg.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        userOrg.setUserId(sourceUserId);
        userOrg.setOrgId("ORG-MERGE-INSERT");
        userOrg.setCreateTime(new Date());
        userOrg.setUpdateTime(new Date());
        jdbcInsertDao.insert(userOrg);

        EntityTable userOrgTable = EntityTable.of(UserOrg.class);
        EzQuery<UserOrg> useQuery = EzQuery.builder(UserOrg.class)
                .from(userOrgTable)
                .select()
                .addAll()
                .done()
                .where()
                .addCondition(userOrgTable.field(UserOrg.Fields.userId), sourceUserId)
                .done()
                .build();
        EzQueryTable useTable = EzQueryTable.of(useQuery);
        EntityTable mergeTable = EntityTable.of(User.class);
        User user = new User();
        user.setId(sourceUserId);
        user.setName("merge_name_insert_test");
        user.setAge(18);
        user.setSex(User.Sex.MAN);

        Merge merge = Merge.into(mergeTable)
                .using(useTable)
                .on()
                .addCondition(mergeTable.field(BaseEntity.Fields.id), useTable.column("USER_ID"))
                .done()
                .set()
                .add(mergeTable.field(User.Fields.name).set("merge_name_test"))
                .done()
                .whenNotMatchedThenInsert(user)
                .build();

        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        mapper.expandUpdate(merge);
        this.sqlSession.commit();

        EzQuery<User> verifyQuery = EzQuery.builder(User.class)
                .from(mergeTable)
                .select()
                .addAll()
                .done()
                .where()
                .addCondition(mergeTable.field(BaseEntity.Fields.id), sourceUserId)
                .done()
                .build();
        User inserted = mapper.queryOne(verifyQuery);
        Assert.assertNotNull(inserted);
        Assert.assertEquals(sourceUserId, inserted.getId());
        Assert.assertEquals("merge_name_insert_test", inserted.getName());
        Assert.assertEquals(Integer.valueOf(18), inserted.getAge());
        Assert.assertEquals(User.Sex.MAN, inserted.getSex());
    }
}
