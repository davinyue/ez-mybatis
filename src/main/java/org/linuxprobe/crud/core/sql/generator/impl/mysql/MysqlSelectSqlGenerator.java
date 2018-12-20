package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.linuxprobe.crud.core.annoatation.Query;
import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.EntityInfo.FieldInfo;
import org.linuxprobe.crud.core.content.QueryInfo;
import org.linuxprobe.crud.core.content.QueryInfo.QueryFieldInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.query.BaseQuery.JoinType;
import org.linuxprobe.crud.core.query.param.BaseParam;
import org.linuxprobe.crud.core.query.param.BaseParam.Operator;
import org.linuxprobe.crud.core.query.param.impl.BooleanParam;
import org.linuxprobe.crud.core.query.param.impl.DateParam;
import org.linuxprobe.crud.core.query.param.impl.NumberParam;
import org.linuxprobe.crud.core.query.param.impl.StringParam;
import org.linuxprobe.crud.core.query.param.impl.StringParam.Fuzzt;
import org.linuxprobe.crud.core.sql.generator.SelectSqlGenerator;
import org.linuxprobe.crud.utils.FieldUtil;
import org.linuxprobe.crud.utils.SqlEscapeUtil;

public class MysqlSelectSqlGenerator implements SelectSqlGenerator {
	/** 转换为查询sql */
	@Override
	public String toSelectSql(BaseQuery searcher) {
		String alias = searcher.getAlias();
		StringBuilder sqlBuilder = new StringBuilder("SELECT DISTINCT `" + alias + "`.* FROM `"
				+ getTable(searcher.getClass()) + "` AS `" + searcher.getAlias() + "` ");
		sqlBuilder.append(toJoin(searcher));
		sqlBuilder.append(getFormatWhere(searcher));
		sqlBuilder.append(toOrder(searcher));
		sqlBuilder.append(toLimit(searcher));
		return sqlBuilder.toString();
	}

	/**
	 * 转换为查询sql
	 * 
	 * @param id        主键
	 * @param modelType model类型
	 * @return 返回生成sql
	 */
	@Override
	public String toSelectSql(Serializable id, Class<?> modelType) {
		if (id == null) {
			throw new IllegalArgumentException("id cannot be null");
		}
		if (id instanceof String) {
			if ("".equals(id)) {
				throw new IllegalArgumentException("id cannot be empty");
			}
		}
		if (modelType == null) {
			throw new IllegalArgumentException("modelType cannot be null");
		}
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(modelType);
		String table = entityInfo.getTableName();
		String idColumn = entityInfo.getPrimaryKey().getColumnName();
		if (String.class.isAssignableFrom(id.getClass())) {
			id = SqlEscapeUtil.mysqlEscape((String)id);
			id = "'" + id + "'";
		}
		String sql = "SELECT * FROM `" + table + "` WHERE `" + idColumn + "` = " + id;
		return sql;
	}
	
	@Override
	public String toSelectSql(String column, Serializable columnValue, Class<?> modelType) {
		if (column == null) {
			throw new IllegalArgumentException("column cannot be null");
		}
		if (columnValue instanceof String) {
			if ("".equals(columnValue)) {
				throw new IllegalArgumentException("columnValue cannot be empty");
			}
		}
		if (modelType == null) {
			throw new IllegalArgumentException("modelType cannot be null");
		}
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(modelType);
		String table = entityInfo.getTableName();
		if (String.class.isAssignableFrom(columnValue.getClass())) {
			columnValue = SqlEscapeUtil.mysqlEscape((String)columnValue);
			columnValue = "'" + columnValue + "'";
		}
		String sql = "SELECT * FROM `" + table + "` WHERE `" + column + "` = " + columnValue;
		return sql;
	}
	
	@Override
	public String toSelectSqlByFieldName(String fieldName, Serializable fieldValue, Class<?> modelType) {
		EntityInfo entityInfo = UniversalCrudContent.getEntityInfo(modelType);
		List<FieldInfo> fieldInfos = entityInfo.getFieldInfos();
		String columnName = null;
		for(FieldInfo fieldInfo:fieldInfos) {
			if(fieldInfo.getField().getName().equals(fieldName)) {
				columnName = fieldInfo.getColumnName();
			}
		}
		if(columnName==null) {
			throw new IllegalArgumentException(fieldName+" is not "+modelType.getClass().getName()+" field");
		}
		return this.toSelectSql(columnName, fieldValue, modelType);
	}

