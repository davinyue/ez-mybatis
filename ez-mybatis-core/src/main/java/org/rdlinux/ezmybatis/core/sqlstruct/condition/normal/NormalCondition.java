package org.rdlinux.ezmybatis.core.sqlstruct.condition.normal;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamEscape;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

import java.util.Collection;

/**
 * 普通条件
 */
@Getter
public abstract class NormalCondition implements Condition {
    protected LoginSymbol loginSymbol;
    protected Operator operator;
    protected Object value;

    protected abstract String getSqlField(Configuration configuration);

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        StringBuilder sql = new StringBuilder();
        String param = mybatisParamHolder.getParamName(this.getValue());
        sql.append(" ").append(this.getSqlField(configuration)).append(" ")
                .append(this.getOperator().getOperator()).append(" ");
        if (this.getOperator() == Operator.in) {
            sql.append("( ");
            if (this.getValue() instanceof Collection) {
                int i = 0;
                for (Object value : (Collection<?>) this.getValue()) {
                    sql.append(MybatisParamEscape.getEscapeChar(value)).append("{").append(param)
                            .append("[").append(i).append("]").append("}");
                    if (i + 1 < ((Collection<?>) this.getValue()).size()) {
                        sql.append(", ");
                    }
                    i++;
                }
            } else if (this.getValue().getClass().isArray()) {
                int i = 0;
                for (Object value : (Object[]) this.getValue()) {
                    sql.append(MybatisParamEscape.getEscapeChar(value)).append("{").append(param)
                            .append("[").append(i).append("]").append("}");
                    if (i + 1 < ((Object[]) this.getValue()).length) {
                        sql.append(", ");
                    }
                    i++;
                }
            } else {
                sql.append(MybatisParamEscape.getEscapeChar(this.getValue())).append("{").append(param)
                        .append("}").append(" ");
            }
            sql.append(" ) ");
        } else {
            sql.append(MybatisParamEscape.getEscapeChar(this.getValue())).append("{").append(param)
                    .append("}").append(" ");
        }
        return sql.toString();
    }
}
