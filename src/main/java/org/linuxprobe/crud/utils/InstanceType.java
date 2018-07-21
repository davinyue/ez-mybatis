package org.linuxprobe.crud.utils;

import java.util.Collection;

/** 实例类型判断 */
public class InstanceType {
	/** 判读是否为空 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).isEmpty();
		}
		if ((obj instanceof String)) {
			return "".equals(((String) obj).trim());
		}
		if ((obj instanceof StringBuffer)) {
			String strobj = ((StringBuffer) obj).toString();
			return "".equals(strobj.trim());
		}
		return false;
	}

	/** 是否是整数 */
	public static boolean isInteger(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof String) {
			try {
				Integer.parseInt((String) obj);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		if ((obj instanceof Integer)) {
			return true;
		}
		return false;
	}

	/** 是否是长整数 */
	public static boolean isLong(Object obj) {
		if (obj == null) {
			return false;
		}
		if ((obj instanceof String)) {
			try {
				Long.parseLong((String) obj);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		if ((obj instanceof Long)) {
			return true;
		}
		return false;
	}

	/** 是否是单精度浮点数 */
	public static boolean isFloat(Object obj) {
		if (obj == null) {
			return false;
		}
		if ((obj instanceof String)) {
			try {
				Float.parseFloat((String) obj);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		if ((obj instanceof Float)) {
			return true;
		}
		return false;
	}

	/** 是否是双精度浮点数 */
	public static boolean isDouble(Object obj) {
		if (obj == null) {
			return false;
		}
		if ((obj instanceof String)) {
			try {
				Double.parseDouble((String) obj);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		if ((obj instanceof Double)) {
			return true;
		}
		return false;
	}

	/** 是否是数字 */
	public static boolean isNumber(Object obj) {
		return isInteger(obj) || isLong(obj) || isFloat(obj) || isDouble(obj);
	}
}