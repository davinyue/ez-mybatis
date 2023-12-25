package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.QueryRetOperand;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectOperand;

public class MySqlSelectOperandConverter extends AbstractConverter<SelectOperand> implements Converter<SelectOperand> {
    private static volatile MySqlSelectOperandConverter instance;

    protected MySqlSelectOperandConverter() {
    }

    public static MySqlSelectOperandConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlSelectOperandConverter.class) {
                if (instance == null) {
                    instance = new MySqlSelectOperandConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration
            , SelectOperand obj, MybatisParamHolder mybatisParamHolder) {
        Converter<? extends QueryRetOperand> converter = EzMybatisContent.getConverter(configuration,
                obj.getOperand().getClass());
        converter.buildSql(type, sqlBuilder, configuration, obj.getOperand(), mybatisParamHolder);
        String alias = obj.getAlias();
        if (alias != null && !alias.isEmpty()) {
            String keywordQM = EzMybatisContent.getKeywordQM(configuration);
            sqlBuilder.append(" ").append(keywordQM).append(alias).append(keywordQM).append(" ");
        }
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
