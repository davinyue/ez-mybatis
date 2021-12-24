package ink.dvc.ezmybatis.core.sqlstruct.condition.normal;

import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamEscape;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Condition;
import ink.dvc.ezmybatis.core.sqlstruct.condition.Operator;
import ink.dvc.ezmybatis.core.utils.Assert;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;

import java.util.Collection;

/**
 * 普通条件
 */
@Getter
public abstract class NormalCondition implements Condition {
    protected LoginSymbol loginSymbol;
    protected Operator operator;
    protected Object value;

    public NormalCondition(LoginSymbol loginSymbol, Operator operator, Object value) {
        if (loginSymbol == null) {
            loginSymbol = LoginSymbol.AND;
        }
        if (operator == Operator.between || operator == Operator.notBetween || operator == Operator.isNull ||
                operator == Operator.isNotNull) {
            throw new IllegalArgumentException("Unsupported operator");
        }
        Assert.notNull(value, "value can not be null");
        this.operator = operator;
        this.loginSymbol = loginSymbol;
        this.value = value;
    }

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
                    String inValueParam = mybatisParamHolder.getParamName(value);
                    sql.append(MybatisParamEscape.getEscapeChar(value)).append("{").append(inValueParam).append("}");
                    if (i + 1 < ((Collection<?>) this.getValue()).size()) {
                        sql.append(", ");
                    }
                    i++;
                }
            } else if (this.getValue().getClass().isArray()) {
                int i = 0;
                for (Object value : (Object[]) this.getValue()) {
                    String inValueParam = mybatisParamHolder.getParamName(value);
                    sql.append(MybatisParamEscape.getEscapeChar(value)).append("{").append(inValueParam).append("}");
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
