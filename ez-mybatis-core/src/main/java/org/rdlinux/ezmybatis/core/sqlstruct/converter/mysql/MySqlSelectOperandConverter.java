package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateContext;
import org.rdlinux.ezmybatis.core.sqlstruct.QueryRetOperand;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.selectitem.SelectOperand;
import org.rdlinux.ezmybatis.utils.SqlEscaping;

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
    protected void doBuildSql(Type type, SelectOperand obj, SqlGenerateContext sqlGenerateContext) {
        Configuration configuration = sqlGenerateContext.getConfiguration();
        StringBuilder sqlBuilder = sqlGenerateContext.getSqlBuilder();
        Converter<? extends QueryRetOperand> converter = EzMybatisContent.getConverter(configuration,
                obj.getOperand().getClass());
        converter.buildSql(type, obj.getOperand(), sqlGenerateContext);
        String alias = obj.getAlias();
        if (alias != null && !alias.isEmpty()) {
            //这里必须使用方言提供者来获取关键词引号, 因为EzMybatisContent来获取的话，可能会返回空字符串，会导致别名丢失关键词引号，
            //在某些数据库中，比如oracle，指定别名是驼峰格式，会被转换成全大写，最终导致无法和结果集实体对应
            String keywordQM = EzMybatisContent.getDbDialectProvider(configuration).getKeywordQuoteMark();
            sqlBuilder.append(" ").append(keywordQM).append(SqlEscaping.nameEscaping(alias)).append(keywordQM)
                    .append(" ");
        }
    }

}
