package org.rdlinux.mysql;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.demo.entity.ComplexUser;
import org.rdlinux.ezmybatis.demo.entity.ExtInfo;
import org.rdlinux.ezmybatis.demo.mapper.ComplexUserMapper;
import org.rdlinux.ezmybatis.expand.mysql.update.MySqlUpsert;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

/**
 * MySQL 环境下 ComplexUser 的 Upsert (INSERT ... ON DUPLICATE KEY UPDATE) 测试。
 */
@Slf4j
public class MySqlComplexEntityUpsertTest extends MySqlBaseTest {

    private static final Faker faker = new Faker(java.util.Locale.CHINA);

    private ComplexUser buildComplexUser() {
        ComplexUser user = new ComplexUser();
        user.setUsername(faker.name().fullName());
        user.setAge(new Random().nextInt(100));
        user.setUserType((short) 1);
        user.setScore(faker.number().randomNumber());
        user.setAccountBalance(faker.number().randomDouble(2, 0, 10000));
        user.setSalary(new BigDecimal(faker.number().randomDouble(2, 3000, 50000)));
        user.setIsActive(new Random().nextBoolean());

        ComplexUser.UserStatus[] statuses = ComplexUser.UserStatus.values();
        user.setStatus(statuses[new Random().nextInt(statuses.length)]);
        user.setBirthday(faker.date().birthday());
        user.setAvatar("binary_avatar_data".getBytes(StandardCharsets.UTF_8));
        user.setDescription(faker.lorem().paragraph());
        user.setSpecificColumn("SpecificCol_Val");
        user.setSecretContent(faker.internet().password());

        ExtInfo extInfo = new ExtInfo();
        extInfo.setHobby(faker.esports().game());
        extInfo.setRemark("Generated for upsert test");
        user.setExtInfo(extInfo);

        user.setDepartmentId(UUID.randomUUID().toString().replaceAll("-", ""));
        return user;
    }

    private String insertAndGetComplexUserId() {
        ComplexUser user = this.buildComplexUser();
        this.sqlSession.getMapper(ComplexUserMapper.class).insert(user);
        this.sqlSession.commit();
        return user.getId();
    }

    /**
     * 测试 Upsert 走 UPDATE 分支：先插入一条记录，再用相同 ID 执行 Upsert，
     * 期望触发 ON DUPLICATE KEY UPDATE 逻辑将 username 更新为指定值。
     */
    @Test
    public void upsertUpdateTest() {
        String userId = this.insertAndGetComplexUserId();
        EntityTable table = EntityTable.of(ComplexUser.class);

        ComplexUser user = this.buildComplexUser();
        user.setId(userId);

        MySqlUpsert upsert = MySqlUpsert.insert(user)
                .onDuplicateKeyUpdate()
                .set()
                .add(table.field(ComplexUser.Fields.username).set("complex_upsert_update_name"))
                .done()
                .build();

        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        Integer ret = mapper.expandUpdate(upsert);
        this.sqlSession.commit();
        Assert.assertNotNull(ret);

        ComplexUser updated = mapper.selectById(ComplexUser.class, userId);
        Assert.assertNotNull(updated);
        Assert.assertEquals("complex_upsert_update_name", updated.getUsername());
        log.info("upsertUpdateTest result: {}", ret);
    }

    /**
     * 测试 Upsert 走 INSERT 分支：使用一个全新的 ID 执行 Upsert，
     * 因为不存在冲突，期望直接插入新记录。
     */
    @Test
    public void upsertInsertTest() {
        EntityTable table = EntityTable.of(ComplexUser.class);

        ComplexUser user = this.buildComplexUser();
        // ID 由框架自动生成，保证唯一性不会触发 duplicate key

        MySqlUpsert upsert = MySqlUpsert.into(table)
                .insert(user)
                .onDuplicateKeyUpdate()
                .set()
                .add(table.field(ComplexUser.Fields.username).set("complex_upsert_update_name"))
                .done()
                .build();

        EzMapper mapper = this.sqlSession.getMapper(EzMapper.class);
        Integer ret = mapper.expandUpdate(upsert);
        this.sqlSession.commit();
        Assert.assertNotNull(ret);

        ComplexUser inserted = mapper.selectById(ComplexUser.class, user.getId());
        Assert.assertNotNull(inserted);
        Assert.assertEquals(user.getUsername(), inserted.getUsername());
        Assert.assertEquals(user.getAge(), inserted.getAge());
        log.info("upsertInsertTest result: {}, insertedId: {}", ret, user.getId());
    }
}
