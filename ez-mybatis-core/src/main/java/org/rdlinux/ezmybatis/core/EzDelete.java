package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;

import java.util.List;

@Getter
public class EzDelete extends EzParam<Integer> {
    private List<Join> joins;
    private Where where;

    private EzDelete() {
        super(Integer.class);
    }
}
