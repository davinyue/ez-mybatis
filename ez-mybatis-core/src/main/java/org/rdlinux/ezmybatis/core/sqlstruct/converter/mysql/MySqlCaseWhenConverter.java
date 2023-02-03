package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
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

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       CaseWhen caseWhen, MybatisParamHolder mybatisParamHolder) {
        if (caseWhen == null || caseWhen.getCaseWhenData() == null || caseWhen.getCaseWhenData().isEmpty()) {
            return sqlBuilder;
        }
        sqlBuilder.append(" CASE ");
        List<CaseWhen.CaseWhenData> caseWhenData = caseWhen.getCaseWhenData();
        for (CaseWhen.CaseWhenData caseWhenDatum : caseWhenData) {
            sqlBuilder.append(" WHEN ");
            StringBuilder whenSql = MySqlWhereConverter.conditionsToSqlPart(new StringBuilder(), configuration,
                    mybatisParamHolder, caseWhenDatum.getConditions());
            sqlBuilder.append(whenSql).append(" ");
            sqlBuilder.append(" THEN ");
            String paramName = mybatisParamHolder.getParamName(caseWhenDatum.getValue(), true);
            sqlBuilder.append(paramName);
        }
        CaseWhen.CaseWhenElse caseWhenElse = caseWhen.getCaseWhenElse();
        if (caseWhenElse != null) {
            sqlBuilder.append(" ELSE ");
            String elseValueParamName = mybatisParamHolder.getParamName(caseWhenElse.getValue(), true);
            sqlBuilder.append(elseValueParamName);
        }
        sqlBuilder.append(" END ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
