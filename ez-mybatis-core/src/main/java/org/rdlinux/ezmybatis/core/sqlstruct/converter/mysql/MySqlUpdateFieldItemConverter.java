package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.Operand;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;

public class MySqlUpdateFieldItemConverter extends AbstractConverter<UpdateFieldItem> implements Converter<UpdateFieldItem> {
    private static volatile MySqlUpdateFieldItemConverter instance;

    protected MySqlUpdateFieldItemConverter() {
    }

    public static MySqlUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    protected boolean appendAlias() {
        return true;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       UpdateFieldItem obj, MybatisParamHolder mybatisParamHolder) {

        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getEntityTable().getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(obj.getField());
        EzMybatisContent.setCurrentAccessField(EntityField.of(obj.getEntityTable(), obj.getField()));
        Converter<? extends Operand> argConverter = EzMybatisContent.getConverter(configuration,
                obj.getValue().getClass());
        StringBuilder valueSql = argConverter.buildSql(type, new StringBuilder(), configuration, obj.getValue(),
                mybatisParamHolder);
        if (this.appendAlias()) {
            sqlBuilder.append(obj.getTable().getAlias()).append(".");
        }
        String column = fieldInfo.getColumnName();
        sqlBuilder.append(keywordQM).append(column).append(keywordQM).append(" = ").append(valueSql);
        EzMybatisContent.cleanCurrentAccessField();
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
