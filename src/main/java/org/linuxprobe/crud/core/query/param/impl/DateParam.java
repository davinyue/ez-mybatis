package org.linuxprobe.crud.core.query.param.impl;

import java.util.Date;
import java.util.List;

import org.linuxprobe.crud.core.query.param.BaseParam;
import org.linuxprobe.crud.exception.OperationNotSupportedException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 日期型参数 */
@Setter
@Getter
@NoArgsConstructor
public class DateParam extends BaseParam<Date> {
	/** 操作符is null或者is not null */
	public DateParam(Operator operator) {
		if (operator != Operator.isNotNull && operator != Operator.isNull) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
		}
	}

	/** 操作符is null或者is not null */
	public DateParam(Condition condition, Operator operator) {
		if (operator != Operator.isNotNull && operator != Operator.isNull) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.setCondition(condition);
		}
	}

	/** 操作符= */
	public DateParam(Date value) {
		this.value = value;
	}

	/** 操作符= */
	public DateParam(Condition condition, Date value) {
		this.setCondition(condition);
		this.value = value;
	}

	/** 操作符不支持in, not in, between, not between */
	public DateParam(Operator operator, Date value) {
		if (operator == Operator.in || operator == Operator.notIn || operator == Operator.between
				|| operator == Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.value = value;
		}
	}

	/** 操作符不支持in, not in, between, not between */
	public DateParam(Condition condition, Operator operator, Date value) {
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
	public DateParam(Operator operator, Date minValue, Date maxValue) {
		if (operator != Operator.between && operator != Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.minValue = minValue;
			this.maxValue = maxValue;
		}
	}

	/** 操作符只支持between, not between */
	public DateParam(Condition condition, Operator operator, Date minValue, Date maxValue) {
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
	public DateParam(Operator operator, List<Date> multiValues) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.multiValues = multiValues;
		}
	}

	/** 操作符只支持in, not in */
	public DateParam(Condition condition, Operator operator, List<Date> multiValues) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setCondition(condition);
			this.setOperator(operator);
			this.multiValues = multiValues;
		}
	}

	private Date value;
	/** 上限 */
	private Date maxValue;
	/** 下限 */
	private Date minValue;
	/** 多值 */
	private List<Date> multiValues;
}
