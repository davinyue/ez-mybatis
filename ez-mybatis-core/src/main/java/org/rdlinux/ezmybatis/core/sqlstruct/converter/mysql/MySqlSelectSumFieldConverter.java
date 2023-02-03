package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectSumField;

public class MySqlSelectSumFieldConverter extends AbstractConverter<SelectSumField> implements Converter<SelectSumField> {
    private static volatile MySqlSelectSumFieldConverter instance;

    protected MySqlSelectSumFieldConverter() {
    }

    public static MySqlSelectSumFieldConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectSumFieldConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectSumFieldConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectSumField ojb, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, ojb.getTable().getEtType());
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String sql = " SUM(" + ojb.getTable().getAlias() + "." + keywordQM + entityClassInfo
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
