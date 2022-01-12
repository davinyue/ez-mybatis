package org.rdlinux.ezmybatis.java.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 支付申请实体
 *
 * @author YY
 * @version 2011/5/30
 */
@Getter
@Setter
@Entity
@Table(name = "PAY_VOUCHER")
public class PayRequest extends BaseAccountSupport {
    private static final long serialVersionUID = 2947897425705623045L;
    /**
     * 支付申请ID
     */
    private String pay_app_id = "0";

    /**
     * 财政监控处理意见
     */
    private String mon_dispose_msg;
    /**
     * 违规规则描述
     */
    private String mon_description;

    /**
     * 计划明细ID
     */
    private String plan_id = "0";
    /**
     * 0 在途 1 已办结
     */
    private int is_final = 0;
    /**
     * 数据类型   0 正常   1 更正  2 核销
     */
    private int data_type = 0;


    private int is_valid;

    /**
     * 拆分来源ID
     */
    private String ori_split_id;

    /**
     * 支付凭证编码
     */
    private String pay_cert_no;
    /***
     * 退回通知书编码
     */
    private String pay_fund_refund_code;


    /**
     * 支票号
     */
    private String check_no;

    /**
     * 退款原ID 当支付申请为退款时，关联原id
     */
    private String ori_request_id = "0";


    /**
     * 更正支付申请id
     */
    private String pay_correct_request_id;


    /**
     * 合同ID
     */
    private String contract_id = "0";


    /**
     * 工资批次号
     */
    private long salary_batch;

    /**
     * 指标文号
     */
    private String doc_no_name;

    private String audit_remark;

    private int task_status;

    /**
     * 凭证编码
     */
    private String vt_code;

    private BigDecimal d_field1;

    private BigDecimal d_field2;

    private BigDecimal d_field3;


    private BigDecimal d_field4;


    private BigDecimal d_field5;

    private long pay_account_notice_id;


    // 默认赋值正向
    private long pa_id = 201011L;


    // 默认赋值正向
    private String pa_code = "1";


    // 默认赋值正向
    private String pa_name = "正";


    /**
     * 数据权限
     */
    private String rcid;

    /**
     * 公务卡补助金额
     */
    private BigDecimal card_subsidy_amount;

    /**
     * 已退款金额
     */
    private BigDecimal pay_refund_amount = BigDecimal.ZERO;


    private Timestamp pay_date;

    private Timestamp clear_date;
    /**
     * 清算
     */
    private long clear_time;

    /**
     * qjs20120407，增加监控相关属性： mon_flag：监控标识.-2
     * 未处理数据，-1代表禁止类数据，0代表正常数据，1代表违规不记流转标识数据，2 及大于2代表违规记流转标识数据
     */
    private int mon_flag = 0;

    /**
     * 跨年退款历史数据显示
     */
    private String old_agency_code;

    /**
     * 退回通知书ID
     */
    private String pay_fund_refund_id;
    /**
     * 划款申请ID
     */
    private String pay_transfer_note_id;
    /**
     * 汇总清算单id
     */
    private String lqd_quota_notice_id = "0";
    /**
     * 划款凭证id
     */
    private String lqd_cert_id;

    /**
     * 收款银行行别编码
     */
    private String payee_bank_kind;
    /**
     * 付款银行行别编码
     */
    private String pay_bank_kind;
    /**
     * 录入方式：0、手工录入、1、批量导入 2、自动生成
     */
    private long input_mode;
    /**
     * 是否挂起 0:未挂起,1:挂起,2:未上传附件,3:已上传附件
     */
    private long is_hang;
    /**
     * 是否涉密 0否  1是
     */
    private long is_secret;
    /**
     * 内设部门
     */
    private String internal_department;
    private String bill_type_id;
    private String create_user;

    private long last_ver;

    private Timestamp create_date;
    private long n_field1;
    private long n_field2;

    /**
     * 支付业务类型来源 1、普通业务  2、公务卡业务 3、工资业务 4、其他批量业务
     * 5、托收业务  6、税收缴纳   8、更正业务  9、其他业务
     */
    private long n_field3;
    private long n_field4;
    private long n_field5;
    private String c_field1;
    private String c_field2;
    private String c_field3;
    private String c_field4;
    private String c_field5;
    private String pay_ele1_id;
    private String pay_ele2_id;
    private String pay_ele3_id;
    private String pay_ele4_id;
    private String pay_ele5_id;

    /**
     * 无规则监控标识
     */
    private int mon_no_flag;
    /**
     * 无规则监控预警级别
     */
    private int mon_no_level;
    /**
     * 无规则监控审核意见
     */
    private String mon_no_remark;
    private String pay_app_no;

    private String bgt_id;
    private String use_des;

    //private String pay_bus_type_id = "0";
    private String pay_bus_type_id;
    //private String pay_bus_type_code = "0";
    private String pay_bus_type_code;
    private String pay_bus_type_name;
    private BigDecimal pay_app_amt;
    private String pay_cert_id = "0";
    //private ElementDTO pay_acct;
    //private ElementDTO payee_acct;
    private String payee_acct_bank_name;

    private String fund_traobj_type_id;
    //private String fund_traobj_type_code = "0";
    private String fund_traobj_type_code;
    private String fund_traobj_type_name;

    private String internal_dep_id;


    //纳税信息字段
    //private String internal_dep_code = "0";
    private String internal_dep_code;
    private String internal_dep_name;
    private int fiscal_year;

    private String mof_div_id;
    private String mof_div_code;
    private String mof_div_name;
    private BigDecimal foreign_amt;

    private String currency_id;
    //private String currency_code = "0";
    private String currency_code;
    private String currency_name;
    private double est_rat;
    private String receiver_code;
    //合同编号
    private String contract_no;
    private Timestamp update_time;
    private Timestamp create_time;
    private int is_deleted;
}
