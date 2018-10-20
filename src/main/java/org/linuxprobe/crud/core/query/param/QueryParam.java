package org.linuxprobe.crud.core.query.param;

import lombok.Getter;

@Getter
public abstract class QueryParam {
	private Operator operator = Operator.equal;
	private Condition condition = Condition.and;

	public String toSqlPart() {
		if (isEmpty()) {
			return null;
		}
		if (operator == Operator.equal || operator == Operator.unequal || operator == Operator.greaterThan
				|| operator == Operator.lessThan || operator == Operator.greaterThanOrEqualTo
				|| operator == Operator.lessThanOrEqualTo || operator == Operator.regexp || operator == Operator.like
				|| operator == Operator.unlike) {
			return operator.getOperator() + " " + getValue();
		} else if (operator == Operator.between || operator == Operator.notBetween) {
			return operator.getOperator() + " " + getLowerLimit() + " and " + getUpperLimit();
		} else if (operator == Operator.in || operator == Operator.notIn) {
			return operator.getOperator() + "(" + getMultipart() + ")";
		} else if (operator == Operator.isNull || operator == Operator.isNotNull) {
			return operator.getOperator() + " null";
		} else {
			return null;
		}
	}

	public boolean isEmpty() {
		if (this.getOperator() == Operator.isNull || this.getOperator() == Operator.isNotNull) {
			return false;
		} else if (this.getOperator() == Operator.between || this.getOperator() == Operator.notBetween) {
			if (this.getUpperLimit() == null || this.getLowerLimit() == null) {
				return true;
			} else {
				return false;
			}
		} else if (this.getOperator() == Operator.in || this.getOperator() == Operator.notIn) {
			return this.getMultipart() != null ? false : true;
		} else {
			return this.getValue() == null ? true : false;
		}
	}

	public abstract String getValue();

	/** in or not in用的多值查询 */
	public abstract String getMultipart();

	public abstract String getUpperLimit();

	public abstract String getLowerLimit();

	public void setOperator(Operator operator) {
		if (operator == null) {
			this.operator = Operator.equal;
		} else {
			this.operator = operator;
		}
	}

	public void setCondition(Condition condition) {
		if (condition == null) {
			this.condition = Condition.and;
		} else {
			this.condition = condition;
		}
	}

	/** 操作符 =,!=,like... */
	public static class Operator {
		private Operator(String operator) {
			this.operator = operator;
		}

		@Getter
		private String operator = "=";
		public static final Operator equal = new Operator("=");
		public static final Operator unequal = new Operator("!=");
		public static final Operator greaterThan = new Operator(">");
		public static final Operator lessThan = new Operator("<");
		public static final Operator greaterThanOrEqualTo = new Operator(">=");
		public static final Operator lessThanOrEqualTo = new Operator("<=");
		public static final Operator between = new Operator("between");
		public static final Operator notBetween = new Operator("not between");
		public static final Operator in = new Operator("in");
		public static final Operator notIn = new Operator("not in");
		public static final Operator like = new Operator("like");
		public static final Operator unlike = new Operator("not like");
		public static final Operator isNull = new Operator("is");
		public static final Operator isNotNull = new Operator("is not");
		/** 正则,只有字符串类,时间类支持 */
		public static final Operator regexp = new Operator("regexp");
	}

	/** 条件,and or */
	public static class Condition {
		private Condition(String condition) {
			this.condition = condition;
		};

		@Override
		public String toString() {
			return condition;
		}

		@Getter
		private String condition;
		public static final Condition and = new Condition("AND");
		public static final Condition or = new Condition("OR");
	}
}
