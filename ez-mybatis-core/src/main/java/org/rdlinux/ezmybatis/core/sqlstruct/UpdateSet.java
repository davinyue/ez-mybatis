package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;

import java.util.LinkedList;
import java.util.List;

@Getter
public class UpdateSet {
    private List<UpdateItem> items;

    public UpdateSet() {
        this.items = new LinkedList<>();
    }
}
