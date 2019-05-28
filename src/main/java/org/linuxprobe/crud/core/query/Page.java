package org.linuxprobe.crud.core.query;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 分页信息 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {
	/** 数据 */
	private List<T> data;
	/** 总数量 */
	private Long total;
	/** 当前页号 */
	private Integer currentPage;
	/** 页大小 */
	private Integer pageSize;
}