package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

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

public class PostgreSqlCaseWhenUpdateFieldItemConverter extends AbstractConverter<CaseWhenUpdateFieldItem> implements Converter<CaseWhenUpdateFieldItem> {
    private static volatile PostgreSqlCaseWhenUpdateFieldItemConverter instance;

    protected PostgreSqlCaseWhenUpdateFieldItemConverter() {
    }

    public static PostgreSqlCaseWhenUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlCaseWhenUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlCaseWhenUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       CaseWhenUpdateFieldItem obj, MybatisParamHolder mybatisParamHolder) {
        Converter<CaseWhen> converter = EzMybatisContent.getConverter(configuration, CaseWhen.class);
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getEntityTable().getEtType());
        String column = etInfo.getFieldInfo(obj.getField()).getColumnName();
        sqlBuilder.append(keywordQM).append(column).append(keywordQM).append(" = ");
        sqlBuilder = converter.buildSql(Type.UPDATE, sqlBuilder, configuration, obj.getCaseWhen(), mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
