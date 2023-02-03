package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.CaseWhenUpdateFieldItem;

public class MySqlCaseWhenUpdateFieldItemConverter extends AbstractConverter<CaseWhenUpdateFieldItem> implements Converter<CaseWhenUpdateFieldItem> {
    private static volatile MySqlCaseWhenUpdateFieldItemConverter instance;

    protected MySqlCaseWhenUpdateFieldItemConverter() {
    }

    public static MySqlCaseWhenUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlCaseWhenUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlCaseWhenUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                        CaseWhenUpdateFieldItem obj, MybatisParamHolder mybatisParamHolder) {
        Converter<CaseWhen> converter = EzMybatisContent.getConverter(configuration, CaseWhen.class);
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getEntityTable().getEtType());
        String column = etInfo.getFieldInfo(obj.getField()).getColumnName();
        sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(column)
                .append(keywordQM).append(" = ");
        sqlBuilder = converter.buildSql(Type.UPDATE, sqlBuilder, configuration, obj.getCaseWhen(), mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
