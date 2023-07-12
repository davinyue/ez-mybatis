package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.ArgType;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.GroupBy.GroupItem;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class MySqlGroupItemConverter extends AbstractConverter<GroupItem> implements Converter<GroupItem> {
    private static volatile MySqlGroupItemConverter instance;

    protected MySqlGroupItemConverter() {
    }

    public static MySqlGroupItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlGroupItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlGroupItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, GroupItem ojb,
                                       MybatisParamHolder mybatisParamHolder) {
        if (ojb.getArgType() == ArgType.COLUMN) {
            String keywordQM = EzMybatisContent.getKeywordQM(configuration);
            sqlBuilder.append(ojb.getTable().getAlias()).append(".").append(keywordQM)
                    .append(ojb.getValue()).append(keywordQM);
        } else if (ojb.getArgType() == ArgType.FILED) {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                    ((EntityTable) ojb.getTable()).getEtType());
            String columnName = entityClassInfo.getFieldInfo((String) ojb.getValue()).getColumnName();
            String keywordQM = EzMybatisContent.getKeywordQM(configuration);
            sqlBuilder.append(ojb.getTable().getAlias()).append(".").append(keywordQM)
                    .append(columnName).append(keywordQM);
        } else if (ojb.getArgType() == ArgType.FUNC) {
            Converter<? extends Function> converter = EzMybatisContent.getConverter(configuration,
                    ((Function) ojb.getValue()).getClass());
            converter.buildSql(type, sqlBuilder, configuration, ojb.getValue(), mybatisParamHolder);
        } else if (ojb.getArgType() == ArgType.FORMULA) {
            Converter<? extends Formula> converter = EzMybatisContent.getConverter(configuration,
                    ((Formula) ojb.getValue()).getClass());
            converter.buildSql(type, sqlBuilder, configuration, ojb.getValue(), mybatisParamHolder);
        } else if (ojb.getArgType() == ArgType.CASE_WHEN) {
            Converter<? extends CaseWhen> converter = EzMybatisContent.getConverter(configuration,
                    ((CaseWhen) ojb.getValue()).getClass());
            converter.buildSql(type, sqlBuilder, configuration, ojb.getValue(), mybatisParamHolder);
        } else if (ojb.getArgType() == ArgType.VALUE) {
            sqlBuilder.append(mybatisParamHolder.getMybatisParamName(ojb.getValue()));
        } else if (ojb.getArgType() == ArgType.ALIAS) {
            String keywordQM = EzMybatisContent.getKeywordQM(configuration);
            sqlBuilder.append(keywordQM).append(ojb.getValue()).append(keywordQM);
        } else if (ojb.getArgType() == ArgType.KEYWORDS) {
            sqlBuilder.append(ojb.getValue());
        }
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
