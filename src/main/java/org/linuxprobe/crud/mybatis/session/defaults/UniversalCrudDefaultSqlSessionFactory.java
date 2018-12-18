package org.linuxprobe.crud.mybatis.session.defaults;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.linuxprobe.crud.core.content.UniversalCrudContent;
import org.linuxprobe.crud.mybatis.session.UniversalCrudConfiguration;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSession;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSessionFactory;

public class UniversalCrudDefaultSqlSessionFactory extends DefaultSqlSessionFactory
		implements UniversalCrudSqlSessionFactory {

	/** mybatis配置 */
	public UniversalCrudDefaultSqlSessionFactory(UniversalCrudConfiguration configuration) {
		super(configuration);
		if (!configuration.hasMapper(org.linuxprobe.crud.mapper.UniversalMapper.class)) {
			configuration.addMapper(org.linuxprobe.crud.mapper.UniversalMapper.class);
		}
		/** 初始化univerCrud */
		UniversalCrudContent.init(configuration);
	}

	@Override
	public UniversalCrudSqlSession openSession() {
		return openSessionFromDataSource(getConfiguration().getDefaultExecutorType(), null, false);
	}

	@Override
	public UniversalCrudSqlSession openSession(boolean autoCommit) {
		return openSessionFromDataSource(getConfiguration().getDefaultExecutorType(), null, autoCommit);
	}

	@Override
	public UniversalCrudSqlSession openSession(ExecutorType execType) {
		return openSessionFromDataSource(execType, null, false);
	}

	@Override
	public UniversalCrudSqlSession openSession(TransactionIsolationLevel level) {
		return openSessionFromDataSource(getConfiguration().getDefaultExecutorType(), level, false);
	}

	@Override
	public UniversalCrudSqlSession openSession(ExecutorType execType, TransactionIsolationLevel level) {
		return openSessionFromDataSource(execType, level, false);
	}

	@Override
	public UniversalCrudSqlSession openSession(ExecutorType execType, boolean autoCommit) {
		return openSessionFromDataSource(execType, null, autoCommit);
	}

	@Override
	public UniversalCrudSqlSession openSession(Connection connection) {
		return openSessionFromConnection(getConfiguration().getDefaultExecutorType(), connection);
	}

	@Override
	public UniversalCrudSqlSession openSession(ExecutorType execType, Connection connection) {
		return openSessionFromConnection(execType, connection);
	}

	private UniversalCrudSqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level,
			boolean autoCommit) {
		Transaction tx = null;
		try {
			final Environment environment = getConfiguration().getEnvironment();
			final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
			tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
			final Executor executor = getConfiguration().newExecutor(tx, execType);
			return new UniversalCrudDefaultSqlSession(getConfiguration(), executor, autoCommit);
		} catch (Exception e) {
			closeTransaction(tx);
			throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
		} finally {
			ErrorContext.instance().reset();
		}
	}

	private UniversalCrudSqlSession openSessionFromConnection(ExecutorType execType, Connection connection) {
		try {
			boolean autoCommit;
			try {
				autoCommit = connection.getAutoCommit();
			} catch (SQLException e) {
				autoCommit = true;
			}
			final Environment environment = getConfiguration().getEnvironment();
			final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
			final Transaction tx = transactionFactory.newTransaction(connection);
			final Executor executor = getConfiguration().newExecutor(tx, execType);
			return new UniversalCrudDefaultSqlSession(getConfiguration(), executor, autoCommit);
		} catch (Exception e) {
			throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
		} finally {
			ErrorContext.instance().reset();
		}
	}

	private TransactionFactory getTransactionFactoryFromEnvironment(Environment environment) {
		if (environment == null || environment.getTransactionFactory() == null) {
			return new ManagedTransactionFactory();
		}
		return environment.getTransactionFactory();
	}

	private void closeTransaction(Transaction tx) {
		if (tx != null) {
			try {
				tx.close();
			} catch (SQLException ignore) {
			}
		}
	}

	@Override
	public UniversalCrudConfiguration getConfiguration() {
		return (UniversalCrudConfiguration) super.getConfiguration();
	}
}
