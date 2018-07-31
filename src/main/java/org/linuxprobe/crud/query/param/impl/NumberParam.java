package org.linuxprobe.crud.query.param.impl;

import java.util.List;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.exception.ParameterException;
import org.linuxprobe.crud.query.param.QueryParam;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 数字型参数 */
@Setter
@NoArgsConstructor
public class NumberParam extends QueryParam {
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
	public NumberParam(Operator operator, Number lowerLimit, Number upperLimit) {
		if (operator != Operator.between && operator != Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.lowerLimit = lowerLimit;
			this.upperLimit = upperLimit;
		}
	}

	/** 操作符只支持between, not between */
	public NumberParam(Condition condition, Operator operator, Number lowerLimit, Number upperLimit) {
		if (operator != Operator.between && operator != Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setCondition(condition);
			this.setOperator(operator);
			this.lowerLimit = lowerLimit;
			this.upperLimit = upperLimit;
		}
	}

	/** 操作符只支持in, not in */
	public NumberParam(Operator operator, List<Number> multipart) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.multipart = multipart;
		}
	}

	/** 操作符只支持in, not in */
	public NumberParam(Condition condition, Operator operator, List<Number> multipart) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setCondition(condition);
			this.setOperator(operator);
			this.multipart = multipart;
		}
	}

	private Number value;
	/** 上限 */
	private Number upperLimit;
	/** 下限 */
	private Number lowerLimit;
	/** 多值 */
	private List<Number> multipart;

	@Override
	public String getValue() {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	@Override
	public String getUpperLimit() {
		if (upperLimit == null) {
			return null;
		}
		return upperLimit.toString();
	}

	@Override
	public String getLowerLimit() {
		if (lowerLimit == null) {
			return null;
		}
		return lowerLimit.toString();
	}

	@Override
	public String getMultipart() {
		if (multipart == null || multipart.isEmpty()) {
			return null;
		} else {
			StringBuffer multipartValue = new StringBuffer();
			for (int i = 0; i < multipart.size(); i++) {
				Number tempvalue = multipart.get(i);
				if (i + 1 != multipart.size()) {
					multipartValue.append(tempvalue.toString() + ", ");
				} else {
					multipartValue.append(tempvalue.toString());
				}
			}
			return multipartValue.toString();
		}
	}

	@Override
	public void setOperator(Operator operator) {
		if (operator == Operator.like || operator == Operator.unlike || this.getOperator() == Operator.regexp) {
			throw new ParameterException("数字类型不支持like nunlike regexp查询");
		} else {
			super.setOperator(operator);
		}
	}
}
