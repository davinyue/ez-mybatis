package org.rdlinux.ezmybatis.dto;

import java.util.List;

public class DcDTO<Dt> {
    private List<Dt> data;
    private int count;

    public DcDTO(int count, List<Dt> data) {
        this.count = count;
        this.data = data;
    }

    public List<Dt> getData() {
        return this.data;
    }

    public DcDTO<Dt> setData(List<Dt> data) {
        this.data = data;
        return this;
    }

    public int getCount() {
        return this.count;
    }

    public DcDTO<Dt> setCount(int count) {
        this.count = count;
        return this;
    }
}
