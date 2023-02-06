package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.ColumnCompareFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlColumnCompareFieldConditionConverter extends AbstractConverter<ColumnCompareFieldCondition> implements Converter<ColumnCompareFieldCondition> {
    private static volatile MySqlColumnCompareFieldConditionConverter instance;

    protected MySqlColumnCompareFieldConditionConverter() {
    }

    public static MySqlColumnCompareFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlColumnCompareFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlColumnCompareFieldConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       ColumnCompareFieldCondition obj, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo oEtInfo = EzEntityClassInfoFactory.forClass(configuration,
                obj.getRightTable().getEtType());
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " " + obj.getLeftTable().getAlias() + "." +
                keywordQM +
                obj.getLeftColumn() +
                keywordQM +
                " " +
                obj.getOperator().getOperator() +
                " " +
                obj.getRightTable().getAlias() + "." +
                keywordQM +
                oEtInfo.getFieldInfo(obj.getRightField()).getColumnName() +
                keywordQM +
                " ";
        return sqlBuilder.append(sql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
