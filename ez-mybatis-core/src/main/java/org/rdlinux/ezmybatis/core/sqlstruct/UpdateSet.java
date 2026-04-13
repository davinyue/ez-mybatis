package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class UpdateSet {
    private final List<UpdateItem> items;

    private UpdateSet(List<UpdateItem> updateItems) {
        this.items = updateItems;
    }

    public static UpdateSet build(Consumer<UpdateSetBuilder> usc) {
        return build(usc, new ArrayList<>());
    }

    public static UpdateSet build(Consumer<UpdateSetBuilder> usc, List<UpdateItem> updateItems) {
        UpdateSetBuilder builder = new UpdateSetBuilder(updateItems);
        usc.accept(builder);
        return builder.build();
    }

    /**
     * update set builder
     */
    public static class UpdateSetBuilder {
        private final UpdateSet updateSet;

        private UpdateSetBuilder(List<UpdateItem> updateItems) {
            if (updateItems == null) {
                updateItems = new ArrayList<>();
            }
            this.updateSet = new UpdateSet(updateItems);
        }

        public UpdateSetBuilder add(boolean sure, UpdateItem updateItem) {
            if (sure) {
                this.updateSet.getItems().add(updateItem);
            }
            return this;
        }

        /**
         * 根据条件延迟构造并添加更新项。
         *
         * <p>适用于更新项构造依赖于 {@code sure} 判定参数的场景。只有在 {@code sure=true}
         * 时才会执行回调，避免在 {@code sure=false} 时提前触发诸如 {@code table.field(a)}
         * 之类的参数校验或异常。</p>
         *
         * @param sure 是否添加
         * @param cb   更新项构造回调
         * @return 当前构造器
         */
        public UpdateSetBuilder add(boolean sure, Consumer<UpdateSetBuilder> cb) {
            if (sure) {
                cb.accept(this);
            }
            return this;
        }

        public UpdateSetBuilder add(UpdateItem updateItem) {
            this.updateSet.getItems().add(updateItem);
            return this;
        }

        private UpdateSet build() {
            return this.updateSet;
        }
    }

}
