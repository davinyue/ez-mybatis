package org.rdlinux.ezmybatis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class DcDTO<Dt> {
    private List<Dt> data;
    private int count;

    public DcDTO(int count, List<Dt> data) {
        this.count = count;
        this.data = data;
    }
}
