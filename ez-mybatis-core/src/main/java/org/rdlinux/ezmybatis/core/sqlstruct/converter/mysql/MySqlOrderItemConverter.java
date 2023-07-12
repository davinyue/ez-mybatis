package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.ArgType;
import org.rdlinux.ezmybatis.core.sqlstruct.CaseWhen;
import org.rdlinux.ezmybatis.core.sqlstruct.Function;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy.OrderItem;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderType;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class MySqlOrderItemConverter extends AbstractConverter<OrderItem> implements Converter<OrderItem> {
    private static volatile MySqlOrderItemConverter instance;

    protected MySqlOrderItemConverter() {
    }

    public static MySqlOrderItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlOrderItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlOrderItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, OrderItem obj,
                                       MybatisParamHolder mybatisParamHolder) {
        if (obj.getOrderType() == null) {
            obj.setOrderType(OrderType.ASC);
        }
        if (obj.getArgType() == ArgType.COLUMN) {
            String keywordQM = EzMybatisContent.getKeywordQM(configuration);
            sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM)
                    .append(obj.getValue()).append(keywordQM);
        } else if (obj.getArgType() == ArgType.FILED) {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                    ((EntityTable) obj.getTable()).getEtType());
            String columnName = entityClassInfo.getFieldInfo((String) obj.getValue()).getColumnName();
            String keywordQM = EzMybatisContent.getKeywordQM(configuration);
            sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM)
                    .append(columnName).append(keywordQM);
        } else if (obj.getArgType() == ArgType.FUNC) {
            Converter<? extends Function> converter = EzMybatisContent.getConverter(configuration,
                    ((Function) obj.getValue()).getClass());
            converter.buildSql(type, sqlBuilder, configuration, obj.getValue(), mybatisParamHolder);
        } else if (obj.getArgType() == ArgType.FORMULA) {
            Converter<? extends Formula> converter = EzMybatisContent.getConverter(configuration,
                    ((Formula) obj.getValue()).getClass());
            converter.buildSql(type, sqlBuilder, configuration, obj.getValue(), mybatisParamHolder);
        } else if (obj.getArgType() == ArgType.CASE_WHEN) {
            Converter<? extends CaseWhen> converter = EzMybatisContent.getConverter(configuration,
                    ((CaseWhen) obj.getValue()).getClass());
            converter.buildSql(type, sqlBuilder, configuration, obj.getValue(), mybatisParamHolder);
        } else if (obj.getArgType() == ArgType.VALUE) {
            sqlBuilder.append(mybatisParamHolder.getMybatisParamName(obj.getValue()));
        } else if (obj.getArgType() == ArgType.ALIAS) {
            String keywordQM = EzMybatisContent.getKeywordQM(configuration);
            sqlBuilder.append(keywordQM).append(obj.getValue()).append(keywordQM);
        } else if (obj.getArgType() == ArgType.KEYWORDS) {
            sqlBuilder.append(obj.getValue());
        }
        sqlBuilder.append(" ").append(obj.getOrderType().name()).append(" ");
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
