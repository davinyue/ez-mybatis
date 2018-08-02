package org.linuxprobe.crud.utils;

/** mysql关键字转义工具 */
public class SqlEscapeUtil {
	private static String[] fbsArr = { "\\", "'" };

	/** 转义 */
	public static String escape(String source) {
		if (source != null) {
			for (String key : fbsArr) {
				if (source.contains(key)) {
					source = source.replace(key, "\\" + key);
				}
			}
		}
		return source;
	}
}
