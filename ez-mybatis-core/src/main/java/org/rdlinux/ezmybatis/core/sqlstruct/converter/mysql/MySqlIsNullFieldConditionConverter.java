package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.nil.IsNullFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

public class MySqlIsNullFieldConditionConverter extends AbstractConverter<IsNullFieldCondition> implements Converter<IsNullFieldCondition> {
    private static volatile MySqlIsNullFieldConditionConverter instance;

    protected MySqlIsNullFieldConditionConverter() {
    }

    public static MySqlIsNullFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlIsNullFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlIsNullFieldConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       IsNullFieldCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getTable().getEtType());
        String column = etInfo.getFieldInfo(obj.getField()).getColumnName();
        String sql = obj.getTable().getAlias() + "." + keywordQM + column + keywordQM;
        return MySqlIsNullAliasConditionConverter.doBuildSql(sqlBuilder, obj, sql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
