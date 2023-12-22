package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.arg;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.arg.FieldArg;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.Assert;

public class MySqlFieldArgConverter extends AbstractConverter<FieldArg> implements Converter<FieldArg> {
    private static volatile MySqlFieldArgConverter instance;

    protected MySqlFieldArgConverter() {
    }

    public static MySqlFieldArgConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFieldArgConverter.class) {
                if (instance == null) {
                    instance = new MySqlFieldArgConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FieldArg obj, MybatisParamHolder mybatisParamHolder) {
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, obj.getTable().getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(obj.getField());
        Assert.notNull(fieldInfo, "Class " + etInfo.getEntityClass().getName() + "cannot find the filed "
                + obj.getField());
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        return sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM)
                .append(fieldInfo.getColumnName()).append(keywordQM);
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
