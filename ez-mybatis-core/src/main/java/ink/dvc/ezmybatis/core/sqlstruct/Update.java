package ink.dvc.ezmybatis.core.sqlstruct;

import ink.dvc.ezmybatis.core.sqlstruct.update.UpdateItem;
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
