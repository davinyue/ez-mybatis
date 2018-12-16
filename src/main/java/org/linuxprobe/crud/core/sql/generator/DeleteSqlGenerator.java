package org.linuxprobe.crud.core.sql.generator;

import java.io.Serializable;
import java.util.Collection;

public interface DeleteSqlGenerator {
	public String toDeleteSql(Object entity);

	public String toDeleteSqlByPrimaryKey(Serializable id, Class<?> type);

	public String toBatchDeleteSql(Collection<?> entitys);

	public <T extends Serializable> String toBatchDeleteSqlByPrimaryKey(Collection<T> ids, Class<?> type);
}
