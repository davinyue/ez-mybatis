package org.linuxprobe.crud.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.linuxprobe.crud.exception.OperationNotSupportedException;
import org.linuxprobe.crud.exception.ParameterException;
import org.linuxprobe.crud.persistence.annotation.Column;
import org.linuxprobe.crud.persistence.annotation.PrimaryKey;
import org.linuxprobe.crud.persistence.annotation.Search;
import org.linuxprobe.crud.persistence.annotation.Table;
import org.linuxprobe.crud.query.BaseQuery;
import org.linuxprobe.crud.query.param.QueryParam;
import org.linuxprobe.crud.utils.StringHumpTool;

public class SelectSqler {
	private static ThreadLocal<Map<Object, String>> alias = new ThreadLocal<>();

	private static Map<Object, String> getAlias() {
		Map<Object, String> result = SelectSqler.alias.get();
		if (result == null) {
			SelectSqler.alias.set(new HashMap<>());
			result = SelectSqler.alias.get();
		}
		return result;
	}

	private static String getAlias(Object key) {
		Map<Object, String> alias = getAlias();
		String result = alias.get(key);
		if (result == null) {
			alias.put(key, AliasGenerate.getAlias());
			result = alias.get(key);
		}
		return result;
	}

	/** 转换为查询sql */
	public static String toSelectSql(Object searcher) {
		String alias = getAlias(searcher);
		StringBuffer sqlBuffer = new StringBuffer(
				"select distinct " + alias + ".* from " + getTable(searcher.getClass()) + " as " + alias + " ");
		sqlBuffer.append(toLeftJoin(searcher));
		sqlBuffer.append(getFormatWhere(searcher));
		sqlBuffer.append(toOrder(searcher));
		sqlBuffer.append(toLimit(searcher));
		return sqlBuffer.toString();
	}

	/** 转换为查询数量的sql */
	public static String toSelectCountSql(Object searcher, String clounm) {
		String countFun = "count(distinct " + getAlias(searcher) + "." + clounm + ")";
		StringBuffer sqlBuffer = new StringBuffer(
				"select " + countFun + " from " + getTable(searcher.getClass()) + " as " + getAlias(searcher) + " ");
		sqlBuffer.append(toLeftJoin(searcher));
		sqlBuffer.append(getFormatWhere(searcher));
		return sqlBuffer.toString();
	}

	/** 转换为查询主键数量的sql */
	public static String toSelectCountSql(Object searcher) {
		String countFun = "count(distinct " + getAlias(searcher) + "."
				+ getPrimaryKeyName(getModelType(searcher.getClass())) + ")";
		StringBuffer sqlBuffer = new StringBuffer(
				"select " + countFun + " from " + getTable(searcher.getClass()) + " as " + getAlias(searcher) + " ");
		sqlBuffer.append(toLeftJoin(searcher));
		sqlBuffer.append(getFormatWhere(searcher));
		return sqlBuffer.toString();
	}

	/** 转换为left join part */
	private static StringBuffer toLeftJoin(Object searcher) {
		List<Field> fields = geteAllFields(searcher.getClass());
		StringBuffer joinBuffer = new StringBuffer();
		for (Field field : fields) {
			/** 如果是关联查询对象 */
			if (field.getType().isAnnotationPresent(Search.class)) {
				/** 获取成员名称 */
				String fieldName = field.getName();
				/** 获取本次参数的方法 */
				String funSuffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method getCurrnetParam = null;
				try {
					getCurrnetParam = searcher.getClass().getMethod("get" + funSuffix);
				} catch (NoSuchMethodException | SecurityException e) {
					continue;
				}
				/** 获得该对象 */
				Object member = null;
				try {
					member = getCurrnetParam.invoke(searcher);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					continue;
				}
				/** 如果对象不为空，则需要join */
				if (member != null) {
					/** 获取列名称 */
					String columnName = StringHumpTool.humpToLine2(fieldName + "Id", "_");
					if (field.isAnnotationPresent(Column.class)) {
						Column column = field.getAnnotation(Column.class);
						if (!column.value().trim().isEmpty()) {
							columnName = column.value();
						}
					}
					/** 获取需要链接的表名 */
					String joinTable = getTable(field.getType());
					String joinTableAlias = getAlias(member);
					joinBuffer.append("left join " + joinTable + " as " + joinTableAlias + " on ");
					joinBuffer.append(joinTableAlias + "." + getPrimaryKeyName(getModelType(field.getType())) + " = ");
					joinBuffer.append(getAlias(searcher) + "." + columnName + " ");
					joinBuffer.append(toLeftJoin(member));
				}
			}
		}
		return joinBuffer;
	}

