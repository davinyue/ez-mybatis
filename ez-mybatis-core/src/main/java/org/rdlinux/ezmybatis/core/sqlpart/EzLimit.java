package org.rdlinux.ezmybatis.core.sqlpart;

public class EzLimit {
    private int skip;
    private int size;

    public EzLimit(int skip, int size) {
        this.skip = skip;
        this.size = size;
    }

    public EzLimit() {
        this.skip = 0;
        this.size = 20;
    }

    public int getSkip() {
        return this.skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
