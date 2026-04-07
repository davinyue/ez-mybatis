package org.rdlinux.ezmybatis.core.sqlgenerate.postgre;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.MySqlEntityInfoBuilder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlDialectProvider;
import org.rdlinux.ezmybatis.core.sqlstruct.Limit;
import org.rdlinux.ezmybatis.core.sqlstruct.Page;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlHint;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre.*;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateColumnItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;

/**
 * PostgreSQL方言提供者
 */
public class PostgreDialectProvider extends MySqlDialectProvider {
    public PostgreDialectProvider() {
        this.setEntityInfoBuilder(MySqlEntityInfoBuilder.getInstance());
    }

    @Override
    public DbType getDbType() {
        return DbType.POSTGRE_SQL;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.toLowerCase().contains("postgresql");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return PostgreSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        super.registerConverters();
        this.addConverter(Page.class, PostgreSqlPageConverter.getInstance());
        this.addConverter(UpdateColumnItem.class, PostgreSqlUpdateColumnItemConverter.getInstance());
        this.addConverter(UpdateFieldItem.class, PostgreSqlUpdateFieldItemConverter.getInstance());
        this.addConverter(ArgCompareArgCondition.class, PostgreSqlArgCompareArgConditionConverter.getInstance());
        this.addConverter(Limit.class, PostgreSqlLimitConverter.getInstance());
        this.addConverter(SqlHint.class, PostgreSqlHintConverter.getInstance());
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }
}
