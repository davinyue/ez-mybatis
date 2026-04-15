package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * HAVING 条件结构。
 */
@Getter
public class Having implements SqlStruct {
    /**
     * 条件
     */
    private final List<Condition> conditions;

    /**
     * 使用条件列表初始化 HAVING 结构。
     *
     * @param conditions 条件列表
     */
    private Having(List<Condition> conditions) {
        this.conditions = conditions;
    }

    /**
     * 基于已有条件列表构建 HAVING。
     *
     * @param hc         HAVING 条件构造回调
     * @param conditions 条件列表
     * @return HAVING 结构对象
     */
    public static Having build(Consumer<Having.HavingBuilder> hc, List<Condition> conditions) {
        Having.HavingBuilder builder = new Having.HavingBuilder(conditions);
        hc.accept(builder);
        return builder.build();
    }

    /**
     * 新建 HAVING 条件结构。
     *
     * @param hc HAVING 条件构造回调
     * @return HAVING 结构对象
     */
    public static Having build(Consumer<Having.HavingBuilder> hc) {
        Having.HavingBuilder builder = new Having.HavingBuilder(new ArrayList<>());
        hc.accept(builder);
        return builder.build();
    }


    /**
     * {@link Having} 构造器。
     */
    public static class HavingBuilder extends ConditionBuilder<HavingBuilder> {
        /**
         * 当前构建中的 HAVING 对象。
         */
        private final Having having;

        /**
         * 使用条件列表初始化 HAVING 构造器。
         *
         * @param conditions 条件列表
         */
        private HavingBuilder(List<Condition> conditions) {
            super(conditions);
            this.having = new Having(conditions);
        }

        private Having build() {
            return this.having;
        }
    }
}
