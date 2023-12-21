package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.normal.NormalFieldCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlNormalFieldConditionConverter;

public class PostgreSqlNormalFieldConditionConverter extends MySqlNormalFieldConditionConverter implements Converter<NormalFieldCondition> {
    private static volatile PostgreSqlNormalFieldConditionConverter instance;

    protected PostgreSqlNormalFieldConditionConverter() {
    }

    public static PostgreSqlNormalFieldConditionConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlNormalFieldConditionConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlNormalFieldConditionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected String getOperatorStr(Operator operator) {
        if (Operator.regexp == operator) {
            return "~";
        }
        return super.getOperatorStr(operator);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
