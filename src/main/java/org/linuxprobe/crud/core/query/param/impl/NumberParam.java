package org.linuxprobe.crud.core.query.param.impl;

import java.util.List;

import org.linuxprobe.crud.core.query.param.BaseParam;
import org.linuxprobe.crud.exception.OperationNotSupportedException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 数字型参数 */
@Setter
@Getter
@NoArgsConstructor
public class NumberParam extends BaseParam<Number> {
	private Number value;
	/** 下限 */
	private Number minValue;
	/** 上限 */
	private Number maxValue;
	/** 多值 */
	private List<Number> multiValues;

	/** 操作符is null或者is not null */
	public NumberParam(Operator operator) {
		if (operator != Operator.isNotNull && operator != Operator.isNull) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
		}
	}

	/** 操作符is null或者is not null */
	public NumberParam(Condition condition, Operator operator) {
		if (operator != Operator.isNotNull && operator != Operator.isNull) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.setCondition(condition);
		}
	}

	/** 操作符= */
	public NumberParam(Number value) {
		this.value = value;
	}

	/** 操作符= */
	public NumberParam(Condition condition, Number value) {
		this.setCondition(condition);
		this.value = value;
	}

	/** 操作符不支持in, not in, between, not between */
	public NumberParam(Operator operator, Number value) {
		if (operator == Operator.in || operator == Operator.notIn || operator == Operator.between
				|| operator == Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.value = value;
		}
	}

	/** 操作符不支持in, not in, between, not between */
	public NumberParam(Condition condition, Operator operator, Number value) {
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
	public NumberParam(Operator operator, Number minValue, Number maxValue) {
		if (operator != Operator.between && operator != Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.minValue = minValue;
			this.maxValue = maxValue;
		}
	}

	/** 操作符只支持between, not between */
	public NumberParam(Condition condition, Operator operator, Number minValue, Number maxValue) {
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
	public NumberParam(Operator operator, List<Number> multiValues) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.multiValues = multiValues;
		}
	}

	/** 操作符只支持in, not in */
	public NumberParam(Condition condition, Operator operator, List<Number> multiValues) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setCondition(condition);
			this.setOperator(operator);
			this.multiValues = multiValues;
		}
	}

	@Override
	public void setOperator(Operator operator) {
		if (operator == Operator.like || operator == Operator.unlike || this.getOperator() == Operator.regexp) {
			throw new IllegalArgumentException("数字类型不支持like nunlike regexp查询");
		} else {
			super.setOperator(operator);
		}
	}
}
