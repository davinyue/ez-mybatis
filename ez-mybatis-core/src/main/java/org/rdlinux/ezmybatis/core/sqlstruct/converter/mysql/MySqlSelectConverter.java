package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.Select;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlHint;
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
    protected void doBuildSql(Type type, Select select, SqlGenerateContext sqlGenerateContext) {
        if (select == null) {
            return;
        }
        Configuration configuration = sqlGenerateContext.getConfiguration();
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlBuilder.append("SELECT ");
        if (select.getSqlHint() != null) {
            Converter<? extends SqlHint> sqlHintConverter = EzMybatisContent.getConverter(configuration,
                    select.getSqlHint().getClass());
            sqlHintConverter.buildSql(type, select.getSqlHint(), sqlGenerateContext);
        }
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
                converter.buildSql(type, selectItem, sqlGenerateContext);
                if (i + 1 < selectFields.size()) {
                    sqlBuilder.append(", ");
                }
            }
        }
    }

}
