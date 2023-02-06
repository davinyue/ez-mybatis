package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FieldCompareColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlFieldCompareColumnConditionConverter extends AbstractConverter<FieldCompareColumnCondition> implements Converter<FieldCompareColumnCondition> {
    private static volatile MySqlFieldCompareColumnConditionConverter instance;

    protected MySqlFieldCompareColumnConditionConverter() {
    }

    public static MySqlFieldCompareColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFieldCompareColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlFieldCompareColumnConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FieldCompareColumnCondition obj, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getLeftTable()
                .getEtType());
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " " + obj.getLeftTable().getAlias() + "." +
                keywordQM +
                etInfo.getFieldInfo(obj.getLeftField()).getColumnName() +
                keywordQM +
                " " +
                obj.getOperator().getOperator() +
                " " +
                obj.getRightTable().getAlias() + "." +
                keywordQM +
                obj.getRightColumn() +
                keywordQM +
                " ";
        return sqlBuilder.append(sql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