	/** 转换为查询数量的sql */
	@Override
	public String toSelectCountSql(BaseQuery searcher, String clounm) {
		String alias = searcher.getAlias();
		String countFun = "COUNT(DISTINCT `" + alias + "`.`" + clounm + "`)";
		StringBuilder sqlBuilder = new StringBuilder(
				"SELECT " + countFun + " FROM `" + getTable(searcher.getClass()) + "` AS `" + alias + "` ");
		sqlBuilder.append(toJoin(searcher));
		sqlBuilder.append(getFormatWhere(searcher));
		return sqlBuilder.toString();
	}

	/** 转换为查询主键数量的sql */
	@Override
	public String toSelectCountSql(BaseQuery searcher) {
		String alias = searcher.getAlias();
		String countFun = "COUNT(DISTINCT `" + alias + "`.`" + getPrimaryKeyName(getModelType(searcher.getClass()))
				+ "`)";
		StringBuilder sqlBuilder = new StringBuilder(
				"SELECT " + countFun + " FROM `" + getTable(searcher.getClass()) + "` AS `" + alias + "` ");
		sqlBuilder.append(toJoin(searcher));
		sqlBuilder.append(getFormatWhere(searcher));
		return sqlBuilder.toString();
	}

	/** 转换为join part */
	private static StringBuilder toJoin(BaseQuery searcher) {
		StringBuilder joinBuffer = new StringBuilder();
		QueryInfo queryInfo = UniversalCrudContent.getQueryInfo(searcher.getClass());
		/** 获取所有关联查询field */
		List<QueryFieldInfo> baseQueryFieldInfos = queryInfo.getBaseQueryFieldInfos();
		for (QueryFieldInfo queryFieldInfo : baseQueryFieldInfos) {
			Field field = queryFieldInfo.getField();
			/** 获得该对象 */
			BaseQuery member = (BaseQuery) FieldUtil.getFieldValue(searcher, field);
			/** 如果对象不为空，则需要join */
			if (member != null) {
				/** 设置主表链接列 */
				String principalColumn = queryFieldInfo.getPrincipalColumn();
				/** 从表链接列 */
				String subordinateColumn = queryFieldInfo.getSubordinateColumn();
				/** 获取需要链接的表名 */
				String joinTable = getTable(field.getType());
				String joinTableAlias = member.getAlias();
				/** 处理连接方式 */
				String joinStr = "LEFT";
				JoinType joinType = member.getJoinType();
				if (joinType.equals(JoinType.RightJoin)) {
					joinStr = "RIGHT";
				} else if (joinType.equals(JoinType.FullJoin)) {
					joinStr = "FULL";
				} else if (joinType.equals(JoinType.InnerJoin)) {
					joinStr = "INNER";
				} else if (joinType.equals(JoinType.CrossJoin)) {
					joinStr = "CROSS";
				}
				joinBuffer.append(joinStr + " JOIN `" + joinTable + "` AS `" + joinTableAlias + "` ON ");
				joinBuffer.append("`" + joinTableAlias + "`.`" + subordinateColumn + "` = ");
				joinBuffer.append("`" + searcher.getAlias() + "`.`" + principalColumn + "` ");
				joinBuffer.append(toJoin(member));
			}
		}
		return joinBuffer;
	}

	/** 转换为order by part */
	private static StringBuilder toOrder(BaseQuery baseQuery) {
		StringBuilder result = new StringBuilder();
		String strOrder = baseQuery.getOrder();
		if (strOrder != null) {
			String[] orders = strOrder.split(",");
			for (int i = 0; i < orders.length; i++) {
				String[] parts = orders[i].split(" ");
				String fieldName = parts[0];
				String orderName = getOrderMember(baseQuery, fieldName);
				if (orderName != null) {
					result.append(orderName + " " + parts[1] + ", ");
				} else {
					throw new IllegalArgumentException(baseQuery.getClass().getName() + "类查询对象里没有与'" + fieldName
							+ "'对应的字段,如果这是一个深层次排序，这可能是关联查询对象未赋值引起的");
				}
			}
		}
		if (result.indexOf(", ") != -1) {
			result.delete(result.length() - 2, result.length());
			result.insert(0, "ORDER BY ");
		}
		result.append(" ");
		return result;
	}

