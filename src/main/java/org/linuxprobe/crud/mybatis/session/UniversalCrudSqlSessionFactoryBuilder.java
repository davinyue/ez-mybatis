package org.linuxprobe.crud.mybatis.session;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.linuxprobe.crud.mybatis.session.defaults.UniversalCrudDefaultSqlSessionFactory;

public class UniversalCrudSqlSessionFactoryBuilder extends SqlSessionFactoryBuilder {
	@Override
	public UniversalCrudSqlSessionFactory build(Reader reader) {
		return this.build(reader, null, null);
	}

	@Override
	public UniversalCrudSqlSessionFactory build(Reader reader, String environment) {
		return this.build(reader, environment, null);
	}

	@Override
	public UniversalCrudSqlSessionFactory build(Reader reader, Properties properties) {
		return this.build(reader, null, properties);
	}

	@Override
	public UniversalCrudSqlSessionFactory build(Reader reader, String environment, Properties properties) {
		XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
		return this.build(parser.parse());
	}

	@Override
	public UniversalCrudSqlSessionFactory build(InputStream inputStream) {
		return this.build(inputStream, null, null);
	}

	@Override
	public UniversalCrudSqlSessionFactory build(InputStream inputStream, String environment) {
		return this.build(inputStream, environment, null);
	}

	@Override
	public UniversalCrudSqlSessionFactory build(InputStream inputStream, Properties properties) {
		return this.build(inputStream, null, properties);
	}

	@Override
	public UniversalCrudSqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
		XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
		return this.build(parser.parse());
	}

	@Override
	public UniversalCrudSqlSessionFactory build(Configuration config) {
		return new UniversalCrudDefaultSqlSessionFactory(config);
	}
}
