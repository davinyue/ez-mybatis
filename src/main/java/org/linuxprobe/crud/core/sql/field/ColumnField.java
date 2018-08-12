package org.linuxprobe.crud.core.sql.field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnField {
	/** 数据库对于列 */
	private String column;
	/** 成员名称 */
	private String name;
	/** 成员值 */
	private String value;
	/** 是否是主键 */
	Boolean isPrimaryKey;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnField other = (ColumnField) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		return true;
	}
}
