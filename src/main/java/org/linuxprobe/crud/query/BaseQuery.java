package org.linuxprobe.crud.query;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.linuxprobe.crud.exception.ParameterException;
import org.linuxprobe.crud.query.param.QueryParam;
import org.linuxprobe.crud.query.param.impl.StringParam;
import org.linuxprobe.crud.utils.StringHumpTool;
import lombok.Getter;
import lombok.Setter;

/** 实体查询dto */
@Getter
@Setter
public class BaseQuery {
	public BaseQuery(String table) {
		this.table = table;
		this.alias = AliasGenerate.getAlias();
	}

	/** 因为mybatis不能直接调用参数的方法，但能调用参数成员的方法，故这个类就出现了 */
	public class Sqlr {
		public String toSelectSql() throws Exception {
			return BaseQuery.this.toSelectSql();
		}

		public String toSelectCountSql() throws Exception {
			return BaseQuery.this.toSelectCountSql();
		}

		public String toSelectCountSql(String clounm) throws Exception {
			return BaseQuery.this.toSelectCountSql(clounm);
		}
	}

	/** sql管理对象 */
	private Sqlr sqlr = new Sqlr();
	/** 表名 */
	private String table;
	/** 表别名 */
	private String alias;
	/** 排序 */
	private String order;
	/** 主键 */
	private StringParam id;
	/** 分页 */
	private Limit limit = new Limit();

	public void setOrder(String order) {
		String[] orders = order.split(",");
		StringBuffer result = new StringBuffer();
		if (orders != null) {
			for (int i = 0; i < orders.length; i++) {
				String[] orderMembers = orders[i].trim().split(" ");
				/** 如果参数使用多个空格来分隔列和模式，则模式在数组最后一个元素，把它赋值给数组第二个元素 */
				if (orderMembers.length > 2) {
					orderMembers[1] = orderMembers[orderMembers.length - 1];
				}
				if (orderMembers != null && orderMembers.length > 0) {
					String[] members = new String[2];
					members[0] = StringHumpTool.humpToLine2(orderMembers[0], "_");
					if (orderMembers.length == 1) {
						members[1] = "asc";
					} else {
						if (orderMembers[1].equalsIgnoreCase("asc")) {
							members[1] = "asc";
						} else if (orderMembers[1].equalsIgnoreCase("desc")) {
							members[1] = "desc";
						} else {
							throw new ParameterException("排序模式只能为asc和desc");
						}
					}
					if (i + 1 == orders.length) {
						result.append(members[0] + " " + members[1]);
					} else {
						result.append(members[0] + " " + members[1] + ",");
					}
				}
			}
		}
		this.order = result.toString();
	}

	private String toWhere(BaseQuery query) throws Exception {
		StringBuffer result = new StringBuffer();
		List<Field> fields = Arrays.asList(query.getClass().getDeclaredFields());
		fields = new ArrayList<Field>(fields);
		Class<?> superClass = query.getClass().getSuperclass();
		if (superClass != null) {
			for (;;) {
				fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
				if (superClass.equals(BaseQuery.class)) {
					break;
				} else {
					superClass = superClass.getSuperclass();
				}
			}
		}
		for (Field field : fields) {

			/** 如果是QueryParam的子类，则组合条件 */
			if (QueryParam.class.isAssignableFrom(field.getType())) {
				/** 参数名称 */
				String paramName = field.getName();
				/** 获取本次参数的方法 */
				String afterOfGet = paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
				Method getCurrnetParam = query.getClass().getMethod("get" + afterOfGet);
				/** 得到本次参数 */
				QueryParam currnetParam = (QueryParam) getCurrnetParam.invoke(query);
				if (currnetParam != null) {
					if (!currnetParam.isEmpty()) {
						if (result.length() > 0) {
							result.append("and " + query.getAlias() + "." + StringHumpTool.humpToLine2(paramName, "_")
									+ " " + currnetParam.toSqlPart() + " ");
						} else {
							result.append(query.getAlias() + "." + StringHumpTool.humpToLine2(paramName, "_") + " "
									+ currnetParam.toSqlPart() + " ");
						}
					}
				}
			}
			/** 关联对象参数 */
			else if (field.getType().getSuperclass().equals(BaseQuery.class)) {
				/** 参数名称 */
				String paramName = field.getName();
				/** 获取本次参数的方法 */
				String afterOfGet = paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
				Method getCurrnetParam = query.getClass().getMethod("get" + afterOfGet);
				/** 得到本次参数 */
				BaseQuery currnetParam = (BaseQuery) getCurrnetParam.invoke(query);
				if (currnetParam != null) {
					String otherWhere = toWhere(currnetParam);
					if (otherWhere != null && !otherWhere.trim().isEmpty()) {
						if (result.length() > 0) {
							result.append("and " + otherWhere);
						} else {
							result.append(otherWhere);
						}
					}
				}
			}
		}
		return result.toString();
	}

