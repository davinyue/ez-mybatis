package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.compare.FieldCompareCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlFieldCompareConditionConverter extends AbstractConverter<FieldCompareCondition> implements Converter<FieldCompareCondition> {
    private static volatile MySqlFieldCompareConditionConverter instance;

    protected MySqlFieldCompareConditionConverter() {
    }

    public static MySqlFieldCompareConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFieldCompareConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlFieldCompareConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FieldCompareCondition obj, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getLeftTable()
                .getEtType());
        EntityClassInfo oEtInfo = EzEntityClassInfoFactory.forClass(configuration,
                obj.getRightTable().getEtType());
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
