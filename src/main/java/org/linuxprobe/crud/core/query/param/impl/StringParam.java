package org.linuxprobe.crud.core.query.param.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.linuxprobe.crud.core.query.param.BaseParam;
import org.linuxprobe.crud.exception.OperationNotSupportedException;

import java.util.List;

/**
 * 字符串型参数
 */
@Setter
@Getter
@NoArgsConstructor
public class StringParam extends BaseParam<String> {
    private String value;
    /**
     * 上限
     */
    private String maxValue;
    /**
     * 下限
     */
    private String minValue;
    /**
     * 多值
     */
    private List<String> multiValues;
    /**
     * 模糊匹配模式
     */
    private Fuzzt fuzzt = Fuzzt.All;

    /**
     * 操作符支持is null和is not null
     */
    public StringParam(Operator operator) {
        if (operator != Operator.isNotNull && operator != Operator.isNull) {
            throw new OperationNotSupportedException();
        } else {
            this.setOperator(operator);
        }
    }

    /**
     * 自定义条件连接and和or, 操作符支持is null和is not null
     */
    public StringParam(Condition condition, Operator operator) {
        if (operator != Operator.isNotNull && operator != Operator.isNull) {
            throw new OperationNotSupportedException();
        } else {
            this.setOperator(operator);
            this.setCondition(condition);
        }
    }

    /**
     * 操作符默认是=
     */
    public StringParam(String value) {
        this.value = value;
    }

    /**
     * 自定义条件连接and和or, 操作符默认是=
     */
    public StringParam(Condition condition, String value) {
        this.setCondition(condition);
        this.value = value;
    }

    /**
     * 操作符不支持in, not in, between, not between
     */
    public StringParam(Operator operator, String value) {
        if (operator == Operator.in || operator == Operator.notIn || operator == Operator.between
                || operator == Operator.notBetween) {
            throw new OperationNotSupportedException();
        } else {
            this.setOperator(operator);
            this.value = value;
        }
    }

    /**
     * 自定义条件连接and和or, 操作符支持=, !=, >, >=, <, <=, like, not like, is null, is not
     * null, 其中模糊匹配默认是全模糊匹配
     */
    public StringParam(Condition condition, Operator operator, String value) {
        if (operator == Operator.in || operator == Operator.notIn || operator == Operator.between
                || operator == Operator.notBetween) {
            throw new OperationNotSupportedException();
        } else {
            this.setCondition(condition);
            this.setOperator(operator);
            this.value = value;
        }
    }

    /**
     * 条件连接是and,操作符支持like, not like, 可指定模糊匹配模式
     */
    public StringParam(Operator operator, Fuzzt fuzzt, String value) {
        if (operator != Operator.like && operator != Operator.unlike) {
            throw new OperationNotSupportedException();
        } else {
            this.setOperator(operator);
            this.fuzzt = fuzzt;
            this.value = value;
        }
    }

    /**
     * 自定义条件连接and和or, 操作符支持like, not like, 可指定模糊匹配模式
     */
    public StringParam(Condition condition, Operator operator, Fuzzt fuzzt, String value) {
        if (operator != Operator.like && operator != Operator.unlike) {
            throw new OperationNotSupportedException();
        } else {
            this.setCondition(condition);
            this.setOperator(operator);
            this.fuzzt = fuzzt;
            this.value = value;
        }
    }

    /**
     * 操作符支持between, not between
     */
    public StringParam(Operator operator, String minValue, String maxValue) {
        if (operator != Operator.between && operator != Operator.notBetween) {
            throw new OperationNotSupportedException();
        } else {
            this.setOperator(operator);
            this.minValue = minValue;
            this.maxValue = maxValue;
        }
    }

    /**
     * 自定义条件连接and和or, 操作符只支持between, not between
     */
    public StringParam(Condition condition, Operator operator, String minValue, String maxValue) {
        if (operator != Operator.between && operator != Operator.notBetween) {
            throw new OperationNotSupportedException();
        } else {
            this.setOperator(operator);
            this.setOperator(operator);
            this.minValue = minValue;
            this.maxValue = maxValue;
        }
    }

    /**
     * 操作符支持in, not in
     */
    public StringParam(Operator operator, List<String> multiValues) {
        if (operator != Operator.in && operator != Operator.notIn) {
            throw new OperationNotSupportedException();
        } else {
            this.setOperator(operator);
            this.multiValues = multiValues;
        }
    }

    /**
     * 自定义条件连接and和or, 操作符支持in, not in
     */
    public StringParam(Condition condition, Operator operator, List<String> multiValues) {
        if (operator != Operator.in && operator != Operator.notIn) {
            throw new OperationNotSupportedException();
        } else {
            this.setOperator(operator);
            this.setOperator(operator);
            this.multiValues = multiValues;
        }
    }

    @Override
    public boolean isEmpty() {
        Operator operator = this.getOperator();
        if (Operator.isNull.equals(operator) || Operator.isNotNull.equals(operator)) {
            return false;
        } else if (Operator.between.equals(operator) || Operator.notBetween.equals(operator)) {
            return this.getMinValue() == null || this.getMaxValue() == null;
        } else if (Operator.in.equals(operator) || Operator.notIn.equals(operator)) {
            return this.getMultiValues() == null;
        } else {
            return StringUtils.isEmpty(this.value);
        }
    }

    /**
     * 模糊匹配模式
     */
    public static enum Fuzzt {
        /**
         * 左侧数据模糊匹配
         */
        Left,
        /**
         * 右侧数据模糊匹配
         */
        Right,
        /**
         * 左右模糊匹配
         */
        All,
        /**
         * 自定义模糊匹配
         */
        Custom
    }
}
