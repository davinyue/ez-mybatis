package org.linuxprobe.crud.mybatis.spring;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.state;
import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.linuxprobe.crud.mybatis.session.UniversalCrudConfiguration;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSessionFactory;
import org.linuxprobe.crud.mybatis.session.UniversalCrudSqlSessionFactoryBuilder;
import org.linuxprobe.crud.mybatis.session.builder.xml.UniversalCrudXMLConfigBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import lombok.Getter;
import lombok.Setter;

/** org.mybatis.spring.SqlSessionFactoryBean */
@Getter
@Setter
public class UniversalCrudSqlSessionFactoryBean
		implements FactoryBean<UniversalCrudSqlSessionFactory>, ApplicationListener<ApplicationEvent> {
	private static final Log LOGGER = LogFactory.getLog(UniversalCrudSqlSessionFactoryBean.class);
	private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
	private static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();
	private boolean failFast;
	private DataSource dataSource;
	private Resource configLocation;
	private UniversalCrudConfiguration configuration;
	private Properties configurationProperties;
	private ObjectFactory objectFactory;
	private ObjectWrapperFactory objectWrapperFactory;
	private String typeAliasesPackage;
	private Class<?> typeAliasesSuperType;
	private Class<? extends VFS> vfs;
	private Interceptor[] plugins;
	private Class<?>[] typeAliases;
	private String typeHandlersPackage;
	private TypeHandler<?>[] typeHandlers;
	private DatabaseIdProvider databaseIdProvider;
	private Cache cache;
	private String environment = SqlSessionFactoryBean.class.getSimpleName();
	private TransactionFactory transactionFactory;
	private Resource[] mapperLocations;
	private UniversalCrudSqlSessionFactoryBuilder sqlSessionFactoryBuilder = new UniversalCrudSqlSessionFactoryBuilder();
	private UniversalCrudSqlSessionFactory sqlSessionFactory;
	/** 实体扫描路径 */
	private String universalCrudScan;

	private Set<Class<?>> scanClasses(String packagePatterns, Class<?> assignableType) throws IOException {
		Set<Class<?>> classes = new HashSet<>();
		String[] packagePatternArray = tokenizeToStringArray(packagePatterns,
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		for (String packagePattern : packagePatternArray) {
			Resource[] resources = RESOURCE_PATTERN_RESOLVER
					.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
							+ ClassUtils.convertClassNameToResourcePath(packagePattern) + "/**/*.class");
			for (Resource resource : resources) {
				try {
					ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource)
							.getClassMetadata();
					Class<?> clazz = Resources.classForName(classMetadata.getClassName());
					if (assignableType == null || assignableType.isAssignableFrom(clazz)) {
						classes.add(clazz);
					}
				} catch (Throwable e) {
					LOGGER.warn("Cannot load the '" + resource + "'. Cause by " + e.toString());
				}
			}
		}
		return classes;
	}

	protected UniversalCrudSqlSessionFactory buildSqlSessionFactory() throws Exception {
		final UniversalCrudConfiguration targetConfiguration;
		UniversalCrudXMLConfigBuilder xmlConfigBuilder = null;
		if (this.configuration != null) {
			targetConfiguration = this.configuration;
			if (targetConfiguration.getVariables() == null) {
				targetConfiguration.setVariables(this.configurationProperties);
			} else if (this.configurationProperties != null) {
				targetConfiguration.getVariables().putAll(this.configurationProperties);
			}
		} else if (this.configLocation != null) {
			xmlConfigBuilder = new UniversalCrudXMLConfigBuilder(this.configLocation.getInputStream(), null,
					this.configurationProperties);
			targetConfiguration = (UniversalCrudConfiguration) xmlConfigBuilder.getConfiguration();
		} else {
			LOGGER.debug(
					"Property 'configuration' or 'configLocation' not specified, using default MyBatis Configuration");
			targetConfiguration = new UniversalCrudConfiguration();
			Optional.ofNullable(this.configurationProperties).ifPresent(targetConfiguration::setVariables);
		}
		Optional.ofNullable(this.objectFactory).ifPresent(targetConfiguration::setObjectFactory);
		Optional.ofNullable(this.objectWrapperFactory).ifPresent(targetConfiguration::setObjectWrapperFactory);
		Optional.ofNullable(this.vfs).ifPresent(targetConfiguration::setVfsImpl);
		if (hasLength(this.typeAliasesPackage)) {
			scanClasses(this.typeAliasesPackage, this.typeAliasesSuperType)
					.forEach(targetConfiguration.getTypeAliasRegistry()::registerAlias);
		}
		if (!isEmpty(this.typeAliases)) {
			Stream.of(this.typeAliases).forEach(typeAlias -> {
				targetConfiguration.getTypeAliasRegistry().registerAlias(typeAlias);
				LOGGER.debug("Registered type alias: '" + typeAlias + "'");
			});
		}
		if (!isEmpty(this.plugins)) {
			Stream.of(this.plugins).forEach(plugin -> {
				targetConfiguration.addInterceptor(plugin);
				LOGGER.debug("Registered plugin: '" + plugin + "'");
			});
		}
		if (hasLength(this.typeHandlersPackage)) {
			scanClasses(this.typeHandlersPackage, TypeHandler.class).stream().filter(clazz -> !clazz.isInterface())
					.filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
					.filter(clazz -> ClassUtils.getConstructorIfAvailable(clazz) != null)
					.forEach(targetConfiguration.getTypeHandlerRegistry()::register);
		}
		if (!isEmpty(this.typeHandlers)) {
			Stream.of(this.typeHandlers).forEach(typeHandler -> {
				targetConfiguration.getTypeHandlerRegistry().register(typeHandler);
				LOGGER.debug("Registered type handler: '" + typeHandler + "'");
			});
		}
		if (this.databaseIdProvider != null) {// fix #64 set databaseId before parse mapper xmls
			try {
				targetConfiguration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
			} catch (SQLException e) {
				throw new NestedIOException("Failed getting a databaseId", e);
			}
		}
		Optional.ofNullable(this.cache).ifPresent(targetConfiguration::addCache);

		if (xmlConfigBuilder != null) {
			try {
				xmlConfigBuilder.parse();
				LOGGER.debug("Parsed configuration file: '" + this.configLocation + "'");
			} catch (Exception ex) {
				throw new NestedIOException("Failed to parse config resource: " + this.configLocation, ex);
			} finally {
				ErrorContext.instance().reset();
			}
		}
		targetConfiguration.setEnvironment(new Environment(this.environment,
				this.transactionFactory == null ? new SpringManagedTransactionFactory() : this.transactionFactory,
				this.dataSource));
		if (this.mapperLocations != null) {
			if (this.mapperLocations.length == 0) {
				LOGGER.warn("Property 'mapperLocations' was specified but matching resources are not found.");
			} else {
				for (Resource mapperLocation : this.mapperLocations) {
					if (mapperLocation == null) {
						continue;
					}
					try {
						XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(),
								targetConfiguration, mapperLocation.toString(), targetConfiguration.getSqlFragments());
						xmlMapperBuilder.parse();
					} catch (Exception e) {
						throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
					} finally {
						ErrorContext.instance().reset();
					}
					LOGGER.debug("Parsed mapper file: '" + mapperLocation + "'");
				}
			}
		} else {
			LOGGER.debug("Property 'mapperLocations' was not specified.");
		}
		configuration.setUniversalCrudScan(universalCrudScan);
		return this.sqlSessionFactoryBuilder.build(targetConfiguration);
	}

	public void afterPropertiesSet() throws Exception {
		notNull(universalCrudScan, "Property 'universalCrudScan' is required");
		notNull(dataSource, "Property 'dataSource' is required");
		notNull(sqlSessionFactoryBuilder, "Property 'sqlSessionFactoryBuilder' is required");
		state((configuration == null && configLocation == null) || !(configuration != null && configLocation != null),
				"Property 'configuration' and 'configLocation' can not specified with together");
		this.sqlSessionFactory = buildSqlSessionFactory();
	}

	@Override
	public UniversalCrudSqlSessionFactory getObject() throws Exception {
		if (this.sqlSessionFactory == null) {
			afterPropertiesSet();
		}
		return this.sqlSessionFactory;
	}

	@Override
	public Class<?> getObjectType() {
		return this.sqlSessionFactory == null ? UniversalCrudSqlSessionFactory.class
				: this.sqlSessionFactory.getClass();
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (failFast && event instanceof ContextRefreshedEvent) {
			this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
		}
	}
}
