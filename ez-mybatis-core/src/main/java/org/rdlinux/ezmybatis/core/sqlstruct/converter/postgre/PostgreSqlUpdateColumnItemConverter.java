package org.rdlinux.ezmybatis.core.sqlstruct.converter.postgre;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityFieldInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateColumnItem;

public class PostgreSqlUpdateColumnItemConverter extends AbstractConverter<UpdateColumnItem> implements Converter<UpdateColumnItem> {
    private static volatile PostgreSqlUpdateColumnItemConverter instance;

    protected PostgreSqlUpdateColumnItemConverter() {
    }

    public static PostgreSqlUpdateColumnItemConverter getInstance() {
        if (instance == null) {
            synchronized (PostgreSqlUpdateColumnItemConverter.class) {
                if (instance == null) {
                    instance = new PostgreSqlUpdateColumnItemConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       UpdateColumnItem obj, MybatisParamHolder mybatisParamHolder) {
        String column = obj.getColumn();
        String paramName = null;
        if (obj.getTable() instanceof EntityTable) {
            EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                    ((EntityTable) obj.getTable()).getEtType());
            EntityFieldInfo entityFieldInfo = entityClassInfo.getColumnMapFieldInfo().get(column);
            if (entityFieldInfo != null) {
                paramName = mybatisParamHolder.getMybatisParamName(entityClassInfo.getEntityClass(),
                        entityFieldInfo.getField(), obj.getValue());
            }
        }
        if (paramName == null) {
            paramName = mybatisParamHolder.getMybatisParamName(obj.getValue());
        }
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        sqlBuilder.append(keywordQM).append(column).append(keywordQM).append(" = ").append(paramName);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.POSTGRE_SQL;
    }
}
