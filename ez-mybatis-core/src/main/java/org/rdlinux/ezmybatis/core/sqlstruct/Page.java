package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.EzQuery;

@Setter
@Getter
public class Page implements SqlStruct {
    private EzQuery<?> query;
    private int skip;
    private int size;

    public Page(EzQuery<?> query, int skip, int size) {
        this.query = query;
        this.skip = skip;
        this.size = size;
    }

    public Page(int skip, int size) {
        this.skip = skip;
        this.size = size;
    }

    public Page() {
        this.skip = 0;
        this.size = 20;
    }
}
