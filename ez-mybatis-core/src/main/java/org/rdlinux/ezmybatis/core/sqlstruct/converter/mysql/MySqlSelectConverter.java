package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.Select;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectItem;

import java.util.List;

public class MySqlSelectConverter extends AbstractConverter<Select> implements Converter<Select> {
    private static volatile MySqlSelectConverter instance;

    protected MySqlSelectConverter() {
    }

    public static MySqlSelectConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doToSqlPart(Type type, StringBuilder sqlBuilder, Configuration configuration, Select select,
                                        MybatisParamHolder mybatisParamHolder) {
        if (select == null) {
            return sqlBuilder;
        }
        sqlBuilder.append("SELECT ");
        if (select.isDistinct()) {
            sqlBuilder.append("DISTINCT ");
        }
        if (select.getSelectFields() == null || select.getSelectFields().isEmpty()) {
            sqlBuilder.append(select.getQuery().getFrom().getTable().getAlias()).append(".* ");
        } else {
            List<SelectItem> selectFields = select.getSelectFields();
            for (int i = 0; i < selectFields.size(); i++) {
                SelectItem selectItem = selectFields.get(i);
                Converter<?> converter = EzMybatisContent.getConverter(configuration, selectItem.getClass());
                sqlBuilder = converter.toSqlPart(type, sqlBuilder, configuration, selectItem, mybatisParamHolder);
                if (i + 1 < selectFields.size()) {
                    sqlBuilder.append(", ");
                }
            }
        }
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
