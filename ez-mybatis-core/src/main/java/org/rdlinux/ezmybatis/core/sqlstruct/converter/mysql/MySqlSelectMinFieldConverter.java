package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectMinField;

public class MySqlSelectMinFieldConverter extends AbstractConverter<SelectMinField> implements Converter<SelectMinField> {
    private static volatile MySqlSelectMinFieldConverter instance;

    protected MySqlSelectMinFieldConverter() {
    }

    public static MySqlSelectMinFieldConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectMinFieldConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectMinFieldConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder dobuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectMinField ojb, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ojb.getTable().getEtType());
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " MIN(" + ojb.getTable().getAlias() + "." + keywordQM + entityClassInfo
                .getFieldInfo(ojb.getField()).getColumnName() + keywordQM + ") ";
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
