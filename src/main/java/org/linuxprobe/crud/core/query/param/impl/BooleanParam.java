package org.linuxprobe.crud.core.query.param.impl;

import java.util.List;

import org.linuxprobe.crud.core.query.param.BaseParam;
import org.linuxprobe.crud.exception.OperationNotSupportedException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 布尔型参数 */
@Setter
@Getter
@NoArgsConstructor
public class BooleanParam extends BaseParam<Boolean> {
	private Boolean value;
	/** 上限 */
	private Boolean maxValue;
	/** 下限 */
	private Boolean minValue;
	/** 多值 */
	private List<Boolean> multiValues;

	/** 操作符支持is null和is not null */
	public BooleanParam(Operator operator) {
		if (operator != Operator.isNotNull && operator != Operator.isNull) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
		}
	}

	/** 自定义条件连接and和or, 操作符支持is null和is not null */
	public BooleanParam(Condition condition, Operator operator) {
		if (operator != Operator.isNotNull && operator != Operator.isNull) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.setCondition(condition);
		}
	}

	/** 操作符默认是= */
	public BooleanParam(Boolean value) {
		this.value = value;
	}

	/** 自定义条件连接and和or, 操作符默认是= */
	public BooleanParam(Condition condition, Boolean value) {
		this.setCondition(condition);
		this.value = value;
	}

	/** 操作符不支持in, not in, between, not between */
	public BooleanParam(Operator operator, Boolean value) {
		if (operator == Operator.in || operator == Operator.notIn || operator == Operator.between
				|| operator == Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.value = value;
		}
	}

	/** 操作符不支持in, not in, between, not between */
	public BooleanParam(Condition condition, Operator operator, Boolean value) {
		if (operator == Operator.in || operator == Operator.notIn || operator == Operator.between
				|| operator == Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setCondition(condition);
			this.setOperator(operator);
			this.value = value;
		}
	}

	/** 操作符只支持between, not between */
	public BooleanParam(Operator operator, Boolean minValue, Boolean maxValue) {
		if (operator != Operator.between && operator != Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.minValue = minValue;
			this.maxValue = maxValue;
		}
	}

	/** 操作符只支持between, not between */
	public BooleanParam(Condition condition, Operator operator, Boolean minValue, Boolean maxValue) {
		if (operator != Operator.between && operator != Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setCondition(condition);
			this.setOperator(operator);
			this.minValue = minValue;
			this.maxValue = maxValue;
		}
	}

	/** 操作符只支持in, not in */
	public BooleanParam(Operator operator, List<Boolean> multiValues) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.multiValues = multiValues;
		}
	}

	/** 操作符只支持in, not in */
	public BooleanParam(Condition condition, Operator operator, List<Boolean> multiValues) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setCondition(condition);
			this.setOperator(operator);
			this.multiValues = multiValues;
		}
	}
}