	private static String getOrderMember(BaseQuery searcher, String fieldName) {
		/** 拆分层次 */
		String[] fieldNames = fieldName.split("\\.");
		/** 获取查询对象的成员 */
		QueryInfo queryInfo = UniversalCrudContent.getQueryInfo(searcher.getClass());
		List<QueryFieldInfo> queryFieldInfos = queryInfo.getQueryFieldInfos();
		for (QueryFieldInfo queryFieldInfo : queryFieldInfos) {
			Field searcherField = queryFieldInfo.getField();
			/**
			 * 两种情况，1指定了单层次排序，eg:name desc;2指定了深层次排序,eg:parent.name asc
			 */
			/** 第一种情况 */
			if (fieldNames.length == 1) {
				/** 如果名称匹配上 */
				if (searcherField.getName().equals(fieldName)) {
					/** 如果是查询类参数对象 */
					if (BaseParam.class.isAssignableFrom(searcherField.getType())) {
						String orderName = "`" + searcher.getAlias() + "`.`" + queryFieldInfo.getColumnName() + "`";
						return orderName;
					}
					break;
				}
			}
			/** 第二种情况 */
			else {
				/** 如果第一层名称匹配上 */
				if (searcherField.getName().equals(fieldNames[0])) {
					if (BaseQuery.class.isAssignableFrom(searcherField.getType())) {
						BaseQuery sonSearcher = (BaseQuery) FieldUtil.getFieldValue(searcher, searcherField);
						if (sonSearcher != null) {
							return getOrderMember(sonSearcher, fieldName.substring(fieldName.indexOf(".") + 1));
						}
					} else {
						throw new IllegalArgumentException("深层次排序必须指定在连接查询上");
					}
				}
			}
		}
		return null;
	}

	/** 转换为分页部分 */
	private static String toLimit(BaseQuery baseQuery) {
		if (baseQuery.getLimit() != null) {
			return "LIMIT " + baseQuery.getLimit().getStartRow() + "," + baseQuery.getLimit().getPageSize() + " ";
		} else {
			return " ";
		}
	}

	/** 转换为where part */
	private LinkedList<String> toWhere(BaseQuery searcher) {
		LinkedList<String> result = new LinkedList<>();
		QueryInfo queryInfo = UniversalCrudContent.getQueryInfo(searcher.getClass());
		List<QueryFieldInfo> queryFieldInfos = queryInfo.getQueryFieldInfos();
		for (QueryFieldInfo queryFieldInfo : queryFieldInfos) {
			Field field = queryFieldInfo.getField();
			/** 列名 */
			String columnName = queryFieldInfo.getColumnName();
			/** 获得该对象 */
			Object member = FieldUtil.getFieldValue(searcher, field);
			/** 如果该成员是空值，则不需要加入where条件 */
			if (member == null) {
				continue;
			}
			/** 如果是基础查询对象 */
			if (BaseParam.class.isAssignableFrom(field.getType())) {
				@SuppressWarnings("unchecked")
				BaseParam<Serializable> baseMember = (BaseParam<Serializable>) member;
				if (!baseMember.isEmpty()) {
					result.add(baseMember.getCondition() + " `" + searcher.getAlias() + "`.`" + columnName + "` "
							+ this.paramToSqlPart(baseMember) + " ");
				}
			}
			/** 如果是关联查询对象 */
			else if (field.getType().isAnnotationPresent(Query.class)) {
				result.addAll(toWhere((BaseQuery) member));
			}
		}
		return result;
	}