	/** 转换为order by part */
	private static StringBuffer toOrder(Object searcher) {
		StringBuffer result = new StringBuffer();
		if (BaseQuery.class.isAssignableFrom(searcher.getClass())) {
			BaseQuery baseQuery = (BaseQuery) searcher;
			String strOrder = baseQuery.getOrder();
			if (strOrder != null) {
				String[] orders = strOrder.split(",");
				for (int i = 0; i < orders.length; i++) {
					String[] parts = orders[i].split(" ");
					String fieldName = parts[0];
					String orderName = getOrderMember(searcher, fieldName);
					if (orderName != null) {
						result.append(orderName + " " + parts[1] + ", ");
					} else {
						throw new ParameterException(searcher.getClass().getName() + "类查询对象里没有与'" + fieldName
								+ "'对应的字段,如果这是一个深层次排序，这可能是关联查询对象未赋值引起的");
					}
				}
			}
		}
		if (result.indexOf(", ") != -1) {
			result.delete(result.length() - 2, result.length());
			result.insert(0, "order by ");
		}
		result.append(" ");
		return result;
	}

	private static String getOrderMember(Object searcher, String fieldName) {
		/** 拆分层次 */
		String[] fieldNames = fieldName.split("\\.");
		/** 获取查询对象的成员 */
		List<Field> searcherFields = geteAllFields(searcher.getClass());
		for (Field searcherField : searcherFields) {
			/**
			 * 两种情况，1指定了单层次排序，eg:name desc;2指定了深层次排序,eg:parent.name asc
			 */
			/** 第一章情况 */
			if (fieldNames.length == 1) {
				/** 如果名称匹配上 */
				if (searcherField.getName().equals(fieldName)) {
					/** 如果是查询类参数对象 */
					if (QueryParam.class.isAssignableFrom(searcherField.getType())) {
						String orderName = getAlias(searcher) + "." + fieldName;
						/** 如果标有列注解 */
						if (searcherField.isAnnotationPresent(Column.class)) {
							Column column = searcherField.getAnnotation(Column.class);
							if (!column.value().trim().isEmpty()) {
								orderName = getAlias(searcher) + "." + column.value().trim();
							}
						}
						return orderName;
					}
					break;
				}
			}
			/** 第二种情况 */
			else {
				/** 如果第一层名称匹配上 */
				if (searcherField.getName().equals(fieldNames[0])) {
					searcherField.setAccessible(true);
					try {
						Object sonSearcher = searcherField.get(searcher);
						if (sonSearcher != null) {
							return getOrderMember(sonSearcher, fieldName.substring(fieldName.indexOf(".") + 1));
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						continue;
					}
				}
			}
		}
		return null;
	}

	/** 转换为order by part */
	private static String toLimit(Object searcher) {
		if (BaseQuery.class.isAssignableFrom(searcher.getClass())) {
			BaseQuery baseQuery = (BaseQuery) searcher;
			if (baseQuery.getLimit() != null) {
				return "limit " + baseQuery.getLimit().toLimit() + " ";
			}
		}
		return "";
	}

	/** 转换为where part */
	private static LinkedList<String> toWhere(Object searcher) {
		List<Field> fields = geteAllFields(searcher.getClass());
		LinkedList<String> result = new LinkedList<>();
		for (Field field : fields) {
			/** 获取成员名称 */
			String fieldName = field.getName();
			/** 列名 */
			String columnName = StringHumpTool.humpToLine2(fieldName, "_");
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				if (!column.value().trim().isEmpty()) {
					columnName = column.value();
				}
			}
			/** 获取本次参数的方法 */
			String funSuffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method getCurrnetParam = null;
			try {
				getCurrnetParam = searcher.getClass().getMethod("get" + funSuffix);
			} catch (NoSuchMethodException | SecurityException e) {
				continue;
			}
			/** 获得该对象 */
			Object member = null;
			try {
				member = getCurrnetParam.invoke(searcher);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				continue;
			}
			/** 如果该成员是空值，则不需要加入where条件 */
			if (member == null) {
				continue;
			}
			/** 如果是基础查询对象 */
			if (QueryParam.class.isAssignableFrom(field.getType())) {
				QueryParam baseMember = (QueryParam) member;
				if (!baseMember.isEmpty()) {
					result.add(baseMember.getCondition() + " " + getAlias(searcher) + "." + columnName + " "
							+ baseMember.toSqlPart() + " ");
				}
			}
			/** 如果是关联查询对象 */
			else if (field.getType().isAnnotationPresent(Search.class)) {
				result.addAll(toWhere(member));
			}
		}
		return result;
	}

