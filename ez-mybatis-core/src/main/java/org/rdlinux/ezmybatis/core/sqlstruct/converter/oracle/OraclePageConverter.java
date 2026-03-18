package org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.EzMybatisConfig;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.constant.EzMybatisConstant;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.GroupBy;
import org.rdlinux.ezmybatis.core.sqlstruct.OrderBy;
import org.rdlinux.ezmybatis.core.sqlstruct.Page;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.utils.AliasGenerate;

public class OraclePageConverter extends AbstractConverter<Page> implements Converter<Page> {
    private static volatile OraclePageConverter instance;

    protected OraclePageConverter() {
    }

    public static OraclePageConverter getInstance() {
        if (instance == null) {
            synchronized (OraclePageConverter.class) {
                if (instance == null) {
                    instance = new OraclePageConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected void doBuildSql(Type type, Page page, SqlGenerateContext sqlGenerateContext) {
        if (page == null) {
            return;
        }
        GroupBy groupBy = null;
        OrderBy orderBy = null;
        EzQuery<?> query = page.getQuery();
        if (query != null) {
            groupBy = query.getGroupBy();
            orderBy = query.getOrderBy();
        }
        Configuration configuration = sqlGenerateContext.getConfiguration();
        EzMybatisConfig ezMybatisConfig = EzMybatisContent.getContentConfig(configuration).getEzMybatisConfig();
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        if (ezMybatisConfig.isEnableOracleOffsetFetchPage()) {
            sqlBuilder.append(" OFFSET ").append(page.getSkip()).append(" ROWS FETCH NEXT ")
                    .append(page.getSize()).append(" ROWS ONLY ");
            return;
        }
        //不排序, 不分组时
        if ((groupBy == null || groupBy.getItems() == null || groupBy.getItems().isEmpty())
                && (orderBy == null || orderBy.getItems() == null || orderBy.getItems().isEmpty())) {
            //如果查询第一页, 不需要判断rownum > 0的问题
            if (page.getSkip() == 0) {
                return;
            }
            //需要判断rownum > 0的问题
            else {
                String bodyAlias = AliasGenerate.getAlias();
                sqlBuilder.insert(0, "SELECT ").append(bodyAlias).append(".*  FROM ( ")
                        .append(" ) ").append(bodyAlias)
                        .append(" WHERE ").append(bodyAlias).append(".\"")
                        .append(EzMybatisConstant.ORACLE_ROW_NUM_ALIAS)
                        .append("\" > ")
                        .append(page.getSkip());
            }
        }
        //排序和分组时, 需要将原始查询嵌套为子查询后再进行分页操作
        else {
            String bodyAlias = AliasGenerate.getAlias();
            StringBuilder outSqlBuilder = new StringBuilder("SELECT ").append(bodyAlias).append(".*");
            //当不查询第一页是, 才查询出rownum
            if (page.getSkip() > 0) {
                outSqlBuilder.append(", ROWNUM \"").append(EzMybatisConstant.ORACLE_ROW_NUM_ALIAS).append("\"");
            }
            outSqlBuilder.append(" FROM (").append(sqlBuilder).append(") ").append(bodyAlias)
                    .append(" WHERE ROWNUM <= ").append(page.getSkip() + page.getSize()).append(" ");
            String outAlias = AliasGenerate.getAlias();
            String outSqlHead = "";
            String outSqlTail = "";
            if (page.getSkip() > 0) {
                outSqlHead = "SELECT " + outAlias + ".* FROM ( ";
                outSqlTail = " ) " + outAlias + " WHERE " + outAlias + ".\""
                        + EzMybatisConstant.ORACLE_ROW_NUM_ALIAS + "\" > " + page.getSkip();
            }
            sqlGenerateContext.getSqlBuilder().setLength(0);
            sqlGenerateContext.getSqlBuilder().append(outSqlHead).append(outSqlBuilder).append(outSqlTail);
        }
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.ORACLE;
    }
}
