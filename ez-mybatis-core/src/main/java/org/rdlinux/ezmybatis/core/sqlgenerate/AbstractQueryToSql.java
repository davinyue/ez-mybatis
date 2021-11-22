package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.content.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.content.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlpart.*;
import org.rdlinux.ezmybatis.core.sqlpart.condition.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractQueryToSql implements QueryToSql, KeywordQM {
    @Override
    public String toSql(Configuration configuration, EzQuery query, Map<String, Object> mybatisParam) {
        MybatisParamHolder mybatisParamHolder = new MybatisParamHolder(mybatisParam);
        EzFrom from = query.getFrom();
        EzTable fromTable = from.getTable();
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, fromTable.getEtType());
        StringBuilder sql = new StringBuilder("SELECT ").append(from.getTable().getAlias()).append(".* ")
                .append(" FROM ").append(entityClassInfo.getTableName()).append(" ").append(from.getTable().getAlias())
                .append(this.joinsToSql(configuration, query.getJoins(), mybatisParamHolder))
                .append(this.whereToSql(configuration, query.getWhere(), mybatisParamHolder))
                .append(this.groupByToSql(configuration, query.getGroupBy()))
                .append(this.orderByToSql(configuration, query.getOrderBy()))
                .append(this.whereToSql(configuration, query.getHaving(), mybatisParamHolder));
        if (query.getLimit() != null) {
            sql = this.limitToSql(sql, query.getLimit());
        }
        return sql.toString();
    }

    @Override
    public String toCountSql(Configuration configuration, EzQuery query, Map<String, Object> mybatisParam) {
        MybatisParamHolder mybatisParamHolder = new MybatisParamHolder(mybatisParam);
        EzFrom from = query.getFrom();
        EzTable fromTable = from.getTable();
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration, fromTable.getEtType());
        return "SELECT COUNT(" + from.getTable().getAlias() + ".*) " +
                " FROM " + entityClassInfo.getTableName() + " " + from.getTable().getAlias() +
                this.joinsToSql(configuration, query.getJoins(), mybatisParamHolder) +
                this.whereToSql(configuration, query.getWhere(), mybatisParamHolder) +
                this.groupByToSql(configuration, query.getGroupBy()) +
                this.whereToSql(configuration, query.getHaving(), mybatisParamHolder);
    }

    protected StringBuilder limitToSql(StringBuilder sql, EzLimit limit) {
        return new StringBuilder();
    }

    protected String orderByToSql(Configuration configuration, EzOrder order) {
        if (order == null || order.getItems() == null) {
            return "";
        } else {
            StringBuilder sql = new StringBuilder(" GROUP BY ");
            for (int i = 0; i < order.getItems().size(); i++) {
                EzOrder.OrderItem orderItem = order.getItems().get(i);
                EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                        orderItem.getTable().getEtType());
                sql.append(orderItem.getTable().getAlias()).append(".")
                        .append(entityClassInfo.getFieldInfo(orderItem.getField()).getColumnName())
                        .append(" ").append(orderItem.getType().name());
                if (i + 1 < order.getItems().size()) {
                    sql.append(", ");
                } else {
                    sql.append(" ");
                }
            }
            return sql.toString();
        }
    }

    protected String groupByToSql(Configuration configuration, EzGroup group) {
        if (group == null || group.getItems() == null) {
            return "";
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
            return sql.toString();
        }
    }

    protected String whereToSql(Configuration configuration, EzWhere where, MybatisParamHolder mybatisParamHolder) {
        if (where == null) {
            return "";
        }
        return this.conditionsToSql(configuration, where.getConditions(), mybatisParamHolder);
    }

    protected String joinsToSql(Configuration configuration, List<EzJoin> joins,
                                MybatisParamHolder mybatisParamHolder) {
        StringBuilder sql = new StringBuilder();
        if (joins != null) {
            for (EzJoin join : joins) {
                sql.append(this.joinToSql(configuration, join, mybatisParamHolder));
            }
        }
        return sql.toString();
    }

    protected String joinToSql(Configuration configuration, EzJoin join, MybatisParamHolder mybatisParamHolder) {
        if (join == null) {
            return "";
        }
        EzTable joinTable = join.getJoinTable();
        EntityClassInfo jEtInfo = EzEntityClassInfoFactory.forClass(configuration, joinTable.getEtType());
        EzJoin.JoinType joinType = join.getJoinType();
        String sonSql;
        if (joinType == EzJoin.JoinType.CrossJoin) {
            sonSql = "";
        } else {
            sonSql = this.conditionsToSql(configuration, join.getOnConditions(), mybatisParamHolder);
            if (sonSql == null || sonSql.isEmpty()) {
                return "";
            }
        }
        StringBuilder sql = new StringBuilder();
        if (joinType == EzJoin.JoinType.InnerJoin) {
            sql.append(" INNER JOIN ").append(jEtInfo.getTableName()).append(" ").append(joinTable.getAlias())
                    .append(" ON ");
        } else if (joinType == EzJoin.JoinType.LeftJoin) {
            sql.append(" LEFT JOIN ").append(jEtInfo.getTableName()).append(" ").append(joinTable.getAlias())
                    .append(" ON ");
        } else if (joinType == EzJoin.JoinType.RightJoin) {
            sql.append(" RIGHT JOIN ").append(jEtInfo.getTableName()).append(" ").append(joinTable.getAlias())
                    .append(" ON ");
        } else if (joinType == EzJoin.JoinType.FullJoin) {
            sql.append(" FULL JOIN ").append(jEtInfo.getTableName()).append(" ").append(joinTable.getAlias())
                    .append(" ON ");
        } else if (joinType == EzJoin.JoinType.CrossJoin) {
            sql.append(", ").append(jEtInfo.getTableName()).append(" ").append(joinTable.getAlias());
        }
        sql.append(sonSql);
        return sql.toString();
    }

    protected String conditionsToSql(Configuration configuration, List<EzCondition> conditions,
                                     MybatisParamHolder mybatisParamHolder) {
        if (conditions == null) {
            return "";
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
            sql.append(this.conditionToSql(configuration, condition, mybatisParamHolder));
        }
        return sql.toString();
    }

    protected String conditionToSql(Configuration configuration, EzCondition condition,
                                    MybatisParamHolder mybatisParamHolder) {
        if (condition == null) {
            return "";
        }
        StringBuilder sql = new StringBuilder();
        if (condition instanceof EzGroupCondition) {
            String sonSql = this.conditionsToSql(configuration, ((EzGroupCondition) condition)
                    .getConditions(), mybatisParamHolder);
            if (sonSql != null && !sonSql.isEmpty()) {
                sql.append("( ").append(sonSql).append(" )");
            }
        } else if (condition instanceof EzNormalCondition) {
            EzNormalCondition oCondition = (EzNormalCondition) condition;
            String param = mybatisParamHolder.getParamName(oCondition.getValue());
            EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration,
                    oCondition.getTable().getEtType());
            sql.append(" ").append(oCondition.getTable().getAlias()).append(".")
                    .append(etInfo.getFieldInfo(oCondition.getField()).getColumnName())
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
                    .append(etInfo.getFieldInfo(oCondition.getField()).getColumnName())
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
                    .append(etInfo.getFieldInfo(oCondition.getField()).getColumnName())
                    .append(" ")
                    .append(oCondition.getOperator().getOperator()).append(" ");
        } else if (condition instanceof EzTableCondition) {
            EzTableCondition oCondition = (EzTableCondition) condition;
            EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration,
                    oCondition.getTable().getEtType());
            EntityClassInfo oEtInfo = EzEntityClassInfoFactory.forClass(configuration,
                    oCondition.getOtherTable().getEtType());
            sql.append(" ").append(oCondition.getTable().getAlias()).append(".")
                    .append(etInfo.getFieldInfo(oCondition.getField()).getColumnName())
                    .append(" ")
                    .append(oCondition.getOperator().getOperator())
                    .append(" ")
                    .append(oCondition.getOtherTable().getAlias()).append(".")
                    .append(oEtInfo.getFieldInfo(oCondition.getField()).getColumnName())
                    .append(" ");
        }
        return sql.toString();
    }
}
