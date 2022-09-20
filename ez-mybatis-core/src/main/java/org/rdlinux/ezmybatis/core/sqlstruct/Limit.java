package org.rdlinux.ezmybatis.core.sqlstruct;

import org.rdlinux.ezmybatis.core.EzQuery;

public class Limit implements SqlPart {
    private EzQuery<?> query;
    private int skip;
    private int size;

    public Limit(EzQuery<?> query, int skip, int size) {
        this.query = query;
        this.skip = skip;
        this.size = size;
    }

    public Limit(int skip, int size) {
        this.skip = skip;
        this.size = size;
    }

    public Limit() {
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

    public EzQuery<?> getQuery() {
        return this.query;
    }

    public void setQuery(EzQuery<?> query) {
        this.query = query;
    }
}
