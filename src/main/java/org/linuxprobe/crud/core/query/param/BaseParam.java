package org.linuxprobe.crud.core.query.param;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import lombok.Getter;

@Getter
public abstract class BaseParam<T extends Serializable> {
	private Operator operator = Operator.equal;
	private Condition condition = Condition.and;

	public boolean isEmpty() {
		if (this.getOperator() == Operator.isNull || this.getOperator() == Operator.isNotNull) {
			return false;
		} else if (this.getOperator() == Operator.between || this.getOperator() == Operator.notBetween) {
			if (this.getMinValue() == null || this.getMaxValue() == null) {
				return true;
			} else {
				return false;
			}
		} else if (this.getOperator() == Operator.in || this.getOperator() == Operator.notIn) {
			return this.getMultiValues() != null ? false : true;
		} else {
			return this.getValue() == null ? true : false;
		}
	}

	public abstract T getValue();

	public abstract T getMinValue();

	public abstract T getMaxValue();

	/** in or not in用的多值查询 */
	public abstract List<T> getMultiValues();

	public Class<?> getParamCalss() {
		Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Class<?> modelClass = null;
		try {
			modelClass = Class.forName(type.getTypeName());
		} catch (ClassNotFoundException e) {
		}
		return modelClass;
	}

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
		public static final Operator more = new Operator(">");
		public static final Operator less = new Operator("<");
		public static final Operator moreOrEqual = new Operator(">=");
		public static final Operator lessOrEqual = new Operator("<=");
		public static final Operator between = new Operator("BETWEEN");
		public static final Operator notBetween = new Operator("NOT BETWEEN");
		public static final Operator in = new Operator("IN");
		public static final Operator notIn = new Operator("NOT IN");
		public static final Operator like = new Operator("LIKE");
		public static final Operator unlike = new Operator("NOT LIKE");
		public static final Operator isNull = new Operator("IS");
		public static final Operator isNotNull = new Operator("IS NOT");
		/** 正则,只有字符串类,时间类支持 */
		public static final Operator regexp = new Operator("REGEXP");
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
