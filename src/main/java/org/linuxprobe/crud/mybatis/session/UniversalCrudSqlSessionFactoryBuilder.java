package org.linuxprobe.crud.mybatis.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.linuxprobe.crud.mybatis.session.builder.xml.UniversalCrudXMLConfigBuilder;
import org.linuxprobe.crud.mybatis.session.defaults.UniversalCrudDefaultSqlSessionFactory;

public class UniversalCrudSqlSessionFactoryBuilder {

	public UniversalCrudSqlSessionFactory build(Reader reader) {
		return build(reader, null, null);
	}

	public UniversalCrudSqlSessionFactory build(Reader reader, String environment) {
		return build(reader, environment, null);
	}

	public UniversalCrudSqlSessionFactory build(Reader reader, Properties properties) {
		return build(reader, null, properties);
	}

	public UniversalCrudSqlSessionFactory build(Reader reader, String environment, Properties properties) {
		try {
			UniversalCrudXMLConfigBuilder parser = new UniversalCrudXMLConfigBuilder(reader, environment, properties);
			return build(parser.parse());
		} catch (Exception e) {
			throw ExceptionFactory.wrapException("Error building SqlSession.", e);
		} finally {
			ErrorContext.instance().reset();
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
	}

	public UniversalCrudSqlSessionFactory build(InputStream inputStream) {
		return build(inputStream, null, null);
	}

	public UniversalCrudSqlSessionFactory build(InputStream inputStream, String environment) {
		return build(inputStream, environment, null);
	}

	public UniversalCrudSqlSessionFactory build(InputStream inputStream, Properties properties) {
		return build(inputStream, null, properties);
	}

	public UniversalCrudSqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
		try {
			UniversalCrudXMLConfigBuilder parser = new UniversalCrudXMLConfigBuilder(inputStream, environment,
					properties);
			return build(parser.parse());
		} catch (Exception e) {
			throw ExceptionFactory.wrapException("Error building SqlSession.", e);
		} finally {
			ErrorContext.instance().reset();
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}
	}

	public UniversalCrudSqlSessionFactory build(UniversalCrudConfiguration config) {
		return new UniversalCrudDefaultSqlSessionFactory(config);
	}
}
