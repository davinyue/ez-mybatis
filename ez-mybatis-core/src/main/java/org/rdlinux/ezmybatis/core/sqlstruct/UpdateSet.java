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

        public UpdateSetBuilder add(UpdateItem updateItem) {
            this.updateSet.getItems().add(updateItem);
            return this;
        }

        private UpdateSet build() {
            return this.updateSet;
        }
    }

}
