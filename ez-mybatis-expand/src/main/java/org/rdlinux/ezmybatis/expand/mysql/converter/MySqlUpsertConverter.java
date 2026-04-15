package org.rdlinux.ezmybatis.expand.mysql.converter;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.sqlgenerate.AbstractInsertSqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.TableColumn;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateColumnItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;
import org.rdlinux.ezmybatis.expand.mysql.update.MySqlUpsert;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

import java.util.List;

public class MySqlUpsertConverter extends AbstractConverter<MySqlUpsert> implements Converter<MySqlUpsert> {
    private static volatile MySqlUpsertConverter instance;

    protected MySqlUpsertConverter() {
    }

    public static MySqlUpsertConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlUpsertConverter.class) {
                if (instance == null) {
                    instance = new MySqlUpsertConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, MySqlUpsert upsert, SqlGenerateContext sqlGenerateContext) {
        String tableName = AbstractInsertSqlGenerate.getTableName(sqlGenerateContext, upsert.getTable(),
                upsert.getInsertEntity());
        List<EzMybatisInsertListener> listeners = EzMybatisContent
                .getInsertListeners(sqlGenerateContext.getConfiguration());
        if (listeners != null) {
            for (EzMybatisInsertListener listener : listeners) {
                listener.onInsert(upsert.getInsertEntity());
            }
        }
        AbstractInsertSqlGenerate.InsertSqlParts insertSqlParts = AbstractInsertSqlGenerate.getInsertSqlParts(
                sqlGenerateContext, upsert.getInsertEntity());
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlBuilder.append("INSERT INTO ").append(tableName).append(" ")
                .append(insertSqlParts.getColumnsSql())
                .append(" VALUES ")
                .append(insertSqlParts.getValuesSql())
                .append(" ON DUPLICATE KEY UPDATE ");
        List<UpdateItem> items = upsert.getSet().getItems();
        for (int i = 0; i < items.size(); i++) {
            this.buildUpdateItemSql(items.get(i), sqlGenerateContext);
            if (i + 1 < items.size()) {
                sqlBuilder.append(", ");
            }
        }
    }

    private void buildUpdateItemSql(UpdateItem updateItem, SqlGenerateContext sqlGenerateContext) {
        if (updateItem instanceof UpdateFieldItem) {
            this.buildUpdateFieldItemSql((UpdateFieldItem) updateItem, sqlGenerateContext);
            return;
        }
        if (updateItem instanceof UpdateColumnItem) {
            this.buildUpdateColumnItemSql((UpdateColumnItem) updateItem, sqlGenerateContext);
            return;
        }
        throw new IllegalArgumentException("Unsupported upsert update item type: " + updateItem.getClass().getName());
    }

    private void buildUpdateFieldItemSql(UpdateFieldItem updateItem, SqlGenerateContext sqlGenerateContext) {
        Configuration configuration = sqlGenerateContext.getConfiguration();
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, updateItem.getEntityTable().getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(updateItem.getField());
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(configuration);
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        sqlGenerateContext.pushAccessField(EntityField.of(updateItem.getEntityTable(), updateItem.getField()));
        sqlBuilder.append(keywordQM).append(SqlEscaping.nameEscaping(fieldInfo.getColumnName())).append(keywordQM)
                .append(" = ");
        this.buildOperandSql(updateItem.getValue(), sqlGenerateContext);
        sqlGenerateContext.popAccessField();
    }

    private void buildUpdateColumnItemSql(UpdateColumnItem updateItem, SqlGenerateContext sqlGenerateContext) {
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(sqlGenerateContext.getConfiguration());
        sqlGenerateContext.getSqlBuilder()
                .append(keywordQM).append(SqlEscaping.nameEscaping(updateItem.getColumn())).append(keywordQM)
                .append(" = ");
        this.buildOperandSql(updateItem.getValue(), sqlGenerateContext);
    }

    private void buildOperandSql(Operand operand, SqlGenerateContext sqlGenerateContext) {
        if (operand instanceof EntityField) {
            this.buildEntityFieldSql((EntityField) operand, sqlGenerateContext);
            return;
        }
        if (operand instanceof TableColumn) {
            this.buildTableColumnSql((TableColumn) operand, sqlGenerateContext);
            return;
        }
        Converter<? extends Operand> argConverter = EzMybatisContent.getConverter(sqlGenerateContext.getConfiguration(),
                operand.getClass());
        argConverter.buildSql(Type.UPDATE, operand, sqlGenerateContext);
    }

    private void buildEntityFieldSql(EntityField entityField, SqlGenerateContext sqlGenerateContext) {
        Configuration configuration = sqlGenerateContext.getConfiguration();
        EntityTable table = entityField.getTable();
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, table.getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(entityField.getField());
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(configuration);
        sqlGenerateContext.getSqlBuilder()
                .append(keywordQM).append(SqlEscaping.nameEscaping(fieldInfo.getColumnName())).append(keywordQM);
    }

    private void buildTableColumnSql(TableColumn tableColumn, SqlGenerateContext sqlGenerateContext) {
        String keywordQM = EzMybatisContent.getKeywordQuoteMark(sqlGenerateContext.getConfiguration());
        sqlGenerateContext.getSqlBuilder()
                .append(keywordQM).append(SqlEscaping.nameEscaping(tableColumn.getColumn())).append(keywordQM);
    }
}
