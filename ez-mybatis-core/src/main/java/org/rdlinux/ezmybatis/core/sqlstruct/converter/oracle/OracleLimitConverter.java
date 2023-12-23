package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.GroupBy;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.AliasGenerate;

public class OracleLimitConverter extends AbstractConverter<Limit> implements Converter<Limit> {
    private static volatile OracleLimitConverter instance;

    protected OracleLimitConverter() {
    }

    public static OracleLimitConverter getInstance() {
        if (instance == null) {
            synchronized (OracleLimitConverter.class) {
                if (instance == null) {
                    instance = new OracleLimitConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration, Limit limit,
                                       MybatisParamHolder mybatisParamHolder) {
        if (limit == null) {
            return sqlBuilder;
        }
        GroupBy groupBy = null;
        OrderBy orderBy = null;
        EzQuery<?> query = limit.getQuery();
        if (query != null) {
            groupBy = query.getGroupBy();
            orderBy = query.getOrderBy();
        }
        EzMybatisConfig ezMybatisConfig = EzMybatisContent.getContentConfig(configuration).getEzMybatisConfig();
        if (ezMybatisConfig.isEnableOracleOffsetFetchPage()) {
            return sqlBuilder.append(" OFFSET ").append(limit.getSkip()).append(" ROWS FETCH NEXT ")
                    .append(limit.getSize()).append(" ROWS ONLY ");
        }
        //不排序, 不分组时
        if ((groupBy == null || groupBy.getItems() == null || groupBy.getItems().isEmpty())
                && (orderBy == null || orderBy.getItems() == null || orderBy.getItems().isEmpty())) {
            //如果查询第一页, 不需要判断rownum > 0的问题
            if (limit.getSkip() == 0) {
                return sqlBuilder;
            }
            //需要判断rownum > 0的问题
            else {
                String bodyAlias = AliasGenerate.getAlias();
                return new StringBuilder("SELECT ").append(bodyAlias).append(".* ")
                        .append(" FROM ( ").append(sqlBuilder).append(" ) ").append(bodyAlias)
                        .append(" WHERE ").append(bodyAlias).append(".\"")
                        .append(EzMybatisConstant.ORACLE_ROW_NUM_ALIAS)
                        .append("\" > ")
                        .append(limit.getSkip());
            }
        }
        //排序和分组时, 需要将原始查询嵌套为子查询后再进行分页操作
        else {
            String bodyAlias = AliasGenerate.getAlias();
            StringBuilder outSqlBuilder = new StringBuilder("SELECT ").append(bodyAlias).append(".*");
            //当不查询第一页是, 才查询出rownum
            if (limit.getSkip() > 0) {
                outSqlBuilder.append(", ROWNUM \"").append(EzMybatisConstant.ORACLE_ROW_NUM_ALIAS).append("\"");
            }
            outSqlBuilder.append(" FROM (").append(sqlBuilder).append(") ").append(bodyAlias)
                    .append(" WHERE ROWNUM <= ").append(limit.getSkip() + limit.getSize()).append(" ");
            String outAlias = AliasGenerate.getAlias();
            String outSqlHead = "";
            String outSqlTail = "";
            if (limit.getSkip() > 0) {
                outSqlHead = "SELECT " + outAlias + ".* FROM ( ";
                outSqlTail = " ) " + outAlias + " WHERE " + outAlias + ".\""
                        + EzMybatisConstant.ORACLE_ROW_NUM_ALIAS + "\" > " + limit.getSkip();
            }
            return new StringBuilder().append(outSqlHead).append(outSqlBuilder).append(outSqlTail);
        }
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
