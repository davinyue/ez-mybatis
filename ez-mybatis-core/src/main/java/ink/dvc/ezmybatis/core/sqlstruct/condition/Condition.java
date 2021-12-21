package ink.dvc.ezmybatis.core.sqlstruct.condition;

import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.apache.ibatis.session.Configuration;

/**
 * 条件
 */
public interface Condition {
    /**
     * 获取逻辑运算符号
     */
    LoginSymbol getLoginSymbol();

    String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder);

    static enum LoginSymbol {
        OR,
        AND;
    }
}
