package org.linuxprobe.crud.mybatis.session;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.sql.DataSource;
import org.apache.ibatis.session.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UniversalCrudConfiguration extends Configuration {
	/** 实体和查询类扫描路径 */
	protected String universalCrudScan;

	private String driver;

	public String getDriver() {
		if (driver != null) {
			return driver;
		}
		Method getDriverMethod = null;
		DataSource dataSource = this.getEnvironment().getDataSource();
		try {
			getDriverMethod = dataSource.getClass().getMethod("getDriver");
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
		try {
			driver = (String) getDriverMethod.invoke(dataSource);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
		return driver;
	}
}
