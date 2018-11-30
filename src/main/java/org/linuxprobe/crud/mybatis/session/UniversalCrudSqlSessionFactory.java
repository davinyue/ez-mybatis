package org.linuxprobe.crud.mybatis.session;

import java.sql.Connection;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;

public interface UniversalCrudSqlSessionFactory extends SqlSessionFactory {
	@Override
	UniversalCrudSqlSession openSession();

	@Override
	UniversalCrudSqlSession openSession(boolean autoCommit);

	@Override
	UniversalCrudSqlSession openSession(Connection connection);

	@Override
	UniversalCrudSqlSession openSession(TransactionIsolationLevel level);

	@Override
	UniversalCrudSqlSession openSession(ExecutorType execType);

	@Override
	UniversalCrudSqlSession openSession(ExecutorType execType, boolean autoCommit);

	@Override
	UniversalCrudSqlSession openSession(ExecutorType execType, TransactionIsolationLevel level);

	@Override
	UniversalCrudSqlSession openSession(ExecutorType execType, Connection connection);
	
	public UniversalCrudConfiguration getConfiguration();
}
