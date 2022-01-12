package org.rdlinux.ezmybatis.java.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 账户类
 * 计划和支付环节父类
 *
 * @author YY
 * @version 1.0
 */
@Getter
@Setter
public abstract class BaseAccountSupport extends BaseDataSupport {


    /**
     * 序列化
     */
    private static final long serialVersionUID = 2122332247415302763L;
    protected String lqd_receiv_acc_no;
    protected String lqd_receiv_acc_name;
    protected String lqd_receiv_bank_no;
    protected String lqd_receiv_bank_name;

    protected String clear_acct_no;
    protected String clear_acct_name;
    protected String clear_acct_bank_no;

    protected String clear_acct_bank_name;
    /**
     * 支付账户账号
     */
    protected String pay_acct_no;
    /**
     * 支付账户名称
     */
    protected String pay_acct_name;
    /**
     * 付款银行编码
     */
    protected String pay_acct_bank_code;
    /**
     * 付款银行名称
     */
    protected String pay_acct_bank_name;

    private String lqd_receiv_acc_id = "0";
    /**
     * 代理银行
     */
    private String lqd_receiv_bank_id = "0";


    private String clear_acct_id = "0";

    private String clear_acct_bank_id = "0";

    /**
     * 支付账户ID
     */
    private String pay_acct_id = "0";

    /**
     * 付款银行
     */
    private String pay_acct_bank_id = "0";


    /**
     * 收款人账号ID
     */
    private String payee_acct_id;

    /**
     * 收款人全称
     */
    private String payee_acct_name;

    /**
     * 收款人账号
     */
    private String payee_acct_no;

    /**
     * 收款人银行行号
     */
    private String payee_acct_bank_no;

    /**
     * 收款人银行行号
     */
    private String payee_acct_bank_code;

    /**
     * 收款人银行行号
     */
    private String payee_acct_bank_id;
}