package org.linuxprobe.crud.mybatis.session;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UniversalCrudConfiguration extends Configuration {
	/** 实体和查询类扫描路径 */
	protected String universalCrudScan;

	public String getDriverClassName() {
		DataSource dataSource = this.getEnvironment().getDataSource();
		if (PooledDataSource.class.isAssignableFrom(dataSource.getClass())) {
			return ((PooledDataSource) dataSource).getDriver();
		} else {
			Method getDriverMethod = null;
			try {
				getDriverMethod = dataSource.getClass().getMethod("getDriverClassName");
			} catch (NoSuchMethodException | SecurityException e1) {
				return null;
			}
			try {
				return (String) getDriverMethod.invoke(dataSource);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				return null;
			}
		}
	}
}
