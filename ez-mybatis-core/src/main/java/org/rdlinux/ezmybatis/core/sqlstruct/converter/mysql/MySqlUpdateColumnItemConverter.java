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
    protected StringBuilder dobuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       UpdateColumnItem obj, MybatisParamHolder mybatisParamHolder) {
        String paramName = mybatisParamHolder.getParamName(obj.getValue(), true);
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        String column = obj.getColumn();
        sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(column)
                .append(keywordQM).append(" = ").append(paramName);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
