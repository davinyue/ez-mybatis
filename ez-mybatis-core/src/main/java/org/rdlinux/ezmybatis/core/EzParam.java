package org.rdlinux.ezmybatis.core;

import lombok.Getter;
import org.rdlinux.ezmybatis.core.sqlstruct.From;
import org.rdlinux.ezmybatis.core.sqlstruct.Where;

@Getter
public abstract class EzParam {
    protected From from;
    protected Where where;
}
