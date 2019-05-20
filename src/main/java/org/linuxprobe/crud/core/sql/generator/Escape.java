package org.linuxprobe.crud.core.sql.generator;

public interface Escape {
	/** 转义字符串 */
	public abstract String escape(String value);

	/** 获取sql字符串引号 */
	public abstract String getQuotation();
}
