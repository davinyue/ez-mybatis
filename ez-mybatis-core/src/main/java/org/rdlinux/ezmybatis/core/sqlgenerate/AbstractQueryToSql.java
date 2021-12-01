package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlpart.*;
import org.rdlinux.ezmybatis.core.sqlpart.condition.*;
import org.rdlinux.ezmybatis.core.sqlpart.selectfield.EzSelectField;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractQueryToSql implements QueryToSql, KeywordQM {
    @Override
    public String toSql(Configuration configuration, EzQuery query, Map<String, Object> mybatisParam) {
        StringBuilder sqlBuilder = new StringBuilder();
        MybatisParamHolder mybatisParamHolder = new MybatisParamHolder(mybatisParam);
        sqlBuilder = this.selectToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.fromToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.joinsToSql(sqlBuilder, configuration, query.getJoins(), mybatisParamHolder);
        sqlBuilder = this.whereToSql(sqlBuilder, configuration, query.getWhere(), mybatisParamHolder);
        sqlBuilder = this.groupByToSql(sqlBuilder, configuration, query.getGroupBy());
        sqlBuilder = this.orderByToSql(sqlBuilder, configuration, query.getOrderBy());
        sqlBuilder = this.whereToSql(sqlBuilder, configuration, query.getHaving(), mybatisParamHolder);
        sqlBuilder = this.limitToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        return sqlBuilder.toString();
    }

    @Override
    public String toCountSql(Configuration configuration, EzQuery query, Map<String, Object> mybatisParam) {
        MybatisParamHolder mybatisParamHolder = new MybatisParamHolder(mybatisParam);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder = this.selectCountToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.fromToSql(sqlBuilder, configuration, query, mybatisParamHolder);
        sqlBuilder = this.joinsToSql(sqlBuilder, configuration, query.getJoins(), mybatisParamHolder);
        sqlBuilder = this.whereToSql(sqlBuilder, configuration, query.getWhere(), mybatisParamHolder);
        sqlBuilder = this.groupByToSql(sqlBuilder, configuration, query.getGroupBy());
        sqlBuilder = this.whereToSql(sqlBuilder, configuration, query.getHaving(), mybatisParamHolder);
        return sqlBuilder.toString();
    }

    protected StringBuilder selectCountToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery query,
                                             MybatisParamHolder mybatisParamHolder) {
        sqlBuilder.append("SELECT COUNT(1) ");
        return sqlBuilder;
    }

    protected StringBuilder selectToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery query,
                                        MybatisParamHolder mybatisParamHolder) {
        EzFrom from = query.getFrom();
        EzSelect select = query.getSelect();
        if (select == null || select.getSelectFields() == null || select.getSelectFields().isEmpty()) {
            sqlBuilder.append("SELECT ").append(from.getTable().getAlias()).append(".* ");
        } else {
            List<EzSelectField> selectFields = select.getSelectFields();
            sqlBuilder.append("SELECT ");
            for (int i = 0; i < selectFields.size(); i++) {
                sqlBuilder.append(selectFields.get(i).toSqlPart(configuration));
                if (i + 1 < selectFields.size()) {
                    sqlBuilder.append(", ");
                }
            }
        }
        return sqlBuilder;
    }

    protected StringBuilder fromToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery query,
                                      MybatisParamHolder mybatisParamHolder) {
        EzFrom from = query.getFrom();
        EzTable fromTable = from.getTable();
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, fromTable.getEtType());
        sqlBuilder.append(" FROM ").append(this.getKeywordQM()).append(entityClassInfo.getTableName())
                .append(this.getKeywordQM()).append(" ").append(from.getTable().getAlias());
        return sqlBuilder;
    }

    protected abstract StringBuilder limitToSql(StringBuilder sqlBuilder, Configuration configuration, EzQuery query,
                                                MybatisParamHolder mybatisParamHolder);

    protected StringBuilder orderByToSql(StringBuilder sqlBuilder, Configuration configuration, EzOrder order) {
        if (order == null || order.getItems() == null) {
            return sqlBuilder;
        } else {
            StringBuilder sql = new StringBuilder(" ORDER BY ");
            for (int i = 0; i < order.getItems().size(); i++) {
                EzOrder.OrderItem orderItem = order.getItems().get(i);
                EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                        orderItem.getTable().getEtType());
                sql.append(orderItem.getTable().getAlias()).append(".")
                        .append(this.getKeywordQM())
                        .append(entityClassInfo.getFieldInfo(orderItem.getField()).getColumnName())
                        .append(this.getKeywordQM())
                        .append(" ").append(orderItem.getType().name());
                if (i + 1 < order.getItems().size()) {
                    sql.append(", ");
                } else {
                    sql.append(" ");
                }
            }
            return sqlBuilder.append(sql);
        }
    }

    protected StringBuilder groupByToSql(StringBuilder sqlBuilder, Configuration configuration, EzGroup group) {
        if (group == null || group.getItems() == null) {
            return sqlBuilder;
        } else {
            StringBuilder sql = new StringBuilder(" GROUP BY ");
            for (int i = 0; i < group.getItems().size(); i++) {
                EzGroup.GroupItem groupItem = group.getItems().get(i);
                EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                        groupItem.getTable().getEtType());
                sql.append(groupItem.getTable().getAlias()).append(".")
                        .append(entityClassInfo.getFieldInfo(groupItem.getField()).getColumnName());
                if (i + 1 < group.getItems().size()) {
                    sql.append(", ");
                } else {
                    sql.append(" ");
                }
            }
            return sqlBuilder.append(sql);
        }
    }

    protected StringBuilder whereToSql(StringBuilder sqlBuilder, Configuration configuration, EzWhere where,
                                       MybatisParamHolder mybatisParamHolder) {
        if (where == null) {
            return sqlBuilder;
        }
        sqlBuilder.append(" WHERE ").append(this.conditionsToSql(sqlBuilder, configuration, where.getConditions(),
                mybatisParamHolder));
        return sqlBuilder;
    }

    protected StringBuilder joinsToSql(StringBuilder sqlBuilder, Configuration configuration, List<EzJoin> joins,
                                       MybatisParamHolder mybatisParamHolder) {
        StringBuilder sql = new StringBuilder();
        if (joins != null) {
            for (EzJoin join : joins) {
                sql.append(this.joinToSql(sqlBuilder, configuration, join, mybatisParamHolder));
            }
        }
        return sqlBuilder.append(sql);
    }

    protected StringBuilder joinToSql(StringBuilder sqlBuilder, Configuration configuration, EzJoin join,
                                      MybatisParamHolder mybatisParamHolder) {
        if (join == null) {
            return new StringBuilder();
        }
        EzTable joinTable = join.getJoinTable();
        EntityClassInfo jEtInfo = EzEntityClassInfoFactory.forClass(configuration, joinTable.getEtType());
        EzJoin.JoinType joinType = join.getJoinType();
        StringBuilder sonSql;
        if (joinType == EzJoin.JoinType.CrossJoin) {
            sonSql = new StringBuilder();
        } else {
            sonSql = this.conditionsToSql(sqlBuilder, configuration, join.getOnConditions(), mybatisParamHolder);
            if (sonSql == null || sonSql.length() == 0) {
                return new StringBuilder();
            }
        }
        StringBuilder sql = new StringBuilder();
        if (joinType == EzJoin.JoinType.InnerJoin) {
            sql.append(" INNER JOIN ").append(this.getKeywordQM()).append(jEtInfo.getTableName())
                    .append(this.getKeywordQM()).append(" ").append(joinTable.getAlias()).append(" ON ");
        } else if (joinType == EzJoin.JoinType.LeftJoin) {
            sql.append(" LEFT JOIN ").append(this.getKeywordQM()).append(jEtInfo.getTableName())
                    .append(this.getKeywordQM()).append(" ").append(joinTable.getAlias()).append(" ON ");
        } else if (joinType == EzJoin.JoinType.RightJoin) {
            sql.append(" RIGHT JOIN ").append(this.getKeywordQM()).append(jEtInfo.getTableName())
                    .append(this.getKeywordQM()).append(" ").append(joinTable.getAlias()).append(" ON ");
        } else if (joinType == EzJoin.JoinType.FullJoin) {
            sql.append(" FULL JOIN ").append(this.getKeywordQM()).append(jEtInfo.getTableName())
                    .append(this.getKeywordQM()).append(" ").append(joinTable.getAlias()).append(" ON ");
        } else if (joinType == EzJoin.JoinType.CrossJoin) {
            sql.append(", ").append(this.getKeywordQM()).append(jEtInfo.getTableName())
                    .append(this.getKeywordQM()).append(" ").append(joinTable.getAlias());
        }
        sql.append(sonSql);
        return sql;
    }

    protected StringBuilder conditionsToSql(StringBuilder sqlBuilder, Configuration configuration,
                                            List<EzCondition> conditions, MybatisParamHolder mybatisParamHolder) {
        if (conditions == null) {
            return sqlBuilder;
        }
        StringBuilder sql = new StringBuilder();
        for (int i = 0; i < conditions.size(); i++) {
            EzCondition condition = conditions.get(i);
            if (condition == null) {
                continue;
            }
            if (i != 0) {
                sql.append(condition.getLoginSymbol().name()).append(" ");
            }
            sql.append(this.conditionToSql(sqlBuilder, configuration, condition, mybatisParamHolder));
        }
        return sql;
    }

    protected StringBuilder conditionToSql(StringBuilder sqlBuilder, Configuration configuration, EzCondition condition,
                                           MybatisParamHolder mybatisParamHolder) {
        if (condition == null) {
            return new StringBuilder();
        }
        StringBuilder sql = new StringBuilder();
        if (condition instanceof EzGroupCondition) {
            StringBuilder sonSql = this.conditionsToSql(sqlBuilder, configuration, ((EzGroupCondition) condition)
                    .getConditions(), mybatisParamHolder);
            if (sonSql != null && !(sonSql.length() == 0)) {
                sql.append("( ").append(sonSql).append(" )");
            }
        } else if (condition instanceof EzNormalCondition) {
            EzNormalCondition oCondition = (EzNormalCondition) condition;
            String param = mybatisParamHolder.getParamName(oCondition.getValue());
            EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration,
                    oCondition.getTable().getEtType());
            sql.append(" ").append(oCondition.getTable().getAlias()).append(".")
                    .append(this.getKeywordQM())
                    .append(etInfo.getFieldInfo(oCondition.getField()).getColumnName())
                    .append(this.getKeywordQM())
                    .append(" ")
                    .append(oCondition.getOperator().getOperator())
                    .append(" ");
            if (oCondition.getOperator() == Operator.in) {
                sql.append("( ");
                if (oCondition.getValue() instanceof Collection) {
                    int i = 0;
                    for (Object value : (Collection<?>) oCondition.getValue()) {
                        sql.append(MybatisParamEscape.getEscapeChar(value)).append("{").append(param)
                                .append("[").append(i).append("]").append("}");
                        if (i + 1 < ((Collection<?>) oCondition.getValue()).size()) {
                            sql.append(", ");
                        }
                        i++;
                    }
                } else if (oCondition.getValue().getClass().isArray()) {
                    int i = 0;
                    for (Object value : (Object[]) oCondition.getValue()) {
                        sql.append(MybatisParamEscape.getEscapeChar(value)).append("{").append(param)
                                .append("[").append(i).append("]").append("}");
                        if (i + 1 < ((Object[]) oCondition.getValue()).length) {
                            sql.append(", ");
                        }
                        i++;
                    }
                } else {
                    sql.append(MybatisParamEscape.getEscapeChar(oCondition.getValue())).append("{").append(param)
                            .append("}").append(" ");
                }
                sql.append(" ) ");
            } else {
                sql.append(MybatisParamEscape.getEscapeChar(oCondition.getValue())).append("{").append(param)
                        .append("}").append(" ");
            }
        } else if (condition instanceof EzBetweenCondition) {
            EzBetweenCondition oCondition = (EzBetweenCondition) condition;
            String minParam = mybatisParamHolder.getParamName(oCondition.getMinValue());
            String maxParam = mybatisParamHolder.getParamName(oCondition.getMaxValue());
            EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration,
                    oCondition.getTable().getEtType());
            sql.append(" ").append(oCondition.getTable().getAlias()).append(".")
                    .append(this.getKeywordQM())
                    .append(etInfo.getFieldInfo(oCondition.getField()).getColumnName())
                    .append(this.getKeywordQM())
                    .append(" ")
                    .append(oCondition.getOperator().getOperator())
                    .append(" ")
                    .append(MybatisParamEscape.getEscapeChar(oCondition.getMinValue())).append("{").append(minParam)
                    .append("}").append(" AND ")
                    .append(MybatisParamEscape.getEscapeChar(oCondition.getMaxValue())).append("{").append(maxParam)
                    .append("}").append(" ");
        } else if (condition instanceof EzIsNullCondition) {
            EzIsNullCondition oCondition = (EzIsNullCondition) condition;
            EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration,
                    oCondition.getTable().getEtType());
            sql.append(" ").append(oCondition.getTable().getAlias()).append(".")
                    .append(this.getKeywordQM())
                    .append(etInfo.getFieldInfo(oCondition.getField()).getColumnName())
                    .append(this.getKeywordQM())
                    .append(" ")
                    .append(oCondition.getOperator().getOperator()).append(" ");
        } else if (condition instanceof EzTableCondition) {
            EzTableCondition oCondition = (EzTableCondition) condition;
            EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, oCondition.getTable()
                    .getEtType());
            EntityClassInfo oEtInfo = EzEntityClassInfoFactory.forClass(configuration,
                    oCondition.getOtherTable().getEtType());
            sql.append(" ").append(oCondition.getTable().getAlias()).append(".")
                    .append(this.getKeywordQM())
                    .append(etInfo.getFieldInfo(oCondition.getField()).getColumnName())
                    .append(this.getKeywordQM())
                    .append(" ")
                    .append(oCondition.getOperator().getOperator())
                    .append(" ")
                    .append(oCondition.getOtherTable().getAlias()).append(".")
                    .append(this.getKeywordQM())
                    .append(oEtInfo.getFieldInfo(oCondition.getField()).getColumnName())
                    .append(this.getKeywordQM())
                    .append(" ");
        }
        return sql;
    }
}
