package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.ObjArg;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

import java.lang.reflect.Field;

public class MySqlObjArgConverter extends AbstractConverter<ObjArg> implements Converter<ObjArg> {
    private static volatile MySqlObjArgConverter instance;

    protected MySqlObjArgConverter() {
    }

    public static MySqlObjArgConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlObjArgConverter.class) {
                if (instance == null) {
                    instance = new MySqlObjArgConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       ObjArg obj, MybatisParamHolder mybatisParamHolder) {
        EntityTable entityTable = obj.getEntityTable();
        EntityClassInfo etInfo;
        EntityFieldInfo fieldInfo;
        Class<?> modelType = null;
        Field field = null;
        if (entityTable != null) {
            etInfo = EzEntityClassInfoFactory.forClass(configuration, entityTable.getEtType());
            if (etInfo != null) {
                modelType = etInfo.getEntityClass();
                fieldInfo = etInfo.getFieldInfo(obj.getField());
                if (fieldInfo != null) {
                    field = fieldInfo.getField();
                }
            }
        }
        String paramName = mybatisParamHolder.getMybatisParamName(modelType, field, obj.getArg());
        return sqlBuilder.append(paramName);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
