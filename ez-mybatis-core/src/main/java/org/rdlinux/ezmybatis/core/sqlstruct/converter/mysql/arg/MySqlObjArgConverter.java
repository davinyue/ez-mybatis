package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.ObjArg;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;

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
        return sqlBuilder.append(mybatisParamHolder.getMybatisParamName(obj.getArg()));
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
