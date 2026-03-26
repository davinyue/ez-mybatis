package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectOperand;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.enumeration.AndOr;
import org.rdlinux.ezmybatis.enumeration.Operator;
import org.rdlinux.ezmybatis.utils.Assert;

/**
 * 数据库表列参数，代表数据库表中的一列直接关联。
 * 可以将其用于构建查询条件（如比较、模糊查询等）、查询列定义或统计函数。
 */
@Getter
public class TableColumn implements QueryRetOperand {
    /**
     * 列归属的表对象
     */
    private final Table table;
    /**
     * 表中的列名
     */
    private final String column;

    /**
     * 构建数据库表列参数
     *
     * @param table 表对象
     * @param column 表中的列名
     */
    private TableColumn(Table table, String column) {
        Assert.notNull(table, "table can not be null");
        Assert.notNull(column, "column can not be null");
        this.table = table;
        this.column = column;
    }

    /**
     * 创建表列的静态工厂方法
     *
     * @param table 表对象
     * @param column 表中的列名
     * @return 表列构建对象
     */
    public static TableColumn of(Table table, String column) {
        return new TableColumn(table, column);
    }

    /**
     * 构建在 WHERE 语句中的等于（=）条件
     *
     * @param value 要比较的目标值
     * @return 这个列等于指定值的条件对象
     */
    public Condition eq(Object value) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.eq, Operand.objToOperand(value));
    }

    /**
     * 构建在 WHERE 语句中的大于（&gt;）条件
     *
     * @param value 要比较的目标值
     * @return 这个列大于指定值的条件对象
     */
    public Condition gt(Object value) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.gt, Operand.objToOperand(value));
    }

    /**
     * 构建在 WHERE 语句中的小于（&lt;）条件
     *
     * @param value 要比较的目标值
     * @return 这个列小于指定值的条件对象
     */
    public Condition lt(Object value) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.lt, Operand.objToOperand(value));
    }

    /**
     * 构建在 WHERE 语句中的模糊查询（LIKE）条件
     *
     * @param value 要比较的目标值，如 "%abc%"
     * @return 这个列进行模糊匹配的条件对象
     */
    public Condition like(Object value) {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.like, Operand.objToOperand(value));
    }

    /**
     * 构建在 WHERE 语句中的判断为空（IS NULL）条件
     *
     * @return 这个列为空的条件对象
     */
    public Condition isNull() {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.isNull);
    }

    /**
     * 构建在 WHERE 语句中的判断不为空（IS NOT NULL）条件
     *
     * @return 这个列不为空的条件对象
     */
    public Condition isNotNull() {
        return new ArgCompareArgCondition(AndOr.AND, this, Operator.isNotNull);
    }

    /**
     * 构建基于此列的最大值（MAX）聚合函数查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return MAX 函数操作数对象
     */
    public SelectOperand max(String alias) {
        return new SelectOperand(Function.builder("MAX").addArg(this).build(), alias);
    }

    /**
     * 构建基于此列的最小值（MIN）聚合函数查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return MIN 函数操作数对象
     */
    public SelectOperand min(String alias) {
        return new SelectOperand(Function.builder("MIN").addArg(this).build(), alias);
    }

    /**
     * 构建基于此列的求和（SUM）聚合函数查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return SUM 函数操作数对象
     */
    public SelectOperand sum(String alias) {
        return new SelectOperand(Function.builder("SUM").addArg(this).build(), alias);
    }

    /**
     * 构建基于此列的平均值（AVG）聚合函数查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return AVG 函数操作数对象
     */
    public SelectOperand avg(String alias) {
        return new SelectOperand(Function.builder("AVG").addArg(this).build(), alias);
    }

    /**
     * 构建基于此列的计数（COUNT）聚合函数查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return COUNT 函数操作数对象
     */
    public SelectOperand count(String alias) {
        return new SelectOperand(Function.builder("COUNT").addArg(this).build(), alias);
    }

    /**
     * 构建基于此列的绝对值（ABS）查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return ABS 函数操作数对象
     */
    public SelectOperand abs(String alias) {
        return new SelectOperand(Function.builder("ABS").addArg(this).build(), alias);
    }

    /**
     * 构建基于此列转换为大写（UPPER）查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return UPPER 函数操作数对象
     */
    public SelectOperand upper(String alias) {
        return new SelectOperand(Function.builder("UPPER").addArg(this).build(), alias);
    }

    /**
     * 构建基于此列转换为小写（LOWER）查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return LOWER 函数操作数对象
     */
    public SelectOperand lower(String alias) {
        return new SelectOperand(Function.builder("LOWER").addArg(this).build(), alias);
    }

    /**
     * 构建基于此列进行四舍五入求整（ROUND）查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return ROUND 函数操作数对象
     */
    public SelectOperand round(String alias) {
        return new SelectOperand(Function.builder("ROUND").addArg(this).build(), alias);
    }

    /**
     * 构建基于此列进行向下求整（FLOOR）查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return FLOOR 函数操作数对象
     */
    public SelectOperand floor(String alias) {
        return new SelectOperand(Function.builder("FLOOR").addArg(this).build(), alias);
    }

    /**
     * 构建基于此列去除两端空格（TRIM）查询列
     *
     * @param alias 查询结果的列别名，如果不需要别名可以传 null
     * @return TRIM 函数操作数对象
     */
    public SelectOperand trim(String alias) {
        return new SelectOperand(Function.builder("TRIM").addArg(this).build(), alias);
    }

    /**
     * 为当前列设置查询展示的别名
     *
     * @param alias 别名名称
     * @return 包含别名的选择操作数对象
     */
    public SelectOperand as(String alias) {
        return new SelectOperand(this, alias);
    }


}