	/** 获取格式化后的where条件 */
	private StringBuilder getFormatWhere(BaseQuery searcher) {
		List<String> wheres = toWhere(searcher);
		/** 把and的条件排序在前面 */
		Collections.sort(wheres, new Comparator<String>() {
			@Override
			public int compare(String str1, String str2) {
				return str1.trim().compareToIgnoreCase(str2.trim());
			}
		});
		StringBuilder resultBuffer = new StringBuilder();
		for (String where : wheres) {
			resultBuffer.append(where);
		}
		/** 有条件 */
		if (resultBuffer.indexOf("AND") != -1 || resultBuffer.indexOf("OR") != -1) {
			resultBuffer.delete(0, resultBuffer.indexOf(" "));
			resultBuffer.insert(0, "WHERE");
		}
		return resultBuffer;
	}

	/**
	 * 获取模型对应的表的主键列名称
	 * 
	 * @param modelType 模型的类型
	 */
	private static String getPrimaryKeyName(Class<?> modelType) {
		return UniversalCrudContent.getEntityInfo(modelType).getPrimaryKey().getColumnName();
	}

	/**
	 * 获取要查询的模型类型
	 * 
	 * @param searcherType 用于查询的对象类型
	 * @return 返回对象不会为空，没有结果会抛出异常
	 */
	private static Class<?> getModelType(Class<?> queryType) {
		if (queryType == null) {
			throw new IllegalArgumentException("queryType cant not be null");
		}
		QueryInfo queryInfo = UniversalCrudContent.getQueryInfo(queryType);
		Class<?> modelType = queryInfo.getQueryEntityCalss();
		return modelType;
	}

	/**
	 * 获取要搜索的表名
	 * 
	 * @param searcherType 用于查询的对象类型
	 * @return 返回对象不会为空，没有结果会抛出异常
	 */
	private static String getTable(Class<?> queryType) {
		QueryInfo queryInfo = UniversalCrudContent.getQueryInfo(queryType);
		Class<?> modelType = queryInfo.getQueryEntityCalss();
		return UniversalCrudContent.getEntityInfo(modelType).getTableName();
	}

	private String paramToSqlPart(BaseParam<? extends Serializable> param) {
		if (StringParam.class.isAssignableFrom(param.getClass())) {
			return this.stringParamToSqlPart((StringParam) param);
		} else if (DateParam.class.isAssignableFrom(param.getClass())) {
			return this.dateParamToSqlPart((DateParam) param);
		} else if (NumberParam.class.isAssignableFrom(param.getClass())) {
			return this.numberParamToSqlPart((NumberParam) param);
		} else if (BooleanParam.class.isAssignableFrom(param.getClass())) {
			return this.booleanParamToSqlPart((BooleanParam) param);
		} else {
			return " ";
		}
	}

	private String stringParamToSqlPart(StringParam param) {
		if (param == null || param.isEmpty()) {
			return " ";
		} else {
			String escape = "'";
			Operator operator = param.getOperator();
			if (operator == Operator.equal || operator == Operator.unequal || operator == Operator.more
					|| operator == Operator.less || operator == Operator.moreOrEqual || operator == Operator.lessOrEqual
					|| operator == Operator.regexp) {
				String value = param.getValue();
				value = SqlEscapeUtil.mysqlEscape(value);
				value = escape + value + escape;
				return operator.getOperator() + " " + value + " ";
			} else if (operator == Operator.like || operator == Operator.unlike) {
				String value = param.getValue();
				value = SqlEscapeUtil.mysqlEscape(value);
				if (Fuzzt.Left.equals(param.getFuzzt())) {
					value = "%" + value;
				} else if (Fuzzt.Right.equals(param.getFuzzt())) {
					value = value + "%";
				} else if (Fuzzt.All.equals(param.getFuzzt())) {
					value = "%" + value + "%";
				}
				value = escape + value + escape;
				return operator.getOperator() + " " + value + " ";
			} else if (operator == Operator.between || operator == Operator.notBetween) {
				String minValue = param.getMinValue();
				minValue = SqlEscapeUtil.mysqlEscape(minValue);
				minValue = escape + minValue + escape;
				String maxValue = param.getMaxValue();
				maxValue = SqlEscapeUtil.mysqlEscape(maxValue);
				maxValue = escape + maxValue + escape;
				return operator.getOperator() + " " + minValue + " AND " + maxValue + " ";
			} else if (operator == Operator.in || operator == Operator.notIn) {
				List<String> multiValues = param.getMultiValues();
				StringBuilder stringValues = new StringBuilder();
				for (int i = 0; i < multiValues.size(); i++) {
					String multiValue = multiValues.get(i);
					multiValue = SqlEscapeUtil.mysqlEscape(multiValue);
					if (i + 1 == multiValues.size()) {
						multiValue = escape + multiValue + escape;
					} else {
						multiValue = escape + multiValue + escape + ", ";
					}
					stringValues.append(multiValue);
				}
				return operator.getOperator() + "(" + stringValues.toString() + ") ";
			} else if (operator == Operator.isNull || operator == Operator.isNotNull) {
				return operator.getOperator() + " NULL ";
			} else {
				return " ";
			}
		}
	}

