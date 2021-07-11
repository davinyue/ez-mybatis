package org.rdlinux.ezmybatis.core;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.loader.ResultLoaderMap;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.result.DefaultResultContext;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetWrapper;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.rdlinux.ezmybatis.core.content.EzResultClassInfoFactory;
import org.rdlinux.ezmybatis.core.utils.ReflectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EzResultSetHandler extends DefaultResultSetHandler {
    private final Configuration configuration;
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final MappedStatement mappedStatement;
    private final Map<String, List<UnMappedColumnAutoMapping>> autoMappingsCache = new HashMap<>();

    public EzResultSetHandler(Executor executor, MappedStatement mappedStatement, ParameterHandler parameterHandler,
                              ResultHandler<?> resultHandler, BoundSql boundSql, RowBounds rowBounds) {
        super(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
        this.mappedStatement = mappedStatement;
    }


    @Override
    public void handleRowValues(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler,
                                RowBounds rowBounds, ResultMapping parentMapping) throws SQLException {
        if (resultMap.hasNestedResultMaps()) {
            super.handleRowValues(rsw, resultMap, resultHandler, rowBounds, parentMapping);
        } else {
            this.handleRowValuesForSimpleResultMap(rsw, resultMap, resultHandler, rowBounds, parentMapping);
        }
    }

    private void _skipRows(ResultSet rs, RowBounds rowBounds) throws SQLException {
        ReflectionUtils.invokeMethod(this, "skipRows",
                new Class[]{ResultSet.class, RowBounds.class},
                new Object[]{rs, rowBounds});
    }

    private boolean _shouldProcessMoreRows(ResultContext<?> context, RowBounds rowBounds) {
        return ReflectionUtils.invokeMethod(this, "shouldProcessMoreRows",
                new Class[]{ResultContext.class, RowBounds.class}, new Object[]{context, rowBounds});
    }

    private void _storeObject(ResultHandler<?> resultHandler, DefaultResultContext<Object> resultContext,
                              Object rowValue, ResultMapping parentMapping, ResultSet rs) throws SQLException {
        ReflectionUtils.invokeMethod(this, "storeObject",
                new Class[]{ResultHandler.class, DefaultResultContext.class, Object.class, ResultMapping.class,
                        ResultSet.class},
                new Object[]{resultHandler, resultContext, rowValue, parentMapping, rs});
    }

    private Object _createResultObject(ResultSetWrapper rsw, ResultMap resultMap, ResultLoaderMap lazyLoader,
                                       String columnPrefix) throws SQLException {
        return ReflectionUtils.invokeMethod(this, "createResultObject",
                new Class[]{ResultSetWrapper.class, ResultMap.class, ResultLoaderMap.class, String.class},
                new Object[]{rsw, resultMap, lazyLoader, columnPrefix});
    }

    private boolean _hasTypeHandlerForResultObject(ResultSetWrapper rsw, Class<?> resultType) {
        return ReflectionUtils.invokeMethod(this, "hasTypeHandlerForResultObject",
                new Class[]{ResultSetWrapper.class, Class.class},
                new Object[]{rsw, resultType});
    }

    private boolean _shouldApplyAutomaticMappings(ResultMap resultMap, boolean isNested) {
        return ReflectionUtils.invokeMethod(this, "shouldApplyAutomaticMappings",
                new Class[]{ResultMap.class, boolean.class},
                new Object[]{resultMap, isNested});
    }

    private boolean _applyPropertyMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject,
                                           ResultLoaderMap lazyLoader, String columnPrefix)
            throws SQLException {
        return ReflectionUtils.invokeMethod(this, "applyPropertyMappings",
                new Class[]{ResultSetWrapper.class, ResultMap.class, MetaObject.class, ResultLoaderMap.class,
                        String.class},
                new Object[]{rsw, resultMap, metaObject, lazyLoader, columnPrefix});
    }

    private void handleRowValuesForSimpleResultMap(ResultSetWrapper rsw, ResultMap resultMap,
                                                   ResultHandler<?> resultHandler, RowBounds rowBounds,
                                                   ResultMapping parentMapping)
            throws SQLException {
        DefaultResultContext<Object> resultContext = new DefaultResultContext<>();
        ResultSet resultSet = rsw.getResultSet();
        this._skipRows(resultSet, rowBounds);
        while (this._shouldProcessMoreRows(resultContext, rowBounds) && !resultSet.isClosed() && resultSet.next()) {
            ResultMap discriminatedResultMap = this.resolveDiscriminatedResultMap(resultSet, resultMap,
                    null);
            Object rowValue = this.getRowValue(rsw, discriminatedResultMap, null);
            this._storeObject(resultHandler, resultContext, rowValue, parentMapping, resultSet);
        }
    }


    private Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap, String columnPrefix) throws SQLException {
        final ResultLoaderMap lazyLoader = new ResultLoaderMap();
        Object rowValue = this._createResultObject(rsw, resultMap, lazyLoader, columnPrefix);
        if (rowValue != null && !this._hasTypeHandlerForResultObject(rsw, resultMap.getType())) {
            final MetaObject metaObject = this.configuration.newMetaObject(rowValue);
            //boolean foundValues = this.useConstructorMappings;
            boolean foundValues = ReflectionUtils.getFieldValue(this, "useConstructorMappings");
            if (this._shouldApplyAutomaticMappings(resultMap, false)) {
                foundValues = this.applyAutomaticMappings(rsw, resultMap, metaObject, columnPrefix) || foundValues;
            }
            foundValues = this._applyPropertyMappings(rsw, resultMap, metaObject, lazyLoader, columnPrefix)
                    || foundValues;
            foundValues = lazyLoader.size() > 0 || foundValues;
            rowValue = foundValues || this.configuration.isReturnInstanceForEmptyRow() ? rowValue : null;
        }
        return rowValue;
    }

    private boolean applyAutomaticMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject,
                                           String columnPrefix) throws SQLException {
        List<UnMappedColumnAutoMapping> autoMapping = this.createAutomaticMappings(rsw, resultMap, metaObject,
                columnPrefix);
        boolean foundValues = false;
        if (!autoMapping.isEmpty()) {
            for (UnMappedColumnAutoMapping mapping : autoMapping) {
                final Object value = mapping.typeHandler.getResult(rsw.getResultSet(), mapping.column);
                if (value != null) {
                    foundValues = true;
                }
                if (value != null || (this.configuration.isCallSettersOnNulls() && !mapping.primitive)) {
                    // gcode issue #377, call setter on nulls (value is not 'found')
                    metaObject.setValue(mapping.property, value);
                }
            }
        }
        return foundValues;
    }

    /**
     * 次方法将返回列与实体属性的映射以及typeHandle
     * <div>重要, 在此处实现注解映射</div>
     */
    private List<UnMappedColumnAutoMapping> createAutomaticMappings(ResultSetWrapper rsw, ResultMap resultMap,
                                                                    MetaObject metaObject, String columnPrefix)
            throws SQLException {
        final String mapKey = resultMap.getId() + ":" + columnPrefix;
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
                String property = EzResultClassInfoFactory.forClass(this.configuration, metaObject.getOriginalObject()
                        .getClass()).getPropertyByColumn(skipPfCN);
                if (property != null && metaObject.hasSetter(property)) {
                    if (resultMap.getMappedProperties().contains(property)) {
                        continue;
                    }
                    final Class<?> propertyType = metaObject.getSetterType(property);
                    if (this.typeHandlerRegistry.hasTypeHandler(propertyType, rsw.getJdbcType(columnName))) {
                        final TypeHandler<?> typeHandler = rsw.getTypeHandler(propertyType, columnName);
                        autoMapping.add(new UnMappedColumnAutoMapping(columnName, property, typeHandler,
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

    private static class UnMappedColumnAutoMapping {
        private final String column;
        private final String property;
        private final TypeHandler<?> typeHandler;
        private final boolean primitive;

        public UnMappedColumnAutoMapping(String column, String property, TypeHandler<?> typeHandler,
                                         boolean primitive) {
            this.column = column;
            this.property = property;
            this.typeHandler = typeHandler;
            this.primitive = primitive;
        }
    }
}
