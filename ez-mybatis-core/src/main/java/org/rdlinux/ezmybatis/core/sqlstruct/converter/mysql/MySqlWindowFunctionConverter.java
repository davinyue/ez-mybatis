package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy;
import org.rdlinux.ezmybatis.core.sqlstruct.WindowFunction;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

import java.util.List;

public class MySqlWindowFunctionConverter extends AbstractConverter<WindowFunction> {
    private static volatile MySqlWindowFunctionConverter instance;

    protected MySqlWindowFunctionConverter() {
    }

    public static MySqlWindowFunctionConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlWindowFunctionConverter.class) {
                if (instance == null) {
                    instance = new MySqlWindowFunctionConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, WindowFunction obj, SqlGenerateContext sqlGenerateContext) {
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();

        // 基础函数部分，如 ROW_NUMBER()
        Function function = obj.getFunction();
        Converter<Function> functionConverter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                Function.class);
        functionConverter.buildSql(type, function, sqlGenerateContext);

        sqlBuilder.append(" OVER (");

        boolean hasPartition = false;
        List<Operand> partitionByList = obj.getPartitionBy();
        if (partitionByList != null && !partitionByList.isEmpty()) {
            sqlBuilder.append(" PARTITION BY ");
            for (int i = 0; i < partitionByList.size(); i++) {
                Operand partitionObj = partitionByList.get(i);
                Converter<? extends Operand> converter = EzMybatisContent.getConverter(
                        sqlGenerateContext.getConfiguration(), partitionObj.getClass());
                converter.buildSql(type, partitionObj, sqlGenerateContext);
                if (i + 1 < partitionByList.size()) {
                    sqlBuilder.append(", ");
                }
            }
            hasPartition = true;
        }

        List<OrderBy.OrderItem> orderByList = obj.getOrderBy();
        if (orderByList != null && !orderByList.isEmpty()) {
            if (hasPartition) {
                sqlBuilder.append(" ");
            }
            sqlBuilder.append("ORDER BY ");
            Converter<OrderBy.OrderItem> converter = EzMybatisContent.getConverter(
                    sqlGenerateContext.getConfiguration(), OrderBy.OrderItem.class);
            for (int i = 0; i < orderByList.size(); i++) {
                OrderBy.OrderItem orderItem = orderByList.get(i);
                converter.buildSql(type, orderItem, sqlGenerateContext);
                if (i + 1 < orderByList.size()) {
                    sqlBuilder.append(", ");
                }
            }
        }

        WindowFunction.WindowFrameType frameType = obj.getFrameType();
        if (frameType != null) {
            if (hasPartition || (orderByList != null && !orderByList.isEmpty())) {
                sqlBuilder.append(" ");
            }
            sqlBuilder.append(frameType.name()).append(" ");
            WindowFunction.WindowFrameBound frameStart = obj.getFrameStart();
            WindowFunction.WindowFrameBound frameEnd = obj.getFrameEnd();
            if (frameEnd == null) {
                if (frameStart.getOffset() != null) {
                    sqlBuilder.append(frameStart.getOffset()).append(" ");
                }
                sqlBuilder.append(frameStart.getType().getSql());
            } else {
                sqlBuilder.append("BETWEEN ");
                if (frameStart.getOffset() != null) {
                    sqlBuilder.append(frameStart.getOffset()).append(" ");
                }
                sqlBuilder.append(frameStart.getType().getSql()).append(" AND ");
                if (frameEnd.getOffset() != null) {
                    sqlBuilder.append(frameEnd.getOffset()).append(" ");
                }
                sqlBuilder.append(frameEnd.getType().getSql());
            }
        }

        sqlBuilder.append(") ");
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
