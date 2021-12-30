package ink.dvc.oracle;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.linuxprobe.luava.json.JacksonUtils;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.java.entity.PayRequest;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.PayVoucherMapper;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

@Log4j2
public class OracleSelectTest {
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
        List<Map<String, Object>> users = sqlSession.getMapper(EzMapper.class)
                .selectMapBySql("select * from \"user\"", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(users));
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
    public void jt() {
        EntityTable userTable = EntityTable.of(PayRequest.class);
        EzQuery<PayRequest> query = EzQuery.builder(PayRequest.class).from(userTable).select().addAll().done()
                .page(1, 1)
                .build();
        PayVoucherMapper userMapper = sqlSession.getMapper(PayVoucherMapper.class);
        List<PayRequest> users = userMapper.query(query);
        log.info(JacksonUtils.toJsonString(users));
    }

    @Test
    public void jt1() {
        String json = "{\"agency_id\":\"691E66E9-C4CA-4696-AA47-6A197F5D7285\",\"agency_code\":\"101001\"," +
                "\"agency_name\":\"云南省人民政府办公厅\",\"bgt_source_id\":\"3BEEAD30-B444-49C3-A8E6-B34F9D8FFDFA\"," +
                "\"bgt_source_code\":\"1\",\"bgt_source_name\":\"年初批复\",\"bgt_year_id\":\"0\"," +
                "\"found_type_id\":\"97EA156A-3EC7-4460-B091-7935A366E891\",\"found_type_code\":\"11\"," +
                "\"found_type_name\":\"年初安排\",\"pro_id\":\"a84d531c626b427ab294fe0c7b3c0abb\"," +
                "\"pro_code\":\"530000210000000023390\",\"pro_name\":\"社会保障缴费\"," +
                "\"dep_bgt_eco_id\":\"645AE958-5159-4B19-83FB-AC93A457C974\",\"dep_bgt_eco_code\":\"30112\"," +
                "\"dep_bgt_eco_name\":\"其他社会保障缴费\",\"exp_func_id\":\"C5B27722-2BA1-47B4-AFAB-D3016BE64886\"," +
                "\"exp_func_code\":\"2010350\",\"exp_func_name\":\"事业运行\"," +
                "\"fund_type_id\":\"283D462C-0D30-40AB-9673-74757C4932B3\",\"fund_type_code\":\"1111\"," +
                "\"fund_type_name\":\"本级财力安排\",\"manage_mof_dep_id\":\"3B000F64-E6C2-4451-97D1-00A4C8A2F2A9\"," +
                "\"manage_mof_dep_code\":\"12\",\"manage_mof_dep_name\":\"行政处\",\"pay_kind_id\":\"1\"," +
                "\"pay_type_id\":\"37330005-28C0-4FE7-942C-CA477DD0153B\",\"pay_type_code\":\"121\"," +
                "\"pay_type_name\":\"财政授权支付\",\"set_mode_id\":\"966AAD33-7DEC-4DFC-9A38-05D0A345E330\"," +
                "\"set_mode_code\":\"1\",\"set_mode_name\":\"电子转账支付\",\"all_met_id\":\"0\"," +
                "\"gov_bgt_eco_id\":\"71CCDF88-878B-4170-A041-19856E41584A\",\"gov_bgt_eco_code\":\"50501\"," +
                "\"gov_bgt_eco_name\":\"工资福利支出\",\"budget_pro_id\":\"429A065F-D5B1-4E88-8024-914D965C7887\"," +
                "\"budget_type_id\":\"0\",\"bgt_type_id\":\"591B1C59-BCCA-4887-A2E2-64A189BEFB93\"," +
                "\"bgt_type_code\":\"21\",\"bgt_type_name\":\"当年预算\"," +
                "\"pro_cat_id\":\"429A065F-D5B1-4E88-8024-914D965C7887\",\"pro_cat_code\":\"112\"," +
                "\"pro_cat_name\":\"社会保障缴费\",\"lqd_receiv_acc_no\":\"9160460017381002\"," +
                "\"lqd_receiv_acc_name\":\"财政性资金垫款\",\"lqd_receiv_bank_no\":\"104731003017\"," +
                "\"lqd_receiv_bank_name\":\"中国银行股份有限公司云南省分行（本部）\"," +
                "\"clear_acct_no\":\"240000000002271001\",\"clear_acct_name\":\"云南省财政厅\",\"clear_acct_bank_no\":\"011731002002\",\"clear_acct_bank_name\":\"中华人民共和国国家金库云南省分库\",\"pay_acct_no\":\"135611872299\",\"pay_acct_name\":\"云南省人民政府办公厅\",\"pay_acct_bank_code\":\"104731003017\",\"pay_acct_bank_name\":\"中国银行股份有限公司云南省分行（本部）\",\"lqd_receiv_acc_id\":\"E1D3A9CD-A3FA-11DE-8728-830C28ABEB67\",\"lqd_receiv_bank_id\":\"530000657\",\"clear_acct_id\":\"1EDA6BC8-5448-11E9-A43E-D34BB51E3C3\",\"clear_acct_bank_id\":\"5300004914\",\"pay_acct_id\":\"6327\",\"pay_acct_bank_id\":\"530000657\",\"payee_acct_name\":\"测试一\",\"payee_acct_no\":\"110120119\",\"pay_app_id\":\"69edb7cf-714d-4104-ae03-0804d1a6841700\",\"plan_id\":\"0\",\"is_final\":0,\"data_type\":0,\"is_valid\":0,\"pay_cert_no\":\"10100111000002\",\"ori_request_id\":\"0\",\"contract_id\":\"0\",\"salary_batch\":0,\"doc_no_name\":\"云财预〔2020〕114号\",\"task_status\":0,\"vt_code\":\"8202\",\"pay_account_notice_id\":0,\"pa_id\":201011,\"pa_code\":\"1\",\"pa_name\":\"正\",\"pay_refund_amount\":0,\"clear_time\":0,\"mon_flag\":0,\"lqd_quota_notice_id\":\"0\",\"pay_bank_kind\":\"104\",\"input_mode\":0,\"is_hang\":0,\"is_secret\":0,\"bill_type_id\":\"9205\",\"create_user\":\"张颖\",\"last_ver\":1626249958348,\"create_date\":\"2021-07-14 16:05:57\",\"n_field1\":0,\"n_field2\":0,\"n_field3\":0,\"n_field4\":0,\"n_field5\":0,\"mon_no_flag\":0,\"mon_no_level\":0,\"pay_app_no\":\"SQ-530000-20210714-00423\",\"bgt_id\":\"B7A41072F5B4B32EE053090BA8C06A0D\",\"use_des\":\"测试第一次\",\"pay_bus_type_id\":\"EE8704DB-6218-493B-A236-5545E7B1B2ED\",\"pay_bus_type_code\":\"1\",\"pay_bus_type_name\":\"普通业务\",\"pay_app_amt\":0.88,\"pay_cert_id\":\"69edb7cf-714d-4104-ae03-0804d1a6841700\",\"payee_acct_bank_name\":\"农行\",\"fund_traobj_type_id\":\"D1EF0A70-1E0A-4260-8EAB-8CF3E6103523\",\"fund_traobj_type_code\":\"21\",\"fund_traobj_type_name\":\"与部门内下级单位\",\"internal_dep_code\":\"0\",\"fiscal_year\":2021,\"mof_div_id\":\"1\",\"mof_div_code\":\"530000000\",\"mof_div_name\":\"云南省\",\"currency_id\":\"eea4d6d26jl04c26b77a638232c9ecd5\",\"currency_code\":\"CNY\",\"currency_name\":\"人民币\",\"est_rat\":0.0,\"update_time\":\"2021-07-14 16:06:29\",\"create_time\":\"2021-07-14 16:05:57\",\"is_deleted\":2}";
        this.jt();

        PayVoucherMapper userMapper = sqlSession.getMapper(PayVoucherMapper.class);
        List<PayRequest> requests = new LinkedList<>();
        for (int i = 0; i < 500; i++) {
            PayRequest request = JacksonUtils.conversion(json, PayRequest.class);
            request.setPay_app_id(UUID.randomUUID().toString().replaceAll("-", ""));
            request.setBgt_id("1234test");
            requests.add(request);
        }
        log.info("插入开始");
        long start = System.currentTimeMillis();
        //int size = 6;
        int size = 50;
        int count = 500 / size;
        if ((500 % size) > 0) {
            count++;
        }
        for (int i = 0; i < count; i++) {
            int startIndex = i * size;
            int endIndex = (i + 1) * size;
            if (endIndex > requests.size()) {
                endIndex = requests.size();
            }
            List<PayRequest> requests1 = requests.subList(startIndex, endIndex);
            userMapper.batchInsert(requests1);
        }
        long end = System.currentTimeMillis();
        log.info("耗时{}", end - start);
        sqlSession.commit();
    }

    @Test
    public void groupTest() {
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().add("name").done()
                .where().addColumnCondition("name", Operator.gt, 1).done()
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
