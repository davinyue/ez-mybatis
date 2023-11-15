package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectTableAllItem;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.utils.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class MySqlSelectTableAllItemConverter extends AbstractConverter<SelectTableAllItem> implements Converter<SelectTableAllItem> {
    private static volatile MySqlSelectTableAllItemConverter instance;

    protected MySqlSelectTableAllItemConverter() {
    }

    public static MySqlSelectTableAllItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectTableAllItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectTableAllItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectTableAllItem ojb, MybatisParamHolder mybatisParamHolder) {
        boolean execExcludeField = false;
        if (ojb.getTable() instanceof EntityTable) {
            if (ojb.getExcludeField() != null && ojb.getExcludeField().size() > 0) {
                execExcludeField = true;
            }
        }
        if (execExcludeField) {
            EntityTable table = (EntityTable) ojb.getTable();
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, table.getEtType());
            List<EntityFieldInfo> fieldInfos = entityClassInfo.getFieldInfos();
            fieldInfos = fieldInfos.stream().filter(e -> !ojb.getExcludeField().contains(e.getFieldName()))
                    .collect(Collectors.toList());
            Assert.notEmpty(fieldInfos, "No valid select item");
            for (int i = 0; i < fieldInfos.size(); i++) {
                EntityFieldInfo fieldInfo = fieldInfos.get(i);
                sqlBuilder.append(" ").append(ojb.getTable().getAlias()).append(".").append(fieldInfo.getColumnName());
                if (i + 1 < fieldInfos.size()) {
                    sqlBuilder.append(", ");
                }
            }
            return sqlBuilder;
        } else {
            return sqlBuilder.append(" ").append(ojb.getTable().getAlias()).append(".* ");
        }
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