	private String dateParamToSqlPart(DateParam param) {
		SimpleDateFormat dateForma = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringParam stringParam = new StringParam();
		stringParam.setFuzzt(Fuzzt.Custom);
		stringParam.setCondition(param.getCondition());
		stringParam.setOperator(param.getOperator());
		if (param.getValue() != null) {
			stringParam.setValue(dateForma.format(param.getValue()));
		} else if (param.getMinValue() != null) {
			stringParam.setMinValue(dateForma.format(param.getMinValue()));
		} else if (param.getMaxValue() != null) {
			stringParam.setMaxValue(dateForma.format(param.getMaxValue()));
		} else if (param.getMultiValues() != null) {
			List<String> multiValues = new LinkedList<>();
			for (Date multiValue : param.getMultiValues()) {
				multiValues.add(dateForma.format(multiValue));
			}
			stringParam.setMultiValues(multiValues);
		}
		return this.stringParamToSqlPart(stringParam);
	}

	private String numberParamToSqlPart(NumberParam param) {
		if (param == null || param.isEmpty()) {
			return " ";
		} else {
			Operator operator = param.getOperator();
			if (operator == Operator.equal || operator == Operator.unequal || operator == Operator.more
					|| operator == Operator.less || operator == Operator.moreOrEqual
					|| operator == Operator.lessOrEqual) {
				return operator.getOperator() + " " + param.getValue() + " ";
			} else if (operator == Operator.between || operator == Operator.notBetween) {
				return operator.getOperator() + " " + param.getMinValue() + " AND " + param.getMaxValue() + " ";
			} else if (operator == Operator.in || operator == Operator.notIn) {
				List<Number> multiValues = param.getMultiValues();
				StringBuilder stringValues = new StringBuilder();
				for (int i = 0; i < multiValues.size(); i++) {
					Number multiValue = multiValues.get(i);
					if (i + 1 == multiValues.size()) {
						stringValues.append(multiValue);
					} else {
						stringValues.append(multiValue + ", ");
					}
				}
				return operator.getOperator() + "(" + stringValues.toString() + ") ";
			} else if (operator == Operator.isNull || operator == Operator.isNotNull) {
				return operator.getOperator() + " NULL ";
			} else {
				return " ";
			}
		}
	}

	private String booleanParamToSqlPart(BooleanParam param) {
		NumberParam numberParam = new NumberParam();
		numberParam.setCondition(param.getCondition());
		numberParam.setOperator(param.getOperator());
		if (param.getValue() != null) {
			Number value = param.getValue() ? 1 : 0;
			numberParam.setValue(value);
		} else if (param.getMinValue() != null) {
			Number minValue = param.getMinValue() ? 1 : 0;
			numberParam.setMinValue(minValue);
		} else if (param.getMaxValue() != null) {
			Number maxValue = param.getMaxValue() ? 1 : 0;
			numberParam.setMaxValue(maxValue);
		} else if (param.getMultiValues() != null) {
			List<Number> multiValues = new LinkedList<>();
			for (Boolean multiValue : param.getMultiValues()) {
				Number value = multiValue ? 1 : 0;
				multiValues.add(value);
			}
			numberParam.setMultiValues(multiValues);
		}
		return this.numberParamToSqlPart(numberParam);
	}
}
