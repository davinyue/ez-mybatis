package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;

public class PostgreSqlUpdateFieldItemConverter extends AbstractConverter<UpdateFieldItem> implements Converter<UpdateFieldItem> {
    private static volatile PostgreSqlUpdateFieldItemConverter instance;

    protected PostgreSqlUpdateFieldItemConverter() {
    }

    public static PostgreSqlUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       UpdateFieldItem obj, MybatisParamHolder mybatisParamHolder) {

        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getEntityTable().getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(obj.getField());
        String column = fieldInfo.getColumnName();
        String paramName = mybatisParamHolder.getMybatisParamName(etInfo.getEntityClass(), fieldInfo.getField(),
                obj.getValue());
        sqlBuilder.append(keywordQM).append(column).append(keywordQM).append(" = ").append(paramName);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
