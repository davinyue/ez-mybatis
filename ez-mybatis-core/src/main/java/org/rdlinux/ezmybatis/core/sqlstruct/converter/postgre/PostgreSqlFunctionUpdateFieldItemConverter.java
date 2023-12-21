package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.update.FunctionUpdateFieldItem;

public class PostgreSqlFunctionUpdateFieldItemConverter extends AbstractConverter<FunctionUpdateFieldItem>
        implements Converter<FunctionUpdateFieldItem> {
    private static volatile PostgreSqlFunctionUpdateFieldItemConverter instance;

    protected PostgreSqlFunctionUpdateFieldItemConverter() {
    }

    public static PostgreSqlFunctionUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlFunctionUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlFunctionUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FunctionUpdateFieldItem obj,
                                       MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, ((EntityTable) obj.getTable())
                .getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(obj.getField());
        String column = fieldInfo.getColumnName();
        Function function = obj.getFunction();
        sqlBuilder.append(keywordQM).append(column).append(keywordQM).append(" = ");
        Converter<? extends Function> converter = EzMybatisContent.getConverter(configuration, function.getClass());
        converter.buildSql(type, sqlBuilder, configuration, function, mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
