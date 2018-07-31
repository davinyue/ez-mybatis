package org.linuxprobe.crud.query.param.impl;

import java.util.List;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.query.param.QueryParam;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 字符串型参数 */
@Setter
@NoArgsConstructor
public class StringParam extends QueryParam {
	/** 操作符= */
	public StringParam(String value) {
		this.value = value;
	}

	/** 操作符= */
	public StringParam(Condition condition, String value) {
		this.setCondition(condition);
		this.value = value;
	}

	/** 操作符不支持in, not in, between, not between */
	public StringParam(Operator operator, String value) {
		if (operator == Operator.in || operator == Operator.notIn || operator == Operator.between
				|| operator == Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.value = value;
		}
	}

	/** 操作符不支持in, not in, between, not between */
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

	/** 操作符只支持between, not between */
	public StringParam(Operator operator, String lowerLimit, String upperLimit) {
		if (operator != Operator.between && operator != Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.lowerLimit = lowerLimit;
			this.upperLimit = upperLimit;
		}
	}

	/** 操作符只支持between, not between */
	public StringParam(Condition condition, Operator operator, String lowerLimit, String upperLimit) {
		if (operator != Operator.between && operator != Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.setOperator(operator);
			this.lowerLimit = lowerLimit;
			this.upperLimit = upperLimit;
		}
	}

	/** 操作符只支持in, not in */
	public StringParam(Operator operator, List<String> multipart) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.multipart = multipart;
		}
	}

	/** 操作符只支持in, not in */
	public StringParam(Condition condition, Operator operator, List<String> multipart) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.setOperator(operator);
			this.multipart = multipart;
		}
	}

	private String value;
	/** 上限 */
	private String upperLimit;
	/** 下限 */
	private String lowerLimit;
	/** 多值 */
	private List<String> multipart;

	@Override
	public String getValue() {
		if (value == null) {
			return null;
		} else {
			if (this.getOperator() == Operator.like || this.getOperator() == Operator.unlike) {
				return "'%" + value + "%'";
			} else {
				return "'" + value + "'";
			}
		}
	}

	@Override
	public String getMultipart() {
		if (multipart == null || multipart.isEmpty()) {
			return null;
		} else {
			StringBuffer valueBufffer = new StringBuffer();
			for (int i = 0; i < multipart.size(); i++) {
				String tempvalue = multipart.get(i);
				if (i + 1 != multipart.size()) {
					valueBufffer.append("'" + tempvalue + "', ");
				} else {
					valueBufffer.append("'" + tempvalue + "'");
				}
			}
			return valueBufffer.toString();
		}
	}

	@Override
	public String getUpperLimit() {
		if (upperLimit == null) {
			return null;
		} else {
			return "'" + upperLimit + "'";
		}
	}

	@Override
	public String getLowerLimit() {
		if (lowerLimit == null) {
			return null;
		} else {
			return "'" + lowerLimit + "'";
		}
	}
}
