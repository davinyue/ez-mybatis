package org.linuxprobe.crud.query;

import org.linuxprobe.crud.exception.ParameterException;
import org.linuxprobe.crud.persistence.SelectSqler;
import org.linuxprobe.crud.query.param.impl.StringParam;
import lombok.Getter;
import lombok.Setter;

/** 实体查询dto */
public abstract class BaseQuery {
	/** 因为mybatis不能直接调用参数的方法，但能调用参数成员的方法，故这个类就出现了 */

	public class Sqlr {
		public String toSelectSql() throws Exception {
			return SelectSqler.toSelectSql(BaseQuery.this);
		}

		public String toSelectCountSql() throws Exception {
			return SelectSqler.toSelectCountSql(BaseQuery.this);
		}

		public String toSelectCountSql(String clounm) throws Exception {
			return SelectSqler.toSelectCountSql(BaseQuery.this, clounm);
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
				String[] orderMembers = orders[i].trim().split(" ");
				/** 如果参数使用多个空格来分隔列和模式，则模式在数组最后一个元素，把它赋值给数组第二个元素 */
				if (orderMembers.length > 2) {
					orderMembers[1] = orderMembers[orderMembers.length - 1];
				}
				if (orderMembers != null && orderMembers.length > 0) {
					String[] members = new String[2];
					members[0] = orderMembers[0];
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
