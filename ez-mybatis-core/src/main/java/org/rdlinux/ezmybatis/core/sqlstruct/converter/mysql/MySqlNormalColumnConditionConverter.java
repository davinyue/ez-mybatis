package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

import java.lang.reflect.Field;

public class MySqlNormalColumnConditionConverter extends AbstractConverter<NormalColumnCondition> implements Converter<NormalColumnCondition> {
    private static volatile MySqlNormalColumnConditionConverter instance;

    protected MySqlNormalColumnConditionConverter() {
    }

    public static MySqlNormalColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlNormalColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new MySqlNormalColumnConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       NormalColumnCondition obj, MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        Class<?> etType = null;
        Field field = null;
        if (obj.getTable() instanceof EntityTable) {
            etType = ((EntityTable) obj.getTable()).getEtType();
            EntityClassInfo classInfo = EzEntityClassInfoFactory.forClass(configuration, etType);
            field = classInfo.getColumnMapFieldInfo().get(obj.getColumn()).getField();
            if (field == null) {
                etType = null;
            }
        }
        String column = obj.getTable().getAlias() + "." + keywordQM + obj.getColumn() + keywordQM;
        return MySqlNormalFieldConditionConverter.doBuildSql(etType, field, sqlBuilder, configuration, obj,
                mybatisParamHolder, column);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
