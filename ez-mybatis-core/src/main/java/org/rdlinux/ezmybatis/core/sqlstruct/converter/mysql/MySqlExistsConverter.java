package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ExistsCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

@SuppressWarnings("rawtypes")
public class MySqlExistsConverter extends AbstractConverter<ExistsCondition> implements Converter<ExistsCondition> {
    private static volatile MySqlExistsConverter instance;

    protected MySqlExistsConverter() {
    }

    public static MySqlExistsConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlExistsConverter.class) {
                if (instance == null) {
                    instance = new MySqlExistsConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, ExistsCondition obj, SqlGenerateContext sqlGenerateContext) {
        if (obj.getQuery() == null) {
            return;
        }
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        if (obj.isNot()) {
            sqlBuilder.append(" NOT ");
        }
        sqlBuilder.append(" EXISTS  ");
        Converter<EzQuery> ezQueryConverter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                EzQuery.class);
        ezQueryConverter.buildSql(type, obj.getQuery(), sqlGenerateContext);
    }

}
