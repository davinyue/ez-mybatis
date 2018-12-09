package org.linuxprobe.crud.core.query;

import java.util.LinkedList;
import java.util.List;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.core.query.param.impl.StringParam;
import org.linuxprobe.crud.core.sql.generator.SelectSqlGenerator;
import lombok.Getter;
import lombok.Setter;

/** 实体查询dto */
public abstract class BaseQuery {
	public BaseQuery() {
		this.alias = AliasGenerate.getAlias();
	}

	/** 因为mybatis不能直接调用参数的方法，但能调用参数成员的方法，故这个类就出现了 */
	public class Sqlr {
		public String toSelectSql() throws Exception {
			SelectSqlGenerator sqlGenerator = UniversalCrudContent.getSelectSqlGenerator();
			return sqlGenerator.toSelectSql(BaseQuery.this);
		}

		public String toSelectCountSql() throws Exception {
			SelectSqlGenerator sqlGenerator = UniversalCrudContent.getSelectSqlGenerator();
			return sqlGenerator.toSelectCountSql(BaseQuery.this);
		}

		public String toSelectCountSql(String clounm) throws Exception {
			SelectSqlGenerator sqlGenerator = UniversalCrudContent.getSelectSqlGenerator();
			return sqlGenerator.toSelectCountSql(BaseQuery.this, clounm);
		}
	}

	/** sql管理对象 */
	@Getter
	private Sqlr sqlr = new Sqlr();
	/** 排序 */
	@Getter
	private String order;
	/** 主键 */
	@Getter
	@Setter
	private StringParam id;

	/** 分页 */
	@Getter
	@Setter
	private Limit limit = new Limit();

	/** 别名 */
	@Getter
	@Setter
	private String alias;

	/** 被连接方式 */
	@Getter
	@Setter
	private JoinType joinType = JoinType.LeftJoin;

	public void setOrder(String order) {
		if (order == null) {
			return;
		}
		order = order.trim();
		if (order.isEmpty()) {
			return;
		}
		String[] orders = order.split(",");
		StringBuilder result = new StringBuilder();
		if (orders != null) {
			for (int i = 0; i < orders.length; i++) {
				String[] tempOrderMembers = orders[i].trim().split(" ");
				/** 删除值是空格的元素 */
				List<String> orderMembers = new LinkedList<>();
				for (String tempOrderMember : tempOrderMembers) {
					if (!tempOrderMember.trim().isEmpty()) {
						orderMembers.add(tempOrderMember);
					}
				}
				if (orderMembers.size() > 2) {
					throw new IllegalArgumentException(
							"参数格式错误，eg:单字段排序'name desc',多字段排序'name desc, code asc, email desc'");
				} else if (orderMembers.size() == 1) {
					result.append(orderMembers.get(0) + " " + "ASC,");
				} else if (orderMembers.size() == 2) {
					if (orderMembers.get(1).equalsIgnoreCase("asc")) {
						result.append(orderMembers.get(0) + " " + "ASC,");
					} else if (orderMembers.get(1).equalsIgnoreCase("desc")) {
						result.append(orderMembers.get(0) + " " + "DESC,");
					} else {
						throw new IllegalArgumentException("排序模式只能为asc和desc");
					}
				}
			}
		}
		if (result.indexOf(",") != -1) {
			result.delete(result.length() - 1, result.length());
		}
		this.order = result.length() == 0 ? null : result.toString();
	}

	public static class Limit {
		/** 开始行号 */
		private int startRow = 0;
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

		/** 获取当前页号 */
		public int getCurrentPage() {
			return currentPage;
		}

		/** 获取页大小 */
		public int getPageSize() {
			return pageSize;
		}

		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
			this.init();
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
			this.init();
		}

		public int getStartRow() {
			return this.startRow;
		}
	}

	public static enum JoinType {
		LeftJoin, RightJoin, FullJoin, InnerJoin, CrossJoin;
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
