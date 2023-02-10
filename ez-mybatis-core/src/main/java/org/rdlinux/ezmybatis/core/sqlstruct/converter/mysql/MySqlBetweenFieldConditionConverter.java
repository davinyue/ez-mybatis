package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.between.BetweenFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlBetweenFieldConditionConverter extends AbstractConverter<BetweenFieldCondition> implements Converter<BetweenFieldCondition> {
    private static volatile MySqlBetweenFieldConditionConverter instance;

    protected MySqlBetweenFieldConditionConverter() {
    }

    public static MySqlBetweenFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlBetweenFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlBetweenFieldConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       BetweenFieldCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getTable().getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(obj.getField());
        String column = fieldInfo.getColumnName();
        String sql = obj.getTable().getAlias() + "." + keywordQM + column + keywordQM;
        return MySqlBetweenAliasConditionConverter.doBuildSql(sqlBuilder, configuration, obj,
                mybatisParamHolder, sql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
