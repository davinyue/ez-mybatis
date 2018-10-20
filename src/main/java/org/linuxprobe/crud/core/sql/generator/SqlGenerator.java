package org.linuxprobe.crud.core.sql.generator;

public class SqlGenerator {
	private static DataBaseType dataBaseType;

	public static enum DataBaseType {
		Mysql, Postgre;
	}

	/** 关键字转义字符 */
	private static String escapeCharacter;

	public static DataBaseType getDataBaseType() {
		return dataBaseType;
	}

	public static void setDataBaseType(DataBaseType dataBaseType) {
		SqlGenerator.dataBaseType = dataBaseType;
		if (DataBaseType.Mysql.equals(dataBaseType)) {
			SqlGenerator.escapeCharacter = "`";
		} else if (DataBaseType.Postgre.equals(dataBaseType)) {
			SqlGenerator.escapeCharacter = "\"";
		}
	}

	/** 获取转义系统关键字用的字符 */
	public static String getEscapeCharacter() {
		return escapeCharacter;
	}
}
