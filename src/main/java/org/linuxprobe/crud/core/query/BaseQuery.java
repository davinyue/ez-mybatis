package org.linuxprobe.crud.core.query;

import java.util.LinkedList;
import java.util.List;
import org.linuxprobe.crud.core.query.param.impl.StringParam;
import org.linuxprobe.crud.core.sql.generator.SelectSqlGenerator;
import org.linuxprobe.crud.exception.ParameterException;
import lombok.Getter;
import lombok.Setter;

/** 实体查询dto */
public abstract class BaseQuery {
	/** 因为mybatis不能直接调用参数的方法，但能调用参数成员的方法，故这个类就出现了 */

	public class Sqlr {
		public String toSelectSql() throws Exception {
			return SelectSqlGenerator.toSelectSql(BaseQuery.this);
		}

		public String toSelectCountSql() throws Exception {
			return SelectSqlGenerator.toSelectCountSql(BaseQuery.this);
		}

		public String toSelectCountSql(String clounm) throws Exception {
			return SelectSqlGenerator.toSelectCountSql(BaseQuery.this, clounm);
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

	public void setOrder(String order) {
		if (order == null) {
			return;
		}
		order = order.trim();
		if (order.isEmpty()) {
			return;
		}
		String[] orders = order.split(",");
		StringBuffer result = new StringBuffer();
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
					throw new ParameterException("参数格式错误，eg:单字段排序'name desc',多字段排序'name desc, code asc, email desc'");
				} else if (orderMembers.size() == 1) {
					result.append(orderMembers.get(0) + " " + "asc,");
				} else if (orderMembers.size() == 2) {
					if (orderMembers.get(1).equalsIgnoreCase("asc")) {
						result.append(orderMembers.get(0) + " " + "asc,");
					} else if (orderMembers.get(1).equalsIgnoreCase("desc")) {
						result.append(orderMembers.get(0) + " " + "desc,");
					} else {
						throw new ParameterException("排序模式只能为asc和desc");
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
