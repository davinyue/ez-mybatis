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

public class MySqlCaseWhenConverter extends AbstractConverter<CaseWhen> implements Converter<CaseWhen> {
    private static volatile MySqlCaseWhenConverter instance;

    protected MySqlCaseWhenConverter() {
    }

    public static MySqlCaseWhenConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlCaseWhenConverter.class) {
                if (instance == null) {
                    instance = new MySqlCaseWhenConverter();
                }
            }
        }
        return instance;
    }

    private void handleValue(Type type, StringBuilder sqlBuilder, Configuration configuration,
                             CaseWhen.CaseWhenData caseWhenDatum, MybatisParamHolder mybatisParamHolder) {
        ArgType argType = caseWhenDatum.getArgType();
        if (argType == ArgType.COLUMN) {
            String keywordQM = EzMybatisContent.getKeywordQM(configuration);
            sqlBuilder.append(caseWhenDatum.getTable().getAlias()).append(".").append(keywordQM)
                    .append(caseWhenDatum.getValue()).append(keywordQM);
        } else if (argType == ArgType.FILED) {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                    ((EntityTable) caseWhenDatum.getTable()).getEtType());
            String columnName = entityClassInfo.getFieldInfo((String) caseWhenDatum.getValue()).getColumnName();
            String keywordQM = EzMybatisContent.getKeywordQM(configuration);
            sqlBuilder.append(caseWhenDatum.getTable().getAlias()).append(".").append(keywordQM)
                    .append(columnName).append(keywordQM);
        } else if (argType == ArgType.FUNC) {
            Converter<? extends Function> converter = EzMybatisContent.getConverter(configuration,
                    ((Function) caseWhenDatum.getValue()).getClass());
            converter.buildSql(type, sqlBuilder, configuration, caseWhenDatum.getValue(), mybatisParamHolder);
        } else if (argType == ArgType.FORMULA) {
            Converter<? extends Formula> converter = EzMybatisContent.getConverter(configuration,
                    ((Formula) caseWhenDatum.getValue()).getClass());
            converter.buildSql(type, sqlBuilder, configuration, caseWhenDatum.getValue(), mybatisParamHolder);
        } else if (argType == ArgType.CASE_WHEN) {
            this.doBuildSql(type, sqlBuilder, configuration, (CaseWhen) caseWhenDatum.getValue(), mybatisParamHolder);
        } else if (argType == ArgType.VALUE) {
            sqlBuilder.append(mybatisParamHolder.getMybatisParamName(caseWhenDatum.getValue()));
        } else if (argType == ArgType.KEYWORDS) {
            sqlBuilder.append(caseWhenDatum.getValue());
        }
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       CaseWhen caseWhen, MybatisParamHolder mybatisParamHolder) {
        if (caseWhen == null || caseWhen.getCaseWhenData() == null || caseWhen.getCaseWhenData().isEmpty()) {
            return sqlBuilder;
        }
        sqlBuilder.append(" (CASE ");
        List<CaseWhen.CaseWhenData> caseWhenData = caseWhen.getCaseWhenData();
        for (CaseWhen.CaseWhenData caseWhenDatum : caseWhenData) {
            sqlBuilder.append(" WHEN ");
            StringBuilder whenSql = MySqlWhereConverter.conditionsToSql(type, new StringBuilder(), configuration,
                    mybatisParamHolder, caseWhenDatum.getConditions());
            sqlBuilder.append(whenSql).append(" ");
            sqlBuilder.append(" THEN ");
            this.handleValue(type, sqlBuilder, configuration, caseWhenDatum, mybatisParamHolder);
        }
        CaseWhen.CaseWhenData els = caseWhen.getEls();
        if (els != null) {
            sqlBuilder.append(" ELSE ");
            this.handleValue(type, sqlBuilder, configuration, els, mybatisParamHolder);
        }
        sqlBuilder.append(" END) ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
