package org.rdlinux.ezmybatis.core.sqlstruct.condition;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlStruct;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

/**
 * 条件
 */
public interface Condition extends SqlStruct {
    static String valueToSqlStruct(String paramName, Configuration configuration, MybatisParamHolder mybatisParamHolder,
                                   Object value) {
        if (value instanceof EzQuery) {
            Converter<?> converter = EzMybatisContent.getConverter(configuration, EzQuery.class);
            return converter.buildSql(Converter.Type.SELECT, new StringBuilder(), configuration, value,
                    mybatisParamHolder).toString();
        } else {
            return mybatisParamHolder.getMybatisParamName(paramName, value, true);
        }
    }

    /**
     * 获取逻辑运算符号
     */
    LogicalOperator getLogicalOperator();
}
