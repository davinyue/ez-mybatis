package org.rdlinux.ezmybatis.core.sqlstruct;

import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectOperand;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.enumeration.Operator;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 操作数, 左值/右值
 */
public interface Operand extends SqlStruct {
    static Operand objToOperand(Object object) {
        if (object instanceof Operand) {
            return (Operand) object;
        } else {
            return ObjArg.of(object);
        }
    }

    static Collection<?> valueToCollection(Object obj) {
        if (obj == null) {
            return Collections.emptyList();
        }
        if (obj instanceof Collection) {
            return (Collection<?>) obj;
        } else if (obj.getClass().isArray()) {
            //原始类型数组, 比如int[]
            if (obj.getClass().getComponentType().isPrimitive()) {
                int length = Array.getLength(obj);
                List<Object> ret = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    Object element = Array.get(obj, i);
                    ret.add(element);
                }
                return ret;
            }
            return Arrays.asList((Object[]) obj);
        } else {
            return Collections.singletonList(obj);
        }
    }

    static List<Operand> valueToArgList(Object value) {
        Collection<?> objects = valueToCollection(value);
        List<Operand> args = new ArrayList<>(objects.size());
        for (Object datum : objects) {
            args.add(objToOperand(datum));
        }
        return args;
    }

    static List<Operand> valueToArgListFromVarargs(Object... values) {
        if (values != null && values.length == 1) {
            Object first = values[0];
            if (first instanceof Collection || (first != null && first.getClass().isArray())) {
                return valueToArgList(first);
            }
        }
        return valueToArgList(values);
    }

    // ================== isNull / isNotNull ==================

    /**
     * 构建基于此操作数的 IS NULL (为空) AND 条件
     *
     * @return 对比条件
     */
    default Condition isNull() {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.isNull);
    }

    /**
     * 构建基于此操作数的 IS NULL (为空) OR 条件
     *
     * @return 对比条件
     */
    default Condition orIsNull() {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.isNull);
    }

    /**
     * 构建基于此操作数的 IS NOT NULL (不为空) AND 条件
     *
     * @return 对比条件
     */
    default Condition isNotNull() {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.isNotNull);
    }

    /**
     * 构建基于此操作数的 IS NOT NULL (不为空) OR 条件
     *
     * @return 对比条件
     */
    default Condition orIsNotNull() {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.isNotNull);
    }

    // ================== eq ==================

    /**
     * 构建基于此操作数的等于 (=) AND 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition eq(Operand operand) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.eq, operand);
    }

    /**
     * 构建基于此操作数的等于 (=) AND 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition eq(Object operand) {
        return this.eq(Operand.objToOperand(operand));
    }

    /**
     * 构建基于此操作数的等于 (=) OR 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition orEq(Operand operand) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.eq, operand);
    }

    /**
     * 构建基于此操作数的等于 (=) OR 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition orEq(Object operand) {
        return this.orEq(Operand.objToOperand(operand));
    }

    // ================== ne ==================

    /**
     * 构建基于此操作数的不等于 (!=) AND 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition ne(Operand operand) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.ne, operand);
    }

    /**
     * 构建基于此操作数的不等于 (!=) AND 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition ne(Object operand) {
        return this.ne(Operand.objToOperand(operand));
    }

    /**
     * 构建基于此操作数的不等于 (!=) OR 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition orNe(Operand operand) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.ne, operand);
    }

    /**
     * 构建基于此操作数的不等于 (!=) OR 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition orNe(Object operand) {
        return this.orNe(Operand.objToOperand(operand));
    }

    // ================== gt ==================

    /**
     * 构建基于此操作数的大于 (>) AND 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition gt(Operand operand) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.gt, operand);
    }

    /**
     * 构建基于此操作数的大于 (>) AND 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition gt(Object operand) {
        return this.gt(Operand.objToOperand(operand));
    }

    /**
     * 构建基于此操作数的大于 (>) OR 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition orGt(Operand operand) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.gt, operand);
    }

    /**
     * 构建基于此操作数的大于 (>) OR 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition orGt(Object operand) {
        return this.orGt(Operand.objToOperand(operand));
    }

    // ================== ge ==================

    /**
     * 构建基于此操作数的大于等于 (>=) AND 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition ge(Operand operand) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.ge, operand);
    }

    /**
     * 构建基于此操作数的大于等于 (>=) AND 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition ge(Object operand) {
        return this.ge(Operand.objToOperand(operand));
    }

    /**
     * 构建基于此操作数的大于等于 (>=) OR 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition orGe(Operand operand) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.ge, operand);
    }

    /**
     * 构建基于此操作数的大于等于 (>=) OR 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition orGe(Object operand) {
        return this.orGe(Operand.objToOperand(operand));
    }

    // ================== lt ==================

    /**
     * 构建基于此操作数的小于 (<) AND 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition lt(Operand operand) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.lt, operand);
    }

    /**
     * 构建基于此操作数的小于 (<) AND 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition lt(Object operand) {
        return this.lt(Operand.objToOperand(operand));
    }

    /**
     * 构建基于此操作数的小于 (<) OR 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition orLt(Operand operand) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.lt, operand);
    }

    /**
     * 构建基于此操作数的小于 (<) OR 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition orLt(Object operand) {
        return this.orLt(Operand.objToOperand(operand));
    }

    // ================== le ==================

    /**
     * 构建基于此操作数的小于等于 (<=) AND 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition le(Operand operand) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.le, operand);
    }

    /**
     * 构建基于此操作数的小于等于 (<=) AND 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition le(Object operand) {
        return this.le(Operand.objToOperand(operand));
    }

    /**
     * 构建基于此操作数的小于等于 (<=) OR 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition orLe(Operand operand) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.le, operand);
    }

    /**
     * 构建基于此操作数的小于等于 (<=) OR 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition orLe(Object operand) {
        return this.orLe(Operand.objToOperand(operand));
    }

    // ================== like ==================

    /**
     * 构建基于此操作数的 LIKE (模糊匹配) AND 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition like(Operand operand) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.like, operand);
    }

    /**
     * 构建基于此操作数的 LIKE (模糊匹配) AND 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition like(Object operand) {
        return this.like(Operand.objToOperand(operand));
    }

    /**
     * 构建基于此操作数的 LIKE (模糊匹配) OR 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition orLike(Operand operand) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.like, operand);
    }

    /**
     * 构建基于此操作数的 LIKE (模糊匹配) OR 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition orLike(Object operand) {
        return this.orLike(Operand.objToOperand(operand));
    }

    // ================== unlike ==================

    /**
     * 构建基于此操作数的 NOT LIKE (不匹配) AND 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition unlike(Operand operand) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.unlike, operand);
    }

    /**
     * 构建基于此操作数的 NOT LIKE (不匹配) AND 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition unlike(Object operand) {
        return this.unlike(Operand.objToOperand(operand));
    }

    /**
     * 构建基于此操作数的 NOT LIKE (不匹配) OR 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition orUnlike(Operand operand) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.unlike, operand);
    }

    /**
     * 构建基于此操作数的 NOT LIKE (不匹配) OR 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition orUnlike(Object operand) {
        return this.orUnlike(Operand.objToOperand(operand));
    }

    // ================== regexp ==================

    /**
     * 构建基于此操作数的 REGEXP (正则匹配) AND 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition regexp(Operand operand) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.regexp, operand);
    }

    /**
     * 构建基于此操作数的 REGEXP (正则匹配) AND 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition regexp(Object operand) {
        return this.regexp(Operand.objToOperand(operand));
    }

    /**
     * 构建基于此操作数的 REGEXP (正则匹配) OR 条件
     *
     * @param operand 另一个操作数
     * @return 对比条件
     */
    default Condition orRegexp(Operand operand) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.regexp, operand);
    }

    /**
     * 构建基于此操作数的 REGEXP (正则匹配) OR 条件
     *
     * @param operand 通用对象，将自动转换为操作数
     * @return 对比条件
     */
    default Condition orRegexp(Object operand) {
        return this.orRegexp(Operand.objToOperand(operand));
    }

    // ================== between ==================

    /**
     * 构建基于此操作数的 BETWEEN (在...之间) AND 条件
     *
     * @param min 最小值操作数
     * @param max 最大值操作数
     * @return 对比条件
     */
    default Condition between(Operand min, Operand max) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.between, min, max);
    }

    /**
     * 构建基于此操作数的 BETWEEN (在...之间) AND 条件
     *
     * @param min 最小值通用对象
     * @param max 最大值通用对象
     * @return 对比条件
     */
    default Condition between(Object min, Object max) {
        return this.between(Operand.objToOperand(min), Operand.objToOperand(max));
    }

    /**
     * 构建基于此操作数的 BETWEEN (在...之间) OR 条件
     *
     * @param min 最小值操作数
     * @param max 最大值操作数
     * @return 对比条件
     */
    default Condition orBetween(Operand min, Operand max) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.between, min, max);
    }

    /**
     * 构建基于此操作数的 BETWEEN (在...之间) OR 条件
     *
     * @param min 最小值通用对象
     * @param max 最大值通用对象
     * @return 对比条件
     */
    default Condition orBetween(Object min, Object max) {
        return this.orBetween(Operand.objToOperand(min), Operand.objToOperand(max));
    }

    // ================== notBetween ==================

    /**
     * 构建基于此操作数的 NOT BETWEEN (不在...之间) AND 条件
     *
     * @param min 最小值操作数
     * @param max 最大值操作数
     * @return 对比条件
     */
    default Condition notBetween(Operand min, Operand max) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.notBetween, min, max);
    }

    /**
     * 构建基于此操作数的 NOT BETWEEN (不在...之间) AND 条件
     *
     * @param min 最小值通用对象
     * @param max 最大值通用对象
     * @return 对比条件
     */
    default Condition notBetween(Object min, Object max) {
        return this.notBetween(Operand.objToOperand(min), Operand.objToOperand(max));
    }

    /**
     * 构建基于此操作数的 NOT BETWEEN (不在...之间) OR 条件
     *
     * @param min 最小值操作数
     * @param max 最大值操作数
     * @return 对比条件
     */
    default Condition orNotBetween(Operand min, Operand max) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.notBetween, min, max);
    }

    /**
     * 构建基于此操作数的 NOT BETWEEN (不在...之间) OR 条件
     *
     * @param min 最小值通用对象
     * @param max 最大值通用对象
     * @return 对比条件
     */
    default Condition orNotBetween(Object min, Object max) {
        return this.orNotBetween(Operand.objToOperand(min), Operand.objToOperand(max));
    }

    // ================== in ==================

    /**
     * 构建基于此操作数的 IN (在...其中) AND 条件
     *
     * @param operands 操作数列表
     * @return 对比条件
     */
    default Condition in(Collection<?> operands) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.in, valueToArgList(operands));
    }

    /**
     * 构建基于此操作数的 IN (在...其中) AND 条件
     *
     * @param operands 操作数数组
     * @return 对比条件
     */
    default Condition in(Object... operands) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.in, valueToArgListFromVarargs(operands));
    }

    /**
     * 构建基于此操作数的 IN (在...其中) OR 条件
     *
     * @param operands 操作数列表
     * @return 对比条件
     */
    default Condition orIn(Collection<?> operands) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.in, valueToArgList(operands));
    }

    /**
     * 构建基于此操作数的 IN (在...其中) OR 条件
     *
     * @param operands 操作数数组
     * @return 对比条件
     */
    default Condition orIn(Object... operands) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.in, valueToArgListFromVarargs(operands));
    }

    // ================== notIn ==================

    /**
     * 构建基于此操作数的 NOT IN (不在...其中) AND 条件
     *
     * @param operands 操作数列表
     * @return 对比条件
     */
    default Condition notIn(Collection<?> operands) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.notIn, valueToArgList(operands));
    }

    /**
     * 构建基于此操作数的 NOT IN (不在...其中) AND 条件
     *
     * @param operands 操作数数组
     * @return 对比条件
     */
    default Condition notIn(Object... operands) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.notIn, valueToArgListFromVarargs(operands));
    }

    /**
     * 构建基于此操作数的 NOT IN (不在...其中) OR 条件
     *
     * @param operands 操作数列表
     * @return 对比条件
     */
    default Condition orNotIn(Collection<?> operands) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.notIn, valueToArgList(operands));
    }

    /**
     * 构建基于此操作数的 NOT IN (不在...其中) OR 条件
     *
     * @param operands 操作数数组
     * @return 对比条件
     */
    default Condition orNotIn(Object... operands) {
        return new ArgCompareArgCondition(AndOr.OR, this, Operator.notIn, valueToArgListFromVarargs(operands));
    }

    /**
     * 构建基于此属性的最大值（MAX）聚合函数查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return MAX 函数操作数对象
     */
    default SelectOperand max(String alias) {
        return new SelectOperand(Function.build("MAX", f -> f.addArg(this)), alias);
    }

    /**
     * 构建基于此属性的最小值（MIN）聚合函数查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return MIN 函数操作数对象
     */
    default SelectOperand min(String alias) {
        return new SelectOperand(Function.build("MIN", f -> f.addArg(this)), alias);
    }

    /**
     * 构建基于此属性的求和（SUM）聚合函数查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return SUM 函数操作数对象
     */
    default SelectOperand sum(String alias) {
        return new SelectOperand(Function.build("SUM", f -> f.addArg(this)), alias);
    }

    /**
     * 构建基于此属性的平均值（AVG）聚合函数查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return AVG 函数操作数对象
     */
    default SelectOperand avg(String alias) {
        return new SelectOperand(Function.build("AVG", f -> f.addArg(this)), alias);
    }

    /**
     * 构建基于此属性的计数（COUNT）聚合函数查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return COUNT 函数操作数对象
     */
    default SelectOperand count(String alias) {
        return new SelectOperand(Function.build("COUNT", f -> f.addArg(this)), alias);
    }

    /**
     * 构建基于此属性的计数（COUNT distinct）聚合函数查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return COUNT 函数操作数对象
     */
    default SelectOperand distinctCount(String alias) {
        return new SelectOperand(Function.build("COUNT", f -> f.addDistinctArg(this)), alias);
    }

    /**
     * 构建基于此属性的绝对值（ABS）查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return ABS 函数操作数对象
     */
    default SelectOperand abs(String alias) {
        return new SelectOperand(Function.build("ABS", f -> f.addArg(this)), alias);
    }

    /**
     * 构建基于此属性转换为大写（UPPER）查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return UPPER 函数操作数对象
     */
    default SelectOperand upper(String alias) {
        return new SelectOperand(Function.build("UPPER", f -> f.addArg(this)), alias);
    }

    /**
     * 构建基于此属性转换为小写（LOWER）查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return LOWER 函数操作数对象
     */
    default SelectOperand lower(String alias) {
        return new SelectOperand(Function.build("LOWER", f -> f.addArg(this)), alias);
    }

    /**
     * 构建基于此属性进行四舍五入求整（ROUND）查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return ROUND 函数操作数对象
     */
    default SelectOperand round(String alias) {
        return new SelectOperand(Function.build("ROUND", f -> f.addArg(this)), alias);
    }

    /**
     * 构建基于此属性进行向下求整（FLOOR）查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return FLOOR 函数操作数对象
     */
    default SelectOperand floor(String alias) {
        return new SelectOperand(Function.build("FLOOR", f -> f.addArg(this)), alias);
    }

    /**
     * 构建基于此属性去除两端空格（TRIM）查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return TRIM 函数操作数对象
     */
    default SelectOperand trim(String alias) {
        return new SelectOperand(Function.build("TRIM", f -> f.addArg(this)), alias);
    }
}
