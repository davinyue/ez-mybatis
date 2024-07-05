package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

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
        Operand value = caseWhenDatum.getValue();
        Converter<? extends Operand> converter = EzMybatisContent.getConverter(configuration, value.getClass());
        converter.buildSql(type, sqlBuilder, configuration, value, mybatisParamHolder);
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
