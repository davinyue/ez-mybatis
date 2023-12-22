package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalColumnCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

import java.lang.reflect.Field;

public class PostgreSqlNormalColumnConditionConverter extends AbstractConverter<NormalColumnCondition> implements Converter<NormalColumnCondition> {
    private static volatile PostgreSqlNormalColumnConditionConverter instance;

    protected PostgreSqlNormalColumnConditionConverter() {
    }

    public static PostgreSqlNormalColumnConditionConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlNormalColumnConditionConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlNormalColumnConditionConverter();
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
            EntityFieldInfo entityFieldInfo = classInfo.getColumnMapFieldInfo().get(obj.getColumn());
            if (entityFieldInfo != null) {
                field = entityFieldInfo.getField();
            }
            if (field == null) {
                etType = null;
            }
        }
        String column = obj.getTable().getAlias() + "." + keywordQM + obj.getColumn() + keywordQM;
        return PostgreSqlNormalFieldConditionConverter.getInstance().doBuildSql(etType, field, sqlBuilder, configuration,
                obj, mybatisParamHolder, column);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}