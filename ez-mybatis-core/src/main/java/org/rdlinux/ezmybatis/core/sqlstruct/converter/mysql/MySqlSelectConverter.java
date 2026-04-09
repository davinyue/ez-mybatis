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

/**
 * MySQL 方言下的 SELECT 转换器。
 */
public class MySqlSelectConverter extends AbstractConverter<Select> implements Converter<Select> {
    /**
     * 单例实例。
     */
    private static volatile MySqlSelectConverter instance;

    protected MySqlSelectConverter() {
    }

    /**
     * 返回转换器单例。
     *
     * @return 转换器实例
     */
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

    /**
     * 将 SELECT 结构拼接为 MySQL SQL 片段。
     *
     * @param type               构建类型
     * @param select             SELECT 结构
     * @param sqlGenerateContext SQL 构建上下文
     */
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
        if (select.getSelectItems() == null || select.getSelectItems().isEmpty()) {
            sqlBuilder.append(select.getQuery().getFrom().getTable().getAlias()).append(".* ");
        } else {
            List<SelectItem> selectFields = select.getSelectItems();
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
