package org.rdlinux.mysql;

import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.demo.entity.User;
import org.rdlinux.ezmybatis.expand.mysql.update.MySqlUpsert;
import org.rdlinux.mssql.MsSqlBaseTest;

import java.util.UUID;

public class MySqlUpsertTest extends MsSqlBaseTest {
    @Test
    public void upsertUpdateTest() {
        String userId = this.getOneUserId();
        EntityTable table = EntityTable.of(User.class);
        User user = new User();
        user.setId(userId);
        user.setName("mysql_upsert_insert_name");
        user.setUserAge(18);
        user.setSex(User.Sex.MAN);
        MySqlUpsert upsert = MySqlUpsert.insert(user)
                .onDuplicateKeyUpdate()
                .set()
                .add(table.field(User.Fields.name).set("mysql_upsert_update_name"))
                .done()
                .build();
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        Integer ret = mapper.expandUpdate(upsert);
        this.sqlSession.commit();
        Assert.assertNotNull(ret);
        User updated = mapper.selectById(User.class, userId);
        Assert.assertNotNull(updated);
        Assert.assertEquals("mysql_upsert_update_name", updated.getName());
    }

    @Test
    public void upsertInsertTest() {
        String userId = UUID.randomUUID().toString().replaceAll("-", "");
        EntityTable table = EntityTable.of(User.class);
        User user = new User();
        user.setId(userId);
        user.setName("mysql_upsert_insert_name");
        user.setUserAge(18);
        user.setSex(User.Sex.MAN);
        MySqlUpsert upsert = MySqlUpsert.into(table)
                .insert(user)
                .onDuplicateKeyUpdate()
                .set()
                .add(table.field(User.Fields.name).set("mysql_upsert_update_name"))
                .done()
                .build();
        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        Integer ret = mapper.expandUpdate(upsert);
        this.sqlSession.commit();
        Assert.assertNotNull(ret);
        User inserted = mapper.selectById(User.class, userId);
        Assert.assertNotNull(inserted);
        Assert.assertEquals("mysql_upsert_insert_name", inserted.getName());
        Assert.assertEquals(Integer.valueOf(18), inserted.getUserAge());
        Assert.assertEquals(User.Sex.MAN, inserted.getSex());
    }
}
