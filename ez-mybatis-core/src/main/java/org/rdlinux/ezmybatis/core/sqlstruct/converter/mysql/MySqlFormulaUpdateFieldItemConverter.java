package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.Formula;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.update.FormulaUpdateFieldItem;

public class MySqlFormulaUpdateFieldItemConverter extends AbstractConverter<FormulaUpdateFieldItem>
        implements Converter<FormulaUpdateFieldItem> {
    private static volatile MySqlFormulaUpdateFieldItemConverter instance;

    protected MySqlFormulaUpdateFieldItemConverter() {
    }

    public static MySqlFormulaUpdateFieldItemConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFormulaUpdateFieldItemConverter.class) {
                if (instance == null) {
                    instance = new MySqlFormulaUpdateFieldItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FormulaUpdateFieldItem obj,
                                       MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, ((EntityTable) obj.getTable())
                .getEtType());
        EntityFieldInfo fieldInfo = etInfo.getFieldInfo(obj.getField());
        Formula formula = obj.getFormula();
        sqlBuilder.append(obj.getTable().getAlias()).append(".").append(keywordQM).append(fieldInfo.getColumnName())
                .append(keywordQM).append(" = ");
        Converter<? extends Formula> converter = EzMybatisContent.getConverter(configuration,
                formula.getClass());
        converter.buildSql(type, sqlBuilder, configuration, formula, mybatisParamHolder);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}
