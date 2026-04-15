package org.rdlinux.ezmybatis.core.validation;

import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public final class SqlTableScopeResolver {
    private SqlTableScopeResolver() {
    }

    public static Set<Table> resolve(Table mainTable, List<Join> joins) {
        Set<Table> tables = Collections.newSetFromMap(new IdentityHashMap<Table, Boolean>());
        if (mainTable != null) {
            tables.add(mainTable);
        }
        collectJoinTables(joins, tables);
        return tables;
    }

    private static void collectJoinTables(List<Join> joins, Set<Table> tables) {
        if (joins == null || joins.isEmpty()) {
            return;
        }
        for (Join join : joins) {
            if (join == null) {
                continue;
            }
            if (join.getJoinTable() != null) {
                tables.add(join.getJoinTable());
            }
            collectJoinTables(join.getJoins(), tables);
        }
    }
}
