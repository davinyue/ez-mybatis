package org.rdlinux.ezmybatis.core.interceptor.resultsethandler;

import org.apache.ibatis.annotations.AutomapConstructor;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.cursor.defaults.DefaultCursor;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.loader.ResultLoader;
import org.apache.ibatis.executor.loader.ResultLoaderMap;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.result.DefaultResultContext;
import org.apache.ibatis.executor.result.DefaultResultHandler;
import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetWrapper;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.*;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.util.MapUtil;
import org.rdlinux.ezmybatis.constant.MapRetKeyPattern;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.interceptor.executor.ResultMapInitLogic;
import org.rdlinux.ezmybatis.utils.HumpLineStringUtils;

import java.lang.reflect.Constructor;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class EzResultSetHandler extends DefaultResultSetHandler {
    private static final Object DEFERRED = new Object();

    private final Executor executor;
    private final Configuration configuration;
    private final MappedStatement mappedStatement;
    private final RowBounds rowBounds;
    private final ParameterHandler parameterHandler;
    private final ResultHandler<?> resultHandler;
    private final BoundSql boundSql;
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final ObjectFactory objectFactory;
    private final ReflectorFactory reflectorFactory;

    // nested resultmaps
    private final Map<CacheKey, Object> nestedResultObjects = new HashMap<>();
    private final Map<String, Object> ancestorObjects = new HashMap<>();
    // multiple resultsets
    private final Map<String, ResultMapping> nextResultMaps = new HashMap<>();
    private final Map<CacheKey, List<EzResultSetHandler.PendingRelation>> pendingRelations = new HashMap<>();
    // Cached Automappings
    private final Map<String, List<EzResultSetHandler.UnMappedColumnAutoMapping>> autoMappingsCache = new HashMap<>();
    private Object previousRowValue;
    // temporary marking flag that indicate using constructor mapping (use field to reduce memory usage)
    private boolean useConstructorMappings;

    public EzResultSetHandler(Executor executor, MappedStatement mappedStatement,
                              ParameterHandler parameterHandler, ResultHandler<?> resultHandler,
                              BoundSql boundSql,
                              RowBounds rowBounds) {
        super(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
        this.executor = executor;
        this.configuration = mappedStatement.getConfiguration();
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;
        this.parameterHandler = parameterHandler;
        this.boundSql = boundSql;
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
        this.objectFactory = this.configuration.getObjectFactory();
        this.reflectorFactory = this.configuration.getReflectorFactory();
        this.resultHandler = resultHandler;
    }

    @Override
    public void handleOutputParameters(CallableStatement cs) throws SQLException {
        final Object parameterObject = this.parameterHandler.getParameterObject();
        final MetaObject metaParam = this.configuration.newMetaObject(parameterObject);
        final List<ParameterMapping> parameterMappings = this.boundSql.getParameterMappings();
        for (int i = 0; i < parameterMappings.size(); i++) {
            final ParameterMapping parameterMapping = parameterMappings.get(i);
            if (parameterMapping.getMode() == ParameterMode.OUT || parameterMapping.getMode() == ParameterMode.INOUT) {
                if (ResultSet.class.equals(parameterMapping.getJavaType())) {
                    this.handleRefCursorOutputParameter((ResultSet) cs.getObject(i + 1), parameterMapping, metaParam);
                } else {
                    final TypeHandler<?> typeHandler = parameterMapping.getTypeHandler();
                    metaParam.setValue(parameterMapping.getProperty(), typeHandler.getResult(cs, i + 1));
                }
            }
        }
    }

    private void handleRefCursorOutputParameter(ResultSet rs, ParameterMapping parameterMapping, MetaObject metaParam) throws SQLException {
        if (rs == null) {
            return;
        }
        try {
            final String resultMapId = parameterMapping.getResultMapId();
            final ResultMap resultMap = this.configuration.getResultMap(resultMapId);
            final ResultSetWrapper rsw = new ResultSetWrapper(rs, this.configuration);
            if (this.resultHandler == null) {
                final DefaultResultHandler resultHandler = new DefaultResultHandler(this.objectFactory);
                this.handleRowValues(rsw, resultMap, resultHandler, new RowBounds(), null);
                metaParam.setValue(parameterMapping.getProperty(), resultHandler.getResultList());
            } else {
                this.handleRowValues(rsw, resultMap, this.resultHandler, new RowBounds(), null);
            }
        } finally {
            // issue #228 (close resultsets)
            this.closeResultSet(rs);
        }
    }

    //
    // HANDLE OUTPUT PARAMETER
    //

    //
    // HANDLE RESULT SETS
    //
    @Override
    public List<Object> handleResultSets(Statement stmt) throws SQLException {
        ErrorContext.instance().activity("handling results").object(this.mappedStatement.getId());

        final List<Object> multipleResults = new ArrayList<>();

        int resultSetCount = 0;
        ResultSetWrapper rsw = this.getFirstResultSet(stmt);

        List<ResultMap> resultMaps = this.mappedStatement.getResultMaps();
        int resultMapCount = resultMaps.size();
        this.validateResultMapsCount(rsw, resultMapCount);
        while (rsw != null && resultMapCount > resultSetCount) {
            ResultMap resultMap = resultMaps.get(resultSetCount);
            this.handleResultSet(rsw, resultMap, multipleResults, null);
            rsw = this.getNextResultSet(stmt);
            this.cleanUpAfterHandlingResultSet();
            resultSetCount++;
        }

        String[] resultSets = this.mappedStatement.getResultSets();
        if (resultSets != null) {
            while (rsw != null && resultSetCount < resultSets.length) {
                ResultMapping parentMapping = this.nextResultMaps.get(resultSets[resultSetCount]);
                if (parentMapping != null) {
                    String nestedResultMapId = parentMapping.getNestedResultMapId();
                    ResultMap resultMap = this.configuration.getResultMap(nestedResultMapId);
                    this.handleResultSet(rsw, resultMap, null, parentMapping);
                }
                rsw = this.getNextResultSet(stmt);
                this.cleanUpAfterHandlingResultSet();
                resultSetCount++;
            }
        }
        List<Object> ret = this.collapseSingleResultList(multipleResults);
        ResultMapInitLogic.cleanRetType();
        return ret;
    }

    @Override
    public <E> Cursor<E> handleCursorResultSets(Statement stmt) throws SQLException {
        ErrorContext.instance().activity("handling cursor results").object(this.mappedStatement.getId());

        ResultSetWrapper rsw = this.getFirstResultSet(stmt);

        List<ResultMap> resultMaps = this.mappedStatement.getResultMaps();

        int resultMapCount = resultMaps.size();
        this.validateResultMapsCount(rsw, resultMapCount);
        if (resultMapCount != 1) {
            throw new ExecutorException("Cursor results cannot be mapped to multiple resultMaps");
        }

        ResultMap resultMap = resultMaps.get(0);
        return new DefaultCursor<>(this, resultMap, rsw, this.rowBounds);
    }

    private ResultSetWrapper getFirstResultSet(Statement stmt) throws SQLException {
        ResultSet rs = stmt.getResultSet();
        while (rs == null) {
            // move forward to get the first resultset in case the driver
            // doesn't return the resultset as the first result (HSQLDB 2.1)
            if (stmt.getMoreResults()) {
                rs = stmt.getResultSet();
            } else {
                if (stmt.getUpdateCount() == -1) {
                    // no more results. Must be no resultset
                    break;
                }
            }
        }
        return rs != null ? new ResultSetWrapper(rs, this.configuration) : null;
    }

    private ResultSetWrapper getNextResultSet(Statement stmt) {
        // Making this method tolerant of bad JDBC drivers
        try {
            if (stmt.getConnection().getMetaData().supportsMultipleResultSets()) {
                // Crazy Standard JDBC way of determining if there are more results
                if (!(!stmt.getMoreResults() && stmt.getUpdateCount() == -1)) {
                    ResultSet rs = stmt.getResultSet();
                    if (rs == null) {
                        return this.getNextResultSet(stmt);
                    } else {
                        return new ResultSetWrapper(rs, this.configuration);
                    }
                }
            }
        } catch (Exception e) {
            // Intentionally ignored.
        }
        return null;
    }

    private void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            // ignore
        }
    }

    private void cleanUpAfterHandlingResultSet() {
        this.nestedResultObjects.clear();
    }

    private void validateResultMapsCount(ResultSetWrapper rsw, int resultMapCount) {
        if (rsw != null && resultMapCount < 1) {
            throw new ExecutorException("A query was run and no Result Maps were found for the Mapped Statement '" + this.mappedStatement.getId()
                    + "'.  It's likely that neither a Result Type nor a Result Map was specified.");
        }
    }

    private void handleResultSet(ResultSetWrapper rsw, ResultMap resultMap, List<Object> multipleResults, ResultMapping parentMapping) throws SQLException {
        try {
            if (parentMapping != null) {
                this.handleRowValues(rsw, resultMap, null, RowBounds.DEFAULT, parentMapping);
            } else {
                if (this.resultHandler == null) {
                    DefaultResultHandler defaultResultHandler = new DefaultResultHandler(this.objectFactory);
                    this.handleRowValues(rsw, resultMap, defaultResultHandler, this.rowBounds, null);
                    multipleResults.add(defaultResultHandler.getResultList());
                } else {
                    this.handleRowValues(rsw, resultMap, this.resultHandler, this.rowBounds, null);
                }
            }
        } finally {
            // issue #228 (close resultsets)
            this.closeResultSet(rsw.getResultSet());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Object> collapseSingleResultList(List<Object> multipleResults) {
        return multipleResults.size() == 1 ? (List<Object>) multipleResults.get(0) : multipleResults;
    }

    @Override
    public void handleRowValues(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping) throws SQLException {
        if (resultMap.hasNestedResultMaps()) {
            this.ensureNoRowBounds();
            this.checkResultHandler();
            this.handleRowValuesForNestedResultMap(rsw, resultMap, resultHandler, rowBounds, parentMapping);
        } else {
            this.handleRowValuesForSimpleResultMap(rsw, resultMap, resultHandler, rowBounds, parentMapping);
        }
    }

    private void ensureNoRowBounds() {
        if (this.configuration.isSafeRowBoundsEnabled() && this.rowBounds != null && (this.rowBounds.getLimit() < RowBounds.NO_ROW_LIMIT || this.rowBounds.getOffset() > RowBounds.NO_ROW_OFFSET)) {
            throw new ExecutorException("Mapped Statements with nested result mappings cannot be safely constrained by RowBounds. "
                    + "Use safeRowBoundsEnabled=false setting to bypass this check.");
        }
    }

    //
    // HANDLE ROWS FOR SIMPLE RESULTMAP
    //

    @Override
    protected void checkResultHandler() {
        if (this.resultHandler != null && this.configuration.isSafeResultHandlerEnabled() && !this.mappedStatement.isResultOrdered()) {
            throw new ExecutorException("Mapped Statements with nested result mappings cannot be safely used with a custom ResultHandler. "
                    + "Use safeResultHandlerEnabled=false setting to bypass this check "
                    + "or ensure your statement returns ordered data and set resultOrdered=true on it.");
        }
    }

    private void handleRowValuesForSimpleResultMap(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping)
            throws SQLException {
        DefaultResultContext<Object> resultContext = new DefaultResultContext<>();
        ResultSet resultSet = rsw.getResultSet();
        this.skipRows(resultSet, rowBounds);
        while (this.shouldProcessMoreRows(resultContext, rowBounds) && !resultSet.isClosed() && resultSet.next()) {
            ResultMap discriminatedResultMap = this.resolveDiscriminatedResultMap(resultSet, resultMap, null);
            Object rowValue = this.getRowValue(rsw, discriminatedResultMap, null);
            this.storeObject(resultHandler, resultContext, rowValue, parentMapping, resultSet);
        }
    }

    private void storeObject(ResultHandler<?> resultHandler, DefaultResultContext<Object> resultContext, Object rowValue, ResultMapping parentMapping, ResultSet rs) throws SQLException {
        if (parentMapping != null) {
            this.linkToParents(rs, parentMapping, rowValue);
        } else {
            this.callResultHandler(resultHandler, resultContext, rowValue);
        }
    }

    @SuppressWarnings("unchecked" /* because ResultHandler<?> is always ResultHandler<Object>*/)
    private void callResultHandler(ResultHandler<?> resultHandler, DefaultResultContext<Object> resultContext, Object rowValue) {
        resultContext.nextResultObject(rowValue);
        ((ResultHandler<Object>) resultHandler).handleResult(resultContext);
    }

    private boolean shouldProcessMoreRows(ResultContext<?> context, RowBounds rowBounds) {
        return !context.isStopped() && context.getResultCount() < rowBounds.getLimit();
    }

    private void skipRows(ResultSet rs, RowBounds rowBounds) throws SQLException {
        if (rs.getType() != ResultSet.TYPE_FORWARD_ONLY) {
            if (rowBounds.getOffset() != RowBounds.NO_ROW_OFFSET) {
                rs.absolute(rowBounds.getOffset());
            }
        } else {
            for (int i = 0; i < rowBounds.getOffset(); i++) {
                if (!rs.next()) {
                    break;
                }
            }
        }
    }

    private Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap, String columnPrefix) throws SQLException {
        final ResultLoaderMap lazyLoader = new ResultLoaderMap();
        Object rowValue = this.createResultObject(rsw, resultMap, lazyLoader, columnPrefix);
        Class<?> retType = ResultMapInitLogic.getRetType(resultMap);
        if (rowValue != null && !this.hasTypeHandlerForResultObject(rsw, retType)) {
            final MetaObject metaObject = this.configuration.newMetaObject(rowValue);
            boolean foundValues = this.useConstructorMappings;
            if (this.shouldApplyAutomaticMappings(resultMap, false)) {
                foundValues = this.applyAutomaticMappings(rsw, resultMap, metaObject, columnPrefix) || foundValues;
            }
            foundValues = this.applyPropertyMappings(rsw, resultMap, metaObject, lazyLoader, columnPrefix) || foundValues;
            foundValues = lazyLoader.size() > 0 || foundValues;
            rowValue = foundValues || this.configuration.isReturnInstanceForEmptyRow() ? rowValue : null;
        }
        return rowValue;
    }

    private Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap, CacheKey combinedKey, String columnPrefix, Object partialObject) throws SQLException {
        final String resultMapId = resultMap.getId();
        Object rowValue = partialObject;
        if (rowValue != null) {
            final MetaObject metaObject = this.configuration.newMetaObject(rowValue);
            this.putAncestor(rowValue, resultMapId);
            this.applyNestedResultMappings(rsw, resultMap, metaObject, columnPrefix, combinedKey, false);
            this.ancestorObjects.remove(resultMapId);
        } else {
            final ResultLoaderMap lazyLoader = new ResultLoaderMap();
            rowValue = this.createResultObject(rsw, resultMap, lazyLoader, columnPrefix);
            if (rowValue != null && !this.hasTypeHandlerForResultObject(rsw, ResultMapInitLogic.getRetType(resultMap))) {
                final MetaObject metaObject = this.configuration.newMetaObject(rowValue);
                boolean foundValues = this.useConstructorMappings;
                if (this.shouldApplyAutomaticMappings(resultMap, true)) {
                    foundValues = this.applyAutomaticMappings(rsw, resultMap, metaObject, columnPrefix) || foundValues;
                }
                foundValues = this.applyPropertyMappings(rsw, resultMap, metaObject, lazyLoader, columnPrefix) || foundValues;
                this.putAncestor(rowValue, resultMapId);
                foundValues = this.applyNestedResultMappings(rsw, resultMap, metaObject, columnPrefix, combinedKey, true) || foundValues;
                this.ancestorObjects.remove(resultMapId);
                foundValues = lazyLoader.size() > 0 || foundValues;
                rowValue = foundValues || this.configuration.isReturnInstanceForEmptyRow() ? rowValue : null;
            }
            if (combinedKey != CacheKey.NULL_CACHE_KEY) {
                this.nestedResultObjects.put(combinedKey, rowValue);
            }
        }
        return rowValue;
    }

    //
    // GET VALUE FROM ROW FOR SIMPLE RESULT MAP
    //

    private void putAncestor(Object resultObject, String resultMapId) {
        this.ancestorObjects.put(resultMapId, resultObject);
    }

    //
    // GET VALUE FROM ROW FOR NESTED RESULT MAP
    //

    private boolean shouldApplyAutomaticMappings(ResultMap resultMap, boolean isNested) {
        if (resultMap.getAutoMapping() != null) {
            return resultMap.getAutoMapping();
        } else {
            if (isNested) {
                return AutoMappingBehavior.FULL == this.configuration.getAutoMappingBehavior();
            } else {
                return AutoMappingBehavior.NONE != this.configuration.getAutoMappingBehavior();
            }
        }
    }

    private boolean applyPropertyMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, ResultLoaderMap lazyLoader, String columnPrefix)
            throws SQLException {
        final List<String> mappedColumnNames = rsw.getMappedColumnNames(resultMap, columnPrefix);
        boolean foundValues = false;
        final List<ResultMapping> propertyMappings = resultMap.getPropertyResultMappings();
        for (ResultMapping propertyMapping : propertyMappings) {
            String column = this.prependPrefix(propertyMapping.getColumn(), columnPrefix);
            if (propertyMapping.getNestedResultMapId() != null) {
                // the user added a column attribute to a nested result map, ignore it
                column = null;
            }
            if (propertyMapping.isCompositeResult()
                    || (column != null && mappedColumnNames.contains(column.toUpperCase(Locale.ENGLISH)))
                    || propertyMapping.getResultSet() != null) {
                Object value = this.getPropertyMappingValue(rsw.getResultSet(), metaObject, propertyMapping, lazyLoader, columnPrefix);
                // issue #541 make property optional
                final String property = propertyMapping.getProperty();
                if (property == null) {
                    continue;
                } else if (value == DEFERRED) {
                    foundValues = true;
                    continue;
                }
                if (value != null) {
                    foundValues = true;
                }
                if (value != null || (this.configuration.isCallSettersOnNulls() && !metaObject.getSetterType(property).isPrimitive())) {
                    // gcode issue #377, call setter on nulls (value is not 'found')
                    metaObject.setValue(property, value);
                }
            }
        }
        return foundValues;
    }

    private Object getPropertyMappingValue(ResultSet rs, MetaObject metaResultObject, ResultMapping propertyMapping, ResultLoaderMap lazyLoader, String columnPrefix)
            throws SQLException {
        if (propertyMapping.getNestedQueryId() != null) {
            return this.getNestedQueryMappingValue(rs, metaResultObject, propertyMapping, lazyLoader, columnPrefix);
        } else if (propertyMapping.getResultSet() != null) {
            this.addPendingChildRelation(rs, metaResultObject, propertyMapping);   // TODO is that OK?
            return DEFERRED;
        } else {
            final TypeHandler<?> typeHandler = propertyMapping.getTypeHandler();
            final String column = this.prependPrefix(propertyMapping.getColumn(), columnPrefix);
            return typeHandler.getResult(rs, column);
        }
    }

    /**
     * 结果列转为结果对象属性
     */
    private String retColumnToField(String column, MetaObject metaObject) {
        String property;
        //如果是map, 则根据配置规则进行推算map属性
        if (metaObject.getOriginalObject() instanceof Map) {
            MapRetKeyPattern mapRetKeyPattern = EzMybatisContent.getContentConfig(this.configuration)
                    .getEzMybatisConfig().getMapRetKeyPattern();
            if (mapRetKeyPattern == null) {
                mapRetKeyPattern = MapRetKeyPattern.HUMP;
            }
            if (mapRetKeyPattern == MapRetKeyPattern.HUMP) {
                property = HumpLineStringUtils.lineToHump(column.toLowerCase());
            } else if (mapRetKeyPattern == MapRetKeyPattern.LOWER_CASE) {
                property = column.toLowerCase();
            } else if (mapRetKeyPattern == MapRetKeyPattern.UPPER_CASE) {
                property = column.toUpperCase();
            } else if (mapRetKeyPattern == MapRetKeyPattern.ORIGINAL) {
                property = column;
            } else {
                property = column;
            }
        }
        //如果是实体,则根据实体信息来获取实体属性名称
        else {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(this.configuration,
                    metaObject.getOriginalObject().getClass());
            //一般情况下, 默认数据库使用下划线风格进行命名列
            property = entityClassInfo.getFieldNameByColumn(column);
            //如果根据下划线风格找不到对应属性, 则认为数据库使用了驼峰风格
            if (property == null && !column.contains("_")) {
                //将列转换为下划线风格尝试重新查找
                column = HumpLineStringUtils.humpToLine(column);
                property = entityClassInfo.getFieldNameByColumn(column);
                if (property == null) {
                    //在下划线风格的基础上转大写重试一次
                    property = entityClassInfo.getFieldNameByColumn(column.toUpperCase());
                }
            }
        }
        return property;
    }

    /**
     * 次方法将返回列与实体属性的映射以及typeHandle
     * <div>重要, 在此处实现注解映射</div>
     */
    private List<EzResultSetHandler.UnMappedColumnAutoMapping> createAutomaticMappings(ResultSetWrapper rsw,
                                                                                       ResultMap resultMap,
                                                                                       MetaObject metaObject,
                                                                                       String columnPrefix)
            throws SQLException {
        //EzMapper的查询返回的结果类型是动态的, 所以此处的缓存key需要加上返回结果类型
        final String mapKey = resultMap.getId() + ":" + metaObject.getOriginalObject().getClass().getName()
                + ":" + columnPrefix;
        List<UnMappedColumnAutoMapping> autoMapping = this.autoMappingsCache.get(mapKey);
        if (autoMapping == null) {
            autoMapping = new ArrayList<>();
            final List<String> unmappedColumnNames = rsw.getUnmappedColumnNames(resultMap, columnPrefix);
            for (String columnName : unmappedColumnNames) {
                //去掉前缀的列名
                String skipPfCN = columnName;
                if (columnPrefix != null && !columnPrefix.isEmpty()) {
                    // When columnPrefix is specified,
                    // ignore columns without the prefix.
                    if (columnName.toUpperCase(Locale.ENGLISH).startsWith(columnPrefix)) {
                        //原有逻辑是直接返回与column相同的熟悉, 修改为注解读取
                        skipPfCN = columnName.substring(columnPrefix.length());
                    } else {
                        continue;
                    }
                }
                //原有的查找类字段名称逻辑
                //final String property = metaObject.findProperty(skipPfCN, this.configuration
                //.isMapUnderscoreToCamelCase());
                //改为调用自定义的查找逻辑
                String property = this.retColumnToField(skipPfCN, metaObject);
                if (property != null && metaObject.hasSetter(property)) {
                    if (resultMap.getMappedProperties().contains(property)) {
                        continue;
                    }
                    TypeHandler<?> setTypeHandler = null;
                    if (!(metaObject.getOriginalObject() instanceof Map)) {
                        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(this.configuration,
                                metaObject.getOriginalObject().getClass());
                        EntityFieldInfo entityFieldInfo = entityClassInfo.getColumnMapFieldInfo().get(property);
                        if (entityFieldInfo != null && entityFieldInfo.getTypeHandler() != null) {
                            setTypeHandler = entityFieldInfo.getTypeHandler();
                        }
                    }
                    final Class<?> propertyType = metaObject.getSetterType(property);
                    if (this.typeHandlerRegistry.hasTypeHandler(propertyType, rsw.getJdbcType(columnName))) {
                        TypeHandler<?> typeHandler = rsw.getTypeHandler(propertyType, columnName);
                        if (setTypeHandler != null) {
                            typeHandler = setTypeHandler;
                        }
                        autoMapping.add(new UnMappedColumnAutoMapping(columnName, property, typeHandler,
                                propertyType.isPrimitive()));
                    } else if (setTypeHandler != null) {
                        autoMapping.add(new UnMappedColumnAutoMapping(columnName, property, setTypeHandler,
                                propertyType.isPrimitive()));
                    } else {
                        this.configuration.getAutoMappingUnknownColumnBehavior()
                                .doAction(this.mappedStatement, columnName, property, propertyType);
                    }
                } else {
                    this.configuration.getAutoMappingUnknownColumnBehavior()
                            .doAction(this.mappedStatement, columnName, (property != null) ? property : skipPfCN,
                                    null);
                }
            }
            this.autoMappingsCache.put(mapKey, autoMapping);
        }
        return autoMapping;
    }

    private boolean applyAutomaticMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, String columnPrefix) throws SQLException {
        List<EzResultSetHandler.UnMappedColumnAutoMapping> autoMapping = this.createAutomaticMappings(rsw, resultMap, metaObject, columnPrefix);
        boolean foundValues = false;
        if (!autoMapping.isEmpty()) {
            for (EzResultSetHandler.UnMappedColumnAutoMapping mapping : autoMapping) {
                Object value = mapping.typeHandler.getResult(rsw.getResultSet(), mapping.column);
                if (value != null) {
                    foundValues = true;
                }
                if (value != null || (this.configuration.isCallSettersOnNulls() && !mapping.primitive)) {
                    // gcode issue #377, call setter on nulls (value is not 'found')
                    // set事件处理
                    Object originalObject = metaObject.getOriginalObject();
                    value = EzMybatisContent.onFieldSet(this.configuration, originalObject, mapping.property, value);
                    if (value != null) {
                        metaObject.setValue(mapping.property, value);
                    }
                }
            }
        }
        return foundValues;
    }

    private void linkToParents(ResultSet rs, ResultMapping parentMapping, Object rowValue) throws SQLException {
        CacheKey parentKey = this.createKeyForMultipleResults(rs, parentMapping, parentMapping.getColumn(), parentMapping.getForeignColumn());
        List<EzResultSetHandler.PendingRelation> parents = this.pendingRelations.get(parentKey);
        if (parents != null) {
            for (EzResultSetHandler.PendingRelation parent : parents) {
                if (parent != null && rowValue != null) {
                    this.linkObjects(parent.metaObject, parent.propertyMapping, rowValue);
                }
            }
        }
    }

    private void addPendingChildRelation(ResultSet rs, MetaObject metaResultObject, ResultMapping parentMapping) throws SQLException {
        CacheKey cacheKey = this.createKeyForMultipleResults(rs, parentMapping, parentMapping.getColumn(), parentMapping.getColumn());
        EzResultSetHandler.PendingRelation deferLoad = new EzResultSetHandler.PendingRelation();
        deferLoad.metaObject = metaResultObject;
        deferLoad.propertyMapping = parentMapping;
        List<EzResultSetHandler.PendingRelation> relations = MapUtil.computeIfAbsent(this.pendingRelations, cacheKey, k -> new ArrayList<>());
        // issue #255
        relations.add(deferLoad);
        ResultMapping previous = this.nextResultMaps.get(parentMapping.getResultSet());
        if (previous == null) {
            this.nextResultMaps.put(parentMapping.getResultSet(), parentMapping);
        } else {
            if (!previous.equals(parentMapping)) {
                throw new ExecutorException("Two different properties are mapped to the same resultSet");
            }
        }
    }

    // MULTIPLE RESULT SETS

    private CacheKey createKeyForMultipleResults(ResultSet rs, ResultMapping resultMapping, String names, String columns) throws SQLException {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(resultMapping);
        if (columns != null && names != null) {
            String[] columnsArray = columns.split(",");
            String[] namesArray = names.split(",");
            for (int i = 0; i < columnsArray.length; i++) {
                Object value = rs.getString(columnsArray[i]);
                if (value != null) {
                    cacheKey.update(namesArray[i]);
                    cacheKey.update(value);
                }
            }
        }
        return cacheKey;
    }

    private Object createResultObject(ResultSetWrapper rsw, ResultMap resultMap, ResultLoaderMap lazyLoader, String columnPrefix) throws SQLException {
        this.useConstructorMappings = false; // reset previous mapping result
        final List<Class<?>> constructorArgTypes = new ArrayList<>();
        final List<Object> constructorArgs = new ArrayList<>();
        Object resultObject = this.createResultObject(rsw, resultMap, constructorArgTypes, constructorArgs, columnPrefix);
        if (resultObject != null && !this.hasTypeHandlerForResultObject(rsw, ResultMapInitLogic.getRetType(resultMap))) {
            final List<ResultMapping> propertyMappings = resultMap.getPropertyResultMappings();
            for (ResultMapping propertyMapping : propertyMappings) {
                // issue gcode #109 && issue #149
                if (propertyMapping.getNestedQueryId() != null && propertyMapping.isLazy()) {
                    resultObject = this.configuration.getProxyFactory().createProxy(resultObject, lazyLoader, this.configuration, this.objectFactory, constructorArgTypes, constructorArgs);
                    break;
                }
            }
        }
        this.useConstructorMappings = resultObject != null && !constructorArgTypes.isEmpty(); // set current mapping result
        return resultObject;
    }

    private Object createResultObject(ResultSetWrapper rsw, ResultMap resultMap, List<Class<?>> constructorArgTypes, List<Object> constructorArgs, String columnPrefix)
            throws SQLException {
        Class<?> resultType = ResultMapInitLogic.getRetType(resultMap);
        final MetaClass metaType = MetaClass.forClass(resultType, this.reflectorFactory);
        final List<ResultMapping> constructorMappings = resultMap.getConstructorResultMappings();
        if (this.hasTypeHandlerForResultObject(rsw, resultType)) {
            return this.createPrimitiveResultObject(rsw, resultMap, columnPrefix);
        } else if (!constructorMappings.isEmpty()) {
            return this.createParameterizedResultObject(rsw, resultType, constructorMappings, constructorArgTypes, constructorArgs, columnPrefix);
        } else if (resultType.isInterface() || metaType.hasDefaultConstructor()) {
            return this.objectFactory.create(resultType);
        } else if (this.shouldApplyAutomaticMappings(resultMap, false)) {
            return this.createByConstructorSignature(rsw, resultType, constructorArgTypes, constructorArgs);
        }
        throw new ExecutorException("Do not know how to create an instance of " + resultType);
    }

    //
    // INSTANTIATION & CONSTRUCTOR MAPPING
    //

    Object createParameterizedResultObject(ResultSetWrapper rsw, Class<?> resultType, List<ResultMapping> constructorMappings,
                                           List<Class<?>> constructorArgTypes, List<Object> constructorArgs, String columnPrefix) {
        boolean foundValues = false;
        for (ResultMapping constructorMapping : constructorMappings) {
            final Class<?> parameterType = constructorMapping.getJavaType();
            final String column = constructorMapping.getColumn();
            final Object value;
            try {
                if (constructorMapping.getNestedQueryId() != null) {
                    value = this.getNestedQueryConstructorValue(rsw.getResultSet(), constructorMapping, columnPrefix);
                } else if (constructorMapping.getNestedResultMapId() != null) {
                    final ResultMap resultMap = this.configuration.getResultMap(constructorMapping.getNestedResultMapId());
                    value = this.getRowValue(rsw, resultMap, this.getColumnPrefix(columnPrefix, constructorMapping));
                } else {
                    final TypeHandler<?> typeHandler = constructorMapping.getTypeHandler();
                    value = typeHandler.getResult(rsw.getResultSet(), this.prependPrefix(column, columnPrefix));
                }
            } catch (ResultMapException | SQLException e) {
                throw new ExecutorException("Could not process result for mapping: " + constructorMapping, e);
            }
            constructorArgTypes.add(parameterType);
            constructorArgs.add(value);
            foundValues = value != null || foundValues;
        }
        return foundValues ? this.objectFactory.create(resultType, constructorArgTypes, constructorArgs) : null;
    }

    private Object createByConstructorSignature(ResultSetWrapper rsw, Class<?> resultType, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) throws SQLException {
        final Constructor<?>[] constructors = resultType.getDeclaredConstructors();
        final Constructor<?> defaultConstructor = this.findDefaultConstructor(constructors);
        if (defaultConstructor != null) {
            return this.createUsingConstructor(rsw, resultType, constructorArgTypes, constructorArgs, defaultConstructor);
        } else {
            for (Constructor<?> constructor : constructors) {
                if (this.allowedConstructorUsingTypeHandlers(constructor, rsw.getJdbcTypes())) {
                    return this.createUsingConstructor(rsw, resultType, constructorArgTypes, constructorArgs, constructor);
                }
            }
        }
        throw new ExecutorException("No constructor found in " + resultType.getName() + " matching " + rsw.getClassNames());
    }

    private Object createUsingConstructor(ResultSetWrapper rsw, Class<?> resultType, List<Class<?>> constructorArgTypes, List<Object> constructorArgs, Constructor<?> constructor) throws SQLException {
        boolean foundValues = false;
        for (int i = 0; i < constructor.getParameterTypes().length; i++) {
            Class<?> parameterType = constructor.getParameterTypes()[i];
            String columnName = rsw.getColumnNames().get(i);
            TypeHandler<?> typeHandler = rsw.getTypeHandler(parameterType, columnName);
            Object value = typeHandler.getResult(rsw.getResultSet(), columnName);
            constructorArgTypes.add(parameterType);
            constructorArgs.add(value);
            foundValues = value != null || foundValues;
        }
        return foundValues ? this.objectFactory.create(resultType, constructorArgTypes, constructorArgs) : null;
    }

    private Constructor<?> findDefaultConstructor(final Constructor<?>[] constructors) {
        if (constructors.length == 1) {
            return constructors[0];
        }

        for (final Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(AutomapConstructor.class)) {
                return constructor;
            }
        }
        return null;
    }

    private boolean allowedConstructorUsingTypeHandlers(final Constructor<?> constructor, final List<JdbcType> jdbcTypes) {
        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length != jdbcTypes.size()) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!this.typeHandlerRegistry.hasTypeHandler(parameterTypes[i], jdbcTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    private Object createPrimitiveResultObject(ResultSetWrapper rsw, ResultMap resultMap, String columnPrefix) throws SQLException {
        Class<?> resultType = ResultMapInitLogic.getRetType(resultMap);
        final String columnName;
        if (!resultMap.getResultMappings().isEmpty()) {
            final List<ResultMapping> resultMappingList = resultMap.getResultMappings();
            final ResultMapping mapping = resultMappingList.get(0);
            columnName = this.prependPrefix(mapping.getColumn(), columnPrefix);
        } else {
            columnName = rsw.getColumnNames().get(0);
        }
        final TypeHandler<?> typeHandler = rsw.getTypeHandler(resultType, columnName);
        return typeHandler.getResult(rsw.getResultSet(), columnName);
    }

    private Object getNestedQueryConstructorValue(ResultSet rs, ResultMapping constructorMapping, String columnPrefix) throws SQLException {
        final String nestedQueryId = constructorMapping.getNestedQueryId();
        final MappedStatement nestedQuery = this.configuration.getMappedStatement(nestedQueryId);
        final Class<?> nestedQueryParameterType = nestedQuery.getParameterMap().getType();
        final Object nestedQueryParameterObject = this.prepareParameterForNestedQuery(rs, constructorMapping, nestedQueryParameterType, columnPrefix);
        Object value = null;
        if (nestedQueryParameterObject != null) {
            final BoundSql nestedBoundSql = nestedQuery.getBoundSql(nestedQueryParameterObject);
            final CacheKey key = this.executor.createCacheKey(nestedQuery, nestedQueryParameterObject, RowBounds.DEFAULT, nestedBoundSql);
            final Class<?> targetType = constructorMapping.getJavaType();
            final ResultLoader resultLoader = new ResultLoader(this.configuration, this.executor, nestedQuery, nestedQueryParameterObject, targetType, key, nestedBoundSql);
            value = resultLoader.loadResult();
        }
        return value;
    }

    private Object getNestedQueryMappingValue(ResultSet rs, MetaObject metaResultObject, ResultMapping propertyMapping, ResultLoaderMap lazyLoader, String columnPrefix)
            throws SQLException {
        final String nestedQueryId = propertyMapping.getNestedQueryId();
        final String property = propertyMapping.getProperty();
        final MappedStatement nestedQuery = this.configuration.getMappedStatement(nestedQueryId);
        final Class<?> nestedQueryParameterType = nestedQuery.getParameterMap().getType();
        final Object nestedQueryParameterObject = this.prepareParameterForNestedQuery(rs, propertyMapping, nestedQueryParameterType, columnPrefix);
        Object value = null;
        if (nestedQueryParameterObject != null) {
            final BoundSql nestedBoundSql = nestedQuery.getBoundSql(nestedQueryParameterObject);
            final CacheKey key = this.executor.createCacheKey(nestedQuery, nestedQueryParameterObject, RowBounds.DEFAULT, nestedBoundSql);
            final Class<?> targetType = propertyMapping.getJavaType();
            if (this.executor.isCached(nestedQuery, key)) {
                this.executor.deferLoad(nestedQuery, metaResultObject, property, key, targetType);
                value = DEFERRED;
            } else {
                final ResultLoader resultLoader = new ResultLoader(this.configuration, this.executor, nestedQuery, nestedQueryParameterObject, targetType, key, nestedBoundSql);
                if (propertyMapping.isLazy()) {
                    lazyLoader.addLoader(property, metaResultObject, resultLoader);
                    value = DEFERRED;
                } else {
                    value = resultLoader.loadResult();
                }
            }
        }
        return value;
    }

    //
    // NESTED QUERY
    //

    private Object prepareParameterForNestedQuery(ResultSet rs, ResultMapping resultMapping, Class<?> parameterType, String columnPrefix) throws SQLException {
        if (resultMapping.isCompositeResult()) {
            return this.prepareCompositeKeyParameter(rs, resultMapping, parameterType, columnPrefix);
        } else {
            return this.prepareSimpleKeyParameter(rs, resultMapping, parameterType, columnPrefix);
        }
    }

    private Object prepareSimpleKeyParameter(ResultSet rs, ResultMapping resultMapping, Class<?> parameterType, String columnPrefix) throws SQLException {
        final TypeHandler<?> typeHandler;
        if (this.typeHandlerRegistry.hasTypeHandler(parameterType)) {
            typeHandler = this.typeHandlerRegistry.getTypeHandler(parameterType);
        } else {
            typeHandler = this.typeHandlerRegistry.getUnknownTypeHandler();
        }
        return typeHandler.getResult(rs, this.prependPrefix(resultMapping.getColumn(), columnPrefix));
    }

    private Object prepareCompositeKeyParameter(ResultSet rs, ResultMapping resultMapping, Class<?> parameterType, String columnPrefix) throws SQLException {
        final Object parameterObject = this.instantiateParameterObject(parameterType);
        final MetaObject metaObject = this.configuration.newMetaObject(parameterObject);
        boolean foundValues = false;
        for (ResultMapping innerResultMapping : resultMapping.getComposites()) {
            final Class<?> propType = metaObject.getSetterType(innerResultMapping.getProperty());
            final TypeHandler<?> typeHandler = this.typeHandlerRegistry.getTypeHandler(propType);
            final Object propValue = typeHandler.getResult(rs, this.prependPrefix(innerResultMapping.getColumn(), columnPrefix));
            // issue #353 & #560 do not execute nested query if key is null
            if (propValue != null) {
                metaObject.setValue(innerResultMapping.getProperty(), propValue);
                foundValues = true;
            }
        }
        return foundValues ? parameterObject : null;
    }

    private Object instantiateParameterObject(Class<?> parameterType) {
        if (parameterType == null) {
            return new HashMap<>();
        } else if (MapperMethod.ParamMap.class.equals(parameterType)) {
            return new HashMap<>(); // issue #649
        } else {
            return this.objectFactory.create(parameterType);
        }
    }

    @Override
    public ResultMap resolveDiscriminatedResultMap(ResultSet rs, ResultMap resultMap, String columnPrefix) throws SQLException {
        Set<String> pastDiscriminators = new HashSet<>();
        Discriminator discriminator = resultMap.getDiscriminator();
        while (discriminator != null) {
            final Object value = this.getDiscriminatorValue(rs, discriminator, columnPrefix);
            final String discriminatedMapId = discriminator.getMapIdFor(String.valueOf(value));
            if (this.configuration.hasResultMap(discriminatedMapId)) {
                resultMap = this.configuration.getResultMap(discriminatedMapId);
                Discriminator lastDiscriminator = discriminator;
                discriminator = resultMap.getDiscriminator();
                if (discriminator == lastDiscriminator || !pastDiscriminators.add(discriminatedMapId)) {
                    break;
                }
            } else {
                break;
            }
        }
        return resultMap;
    }

    private Object getDiscriminatorValue(ResultSet rs, Discriminator discriminator, String columnPrefix) throws SQLException {
        final ResultMapping resultMapping = discriminator.getResultMapping();
        final TypeHandler<?> typeHandler = resultMapping.getTypeHandler();
        return typeHandler.getResult(rs, this.prependPrefix(resultMapping.getColumn(), columnPrefix));
    }

    //
    // DISCRIMINATOR
    //

    private String prependPrefix(String columnName, String prefix) {
        if (columnName == null || columnName.length() == 0 || prefix == null || prefix.length() == 0) {
            return columnName;
        }
        return prefix + columnName;
    }

    private void handleRowValuesForNestedResultMap(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping) throws SQLException {
        final DefaultResultContext<Object> resultContext = new DefaultResultContext<>();
        ResultSet resultSet = rsw.getResultSet();
        this.skipRows(resultSet, rowBounds);
        Object rowValue = this.previousRowValue;
        while (this.shouldProcessMoreRows(resultContext, rowBounds) && !resultSet.isClosed() && resultSet.next()) {
            final ResultMap discriminatedResultMap = this.resolveDiscriminatedResultMap(resultSet, resultMap, null);
            final CacheKey rowKey = this.createRowKey(discriminatedResultMap, rsw, null);
            Object partialObject = this.nestedResultObjects.get(rowKey);
            // issue #577 && #542
            if (this.mappedStatement.isResultOrdered()) {
                if (partialObject == null && rowValue != null) {
                    this.nestedResultObjects.clear();
                    this.storeObject(resultHandler, resultContext, rowValue, parentMapping, resultSet);
                }
                rowValue = this.getRowValue(rsw, discriminatedResultMap, rowKey, null, partialObject);
            } else {
                rowValue = this.getRowValue(rsw, discriminatedResultMap, rowKey, null, partialObject);
                if (partialObject == null) {
                    this.storeObject(resultHandler, resultContext, rowValue, parentMapping, resultSet);
                }
            }
        }
        if (rowValue != null && this.mappedStatement.isResultOrdered() && this.shouldProcessMoreRows(resultContext, rowBounds)) {
            this.storeObject(resultHandler, resultContext, rowValue, parentMapping, resultSet);
            this.previousRowValue = null;
        } else if (rowValue != null) {
            this.previousRowValue = rowValue;
        }
    }

    private boolean applyNestedResultMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, String parentPrefix, CacheKey parentRowKey, boolean newObject) {
        boolean foundValues = false;
        for (ResultMapping resultMapping : resultMap.getPropertyResultMappings()) {
            final String nestedResultMapId = resultMapping.getNestedResultMapId();
            if (nestedResultMapId != null && resultMapping.getResultSet() == null) {
                try {
                    final String columnPrefix = this.getColumnPrefix(parentPrefix, resultMapping);
                    final ResultMap nestedResultMap = this.getNestedResultMap(rsw.getResultSet(), nestedResultMapId, columnPrefix);
                    if (resultMapping.getColumnPrefix() == null) {
                        // try to fill circular reference only when columnPrefix
                        // is not specified for the nested result map (issue #215)
                        Object ancestorObject = this.ancestorObjects.get(nestedResultMapId);
                        if (ancestorObject != null) {
                            if (newObject) {
                                this.linkObjects(metaObject, resultMapping, ancestorObject); // issue #385
                            }
                            continue;
                        }
                    }
                    final CacheKey rowKey = this.createRowKey(nestedResultMap, rsw, columnPrefix);
                    final CacheKey combinedKey = this.combineKeys(rowKey, parentRowKey);
                    Object rowValue = this.nestedResultObjects.get(combinedKey);
                    boolean knownValue = rowValue != null;
                    this.instantiateCollectionPropertyIfAppropriate(resultMapping, metaObject); // mandatory
                    if (this.anyNotNullColumnHasValue(resultMapping, columnPrefix, rsw)) {
                        rowValue = this.getRowValue(rsw, nestedResultMap, combinedKey, columnPrefix, rowValue);
                        if (rowValue != null && !knownValue) {
                            this.linkObjects(metaObject, resultMapping, rowValue);
                            foundValues = true;
                        }
                    }
                } catch (SQLException e) {
                    throw new ExecutorException("Error getting nested result map values for '" + resultMapping.getProperty() + "'.  Cause: " + e, e);
                }
            }
        }
        return foundValues;
    }

    //
    // HANDLE NESTED RESULT MAPS
    //

    private String getColumnPrefix(String parentPrefix, ResultMapping resultMapping) {
        final StringBuilder columnPrefixBuilder = new StringBuilder();
        if (parentPrefix != null) {
            columnPrefixBuilder.append(parentPrefix);
        }
        if (resultMapping.getColumnPrefix() != null) {
            columnPrefixBuilder.append(resultMapping.getColumnPrefix());
        }
        return columnPrefixBuilder.length() == 0 ? null : columnPrefixBuilder.toString().toUpperCase(Locale.ENGLISH);
    }

    //
    // NESTED RESULT MAP (JOIN MAPPING)
    //

    private boolean anyNotNullColumnHasValue(ResultMapping resultMapping, String columnPrefix, ResultSetWrapper rsw) throws SQLException {
        Set<String> notNullColumns = resultMapping.getNotNullColumns();
        if (notNullColumns != null && !notNullColumns.isEmpty()) {
            ResultSet rs = rsw.getResultSet();
            for (String column : notNullColumns) {
                rs.getObject(this.prependPrefix(column, columnPrefix));
                if (!rs.wasNull()) {
                    return true;
                }
            }
            return false;
        } else if (columnPrefix != null) {
            for (String columnName : rsw.getColumnNames()) {
                if (columnName.toUpperCase().startsWith(columnPrefix.toUpperCase())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private ResultMap getNestedResultMap(ResultSet rs, String nestedResultMapId, String columnPrefix) throws SQLException {
        ResultMap nestedResultMap = this.configuration.getResultMap(nestedResultMapId);
        return this.resolveDiscriminatedResultMap(rs, nestedResultMap, columnPrefix);
    }

    private CacheKey createRowKey(ResultMap resultMap, ResultSetWrapper rsw, String columnPrefix) throws SQLException {
        final CacheKey cacheKey = new CacheKey();
        cacheKey.update(resultMap.getId());
        List<ResultMapping> resultMappings = this.getResultMappingsForRowKey(resultMap);
        if (resultMappings.isEmpty()) {
            if (Map.class.isAssignableFrom(ResultMapInitLogic.getRetType(resultMap))) {
                this.createRowKeyForMap(rsw, cacheKey);
            } else {
                this.createRowKeyForUnmappedProperties(resultMap, rsw, cacheKey, columnPrefix);
            }
        } else {
            this.createRowKeyForMappedProperties(resultMap, rsw, cacheKey, resultMappings, columnPrefix);
        }
        if (cacheKey.getUpdateCount() < 2) {
            return CacheKey.NULL_CACHE_KEY;
        }
        return cacheKey;
    }

    private CacheKey combineKeys(CacheKey rowKey, CacheKey parentRowKey) {
        if (rowKey.getUpdateCount() > 1 && parentRowKey.getUpdateCount() > 1) {
            CacheKey combinedKey;
            try {
                combinedKey = rowKey.clone();
            } catch (CloneNotSupportedException e) {
                throw new ExecutorException("Error cloning cache key.  Cause: " + e, e);
            }
            combinedKey.update(parentRowKey);
            return combinedKey;
        }
        return CacheKey.NULL_CACHE_KEY;
    }

    //
    // UNIQUE RESULT KEY
    //

    private List<ResultMapping> getResultMappingsForRowKey(ResultMap resultMap) {
        List<ResultMapping> resultMappings = resultMap.getIdResultMappings();
        if (resultMappings.isEmpty()) {
            resultMappings = resultMap.getPropertyResultMappings();
        }
        return resultMappings;
    }

    private void createRowKeyForMappedProperties(ResultMap resultMap, ResultSetWrapper rsw, CacheKey cacheKey, List<ResultMapping> resultMappings, String columnPrefix) throws SQLException {
        for (ResultMapping resultMapping : resultMappings) {
            if (resultMapping.isSimple()) {
                final String column = this.prependPrefix(resultMapping.getColumn(), columnPrefix);
                final TypeHandler<?> th = resultMapping.getTypeHandler();
                List<String> mappedColumnNames = rsw.getMappedColumnNames(resultMap, columnPrefix);
                // Issue #114
                if (column != null && mappedColumnNames.contains(column.toUpperCase(Locale.ENGLISH))) {
                    final Object value = th.getResult(rsw.getResultSet(), column);
                    if (value != null || this.configuration.isReturnInstanceForEmptyRow()) {
                        cacheKey.update(column);
                        cacheKey.update(value);
                    }
                }
            }
        }
    }

    private void createRowKeyForUnmappedProperties(ResultMap resultMap, ResultSetWrapper rsw, CacheKey cacheKey,
                                                   String columnPrefix) throws SQLException {
        final MetaClass metaType = MetaClass.forClass(ResultMapInitLogic.getRetType(resultMap), this.reflectorFactory);
        List<String> unmappedColumnNames = rsw.getUnmappedColumnNames(resultMap, columnPrefix);
        for (String column : unmappedColumnNames) {
            String property = column;
            if (columnPrefix != null && !columnPrefix.isEmpty()) {
                // When columnPrefix is specified, ignore columns without the prefix.
                if (column.toUpperCase(Locale.ENGLISH).startsWith(columnPrefix)) {
                    property = column.substring(columnPrefix.length());
                } else {
                    continue;
                }
            }
            if (metaType.findProperty(property, this.configuration.isMapUnderscoreToCamelCase()) != null) {
                String value = rsw.getResultSet().getString(column);
                if (value != null) {
                    cacheKey.update(column);
                    cacheKey.update(value);
                }
            }
        }
    }

    private void createRowKeyForMap(ResultSetWrapper rsw, CacheKey cacheKey) throws SQLException {
        List<String> columnNames = rsw.getColumnNames();
        for (String columnName : columnNames) {
            final String value = rsw.getResultSet().getString(columnName);
            if (value != null) {
                cacheKey.update(columnName);
                cacheKey.update(value);
            }
        }
    }

    private void linkObjects(MetaObject metaObject, ResultMapping resultMapping, Object rowValue) {
        final Object collectionProperty = this.instantiateCollectionPropertyIfAppropriate(resultMapping, metaObject);
        if (collectionProperty != null) {
            final MetaObject targetMetaObject = this.configuration.newMetaObject(collectionProperty);
            targetMetaObject.add(rowValue);
        } else {
            metaObject.setValue(resultMapping.getProperty(), rowValue);
        }
    }

    private Object instantiateCollectionPropertyIfAppropriate(ResultMapping resultMapping, MetaObject metaObject) {
        final String propertyName = resultMapping.getProperty();
        Object propertyValue = metaObject.getValue(propertyName);
        if (propertyValue == null) {
            Class<?> type = resultMapping.getJavaType();
            if (type == null) {
                type = metaObject.getSetterType(propertyName);
            }
            try {
                if (this.objectFactory.isCollection(type)) {
                    propertyValue = this.objectFactory.create(type);
                    metaObject.setValue(propertyName, propertyValue);
                    return propertyValue;
                }
            } catch (Exception e) {
                throw new ExecutorException("Error instantiating collection property for result '" + resultMapping.getProperty() + "'.  Cause: " + e, e);
            }
        } else if (this.objectFactory.isCollection(propertyValue.getClass())) {
            return propertyValue;
        }
        return null;
    }

    private boolean hasTypeHandlerForResultObject(ResultSetWrapper rsw, Class<?> resultType) {
        if (rsw.getColumnNames().size() == 1) {
            return this.typeHandlerRegistry.hasTypeHandler(resultType, rsw.getJdbcType(rsw.getColumnNames().get(0)));
        }
        return this.typeHandlerRegistry.hasTypeHandler(resultType);
    }

    private static class PendingRelation {
        public MetaObject metaObject;
        public ResultMapping propertyMapping;
    }

    private static class UnMappedColumnAutoMapping {
        private final String column;
        private final String property;
        private final TypeHandler<?> typeHandler;
        private final boolean primitive;

        public UnMappedColumnAutoMapping(String column, String property, TypeHandler<?> typeHandler, boolean primitive) {
            this.column = column;
            this.property = property;
            this.typeHandler = typeHandler;
            this.primitive = primitive;
        }
    }
}
