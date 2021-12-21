package ink.dvc.ezmybatis.core.sqlstruct;

import lombok.Getter;
import ink.dvc.ezmybatis.core.sqlstruct.update.UpdateItem;

import java.util.LinkedList;
import java.util.List;

@Getter
public class UpdateSet {
    private List<UpdateItem> items;

    public UpdateSet() {
        this.items = new LinkedList<>();
    }
}
