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
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

import java.util.List;

public class MySqlFunctionConverter extends AbstractConverter<Function> implements Converter<Function> {
    private static volatile MySqlFunctionConverter instance;

    protected MySqlFunctionConverter() {
    }

    public static MySqlFunctionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFunctionConverter.class) {
                if (instance == null) {
                    instance = new MySqlFunctionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Function ojb,
                                       MybatisParamHolder mybatisParamHolder) {
        sqlBuilder.append(" ").append(ojb.getFunName()).append("(");
        List<Function.FunArg> funArgs = ojb.getFunArgs();
        if (funArgs != null && !funArgs.isEmpty()) {
            for (int i = 0; i < funArgs.size(); i++) {
                Function.FunArg arg = funArgs.get(i);
                if (arg.getArgType() == ArgType.COLUMN) {
                    String keywordQM = EzMybatisContent.getKeywordQM(configuration);
                    sqlBuilder.append(arg.getTable().getAlias()).append(".").append(keywordQM)
                            .append(arg.getArgValue()).append(keywordQM);
                } else if (arg.getArgType() == ArgType.FILED) {
                    EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                            ((EntityTable) arg.getTable()).getEtType());
                    String columnName = entityClassInfo.getFieldInfo((String) arg.getArgValue()).getColumnName();
                    String keywordQM = EzMybatisContent.getKeywordQM(configuration);
                    sqlBuilder.append(arg.getTable().getAlias()).append(".").append(keywordQM)
                            .append(columnName).append(keywordQM);
                } else if (arg.getArgType() == ArgType.FUNC) {
                    this.doBuildSql(type, sqlBuilder, configuration, (Function) arg.getArgValue(), mybatisParamHolder);
                } else if (arg.getArgType() == ArgType.FORMULA) {
                    Converter<? extends Formula> converter = EzMybatisContent.getConverter(configuration,
                            ((Formula) arg.getArgValue()).getClass());
                    converter.buildSql(type, sqlBuilder, configuration, arg.getArgValue(), mybatisParamHolder);
                } else if (arg.getArgType() == ArgType.CASE_WHEN) {
                    Converter<? extends CaseWhen> converter = EzMybatisContent.getConverter(configuration,
                            ((CaseWhen) arg.getArgValue()).getClass());
                    converter.buildSql(type, sqlBuilder, configuration, arg.getArgValue(), mybatisParamHolder);
                } else if (arg.getArgType() == ArgType.VALUE) {
                    sqlBuilder.append(mybatisParamHolder.getMybatisParamName(arg.getArgValue()));
                }
                if (i + 1 < funArgs.size()) {
                    sqlBuilder.append(", ");
                }
            }
        }
        sqlBuilder.append(") ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
