package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
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

    private void handleValue(Type type, CaseWhen.CaseWhenData caseWhenDatum, SqlGenerateContext sqlGenerateContext) {
        Operand value = caseWhenDatum.getValue();
        Converter<? extends Operand> converter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                value.getClass());
        converter.buildSql(type, value, sqlGenerateContext);
    }

    @Override
    protected void doBuildSql(Type type, CaseWhen caseWhen, SqlGenerateContext sqlGenerateContext) {
        if (caseWhen == null || caseWhen.getCaseWhenData() == null || caseWhen.getCaseWhenData().isEmpty()) {
            return;
        }
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlBuilder.append(" (CASE ");
        List<CaseWhen.CaseWhenData> caseWhenData = caseWhen.getCaseWhenData();
        for (CaseWhen.CaseWhenData caseWhenDatum : caseWhenData) {
            sqlBuilder.append(" WHEN ");
            StringBuilder whenSql = MySqlWhereConverter.conditionsToSql(type, sqlGenerateContext,
                    caseWhenDatum.getConditions());
            sqlBuilder.append(whenSql).append(" ");
            sqlBuilder.append(" THEN ");
            this.handleValue(type, caseWhenDatum, sqlGenerateContext);
        }
        CaseWhen.CaseWhenData els = caseWhen.getEls();
        if (els != null) {
            sqlBuilder.append(" ELSE ");
            this.handleValue(type, els, sqlGenerateContext);
        }
        sqlBuilder.append(" END) ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
