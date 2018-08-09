package org.linuxprobe.crud.query.param.impl;

import java.util.List;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.query.param.QueryParam;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 布尔型参数 */
@Setter
@NoArgsConstructor
public class BooleanParam extends QueryParam {
	/** 操作符is null或者is not null */
	public BooleanParam(Operator operator) {
		if (operator != Operator.isNotNull && operator != Operator.isNull) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
		}
	}

	/** 操作符is null或者is not null */
	public BooleanParam(Condition condition, Operator operator) {
		if (operator != Operator.isNotNull && operator != Operator.isNull) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.setCondition(condition);
		}
	}

	/** 操作符= */
	public BooleanParam(Boolean value) {
		this.value = value;
	}

	/** 操作符= */
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
	public BooleanParam(Operator operator, Boolean lowerLimit, Boolean upperLimit) {
		if (operator != Operator.between && operator != Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.lowerLimit = lowerLimit;
			this.upperLimit = upperLimit;
		}
	}

	/** 操作符只支持between, not between */
	public BooleanParam(Condition condition, Operator operator, Boolean lowerLimit, Boolean upperLimit) {
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
	public BooleanParam(Operator operator, List<Boolean> multipart) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.multipart = multipart;
		}
	}

	/** 操作符只支持in, not in */
	public BooleanParam(Condition condition, Operator operator, List<Boolean> multipart) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setCondition(condition);
			this.setOperator(operator);
			this.multipart = multipart;
		}
	}

	private Boolean value;
	/** 上限 */
	private Boolean upperLimit;
	/** 下限 */
	private Boolean lowerLimit;
	/** 多值 */
	private List<Boolean> multipart;

	@Override
	public String getValue() {
		if (value == null) {
			return null;
		} else {
			return value ? 1 + "" : 0 + "";
		}
	}

	@Override
	public String getMultipart() {
		if (multipart == null || multipart.isEmpty()) {
			return null;
		} else {
			StringBuffer multipartValue = new StringBuffer();
			for (int i = 0; i < multipart.size(); i++) {
				int tempvalue = multipart.get(i) ? 1 : 0;
				if (i + 1 != multipart.size()) {
					multipartValue.append(tempvalue + ", ");
				} else {
					multipartValue.append(tempvalue);
				}
			}
			return multipartValue.toString();
		}
	}

	@Override
	public String getUpperLimit() {
		if (upperLimit == null) {
			return null;
		} else {
			return upperLimit ? 1 + "" : 0 + "";
		}
	}

	@Override
	public String getLowerLimit() {
		if (lowerLimit == null) {
			return null;
		} else {
			return lowerLimit ? 1 + "" : 0 + "";
		}
	}

}
