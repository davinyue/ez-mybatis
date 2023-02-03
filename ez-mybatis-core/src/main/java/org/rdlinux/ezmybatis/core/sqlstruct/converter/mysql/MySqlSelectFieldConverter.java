package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectField;

public class MySqlSelectFieldConverter extends AbstractConverter<SelectField> implements Converter<SelectField> {
    private static volatile MySqlSelectFieldConverter instance;

    protected MySqlSelectFieldConverter() {
    }

    public static MySqlSelectFieldConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectFieldConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectFieldConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectField ojb, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ojb.getTable().getEtType());
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String columnName = keywordQM + entityClassInfo.getFieldInfo(ojb.getField()).getColumnName() + keywordQM;
        String sql = " " + ojb.getTable().getAlias() + "." + columnName + " ";
        String alias = ojb.getAlias();
        if (alias != null && !alias.isEmpty()) {
            sql = sql + keywordQM + alias + keywordQM + " ";
        }
        return sqlBuilder.append(sql);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
