package org.linuxprobe.crud.core.sql.generator.impl.mysql;

import org.linuxprobe.crud.core.sql.generator.Escape;

public class MysqlEscape implements Escape {
	private static String[] mysqlFbsArr = { "\\", "'" };

	@Override
	public String escape(String value) {
		if (value != null) {
			for (String key : mysqlFbsArr) {
				if (value.contains(key)) {
					value = value.replace(key, "\\" + key);
				}
			}
		}
		return value;
	}

	@Override
	public String getQuotation() {
		return "'";
	}

}
