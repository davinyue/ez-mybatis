package ink.dvc.oracle;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.linuxprobe.luava.json.JacksonUtils;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.java.entity.PayRequest;
import org.rdlinux.ezmybatis.java.mapper.PayVoucherMapper;
import org.rdlinux.ezmybatis.java.oracle.OracleDatasourceInit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Slf4j
public class OraclePerformanceTest extends OracleBaseTest {
    private static final String sql = "insert into PAY_VOUCHER (PAY_APP_ID, PAY_APP_NO, CREATE_DATE, AGENCY_CODE, " +
            "BGT_TYPE_CODE, FUND_TYPE_CODE, EXP_FUNC_CODE, GOV_BGT_ECO_CODE, DEP_BGT_ECO_CODE, PRO_CODE, BGT_ID, " +
            "SET_MODE_CODE, PAY_TYPE_CODE, USE_DES, PAY_BUS_TYPE_CODE, PAY_APP_AMT, PAY_CERT_ID, PAY_ACCT_NAME, " +
            "PAY_ACCT_NO, PAY_ACCT_BANK_NAME, PAYEE_ACCT_NAME, PAYEE_ACCT_NO, PAYEE_ACCT_BANK_NAME, " +
            "FUND_TRAOBJ_TYPE_CODE, INTERNAL_DEP_CODE, FISCAL_YEAR, MOF_DIV_CODE, FOREIGN_AMT, CURRENCY_CODE, " +
            "EST_RAT, RECEIVER_CODE, CONTRACT_NO, UPDATE_TIME, IS_DELETED, CREATE_TIME, AGENCY_ID, AGENCY_NAME, " +
            "BGT_TYPE_ID, BGT_TYPE_NAME, FUND_TYPE_ID, FUND_TYPE_NAME, EXP_FUNC_NAME, GOV_BGT_ECO_ID, " +
            "GOV_BGT_ECO_NAME, DEP_BGT_ECO_ID, DEP_BGT_ECO_NAME, PRO_ID, PRO_NAME, SET_MODE_ID, SET_MODE_NAME, " +
            "PAY_TYPE_ID, PAY_TYPE_NAME, PAY_BUS_TYPE_NAME, PAY_CERT_NO, PAY_ACCT_ID, PAY_ACCT_BANK_ID, " +
            "PAY_ACCT_BANK_CODE, PAYEE_ACCT_ID, PAYEE_ACCT_BANK_ID, PAYEE_ACCT_BANK_CODE, FUND_TRAOBJ_TYPE_NAME, " +
            "INTERNAL_DEP_NAME, MOF_DIV_NAME, CURRENCY_NAME, PAY_REFUND_AMOUNT, PAY_DATE, CLEAR_DATE, CLEAR_ACCT_ID, " +
            "CLEAR_ACCT_BANK_ID, MON_FLAG, TASK_STATUS, AUDIT_REMARK, CREATE_USER, LAST_VER, BGT_SOURCE_ID, " +
            "BGT_YEAR_ID, ALL_MET_ID, PAYEE_ACCT_BANK_NO, BUDGET_PRO_ID, BGT_PURPOSE_ID, BUDGET_TYPE_ID, " +
            "OLD_AGENCY_CODE, IS_FINAL, IS_HANG, SALARY_BATCH, PAYEE_BANK_KIND, PAY_BANK_KIND, INPUT_MODE, " +
            "CLEAR_TIME, DOC_NO_NAME, LQD_CERT_ID, PAY_TRANSFER_NOTE_ID, CARD_SUBSIDY_AMOUNT, MON_NO_FLAG, " +
            "MON_NO_LEVEL, MON_NO_REMARK, INTERNAL_DEPARTMENT, LQD_QUOTA_NOTICE_ID, IS_SECRET, CHECK_NO, DATA_TYPE, " +
            "ORI_SPLIT_ID, PAY_ACCOUNT_NOTICE_ID, PAY_CORRECT_REQUEST_ID, PA_CODE, PA_NAME, VT_CODE, N_FIELD1, " +
            "N_FIELD2, N_FIELD3, N_FIELD4, N_FIELD5, C_FIELD1, C_FIELD2, C_FIELD3, C_FIELD4, C_FIELD5, D_FIELD1, " +
            "D_FIELD2, D_FIELD3, D_FIELD4, D_FIELD5, PAY_ELE1_ID, PAY_ELE2_ID, PAY_ELE3_ID, PAY_ELE4_ID, " +
            "PAY_ELE5_ID, IS_VALID, PAY_FUND_REFUND_CODE, PAY_FUND_REFUND_ID, BILL_TYPE_ID, RCID, PAY_KIND_ID, " +
            "PAY_BUS_TYPE_ID, EXP_FUNC_ID, FUND_TRAOBJ_TYPE_ID, INTERNAL_DEP_ID, MOF_DIV_ID, CURRENCY_ID, " +
            "ORI_REQUEST_ID, PLAN_ID, CONTRACT_ID, PA_ID, MON_DISPOSE_MSG, MON_DESCRIPTION, MANAGE_MOF_DEP_ID, " +
            "MANAGE_MOF_DEP_CODE, MANAGE_MOF_DEP_NAME, LQD_RECEIV_ACC_ID, LQD_RECEIV_ACC_NO, LQD_RECEIV_ACC_NAME, " +
            "LQD_RECEIV_BANK_ID, LQD_RECEIV_BANK_NO, LQD_RECEIV_BANK_NAME, CLEAR_ACCT_BANK_NO, CLEAR_ACCT_BANK_NAME, " +
            "CLEAR_ACCT_NAME, CLEAR_ACCT_NO, FOUND_TYPE_ID, FOUND_TYPE_CODE, FOUND_TYPE_NAME, PRO_CAT_ID, " +
            "PRO_CAT_CODE, PRO_CAT_NAME, PAY_KIND_CODE, PAY_KIND_NAME, BGT_SOURCE_CODE, BGT_SOURCE_NAME) " +
            "values ('#id', 'SQ-530000-20210714-00423', to_date('14-07-2021 16:05:57', " +
            "'dd-mm-yyyy hh24:mi:ss'), '101001', '21', '1111', '2010350', '50501', '30112', '530000210000000023390', " +
            "'1234test', '1', '121', '测试第一次', '1', 0.88, '69edb7cf-714d-4104-ae03-0804d1a6841700', " +
            "'云南省人民政府办公厅', '135611872299', '中国银行股份有限公司云南省分行（本部）', '测试一', '110120119', '农行'," +
            " '21', '0', '2021', '530000000', null, 'CNY', 0.00000000, null, null, to_date('14-07-2021 16:06:29', " +
            "'dd-mm-yyyy hh24:mi:ss'), 2, to_date('14-07-2021 16:05:57', 'dd-mm-yyyy hh24:mi:ss'), " +
            "'691E66E9-C4CA-4696-AA47-6A197F5D7285', '云南省人民政府办公厅', '591B1C59-BCCA-4887-A2E2-64A189BEFB93', " +
            "'当年预算', '283D462C-0D30-40AB-9673-74757C4932B3', '本级财力安排', '事业运行', " +
            "'71CCDF88-878B-4170-A041-19856E41584A', '工资福利支出', '645AE958-5159-4B19-83FB-AC93A457C974', " +
            "'其他社会保障缴费', 'a84d531c626b427ab294fe0c7b3c0abb', '社会保障缴费', " +
            "'966AAD33-7DEC-4DFC-9A38-05D0A345E330', '电子转账支付', '37330005-28C0-4FE7-942C-CA477DD0153B', " +
            "'财政授权支付', '普通业务', '10100111000002', '6327', '530000657', '104731003017', null, null, null, " +
            "'与部门内下级单位', null, '云南省', '人民币', 0.00, null, null, '1EDA6BC8-5448-11E9-A43E-D34BB51E3C3', " +
            "'5300004914', 0, 0, null, '张颖', 1626249958348, '3BEEAD30-B444-49C3-A8E6-B34F9D8FFDFA', '0', '0', null, " +
            "'429A065F-D5B1-4E88-8024-914D965C7887', null, '0', null, 0, 0, 0, null, '104', 0, 0, " +
            "'云财预〔2020〕114号', null, null, null, 0, 0, null, null, '0', 0, null, 0, null, '0', null, '1', '正', " +
            "'8202', 0, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, 0, 0, 0, 0, 0, 0, " +
            "null, null, '9205', null, '1', 'EE8704DB-6218-493B-A236-5545E7B1B2ED', " +
            "'C5B27722-2BA1-47B4-AFAB-D3016BE64886', 'D1EF0A70-1E0A-4260-8EAB-8CF3E6103523', null, '1', " +
            "'eea4d6d26jl04c26b77a638232c9ecd5', '0', '0', '0', '201011', null, null, " +
            "'3B000F64-E6C2-4451-97D1-00A4C8A2F2A9', '12', '行政处', 'E1D3A9CD-A3FA-11DE-8728-830C28ABEB67', " +
            "'9160460017381002', '财政性资金垫款', '530000657', '104731003017', '中国银行股份有限公司云南省分行（本部）', " +
            "'011731002002', '中华人民共和国国家金库云南省分库', '云南省财政厅', '240000000002271001', " +
            "'97EA156A-3EC7-4460-B091-7935A366E891', '11', '年初安排', '429A065F-D5B1-4E88-8024-914D965C7887', '112', " +
            "'社会保障缴费', null, null, '1', '年初批复')";


