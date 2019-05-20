package org.linuxprobe.crud.utils;

/** sql关键字转义工具 */
public class SqlEscapeUtil {
	private static String[] mysqlFbsArr = { "\\", "'" };

	/** 转义 */
	public static String mysqlEscape(String source) {
		if (source != null) {
			for (String key : mysqlFbsArr) {
				if (source.contains(key)) {
					source = source.replace(key, "\\" + key);
				}
			}
		}
		return source;
	}
}
