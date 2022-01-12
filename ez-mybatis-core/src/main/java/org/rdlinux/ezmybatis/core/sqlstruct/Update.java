package org.rdlinux.ezmybatis.core.sqlstruct;

import org.rdlinux.ezmybatis.core.sqlstruct.update.UpdateItem;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Update {
    private List<UpdateItem> items;

    public Update() {
        this.items = new LinkedList<>();
    }
}