	/**
	 * @param principal
	 *            包含subordinate
	 * @param subordinate
	 *            被principal包含
	 * @param name
	 *            subordinate的名称
	 * @throws Exception
	 */
	private String toLeftJoin(BaseQuery principal, BaseQuery subordinate, String name) throws Exception {
		if (principal == null || subordinate == null || name == null || name.trim().isEmpty()) {
			return "";
		}
		StringBuffer joinBuffer = new StringBuffer();
		joinBuffer.append("left join " + subordinate.getTable() + " as " + subordinate.getAlias());
		joinBuffer.append(" on " + principal.getAlias() + "." + StringHumpTool.humpToLine2(name + "Id", "_") + " = "
				+ subordinate.getAlias() + ".id");
		/** 处理更深层对象的join */
		joinBuffer.append(this.toJoin(subordinate));
		return joinBuffer.toString();
	}

	/** 把继承于BaseQueryDTO的对象传入,构造join连接 */
	private String toJoin(BaseQuery query) throws Exception {
		StringBuffer joinBuffer = new StringBuffer();
		List<Field> fields = Arrays.asList(query.getClass().getDeclaredFields());
		fields = new ArrayList<Field>(fields);
		Class<?> superClass = query.getClass().getSuperclass();
		if (superClass != null) {
			for (;;) {
				fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
				if (superClass.equals(BaseQuery.class)) {
					break;
				} else {
					superClass = superClass.getSuperclass();
				}
			}
		}
		for (Field field : fields) {
			String paramName = field.getName();
			/** 如果是baseQuery的子类,才需要处理join */
			if (BaseQuery.class.isAssignableFrom(field.getType())) {
				/** 获取本次参数的方法 */
				String afterOfGet = paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
				Method getCurrnetParam = query.getClass().getMethod("get" + afterOfGet);
				/** 得到本次参数 */
				BaseQuery currnetParam = (BaseQuery) getCurrnetParam.invoke(query);
				joinBuffer.append(" " + toLeftJoin(query, currnetParam, paramName) + " ");
			}
		}
		return joinBuffer.toString();
	}

	public String toSelectSql() throws Exception {
		StringBuffer sqlBuffer = new StringBuffer(
				"select distinct " + alias + ".* from " + table + " as " + alias + " ");
		sqlBuffer.append(toJoin(this) + " ");
		String where = toWhere(this);
		if (!where.trim().isEmpty())
			sqlBuffer.append("where " + where + " ");
		if (this.order != null) {
			sqlBuffer.append(" order by " + this.order + " ");
		}
		if (this.limit != null) {
			sqlBuffer.append("limit " + this.limit.toLimit());
		}
		return sqlBuffer.toString();
	}

	public String toSelectCountSql() throws Exception {
		return this.toSelectCountSql("id");
	}

	public String toSelectCountSql(String clounm) throws Exception {
		String countFun = "count(distinct " + this.alias + "." + clounm + ")";
		StringBuffer sqlBuffer = new StringBuffer("select " + countFun + " from " + table + " as " + alias + " ");
		sqlBuffer.append(toJoin(this) + " ");
		String where = toWhere(this);
		if (!where.trim().isEmpty())
			sqlBuffer.append("where " + where + " ");
		return sqlBuffer.toString();
	}

	public static class Limit {
		private int startRow;
		private int size;
		/** 当前页 */
		private int currentPage = 1;
		/** 页大小 */
		private int pageSize = 10;

		private void init() {
			if (currentPage < 1) {
				currentPage = 1;
			}
			if (pageSize < 1) {
				pageSize = 10;
			}
			this.startRow = (currentPage - 1) * pageSize;
			this.size = pageSize;
		}

		public Limit() {
		}

		public Limit(String currentPage, String pageSize) {
			this(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		}

		public Limit(int currentPage, int pageSize) {
			this.currentPage = currentPage;
			this.pageSize = pageSize;
			this.init();
		}

		/** 获取sql语句limit后面的后门部分 */
		public String toLimit() {
			this.init();
			return startRow + ", " + size;
		}

		/** 获取当前页号 */
		public int getCurrentPage() {
			this.init();
			return currentPage;
		}

		/** 获取页大小 */
		public int getPageSize() {
			this.init();
			return pageSize;
		}

		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
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
