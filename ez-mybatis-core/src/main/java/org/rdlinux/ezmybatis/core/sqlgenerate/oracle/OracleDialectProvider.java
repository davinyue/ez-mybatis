package org.rdlinux.ezmybatis.core.sqlgenerate.oracle;

import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.build.OracleEntityInfoBuilder;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerate;
import org.rdlinux.ezmybatis.core.sqlgenerate.mysql.MySqlDialectProvider;
import org.rdlinux.ezmybatis.core.sqlstruct.*;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ArgCompareArgCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.oracle.*;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.NormalPartition;
import org.rdlinux.ezmybatis.core.sqlstruct.table.partition.SubPartition;

/**
 * Oracle方言提供者
 */
public class OracleDialectProvider extends MySqlDialectProvider {
    public OracleDialectProvider() {
        this.setEntityInfoBuilder(OracleEntityInfoBuilder.getInstance());
    }

    @Override
    public DbType getDbType() {
        return DbType.ORACLE;
    }

    @Override
    public boolean matchDriver(String driverClassName) {
        return driverClassName != null && driverClassName.contains("oracle");
    }

    @Override
    public SqlGenerate getSqlGenerate() {
        return OracleSqlGenerate.getInstance();
    }

    @Override
    public void registerConverters() {
        super.registerConverters();

        this.addConverter(Join.class, new OracleJoinConverter());
        this.addConverter(Select.class, OracleSelectConverter.getInstance());
        this.addConverter(Page.class, OraclePageConverter.getInstance());
        this.addConverter(NormalPartition.class, OracleNormalPartitionConverter.getInstance());
        this.addConverter(SubPartition.class, OracleSubPartitionConverter.getInstance());
        this.addConverter(ArgCompareArgCondition.class, OracleArgCompareArgConditionConverter.getInstance());
        this.addConverter(EzQuery.class, OracleEzQueryConverter.getInstance());
        this.addConverter(Function.class, OracleFunctionConverter.getInstance());
        this.addConverter(Limit.class, OracleLimitConverter.getInstance());
    }

    @Override
    public String getKeywordQuoteMark() {
        return "\"";
    }
}
