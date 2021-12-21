package ink.dvc.ezmybatis.core.sqlstruct;

import ink.dvc.ezmybatis.core.EzDelete;
import ink.dvc.ezmybatis.core.EzParam;
import ink.dvc.ezmybatis.core.EzQuery;
import ink.dvc.ezmybatis.core.EzUpdate;
import ink.dvc.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.apache.ibatis.session.Configuration;

public interface SqlStruct {
    /**
     * 转换为sql组成部分
     *
     * @param sqlBuilder         当前sql
     * @param configuration      mybatis配置
     * @param ezParam            构造参数
     * @param mybatisParamHolder mybatis参数持有
     */
    StringBuilder toSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzParam<?> ezParam,
                            MybatisParamHolder mybatisParamHolder);

    /**
     * 转换为查询sql组成部分
     *
     * @param sqlBuilder         当前sql
     * @param configuration      mybatis配置
     * @param ezParam            构造参数
     * @param mybatisParamHolder mybatis参数持有
     */
    default StringBuilder queryToSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzQuery<?> ezParam,
                                         MybatisParamHolder mybatisParamHolder) {
        return this.toSqlPart(sqlBuilder, configuration, ezParam, mybatisParamHolder);
    }

    /**
     * 转换为更新sql组成部分
     *
     * @param sqlBuilder         当前sql
     * @param configuration      mybatis配置
     * @param ezParam            构造参数
     * @param mybatisParamHolder mybatis参数持有
     */
    default StringBuilder updateToSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzUpdate ezParam,
                                          MybatisParamHolder mybatisParamHolder) {
        return this.toSqlPart(sqlBuilder, configuration, ezParam, mybatisParamHolder);
    }

    /**
     * 转换为删除sql组成部分
     *
     * @param sqlBuilder         当前sql
     * @param configuration      mybatis配置
     * @param ezParam            构造参数
     * @param mybatisParamHolder mybatis参数持有
     */
    default StringBuilder deleteToSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzDelete ezParam,
                                          MybatisParamHolder mybatisParamHolder) {
        return this.toSqlPart(sqlBuilder, configuration, ezParam, mybatisParamHolder);
    }
}
