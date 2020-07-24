package org.linuxprobe.crud.core.query;

import lombok.Getter;
import lombok.Setter;
import org.linuxprobe.crud.utils.TableAliasGenerate;

import java.util.LinkedList;
import java.util.List;

/**
 * 实体查询dto
 *
 * @author LarryYu
 * @version 2.0.9.RELEASE
 */
@Getter
@Setter
public abstract class BaseQuery {
    /**
     * 排序
     */
    private String sort;
    /**
     * 分页
     */
    private Limit limit = new Limit();
    /**
     * 别名
     */
    private String alias;
    /**
     * 被连接方式
     */
    private JoinType joinType = JoinType.InnerJoin;

    /**
     * <p>
     * Constructor for BaseQuery.
     * </p>
     */
    public BaseQuery() {
        this.alias = TableAliasGenerate.getAlias();
    }

    /**
     * 请使用 {@link #setSort(String)}
     */
    @Deprecated
    public void setOrder(String order) {
        this.setSort(order);
    }

    /**
     * 设置排序
     */
    public void setSort(String order) {
        if (order == null) {
            return;
        }
        order = order.trim();
        if (order.isEmpty()) {
            return;
        }
        String[] orders = order.split(",");
        StringBuilder result = new StringBuilder();
        for (String s : orders) {
            String[] tempOrderMembers = s.trim().split(" ");
            // 删除值是空格的元素
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
                result.append(orderMembers.get(0)).append(" ").append("ASC,");
            } else if (orderMembers.size() == 2) {
                if (orderMembers.get(1).equalsIgnoreCase("asc")) {
                    result.append(orderMembers.get(0)).append(" ").append("ASC,");
                } else if (orderMembers.get(1).equalsIgnoreCase("desc")) {
                    result.append(orderMembers.get(0)).append(" ").append("DESC,");
                } else {
                    throw new IllegalArgumentException("排序模式只能为asc和desc");
                }
            }
        }
        if (result.indexOf(",") != -1) {
            result.delete(result.length() - 1, result.length());
        }
        this.sort = result.length() == 0 ? null : result.toString();
    }

    /**
     * join类型枚举
     */
    public static enum JoinType {
        LeftJoin, RightJoin, FullJoin, InnerJoin, CrossJoin
    }

    public static class Limit {
        /**
         * 开始行号
         */
        private int startRow = 0;
        /**
         * 当前页
         */
        private int currentPage = 1;
        /**
         * 页大小
         */
        private int pageSize = 10;

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

        private void init() {
            if (this.currentPage < 1) {
                this.currentPage = 1;
            }
            if (this.pageSize < 1) {
                this.pageSize = 10;
            }
            this.startRow = (this.currentPage - 1) * this.pageSize;
        }

        /**
         * 获取当前页号
         */
        public int getCurrentPage() {
            return this.currentPage;
        }

        /**
         * 设置当前页
         */
        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
            this.init();
        }

        /**
         * 获取页大小
         */
        public int getPageSize() {
            return this.pageSize;
        }

        /**
         * 设置页大小
         */
        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
            this.init();
        }

        /**
         * 获取开始行
         */
        public int getStartRow() {
            return this.startRow;
        }
    }
}

