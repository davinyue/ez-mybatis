package org.linuxprobe.crud.core.query.param.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.linuxprobe.crud.core.query.param.QueryParam;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 日期型参数 */
@Setter
@NoArgsConstructor
public class DateParam extends QueryParam {
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
	public DateParam(Operator operator, Date lowerLimit, Date upperLimit) {
		if (operator != Operator.between && operator != Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.lowerLimit = lowerLimit;
			this.upperLimit = upperLimit;
		}
	}

	/** 操作符只支持between, not between */
	public DateParam(Condition condition, Operator operator, Date lowerLimit, Date upperLimit) {
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
	public DateParam(Operator operator, List<Date> multipart) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.multipart = multipart;
		}
	}

	/** 操作符只支持in, not in */
	public DateParam(Condition condition, Operator operator, List<Date> multipart) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setCondition(condition);
			this.setOperator(operator);
			this.multipart = multipart;
		}
	}

	private Date value;
	/** 上限 */
	private Date upperLimit;
	/** 下限 */
	private Date lowerLimit;
	/** 多值 */
	private List<Date> multipart;
	final static private SimpleDateFormat dateForma = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String getValue() {
		if (value == null) {
			return null;
		} else {
			String strValue = dateForma.format(value);
			if (this.getOperator() == Operator.like || this.getOperator() == Operator.unlike) {
				return "'%" + strValue + "%'";
			} else {
				return "'" + strValue + "'";
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
				Date tempvalue = multipart.get(i);
				String strTempvalue = dateForma.format(tempvalue);
				if (i + 1 != multipart.size()) {
					valueBufffer.append("'" + strTempvalue + "', ");
				} else {
					valueBufffer.append("'" + strTempvalue + "'");
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
			String strUpperLimit = dateForma.format(upperLimit);
			return "'" + strUpperLimit + "'";
		}
	}

	@Override
	public String getLowerLimit() {
		if (lowerLimit == null) {
			return null;
		} else {
			String strLowerLimit = dateForma.format(lowerLimit);
			return "'" + strLowerLimit + "'";
		}
	}
}