	/** 获取格式化后的where条件 */
	private static StringBuffer getFormatWhere(Object searcher) {
		List<String> wheres = toWhere(searcher);
		/** 把and的条件排序在前面 */
		Collections.sort(wheres, new Comparator<String>() {
			@Override
			public int compare(String str1, String str2) {
				return str1.trim().compareToIgnoreCase(str2.trim());
			}
		});
		StringBuffer resultBuffer = new StringBuffer();
		for (String where : wheres) {
			resultBuffer.append(where);
		}
		/** 有条件 */
		if (resultBuffer.indexOf("and") != -1 || resultBuffer.indexOf("or") != -1) {
			resultBuffer.delete(0, resultBuffer.indexOf(" "));
			resultBuffer.insert(0, "where");
		}
		return resultBuffer;
	}

	/**
	 * 获取要搜索的表名
	 * 
	 * @param searcherType
	 *            用于查询的对象类型
	 * @return 返回对象不会为空，没有结果会抛出异常
	 */
	private static String getTable(Class<?> searcherType) {
		Class<?> entityType = getModelType(searcherType);
		if (entityType.isAnnotationPresent(Table.class)) {
			Table table = entityType.getAnnotation(Table.class);
			if (table.value().trim().isEmpty()) {
				throw new OperationNotSupportedException(entityType.getName() + "类的@Table注解没有赋值");
			} else {
				return table.value();
			}
		} else {
			throw new OperationNotSupportedException(entityType.getName() + "类没有@Table注解");
		}
	}

	/** 获取一个类的所有成员，包括它的父类的 */
	private static List<Field> geteAllFields(Class<?> type) {
		List<Field> fields = Arrays.asList(type.getDeclaredFields());
		fields = new ArrayList<Field>(fields);
		Class<?> superClass = type.getSuperclass();
		if (superClass != null) {
			for (;;) {
				if (!superClass.equals(Object.class)) {
					fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
					superClass = superClass.getSuperclass();
				} else {
					break;
				}
			}
		}
		return fields;
	}

	/**
	 * 获取模型对应的表的主键列名称
	 * 
	 * @param modelType
	 *            模型的类型
	 */
	private static String getPrimaryKeyName(Class<?> modelType) {
		List<Field> fields = geteAllFields(modelType);
		String primaryKeyName = null;
		for (Field field : fields) {
			if (field.isAnnotationPresent(PrimaryKey.class)) {
				primaryKeyName = field.getName();
				if (field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					String strColumn = column.value().trim();
					if (!strColumn.isEmpty()) {
						primaryKeyName = strColumn;
					}
				}
				break;
			}
		}
		if (primaryKeyName == null) {
			throw new OperationNotSupportedException(modelType.getName() + "类的成员没有@PrimaryKey注解");
		}
		return primaryKeyName;
	}

	/**
	 * 获取要查询的模型类型
	 * 
	 * @param searcherType
	 *            用于查询的对象类型
	 * @return 返回对象不会为空，没有结果会抛出异常
	 */
	private static Class<?> getModelType(Class<?> searcherType) {
		if (searcherType == null) {
			throw new OperationNotSupportedException("传入对象不能为空");
		}
		if (searcherType.isAnnotationPresent(Search.class)) {
			Search search = searcherType.getAnnotation(Search.class);
			if (search.value() != null) {
				return search.value();
			} else {
				throw new OperationNotSupportedException("该类@Search注解没有赋值");
			}
		} else {
			throw new OperationNotSupportedException("该类没有标注@Search注解");
		}
	}
}

/** 生成表别名 */
class AliasGenerate {
	private static char first = 96;
	private static int second = 0;

	public static String getAlias(String prefix) {
		first++;
		if (first == 123) {
			first = 97;
		}
		second++;
		if (second == 10) {
			second = 1;
		}
		return prefix + String.valueOf(first) + second;
	}

	public static String getAlias() {
		return getAlias("t");
	}
}