    private String createSql() {
        String id = UUID.randomUUID().toString().replaceAll("-", "-");
        return sql.replace("#id", id);
    }

    /**
     * 耗时3606
     */
    @Test
    public void connectionTest() throws SQLException {
        Connection connection = OracleDatasourceInit.dataSource.getConnection();
        long start = System.currentTimeMillis();
        int batchSize = 6;
        for (int h = 0; h < 500 / batchSize; h++) {
            Statement statement = connection.createStatement();
            for (int i = 0; i < batchSize; i++) {
                statement.addBatch(this.createSql());
            }
            statement.executeBatch();
            connection.commit();
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - start));
    }

    /**
     * 耗时3041
     */
    @Test
    public void mybatisSqlTest() {
        EzMapper ezMapper = OracleBaseTest.sqlSession.getMapper(EzMapper.class);
        long start = System.currentTimeMillis();
        int batchSize = 6;
        for (int h = 0; h < 500 / batchSize; h++) {
            StringBuilder sql = new StringBuilder("begin \n");
            for (int i = 0; i < batchSize; i++) {
                sql.append(this.createSql()).append(";\n");
            }
            sql.append("end;");
            ezMapper.updateBySql(sql.toString(), new HashMap<>());
        }
        OracleBaseTest.sqlSession.commit();
        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - start));
    }

    @Test
    public void mybatisMapperTest() {
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
                "\"clear_acct_no\":\"240000000002271001\",\"clear_acct_name\":\"云南省财政厅\",\"clear_acct_bank_no\":" +
                "\"011731002002\",\"clear_acct_bank_name\":\"中华人民共和国国家金库云南省分库\",\"pay_acct_no\":" +
                "\"135611872299\",\"pay_acct_name\":\"云南省人民政府办公厅\",\"pay_acct_bank_code\":\"104731003017\"," +
                "\"pay_acct_bank_name\":\"中国银行股份有限公司云南省分行（本部）\",\"lqd_receiv_acc_id\":" +
                "\"E1D3A9CD-A3FA-11DE-8728-830C28ABEB67\",\"lqd_receiv_bank_id\":\"530000657\",\"clear_acct_id\":" +
                "\"1EDA6BC8-5448-11E9-A43E-D34BB51E3C3\",\"clear_acct_bank_id\":\"5300004914\",\"pay_acct_id\":" +
                "\"6327\",\"pay_acct_bank_id\":\"530000657\",\"payee_acct_name\":\"测试一\",\"payee_acct_no\":" +
                "\"110120119\",\"pay_app_id\":\"69edb7cf-714d-4104-ae03-0804d1a6841700\",\"plan_id\":\"0\"," +
                "\"is_final\":0,\"data_type\":0,\"is_valid\":0,\"pay_cert_no\":\"10100111000002\",\"ori_request_id\"" +
                ":\"0\",\"contract_id\":\"0\",\"salary_batch\":0,\"doc_no_name\":\"云财预〔2020〕114号\"," +
                "\"task_status\":0,\"vt_code\":\"8202\",\"pay_account_notice_id\":0,\"pa_id\":201011,\"pa_code\":" +
                "\"1\",\"pa_name\":\"正\",\"pay_refund_amount\":0,\"clear_time\":0,\"mon_flag\":0," +
                "\"lqd_quota_notice_id\":\"0\",\"pay_bank_kind\":\"104\",\"input_mode\":0,\"is_hang\":0," +
                "\"is_secret\":0,\"bill_type_id\":\"9205\",\"create_user\":\"张颖\",\"last_ver\":1626249958348," +
                "\"create_date\":\"2021-07-14 16:05:57\",\"n_field1\":0,\"n_field2\":0,\"n_field3\":0,\"n_field4\":" +
                "0,\"n_field5\":0,\"mon_no_flag\":0,\"mon_no_level\":0,\"pay_app_no\":\"SQ-530000-20210714-00423\"" +
                ",\"bgt_id\":\"B7A41072F5B4B32EE053090BA8C06A0D\",\"use_des\":\"测试第一次\",\"pay_bus_type_id\":\"" +
                "EE8704DB-6218-493B-A236-5545E7B1B2ED\",\"pay_bus_type_code\":\"1\",\"pay_bus_type_name\":\"普通业务" +
                "\",\"pay_app_amt\":0.88,\"pay_cert_id\":\"69edb7cf-714d-4104-ae03-0804d1a6841700\",\"" +
                "payee_acct_bank_name\":\"农行\",\"fund_traobj_type_id\":\"D1EF0A70-1E0A-4260-8EAB-8CF3E6103523\"," +
                "\"fund_traobj_type_code\":\"21\",\"fund_traobj_type_name\":\"与部门内下级单位\",\"internal_dep_code\":" +
                "\"0\",\"fiscal_year\":2021,\"mof_div_id\":\"1\",\"mof_div_code\":\"530000000\",\"mof_div_name\":" +
                "\"云南省\",\"currency_id\":\"eea4d6d26jl04c26b77a638232c9ecd5\",\"currency_code\":\"CNY\"," +
                "\"currency_name\":\"人民币\",\"est_rat\":0.0,\"update_time\":\"2021-07-14 16:06:29\",\"create_time\"" +
                ":\"2021-07-14 16:05:57\",\"is_deleted\":2}";
        PayVoucherMapper voucherMapper = OracleBaseTest.sqlSession.getMapper(PayVoucherMapper.class);
        List<PayRequest> requests = new LinkedList<>();
        for (int i = 0; i < 500; i++) {
            PayRequest request = JacksonUtils.conversion(json, PayRequest.class);
            request.setPay_app_id(UUID.randomUUID().toString().replaceAll("-", ""));
            request.setBgt_id("1234test");
            requests.add(request);
        }
        System.out.println("插入开始");
        long start = System.currentTimeMillis();
        int size = 50;
        int count = requests.size() / size;
        if ((requests.size() % size) > 0) {
            count++;
        }
        for (int i = 0; i < count; i++) {
            int startIndex = i * size;
            int endIndex = (i + 1) * size;
            if (endIndex > requests.size()) {
                endIndex = requests.size();
            }
            List<PayRequest> requests1 = requests.subList(startIndex, endIndex);
            voucherMapper.batchInsert(requests1);
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时" + (end - start));
        OracleBaseTest.sqlSession.commit();
    }

    @Test
    public void test() {
        int count = 100;
        List<Long> total = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            this.mybatisMapperTest();
            long end = System.currentTimeMillis();
            total.add(end - start);
        }
        OptionalDouble average = total.stream().mapToInt(e -> (int) (long) e).average();
        System.out.println("平均耗时" + average.getAsDouble());
    }
}
