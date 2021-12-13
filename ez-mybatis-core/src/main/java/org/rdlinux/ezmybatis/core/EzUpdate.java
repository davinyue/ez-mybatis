package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.Join;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;

import java.util.List;

@Getter
public class EzUpdate extends EzParam {
    private List<Join> joins;
    private Where where;

    private EzUpdate() {
    }
}
