package org.linuxprobe.crud.core.query.param.impl;

import java.util.List;
import org.linuxprobe.crud.core.query.param.QueryParam;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.utils.SqlEscapeUtil;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 字符串型参数 */
@Setter
@NoArgsConstructor
public class StringParam extends QueryParam {
	/** 操作符支持is null和is not null */
	public StringParam(Operator operator) {
		if (operator != Operator.isNotNull && operator != Operator.isNull) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
		}
	}

	/** 自定义条件连接and和or, 操作符支持is null和is not null */
	public StringParam(Condition condition, Operator operator) {
		if (operator != Operator.isNotNull && operator != Operator.isNull) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.setCondition(condition);
		}
	}

	/** 操作符默认是= */
	public StringParam(String value) {
		this.value = value;
	}

	/** 自定义条件连接and和or, 操作符默认是= */
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

	/** 条件连接是and,操作符支持like, not like, 可指定模糊匹配模式 */
	public StringParam(Operator operator, Fuzzt fuzzt, String value) {
		if (operator != Operator.like && operator != Operator.unlike) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.fuzzt = fuzzt;
			this.value = value;
		}
	}

	/** 自定义条件连接and和or, 操作符支持like, not like, 可指定模糊匹配模式 */
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

	/** 操作符支持between, not between */
	public StringParam(Operator operator, String lowerLimit, String upperLimit) {
		if (operator != Operator.between && operator != Operator.notBetween) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.lowerLimit = lowerLimit;
			this.upperLimit = upperLimit;
		}
	}

	/** 自定义条件连接and和or, 操作符只支持between, not between */
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

	/** 操作符支持in, not in */
	public StringParam(Operator operator, List<String> multipart) {
		if (operator != Operator.in && operator != Operator.notIn) {
			throw new OperationNotSupportedException();
		} else {
			this.setOperator(operator);
			this.multipart = multipart;
		}
	}

	/** 自定义条件连接and和or, 操作符支持in, not in */
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
	/** 模糊匹配模式 */
	private Fuzzt fuzzt = Fuzzt.All;

	@Override
	public String getValue() {
		String temp = value;
		if (temp == null) {
			return null;
		} else {
			temp = SqlEscapeUtil.escape(temp);
			if (this.getOperator() == Operator.like || this.getOperator() == Operator.unlike) {
				if (Fuzzt.All.equals(this.fuzzt) || this.fuzzt == null) {
					return "'%" + temp + "%'";
				} else if (Fuzzt.Right.equals(this.fuzzt)) {
					return "'" + temp + "%'";
				} else if (Fuzzt.Left.equals(this.fuzzt)) {
					return "'%" + temp + "'";
				} else {
					return "'" + temp + "'";
				}
			} else {
				return "'" + temp + "'";
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
				tempvalue = SqlEscapeUtil.escape(tempvalue);
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
		String tempvalue = upperLimit;
		if (upperLimit == null) {
			return null;
		} else {
			tempvalue = SqlEscapeUtil.escape(tempvalue);
			return "'" + tempvalue + "'";
		}
	}

	@Override
	public String getLowerLimit() {
		String tempvalue = lowerLimit;
		if (lowerLimit == null) {
			return null;
		} else {
			tempvalue = SqlEscapeUtil.escape(tempvalue);
			return "'" + tempvalue + "'";
		}
	}

	/** 模糊匹配模式 */
	public static enum Fuzzt {
		/** 左侧数据模糊匹配 */
		Left,
		/** 右侧数据模糊匹配 */
		Right,
		/** 左右模糊匹配 */
		All,
		/** 自定义模糊匹配 */
		Custom;
	}
}
