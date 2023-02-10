package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateColumnItem;

public class MySqlUpdateColumnItemConverter extends AbstractConverter<UpdateColumnItem> implements Converter<UpdateColumnItem> {
    private static volatile MySqlUpdateColumnItemConverter instance;

    protected MySqlUpdateColumnItemConverter() {
    }

    public static MySqlUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlUpdateColumnItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       UpdateColumnItem obj, MybatisParamHolder mybatisParamHolder) {
        String column = obj.getColumn();
        String paramName = mybatisParamHolder.getMybatisParamName(obj.getValue());
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(column)
                .append(keywordQM).append(" = ").append(paramName);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
