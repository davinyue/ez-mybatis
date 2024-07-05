package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.GroupBy;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy;
import org.rdlinux.ezmybatis.core.sqlstruct.Page;
import org.rdlinux.ezmybatis.core.sqlstruct.Select;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql.MySqlSelectConverter;

public class OracleSelectConverter extends MySqlSelectConverter {
    private static volatile OracleSelectConverter instance;

    protected OracleSelectConverter() {
    }

    public static OracleSelectConverter getInstance() {
        if (instance == null) {
            synchronized (OracleSelectConverter.class) {
                if (instance == null) {
                    instance = new OracleSelectConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Select select,
                                       MybatisParamHolder mybatisParamHolder) {
        if (select == null) {
            return sqlBuilder;
        }
        sqlBuilder = super.doBuildSql(type, sqlBuilder, configuration, select, mybatisParamHolder);
        Page limit = select.getQuery().getPage();
        GroupBy groupBy = select.getQuery().getGroupBy();
        OrderBy orderBy = select.getQuery().getOrderBy();
        EzMybatisConfig ezMybatisConfig = EzMybatisContent.getContentConfig(configuration).getEzMybatisConfig();
        if (!ezMybatisConfig.isEnableOracleOffsetFetchPage() && limit != null
                && (groupBy == null || groupBy.getItems() == null || groupBy.getItems().isEmpty())
                && (orderBy == null || orderBy.getItems() == null || orderBy.getItems().isEmpty())) {
            //如果不是查询第一页, 则需要将rownum查询出来后并取别名, 方便外层查询跳过指定行数
            if (limit.getSkip() != 0) {
                sqlBuilder.append(", ROWNUM \"").append(EzMybatisConstant.ORACLE_ROW_NUM_ALIAS).append("\" ");
            }
        }
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
