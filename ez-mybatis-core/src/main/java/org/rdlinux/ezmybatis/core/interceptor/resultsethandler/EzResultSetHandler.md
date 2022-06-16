```
getRowValue(ResultSetWrapper rsw, ResultMap resultMap, String columnPrefix)

getRowValue(ResultSetWrapper rsw, ResultMap resultMap, CacheKey combinedKey, String columnPrefix, Object partialObject)

createResultObject(ResultSetWrapper rsw, ResultMap resultMap, ResultLoaderMap lazyLoader, String columnPrefix)

createResultObject(ResultSetWrapper rsw, ResultMap resultMap, List<Class<?>> constructorArgTypes, 
                                                   List<Object> constructorArgs, String columnPrefix)

createPrimitiveResultObject(ResultSetWrapper rsw, ResultMap resultMap, String columnPrefix)

createRowKey(ResultMap resultMap, ResultSetWrapper rsw, String columnPrefix)

createRowKeyForUnmappedProperties(ResultMap resultMap, ResultSetWrapper rsw, CacheKey cacheKey,
                                                   String columnPrefix)
```

以上方法从ResultMapInitLogic获取结果类型


handleResultSets方法的最后需要清空ResultMapInitLogic设置的threadlocal结果变量


createAutomaticMappings方法负责解析类与数据库列的映射
