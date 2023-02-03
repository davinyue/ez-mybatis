package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;

public class MySqlUpdateFieldItemConverter extends AbstractConverter<UpdateFieldItem> implements Converter<UpdateFieldItem> {
    private static volatile MySqlUpdateFieldItemConverter instance;

    protected MySqlUpdateFieldItemConverter() {
    }

    public static MySqlUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                        UpdateFieldItem obj, MybatisParamHolder mybatisParamHolder) {
        String paramName = mybatisParamHolder.getParamName(obj.getValue(), true);
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getEntityTable().getEtType());
        String column = etInfo.getFieldInfo(obj.getField()).getColumnName();
        sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(column)
                .append(keywordQM).append(" = ").append(paramName);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
