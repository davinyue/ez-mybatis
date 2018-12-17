package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.linuxprobe.crud.core.annoatation.Column;
import org.linuxprobe.crud.core.annoatation.Query;
import org.linuxprobe.crud.core.content.EntityInfo;
import org.linuxprobe.crud.core.content.QueryInfo;
import org.linuxprobe.crud.core.content.QueryInfo.QueryFieldInfo;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.query.BaseQuery;
import org.linuxprobe.crud.core.query.BaseQuery.JoinType;
import org.linuxprobe.crud.core.query.param.QueryParam;
import org.linuxprobe.crud.core.sql.generator.SelectSqlGenerator;
import org.linuxprobe.crud.core.sql.generator.SqlGenerator;
import org.linuxprobe.crud.core.sql.generator.SqlGenerator.DataBaseType;
import org.linuxprobe.crud.utils.FieldUtil;
import org.linuxprobe.crud.utils.StringHumpTool;

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
		String idColumn = entityInfo.getPrimaryKey().getFiledColumn();
		if (String.class.isAssignableFrom(id.getClass())) {
			id = "'" + id + "'";
		}
		String sql = "SELECT * FROM `" + table + "` WHERE `" + idColumn + "` = " + id;
		return sql;
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

	/** 转换为left join part */
	private static StringBuilder toJoin(BaseQuery searcher) {
		StringBuilder joinBuffer = new StringBuilder();
		QueryInfo queryInfo = UniversalCrudContent.getQueryInfo(searcher.getClass());
		List<QueryFieldInfo> baseQueryFieldInfos = queryInfo.getBaseQueryFieldInfos();
		for (QueryFieldInfo queryFieldInfo : baseQueryFieldInfos) {
			Field field = queryFieldInfo.getField();
			/** 如果是关联查询对象 */
			if (field.getType().isAnnotationPresent(Query.class)) {
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
		}
		return joinBuffer;
	}

	/** 转换为order by part */
	private static StringBuilder toOrder(BaseQuery searcher) {
		StringBuilder result = new StringBuilder();
		if (BaseQuery.class.isAssignableFrom(searcher.getClass())) {
			BaseQuery baseQuery = searcher;
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
						throw new IllegalArgumentException(searcher.getClass().getName() + "类查询对象里没有与'" + fieldName
								+ "'对应的字段,如果这是一个深层次排序，这可能是关联查询对象未赋值引起的");
					}
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
		List<Field> searcherFields = FieldUtil.getAllFields(searcher.getClass());
		for (Field searcherField : searcherFields) {
			/**
			 * 两种情况，1指定了单层次排序，eg:name desc;2指定了深层次排序,eg:parent.name asc
			 */
			/** 第一种情况 */
			if (fieldNames.length == 1) {
				/** 如果名称匹配上 */
				if (searcherField.getName().equals(fieldName)) {
					/** 如果是查询类参数对象 */
					if (QueryParam.class.isAssignableFrom(searcherField.getType())) {
						String orderName = "`" + searcher.getAlias() + "`.`"
								+ StringHumpTool.humpToLine2(fieldName, "_") + "`";
						/** 如果标有列注解 */
						if (searcherField.isAnnotationPresent(Column.class)) {
							Column column = searcherField.getAnnotation(Column.class);
							if (!column.value().trim().isEmpty()) {
								orderName = "`" + searcher.getAlias() + "`.`" + column.value().trim() + "`";
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
						BaseQuery sonSearcher = (BaseQuery) searcherField.get(searcher);
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

	/** 转换为分页部分 */
	private static String toLimit(Object searcher) {
		if (BaseQuery.class.isAssignableFrom(searcher.getClass())) {
			BaseQuery baseQuery = (BaseQuery) searcher;
			if (baseQuery.getLimit() != null) {
				if (DataBaseType.Mysql.equals(SqlGenerator.getDataBaseType())) {
					return "LIMIT " + baseQuery.getLimit().getStartRow() + "," + baseQuery.getLimit().getPageSize()
							+ " ";
				} else if (DataBaseType.Postgre.equals(SqlGenerator.getDataBaseType())) {
					return "LIMIT " + baseQuery.getLimit().getPageSize() + " OFFSET "
							+ baseQuery.getLimit().getStartRow() + " ";
				}
			}
		}
		return "";
	}

	/** 转换为where part */
	private static LinkedList<String> toWhere(BaseQuery searcher) {
		List<Field> fields = FieldUtil.getAllFields(searcher.getClass());
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
					result.add(baseMember.getCondition() + " `" + searcher.getAlias() + "`.`" + columnName + "` "
							+ baseMember.toSqlPart() + " ");
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
	private static StringBuilder getFormatWhere(BaseQuery searcher) {
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
		return UniversalCrudContent.getEntityInfo(modelType).getPrimaryKey().getFiledColumn();
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
}
