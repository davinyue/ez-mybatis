package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * WHERE 条件结构。
 */
@Getter
@Setter
public class Where implements SqlStruct {
    /**
     * 条件
     */
    private List<Condition> conditions;

    /**
     * 使用条件列表初始化 WHERE 结构。
     *
     * @param conditions 条件列表
     */
    private Where(List<Condition> conditions) {
        this.conditions = conditions;
    }

    /**
     * 基于已有条件列表构建 WHERE。
     *
     * @param wcb        WHERE 条件构造回调
     * @param conditions 条件列表
     * @return WHERE 结构对象
     */
    public static Where build(Consumer<WhereBuilder> wcb, List<Condition> conditions) {
        WhereBuilder builder = new WhereBuilder(conditions);
        wcb.accept(builder);
        return builder.build();
    }

    /**
     * 新建 WHERE 条件结构。
     *
     * @param wcb WHERE 条件构造回调
     * @return WHERE 结构对象
     */
    public static Where build(Consumer<WhereBuilder> wcb) {
        return build(wcb, new ArrayList<>());
    }

    /**
     * {@link Where} 构造器。
     */
    public static class WhereBuilder extends ConditionBuilder<WhereBuilder> {
        /**
         * 当前构建中的 WHERE 对象。
         */
        private final Where where;

        /**
         * 使用条件列表初始化 WHERE 构造器。
         *
         * @param conditions 条件列表
         */
        private WhereBuilder(List<Condition> conditions) {
            super(conditions);
            this.where = new Where(conditions);
        }

        private Where build() {
            return this.where;
        }
    }
}
