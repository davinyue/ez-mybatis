package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.EntityInfoBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

/**
 * 数据库方言提供者SPI接口
 * <p>
 * 每种数据库方言实现此接口，通过 ServiceLoader 机制动态发现。
 * 新增数据库支持只需：实现此接口 + 在 META-INF/services/ 中注册，无需修改引擎源码。
 */
public interface DbDialectProvider {

    /**
     * 该方言对应的数据库类型
     */
    DbType getDbType();

    /**
     * 判断给定的驱动类名是否匹配此方言
     */
    boolean matchDriver(String driverClassName);

    /**
     * 获取SQL生成器
     */
    SqlGenerate getSqlGenerate();

    /**
     * 注册该方言的Converter
     */
    void registerConverters();

    /**
     * 获取数据库关键字引号字符（如MySQL用`, Oracle用"）
     */
    String getKeywordQuoteMark();

    /**
     * 注册转换器
     */
    <T extends SqlStruct> void addConverter(Class<T> sqlStruct, Converter<T> converter);

    /**
     * 获取转换器
     */
    <T extends SqlStruct> Converter<T> getConverter(Class<T> sqlStruct);

    /**
     * 获取实体信息构造器
     */
    EntityInfoBuilder getEntityInfoBuilder();
}
