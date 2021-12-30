package org.rdlinux.ezmybatis.java.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 要素类 一般为指标父类
 *
 * @author YY
 * @version 1.0
 */
@Getter
@Setter
public abstract class BaseDataSupport implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = -5784326702575958324L;


    private String agency_id = "0";
    private String agency_code;
    private String agency_name;


    private String bgt_source_id = "0";
    private String bgt_source_code;
    private String bgt_source_name;


    private String bgt_year_id = "0";


    private String found_type_id = "0";
    private String found_type_code;
    private String found_type_name;

    private String pro_id = "0";
    private String pro_code;
    private String pro_name;


    private String dep_bgt_eco_id = "0";
    private String dep_bgt_eco_code;
    private String dep_bgt_eco_name;

    private String exp_func_id = "0";
    private String exp_func_code;
    private String exp_func_name;


    private String fund_type_id = "0";
    private String fund_type_code;
    private String fund_type_name;

    private String manage_mof_dep_id = "0";
    private String manage_mof_dep_code;
    private String manage_mof_dep_name;

    private String pay_kind_id = "0";
    private String pay_kind_code;
    private String pay_kind_name;

    private String pay_type_id = "0";
    private String pay_type_code;
    private String pay_type_name;


    private String set_mode_id = "0";
    private String set_mode_code;
    private String set_mode_name;


    private String all_met_id = "0";

    private String gov_bgt_eco_id = "0";
    private String gov_bgt_eco_code;
    private String gov_bgt_eco_name;

    private String pay_ele1_id = "0";

    private String pay_ele2_id = "0";


    private String pay_ele3_id = "0";


    private String pay_ele4_id = "0";


    private String pay_ele5_id = "0";


    /**
     * 项目分类
     */
    private String budget_pro_id = "0";

    private String budget_type_id = "0";

    private String bgt_type_id = "0";
    private String bgt_type_code;
    private String bgt_type_name;

    private String pro_cat_id = "0";
    private String pro_cat_code;
    private String pro_cat_name;
}
