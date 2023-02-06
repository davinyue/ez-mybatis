package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.ColumnCompareCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlColumnCompareConditionConverter extends AbstractConverter<ColumnCompareCondition> implements Converter<ColumnCompareCondition> {
    private static volatile MySqlColumnCompareConditionConverter instance;

    protected MySqlColumnCompareConditionConverter() {
    }

    public static MySqlColumnCompareConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlColumnCompareConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlColumnCompareConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       ColumnCompareCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " " + obj.getLeftTable().getAlias() + "." +
                keywordQM + obj.getLeftColumn() + keywordQM +
                " " +
                obj.getOperator().getOperator() +
                " " +
                obj.getRightTable().getAlias() + "." +
                keywordQM + obj.getRightColumn() + keywordQM +
                " ";
        return sqlBuilder.append(sql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
