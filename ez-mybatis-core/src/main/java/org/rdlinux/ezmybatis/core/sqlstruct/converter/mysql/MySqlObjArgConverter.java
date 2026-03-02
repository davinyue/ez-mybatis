package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.EntityField;
import org.rdlinux.ezmybatis.core.sqlstruct.ObjArg;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

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
    protected void doBuildSql(Type type, ObjArg obj, SqlGenerateContext sqlGenerateContext) {
        Class<?> modelType = null;
        Field field = null;
        EntityField currentAccessField = EzMybatisContent.getCurrentAccessField();
        if (currentAccessField != null) {
            modelType = currentAccessField.getTable().getEtType();
            EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(sqlGenerateContext.getConfiguration(),
                    currentAccessField.getTable()
                            .getEtType());
            if (etInfo != null) {
                EntityFieldInfo fieldInfo = etInfo.getFieldInfo(currentAccessField.getField());
                if (fieldInfo != null) {
                    field = fieldInfo.getField();
                }
            }
        }
        String paramName = sqlGenerateContext.getMybatisParamHolder().getMybatisParamName(modelType, field,
                obj.getArg());
        sqlGenerateContext.getSqlBuilder().append(paramName);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
