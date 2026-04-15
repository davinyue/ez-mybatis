package org.rdlinux.ezmybatis.core.sqlgenerate.mssql;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.MySqlEntityInfoBuilder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlDialectProvider;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.mssql.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.SqlTable;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateColumnItem;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateFieldItem;

/**
 * SQL Server方言提供者
 */
public class SqlServerDialectProvider extends MySqlDialectProvider {

    public SqlServerDialectProvider() {
        this.setEntityInfoBuilder(MySqlEntityInfoBuilder.getInstance());
    }

    @Override
    public DbType getDbType() {
        return DbType.SQL_SERVER;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.toLowerCase().contains("sqlserver");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return SqlServerSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        super.registerConverters();
        this.addConverter(OrderBy.class, SqlServerOrderByConverter.getInstance());
        this.addConverter(Page.class, SqlServerPageConverter.getInstance());
        this.addConverter(DbTable.class, SqlServerDbTableConverter.getInstance());
        this.addConverter(EntityTable.class, SqlServerEntityTableConverter.getInstance());
        this.addConverter(SqlTable.class, SqlServerSqlTableConverter.getInstance());
        this.addConverter(UpdateColumnItem.class, SqlServerUpdateColumnItemConverter.getInstance());
        this.addConverter(UpdateFieldItem.class, SqlServerUpdateFieldItemConverter.getInstance());
        this.addConverter(ArgCompareArgCondition.class, SqlServerArgCompareArgConditionConverter.getInstance());
        this.addConverter(TableColumn.class, SqlServerTableColumnConverter.getInstance());
        this.addConverter(EntityField.class, SqlServerEntityFieldConverter.getInstance());
        this.addConverter(Limit.class, SqlServerLimitConverter.getInstance());
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }
}
